package mainmc.listener;

import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Home;
import mainmc.nothing00.functions.ItemPlugin;
import mainmc.nothing00.functions.PluginSign;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.Economy;
import mainmc.nothing00.utils.Time;

public class InteractEvent implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent e) {

		Messages msg = new Messages();
		Conf config = new Conf();
		User user = new User(e.getPlayer().getName());
		MainPermissions s = new MainPermissions(e.getPlayer());

		if (!s.hasPermission("main.build")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("No-Perm"));
			return;
		}

		if (!s.hasPermission("main.build.use")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("No-Perm"));
			return;
		}

		// LOCKED
		if (user.isLocked()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(config.getUnLockMessage());
			return;
		}

		// IN JAIL
		if (user.isJailed()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("jailed"));
			return;
		}

		// BLACKLIST
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null
				&& config.getInteractBlackList().contains("" + e.getClickedBlock().getTypeId())
				&& !s.hasPermission("main.build.use.bypassblacklist")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("BuildInteract"));
			return;
		}
		
		//CHECKBANK
		if(e.getItem()!=null && s.hasPermission("main.withdraw.get")) {
			ItemPlugin item = new ItemPlugin(e.getItem());
			if(item.isBankCheck()) {
				Economy eco = new Economy(e.getPlayer(), item.getCheckBankValue());
				eco.addMoney();
				e.setCancelled(true);
				user.setItem(null);
				e.getPlayer().playNote(e.getPlayer().getLocation(), Instrument.PIANO,  Note.natural(1, Tone.A));
				return;
			}
		}

		// ITEM COMMAND
		if (e.getItem() != null && s.hasPermission("main.item.use")) {
			ItemPlugin item = new ItemPlugin(e.getItem());
			item.executeCommands(e.getPlayer());
		}
		if (e.getItem() != null && e.getItem().getType().equals(Material.BED) && s.hasPermission("main.sethome")) {
			Home home = new Home(e.getPlayer().getName());
			if (home.getHomeNames().contains("bed"))
				home.deleteHome("bed");
			home.setHome(e.getPlayer().getLocation(), "bed");
			e.getPlayer().sendMessage(msg.getMessage("setHome"));
		}

		// SIGN EVENT
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		if (!(e.getClickedBlock().getState() instanceof Sign))
			return;
		Economy eco = new Economy(e.getPlayer());
		Sign clickedSign = (Sign) e.getClickedBlock().getState();
		PluginSign sign = new PluginSign(clickedSign.getLine(0), clickedSign.getLine(1), clickedSign.getLine(2),
				clickedSign.getLine(3));
		if (sign.isBalance() && s.hasPermission("main.sign.balance.use")
				|| sign.isBalance() && s.hasPermission("main.sign.*")) {
			e.getPlayer().sendMessage(msg.getMessage("Balance").replaceAll("%player%", e.getPlayer().getName())
					.replace("%balance%", eco.toString()));
			return;
		}
		if (sign.isDay() && s.hasPermission("main.sign.time.use") || sign.isDay() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					eco.removeMoney();
					Time.setDay();
					e.getPlayer().sendMessage(msg.getMessage("setTime").replaceAll("%time%", "Day"));
					return;
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				Time.setDay();
				e.getPlayer().sendMessage(msg.getMessage("setTime").replaceAll("%time%", "Day"));
				return;
			}
		}
		if (sign.isNight() && s.hasPermission("main.sign.time.use")
				|| sign.isNight() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					eco.removeMoney();
					Time.setNight();
					;
					e.getPlayer().sendMessage(msg.getMessage("setTime").replaceAll("%time%", "Night"));
					return;
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				Time.setNight();
				e.getPlayer().sendMessage(msg.getMessage("setTime").replaceAll("%time%", "Night"));
				return;
			}
		}
		if (sign.isWeather() && s.hasPermission("main.sign.weather.use")
				|| sign.isWeather() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					eco.removeMoney();
					if (sign.isSun()) {
						Time.clearWeather();
						e.getPlayer().sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Sunny"));
						return;
					}
					if (sign.isStorm()) {
						Time.setStorming();
						;
						e.getPlayer().sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Stormy"));
						return;
					}
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				if (sign.isSun()) {
					Time.clearWeather();
					e.getPlayer().sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Sunny"));
					return;
				}
				if (sign.isStorm()) {
					Time.setStorming();
					;
					e.getPlayer().sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Stormy"));
					return;
				}
			}
		}
		if (sign.isTrash() && s.hasPermission("main.sign.disposal.use")
				|| sign.isTrash() && s.hasPermission("main.sign.*")) {
			e.getPlayer().openInventory(sign.getTrash());
			return;
		}
		if (sign.isFree() && s.hasPermission("main.sign.free.use") || sign.isFree() && s.hasPermission("main.sign.*")) {
			e.getPlayer().openInventory(sign.getFree());
			return;
		}
		if (sign.isGameMode() && s.hasPermission("main.sign.gamemode.use")
				|| sign.isGameMode() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					eco.removeMoney();
					if (sign.isSurvival()) {
						user.setGamemode(GameMode.SURVIVAL);
						e.getPlayer().sendMessage(
								msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SURVIVAL.toString()));
						return;
					}
					if (sign.isCreative()) {
						user.setGamemode(GameMode.CREATIVE);
						e.getPlayer().sendMessage(
								msg.getMessage("GameMode").replaceAll("%gm%", GameMode.CREATIVE.toString()));
						return;
					}
					if (sign.isAdventure()) {
						user.setGamemode(GameMode.ADVENTURE);
						e.getPlayer().sendMessage(
								msg.getMessage("GameMode").replaceAll("%gm%", GameMode.ADVENTURE.toString()));
						return;
					}
					if (sign.isSpectator()) {
						user.setGamemode(GameMode.SPECTATOR);
						e.getPlayer().sendMessage(
								msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SPECTATOR.toString()));
						return;
					}
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				if (sign.isSurvival()) {
					user.setGamemode(GameMode.SURVIVAL);
					e.getPlayer()
							.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SURVIVAL.toString()));
					return;
				}
				if (sign.isCreative()) {
					user.setGamemode(GameMode.CREATIVE);
					e.getPlayer()
							.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%", GameMode.CREATIVE.toString()));
					return;
				}
				if (sign.isAdventure()) {
					user.setGamemode(GameMode.ADVENTURE);
					e.getPlayer()
							.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%", GameMode.ADVENTURE.toString()));
					return;
				}
				if (sign.isSpectator()) {
					user.setGamemode(GameMode.SPECTATOR);
					e.getPlayer()
							.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SPECTATOR.toString()));
					return;
				}
			}
		}
		if (sign.isHeal() && s.hasPermission("main.sign.heal.use") || sign.isHeal() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					eco.removeMoney();
					user.heal();
					e.getPlayer().sendMessage(msg.getMessage("Heal"));
					return;
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				user.heal();
				e.getPlayer().sendMessage(msg.getMessage("Heal"));
				return;
			}
		}
		if (sign.isWarp() && s.hasPermission("main.sign.warp.use") || sign.isWarp() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					eco.removeMoney();
					user.teleported(sign.getWarp().getWarp());
					e.getPlayer().sendMessage(msg.getMessage("Warp").replaceAll("%warp%", sign.getWarp().getName()));
					return;
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				user.teleported(sign.getWarp().getWarp());
				e.getPlayer().sendMessage(msg.getMessage("Warp").replaceAll("%warp%", sign.getWarp().getName()));
				return;
			}
		}
		if (sign.isKit() && s.hasPermission("main.sign.kit.use") || sign.isKit() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					eco.removeMoney();
					sign.getKit(e.getPlayer()).giveKit();
					e.getPlayer().updateInventory();
					e.getPlayer().sendMessage(
							msg.getMessage("Kit").replaceAll("%kit%", sign.getKit(e.getPlayer()).getName()));
					return;
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				sign.getKit(e.getPlayer()).giveKit();
				e.getPlayer().updateInventory();
				e.getPlayer()
						.sendMessage(msg.getMessage("Kit").replaceAll("%kit%", sign.getKit(e.getPlayer()).getName()));
				return;
			}
		}
		if (sign.isRepair() && s.hasPermission("main.sign.repair.use")
				|| sign.isRepair() && s.hasPermission("main.sign.*")) {
			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {

					if (sign.isAll()) {
						if (!user.hasEmptyInventory()) {
							eco.removeMoney();
							ItemPlugin.RepairAll(e.getPlayer());
							e.getPlayer().sendMessage(msg.getMessage("Repair"));
							return;
						}
						return;
					}
					if (sign.isHand()) {
						ItemPlugin item = new ItemPlugin(user.getItem());
						if (item.repair()) {
							eco.removeMoney();
							e.getPlayer().sendMessage(msg.getMessage("Repair"));
							return;
						} else {
							e.getPlayer().sendMessage(msg.getMessage("NoItem"));
							return;
						}
					}
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {
				if (sign.isAll()) {
					if (!user.hasEmptyInventory()) {
						ItemPlugin.RepairAll(e.getPlayer());
						e.getPlayer().sendMessage(msg.getMessage("Repair"));
						return;
					}
					return;
				}
				if (sign.isHand()) {
					ItemPlugin item = new ItemPlugin(user.getItem());
					if (item.repair()) {
						e.getPlayer().sendMessage(msg.getMessage("Repair"));
						return;
					} else {
						e.getPlayer().sendMessage(msg.getMessage("NoItem"));
						return;
					}
				}
			}
		}
		if (sign.isEnchant() && s.hasPermission("main.sign.enchant.use")
				|| sign.isEnchant() && s.hasPermission("main.sign.*")) {

			if (sign.hasEconomy()) {
				eco.setMoneyValue(sign.getPrice(false));
				if (eco.detractable()) {
					if (user.getItem() != null) {
						ItemPlugin item = new ItemPlugin(user.getItem());
						item.enchant(sign.getEnchant(), user.getItem());
						e.getPlayer().updateInventory();
						return;
					} else {
						e.getPlayer().sendMessage(msg.getMessage("ItemHand"));
						return;
					}
				} else {
					e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
					return;
				}
			} else {

				if (e.getPlayer().getItemInHand() != null
						&& !e.getPlayer().getItemInHand().getType().equals(Material.AIR)) {
					ItemPlugin item = new ItemPlugin(e.getPlayer().getItemInHand());

					item.enchant(sign.getEnchant(), item.getItem());
					e.getPlayer().updateInventory();
					return;
				} else {
					e.getPlayer().sendMessage(msg.getMessage("ItemHand"));
					return;
				}
			}
		}
		if (sign.isBuy() && s.hasPermission("main.sign.buy.use") || sign.isBuy() && s.hasPermission("main.sign.*")) {
			eco.setMoneyValue(sign.getPrice(true));
			if (eco.detractable()) {
				eco.removeMoney();
				user.addItem(sign.getItem());
				e.getPlayer().updateInventory();
				e.getPlayer()
						.sendMessage(msg.getMessage("Buy").replaceAll("%item%", sign.getItem().getType().toString())
								.replaceAll("%amount%", sign.getItem().getAmount() + ""));
				return;
			} else {
				e.getPlayer().sendMessage(msg.getMessage("NoMoney"));
				return;
			}
		}

		if (sign.isSell() && s.hasPermission("main.sign.sell.use") || sign.isSell() && s.hasPermission("main.sign.*")) {
			eco.setMoneyValue(sign.getPrice(true));
			if (user.hasItems(sign.getItem().getAmount(), sign.getItem().getType(),
					sign.getItem().getData().getData())) {
				eco.addMoney();
				user.removeItems(sign.getItem().getAmount(), sign.getItem().getType(),
						sign.getItem().getData().getData());
				e.getPlayer().updateInventory();
				e.getPlayer().sendMessage(msg.getMessage("givedMoney").replaceAll("%player%", "CONSOLE")
						.replace("%balance%", eco.getMoneyToString()));
				return;
			} else {
				e.getPlayer()
						.sendMessage(msg.getMessage("Sell").replaceAll("%item%", sign.getItem().getType().toString())
								.replaceAll("%count%", sign.getItem().getAmount() + ""));
				return;
			}
		}

	}

}
