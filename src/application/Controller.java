package application;

import java.util.concurrent.ArrayBlockingQueue;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class Controller extends Main {
	
	@FXML
	TextField hostInput;
	
	@FXML
	TextField portInput;
	
	@FXML
	TextArea textInput;
	
	@FXML
	TextArea incomingText;
	
	@FXML
	Button sendBTN;
	
	@FXML
	TextField PosX;
	
	@FXML
	TextField PosY;
	
	@FXML
	Rectangle airCraftCarrier;
	
	@FXML
	Rectangle battleShip;
	
	@FXML
	Rectangle submarine;
	
	@FXML
	Rectangle destroyer;
	
	@FXML
	Rectangle patrolBoat;
	
	@FXML
	GridPane myGrid; 
	
	/*@FXML
	Button ClearGame;*/
	
	private ArrayBlockingQueue<String> channel;
	private TalkThread talker;
	private SocketEchoThread sockets;
	ObservableList <String> line = FXCollections.observableArrayList();
	String message;
	
	/*@FXML
	public void newGame() {
		submarine.setTranslateX(53);
		submarine.setTranslateY(410);
		
		airCraftCarrier.setLayoutX(98);
		airCraftCarrier.setLayoutY(394);
		
		destroyer.setLayoutX(10);
		destroyer.setLayoutY(410);
		
		patrolBoat.setLayoutX(17);
		patrolBoat.setLayoutY(504);
		
		battleShip.setLayoutX(4);
		battleShip.setLayoutY(555);
		
		incomingText.clear();
		
	}*/
	
	
	@FXML
	public void initialize(){
	}
	
	
	@FXML
	public void ShipSelecter(){
		try{
			
		
		airCraftCarrier.setOnMouseClicked (event -> { 
			int X = Integer.parseInt(PosX.getText());
			int Y = Integer.parseInt(PosY.getText());
			myGrid.add(airCraftCarrier, X, Y);
			airCraftCarrier.toBack();
			PosX.clear();
			PosY.clear();
			});
		
		destroyer.setOnMouseClicked (event -> {
			int X = Integer.parseInt(PosX.getText());
			int Y = Integer.parseInt(PosY.getText());
			myGrid.add(destroyer, X, Y);
			destroyer.toBack();
			PosX.clear();
			PosY.clear();
			});
		
		battleShip.setOnMouseClicked (event -> {
			int X = Integer.parseInt(PosX.getText());
			int Y = Integer.parseInt(PosY.getText());
			myGrid.add(battleShip, X, Y);
			battleShip.toBack();
			PosX.clear();
			PosY.clear();
			});
		
		submarine.setOnMouseClicked (event -> {
			int X = Integer.parseInt(PosX.getText());
			int Y = Integer.parseInt(PosY.getText());
			myGrid.add(submarine, X, Y);
			submarine.toBack();
			PosX.clear();
			PosY.clear();
			});
		
		patrolBoat.setOnMouseClicked (event -> {
			int X = Integer.parseInt(PosX.getText());
			int Y = Integer.parseInt(PosY.getText());
			myGrid.add(patrolBoat, X, Y);
			patrolBoat.toBack();
			PosX.clear();
			PosY.clear();
			});
		} catch (NumberFormatException e) {
			incomingText.setText(incomingText.getText() + "\n" + "Remember to select correct coordinates.");
		}
	}
	
	public void setSockets(SocketEchoThread sockets){
		this.sockets = sockets;
	}
	@FXML
	public void sendMessage(){
		
				send(textInput.getText(), hostInput.getText(), Integer.parseInt(portInput.getText()));	
				incomingText.setText(incomingText.getText() + "\n" + textInput.getText());
				textInput.clear();
				
	}
	
	private void send(String msg, String host, int port) {
		
		channel = new ArrayBlockingQueue<String>(2, true);
	
		if (talker != null  && talker.isGoing()) {
			talker.halt();
		}
		talker = new TalkThread(msg, host, port, channel);
		talker.start();
		
	}
	
	
	private class Receiver extends Thread {
		

		private SocketEchoThread sockets;
		
		
		public Receiver(SocketEchoThread sockets){
			this.sockets = sockets;
		}
		
		public void run() {

					 message =  sockets.getString();
					System.out.println("Hello" + "\n");
				
					if (message.length() > 0){
						line.add(message);
						System.out.println("This is line:" + line + "\n");
				
						Platform.runLater(() -> {
							incomingText.setText(incomingText.getText() + "\n" + line);
						});
					}
		}
	}


	
	@FXML
	public void updateText(){
		System.out.println(sockets.isGoing());
		
		new Receiver(sockets).start();
		
		line.clear();
		message = "";
	}
	
	
}
