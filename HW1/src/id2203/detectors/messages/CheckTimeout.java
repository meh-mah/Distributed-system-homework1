
package id2203.detectors.messages;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author M&M
 */
public class CheckTimeout extends Timeout {
    public CheckTimeout(ScheduleTimeout req) {
        super(req);
    }
}