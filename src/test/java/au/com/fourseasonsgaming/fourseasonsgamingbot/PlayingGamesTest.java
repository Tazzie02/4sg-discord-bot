package au.com.fourseasonsgaming.fourseasonsgamingbot;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayingGamesTest {

    private List<Player> players;

    @Before
    public void populateData() {
        players = new ArrayList<>();
        players.add(new Player("tuser01", "Minecraft"));
        players.add(new Player("tuser02", "Quake Champions"));
        players.add(new Player("tuser03", "PLAYERUNKOWN'S BATTLEGROUNDS"));
        players.add(new Player("tuser04", "MapleStory 2"));
        players.add(new Player("tuser05", "Diablo 3"));
        players.add(new Player("tuser06", "Red Dead Redemption 2"));
        players.add(new Player("tuser07", "Path of Exile"));
        players.add(new Player("tuser08", "Rocket League"));
        players.add(new Player("tuser09", "MapleStory 2"));
        players.add(new Player("tuser10", "PLAYERUNKNOWN'S BATTLEGROUNDS"));
        players.add(new Player("tuser11", "Quake Live"));
        players.add(new Player("tuser12", "Quake Live"));
        players.add(new Player("tuser13", "Fortnite"));
        players.add(new Player("tuser14", "Call of Duty: Black Ops 4"));
        players.add(new Player("tuser15", "Call of Duty: Black Ops 4"));
        players.add(new Player("tuser16", "Call of Duty: Black Ops 4"));
        players.add(new Player("tuser17", "Dolphin Emulator"));
        players.add(new Player("tuser18", "EVE Online"));
        players.add(new Player("tuser19", "Quake Champions"));
        players.add(new Player("tuser20", "Rainbow Six Siege"));
    }

    @Test
    public void gameSearchTest() {
        PlayingGames playingGames = new PlayingGames(players);

        Map<String, List<String>> quake = playingGames.getPlayers("Quake");
        // Two games returned
        assertEquals(2, quake.size());
        assertEquals(2, quake.get("Quake Champions").size());
        assertEquals(2, quake.get("Quake Live").size());

    }
}
