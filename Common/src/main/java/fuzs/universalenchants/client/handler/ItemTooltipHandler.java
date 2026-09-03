package fuzs.universalenchants.client.handler;

import fuzs.universalenchants.handler.ItemCompatHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class ItemTooltipHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        // We make armor enchantments compatible with player and animal armor slots.
        // Attributes therefore are displayed for both slot types, duplicating the information.
        // Here we remove the attribute lines again that do not match the equipment slot for this type of armor.
        if (itemStack.isEnchanted()) {
            Collection<EquipmentSlotGroup> groups = getEquipmentSlotGroupsForRemoval(itemStack);
            for (EquipmentSlotGroup equipmentSlotGroup : groups) {
                boolean removalInProgress = false;
                ListIterator<Component> iterator = tooltipLines.listIterator();
                while (iterator.hasNext()) {
                    Component component = iterator.next();
                    if (component.getContents() instanceof TranslatableContents contents) {
                        if (contents.getKey().equals("item.modifiers." + equipmentSlotGroup.getSerializedName())) {
                            removalInProgress = true;
                            iterator.previous();
                            if (iterator.hasPrevious()) {
                                Component previousComponent = iterator.previous();
                                if (previousComponent.getContents() instanceof PlainTextContents previousContents
                                        && previousContents.text().isEmpty()) {
                                    iterator.remove();
                                }
                            }

                            iterator.next();
                        } else if (!contents.getKey().startsWith("attribute.modifier.") && !contents.getKey()
                                .startsWith("neoforge.modifier.")) {
                            removalInProgress = false;
                        }

                        if (removalInProgress) {
                            iterator.remove();
                        }
                    } else {
                        removalInProgress = false;
                    }
                }
            }
        }
    }

    private static Collection<EquipmentSlotGroup> getEquipmentSlotGroupsForRemoval(ItemStack itemStack) {
        Equipable equippable = Equipable.get(itemStack);
        if (equippable != null && equippable.getEquipmentSlot() == EquipmentSlot.BODY) {
            return ItemCompatHandler.ARMOR_EQUIPMENT_SLOT_GROUPS;
        } else {
            return Collections.singleton(EquipmentSlotGroup.BODY);
        }
    }
}
