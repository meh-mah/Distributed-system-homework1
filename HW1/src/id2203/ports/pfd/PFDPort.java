
package id2203.ports.pfd;

import se.sics.kompics.PortType;

/**
 *
 * @author M&M
 */
public class PFDPort extends PortType {
    {
        indication(Crash.class);
    }
}

