
public class Process extends Thread {
	
	private ProcessConfig p_config;
	
	public Process(ProcessConfig p_config) {
		this.p_config = p_config;
		if(p_config.is_root())
			p_config.set_dist(0);
	}
	
	@Override
	public void run() {
		for(int i = 0 ; i < p_config.nbrs().length ; i++)
			p_config.delayers()[i].start();
		
		if(p_config.is_root())
			flood(new Message(0, p_config.node_id(), false), -1);
		
		while(!p_config.is_terminate()) {
			Message msg;
			try {
				msg = p_config.receive().take();
				if(msg.is_termination_msg())
					handle_trm_msg(msg);
				else if(msg.is_ack())
					handle_ack_msg(msg);
				else
					handle_update_msg(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(p_config.is_root()) {
			for(int j = 0 ; j < p_config.parents().length ; j++)
				System.out.printf("#");
			System.out.println();
			System.out.println();
			for(int j = 0 ; j < p_config.parents().length ; j++) {
				for(int i = 0 ; i < p_config.parents().length ; i++) {
					if(i != j && ((p_config.parents()[i][0] == j && i!=p_config.node_id()) ||
							(p_config.parents()[j][0] == i && j!=p_config.node_id()) ))
						System.out.printf("1");
					else
						System.out.printf("0");
				}
				System.out.println();
			}
		}
		
	}

	private void handle_trm_msg(Message trm_msg) {
		p_config.set_is_terminate(true);
		flood(trm_msg, -1);
	}

	private void handle_ack_msg(Message msg) {
		p_config.acks()[get_index(msg.j())]--;
		
		if(msg.parents() != null)
			for(int i = 0 ; i < msg.parents().length ; i++) {
				if(p_config.parents()[i][1] < msg.parents()[i][1]) {
					p_config.parents()[i][0] = msg.parents()[i][0];
					p_config.parents()[i][1] = msg.parents()[i][1];
				}
			}
		
		boolean is_all_ack_received = true;
		for(int i = 0 ; i < p_config.acks().length ; i++)
			if(p_config.acks()[i] > 0)
				is_all_ack_received = false;
		
		if(is_all_ack_received)
			send_the_ack();
	}

	private void send_the_ack() {
		if(p_config.is_root()) {
			p_config.set_is_terminate(true);
			Message trm_msg = new Message(-1, -1, false);
			trm_msg.set_is_termination_msg();
			flood(trm_msg, -1);
			return;
		}
		try {
			if(p_config.parent() >= 0) {
				Message msg = new Message(Integer.MAX_VALUE, p_config.node_id(), true);
				p_config.update_parents();
				msg.set_parents(p_config.parents());
				p_config.send()[get_index(p_config.parent())].put(msg);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void handle_update_msg(Message msg) {
		try {
			int msg_m = msg.m();
			if(msg_m + 1 < p_config.dist()) {
				p_config.set_dist(msg_m + 1);
				if(p_config.parent() >= 0) {
					Message new_msg = new Message(Integer.MAX_VALUE, p_config.node_id(), true);
					p_config.send()[get_index(p_config.parent())].put(new_msg);
				}
				p_config.set_parent(msg.j());
				flood(new Message(p_config.dist(), p_config.node_id(), false), msg.j());
			}
			else
				p_config.send()[get_index(msg.j())].put(new Message(Integer.MAX_VALUE, p_config.node_id(), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void flood(Message msg, int except_id) {
		boolean is_any_neighbor = false;
		try {
			for(int k = 0 ; k < p_config.nbrs().length ; k++) {
				if(p_config.nbrs()[k] != except_id) {
					p_config.send()[k].put(msg);
					p_config.acks()[k]++;
					is_any_neighbor = true;
				}
			}
			if(!is_any_neighbor)
				send_the_ack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int get_index(int node_id) {
		for(int i = 0 ; i < p_config.nbrs().length ; i++)
			if(node_id == p_config.nbrs()[i])
				return i;
		return -1;
	}
}
