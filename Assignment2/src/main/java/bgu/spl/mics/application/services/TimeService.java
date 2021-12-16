package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.FreeGpuBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.SystemConstructor;

import java.io.IOException;
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
	boolean finished;
	int speed;
	Timer timer;
	SystemConstructor system;

	public TimeService(int duration, int speed, SystemConstructor system) {
		super("Time Service");
		currentTick = 1;
		finished = false;
		terminateTick = duration;
		timer = new Timer();
		this.speed = speed;
		this.system = system;
		timer.schedule(new IncrementTick(), 0, speed); // schedule the task
	}

	public TimeService(int duration, int speed) {
		super("Time Service");
		currentTick = 1;
		finished = false;
		terminateTick = duration;
		timer = new Timer();
		this.speed = speed;
		timer.schedule(new IncrementTick(), 0, speed); // schedule the task
	}

	class IncrementTick extends TimerTask {
		public void run() {
			if (currentTick == 1){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendBroadcast(new FreeGpuBroadcast());
				System.out.println("Time service sent start broadcast");
			}
			currentTick++;
			System.out.println(currentTick);
			if (currentTick == terminateTick) {
				System.out.println("Terminating Time service ");
				timer.cancel();
				system.terminateSystem();
				terminate();
			}
			sendBroadcast(new TickBroadcast(currentTick));
		}
	}

	//TODO add terminateAll function and constructor field.

	@Override
	protected void initialize() {

	}
}
