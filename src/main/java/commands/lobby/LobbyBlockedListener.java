package main.java.commands.lobby;

import daybreak.abilitywar.AbilityWar;
import daybreak.abilitywar.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class LobbyBlockedListener extends LobbyBlocked implements Listener {
    private boolean lClick = (boolean) config.get(DEFAULTPATH + "LCLICK");
    private boolean rClick = (boolean) config.get(DEFAULTPATH + "RCLICK");
    private boolean dropItem = (boolean) config.get(DEFAULTPATH + "DROP_ITEM");
    private boolean pickupItem = (boolean) config.get(DEFAULTPATH + "PICKUP_ITEM");
    private boolean swapHand = (boolean) config.get(DEFAULTPATH + "SWAP_HAND");
    private boolean damage = (boolean) config.get(DEFAULTPATH + "DAMAGE");
    private boolean eatFood = (boolean) config.get(DEFAULTPATH + "EAT_FOOD");
    private boolean hunger = (boolean) config.get(DEFAULTPATH + "HUNGER");
    private boolean invClick = (boolean) config.get(DEFAULTPATH + "INV_CLICK");
    private boolean voidOption = (boolean) config.get(DEFAULTPATH + "VOID");
    private boolean onlyInLobby = (boolean) config.get(ONLY_IN_LOBBY);

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(INV_TITLE) && e.getRawSlot() < e.getInventory().getSize()) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            boolean changed = true;
            switch (e.getRawSlot()) {
                case 5: {
                    config.set(ONLY_IN_LOBBY, !onlyInLobby);
                    onlyInLobby = !onlyInLobby;
                    break;
                }
                case 18: {
                    config.set(DEFAULTPATH + "LCLICK", !lClick);
                    lClick = !lClick;
                    break;
                }
                case 19: {
                    config.set(DEFAULTPATH + "RCLICK", !rClick);
                    rClick = !rClick;
                    break;
                }
                case 20: {
                    config.set(DEFAULTPATH + "DROP_ITEM", !dropItem);
                    dropItem = !dropItem;
                    break;
                }
                case 21: {
                    config.set(DEFAULTPATH + "PICKUP_ITEM", !pickupItem);
                    pickupItem = !pickupItem;
                    break;
                }
                case 22: {
                    config.set(DEFAULTPATH + "SWAP_HAND", !swapHand);
                    swapHand = !swapHand;
                    break;
                }
                case 23: {
                    config.set(DEFAULTPATH + "DAMAGE", !damage);
                    damage = !damage;
                    break;
                }
                case 24: {
                    config.set(DEFAULTPATH + "EAT_FOOD", !eatFood);
                    eatFood = !eatFood;
                    break;
                }
                case 25: {
                    config.set(DEFAULTPATH + "HUNGER", !hunger);
                    hunger = !hunger;
                    break;
                }
                case 26: {
                    config.set(DEFAULTPATH + "INV_CLICK", !invClick);
                    invClick = !invClick;
                    break;
                }
                case 27: {
                    config.set(DEFAULTPATH + "VOID", !voidOption);
                    voidOption = !voidOption;
                    if (voidOption) {
                        syncVoid();
                    } else {
                        stopSyncVoid();
                    }
                    break;
                }
                default:
                    changed = false;
            }
            if (changed) {
                open(p);
            }
        } else if (invClick && isMeetDefReq((Player) e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(@NotNull PlayerInteractEvent e) {
        if ((lClick || rClick) && isMeetDefReq(e.getPlayer())) {
            if ((e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) && lClick) {
                e.setCancelled(true);
            }
            if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && rClick) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (dropItem && isMeetDefReq(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (swapHand && isMeetDefReq(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(@NotNull EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && damage && isMeetDefReq((Player) e.getEntity())) {
            Player p = (Player) e.getEntity();
            e.setCancelled(true);
            p.setHealth(((Player) e.getEntity()).getHealthScale());
            if (voidOption && e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                upVoid(p);
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if (eatFood && isMeetDefReq(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (hunger && isMeetDefReq((Player) e.getEntity())) {
            e.setFoodLevel(20);
        }
    }

    private boolean isMeetDefReq(Player p) {
        boolean isInLobby;
        boolean onlyInLobbyV = (boolean) config.get(DEFAULTPATH + "onlyInLobby");
        Object lobbyWorld = config.get("lobby");
        if (onlyInLobbyV) {
            if (lobbyWorld != null) {
                isInLobby = p.getWorld().equals(Bukkit.getWorld(lobbyWorld.toString()));
            } else {
                isInLobby = false;
            }
        } else {
            isInLobby = true;
        }
        return !GameManager.isGameRunning() && !p.getGameMode().equals(GameMode.CREATIVE) && isInLobby;
    }

    public static void upVoid(@NotNull Player p) {
        p.setGravity(false);
        Vector upVec = lookAt(p.getLocation(), p.getWorld().getSpawnLocation()).getDirection().multiply(-3).setY(100000D);
        p.setVelocity(upVec);
        Vector moveVec = lookAt(p.getLocation(), p.getWorld().getSpawnLocation()).getDirection().multiply(10).setY(2D);
        Bukkit.getScheduler().scheduleSyncDelayedTask(AbilityWar.getPlugin(), () -> {
            p.setVelocity(moveVec);
            p.setGravity(true);
        }, 12);
    }

    private static @NotNull Location lookAt(Location loc, @NotNull Location lookat) {
        loc = loc.clone();
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        if (dx != 0) {
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        loc.setPitch((float) -Math.atan(dy / dxz));

        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }
}
