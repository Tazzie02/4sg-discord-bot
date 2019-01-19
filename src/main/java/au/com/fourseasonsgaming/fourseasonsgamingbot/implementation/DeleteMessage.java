package au.com.fourseasonsgaming.fourseasonsgamingbot.implementation;

import au.com.fourseasonsgaming.fourseasonsgamingbot.MessageDeleteTimer;
import com.tazzie02.tazbotdiscordlib.MessageCallback;
import net.dv8tion.jda.core.entities.Message;

public class DeleteMessage implements MessageCallback {

    private final int deleteDelay;

    public DeleteMessage(int deleteDelay) {
        this.deleteDelay = deleteDelay;
    }

    @Override
    public void callback(Message message) {
        MessageDeleteTimer.getInstance().addMessage(message, deleteDelay);
    }

}
