package com.particle_life;

import org.joml.Vector3f;

import pama1234.math.UtilMath;

public class DefaultPositionSetter implements PositionSetter{
  @Override
  public void set(Vector3f position,int type,int nTypes) {
    position.set(
      UtilMath.random()*2-1,
      UtilMath.random()*2-1,
      0);
  }
}
