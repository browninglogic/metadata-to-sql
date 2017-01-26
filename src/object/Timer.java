package object;

public class Timer {
	private long mStartTime;
	private long mEndTime;
	
	public void start()
	{
		mStartTime = System.nanoTime();
		mEndTime = 0;
	}
	
	public void stop()
	{
		mEndTime = System.nanoTime();
	}
	
	public Timer()
	{
		this.start();
	}
	
	public long getTimeSpan()
	{
		if(mEndTime > 0)
		{
			return mEndTime - mStartTime;
		}
		else
			throw new UnsupportedOperationException("You must first stop the timer");
	}
	
	public double getTimeSpanInSeconds()
	{
		double timeSpan = (double)this.getTimeSpan();
		
		return timeSpan / 1000000000;
	}
	
	public double getTimeSpanInMinutes()
	{
		return this.getTimeSpanInSeconds() / 60;
	}
	
	//TODO Move this to a common shared library

}
