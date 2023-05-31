package com.particle_life;

import org.joml.Vector3f;

public interface TypeSetter{
  /**
   *
   * @param position
   * @param velocity
   * @param type     the previous type of the given particle
   * @param nTypes
   * @return the new type
   */
  int getType(Vector3f position,Vector3f velocity,int type,int nTypes);
}
