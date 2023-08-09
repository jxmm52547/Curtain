package dev.dubhe.curtain.features.player.menu;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.dubhe.curtain.api.menu.CustomMenu;
import dev.dubhe.curtain.api.menu.control.AutoResetButton;
import dev.dubhe.curtain.api.menu.control.Button;
import dev.dubhe.curtain.api.menu.control.RadioList;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import dev.dubhe.curtain.features.player.helpers.EntityPlayerActionPack;
import dev.dubhe.curtain.utils.TranslationHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FakePlayerInventoryMenu extends CustomMenu {

    public final NonNullList<ItemStack> items;
    public final NonNullList<ItemStack> armor;
    public final NonNullList<ItemStack> offhand;
    private final NonNullList<ItemStack> buttons = NonNullList.withSize(13, ItemStack.EMPTY);
    private final List<NonNullList<ItemStack>> compartments;
    private final PlayerEntity player;
    private final EntityPlayerActionPack ap;

    public FakePlayerInventoryMenu(PlayerEntity player) {
        this.player = player;
        this.items = this.player.inventory.items;
        this.armor = this.player.inventory.armor;
        this.offhand = this.player.inventory.offhand;
        this.ap = ((IServerPlayer) this.player).getActionPack();
        this.compartments = ImmutableList.of(this.items, this.armor, this.offhand, this.buttons);
        this.createButton();
        this.ap.setSlot(1);
    }

    @Override
    public int getContainerSize() {
        return this.items.size() + this.armor.size() + this.offhand.size() + this.buttons.size();
    }


    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (itemStack.isEmpty()) {
                continue;
            }
            return false;
        }
        for (ItemStack itemStack : this.armor) {
            if (itemStack.isEmpty()) {
                continue;
            }
            return false;
        }
        for (ItemStack itemStack : this.offhand) {
            if (itemStack.isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public @Nonnull ItemStack getItem(int slot) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        if (pair != null) {
            return pair.getFirst().get(pair.getSecond());
        } else {
            return ItemStack.EMPTY;
        }
    }

    public Pair<NonNullList<ItemStack>, Integer> getItemSlot(int slot) {
        switch (slot) {
            case 0: {
                return new Pair<>(buttons, 0);
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                return new Pair<>(armor, 4 - slot);
            }
            case 5:
            case 6: {
                return new Pair<>(buttons, slot - 4);
            }
            case 7: {
                return new Pair<>(offhand, 0);
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17: {
                return new Pair<>(buttons, slot - 5);
            }
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44: {
                return new Pair<>(items, slot - 9);
            }
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53: {
                return new Pair<>(items, slot - 45);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public @Nonnull ItemStack removeItem(int slot, int amount) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        NonNullList<ItemStack> list = null;
        if (pair != null) {
            list = pair.getFirst();
            slot = pair.getSecond();
        }
        if (list != null && !list.get(slot).isEmpty()) {
            return ItemStackHelper.removeItem(list, slot, amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @Nonnull ItemStack removeItemNoUpdate(int slot) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        NonNullList<ItemStack> list = null;
        if (pair != null) {
            list = pair.getFirst();
            slot = pair.getSecond();
        }
        if (list != null && !list.get(slot).isEmpty()) {
            ItemStack itemStack = list.get(slot);
            list.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack stack) {
        Pair<NonNullList<ItemStack>, Integer> pair = getItemSlot(slot);
        NonNullList<ItemStack> list = null;
        if (pair != null) {
            list = pair.getFirst();
            slot = pair.getSecond();
        }
        if (list != null) {
            list.set(slot, stack);
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return player.isAlive() && !(player.distanceToSqr(player) > 64.0);
    }

    @Override
    public void clearContent() {
        for (List<ItemStack> list : this.compartments) {
            list.clear();
        }
    }

    private void createButton() {
        List<Button> hotBarList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            IFormattableTextComponent hotBarComponent = TranslationHelper.translate(
                    "gac.hotbar",
                    TextFormatting.WHITE,
                    Style.EMPTY.withBold(true).withItalic(false),
                    i + 1
            );
            boolean defaultState = i == 0;
            Button button = new Button(defaultState, i + 1,
                    hotBarComponent,
                    hotBarComponent
            );
            int finalI = i + 1;
            button.addTurnOnFunction(() -> ap.setSlot(finalI));
            this.addButton(i + 9, button);
            hotBarList.add(button);
        }
        this.addButtonList(new RadioList(hotBarList, true));

        Button stopAll = new AutoResetButton("action.stop_all");
        Button attackInterval14 = new Button(false, "action.attack.interval.14");
        Button attackContinuous = new Button(false, "action.attack.continuous");
        Button useContinuous = new Button(false, "action.use.continuous");

        stopAll.addTurnOnFunction(() -> {
            attackInterval14.turnOffWithoutFunction();
            attackContinuous.turnOffWithoutFunction();
            useContinuous.turnOffWithoutFunction();
            ap.stopAll();
        });

        attackInterval14.addTurnOnFunction(() -> {
            ap.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.interval(14));
            attackContinuous.turnOffWithoutFunction();
        });
        attackInterval14.addTurnOffFunction(() -> ap.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.once()));
        attackContinuous.addTurnOnFunction(() -> {
            ap.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.continuous());
            attackInterval14.turnOffWithoutFunction();
        });
        attackContinuous.addTurnOffFunction(() -> ap.start(EntityPlayerActionPack.ActionType.ATTACK, EntityPlayerActionPack.Action.once()));

        useContinuous.addTurnOnFunction(() -> ap.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.continuous()));
        useContinuous.addTurnOffFunction(() -> ap.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.once()));
        this.addButton(0, stopAll);
        this.addButton(5, attackInterval14);
        this.addButton(6, attackContinuous);
        this.addButton(8, useContinuous);
    }
}