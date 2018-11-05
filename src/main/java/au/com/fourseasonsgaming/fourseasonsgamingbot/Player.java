package au.com.fourseasonsgaming.fourseasonsgamingbot;

public class Player {
    private String name;
    private String game;

    public Player(String name, String game) {
        this.name = name;
        this.game = game;
    }

    public String getName() {
        return this.name;
    }

    public String getGame() {
        return this.game;
    }
}
