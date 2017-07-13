

package id2203.link.pp2p.delay;

import se.sics.kompics.network.Message;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

public class DelayedMessage extends Timeout {
    private Message message;

    public DelayedMessage(ScheduleTimeout request, Message message) {
        super(request);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}

