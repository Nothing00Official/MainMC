package mainmc.nothing00.functions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Mob {

	private EntityType e;
	private String es;

	public Mob(EntityType e) {
		this.e = e;
		this.es = e.toString();
	}

	public Mob(String e) {
		this.es = e;
		this.e = getMob();
	}
	
	public boolean exists() {
		return this.e!=null;
	}

	public int kill() {
		int cont = 0;
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (e.getType().equals(this.e)) {
					e.remove();
					cont++;
				}
			}
		}
		return cont;
	}
	
	public String getType() {
		return this.es.toUpperCase();
	}

	public EntityType getMob() {
		String s = this.es.toLowerCase();
		EntityType mob;
		switch (s) {

		case "bat":
			mob = EntityType.BAT;
			break;
		case "chicken":
			mob = EntityType.CHICKEN;
			break;
		case "cow":
			mob = EntityType.COW;
			break;
		case "mooshroom":
			mob = EntityType.MUSHROOM_COW;
			break;
		case "pig":
			mob = EntityType.PIG;
			break;
		case "rabbit":
			mob = EntityType.RABBIT;
			break;
		case "sheep":
			mob = EntityType.SHEEP;
			break;
		case "horse":
			mob = EntityType.HORSE;
			break;
		case "squid":
			mob = EntityType.SQUID;
			break;
		case "villager":
			mob = EntityType.VILLAGER;
			break;
		case "cavespider":
			mob = EntityType.CAVE_SPIDER;
			break;
		case "enderman":
			mob = EntityType.ENDERMAN;
			break;
		case "spider":
			mob = EntityType.SPIDER;
			break;
		case "pigmanzombie":
		case "pigzombie":
		case "pigman":
			mob = EntityType.PIG_ZOMBIE;
			break;
		case "blaze":
			mob = EntityType.BLAZE;
			break;
		case "creeper":
			mob = EntityType.CREEPER;
			break;
		case "guardian":
			mob = EntityType.GUARDIAN;
			break;
		case "endermite":
			mob = EntityType.ENDERMITE;
			break;
		case "ghast":
			mob = EntityType.GHAST;
			break;
		case "magmacube":
			mob = EntityType.MAGMA_CUBE;
			break;
		case "silverfiah":
			mob = EntityType.SILVERFISH;
			break;
		case "skeleton":
			mob = EntityType.SKELETON;
			break;
		case "slime":
			mob = EntityType.SLIME;
			break;
		case "witch":
			mob = EntityType.WITCH;
			break;
		case "witherskull":
		case "witherskeleton":
			mob = EntityType.WITHER_SKULL;
			break;
		case "zombie":
			mob = EntityType.ZOMBIE;
			break;
		case "ocelot":
			mob = EntityType.OCELOT;
			break;
		case "wolf":
			mob = EntityType.WOLF;
			break;
		case "irongolem":
			mob = EntityType.IRON_GOLEM;
			break;
		case "snowman":
		case "snowgolem":
			mob = EntityType.SNOWMAN;
			break;
		case "enderdragon":
			mob = EntityType.ENDER_DRAGON;
			break;
		case "wither":
			mob = EntityType.WITHER;
			break;
		case "giant":
			mob = EntityType.GIANT;
			break;
		default:
			mob = EntityType.valueOf(this.es.toUpperCase());
			break;

		}

		return mob;
	}

	public static int killAll() {
		int cont = 0;
		for (World w : Bukkit.getWorlds()) {
			String world = w.getName();
			for (Entity e : Bukkit.getServer().getWorld(world).getEntities()) {
				if (e instanceof Player)
					continue;
				if (!(e instanceof LivingEntity))
					continue;
				e.remove();
				cont++;
			}
		}
		return cont;
	}

}
