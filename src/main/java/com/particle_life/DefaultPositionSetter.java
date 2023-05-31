package com.particle_life;

import org.joml.Vector3f;

public class DefaultPositionSetter implements PositionSetter{
  @Override
  public void set(Vector3f position,int type,int nTypes) {
    position.set(
      Math.random()*2-1,
      Math.random()*2-1,
      0);
  }
}
