package com.tfar.extraanvils.generic;

public class AnvilProperties {

  public final String material;
  public final int cap;
  public final double weight;
  public final double fallResistance;
  public final double durabilityMultiplier;
  public final double costMulti;
  public final boolean causesPlayerDamage;

  public AnvilProperties(String materialName, int maxLevelCap, double weight, double fallResistance, double durabilityMultiplier){
    this(materialName,maxLevelCap,weight,fallResistance,durabilityMultiplier,1,false);
  }

  public AnvilProperties(String materialName, int maxLevelCap, double weight, double fallResistance, double durabilityMultiplier, double costMulti){
    this(materialName,maxLevelCap,weight,fallResistance,durabilityMultiplier,costMulti,false);
  }

  public AnvilProperties(String materialName, int maxLevelCap, double weight, double fallResistance, double durabilityMultiplier, double costMultiplier,boolean causesPlayerDamage){
    this.material = materialName;
    this.cap = maxLevelCap;
    this.weight = weight;
    this.fallResistance = fallResistance;
    this.durabilityMultiplier = durabilityMultiplier;
    this.costMulti = costMultiplier;
    this.causesPlayerDamage = causesPlayerDamage;
  }
}
