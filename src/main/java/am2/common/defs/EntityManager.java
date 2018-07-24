package am2.common.defs;

import am2.ArsMagica2;
import am2.common.LogHelper;
import am2.common.bosses.EntityAirGuardian;
import am2.common.bosses.EntityArcaneGuardian;
import am2.common.bosses.EntityEarthGuardian;
import am2.common.bosses.EntityEnderGuardian;
import am2.common.bosses.EntityFireGuardian;
import am2.common.bosses.EntityLifeGuardian;
import am2.common.bosses.EntityLightningGuardian;
import am2.common.bosses.EntityNatureGuardian;
import am2.common.bosses.EntityWaterGuardian;
import am2.common.bosses.EntityWinterGuardian;
import am2.common.entity.EntityAirSled;
import am2.common.entity.EntityBoundArrow;
import am2.common.entity.EntityBroom;
import am2.common.entity.EntityDarkMage;
import am2.common.entity.EntityDarkling;
import am2.common.entity.EntityDryad;
import am2.common.entity.EntityEarthElemental;
import am2.common.entity.EntityFireElemental;
import am2.common.entity.EntityFlicker;
import am2.common.entity.EntityHecate;
import am2.common.entity.EntityHellCow;
import am2.common.entity.EntityLightMage;
import am2.common.entity.EntityManaCreeper;
import am2.common.entity.EntityManaElemental;
import am2.common.entity.EntityManaVortex;
import am2.common.entity.EntityRiftStorage;
import am2.common.entity.EntityShadowHelper;
import am2.common.entity.EntityShockwave;
import am2.common.entity.EntitySpellEffect;
import am2.common.entity.EntitySpellProjectile;
import am2.common.entity.EntityThrownRock;
import am2.common.entity.EntityThrownSickle;
import am2.common.entity.EntityWaterElemental;
import am2.common.entity.EntityWhirlwind;
import am2.common.entity.EntityWinterGuardianArm;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityManager {
	
	public static final EntityManager instance = new EntityManager();
	
	private EntityManager() {
		
	}
	
	public void registerEntities() {
		EntityRegistry.registerModEntity(EntitySpellProjectile.class, "SpellProjectile", 0, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityRiftStorage.class, "RiftStorage", 1, ArsMagica2.instance, 64, 2, false);
		EntityRegistry.registerModEntity(EntitySpellEffect.class, "SpellEffect", 2, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityThrownRock.class, "ThrownRock", 3, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityBoundArrow.class, "BoundArrow", 4, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityDarkling.class, "Darkling", 5, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityDarkMage.class, "DarkMage", 6, ArsMagica2.instance, 64, 2, true, 0xaa00ff, 0x660066);
		EntityRegistry.registerModEntity(EntityDryad.class, "Dryad", 7, ArsMagica2.instance, 64, 2, true, 0x00ff00, 0x34e122);
		EntityRegistry.registerModEntity(EntityEarthElemental.class, "EarthElemental", 8, ArsMagica2.instance, 64, 2, true, 0x61330b, 0x00ff00);
		EntityRegistry.registerModEntity(EntityFireElemental.class, "FireElemental", 9, ArsMagica2.instance, 64, 2, true, 0xef260b, 0xff0000);
		EntityRegistry.registerModEntity(EntityLightMage.class, "LightMage", 10, ArsMagica2.instance, 64, 2, true, 0xaa00ff, 0xff00ff);
		EntityRegistry.registerModEntity(EntityManaElemental.class, "ManaElemental", 11, ArsMagica2.instance, 64, 2, true, 0xcccccc, 0xb935cd);
		EntityRegistry.registerModEntity(EntityManaVortex.class, "ManaVortex", 12, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityShockwave.class, "Shockwave", 13, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityThrownSickle.class, "ThrownSickle", 14, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWhirlwind.class, "Whirlwind", 15, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWinterGuardianArm.class, "WinterGuardianArm", 16, ArsMagica2.instance, 64, 2, true);
		
		EntityRegistry.registerModEntity(EntityAirGuardian.class, "AirGuardian", 17, ArsMagica2.instance, 64, 2, true, 0xFFFFFF, 0xFFCC00);
		EntityRegistry.registerModEntity(EntityArcaneGuardian.class, "ArcaneGuardian", 18, ArsMagica2.instance, 64, 2, true, 0x999999, 0xcc00cc);
		EntityRegistry.registerModEntity(EntityEarthGuardian.class, "EarthGuardian", 19, ArsMagica2.instance, 64, 2, true, 0x663300, 0x339900);
		EntityRegistry.registerModEntity(EntityEnderGuardian.class, "EnderGuardian", 20, ArsMagica2.instance, 64, 2, true, 0x000000, 0x6633);
		EntityRegistry.registerModEntity(EntityFireGuardian.class, "FireGuardian", 21, ArsMagica2.instance, 64, 2, true, 0xFFFFFF, 0xFF0000);
		EntityRegistry.registerModEntity(EntityLifeGuardian.class, "LifeGuardian", 22, ArsMagica2.instance, 64, 2, true, 0x00E6FF, 0xFFE600);
		EntityRegistry.registerModEntity(EntityLightningGuardian.class, "LightningGuardian", 23, ArsMagica2.instance, 64, 2, true, 0xFFE600, 0x00C4FF);
		EntityRegistry.registerModEntity(EntityNatureGuardian.class, "NatureGuardian", 24, ArsMagica2.instance, 64, 2, true, 0x44FF00, 0x307D0F);
		EntityRegistry.registerModEntity(EntityWaterGuardian.class, "WaterGuardian", 25, ArsMagica2.instance, 64, 2, true, 0x0F387D, 0x0097CE);
		EntityRegistry.registerModEntity(EntityWinterGuardian.class, "WinterGuardian", 26, ArsMagica2.instance, 64, 2, true, 0x00CEBA, 0x104742);
		
		EntityRegistry.registerModEntity(EntityAirSled.class, "AirSled", 27, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityBroom.class, "Broom", 28, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWaterElemental.class, "WaterElemental", 29, ArsMagica2.instance, 64, 2, true, 0x0b5cef, 0x0000ff);
		EntityRegistry.registerModEntity(EntityManaCreeper.class, "ManaCreeper", 30, ArsMagica2.instance, 64, 2, true, 0x0b5cef, 0xb935cd);
		EntityRegistry.registerModEntity(EntityHecate.class, "Hecate", 31, ArsMagica2.instance, 64, 2, true, 0xef260b, 0x3f043d);
		EntityRegistry.registerModEntity(EntityFlicker.class, "Flicker", 32, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityHellCow.class, "HellCow", 33, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityShadowHelper.class, "ShadowHelper", 34, ArsMagica2.instance, 64, 2, true);
	}

	public void initializeSpawns(){
		BiomeDictionary.registerAllBiomes();

		//SpawnListEntry wisps = new SpawnListEntry(EntityWisp.class, 1, 1, 1);
		SpawnListEntry manaElementals = new SpawnListEntry(EntityManaElemental.class, ArsMagica2.config.GetManaElementalSpawnRate(), 1, 1);
		SpawnListEntry dryads = new SpawnListEntry(EntityDryad.class, ArsMagica2.config.GetDryadSpawnRate(), 1, 2);
		SpawnListEntry hecates_nonHell = new SpawnListEntry(EntityHecate.class, ArsMagica2.config.GetHecateSpawnRate(), 1, 1);
		SpawnListEntry hecates_hell = new SpawnListEntry(EntityHecate.class, ArsMagica2.config.GetHecateSpawnRate() * 2, 1, 2);
		SpawnListEntry manaCreepers = new SpawnListEntry(EntityManaCreeper.class, ArsMagica2.config.GetManaCreeperSpawnRate(), 1, 1);
		SpawnListEntry lightMages = new SpawnListEntry(EntityLightMage.class, ArsMagica2.config.GetMageSpawnRate(), 1, 3);
		SpawnListEntry darkMages = new SpawnListEntry(EntityDarkMage.class, ArsMagica2.config.GetMageSpawnRate(), 1, 3);
		SpawnListEntry waterElementals = new SpawnListEntry(EntityWaterElemental.class, ArsMagica2.config.GetWaterElementalSpawnRate(), 1, 3);
		SpawnListEntry darklings = new SpawnListEntry(EntityDarkling.class, ArsMagica2.config.GetDarklingSpawnRate(), 4, 8);
		SpawnListEntry earthElementals = new SpawnListEntry(EntityEarthElemental.class, ArsMagica2.config.GetEarthElementalSpawnRate(), 1, 2);
		SpawnListEntry fireElementals = new SpawnListEntry(EntityFireElemental.class, ArsMagica2.config.GetFireElementalSpawnRate(), 1, 1);
		SpawnListEntry flickers = new SpawnListEntry(EntityFlicker.class, ArsMagica2.config.GetFlickerSpawnRate(), 1, 1);

		initSpawnsForBiomeTypes(manaElementals, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(dryads, EnumCreatureType.CREATURE, new Type[]{Type.BEACH, Type.FOREST, Type.MAGICAL, Type.HILLS, Type.JUNGLE, Type.MOUNTAIN, Type.PLAINS}, new Type[]{Type.END, Type.COLD, Type.MUSHROOM, Type.NETHER, Type.WASTELAND, Type.SWAMP, Type.DRY});

		initSpawnsForBiomeTypes(hecates_nonHell, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(hecates_hell, EnumCreatureType.MONSTER, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

		initSpawnsForBiomeTypes(darklings, EnumCreatureType.MONSTER, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

		initSpawnsForBiomeTypes(manaCreepers, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(lightMages, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(darkMages, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(waterElementals, EnumCreatureType.MONSTER, new Type[]{Type.WATER}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});
		initSpawnsForBiomeTypes(waterElementals, EnumCreatureType.WATER_CREATURE, new Type[]{Type.WATER}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(earthElementals, EnumCreatureType.MONSTER, new Type[]{Type.HILLS, Type.MOUNTAIN}, new Type[]{Type.MUSHROOM});
		initSpawnsForBiomeTypes(fireElementals, EnumCreatureType.MONSTER, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});
		
		initSpawnsForBiomeTypes(flickers, EnumCreatureType.AMBIENT, Type.values(), new Type[0]);

	}

	private void initSpawnsForBiomeTypes(SpawnListEntry spawnListEntry, EnumCreatureType creatureType, Type[] types, Type[] exclusions){
		if (spawnListEntry.itemWeight == 0){
			LogHelper.info("Skipping spawn list entry for %s (as type %s), as the weight is set to 0.  This can be changed in config.", spawnListEntry.entityClass.getName(), creatureType.toString());
			return;
		}
		for (Type type : types){
			initSpawnsForBiomes(BiomeDictionary.getBiomesForType(type), spawnListEntry, creatureType, exclusions);
		}
	}

	private void initSpawnsForBiomes(Biome[] biomes, SpawnListEntry spawnListEntry, EnumCreatureType creatureType, Type[] exclusions){
		if (biomes == null) return;
		for (Biome biome : biomes){
			if (biomeIsExcluded(biome, exclusions)) continue;
			if (!biome.getSpawnableList(creatureType).contains(spawnListEntry))
				biome.getSpawnableList(creatureType).add(spawnListEntry);
		}
	}

	private boolean biomeIsExcluded(Biome biome, Type[] exclusions){

		Type biomeTypes[] = BiomeDictionary.getTypesForBiome(biome);

		for (Type exclusion : exclusions){
			for (Type biomeType : biomeTypes){
				if (biomeType == exclusion) return true;
			}
		}
		return false;
	}}
