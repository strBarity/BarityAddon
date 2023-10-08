package main.java.commands.lobby;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.Command;
import daybreak.abilitywar.utils.base.Messager;
import main.java.util.AddonConfig;
import main.java.util.ItemColor;
import main.java.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyBlocked extends Command {
    private enum BlockedActions {
        LCLICK(Material.DIAMOND_PICKAXE, "§c좌클릭", Arrays.asList("§7모든 좌클릭을 금지합니다.", "§7이 행동에는 블럭 파괴가 포함됩니다.")),
        RCLICK(Material.WORKBENCH, "§b우클릭", Arrays.asList("§7모든 우클릭을 금지합니다.", "§7이 행동에는 블럭 설치가 포함됩니다.")),
        DROP_ITEM(Material.DIAMOND, "§e아이템 드랍", Arrays.asList("§7아이템을 바닥에 버리는 행위를 금지합니다.", "§7무단으로 쓰레기를 버리지 못하게 합니다.")),
        PICKUP_ITEM(Material.EMERALD, "§d아이템 줍기", Arrays.asList("§7아이템을 줍는 행위를 금지합니다.", "§7쓰레기가 없으니 줍지도 못하게 합니다.")),
        SWAP_HAND(Material.ARROW, "§a양손 들기", Arrays.asList("§7아이템을 다른 손에 드는 행위를 금지합니다.", "§7플러그인에서 오류가 날 가능성을 배제합니다.")),
        DAMAGE(Material.REDSTONE, "§4피격", Arrays.asList("§7피해를 입는 모든 행위를 금지합니다.", "§e체력이 최대 체력으로 고정되는 설정도 포함됩니다.")),
        EAT_FOOD(Material.COOKIE, "§5음식 섭취", Arrays.asList("§7음식을 먹는 행위를 금지합니다.", "§7장식용 아이템을 먹어서 없애지 못하게 합니다.")),
        HUNGER(Material.ROTTEN_FLESH, "§2허기", Arrays.asList("§7배고픔 게이지가 감소하지 않게 합니다.", "§7기다리다 배고파서 쓰러지는 플레이어를 방지합니다.")),
        INV_CLICK(Material.STICK, "§9인벤토리 클릭", Arrays.asList("§7인벤토리 아이템을 클릭하지 못하게 합니다.", "§e핫바를 설정했다면 이 설정도 키는게 좋습니다.")),
        VOID(Material.BEDROCK, "§8공허", Arrays.asList("§7맵을 탈출해 공허로 무한히 떨어지는걸 방지합니다.", "§7y=0 미만으로 내려갈 시 스폰 약간 반대 방향으로 위로 튀어오른 후", "§7그대로 스폰 쪽으로 날아가게 합니다."));
        private final Material icon;
        private final String name;
        private final List<String> lore;
        BlockedActions(Material icon, String name, List<String> lore) {
            this.icon = icon;
            this.name = name;
            this.lore = new ArrayList<>();
            this.lore.addAll(lore);
        }
    }
    protected final AddonConfig config;
    protected static final String DEFAULTPATH = "blockedActions.";
    protected static final int INV_SIZE = 45;
    protected static final String INV_TITLE = "§8게임 시작 전 금지 행동 설정";
    protected static final String ONLY_IN_LOBBY = DEFAULTPATH + "onlyInLobby";
    private int voidTaskId;
    private final List<BlockedActions> actionsList = Arrays.asList(BlockedActions.LCLICK, BlockedActions.RCLICK, BlockedActions.DROP_ITEM, BlockedActions.PICKUP_ITEM, BlockedActions.SWAP_HAND, BlockedActions.DAMAGE, BlockedActions.EAT_FOOD, BlockedActions.HUNGER, BlockedActions.INV_CLICK, BlockedActions.VOID);
    public LobbyBlocked() {
        super(Condition.OP);
        config = AddonConfig.getConfig("lobbyConfig");
        resetConfig();
    }

    @Override
    protected boolean onCommand(CommandSender sender, String command, String[] args) {
        if (sender instanceof Player) {
            open((Player) sender);
        }
        else {
            Messager.sendErrorMessage(sender, "이 명령어는 플레이어만 사용할 수 있습니다.");
        }
        return true;
    }

    protected void open(Player p) {
        Inventory gui = Bukkit.createInventory(null, INV_SIZE, INV_TITLE);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, ItemFactory.blank(ItemColor.WHITE, false));
        }
        gui.setItem(3, ItemFactory.createItem(Material.SIGN, 0, "§c금지 행동 설정", Arrays.asList("§7게임 시작 전, 플레이어들이 금지할 설정을 설정합니다.", "§c설정된 금지 행동은 능력 배정이 끝난 후 무적 시간 시작 직전까지 적용됩니다."), null, 1, true));
        applyConfig(gui);
        p.openInventory(gui);
    }

    private void resetConfig() {
        for (BlockedActions a : actionsList) {
            if (config.get(DEFAULTPATH + a.name()) == null) {
                config.set(DEFAULTPATH + a.name(), false);
            }
            if (a.equals(BlockedActions.VOID) && (boolean) config.get(DEFAULTPATH + a.name())) {
                syncVoid();
            }
        }
        if (config.get(ONLY_IN_LOBBY) == null) {
            config.set(ONLY_IN_LOBBY, false);
        }
    }

    protected void syncVoid() {
        voidTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AbilityWar.getPlugin(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().getY() < 0) {
                    LobbyBlockedListener.upVoid(p);
                }
            }
        }, 0, 5);
    }

    protected void stopSyncVoid() {
        Bukkit.getScheduler().cancelTask(voidTaskId);
    }

    private void applyConfig(@NotNull Inventory inv) {
        List<String> lobbyLore = new ArrayList<>();
        lobbyLore.add("§7금지 행동이 로비 월드에서만");
        lobbyLore.add("§7작동할 지 설정합니다.");
        Object lobbyName = config.get("lobby");
        if (lobbyName == null) {
            lobbyName = "§c없음";
        }
        lobbyLore.add("§7현재 로비: §a" + lobbyName);
        lobbyLore.add(" ");
        lobbyLore.add("§e현재 설정: " + AddonConfig.boolTranslate((boolean) config.get(ONLY_IN_LOBBY)));
        inv.setItem(5, ItemFactory.createItem(Material.GRASS, 0, "§c로비에서만 금지하기", lobbyLore, null, 1, (boolean) config.get(ONLY_IN_LOBBY)));
        int i = 18;
        for (BlockedActions a : actionsList) {
            boolean actionConfig = (boolean) config.get(DEFAULTPATH + a.name());
            List<String> lore = new ArrayList<>(a.lore);
            lore.add(" ");
            lore.add("§e현재 설정: " + AddonConfig.boolTranslate(actionConfig));
            inv.setItem(i, ItemFactory.createItem(a.icon, 0, a.name, lore, null, 1, actionConfig));
            i++;
        }
    }
}
