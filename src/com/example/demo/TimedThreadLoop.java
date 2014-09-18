package com.example.demo;


///
/// use this class when you need to call a loop-body at certain intervals in a separate thread
/// it also accounts for the time it takes for the loop-body to run:
/// works like this: 
/// t=t0 => start loop-body
/// t=t1 => end loop-body
/// sleep for (next_loop_time - (t1-t0))
///
public class TimedThreadLoop {
	private boolean mContinueLoop = true;
	private long mLoopTime;
	private LoopBody mLoopBody;

	public abstract class LoopBody {
		abstract public void run();
		public void exitLoop() {
			mContinueLoop = false;
		}
	}

	public TimedThreadLoop() {}
	public TimedThreadLoop(long loopTime, LoopBody callable) {
		set(loopTime, callable);
	}
	
	public void set(long loopTime, LoopBody callable) {
		mLoopTime = loopTime;
		mLoopBody = callable;
	}

	public void start() {
		new Thread(new Runnable() {
			public void run() {
				while (mContinueLoop) {
					long startTime = System.currentTimeMillis();
					mLoopBody.run();
					long loopBodyTime = System.currentTimeMillis() - startTime;
					long sleepTime = mLoopTime - loopBodyTime;
					try {
						if ( sleepTime > 0 )
							Thread.sleep( sleepTime );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
