package com.particle_life;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility class for starting a thread that repeatedly calls a given method.<br>
 * Example:
 * 
 * <pre>
 * Loop loop=new Loop();
 * loop.start(dt-> {
 *   // do something expensive
 * });
 * </pre>
 *
 * To start the thread, call {@link #start(Callback)}.<br>
 * To stop the thread, call {@link #stop(long millis)}.<br>
 * To execute code synchronously with the loop, use {@link #enqueue(Runnable)}. Example:
 * 
 * <pre>
 * loop.enqueue(()-> {
 *   // run code here that should not run
 *   // in parallel with the code in the loop
 * });
 * </pre>
 */
public class Loop{
  /**
   * Upper limit for time step, in seconds.
   * <p>
   * If this is negative (e.g. -1.0), there will be no limit.
   * <p>
   * This won't have any effect on the actual framerate returned by {@link #getActualDt()}.
   */
  public float maxDt=1.0f/20.0f; // min. 20 fps
  /**
   * If this is <code>true</code>, the callback won't be called in the loop.
   */
  public boolean pause=false;
  private final Clock clock=new Clock(60);
  private Thread loopThread=null;
  private final AtomicBoolean loopShouldRun=new AtomicBoolean(false);
  private final LinkedBlockingDeque<Runnable> commandQueue=new LinkedBlockingDeque<>();
  private final AtomicReference<Runnable> once=new AtomicReference<>(null);
  /**
   * Will be invoked repeatedly by the loop started with {@link #start(Callback)} until
   * {@link #stop(long)} is called.
   */
  public interface Callback{
    /**
     * Will be invoked repeatedly by the loop started with {@link #start(Callback)} until
     * {@link #stop(long)} is called. The time for each iteration of the loop is measured and passed
     * to the callback.
     *
     * @param dt The time passed in the iteration of the loop, limited by {@link #maxDt}.
     */
    void call(float dt);
  }
  /**
   * The passed command will be added to the queue and will be processed in the next iteration of
   * the loop thread.<br>
   * The commands will be executed in the order they were added via this method.
   *
   * @param cmd the command to be executed in the loop thread
   */
  public void enqueue(Runnable cmd) {
    //todo: debug print if some GUI elements spam commands
    commandQueue.addLast(cmd);
  }
  /**
   * The passed command will be executed in the next iteration of the loop thread. If this method
   * is called again before the next iteration of the loop thread, the previous command will be
   * replaced by this one.<br>
   * This is helpful if your loop may take longer, and you don't want expensive commands to pile
   * up in the meantime (as this could cause the loop to take even longer).
   *
   * @param cmd the command to be executed in the loop thread
   */
  public void doOnce(Runnable cmd) {
    once.set(cmd);
  }
  private void processCommandQueue() {
    Runnable cmd;
    while((cmd=commandQueue.pollFirst())!=null) {
      cmd.run();
    }
  }
  public synchronized void start(Callback loop) {
    if(loopThread!=null) throw new IllegalStateException("Loop thread didn't finish properly (wasn't null).");
    loopShouldRun.set(true);
    loopThread=new Thread(()-> {
      while(loopShouldRun.get()) {
        loop(loop);
      }
    });
    loopThread.start();
  }
  private void loop(Callback loop) {
    clock.tick();
    processCommandQueue();
    Runnable onceCommand=once.getAndSet(null);
    if(onceCommand!=null) onceCommand.run();
    if(!pause) {
      loop.call(computeDt());
    }
  }
  /**
   * Tells the loop to stop and waits for the current iteration to finish. A timeout of 0 means to
   * wait forever.
   *
   * @param millis the time to wait for the loop to finish in milliseconds
   * @return whether the thread could be stopped
   */
  public synchronized boolean stop(long millis) throws InterruptedException {
    assert loopThread!=null:"Thread is null";
    if(!loopThread.isAlive()) {
      throw new IllegalStateException("Thread is not running.");
    }
    loopShouldRun.set(false);
    loopThread.join(millis); // A timeout of 0 means to wait forever.
    if(loopThread.isAlive()) {
      return false;
    }
    loopThread=null;
    return true;
  }
  private float computeDt() {
    float dt=clock.getDtMillis()/1000.0f;
    return Math.min(maxDt,dt);
  }
  /**
   * Returns how much time passed between the last two iterations of the loop, in seconds. Unlike
   * the value given to the callback in {@link #start(Callback)}, this value is not limited by
   * {@link #maxDt}. Note that therefore the return value of this method can be very small if
   * <code>{@link #pause} == true</code>, as there is no work to be done in the loop.
   * 
   * @return how much time passed between the last two iterations of the loop, in seconds.
   */
  public float getActualDt() {
    return clock.getDtMillis()/1000.0f;
  }
  /**
   * Average framerate over the last couple of frames. Unlike the value given to the callback in
   * {@link #start(Callback)}, this value is not limited by {@link #maxDt}. Note that therefore
   * the return value of this method can be very high if <code>{@link #pause} == true</code>, as
   * there is no work to be done in the loop.
   * 
   * @return average framerate in frames per second.
   * @see #getActualDt()
   */
  public float getAvgFramerate() {
    return clock.getAvgFramerate();
  }
}
