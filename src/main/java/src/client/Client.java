package src.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.sharedClass.AuctionItem;
import src.sharedClass.BidInfo;
import src.sharedClass.LoginInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Application {

  private static String host = "127.0.0.1";
  private BufferedReader fromServer;
  private PrintWriter toServer;
  private UIController UIcontroller;
  private Stage mainView;
  private Scene loginScene;
  private List<AuctionItem> receivedClosedBids;
  private List<AuctionItem> receivedPokemonList;

  // use blocking queues to send data to sever and to receive data if need to wait for input from server before continuing in the program
  private BlockingQueue<BidInfo> bidQueue = new LinkedBlockingQueue<>();

  private BlockingQueue<LoginInfo> loginQueue = new LinkedBlockingQueue<>();
  private BlockingQueue<Boolean> loginResponseQueue = new LinkedBlockingQueue<>();

  private BlockingQueue<LoginInfo> signUpQueue = new LinkedBlockingQueue<>();
  private BlockingQueue<Boolean>  signUpResponseQueue = new LinkedBlockingQueue<>();


  private String custmomerUsername;



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

      mainView = primaryStage;
      //Setting up login page now
      FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/LoginScreen.fxml"));
      Parent loginRoot = loginLoader.load();
      LoginScreenController loginController = loginLoader.getController();
      loginScene = new Scene(loginRoot);
      mainView.setScene(loginScene);
      mainView.show();

      loginController.setClient(this);
      loginController.setmainView(mainView);
      loginController.setPersonalScene(loginScene);
      // each controller should have a reference to the scene that they handle
      // each time you move from one controller to another store a reference to the previous controller
      // call the setscene of the previous controller to go back

      setUpNetworking();



    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setUpNetworking() throws Exception {
    try {
      Thread.sleep(10000);
      Socket socket = new Socket(host, 4242);
      System.out.println("Connecting to... " + socket);
      fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      toServer = new PrintWriter(socket.getOutputStream());

      Thread readerThread = new Thread(() -> {
        String input;
        try {
          while ((input = fromServer.readLine()) != null) {
            if (input.substring(0,1).equals("S")){
              System.out.println("signup attempt reached back to client");
              processSignUpResponse(Boolean.parseBoolean(input.substring(1)));
            }
            else if (input.equals("true") || input.equals("false")) {
              System.out.println("login attempt reached back to client");
              processLoginResponse(Boolean.parseBoolean(input));
            }
            else if (input.substring(0,1).equals("C")) {
              System.out.println("From server: " + input);
              Gson gson = new Gson();
              List<AuctionItem> receivedClosedBids = gson.fromJson(input.substring(1), new TypeToken<List<AuctionItem>>() {
              }.getType());
              processRequestClosedBids(receivedClosedBids);
            }
            else {
              System.out.println("From server: " + input);
              Gson gson = new Gson();
              List<AuctionItem> receivedPokemonList = gson.fromJson(input, new TypeToken<List<AuctionItem>>() {
              }.getType());
              processRequest(receivedPokemonList); // take json data from server and deserialize it back into a List of objects
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      Thread writerThread = new Thread(() -> {
        try {
          while (true) {
            BidInfo bidInfo = bidQueue.take(); // Blocking call until a bid is available
            System.out.println("bidQueue has been polled");
            Gson gson = new Gson();
            String itemData = gson.toJson(bidInfo, BidInfo.class);
            String messageToSend = "0" + itemData;
            sendToServer(messageToSend); // send most recent server data to client upon creation
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });

      // TODO: loginwriterThread
      Thread loginWriterThread = new Thread(() -> {
        try {
          while (true) {
            LoginInfo loginInfo = loginQueue.take(); // Blocking call until a bid is available
            System.out.println("loginQueue has been polled");
            Gson gson = new Gson();
            String itemData = gson.toJson(loginInfo, LoginInfo.class);
            String messageToSend = "1" + itemData;
            sendToServer(messageToSend); // send most recent server data to client upon creation
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });

      // TODO: signUpWriterThread
      Thread signUpWriterThread = new Thread(() -> {
        try {
          while (true) {
            LoginInfo loginInfo = signUpQueue.take(); // Blocking call until a bid is available
            System.out.println("signUpQueue has been polled");
            Gson gson = new Gson();
            String itemData = gson.toJson(loginInfo, LoginInfo.class);
            String messageToSend = "3" + itemData;
            sendToServer(messageToSend); // send most recent server data to client upon creation
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      signUpWriterThread.start();
      loginWriterThread.start();
      readerThread.start();
      writerThread.start();




    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void processRequestClosedBids(List<AuctionItem> receivedClosedBids) {
    this.receivedClosedBids = receivedClosedBids;
  }

  public List<AuctionItem> getReceivedClosedBids(){
    return this.receivedClosedBids;
  }
  public List<AuctionItem> getReceivedPokemonList(){
    return this.receivedPokemonList;
  }

  protected void processRequest(List<AuctionItem> receivedPokemonList) {
    // TODO: Add code to update UI components
    this.receivedPokemonList = receivedPokemonList;

    if (UIcontroller != null){
      System.out.println("UIcontroller not null");
      this.UIcontroller.updateGridPane(receivedPokemonList);
    }
  }

  protected void sendToServer(String string) {
    System.out.println("Sending to server: " + string);
    toServer.println(string);
    toServer.flush();
  }

  public void setController(UIController UIcontroller){
    this.UIcontroller = UIcontroller;
  }

  public void addToBidQueue(BidInfo bidInfo) {
    try {
      bidQueue.put(bidInfo); // Add bid info to the blocking queue
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  public void addToLoginQueue(LoginInfo loginInfo) {
    try {
      loginQueue.put(loginInfo); // Add bid info to the blocking queue
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  protected void processLoginResponse(boolean loginSuccessful) {
    try {
      loginResponseQueue.put(loginSuccessful);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public BlockingQueue<Boolean> getLoginResponseQueue() {
    return this.loginResponseQueue;
  }

  public void addToSignUpQueue(LoginInfo signUpInfo) {
    try {
      signUpQueue.put(signUpInfo); // Add bid info to the blocking queue
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  private void processSignUpResponse(boolean signUpSuccessful) {
    try {
      signUpResponseQueue.put(signUpSuccessful);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  public BlockingQueue<Boolean> getSignUpResponseQueue() {
    return signUpResponseQueue;
  }

  public void setUsername(String custmomerUsername){
    this.custmomerUsername = custmomerUsername;
  }
  public String getUsername() {
    return custmomerUsername;
  }

}
