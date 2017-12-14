package de.craften.plugins.rpgplus.components.entitymanager.traits;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

import net.citizensnpcs.api.trait.Trait;

public class ArmorStandTrait extends Trait{

	public ArmorStandTrait() {
		super("RpgPlus ArmorStand Trait");
	}
	
	private boolean visible = true;
	private boolean small = false;
	private boolean noGravity = false;

	
	private EulerAngle rightArm = new EulerAngle(0, 0, 0);
	
	@Override
	public void run() {
		
		if (getNPC().isSpawned()) {
		
			ArmorStand armorStand = (ArmorStand)getNPC().getEntity();
			armorStand.setVisible(visible);
			armorStand.setSmall(small);
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
		if (getNPC().isSpawned()) ((ArmorStand) getNPC().getEntity()).setVisible(visible);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public EulerAngle getRightArm() {
		return rightArm;
	}

	public void setRightArm(EulerAngle rightArm) {
		this.rightArm = rightArm;
	}
	
}
