package bgu.spl.mics;




import java.util.Collection;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {

	protected Collection<Queue> queues;
	protected Collection<Event> eventsMapping;

	/**
	 * @pre  isListeningToEvent(e) == false
	 * @post getNumOfEventListeners(e) == @pre getNumOfEventListeners(e) +1
	 * @post isListeningToEvent(e) == false
	 *
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @param <T>
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre  isListeningToBroadcast(b) == false
	 * @post getNumOfBroadcastListeners(b) == @pre getNumOfBroadcastListeners(b) +1
	 * @post isListeningToEvent(e) == false
	 *
	 * @param type 	The type to subscribe to.
	 * @param m    	The subscribing micro-service.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param e      The completed event.
	 * @param result The resolved result of the completed event.
	 *
	 * @param <T>
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre (listener : getBroadcastListeners(b)) listener.contains(b) == false
	 * @post (listener : getBroadcastListeners(b)) listener.contains(b) == true
	 *
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre  (listener : getEventListeners(e)) listener.contains(e) == false
	 * @post getEventListeners(e)[0].contains(e) == true
	 * @param e     	The event to add to the queue.
	 * @param <T>
	 * @return
	 */
	//Todo add future test - not sure how it used.
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @pre  isRegistered(m) == false
	 * @post isRegistered(m)  == true
	 * @post getNumberOfMicroServices() == @pre getNumberOfMicroServices() + 1
	 *
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void register(MicroService m) {
		// TODO Auto-generated method stub

	}
	/**

	 * @pre isRegistered(m)  == true
	 * @post isRegistered(m) == false
	 * @post getNumberOfMicroServices() == @pre getNumberOfMicroServices() - 1
	 *
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param m The micro-service requesting to take a message from its message
	 *          queue.
	 * @return
	 * @throws InterruptedException
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}



	//Queries:

	@Override
	public boolean isMicroServiceRegistered(MicroService m) {
		return false;
	}

	@Override
	public int getNumberOfMicroServices() {
		return 0;
	}

	@Override
	public <T> boolean isListeningToEvent(Class<? extends Event<T>> type, MicroService m) {
		return false;
	}

	@Override
	public <T> int getNumOfEventListeners(Class<? extends Event<T>> type) {
		return 0;
	}

	@Override
	public  boolean isListeningToBroadcast(Class<? extends Broadcast> type, MicroService m) {
		return false;
	}

	@Override
	public  int getNumOfBroadcastListeners(Class<? extends Broadcast> type) {
		return 0;
	}

	@Override
	public <T,E> Iterable<Queue<E>> getEventListeners(Class<? extends Event<T>> type) {
		return null;
	}

	@Override
	public <E> Iterable<Queue<E>> getBroadcastListeners(Class<? extends Broadcast> type) {
		return null;
	}

	@Override
	public int getMicroserviceQueueSize(MicroService m) {
		return 0;
	}



}



