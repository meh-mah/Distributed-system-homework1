package id2203.link.pp2p.delay;

import se.sics.kompics.Init;
import se.sics.kompics.launch.Topology;

public class DelayLinkInit extends Init {
    private Topology topology;

    public DelayLinkInit(Topology topology) {
        this.topology = topology;
    }

    public Topology getTopology() {
        return topology;
    }
}
