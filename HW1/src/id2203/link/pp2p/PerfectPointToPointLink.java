package id2203.link.pp2p;

import se.sics.kompics.PortType;

public class PerfectPointToPointLink extends PortType {
    {
        indication(Pp2pDeliver.class);
        request(Pp2pSend.class);
    }
}
