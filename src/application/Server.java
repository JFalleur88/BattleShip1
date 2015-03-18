package application;

import java.net.*;
import java.io.*;

public class Server extends Thread{
	
	private ServerSocket accepter;
	private Main gui;
	
	
	public Server(int port, Main main) throws IOException {
		this.gui = main;
	}

	

	public void listen() throws IOException {
		for (;;) {
			Socket s = accepter.accept();
			SocketEchoThread echoer = new SocketEchoThread(s, gui);
			echoer.start();
			System.out.println(echoer);
			gui.sendSocket(echoer);
			System.out.println("Connection accepted from " + s.getInetAddress());
		}
	}
	public void run(){
		try {
			accepter = new ServerSocket();
			accepter.bind(new InetSocketAddress(8888));
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			accepter.setReuseAddress(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}