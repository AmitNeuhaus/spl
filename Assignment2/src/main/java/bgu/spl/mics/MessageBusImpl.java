package bgu.spl.mics;

import bgu.spl.mics.application.objects.MicroServiceArray;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>> microserviceToQueueMap;
	private ConcurrentHashMap<Class<? extends Message>, MicroServiceArray<LinkedBlockingQueue<Message>>> messagesToMicroserviceMap;

	private static class MessageBusHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {
		microserviceToQueueMap = new ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>>();
		messagesToMicroserviceMap = new ConcurrentHashMap<Class<? extends Message>, MicroServiceArray<LinkedBlockingQueue<Message>>>();
		//TODO add events and arrays to messageToMicroserviceMap.
		Class<? extends Message> type = ExampleBroadcast.class;
		MicroServiceArray<LinkedBlockingQueue<Message>> msArray = new MicroServiceArray();
		messagesToMicroserviceMap.put(type, msArray);
		type = ExampleEvent.class;
		msArray = new MicroServiceArray();
		messagesToMicroserviceMap.put(type, msArray);

	}

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
		System.out.println("Trying to subscribe to event");
		if(isMicroServiceRegistered(m)){
			System.out.println("im in");
			LinkedBlockingQueue<Message> queue = microserviceToQueueMap.get(m);
			messagesToMicroserviceMap.get(type).getArray().add(queue);
			System.out.println("subscribed: "+ m.getName()+" to event");
		}else{
			System.out.println("not registered");
		}
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
		System.out.println("Trying to subscribe to broadcast");
		if (isMicroServiceRegistered(m)){
			System.out.println("im in");
			LinkedBlockingQueue<Message> queue = microserviceToQueueMap.get(m);
			messagesToMicroserviceMap.get(type).getArray().add(queue);
			System.out.println("subscribed: "+ m.getName()+" to broadcast ");
		}
		else{
			System.out.println("not registered");
		}

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
		e.getFuture().resolve(result);
	}

	/**
	 * @pre (listener : getBroadcastListeners(b)) listener.contains(b) == false
	 * @post (listener : getBroadcastListeners(b)) listener.contains(b) == true
	 *
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b)  {
		Class<? extends Message> type = b.getClass();
		CopyOnWriteArrayList<LinkedBlockingQueue<Message>> arr = messagesToMicroserviceMap.get(type).getArray();
		for (LinkedBlockingQueue<Message> microserviceQueue : arr) {
			try{
				microserviceQueue.put(b);
			}catch(Exception error){
				System.out.println("cant put in queue: " + error);
			}

		}


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
	public <T> Future<T> sendEvent(Event<T> e)  {
		Class<? extends Message> type = e.getClass();
		MicroServiceArray<LinkedBlockingQueue<Message>> msArray = messagesToMicroserviceMap.get(type);
		CopyOnWriteArrayList<LinkedBlockingQueue<Message>> arr = msArray.getArray();
		int index = msArray.getNextIndex();
		LinkedBlockingQueue<Message> queue = arr.get(index);
		try{
			queue.put(e);
		}catch(Exception error){
			System.out.println("cant put in queue: " + error);
		}


		return e.getFuture();
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
		if(!isMicroServiceRegistered(m)){
			LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
			microserviceToQueueMap.put(m,queue);
			System.out.println("registered: "+ m.getName());
		}
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
		if (isMicroServiceRegistered(m)){
			LinkedBlockingQueue<Message> queue = microserviceToQueueMap.get(m);
			for (MicroServiceArray<LinkedBlockingQueue<Message>> microserviceArray : messagesToMicroserviceMap.values()){
				microserviceArray.getArray().remove(queue);
			}
		}
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
		LinkedBlockingQueue<Message> queue = microserviceToQueueMap.get(m);
		if (queue!= null){
			Message msg = queue.take();
			return msg;
		}
		else{
			System.out.println("your Q in null you full");
		}
		return null;
	}



	//Queries:

	@Override
	public boolean isMicroServiceRegistered(MicroService m) {
		return microserviceToQueueMap.containsKey(m);
	}

	@Override
	public int getNumberOfMicroServices() {
		return microserviceToQueueMap.size();
	}

	@Override
	public <T> boolean isListeningToEvent(Class<? extends Event<T>> type, MicroService m) {
		LinkedBlockingQueue<Message> queue = microserviceToQueueMap.get(m);
		CopyOnWriteArrayList<LinkedBlockingQueue<Message>> arr = messagesToMicroserviceMap.get(type).getArray();
		return arr.contains(queue);
	}

	@Override
	public <T> int getNumOfEventListeners(Class<? extends Event<T>> type) {
		return messagesToMicroserviceMap.get(type).getArray().size();
	}

	@Override
	public  boolean isListeningToBroadcast(Class<? extends Broadcast> type, MicroService m) {
		LinkedBlockingQueue<Message> queue = microserviceToQueueMap.get(m);
		CopyOnWriteArrayList<LinkedBlockingQueue<Message>> arr = messagesToMicroserviceMap.get(type).getArray();
		return arr.contains(queue);
	}

	@Override
	public  int getNumOfBroadcastListeners(Class<? extends Broadcast> type) {
		return messagesToMicroserviceMap.get(type).getArray().size();
	}

	@Override
	public <T,E> Iterable getEventListeners(Class<? extends Event<T>> type) {
		return messagesToMicroserviceMap.get(type).getArray();
	}

	@Override
	public <E> Iterable getBroadcastListeners(Class<? extends Broadcast> type) {
		return messagesToMicroserviceMap.get(type).getArray();
	}

	@Override
	public int getMicroserviceQueueSize(MicroService m) {
		return microserviceToQueueMap.get(m).size();
	}


	public static MessageBusImpl getInstance(){
		return MessageBusHolder.instance;
	}



}



