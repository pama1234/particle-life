package com.particle_life;

import pama1234.math.UtilMath;

public class DefaultMatrix implements Matrix{
  public final int size;
  public final float[][] values;
  public DefaultMatrix(int size) {
    this.size=size;
    values=new float[size][];
    for(int i=0;i<size;i++) {
      values[i]=new float[size];
    }
    zero();
  }
  public void zero() {
    for(int i=0;i<size;i++) {
      for(int j=0;j<size;j++) {
        values[i][j]=0;
      }
    }
  }
  public void randomize() {
    for(int i=0;i<size;i++) {
      for(int j=0;j<size;j++) {
        values[i][j]=2*UtilMath.random()-1;
      }
    }
  }
  @Override
  public int size() {
    return size;
  }
  @Override
  public float get(int i,int j) {
    return values[i][j];
  }
  @Override
  public void set(int i,int j,float value) {
    values[i][j]=value;
  }
  @Override
  public DefaultMatrix deepCopy() {
    DefaultMatrix copy=new DefaultMatrix(size);
    for(int i=0;i<size;i++) {
      for(int j=0;j<size;j++) {
        copy.values[i][j]=values[i][j];
      }
    }
    return copy;
  }
  @Override
  public boolean equals(Object o) {
    if(o instanceof Matrix m) {
      if(m.size()!=size) {
        return false;
      }
      for(int i=0;i<size;i++) {
        for(int j=0;j<size;j++) {
          if(m.get(i,j)!=get(i,j)) {
            return false;
          }
        }
      }
    }else {
      return false;
    }
    return true;
  }
}
