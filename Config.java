import java.util.concurrent.BlockingQueue;

public class Config {
	
	private int n;
	private int x;
	private boolean[][] connectivity;
	private BlockingQueue<Message> delayed_queues[];

	public BlockingQueue<Message>[] delayed_queues() {
		return delayed_queues;
	}

	public void set_delayed_queues(BlockingQueue<Message>[] delayed_queues) {
		this.delayed_queues = delayed_queues;
	}

	public boolean[][] connectivity() {
		return connectivity;
	}
	
	public void set_connectivity(boolean[][] connectivity) {
		this.connectivity = connectivity;
	}

	public int n() {
		return n;
	}

	public void set_n(int num_of_nodes) {
		this.n = num_of_nodes;
	}

	public int x() {
		return x;
	}

	public void set_x(int x) {
		this.x = x;
	}	
}
