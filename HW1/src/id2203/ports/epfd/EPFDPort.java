
package id2203.ports.epfd;

import se.sics.kompics.PortType;

/**
 *
 * @author M&M
 */
public class EPFDPort extends PortType {

    {
        indication(Suspect.class);
        indication(Restore.class);
    }
}
