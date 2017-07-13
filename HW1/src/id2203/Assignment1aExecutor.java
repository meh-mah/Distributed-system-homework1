/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package id2203;

import se.sics.kompics.launch.Scenario;
import se.sics.kompics.launch.Topology;

/**
 *
 * @author M&M
 */
public class Assignment1aExecutor {
    public static void main(String[] args) {
        Topology topology1 = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                node(4, "127.0.0.1", 22034);

                //link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(500, 0);
            }
        };

        Topology topology2 = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                node(4, "127.0.0.1", 22034);

                //link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(500, 0);
            }
        };

        Scenario scenario1 = new Scenario(Assignment1aMain.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario2 = new Scenario(Assignment1aMain.class) {
            {
                command(1, "S3000:X");
                command(2, "S500");
                command(3, "S500");
                command(4, "S3000:X");

            }
        };

// EX1
        scenario2.executeOn(topology2);
        //scenario2.executeOn(topology1);
        //scenario3.executeOn(topology2);
        // scenario2.executeOn(topology1);
        // scenario2.executeOn(topology2);
        // scenario1.executeOnFullyConnected(topology1);
        // scenario1.executeOnFullyConnected(topology2);
        // scenario2.executeOnFullyConnected(topology1);
        // scenario2.executeOnFullyConnected(topology2);

        System.exit(0);
        // move one of the below scenario executions above the exit for
        // execution

        scenario1.executeOn(topology1);
        scenario1.executeOn(topology2);
        scenario2.executeOn(topology1);
        scenario2.executeOn(topology2);
        scenario1.executeOnFullyConnected(topology1);
        scenario1.executeOnFullyConnected(topology2);
        scenario2.executeOnFullyConnected(topology1);
        scenario2.executeOnFullyConnected(topology2);
    }
}
