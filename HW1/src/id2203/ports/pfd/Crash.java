
package id2203.ports.pfd;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author M&M
 */
public class Crash extends Event {
    private Address detected;

    public Crash(Address detected) {
        this.detected = detected;
    }

    public Address getDetected() {
        return detected;
    }
}
