package com.crazymeow.integratedadditions.network.packet;

import com.crazymeow.integratedadditions.IntegratedAdditions;
import com.crazymeow.integratedadditions.imixin.IMixinContainerLogicProgrammerBase;
import com.crazymeow.integratedadditions.mixin.MixinScrollingInventoryContainerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.client.gui.component.WidgetScrollBar;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElement;
import org.cyclops.integrateddynamics.api.logicprogrammer.ILogicProgrammerElementType;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.item.OperatorVariableFacade;
import org.cyclops.integrateddynamics.core.item.ValueTypeVariableFacade;
import org.cyclops.integrateddynamics.core.item.VariableFacadeBase;
import org.cyclops.integrateddynamics.core.item.VariableFacadeHandlerRegistry;
import org.cyclops.integrateddynamics.core.logicprogrammer.LogicProgrammerElementTypes;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerPortable;
import org.cyclops.integrateddynamics.item.ItemVariable;

import java.util.List;

public class OpenProgrammerGuiPacket extends PacketCodec {
    @CodecField
    private int slotIndex;
    @CodecField
    private int index;

    @CodecField
    private String typeId;
    @CodecField
    private String elementId;
    @CodecField
    private boolean removed;


    public OpenProgrammerGuiPacket() {

    }

    public OpenProgrammerGuiPacket(int slotIndex, int index) {
        this(slotIndex, index, "", "", false);
    }

    public OpenProgrammerGuiPacket(int slotIndex, int index, String typeId, String elementId, boolean removed) {
        this.slotIndex = slotIndex;
        this.index = index;
        this.typeId = typeId;
        this.elementId = elementId;
        this.removed = removed;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(World world, PlayerEntity player) {
        if (player.openContainer instanceof ContainerLogicProgrammerPortable) {
            ContainerLogicProgrammerPortable container = (ContainerLogicProgrammerPortable) player.openContainer;
            ContainerScreenLogicProgrammerBase gui = container.getGui();
            ILogicProgrammerElementType type = LogicProgrammerElementTypes.REGISTRY.getType(new ResourceLocation(this.typeId));
            gui.handleElementActivation(type.getByName(new ResourceLocation(this.elementId)));
            //ContainerLogicProgrammerBase$setElementInventory做了了不得的操作，导致客户端的玩家背包格子不更新，我咋办嘛？0.0，先强制同步吧....
            if (this.removed) {
//            IntegratedAdditions.clog(Level.DEBUG, Integer.toString(player.inventory.getStackInSlot(this.slotIndex).getCount()));
                player.inventory.removeStackFromSlot(this.slotIndex);
            }
            List<ILogicProgrammerElement> unfilteredItems = ((MixinScrollingInventoryContainerAccessor) container).getUnfilteredItems();
            int pos = -1;
            int unfilteredItemSize = unfilteredItems.size();
            for (int i = 0; i < unfilteredItemSize; i++) {
                ILogicProgrammerElement tempElement = unfilteredItems.get(i);
                ILogicProgrammerElementType tempType = tempElement.getType();
                if (tempType.getName(tempElement).toString().equals(this.elementId)) {
                    pos = i;
                    break;
                }
            }
            container.onScroll(pos);
            WidgetScrollBar scrollbar = gui.getScrollbar();
            float scroll = (float) pos / (unfilteredItemSize - scrollbar.getVisibleRows());
            if (scroll > 1) scroll = 1;
            scrollbar.scrollTo(scroll);
        }

    }

    @Override
    public void actionServer(World world, ServerPlayerEntity player) {
        //为了不破坏作者的游戏设定，只允许在背包拥有便携式编程器的时候才允许使用此功能，编程方块不行。
        if (player.openContainer instanceof ContainerLogicProgrammerPortable) {
            SimpleInventory tempWriteSlot = ((IMixinContainerLogicProgrammerBase) player.openContainer).getWriteSlot();
            ItemStack tempWriteSlotItemStack = tempWriteSlot.getStackInSlot(0);
            if (!tempWriteSlotItemStack.isEmpty()) {
                if (player.inventory.getFirstEmptyStack() >= 0) {
                    player.inventory.addItemStackToInventory(tempWriteSlotItemStack);
                    tempWriteSlot.setInventorySlotContents(0, new ItemStack(Items.AIR));
                } else {
                    return;
                }
//                else {
//                    //如果没有空位则要看当前选择的物品是不是只有1个变量卡，是则更换，不是就退出
//                    ItemStack tempPlayerStackInSlot = player.inventory.getStackInSlot(this.slotIndex);
//                    if (tempPlayerStackInSlot.getCount() == 1) {
//                        addTo = tempWriteSlotItemStack;
//                        tempWriteSlot.setInventorySlotContents(0, new ItemStack(Items.AIR));
//                    } else {
//                        return;
//                    }
//                }
            }
        } else {
            ((ItemGui) RegistryEntries.ITEM_PORTABLE_LOGIC_PROGRAMMER).openGuiForItemIndex(world, player, this.index, Hand.MAIN_HAND);
        }
        ContainerLogicProgrammerPortable container = (ContainerLogicProgrammerPortable) player.openContainer;
        ItemStack srcItemStack = player.inventory.getStackInSlot(this.slotIndex);

        ItemVariable item = (ItemVariable) srcItemStack.getItem();
        VariableFacadeBase variableFacadeBase = (VariableFacadeBase) item.getVariableFacade(srcItemStack);
        if (variableFacadeBase instanceof VariableFacadeHandlerRegistry.DummyVariableFacade) {
            //DummyVariableFacade——空白变量卡（自动放入卡，根据默认Integer类型选择Item）
            this.typeId = LogicProgrammerElementTypes.VALUETYPE.getUniqueName().toString();
            this.elementId = ValueTypes.INTEGER.getUniqueName().toString();
        } else if (variableFacadeBase instanceof ValueTypeVariableFacade) {
            //ValueTypeVariableFacade——类型变量卡（自动放入卡，根据类型选择Item）
            this.typeId = LogicProgrammerElementTypes.VALUETYPE.getUniqueName().toString();
            this.elementId = variableFacadeBase.getOutputType().getUniqueName().toString();
        } else if (variableFacadeBase instanceof OperatorVariableFacade) {
            //OperatorVariableFacade——运算符变量卡（自动放入卡，根据operator类型选择Item）
            this.typeId = LogicProgrammerElementTypes.OPERATOR.getUniqueName().toString();
            this.elementId = ((OperatorVariableFacade) variableFacadeBase).getOperator().getUniqueName().toString();
        } else {
            //AspectVariableFacade——读取器变量卡（不放入卡，不选择Item）
            //ProxyVariableFacade——代理器变量卡（不放入卡，不选择Item）
            return;
        }
        ItemStack itemStack = srcItemStack.copy();
        SimpleInventory writeSlot = ((IMixinContainerLogicProgrammerBase) container).getWriteSlot();
        itemStack.setCount(1);
        writeSlot.setInventorySlotContents(0, itemStack);
        srcItemStack.shrink(1);
//        player.container.inventorySlots.get(this.slotIndex).onSlotChanged();
        IntegratedAdditions._instance.getPacketHandler().sendToPlayer(new OpenProgrammerGuiPacket(this.slotIndex, this.index, this.typeId, this.elementId, srcItemStack.getCount() == 0), player);
    }
}