
package id2203.detectors.messages;

import id2203.link.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author M&M
 */
public class HBMsg extends Pp2pDeliver {

    public HBMsg(Address src) {
        super(src);
    }
}