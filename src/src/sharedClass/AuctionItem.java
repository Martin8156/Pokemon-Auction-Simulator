package src.sharedClass;

public class AuctionItem {
    private String name;
    private double minimumBid;

    public AuctionItem(String name, double minimumBid){
        this.name = name;
        this.minimumBid = minimumBid;
    }


    public String getDescription() {
        return name;
    }

    public double getMinPrice() {
        return minimumBid;
    }
}

