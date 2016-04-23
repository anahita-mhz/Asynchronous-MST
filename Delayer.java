import java.util.concurrent.BlockingQueue;

public class Delayer extends Thread {

	BlockingQueue<Message> queue;
	BlockingQueue<Message> delayed_queue;
	
	public Delayer(BlockingQueue<Message> queue,
			BlockingQueue<Message> delayed_queue) {
		this.queue = queue;
		this.delayed_queue = delayed_queue;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				long cur_timestamp = System.currentTimeMillis();
				Message msg = queue.take();
				long rand = ((long) (Math.random() * 15) + 1)*1000;
				if(msg.is_termination_msg()) {
					delayed_queue.put(msg);
					return;
				}
				if(msg.is_ack()) {
					delayed_queue.put(msg);
					continue;
				}
				if(msg.timestamp() < cur_timestamp) {
					rand = (rand + msg.timestamp()) - (cur_timestamp);
				}
				if(rand > 0)
					sleep(rand);
				delayed_queue.put(msg);
			}  catch (InterruptedException e) {
				break;
			} catch (Exception e) {
				System.out.println(e + " in Delayer run().");
			}
		}
	}
}
