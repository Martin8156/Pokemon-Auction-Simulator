package src.server;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import src.sharedClass.AuctionItem;

class Server extends Observable {
  private List<AuctionItem> auctionItems;


  public static void main(String[] args) {
    new Server().runServer();
  }

  private void runServer() {
    try {
      loadItemData();
      setUpNetworking();
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }


  private void setUpNetworking() throws Exception {
    @SuppressWarnings("resource")
    ServerSocket serverSock = new ServerSocket(4242);
    while (true) {
      Socket clientSocket = serverSock.accept();
      System.out.println("Connecting to... " + clientSocket);

      ClientHandler handler = new ClientHandler(this, clientSocket);
      this.addObserver(handler); // adds to a list inside of Observable parent class

      Thread t = new Thread(handler);
      t.start();

      Gson gson = new Gson();
      String itemData = gson.toJson(auctionItems, new TypeToken<List<AuctionItem>>() {}.getType());
      handler.sendToClient(itemData); // send most recent server data to client upon creation
    }
  }

  protected void processRequest(String input) { // need to change this in relation to project requirements
    // TODO:
        String output = "Error";
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        try {
          String temp = "";
          switch (message.type) {
            case "upper":
              temp = message.input.toUpperCase();
              break;
            case "lower":
              temp = message.input.toLowerCase();
              break;
            case "strip":
              temp = message.input.replace(" ", "");
              break;
          }
          output = "";
          for (int i = 0; i < message.number; i++) {
            output += temp;
            output += " ";
//          }


     // TODO: ^^
    // Send item data to the client

          }

      this.setChanged();
      this.notifyObservers(output); // updates all clienthandlers of the change made on the server
    } catch (Exception e) {
      e.printStackTrace();
    }
  }




  private void loadItemData() {
    // Load item data from a JSON file using Gson
    try (BufferedReader reader = new BufferedReader(new FileReader("/Users/brian/IdeaProjects/fa-23-final-project-Martin8156/src/src/server/pokemon_cards.json"))) {
      System.out.println("Item Loaded on Server Sucessfully...");
      Gson gson = new Gson();
      auctionItems = gson.fromJson(reader, new TypeToken<List<AuctionItem>>() {}.getType());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  protected String getItemData() {
    Gson gson = new Gson();
    return gson.toJson(auctionItems, new TypeToken<List<AuctionItem>>() {}.getType());
  }
}