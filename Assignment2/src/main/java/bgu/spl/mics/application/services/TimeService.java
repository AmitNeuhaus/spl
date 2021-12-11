package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	int currentTick;
	int terminateTick;
	Timer timer;

	public TimeService(int duration, int speed) {
		super("Time Service");
		currentTick = 1;
		terminateTick = duration;
		timer = new Timer();
		timer.schedule(new IncrementTick(), speed); // schedule the task
	}


	class IncrementTick extends TimerTask {
		public void run() {
			System.out.println("Tick getting incremented");
			currentTick++;
			if (currentTick == terminateTick)
				timer.cancel();
			sendBroadcast(new TickBroadcast(currentTick));
		}
	}

	@Override
	protected void initialize() {
	}

}
