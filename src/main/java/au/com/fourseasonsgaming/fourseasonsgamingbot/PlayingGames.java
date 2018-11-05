package au.com.fourseasonsgaming.fourseasonsgamingbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayingGames {

    private final Map<String, List<String>> consolidatedPlayers;

    public PlayingGames(List<Player> players) {
        this.consolidatedPlayers = consolidate(players);
    }

    private Map<String, List<String>> consolidate(List<Player> players) {
        Map<String, List<String>> consolidatedPlayers = new HashMap<>();

        for (Player player : players) {
            if (!consolidatedPlayers.containsKey(player.getGame())) {
                List<String> names = new ArrayList<>();
                consolidatedPlayers.put(player.getGame(), names);
            }
            consolidatedPlayers.get(player.getGame()).add(player.getName());
        }

        return consolidatedPlayers;
    }

    public Map<String, List<String>> getPlayers(String game) {
        Map<String, List<String>> players = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : consolidatedPlayers.entrySet()) {
            if (entry.getKey().contains(game)) {
                players.put(entry.getKey(), entry.getValue());
            }
        }

        return players;
    }


}
