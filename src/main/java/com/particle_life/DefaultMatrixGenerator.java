package com.particle_life;

public class DefaultMatrixGenerator implements MatrixGenerator{
  @Override
  public Matrix makeMatrix(int size) {
    DefaultMatrix m=new DefaultMatrix(size);
    m.randomize();
    return m;
  }
}
