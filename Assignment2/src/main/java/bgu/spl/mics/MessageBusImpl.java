package bgu.spl.mics;

import com.sun.tools.javac.comp.Todo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {

	private Collection<Queue> queues;
	private Collection<Event> eventsMapping;

	/**
	 * @pre  eventsMapping.get(type).contains(m) == false
	 * @post eventsMapping.get(type).size() == @pre eventsMapping.get(type).size +1
	 * @post eventsMapping.get(type).contains(m) == true;
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @param <T>
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre  eventsMapping.get(type).contains(m) == false
	 * @post events.get(type).size == @pre events.get(type).size +1
	 * @post events.get(type).contains(m) == true;
	 * @param type 	The type to subscribe to.
	 * @param m    	The subscribing micro-service.
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * e.type == result.type
	 * e != null
	 * result !=null
	 * @param e      The completed event.
	 * @param result The resolved result of the completed event.
	 * @param <T>
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre b!=null
	 * @post for(MicroService : eventsMapping.get(b)){queues.get(MicroService).size() == @pre queues.get(MicroService).size() + 1
	 * 											      queues.get(MicroService).contains(b)}
	 *
	 * @param b 	The message to added to the queues.
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	/**
	 * @post events.get(e)[0].size() == @pre events.get(e)[0].size()+1
	 * @post events.get(e)[0].contains(e)
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
	 * @pre m!=null
	 * @pre queues.contain(m) == false
	 * @post queues.contain(m) == true
	 * @post qoeoes.siz() == @pre queues.size+1
	 *
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void register(MicroService m) {
		// TODO Auto-generated method stub

	}
	/**
	 * @pre m != null
	 * @pre queues.contain(m) ==  true
	 * @post queues.contain(m) == false
	 * @post qoeoes.size() == @pre queues.size-1
	 * @post
	 *
	 * @param m the micro-service to create a queue for.
	 */
	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre if(queues.get(m).isEmpty()){@post m.getStatus == TIMED_WAITING }
	 * 		else{@return == queues.get(m).top()
	 * 			@post queues.get(m).size() == @pre queues.get(m).size()-1;}
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

	

}
