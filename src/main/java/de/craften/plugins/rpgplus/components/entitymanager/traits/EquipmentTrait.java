package de.craften.plugins.rpgplus.components.entitymanager.traits;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.trait.Trait;

public class EquipmentTrait extends Trait{

	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ItemStack hand;

	public EquipmentTrait() {
		super("RpgPlus Equiment Trait");
	}
	
	@Override
	public void onSpawn() {
		
		LivingEntity entity = (LivingEntity)getNPC().getEntity();
		entity.getEquipment().setHelmet(helmet);
		entity.getEquipment().setChestplate(chestplate);
		entity.getEquipment().setLeggings(leggings);
		entity.getEquipment().setBoots(boots);
		entity.getEquipment().setItemInHand(hand);
			
	}
	
	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
        if (getNPC().isSpawned()) ((LivingEntity) getNPC().getEntity()).getEquipment().setHelmet(helmet);
	}

	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
        if (getNPC().isSpawned()) ((LivingEntity) getNPC().getEntity()).getEquipment().setChestplate(helmet);
	}

	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
        if (getNPC().isSpawned()) ((LivingEntity) getNPC().getEntity()).getEquipment().setLeggings(helmet);
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
        if (getNPC().isSpawned()) ((LivingEntity) getNPC().getEntity()).getEquipment().setBoots(helmet);
	}
	
	public ItemStack getHand() {
		return hand;
	}

	public void setHand(ItemStack hand) {
		this.hand = hand;
        if (getNPC().isSpawned()) ((LivingEntity) getNPC().getEntity()).getEquipment().setItemInHand(helmet);
	}
	
	public ItemStack getHelmet() {
		return helmet;
	}
	
	public ItemStack getChestplate() {
		return chestplate;
	}
	
	public ItemStack getLeggings() {
		return leggings;
	}
	
	public ItemStack getBoots() {
		return boots;
	}
}
