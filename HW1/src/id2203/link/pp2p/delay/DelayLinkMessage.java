package id2203.link.pp2p.delay;

import id2203.link.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Message;
import se.sics.kompics.network.Transport;

public class DelayLinkMessage extends Message {
    /**
     *
     */
    private static final long serialVersionUID = -8044668011408046391L;
    private Pp2pDeliver deliverEvent;

    public DelayLinkMessage(Address source, Address destination,
            Pp2pDeliver deliverEvent) {
        super(source, destination, Transport.TCP);
        this.deliverEvent = deliverEvent;
    }

    public Pp2pDeliver getDeliverEvent() {
        return deliverEvent;
    }
}

