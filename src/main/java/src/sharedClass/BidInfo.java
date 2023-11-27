package src.sharedClass;

public class BidInfo {
    private String customerUsername;
    private String pokemonName;
    private double bidAmount;

    public BidInfo(String customerUsername, String pokemonName, double bidAmount) {
        this.customerUsername = customerUsername;
        this.pokemonName = pokemonName;
        this.bidAmount = bidAmount;
    }

    public String getCustomerUsername() {return customerUsername;}
    public String getPokemonName() {
        return pokemonName;
    }

    public double getBidAmount() {
        return bidAmount;
    }


    public String toString() {
      return customerUsername + " has placed a bid on " + pokemonName + " for " + bidAmount;
    }
}

