/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package id2203.detectors.epfdcomponent;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author M&M
 */
public class EPFDInit extends Init {
    private Address myAddress;
    private int Delay;
    private Set<Address> neighbors;
    private int delta;

    public EPFDInit(int td, int d, Set<Address> neighbors, Address myaddress) {
        this.Delay = td;
        this.delta = d;
        this.neighbors = neighbors;
        this.myAddress = myaddress;
    }

    public int getDelta() {
        return delta;
    }

    public Set<Address> getNeighbors() {
        return neighbors;
    }

    public int getDelay() {
        return Delay;
    }

    public Address getMyAddress() {
        return myAddress;
    }
}

