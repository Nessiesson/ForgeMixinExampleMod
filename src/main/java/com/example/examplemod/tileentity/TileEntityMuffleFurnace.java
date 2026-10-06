package com.example.examplemod.tileentity;

import com.example.examplemod.block.BlockMuffleFurnace;
import com.example.examplemod.fluid.IFluidReceiver;
import com.example.examplemod.fluid.ModFluidTank;
import com.example.examplemod.fluid.ModFluids;
import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.recipe.MuffleFurnaceRecipe;
import com.example.examplemod.recipe.MuffleFurnaceRecipes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityMuffleFurnace
        extends TileEntity
        implements IInventory, ITickable, IFluidReceiver
{
    private final NonNullList<ItemStack> inventory =
            NonNullList.withSize(4, ItemStack.EMPTY);

    private int burnTime = 0;
    private int currentItemBurnTime = 0;
    private int cookTime = 0;

    /*
     * Additive fluid tank. Existing item slots remain unchanged.
     * This tank is a Forge IFluidHandler and therefore works with the
     * fluid network, buckets and compatible external pipes.
     */
    private final ModFluidTank processWaterTank =
            new ModFluidTank(
                    this,
                    4000,
                    ModFluids.PROCESS_WATER.getForgeFluid()
            );

    private final int totalCookTime = 200;
    @Override
    public void update()
    {
        if (world == null || world.isRemote)
        {
            return;
        }

        boolean dirty = false;

        MuffleFurnaceRecipe recipe = getCurrentRecipe();

        /*
         * Если рецепта нет — плавить нечего.
         */
        if (recipe == null)
        {
            if (cookTime != 0)
            {
                cookTime = 0;
                dirty = true;
            }

            /*
             * Состояние огня
             */
            IBlockState state = world.getBlockState(pos);

            if (state.getBlock() == ModBlocks.MUFFLE_FURNACE)
            {
                if (state.getValue(BlockMuffleFurnace.LIT))
                {
                    world.setBlockState(
                            pos,
                            state.withProperty(
                                    BlockMuffleFurnace.LIT,
                                    false
                            ),
                            3
                    );
                }
            }

            if (dirty)
            {
                markDirty();
            }

            return;
        }

        /*
         * Если топлива нет — берём следующее.
         */
        if (burnTime <= 0)
        {
            ItemStack fuel = inventory.get(2);

            int fuelTime = getFuelBurnTime(fuel);

            if (fuelTime > 0)
            {
                burnTime = fuelTime;
                currentItemBurnTime = fuelTime;

                fuel.shrink(1);

                if (fuel.isEmpty())
                {
                    inventory.set(2, ItemStack.EMPTY);
                }

                dirty = true;
            }
        }

        /*
         * Плавка.
         */
        if (burnTime > 0)
        {
            burnTime--;
            cookTime++;

            dirty = true;

            if (cookTime >= recipe.getCookTime())
            {
                cookTime = 0;

                smeltItem();

                dirty = true;
            }
        }
        else
        {
            /*
             * Топлива нет — прогресс сбрасываем.
             */
            if (cookTime != 0)
            {
                cookTime = 0;
                dirty = true;
            }
        }

        /*
         * Включение/выключение состояния lit.
         */
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == ModBlocks.MUFFLE_FURNACE)
        {
            boolean lit = burnTime > 0;

            if (state.getValue(BlockMuffleFurnace.LIT) != lit)
            {
                world.setBlockState(
                        pos,
                        state.withProperty(
                                BlockMuffleFurnace.LIT,
                                lit
                        ),
                        3
                );
            }
        }

        if (dirty)
        {
            markDirty();
        }
    }

    /*
     * Возвращает время горения топлива.
     */
    private int getFuelBurnTime(ItemStack fuel)
    {
        if (fuel.isEmpty())
        {
            return 0;
        }

        /*
         * Обычный уголь — 1600 тиков.
         */
        if (fuel.getItem() == net.minecraft.init.Items.COAL
                && fuel.getMetadata() == 0)
        {
            return 1600;
        }

        /*
         * Остальное топливо через Forge.
         */
        return ForgeEventFactory.getItemBurnTime(fuel);
    }

    /*
     * Ищем рецепт для предметов
     * в слотах 0 и 1.
     */
    private MuffleFurnaceRecipe getCurrentRecipe()
    {
        ItemStack input = inventory.get(0);
        ItemStack ingredient = inventory.get(1);

        return MuffleFurnaceRecipes.getRecipe(
                input,
                ingredient
        );
    }

    /*
     * Проверяет, может ли печь сейчас плавить.
     */
    private boolean canSmelt()
    {
        ItemStack input = inventory.get(0);
        ItemStack ingredient = inventory.get(1);
        ItemStack output = inventory.get(3);

        MuffleFurnaceRecipe recipe =
                MuffleFurnaceRecipes.getRecipe(
                        input,
                        ingredient
                );

        /*
         * Подходящего рецепта нет.
         */
        if (recipe == null)
        {
            return false;
        }

        ItemStack result = recipe.getOutput();

        /*
         * Выход пуст.
         */
        if (output.isEmpty())
        {
            return true;
        }

        /*
         * В выходе должен быть тот же предмет.
         */
        if (!ItemStack.areItemsEqual(output, result))
        {
            return false;
        }

        /*
         * Проверяем вместимость.
         */
        return output.getCount() + result.getCount() <= 64;
    }

    /*
     * Выполняет текущий рецепт.
     */
    private void smeltItem()
    {
        if (!canSmelt())
        {
            return;
        }

        ItemStack input = inventory.get(0);
        ItemStack ingredient = inventory.get(1);
        ItemStack output = inventory.get(3);

        MuffleFurnaceRecipe recipe =
                MuffleFurnaceRecipes.getRecipe(
                        input,
                        ingredient
                );

        if (recipe == null)
        {
            return;
        }

        ItemStack recipeInput = recipe.getInput();
        ItemStack recipeIngredient = recipe.getIngredient();
        ItemStack result = recipe.getOutput();

        /*
         * Забираем сырьё.
         */
        input.shrink(recipeInput.getCount());
        ingredient.shrink(recipeIngredient.getCount());

        /*
         * Кладём результат.
         */
        if (output.isEmpty())
        {
            inventory.set(3, result);
        }
        else
        {
            output.grow(result.getCount());
        }

        markDirty();
    }

    @Override
    public IFluidHandler getFluidReceiver()
    {
        return processWaterTank;
    }

    @Override
    public int getFluidInputRate()
    {
        return 200;
    }

    public ModFluidTank getProcessWaterTank()
    {
        return processWaterTank;
    }

    @Override
    public boolean hasCapability(
            Capability<?> capability,
            net.minecraft.util.EnumFacing facing)
    {
        if (capability ==
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(
            Capability<T> capability,
            net.minecraft.util.EnumFacing facing)
    {
        if (capability ==
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler
                    .FLUID_HANDLER_CAPABILITY
                    .cast(processWaterTank);
        }

        return super.getCapability(capability, facing);
    }

    public boolean isBurning()
    {
        return burnTime > 0;
    }

    public int getBurnTime()
    {
        return burnTime;
    }

    public int getCurrentItemBurnTime()
    {
        return currentItemBurnTime;
    }

    public int getCookTime()
    {
        return cookTime;
    }

    public int getTotalCookTime()
    {
        MuffleFurnaceRecipe recipe = getCurrentRecipe();

        if (recipe != null)
        {
            return recipe.getCookTime();
        }

        return totalCookTime;
    }

    @Override
    public int getSizeInventory()
    {
        return 4;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : inventory)
        {
            if (!stack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(
            int index,
            int count)
    {
        return ItemStackHelper.getAndSplit(
                inventory,
                index,
                count
        );
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(
                inventory,
                index
        );
    }

    @Override
    public void setInventorySlotContents(
            int index,
            ItemStack stack)
    {
        inventory.set(index, stack);

        if (stack.getCount() > 64)
        {
            stack.setCount(64);
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return world.getTileEntity(pos) == this
                && player.getDistanceSq(pos) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    /*
     * Проверяет, разрешён ли предмет
     * для конкретного слота.
     */
    @Override
    public boolean isItemValidForSlot(
            int index,
            ItemStack stack)
    {
        /*
         * Слот 0 — сырьё.
         */
        if (index == 0)
        {
            for (MuffleFurnaceRecipe recipe :
                    MuffleFurnaceRecipes.getRecipes())
            {
                ItemStack input =
                        recipe.getInput();

                if (stack.getItem() == input.getItem()
                        && stack.getMetadata()
                        == input.getMetadata())
                {
                    return true;
                }
            }

            return false;
        }

        /*
         * Слот 1 — ингредиент.
         */
        if (index == 1)
        {
            for (MuffleFurnaceRecipe recipe :
                    MuffleFurnaceRecipes.getRecipes())
            {
                ItemStack ingredient =
                        recipe.getIngredient();

                if (stack.getItem()
                        == ingredient.getItem()
                        && stack.getMetadata()
                        == ingredient.getMetadata())
                {
                    return true;
                }
            }

            return false;
        }

        /*
         * Слот 2 — любое нормальное топливо.
         */
        if (index == 2)
        {
            return getFuelBurnTime(stack) > 0;
        }

        /*
         * Слот 3 — только выход.
         */
        return false;
    }

    @Override
    public String getName()
    {
        return "container.examplemod.muffle_furnace";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation(getName());
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return burnTime;

            case 1:
                return currentItemBurnTime;

            case 2:
                return cookTime;

            case 3:
                return getTotalCookTime();

            default:
                return 0;
        }
    }

    @Override
    public void setField(
            int id,
            int value)
    {
        switch (id)
        {
            case 0:
                burnTime = value;
                break;

            case 1:
                currentItemBurnTime = value;
                break;

            case 2:
                cookTime = value;
                break;

            case 3:
                /*
                 * totalCookTime теперь берётся
                 * из рецепта, поэтому здесь
                 * ничего менять не нужно.
                 */
                break;

            default:
                break;
        }
    }

    @Override
    public int getFieldCount()
    {
        return 4;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < inventory.size(); i++)
        {
            inventory.set(
                    i,
                    ItemStack.EMPTY
            );
        }
    }

    @Override
    public NBTTagCompound writeToNBT(
            NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        processWaterTank.writeToNBTWithPressure(
                compound
        );

        ItemStackHelper.saveAllItems(
                compound,
                inventory
        );

        compound.setInteger(
                "BurnTime",
                burnTime
        );

        compound.setInteger(
                "CurrentItemBurnTime",
                currentItemBurnTime
        );

        compound.setInteger(
                "CookTime",
                cookTime
        );

        return compound;
    }

    @Override
    public void readFromNBT(
            NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        processWaterTank.readFromNBTWithPressure(
                compound
        );

        ItemStackHelper.loadAllItems(
                compound,
                inventory
        );

        burnTime =
                compound.getInteger(
                        "BurnTime"
                );

        currentItemBurnTime =
                compound.getInteger(
                        "CurrentItemBurnTime"
                );

        cookTime =
                compound.getInteger(
                        "CookTime"
                );
    }

    /*
     * ВАЖНО:
     * при переключении LIT TileEntity
     * не должен пересоздаваться.
     */
    @Override
    public boolean shouldRefresh(
            net.minecraft.world.World world,
            BlockPos pos,
            IBlockState oldState,
            IBlockState newState)
    {
        return oldState.getBlock()
                != newState.getBlock();
    }
}