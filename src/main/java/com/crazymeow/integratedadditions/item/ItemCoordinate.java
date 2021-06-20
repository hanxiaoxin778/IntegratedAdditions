package com.crazymeow.integratedadditions.item;

import com.shblock.integratedproxy.tileentity.TileAccessProxy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.core.logicprogrammer.LogicProgrammerElementTypes;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeStringLPElement;
import org.cyclops.integrateddynamics.core.persist.world.LabelsWorldStorage;
import org.cyclops.integrateddynamics.item.ItemVariable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemCoordinate extends Item {

    public ItemCoordinate(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        BlockPos position = getPosition(stack);
        if (position != null) {
            tooltip.add(new StringTextComponent("x:" + position.getX() + " y:" + position.getY() + " z:" + position.getZ()));
        }
        if (!ModList.get().isLoaded("integrated_proxy"))
            tooltip.add(new TranslationTextComponent(L10NValues.PART_TOOLTIP_NOASPECTS).mergeStyle(TextFormatting.GOLD));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player.isCrouching()) {
            if (!context.getWorld().isRemote()) {
                if (isLoaded()) {
                    TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
                    if (tileEntity instanceof TileAccessProxy) {
                        BlockPos bindPosition = getPosition(context.getItem());
                        if (bindPosition == null) {
                            player.sendStatusMessage(new TranslationTextComponent("item.integratedadditions.coordinate.error_tip"), true);
                            return ActionResultType.PASS;
                        }
                        TileAccessProxy tileAccessProxy = (TileAccessProxy) tileEntity;
                        if (tryShrunkVariables(player.inventory)) {
                            SimpleInventory simpleInventory = tileAccessProxy.getInventory();
                            if (tileAccessProxy.pos_mode == 0) bindPosition = bindPosition.subtract(context.getPos());
                            int[] bindPos = {bindPosition.getX(), bindPosition.getY(), bindPosition.getZ()};
                            for (int i = 0; i < 3; i++) {
                                ValueTypeStringLPElement valueTypeStringLPElement = (ValueTypeStringLPElement) LogicProgrammerElementTypes.VALUETYPE.getByName(ValueTypes.INTEGER.getUniqueName());
                                ItemStack itemStack = valueTypeStringLPElement.writeElement(player, new ItemStack(RegistryEntries.ITEM_VARIABLE));
                                LabelsWorldStorage.getInstance(IntegratedDynamics._instance).put(RegistryEntries.ITEM_VARIABLE.getVariableFacade(itemStack).getId(), "XYZ".charAt(i) + "");
                                itemStack.getOrCreateTag().putInt("value", bindPos[i]);
//                                ValueTypeInteger.ValueInteger valueInteger = ValueHelpers.parseString(ValueTypes.INTEGER, i + "");
//                                ValueTypes.REGISTRY.setVariableFacade(tag, variableFacadeBase);
//                                IVariableFacadeHandlerRegistry registry = IntegratedDynamics._instance.getRegistryManager().getRegistry(IVariableFacadeHandlerRegistry.class);
//                                registry.writeVariableFacadeItem(true, itemStack, ValueTypes.REGISTRY,
//                                        new ValueTypeLPElementBase.ValueTypeVariableFacadeFactory(getValueType(), getValue()), player, RegistryEntries.BLOCK_LOGIC_PROGRAMMER.getDefaultState());
                                if (!simpleInventory.getStackInSlot(i).isEmpty())
                                    player.addItemStackToInventory(new ItemStack(RegistryEntries.ITEM_VARIABLE));
                                simpleInventory.setInventorySlotContents(i, itemStack);
                            }
                        } else {
                            player.sendStatusMessage(new TranslationTextComponent("item.integratedadditions.coordinate.tip"), true);
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
                setPosition(context.getItem(), context.getPos());
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean tryShrunkVariables(PlayerInventory inventory) {
        int inventorySize = inventory.getSizeInventory();
        IVariableFacade tempVariableFacade;
        int needCount = 3;
        int count = 0;
        boolean exist = false;
        List<ItemStack> dummyVariables = new ArrayList<>();
        for (int j = 0; j < inventorySize; j++) {
            ItemStack element = inventory.getStackInSlot(j);
            if (!(element.getItem() instanceof ItemVariable))
                continue;
            tempVariableFacade = RegistryEntries.ITEM_VARIABLE.getVariableFacade(element);
            if (!tempVariableFacade.isValid()) {
                count += element.getCount();
                dummyVariables.add(element);
                if (count >= needCount) {
                    exist = true;
                    break;
                }
            }
        }
        count = needCount;
        if (exist) {
            for (ItemStack itemStack : dummyVariables) {
                //  -1         2              3
                count = itemStack.getCount() - count;
                if (count >= 0) {
                    itemStack.shrink(needCount);
                    break;
                } else {
                    itemStack.shrink(needCount + count);
                }
                needCount = count = Math.abs(count);
            }
        } else
            return false;
        return true;
    }

    private boolean isLoaded() {
        return ModList.get().isLoaded("integrated_proxy");
    }

    public void setPosition(ItemStack itemStack, BlockPos position) {
        CompoundNBT tag = itemStack.getOrCreateTag();
        tag.putInt("x", position.getX());
        tag.putInt("y", position.getY());
        tag.putInt("z", position.getZ());
    }

    public BlockPos getPosition(ItemStack itemStack) {
        CompoundNBT tag = itemStack.getTag();
        if (tag != null) {
            return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        }
        return null;
    }
}
