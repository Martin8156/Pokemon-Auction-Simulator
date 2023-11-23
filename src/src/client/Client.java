package src.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.sharedClass.AuctionItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client extends Application {

  private static String host = "127.0.0.1";
  private BufferedReader fromServer;
  private PrintWriter toServer;
  private Scanner consoleInput = new Scanner(System.in);
  private UIController controller;
  private Parent root;

  public static void main(String[] args) {
    try {
      launch(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("AuctionUI.fxml"));
      root = loader.load();
      this.controller = loader.getController(); // Get the controller instance from the loader
      controller.setClient(this); // Pass a reference of the client to the controller

      Scene scene = new Scene(root);
      primaryStage.setTitle("Pokemon Bidding");
      primaryStage.setScene(scene);
      primaryStage.show();

      setUpNetworking();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setUpNetworking() throws Exception {
    try {
      Socket socket = new Socket(host, 4242);
      System.out.println("Connecting to... " + socket);
      fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      toServer = new PrintWriter(socket.getOutputStream());

      Thread readerThread = new Thread(() -> {
        String input;
        try {
          while ((input = fromServer.readLine()) != null) {
            System.out.println("From server: " + input);
            Gson gson = new Gson();
            List<AuctionItem> receivedPokemonList = gson.fromJson(input, new TypeToken<List<AuctionItem>>() {
            }.getType());
            processRequest(receivedPokemonList); // take json data from server and deserialize it back into a List of objects
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      Thread writerThread = new Thread(() -> {
        while (true) {
          // System.out.println("From client:... updated info");
        }
      });

      readerThread.start();
      writerThread.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void processRequest(List<AuctionItem> receivedPokemonList) {
    // TODO: Add code to update UI components
    this.controller.updateGridPane(receivedPokemonList);
  }

  protected void sendToServer(String string) {
    System.out.println("Sending to server: " + string);
    toServer.println(string);
    toServer.flush();
  }
}
