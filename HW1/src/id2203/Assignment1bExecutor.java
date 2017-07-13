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
public class Assignment1bExecutor {
    public static void main(String[] args) {
        Topology topology = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                node(4, "127.0.0.1", 22034);

               // link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(500, 0.5);
            }
        };
        Topology topology2 = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                node(4, "127.0.0.1", 22034);

               // link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(4000, 0.0);
            }
        };
        Topology topology3 = new Topology() {
            {
                node(1, "127.0.0.1", 22031);
                node(2, "127.0.0.1", 22032);
                node(3, "127.0.0.1", 22033);
                node(4, "127.0.0.1", 22034);

               // link(1, 2, 500, 0.5).bidirectional();
                // link(1, 2, 3000, 0.5);
                // link(2, 1, 3000, 0.5);
                // link(3, 2, 3000, 0.5);
                // link(4, 2, 3000, 0.5);
                defaultLinks(500, 0.0);
            }
        };

        Scenario scenario1 = new Scenario(Assignment1bMain.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario2 = new Scenario(Assignment1bMain.class) {
            {
                command(1, "S2500:X").recover("R", 3500);
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario1_lossy = new Scenario(Assignment1bMain_FairLoss.class) {
            {
                command(1, "S500");
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        Scenario scenario2_lossy = new Scenario(Assignment1bMain_FairLoss.class) {
            {
                command(1, "S2500:X").recover("S500", 3000);
                command(2, "S500");
                command(3, "S500");
                command(4, "S500");
            }
        };

        
        //EX1
       // scenario1.executeOn(topology3);
        //EX2
//        scenario1.executeOn(topology2);
//        //EX3
//        scenario1_lossy.executeOn(topology);
        //EX4
        scenario2.executeOn(topology3);

        System.exit(0);
    }
}

