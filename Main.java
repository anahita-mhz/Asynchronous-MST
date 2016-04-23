import java.io.File;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
	public static void main(String[] args) {
		Config config = read_config();
		
		for(int i = 0 ; i < config.n() ; i++) {
			ProcessConfig p_config = mask_config(config, i);
			Process process = new Process(p_config);
			process.start();
		}
	}

	private static ProcessConfig mask_config(Config config, int node_id) {
		ProcessConfig p_config = new ProcessConfig();
		
		p_config.set_parents(new int[config.n()][2]);
		p_config.set_node_id(node_id);
		p_config.set_is_root((node_id == config.x() ? true : false));
		p_config.set_parent(-1);
		p_config.set_dist(Integer.MAX_VALUE);
		
		int num_of_nbrs = 0;
		for(int i = 0 ; i < config.n() ; i++) {
			if(config.connectivity()[i][node_id])
				num_of_nbrs++;
		}
		
		p_config.set_send(new LinkedBlockingQueue[num_of_nbrs]);
		p_config.set_delayers(new Delayer[num_of_nbrs]);
		p_config.set_nbrs(new int[num_of_nbrs]);
		
		for(int j = 0, i = 0 ; i < config.n() ; i++) {
			if(config.connectivity()[i][node_id]) {
				p_config.nbrs()[j] = i;
				p_config.send()[j] = new LinkedBlockingQueue<Message>();
				p_config.delayers()[j] = new Delayer(p_config.send()[j], config.delayed_queues()[i]);
				j++;
			}
		}
		
		p_config.set_receive(config.delayed_queues()[node_id]);
		p_config.set_acks(new int[p_config.nbrs().length]);
		p_config.set_is_terminate(false);
		return p_config;
	}

	private static Config read_config() {
		Scanner sc = null;
		Config config = new Config();
		try {
			sc = new Scanner(new File("connectivity.txt"));
			String n_comma = sc.next();
			config.set_n(Integer.parseInt(n_comma.substring(0, n_comma.length()-1)));
			config.set_x(sc.nextInt() - 1);
			config.set_connectivity(new boolean[config.n()][config.n()]);
			config.set_delayed_queues(new LinkedBlockingQueue[config.n()]);
			
			for(int i = 0 ; i < config.n() ; i++) {
				config.delayed_queues()[i] = new LinkedBlockingQueue<Message>();
				for(int j = 0 ; j < config.n() ; j++)
					config.connectivity()[i][j] = (sc.nextInt() == 1 ? true : false);
			}
			return config;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(sc != null)
				sc.close();
		}
		return config;
	}
}
