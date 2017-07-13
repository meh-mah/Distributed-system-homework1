/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package id2203.detectors.pfdcomponent;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author M&M
 */
public class PFDInit extends Init {
    
    private int HBfrequency;
    private int checkfrquency;
    private Set<Address> neighbors;
    private Address myAddress;

    public PFDInit(Set<Address> neighbors, Address myAddress, int HBfrequency, int checkfrquency) {
        this.neighbors = neighbors;
        this.myAddress = myAddress;
        this.HBfrequency = HBfrequency;
        this.checkfrquency = checkfrquency;
    }

    public Set<Address> getNeighbors() {
        return neighbors;
    }

    public Address getMyAddress() {
        return myAddress;
    }

    public int HBfrequency() {
        return HBfrequency;
    }

    public int checkfrquency() {
        return checkfrquency;
    }
}
