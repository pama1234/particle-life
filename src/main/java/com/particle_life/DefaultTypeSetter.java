package com.particle_life;

import org.joml.Vector3f;

import pama1234.math.UtilMath;

public class DefaultTypeSetter implements TypeSetter{
  @Override
  public int getType(Vector3f position,Vector3f velocity,int type,int nTypes) {
    return UtilMath.floor(UtilMath.random()*nTypes);
  }
}
