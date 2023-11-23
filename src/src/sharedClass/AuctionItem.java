package src.sharedClass;

public class AuctionItem {
    private String name;
    private double minimumBid;
    private boolean bidOpen;

    public AuctionItem(String name, double minimumBid){
        this.name = name;
        this.minimumBid = minimumBid;
        bidOpen = true;
    }


    /**
     * setters
     */
    public void setBidStatus(Boolean status) {bidOpen = status;}

    /**
     * getters
     */
    public String getName() {
        return name;
    }
    public double getMinBid() {
        return minimumBid;
    }
    public boolean getBidStatus() {return bidOpen;}

}

