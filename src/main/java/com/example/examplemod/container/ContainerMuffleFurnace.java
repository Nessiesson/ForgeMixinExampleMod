package com.example.examplemod.container;

import com.example.examplemod.tileentity.TileEntityMuffleFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMuffleFurnace extends Container
{
    private final TileEntityMuffleFurnace furnace;

    private int lastBurnTime;
    private int lastCurrentItemBurnTime;
    private int lastCookTime;

    public ContainerMuffleFurnace(
            InventoryPlayer playerInventory,
            TileEntityMuffleFurnace furnace)
    {
        this.furnace = furnace;

        // Сырьё
        addSlotToContainer(new Slot(furnace, 0, 45, 20));

// Уголь
        addSlotToContainer(new Slot(furnace, 1, 67, 20));

// Топливо
        addSlotToContainer(new Slot(furnace, 2, 56, 53));

// Выход
        addSlotToContainer(new Slot(furnace, 3, 116, 35)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }

            @Override
            public ItemStack decrStackSize(int amount)
            {
                if (!getHasStack())
                {
                    return ItemStack.EMPTY;
                }

                return super.decrStackSize(amount);
            }

            @Override
            public ItemStack onTake(
                    EntityPlayer player,
                    ItemStack stack)
            {
                ItemStack result = super.onTake(player, stack);
                furnace.markDirty();
                return result;
            }
        });

        // Инвентарь игрока
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                addSlotToContainer(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }

        // Хотбар
        for (int col = 0; col < 9; col++)
        {
            addSlotToContainer(new Slot(
                    playerInventory,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);

        listener.sendWindowProperty(
                this, 0, furnace.getBurnTime());

        listener.sendWindowProperty(
                this, 1, furnace.getCurrentItemBurnTime());

        listener.sendWindowProperty(
                this, 2, furnace.getCookTime());

        listener.sendWindowProperty(
                this, 3, furnace.getTotalCookTime());
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (IContainerListener listener : listeners)
        {
            if (lastBurnTime != furnace.getBurnTime())
            {
                listener.sendWindowProperty(
                        this, 0, furnace.getBurnTime());
            }

            if (lastCurrentItemBurnTime !=
                    furnace.getCurrentItemBurnTime())
            {
                listener.sendWindowProperty(
                        this, 1, furnace.getCurrentItemBurnTime());
            }

            if (lastCookTime != furnace.getCookTime())
            {
                listener.sendWindowProperty(
                        this, 2, furnace.getCookTime());
            }

            listener.sendWindowProperty(
                    this, 3, furnace.getTotalCookTime());
        }

        lastBurnTime = furnace.getBurnTime();
        lastCurrentItemBurnTime =
                furnace.getCurrentItemBurnTime();
        lastCookTime =
                furnace.getCookTime();
    }

    @Override
    public void updateProgressBar(int id, int data)
    {
        furnace.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return furnace.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(
            EntityPlayer player,
            int index)
    {
        ItemStack result = ItemStack.EMPTY;

        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();

            result = stack.copy();

            if (index < 4)
            {
                if (!mergeItemStack(stack, 4, 40, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (!mergeItemStack(stack, 0, 3, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return result;
    }
    public int getCookTime()
    {
        return furnace.getCookTime();
    }

    public int getTotalCookTime()
    {
        return furnace.getTotalCookTime();
    }

    public int getBurnTime()
    {
        return furnace.getBurnTime();
    }

    public int getCurrentItemBurnTime()
    {
        return furnace.getCurrentItemBurnTime();
    }
}