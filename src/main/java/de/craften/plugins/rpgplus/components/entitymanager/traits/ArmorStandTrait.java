package de.craften.plugins.rpgplus.components.entitymanager.traits;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import net.citizensnpcs.api.trait.Trait;

public class ArmorStandTrait extends Trait{

	public ArmorStandTrait() {
		super("RpgPlus ArmorStand Trait");
	}
	
	private boolean visible = true;
	private boolean small = false;
	private boolean noGravity = false;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ItemStack hand;
	
	private EulerAngle rightArm = new EulerAngle(0, 0, 0);
	
	@Override
	public void onSpawn() {
		
		if (getNPC().getEntity() != null) {
		
			ArmorStand armorStand = (ArmorStand)getNPC().getEntity();
			armorStand.setVisible(visible);
			armorStand.setSmall(small);
			armorStand.setHelmet(helmet);
			armorStand.setChestplate(chestplate);
			armorStand.setLeggings(leggings);
			armorStand.setBoots(boots);
			armorStand.setItemInHand(hand);
			armorStand.setGravity(!noGravity);
			armorStand.setRightArmPose(rightArm);
			
		}
		
	}
	
	public void setSmall(boolean small) {
		this.small = small;
	}
	
	public boolean isSmall() {
		return small;
	}
	
	public void setNoGravity(boolean noGravity) {
		this.noGravity = noGravity;
	}
	
	public boolean isNoGravity() {
		return noGravity;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}
	
	public ItemStack getHand() {
		return hand;
	}

	public void setHand(ItemStack hand) {
		this.hand = hand;
	}
	
	public EulerAngle getRightArm() {
		return rightArm;
	}

	public void setRightArm(EulerAngle rightArm) {
		this.rightArm = rightArm;
	}
	
}
