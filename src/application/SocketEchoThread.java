package application;


import java.net.*;
import java.io.*;

public class SocketEchoThread extends Thread {
    private Socket socket;
    StringBuilder sb = new StringBuilder();
    private boolean going;

    
    public SocketEchoThread(Socket socket, Main gui) {
        this.socket = socket;
        going = true;
    }
    
    public boolean isGoing() {
		return going;
	}

    public void run() {
        try {
            BufferedReader responses = 
                new BufferedReader
                (new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("Connection open.");
            writer.println("I will echo a single message, then close, whenver i feel like it.");


            while (!responses.ready()){}
            while (responses.ready()) {
                sb.append(responses.readLine());
            }
            System.out.println("From: " + socket.getInetAddress() + ": " + sb);

            getString();
             
            writer.print(sb + "wagwan");
            writer.flush();
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } 
    }
    
    public synchronized void halt() {
		going = false;
	}

    
    public String getString(){
    	return sb.toString();
    }

}
