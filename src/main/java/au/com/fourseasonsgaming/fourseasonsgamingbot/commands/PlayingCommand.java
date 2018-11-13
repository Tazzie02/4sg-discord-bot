package au.com.fourseasonsgaming.fourseasonsgamingbot.commands;

import com.tazzie02.tazbotdiscordlib.Command;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayingCommand implements Command {

    @Override
    public void onCommand(MessageReceivedEvent e, String[] args) {
        List<Member> members = e.getGuild().getMembers();

        List<Player> players = new ArrayList<>();
        for (Member member : members) {
            Game game = member.getGame();
            if (game != null && game.getType().equals(Game.GameType.DEFAULT)) {
                players.add(new Player(member.getEffectiveName(), game.getName()));
            }
        }

    }

    @Override
    public CommandAccess getAccess() {
        return CommandAccess.ALL;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("playing");
    }

    @Override
    public String getDescription() {
        return "Get a count of players in a game.";
    }

    @Override
    public String getName() {
        return "Playing command";
    }

    @Override
    public String getDetails() {
        return "playing - Output players in games\n" +
                "playing [game] - Output players in [game]";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    class Player {
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
}
