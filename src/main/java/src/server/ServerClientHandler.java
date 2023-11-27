package src.server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import src.sharedClass.AuctionItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

class ServerClientHandler implements Runnable, Observer {

  private Server server;
  private Socket clientSocket;
  private BufferedReader fromClient;
  private PrintWriter toClient;

  @FXML
  private GridPane displayPane;

  protected ServerClientHandler(Server server, Socket clientSocket) {
    this.server = server;
    this.clientSocket = clientSocket;
    try {
      fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
      toClient = new PrintWriter(this.clientSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  @Override
  public void run() {
    String input;
    try {
      while ((input = fromClient.readLine()) != null) {
        System.out.println("From client: " + input);

        String messageType = input.substring(0, 1);
        // Use a switch statement to process the message type
        switch (messageType) {
          case "0":
            server.processBidRequest(input.substring(1));
            break;
          case "1":
            server.processLoginRequest(input.substring(1));
            break;
          case "2":
            server.getItemData();
            break;
          case "3":
            server.processSignUpRequest(input.substring(1));
            break;
          default:



            // Handle unknown message type
            System.out.println("Undefined message type: " + messageType);
            break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void update(Observable o, Object arg) {
    Gson gson = new Gson();
    String itemData = gson.toJson(server.getAuctionItems(), new TypeToken<List<AuctionItem>>() {}.getType());
    this.sendToClient(itemData);

    Gson gson2 = new Gson();
    String closedBids = "C" + gson2.toJson(server.getClosedBids(), new TypeToken<List<AuctionItem>>() {}.getType());
    this.sendToClient(closedBids);
  } // override method of parent class so that updated info from server is sent to all clients

  protected void sendToClient(String string) {
    System.out.println("Sending to client: " + string);
    toClient.println(string);
    toClient.flush();
  }



}
