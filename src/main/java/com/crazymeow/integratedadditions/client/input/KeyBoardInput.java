package com.crazymeow.integratedadditions.client.input;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import com.crazymeow.integratedadditions.imixin.IMixinContainerScreenScrolling;
import com.crazymeow.integratedadditions.network.packet.ClearVariablePacket;
import com.crazymeow.integratedadditions.network.packet.CopyVariablePacket;
import com.crazymeow.integratedadditions.network.packet.OpenProgrammerGuiPacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.StringHelpers;
import org.cyclops.cyclopscore.item.IInformationProvider;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.core.item.AspectVariableFacade;
import org.cyclops.integrateddynamics.core.item.ItemPart;
import org.cyclops.integrateddynamics.core.item.OperatorVariableFacade;
import org.cyclops.integrateddynamics.core.item.VariableFacadeBase;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamics.item.ItemVariable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBoardInput {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    private static final PacketHandler PACKET_HANDLER = IntegratedAdditions._instance.getPacketHandler();

    private static final String CATEGORY = "key.category." + IntegratedAdditions.MODID;
    private static final KeyModifier KEY_MODIFIER = KeyModifier.ALT;

    public static final KeyBinding CLEAR_KEY = new KeyBinding("key.integratedadditions.clear", KeyConflictContext.GUI, KEY_MODIFIER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_X, CATEGORY);
    public static final KeyBinding SET_KEY = new KeyBinding("key.integratedadditions.set", KeyConflictContext.GUI, KEY_MODIFIER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_S, CATEGORY);
    public static final KeyBinding COPY_KEY = new KeyBinding("key.integratedadditions.copy", KeyConflictContext.GUI, KEY_MODIFIER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY);
    //    public static final KeyBinding MODIFY_KEY = new KeyBinding("key.modify", KeyConflictContext.GUI, KEY_MODIFIER, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY);
    public static final KeyBinding SHOW_KEY = new KeyBinding("key.integratedadditions.show", KeyConflictContext.GUI, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, CATEGORY);

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        //emmm 因为 IntegratedDynamics 特殊GUI机制的原因不触发
//         if (CLEAR_KEY.isPressed()) { System.out.println("CLEAR_KEY.isPressed"); }

        if (event.getAction() != GLFW.GLFW_RELEASE)
            return;
        Screen screen = MINECRAFT.currentScreen;
        if (screen instanceof ContainerScreen) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>) screen;
            Slot slot = containerScreen.getSlotUnderMouse();
            if (slot != null && slot.inventory instanceof PlayerInventory) {
                ItemStack stack = slot.getStack();
                int slotIndex = slot.getSlotIndex();
                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    if (item instanceof ItemVariable) {
                        if (keyRelease(event, CLEAR_KEY)) {
                            PACKET_HANDLER.sendToServer(new ClearVariablePacket(slotIndex));
                        } else if (keyRelease(event, COPY_KEY)) {
                            PACKET_HANDLER.sendToServer(new CopyVariablePacket(slotIndex));
                        } else if (keyRelease(event, SET_KEY)) {
                            int index = getPortableLogicProgrammerIndex();
                            if (index >= 0) {
                                if (MINECRAFT.player.openContainer instanceof ContainerLogicProgrammerPortable) {
                                    IMixinContainerScreenScrolling mixinContainerScreenScrolling = ((IMixinContainerScreenScrolling) ((ContainerLogicProgrammerPortable) MINECRAFT.player.openContainer).getGui());
                                    mixinContainerScreenScrolling.setSearchFieldText("");
                                    mixinContainerScreenScrolling.setSearchFieldTextFocused2(false);
                                }
                                PACKET_HANDLER.sendToServer(new OpenProgrammerGuiPacket(slotIndex, index));
                            }
                        }
//                        else if (keyRelease(event, MODIFY_KEY)) {
//                            int index = getPortableLogicProgrammerIndex();
//                            if (index >= 0) {
//                                PACKET_HANDLER.sendToServer(new OpenProgrammerGuiPacket(slotIndex, index));
//                            }
//                        }
                    } else if (item instanceof ItemPart) {
                        if (keyRelease(event, CLEAR_KEY)) {
                            PACKET_HANDLER.sendToServer(new ClearVariablePacket(slotIndex));
                        }
                    }
                }

            }
        }
    }

    private static int getPortableLogicProgrammerIndex() {
        PlayerInventory inventory = MINECRAFT.player.inventory;
        int size = inventory.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == RegistryEntries.ITEM_PORTABLE_LOGIC_PROGRAMMER) {
                return i;
            }
        }
        return -1;
    }

    private static boolean keyRelease(InputEvent.KeyInputEvent event, KeyBinding keyBinding) {
        int modifiers = event.getModifiers();
        boolean keyBindingModifiers;
        switch (keyBinding.getKeyModifier()) {
            case ALT:
                keyBindingModifiers = modifiers == GLFW.GLFW_MOD_ALT;
                break;
            case SHIFT:
                keyBindingModifiers = modifiers == GLFW.GLFW_MOD_SHIFT;
                break;
            case CONTROL:
                keyBindingModifiers = modifiers == GLFW.GLFW_MOD_CONTROL;
                break;
            default:
                keyBindingModifiers = modifiers == 0;
                break;
        }
        return keyBindingModifiers && event.getKey() == keyBinding.getKey().getKeyCode();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().isEmpty())
            return;
        if (MinecraftHelpers.isShifted()) {
            Item item = event.getItemStack().getItem();
            List<ITextComponent> lores = new ArrayList<ITextComponent>();
            if (item instanceof ItemPart) {
                lores.add(new TranslationTextComponent("item.integratedadditions.tooltip.clear.info", CLEAR_KEY.func_238171_j_()));
            } else if (item instanceof ItemVariable) {
                lores.add(new TranslationTextComponent("item.integratedadditions.tooltip.clear.info", CLEAR_KEY.func_238171_j_()));
                lores.add(new TranslationTextComponent("item.integratedadditions.tooltip.copy.info", COPY_KEY.func_238171_j_()));
                lores.add(new TranslationTextComponent("item.integratedadditions.tooltip.set.info", SET_KEY.func_238171_j_(), new TranslationTextComponent("item.integrateddynamics.portable_logic_programmer")));
                //				lores.add(new TranslationTextComponent("item.tooltip.modify.info", MODIFY_KEY.func_238171_j_(), new TranslationTextComponent("item.integrateddynamics.portable_logic_programmer")));
                lores.add(new TranslationTextComponent("item.integratedadditions.tooltip.show.info", SHOW_KEY.func_238171_j_()));
            } else {
                return;
            }
            for (ITextComponent lore : lores) {
                event.getToolTip().addAll(StringHelpers.splitLines(lore.getString(), L10NHelpers.MAX_TOOLTIP_LINE_LENGTH, IInformationProvider.INFO_PREFIX).stream().map(StringTextComponent::new).collect(Collectors.toList()));
            }
        }
    }


    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Pre event) {
        if (!SHOW_KEY.isInvalid() && InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), SHOW_KEY.getKey().getKeyCode())) {
            Screen screen = MINECRAFT.currentScreen;
            if (screen instanceof ContainerScreen) {
                ContainerScreen<?> containerScreen = (ContainerScreen<?>) screen;
                Slot slot = containerScreen.getSlotUnderMouse();
                if (slot != null) {
                    ItemStack itemStack = event.getStack();
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        if (item instanceof ItemVariable) {
                            ItemVariable itemVariable = (ItemVariable) itemStack.getItem();
                            VariableFacadeBase variableFacadeBase = (VariableFacadeBase) itemVariable.getVariableFacade(itemStack);
                            if (variableFacadeBase instanceof OperatorVariableFacade) {
                                OperatorVariableFacade variableFacade = (OperatorVariableFacade) variableFacadeBase;
                                event.setCanceled(true);
                                drawVariableSlots(containerScreen, event.getMatrixStack(), variableFacade.getVariableIds());
                            } else if (variableFacadeBase instanceof AspectVariableFacade) {
                                AspectVariableFacade variableFacade = (AspectVariableFacade) variableFacadeBase;
                                event.setCanceled(true);
                                drawItemPartSlot(containerScreen, event.getMatrixStack(), variableFacade.getPartId());
                            }
                        }
                    }
                }
            }
        }
    }

    private static void drawVariableSlots(ContainerScreen containerScreen, MatrixStack matrixStack, int[] ids) {
        int left = containerScreen.getGuiLeft();
        int top = containerScreen.getGuiTop();
        List<Slot> slots = MINECRAFT.player.openContainer.inventorySlots;
        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);
            ItemStack itemStack = slot.getStack();
            if (!itemStack.isEmpty()) {
                Item item = itemStack.getItem();
                if (item instanceof ItemVariable) {
                    ItemVariable itemVariable = (ItemVariable) item;
                    IVariableFacade iVariableFacade = itemVariable.getVariableFacade(itemStack);
                    for (int id : ids) {
                        if (iVariableFacade.getId() == id) {
                            int xPos = slot.xPos + left;
                            int yPos = slot.yPos + top;
                            drawRect(matrixStack.getLast().getMatrix(), containerScreen.getBlitOffset(), xPos, yPos, xPos + 16, yPos + 16);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void drawItemPartSlot(ContainerScreen containerScreen, MatrixStack matrixStack, int id) {
        int left = containerScreen.getGuiLeft();
        int top = containerScreen.getGuiTop();
        List<Slot> slots = MINECRAFT.player.openContainer.inventorySlots;
        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);
            ItemStack itemStack = slot.getStack();
            if (!itemStack.isEmpty()) {
                Item item = itemStack.getItem();
                if (item instanceof ItemPart) {
                    if (itemStack.getTag() != null && itemStack.getTag().contains("id", Constants.NBT.TAG_INT)) {
                        int tempId = itemStack.getTag().getInt("id");
                        if (tempId == id) {
                            int xPos = slot.xPos + left;
                            int yPos = slot.yPos + top;
                            drawRect(matrixStack.getLast().getMatrix(), containerScreen.getBlitOffset(), xPos, yPos, xPos + 16, yPos + 16);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void drawRect(Matrix4f matrix, int blitOffset, int left, int top, int right, int bottom) {
        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(matrix, right, top, blitOffset).color(1, 0, 0, 0.5f).endVertex();
        buffer.pos(matrix, left, top, blitOffset).color(1, 0, 0, 0.5f).endVertex();
        buffer.pos(matrix, left, bottom, blitOffset).color(1, 0, 0, 0.5f).endVertex();
        buffer.pos(matrix, right, bottom, blitOffset).color(1, 0, 0, 0.5f).endVertex();
        tessellator.draw();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }
}