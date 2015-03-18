package application;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class TalkThread extends Thread {
	String msg;
	private String host;
	private int port;
	private ArrayBlockingQueue<String> channel;
	private boolean going;
	
	public TalkThread(String msg, String host, int port, ArrayBlockingQueue<String> channel) {
		this.msg = msg.endsWith("\n") ? msg : msg + "\n";
		this.host = host;
		this.port = port;
		this.channel = channel;
		going = true;
	}
	
	public boolean isGoing() {
		return going;
	}

	@Override
	public void run() {
		Socket socket = null;
		channel = new ArrayBlockingQueue<String>(2, true);
		try {
			socket = new Socket(host, port);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println(msg);
			writer.flush();

			BufferedReader responses = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (going) {
				while (going && !responses.ready());
				String line = responses.readLine();
				if (line != null) {channel.put(line);}
			}
			going = false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            try {
                if (socket != null) {socket.close();}
            } catch (IOException ioe) {
                System.out.println("error closing socket");
            }
        }		
	}
	
	public synchronized void halt() {
		going = false;
	}

	public ArrayBlockingQueue<String> getChannel(){
		return channel;
	}
}