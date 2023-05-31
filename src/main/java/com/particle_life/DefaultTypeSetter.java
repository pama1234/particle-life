package com.particle_life;

import org.joml.Vector3f;

public class DefaultTypeSetter implements TypeSetter{
  @Override
  public int getType(Vector3f position,Vector3f velocity,int type,int nTypes) {
    return (int)Math.floor(Math.random()*nTypes);
  }
}
