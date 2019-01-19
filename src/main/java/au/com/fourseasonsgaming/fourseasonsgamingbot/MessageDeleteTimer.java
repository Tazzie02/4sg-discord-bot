package au.com.fourseasonsgaming.fourseasonsgamingbot;

import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageDeleteTimer {

    private static MessageDeleteTimer instance = new MessageDeleteTimer();

    private List<MessageTime> queue;
    private Thread process;
    private boolean isRunning;

    private MessageDeleteTimer() {
        this.queue = new ArrayList<>();
    }

    public static MessageDeleteTimer getInstance() {
        return instance;
    }

    public void addMessage(Message message, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);

        queue.add(new MessageTime(calendar.getTime(), message));

        if (!this.isRunning) {
            startProcessing();
        }
    }

    private void initializeProcess() {
        this.process = new Thread(() -> {
            while (this.isRunning) {
                processQueue();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startProcessing() {
        if (this.process == null) {
            initializeProcess();
        }

        this.process.start();
        this.isRunning = true;
    }

    private void stopProcessing() {
        this.process = null;
        this.isRunning = false;
    }

    private void processQueue() {
        if (this.queue.isEmpty()) {
            stopProcessing();
            return;
        }

        Date now = new Date();
        List<MessageTime> toRemove = new ArrayList<>();

        for (MessageTime mt : this.queue) {
            if (mt.deleteTime.before(now)) {
                mt.message.delete().queue();
                toRemove.add(mt);
            }
        }

        this.queue.removeAll(toRemove);
    }

    private class MessageTime {
        private Date deleteTime;
        private Message message;

        public MessageTime(Date deleteTime, Message message) {
            this.deleteTime = deleteTime;
            this.message = message;
        }

        public Date getDeleteTime() {
            return deleteTime;
        }

        public Message getMessage() {
            return message;
        }

    }

}
