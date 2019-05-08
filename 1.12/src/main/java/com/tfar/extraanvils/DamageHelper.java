package com.tfar.extraanvils;

import net.minecraft.block.BlockFalling;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class DamageHelper {

  public static void itsAnvilTime(World world, BlockPos pos, BlockFalling anvil) {

    float amount = 5;

    int r = 1;
    int x = pos.getX();int y = pos.getY();int z = pos.getZ();

    List<Entity> victims = world.getEntitiesWithinAABB(EntityLivingBase.class,
            new AxisAlignedBB(x - r, y - r, z - r, x + r, y + r, z + r));
    if (victims.size() <= 0) return;

    for (Entity victim : victims)
      if (!((EntityLivingBase) victim).getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
        ((EntityLivingBase) victim).getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int) (amount * 4.0F + (((EntityLivingBase) victim).getRNG().nextFloat() * amount * 2.0F)), (EntityLivingBase) victim);
        amount *= 0.75F;
      }
  }
}
