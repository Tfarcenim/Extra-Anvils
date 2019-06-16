package com.tfar.extraanvils;

public enum  EnumVariants {
  NORMAL("_anvil"),
  CHIPPED("_anvil_chipped"),
  DAMAGED("_anvil_damaged");

  private String s;

  EnumVariants(String variant){
    this.s = variant;
  }

  public String getString(){
    return s;
  }
}
