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

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals("§8게임 시작 전 금지 행동 설정") && e.getRawSlot() < e.getInventory().getSize()) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            boolean changed = true;
            switch (e.getRawSlot() - 8) {
                case 1: {
                    config.set(DEFAULTPATH + "LCLICK", !lClick);
                    lClick = !lClick;
                    break;
                }
                case 2: {
                    config.set(DEFAULTPATH + "RCLICK", !rClick);
                    rClick = !rClick;
                    break;
                }
                case 3: {
                    config.set(DEFAULTPATH + "DROP_ITEM", !dropItem);
                    dropItem = !dropItem;
                    break;
                }
                case 4: {
                    config.set(DEFAULTPATH + "PICKUP_ITEM", !pickupItem);
                    pickupItem = !pickupItem;
                    break;
                }
                case 5: {
                    config.set(DEFAULTPATH + "SWAP_HAND", !swapHand);
                    swapHand = !swapHand;
                    break;
                }
                case 6: {
                    config.set(DEFAULTPATH + "DAMAGE", !damage);
                    damage = !damage;
                    break;
                }
                case 7: {
                    config.set(DEFAULTPATH + "EAT_FOOD", !eatFood);
                    eatFood = !eatFood;
                    break;
                }
                case 8: {
                    config.set(DEFAULTPATH + "HUNGER", !hunger);
                    hunger = !hunger;
                    break;
                }
                case 9: {
                    config.set(DEFAULTPATH + "INV_CLICK", !invClick);
                    invClick = !invClick;
                    break;
                }
                case 10: {
                    config.set(DEFAULTPATH + "VOID", !voidOption);
                    voidOption = !voidOption;
                    if (voidOption) {
                        syncVoid();
                    }
                    else {
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
        } else if (!GameManager.isGameRunning() && !e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) && invClick) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(@NotNull PlayerInteractEvent e) {
        if (!GameManager.isGameRunning() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
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
        if (!GameManager.isGameRunning() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && dropItem) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (!GameManager.isGameRunning() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && swapHand) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!GameManager.isGameRunning() && damage && e.getEntity() instanceof Player) {
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
        if (!GameManager.isGameRunning() && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && eatFood) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (!GameManager.isGameRunning() && hunger) {
            e.setFoodLevel(20);
        }
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
