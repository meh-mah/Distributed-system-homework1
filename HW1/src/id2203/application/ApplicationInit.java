/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package id2203.application;

import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author M&M
 */
public class ApplicationInit extends Init {
    private String commandScript;
    private Address self;

    public ApplicationInit(String commandScript) {
        this.commandScript = commandScript;
    }

    public ApplicationInit(String commandScript, Address self) {
        this(commandScript);
        this.self = self;
    }

    public String getCommandScript() {
        return commandScript;
    }

    public Address getSelf() {
        return self;
    }
}
