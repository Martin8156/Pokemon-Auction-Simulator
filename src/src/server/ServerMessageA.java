package src.server;

import src.sharedClass.AuctionItem;
import java.util.List;

public class ServerMessageA extends Message {
    private List<AuctionItem> auctionItems;

    public ServerMessageA(List<AuctionItem> auctionItems) {
        super(auctionItems);
        this.auctionItems = auctionItems;
    }

    public List<AuctionItem> getAuctionItems() {
        return auctionItems;
    }

    public void setAuctionItems(List<AuctionItem> auctionItems) {
        this.auctionItems = auctionItems;
    }
}
