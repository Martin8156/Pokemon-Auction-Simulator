package src.sharedClass;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class AuctionItem {
    private String id;
    private String name;
    private String description;
    private int timeRemaining;
    private double minimumBid;
    private double buyNowPrice;
    private boolean bidOpen;
    private List<BidInfo> bidHistory;

    public AuctionItem(String id, String name, String description, int timeRemaining, double minimumBid, double buyNowPrice, boolean bidOpen, List<BidInfo> bidHistory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeRemaining = timeRemaining;
        this.minimumBid = minimumBid;
        this.buyNowPrice = buyNowPrice;
        this.bidOpen = bidOpen;
        this.bidHistory = bidHistory;
    }


    /**
     * TODO: setter
     */
    public void addToBidHistory(BidInfo placedBid) {
        bidHistory.add(placedBid);
    }
    public void newBidHistory(){
        bidHistory = new ArrayList<>() ;
    }
    public void setTimeRemaining(int timeRemaining) {this.timeRemaining = timeRemaining;}

    /**
     * TODO: getters
     */
    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {return description;}

    public int getTimeRemaining() {return timeRemaining;}

    public double getMinBid() {
        return minimumBid;
    }

    public double getBuyNowPrice() {
        return buyNowPrice;
    }

    public boolean getBidStatus() {
        return bidOpen;
    }

    public List<BidInfo> getBidHistory() {return bidHistory;}

    public String[] getBidHistoryStringArray() {
        List<String> bidHistoryList = new ArrayList<>();

        for (BidInfo bidInfo : bidHistory) {
            bidHistoryList.add(bidInfo.toString());
        }

        return bidHistoryList.toArray(new String[0]);
    }



}
