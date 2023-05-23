package com.particle_life;

import org.joml.Vector3d;

/**
 * Provides functions for assuring that the coordinates of particles are in [-1, 1].
 * <p>
 * Two approaches are possible:
 * <ol>
 * <li>{@link #clamp(Vector3d) Range.clamp(x)}
 * <p>
 * Leaves coordinates inside [-1, 1] untouched. All other coordinates are clamped between -1 and
 * 1.
 * <p>
 * Example:
 * 
 * <pre>
 * x=new Vector3d(0.4,-1.3,2.0);
 * Range.clamp(x);
 * // x is now (0.4, -1.0, 1.0).
 * </pre>
 * 
 * </li>
 * <li>{@link #wrap(Vector3d) Range.wrap(x)}
 * <p>
 * Leaves coordinates inside [-1, 1) untouched. All other coordinates are modified by repeatedly
 * adding or subtracting 2 until they are in [-1, 1).
 * <p>
 * Example:
 * 
 * <pre>
 * x=new Vector3d(0.4,-1.3,2.0);
 * Range.wrap(x);
 * // x is now (0.4, 0.7, 0.0).
 * </pre>
 * 
 * When using this approach, the shortest path from point <code>a</code> to point <code>b</code>
 * can be determined using <code>wrap(b - a)</code>.</li>
 * </ol>
 */
class Range{
  public static void wrap(Vector3d x) {
    x.x=wrap(x.x);
    x.y=wrap(x.y);
    x.z=0; //todo 3D
  }
  public static void clamp(Vector3d x) {
    x.x=clamp(x.x);
    x.y=clamp(x.y);
    x.z=0; // todo 3D
  }
  private static double wrap(double value) {
    return modulo(value+1,2)-1;
  }
  private static double clamp(double val) {
    if(val<-1) {
      return -1;
    }else if(val>1) {
      return 1;
    }
    return val;
  }
  /**
   * Fast implementation of the floored modulo operation. Only works for positive <code>b</code>,
   * but <code>a</code> may be negative.
   * <p>
   * The speed of this implementation depends on the assumption that <code>a</code> is only a few
   * times larger (positive or negative) than <code>b</code>.
   *
   * @param a dividend
   * @param b divisor, must be positive
   * @return remainder of <code>a / b</code>, always positive
   */
  private static double modulo(double a,double b) {
    if(a<0) {
      do {
        a+=b;
      }while(a<0);
      return a;
    }else if(a>=b) {
      do {
        a-=b;
      }while(a>=b);
      return a;
    }
    return a;
  }
}
