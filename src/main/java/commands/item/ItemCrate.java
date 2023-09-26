package main.java.commands.item;

import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import daybreak.abilitywar.utils.base.math.NumberUtil;
import daybreak.abilitywar.utils.library.MaterialX;
import main.java.util.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemCrate extends Command {
    @Override
    protected boolean onCommand(CommandSender sender, String command, String @NotNull [] args) {
        if (sender instanceof Player) {
           createItem(sender, args);
           return true;
        }
        Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        return false;
    }

    private void createItem(CommandSender sender, String @NotNull [] args) {
        Player p = (Player) sender;
        if (args.length < 7) {
            Messager.sendErrorMessage(sender, "명령어 구문이 너무 짧습니다.");
            return;
        }
        Material material = getMaterial(sender, args[0].toUpperCase());
        if (material == null) {
            return;
        }
        if (!NumberUtil.isInt(args[1])) {
            Messager.sendErrorMessage(sender, "올바르지 않은 숫자입니다: " + args[1]);
            return;
        }
        int damage = Integer.parseInt(args[1]);
        String name = ChatColor.translateAlternateColorCodes('&', args[2].replace("\\", " "));
        List<String> lore = getLore(args[3].replace("\\", " "));
        if (lore.isEmpty()) {
            return;
        }
        Map<Enchantment, Integer> enchant = null;
        if (!args[4].equals("null")) {
            enchant = getEnchant(sender, args[4]);
        }
        if (enchant != null && enchant.isEmpty()) {
            return;
        }
        if (!NumberUtil.isInt(args[5])) {
            Messager.sendErrorMessage(sender, "올바르지 않은 아이템 갯수입니다: " + args[5]);
            return;
        }
        int amount = Integer.parseInt(args[5]);
        if (!args[6].equalsIgnoreCase("true") && !args[6].equalsIgnoreCase("false")) {
            Messager.sendErrorMessage(sender, "올바르지 않은 Boolean입니다: " + args[6] + ", true 또는 false를 사용하세요.");
            return;
        }
        boolean isShiny = Boolean.parseBoolean(args[6]);
        ItemStack itemStack = ItemFactory.createItem(material, damage, name, lore, enchant, amount, isShiny);
        p.getInventory().addItem(itemStack);
        sender.sendMessage("§a아이템을 성공적으로 지급했습니다.");
    }

    private @Nullable Material getMaterial(CommandSender sender, String material) {
        try {
            return Material.valueOf(material);
        } catch (IllegalArgumentException e) {
            try {
                return MaterialX.valueOf(material).getMaterial();
            } catch (IllegalArgumentException e1) {
                Messager.sendErrorMessage(sender, "알 수 없는 아이템 종류입니다: " + material);
                return null;
            }
        }
    }

    private @NotNull List<String> getLore(@NotNull String lore) {
        if (lore.equals("null")) {
            return Collections.emptyList();
        } else {
            String[] loreSplit = lore.split("//");
            List<String> finalLore = new ArrayList<>();
            for (String s : loreSplit) {
                finalLore.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            return finalLore;
        }
    }

    private @NotNull Map<Enchantment, Integer> getEnchant(CommandSender sender, @NotNull String enchantment) {
        String[] enchantmentSplit = enchantment.split("//");
        Map<Enchantment, Integer> enchant = new HashMap<>();
        for (String s : enchantmentSplit) {
            String[] finalSplit = s.split("/");
            if (finalSplit.length != 2) {
                Messager.sendErrorMessage(sender, "인챈트 작성 방식이 올바르지 않습니다: 인챈트1이름/인챈트1레벨//인챈트2이름/인챈트2레벨//... 형식으로 작성하세요.");
                return new HashMap<>();
            }
            if (!NumberUtil.isInt(finalSplit[1])) {
                Messager.sendErrorMessage(sender, "인챈트 레벨이 숫자가 아닙니다: 인챈트1이름/인챈트1레벨//인챈트2이름/인챈트2레벨//... 형식으로 작성하세요.");
                return new HashMap<>();
            }
            int level = Integer.parseInt(finalSplit[1]);
            Enchantment enchant1 = Enchantment.getByName(finalSplit[0]);
            if (enchant1 == null) {
                Messager.sendErrorMessage(sender, "알 수 없는 인챈트입니다: " + finalSplit[0] + ", 인챈트1이름/인챈트1레벨//인챈트2이름/인챈트2레벨//... 형식으로 작성하세요.");
                return new HashMap<>();
            }
            enchant.put(enchant1, level);
        }
        return enchant;
    }
}
