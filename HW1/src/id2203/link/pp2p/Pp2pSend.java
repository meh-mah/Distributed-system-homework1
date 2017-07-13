package id2203.link.pp2p;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

public class Pp2pSend extends Event {
    private Pp2pDeliver deliverEvent;
    private Address destination;

    public Pp2pSend(Address destination, Pp2pDeliver pp2pDeliver) {
        this.destination = destination;
        this.deliverEvent = pp2pDeliver;
    }

    public Pp2pDeliver getDeliverEvent() {
        return deliverEvent;
    }

    public Address getDestination() {
        return destination;
    }
}
