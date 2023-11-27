package src.sharedClass;

import java.util.List;

public class LoginInfo {
    private String username;
    private String password;
    private List<AuctionItem> pokemons;

    public LoginInfo(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}



