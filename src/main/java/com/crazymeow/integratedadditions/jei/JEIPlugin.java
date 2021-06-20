package com.crazymeow.integratedadditions.jei;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import com.crazymeow.integratedadditions.network.packet.JEIPluginPacket;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerBase;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(IntegratedAdditions.MODID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(ContainerScreenLogicProgrammerBase.class, new GhostIngredientHandler());
    }

    private static class GhostIngredientHandler<T extends ContainerScreenLogicProgrammerBase> implements IGhostIngredientHandler<T> {
        @Override
        public <I> List<Target<I>> getTargets(T gui, I ingredient, boolean doStart) {
            List<Target<I>> targets = new ArrayList<>();
            if (doStart) {
                if (ingredient instanceof ItemStack) {
                    int size = gui.getContainer().inventorySlots.size();
                    for (int i = 4; i < size; i++) {
                        Slot slot = gui.getContainer().inventorySlots.get(i);
                        if (slot.inventory instanceof SimpleInventory) {
                            targets.add(new IGhostIngredientHandler.Target<I>() {
                                @Override
                                public Rectangle2d getArea() {
                                    return new Rectangle2d(gui.getGuiLeft() + slot.xPos, gui.getGuiTop() + slot.yPos, 16, 16);
                                }

                                @Override
                                public void accept(I ingredient) {
                                    IntegratedAdditions._instance.getPacketHandler().sendToServer(new JEIPluginPacket(slot.getSlotIndex(), (ItemStack) ingredient));
//                                    ContainerLogicProgrammerBase containerLogicProgrammerBase = (ContainerLogicProgrammerBase) Minecraft.getInstance().player.openContainer;
//                                    System.out.println(ForgeRegistries.ITEMS.getKey(((ItemStack) ingredient).getItem()));
//                                    containerLogicProgrammerBase.getTemporaryInputSlots().setInventorySlotContents(slot.getSlotIndex(), (ItemStack) ingredient);
//                                    gui.getContainer().detectAndSendChanges();
//                                    IMixinContainerLogicProgrammerBase containerLogicProgrammerBase = (IMixinContainerLogicProgrammerBase) Minecraft.getInstance().player.openContainer;
//                                    ILogicProgrammerElement temporarySlotsElement = containerLogicProgrammerBase.getTemporarySlotsElement();
//                                    if (temporarySlotsElement instanceof IValueTypeLogicProgrammerElement) {
//                                        IValueTypeLogicProgrammerElement element = (IValueTypeLogicProgrammerElement) temporarySlotsElement;
//                                        IntegratedDynamics._instance.getPacketHandler().sendToServer(new LogicProgrammerSetElementInventory(element.getValueType(), 0, 0));
//                                    }
                                }
                            });
                        }
                    }
                }
            }
            return targets;
        }

        @Override
        public boolean shouldHighlightTargets() {
            return true;
        }

        @Override
        public void onComplete() {
        }
    }
}
