package com.particle_life;

public interface Matrix{
  int size();
  float get(int i,int j);
  void set(int i,int j,float value);
  Matrix deepCopy();
}
