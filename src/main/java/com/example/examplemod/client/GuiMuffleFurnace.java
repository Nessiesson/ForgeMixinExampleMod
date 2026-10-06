package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.container.ContainerMuffleFurnace;
import com.example.examplemod.tileentity.TileEntityMuffleFurnace;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiMuffleFurnace extends GuiContainer
{
    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(
                    ExampleMod.MODID,
                    "textures/gui/mufflefurnace/muffle_furnace.png"
            );

    // Ванильная текстура печи Minecraft 1.12.2.
    // Из неё берём только огонь и стрелку.
    private static final ResourceLocation FURNACE_TEXTURE =
            new ResourceLocation(
                    "minecraft",
                    "textures/gui/container/furnace.png"
            );

    private final ContainerMuffleFurnace container;

    public GuiMuffleFurnace(
            InventoryPlayer playerInventory,
            TileEntityMuffleFurnace furnace)
    {
        super(new ContainerMuffleFurnace(
                playerInventory,
                furnace
        ));

        this.container =
                (ContainerMuffleFurnace) this.inventorySlots;

        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(
            float partialTicks,
            int mouseX,
            int mouseY)
    {
        GlStateManager.color(
                1.0F,
                1.0F,
                1.0F,
                1.0F
        );

        int x = this.guiLeft;
        int y = this.guiTop;

        // =====================================
        // ФОН ТВОЕГО GUI
        // =====================================

        mc.getTextureManager().bindTexture(GUI_TEXTURE);

        drawTexturedModalRect(
                x,
                y,
                0,
                0,
                176,
                166
        );

        // =====================================
        // АНИМАЦИЯ
        // =====================================

        mc.getTextureManager().bindTexture(FURNACE_TEXTURE);

        // Огонь
        int fireHeight = getBurnLeftScaled(13);

        if (fireHeight > 0)
        {
            drawTexturedModalRect(
                    x + 57,
                    y + 38 + 12 - fireHeight,
                    176,
                    12 - fireHeight,
                    14,
                    fireHeight + 1
            );
        }

        // Стрелка
        int progress = getCookProgressScaled(24);

        if (progress > 0)
        {
            drawTexturedModalRect(
                    x + 78,
                    y + 34,
                    176,
                    14,
                    progress + 1,
                    16
            );
        }
    }

    private int getCookProgressScaled(int pixels)
    {
        int cookTime = container.getCookTime();
        int totalCookTime = container.getTotalCookTime();

        if (totalCookTime <= 0)
        {
            return 0;
        }

        return cookTime * pixels / totalCookTime;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int burnTime = container.getBurnTime();
        int maxBurnTime = container.getCurrentItemBurnTime();

        if (maxBurnTime <= 0)
        {
            maxBurnTime = 200;
        }

        return burnTime * pixels / maxBurnTime;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(
            int mouseX,
            int mouseY)
    {
        fontRenderer.drawString(
                "Муфельная печь",
                8,
                6,
                0x404040
        );

        fontRenderer.drawString(
                "Инвентарь",
                8,
                72,
                0x404040
        );
    }
}