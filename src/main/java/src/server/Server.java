package src.server;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import src.sharedClass.AuctionItem;
import src.sharedClass.BidInfo;
import src.sharedClass.LoginInfo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

class Server extends Observable {
  private List<AuctionItem> auctionItems;
  private List<AuctionItem> closedBids;

  private ServerClientHandler handler;

  private static MongoClient client;
  private static MongoDatabase db;
  private static MongoCollection<Document> pokemonCol;
  private static MongoCollection<Document> loginCol;
  private static MongoCollection<Document> closedCol;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  private boolean scheduleFlag;





  public static void main(String[] args) {
    client = MongoClients.create(" add here ! ");
    db = client.getDatabase("AuctionServerResources");
    pokemonCol = db.getCollection("Available Pokemon");
    loginCol = db.getCollection("CustomerLoginInfo");
    closedCol = db.getCollection("Closed Bids");



    /* sample code
    Document sampleDoc = new Document("_id", "1").append("name", "John Smith");
    col.insertOne(sampleDoc);
    */


    Server server = new Server();
    server.runServer();

  }

  private void runServer() {
    try {
      scheduleFlag = false;
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
      Socket clientSocket = serverSock.accept(); // continusously establish connections between client and server
      System.out.println("Connecting to... " + clientSocket);

      handler = new ServerClientHandler(this, clientSocket);
      this.addObserver(handler); // adds to a list inside of Observable parent class

      Thread t = new Thread(handler);
      t.start();
    }
  }

  protected void processBidRequest(String input) {
    // TODO: bid placement
    System.out.println("arrived at server.." + input);
    // change data in database
    boolean databaseUpdateSuccess = updateDatabase(input);
    // then create a new list based off of the data from database
    if (databaseUpdateSuccess) {
      auctionItems = retrieveUpdatedDataFromDatabase();
      closedBids = retrieveUpdatedDataFromDatabaseClosedBids();
    }
    else {
      System.out.println("Failed to update the database.");
    }
    // now notify all clients about updated data by sending updated pokemonList
    try {
      this.setChanged();
      this.notifyObservers(); // invokes update on all clients so they get the most recent version of the list
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private boolean updateDatabase(String input) {
    try {
      Gson gson = new Gson();
      BidInfo bidInfo = gson.fromJson(input, BidInfo.class);

      // Get the document you want to update based on pokemonName
          Document filter = new Document("name", bidInfo.getPokemonName());
          Document pokemon = pokemonCol.find(filter).first(); // get the document from pokemon collection

      // Check if the bid is above the buy now price
          if (pokemon != null && bidInfo.getBidAmount() >= pokemon.getDouble("buyNowPrice")) {
            Document update = new Document("$set", new Document("bidOpen", false));
            pokemonCol.updateOne(filter, update);
          }

      // Update the bid information with new bid amount
          Document update = new Document("$set", new Document("minimumBid", bidInfo.getBidAmount()));
          pokemonCol.updateOne(filter, update);

      // Update bid history
      Document bidInfoDocument = new Document("customerUsername", bidInfo.getCustomerUsername())
              .append("pokemon", bidInfo.getPokemonName())
              .append("currentBid", bidInfo.getBidAmount());

      Document bidHistoryUpdate = new Document("$push", new Document("bidHistory", bidInfoDocument));
      pokemonCol.updateOne(filter, bidHistoryUpdate);



      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }



  private List<AuctionItem> retrieveUpdatedDataFromDatabase() {
    List<AuctionItem> updatedData = new ArrayList<>();

    try {
      // Iterate through the documents in the collection
      for (Document doc : pokemonCol.find()) {
        String id = doc.getString("_id");
        String name = doc.getString("name");
        String description = doc.getString("description");
        int timeRemaining = doc.getInteger("timeRemaining");
        double minBid = doc.getDouble("minimumBid");
        double buyNowPrice = doc.getDouble("buyNowPrice");
        boolean bidOpen = doc.getBoolean("bidOpen");

        // Retrieve bidHistory as a list of documents
        List<Document> bidHistoryDocuments = doc.getList("bidHistory", Document.class);
        // Convert bidHistoryDocuments to a List<BidInfo>
        List<BidInfo> bidHistory = new ArrayList<>();
        for (Document bidDoc : bidHistoryDocuments) {
          String customerUsername = bidDoc.getString("customerUsername");
          String pokemonName = bidDoc.getString("pokemon");
          double bidAmount = bidDoc.getDouble("currentBid");
          BidInfo bidInfo = new BidInfo(customerUsername, pokemonName, bidAmount);
          bidHistory.add(bidInfo);
        }

        // check if bid is open
        if(bidOpen) {
          updatedData.add(new AuctionItem(id, name, description, timeRemaining, minBid, buyNowPrice, bidOpen, bidHistory));

        }
        else{ // if not add to closed bid database
          Document documentToMove = pokemonCol.find(Filters.eq("_id", doc.getString("_id"))).first();
          closedCol.insertOne(documentToMove); // insert into closed
          pokemonCol.findOneAndDelete(Filters.eq("_id", doc.getString("_id"))); // now delete it from original collection
        }
        // Create and add an AuctionItem to the list

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return updatedData;
  }

  private List<AuctionItem> retrieveUpdatedDataFromDatabaseClosedBids() {
    List<AuctionItem> updatedData = new ArrayList<>();

    try {
      // Iterate through the documents in the collection
      for (Document doc : closedCol.find()) {
        String id = doc.getString("_id");
        String name = doc.getString("name");
        String description = doc.getString("description");
        int timeRemaining = doc.getInteger("timeRemaining");
        double minBid = doc.getDouble("minimumBid");
        double buyNowPrice = doc.getDouble("buyNowPrice");
        boolean bidOpen = doc.getBoolean("bidOpen");

        // Retrieve bidHistory as a list of documents
        List<Document> bidHistoryDocuments = doc.getList("bidHistory", Document.class);
        // Convert bidHistoryDocuments to a List<BidInfo>
        List<BidInfo> bidHistory = new ArrayList<>();
        for (Document bidDoc : bidHistoryDocuments) {
          String customerUsername = bidDoc.getString("customerUsername");
          String pokemonName = bidDoc.getString("pokemon");
          double bidAmount = bidDoc.getDouble("currentBid");
          BidInfo bidInfo = new BidInfo(customerUsername, pokemonName, bidAmount);
          bidHistory.add(bidInfo);
        }
        updatedData.add(new AuctionItem(id, name, description, timeRemaining, minBid, buyNowPrice, bidOpen, bidHistory));
//        // check if bid is open
//        if(bidOpen) {
//          updatedData.add(new AuctionItem(id, name, description, minBid, buyNowPrice, bidOpen, bidHistory));
//
//        }
//        else{ // if not add to closed bid database
//          Document documentToMove = pokemonCol.find(Filters.eq("_id", doc.getString("_id"))).first();
//          closedCol.insertOne(documentToMove); // insert into closed
//          pokemonCol.findOneAndDelete(Filters.eq("_id", doc.getString("_id"))); // now delete it from original collection
//        }
//        // Create and add an AuctionItem to the list

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return updatedData;
  }



  private void loadItemData() {
    // Load item data from a JSON file using Gson
    try (BufferedReader reader = new BufferedReader(new FileReader(" add here ! "))) { // note: JSON files of pokemon pictures were stored locally
      System.out.println("Item Loaded on Server Sucessfully...");
      Gson gson = new Gson();
      auctionItems = gson.fromJson(reader, new TypeToken<List<AuctionItem>>() {}.getType());

      // trying mongodb stuff here
      // Iterate through auctionItems and add each item to the MongoDB collection
      for (AuctionItem item : auctionItems) {
        item.newBidHistory();
        // Check if the document already exists in the collection
        // Will be the case because I am loading data from a JSON file upon start up and then sending that to the MongoDB client
        if (pokemonCol.find(eq("_id", item.getID())).first() == null) {
          Document itemDocument = new Document("_id", item.getID())
                  .append("name", item.getName())
                  .append("description", item.getDescription())
                  .append("timeRemaining", item.getTimeRemaining())
                  .append("minimumBid", item.getMinBid())
                  .append("buyNowPrice", item.getBuyNowPrice())
                  .append("bidOpen", item.getBidStatus());

          // Convert bid history to a list of Documents
          List<Document> bidHistoryDocuments = new ArrayList<>();
          for (BidInfo bidInfo : item.getBidHistory()) {
            Document bidInfoDocument = new Document("customerUsername", bidInfo.getCustomerUsername())
                    .append("pokemon", bidInfo.getPokemonName())
                    .append("currentBid", bidInfo.getBidAmount());
                    bidHistoryDocuments.add(bidInfoDocument);
          }

          // Append bid history to the itemDocument
          itemDocument.append("bidHistory", bidHistoryDocuments);

          System.out.println("data not found in db, inserting data now");
          pokemonCol.insertOne(itemDocument);
        }
        else {
          System.out.println("Document with _id " + item.getID() + " already exists. Skipping...");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

//  private boolean doesDocumentExist(MongoCollection<Document> collection, String id) {
//    return pokemonCol.find(eq("_id", id)).first() != null;
//  }

  protected void getItemData() {
    // Fetch data from the MongoDB collection
    // need to make connection first and login before doing this
      auctionItems = retrieveUpdatedDataFromDatabase();
      Gson gson = new Gson();
      String itemData = gson.toJson(auctionItems, new TypeToken<List<AuctionItem>>() {}.getType());
      handler.sendToClient(itemData);

      closedBids = retrieveUpdatedDataFromDatabaseClosedBids();
      Gson gson2 = new Gson();
      String itemData2 = "C" + gson2.toJson(closedBids, new TypeToken<List<AuctionItem>>() {}.getType());
      handler.sendToClient(itemData2);

  }

  protected List<AuctionItem> getAuctionItems(){
    return auctionItems;
  }


  public void processLoginRequest(String input) {
    // check database for login info
    Gson gson = new Gson();
    LoginInfo loginInfo = gson.fromJson(input, LoginInfo.class);

    try {
      Document userDoc = loginCol.find(eq("userID", loginInfo.getUsername())).first();
      if (userDoc != null) {
        // User exists, check password or other login criteria
        String storedPassword = userDoc.getString("password"); // Replace "password" with your actual field name
        if (storedPassword.equals(loginInfo.getPassword())) {
          // Login successful, load item data onto client
          handler.sendToClient("true");

          if (!scheduleFlag) { // so that relogs dont decrement the timer, should only call this once then it should auto call itself
            scheduleUpdateTask(); // start autoUpdate
            scheduleFlag = true;
          }

        } else {
          // Password incorrect
          System.out.println("Incorrect password");
          handler.sendToClient("false");

        }
      } else {
        // User not found
        System.out.println("User not found");
        handler.sendToClient("false");

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void processSignUpRequest(String input) { // send message as Sfalse or STrue to signify signupFalse signupTrue
    Gson gson = new Gson();
    LoginInfo loginInfo = gson.fromJson(input, LoginInfo.class);
    Document userDoc = loginCol.find(eq("userID", loginInfo.getUsername())).first();

    if (userDoc == null) { // username does not exist
      Document newUser = new Document("userID", loginInfo.getUsername())
              .append("password", loginInfo.getPassword());
      loginCol.insertOne(newUser);
      handler.sendToClient("Strue");
    }
    else { // sign up failed
      handler.sendToClient("Sfalse");
    }

  }

  public List<AuctionItem> getClosedBids() {
    return closedBids;
  }


  public void scheduleUpdateTask() {
    // Schedule the update task to run every minute
    scheduler.scheduleAtFixedRate(() -> {
      try {
        System.out.println("hi");
        Document query = new Document("timeRemaining", new Document("$gt", 0)); // Query for documents where timeRemaining is greater than 0
        Document update = new Document("$inc", new Document("timeRemaining", -1)); // Update each document individually
        // Use updateMany with the query to update only documents with timeRemaining > 0
        pokemonCol.updateMany(query, update);

        // now check collection and see if any of the minutes equals zero, if so move it to closedBids
        Document zeroTimeQuery = new Document("timeRemaining", 0);
        MongoCursor<Document> cursor = pokemonCol.find(zeroTimeQuery).iterator();
        try {
          while (cursor.hasNext()) {
            Document documentToMove = cursor.next();
            closedCol.insertOne(documentToMove); // Insert into closedBids
            pokemonCol.deleteOne(zeroTimeQuery); // Delete from original collection
          }
        } finally {
          cursor.close();
        }
        // notify clients of recent updates should repopulate grid whenever it reaches 0 for a timer
      } catch (Exception e) {
        e.printStackTrace();
      }
      this.setChanged();
      this.notifyObservers();
    }, 0, 60, TimeUnit.SECONDS);
  }

}
