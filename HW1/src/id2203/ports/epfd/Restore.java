
package id2203.ports.epfd;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author M&M
 */
public class Restore extends Event {
    private Address node;
    private int period;

    public Restore(Address n, int p) {
        this.node = n;
        this.period = p;
    }

    public Address getNode() {
        return node;
    }

    public int getPeriod() {
        return period;
    }
}

