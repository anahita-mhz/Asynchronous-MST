public class Message {
	private int m; // the dist notion of the message (can be used to determine if an update is needed)
	private int j; // the id of the sender
	private boolean is_ack;
	private long timestamp;
	private boolean is_termination_msg;
	
	private int parents[][];
	
	public Message(int dist, int node_id, boolean is_ack) {
		super();
		this.m = dist;
		this.j = node_id;
		this.is_ack = is_ack;
		this.timestamp = System.currentTimeMillis();
	}

	public int[][] parents() {
		return parents;
	}

	public void set_parents(int[][] parents) {
		this.parents = parents;
	}

	public boolean is_termination_msg() {
		return is_termination_msg;
	}

	public void set_is_termination_msg() {
		this.is_termination_msg = true;
	}

	public long timestamp() {
		return timestamp;
	}

	public int m() {
		return m;
	}
	
	public int j() {
		return j;
	}

	public boolean is_ack() {
		return is_ack;
	}
}
