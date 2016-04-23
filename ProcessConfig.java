import java.util.concurrent.BlockingQueue;

public class ProcessConfig {
	
	private int node_id;
	private boolean is_root;
	private int parent;
	private int dist;
	private int nbrs[];						// the id of the neighbors
	private BlockingQueue<Message> send[];  // some queues for sending message to neighbors
	private Delayer delayers[];				
	private BlockingQueue<Message> receive; // queue for incoming messages
	
	private int[] acks;
	private boolean is_terminate;
	private int parents[][];
	
	public int[][] parents() {
		return parents;
	}

	public void set_parents(int[][] parents) {
		this.parents = parents;
	}

	public void update_parents() {
		parents[node_id][1]++;
		parents[node_id][0] = parent;
	}

	public Delayer[] delayers() {
		return delayers;
	}

	public void set_delayers(Delayer[] delayers) {
		this.delayers = delayers;
	}

	public boolean is_terminate() {
		return is_terminate;
	}

	public void set_is_terminate(boolean is_terminate) {
		this.is_terminate = is_terminate;
	}

	public int[] acks() {
		return acks;
	}

	public void set_acks(int[] acks) {
		this.acks = acks;
	}

	public int node_id() {
		return node_id;
	}

	public void set_node_id(int node_id) {
		this.node_id = node_id;
	}
	
	public BlockingQueue<Message> receive() {
		return receive;
	}

	public void set_receive(BlockingQueue<Message> my_queue) {
		this.receive = my_queue;
	}

	public BlockingQueue<Message>[] send() {
		return send;
	}

	public void set_send(BlockingQueue<Message>[] send) {
		this.send = send;
	}

	public int dist() {
		return dist;
	}

	public void set_dist(int dist) {
		this.dist = dist;
	}

	public int[] nbrs() {
		return nbrs;
	}

	public void set_nbrs(int[] nbrs) {
		this.nbrs = nbrs;
	}

	public boolean is_root() {
		return is_root;
	}

	public void set_is_root(boolean is_root) {
		this.is_root = is_root;
	}

	public int parent() {
		return parent;
	}

	public void set_parent(int parent) {
		this.parent = parent;
	}
}