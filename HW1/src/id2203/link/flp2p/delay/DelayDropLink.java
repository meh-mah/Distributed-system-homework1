

/**
 * This file is part of the ID2203 course assignments kit.
 *
 * Copyright (C) 2009 Royal Institute of Technology (KTH)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package id2203.link.flp2p.delay;

import id2203.link.flp2p.FairLossPointToPointLink;
import id2203.link.flp2p.Flp2pDeliver;
import id2203.link.flp2p.Flp2pSend;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.NoLinkException;
import se.sics.kompics.launch.Topology;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 * The
 * <code>DelayDropLink</code> class.
 *
 * @author Cosmin Arad <cosmin@sics.se>
 * @version $Id: DelayDropLink.java 516 2009-01-28 04:00:47Z cosmin $
 */
public class DelayDropLink extends ComponentDefinition {
    Negative<FairLossPointToPointLink> flp2p = provides(FairLossPointToPointLink.class);
    Positive<Network> network = requires(Network.class);
    Positive<Timer> timer = requires(Timer.class);
    private Address self;
    private Topology topology;
    private Random random;
    //logger
    private static final Logger logger = LoggerFactory.getLogger(DelayDropLink.class);

    /**
     * Instantiates a new delay drop link.
     */
    public DelayDropLink() {
        subscribe(handleInit, control);
        subscribe(handleFlp2pSend, flp2p);
        subscribe(handleMessage, network);
        subscribe(handleDelayedMessage, timer);
    }
    Handler<DelayDropLinkInit> handleInit = new Handler<DelayDropLinkInit>() {
        @Override
        public void handle(DelayDropLinkInit event) {
            topology = event.getTopology();
            self = topology.getSelfAddress();

            random = new Random(event.getRandomSeed());
        }
    };
    Handler<Flp2pSend> handleFlp2pSend = new Handler<Flp2pSend>() {
        @Override
        public void handle(Flp2pSend event) {
//            logger.debug("Sending msg={} to {}", event.getDeliverEvent(), event.getDestination());
            Address destination = event.getDestination();

            if (destination.equals(self)) {
                // deliver locally
                Flp2pDeliver deliverEvent = event.getDeliverEvent();
                trigger(deliverEvent, flp2p);
                return;
            }

            double lossRate;
            long latency;
            try {
                lossRate = topology.getLossRate(self, destination);
                latency = topology.getLatencyMs(self, destination);
            } catch (NoLinkException e) {
                // there is no link to the destination, we drop the message
                return;
            }

            if (random.nextDouble() < lossRate) {
                // drop the message according to the loss rate
                return;
            }

            // make a DelayDropLinkMessage to be delivered at the destination
            DelayDropLinkMessage message = new DelayDropLinkMessage(self,
                    destination, event.getDeliverEvent());

            if (latency > 0) {
                // delay the sending according to the latency
                ScheduleTimeout st = new ScheduleTimeout(latency);
                st.setTimeoutEvent(new DelayedMessage(st, message));
                trigger(st, timer);
            } else {
                // send immediately
                trigger(message, network);
            }
        }
    };
    Handler<DelayedMessage> handleDelayedMessage = new Handler<DelayedMessage>() {
        @Override
        public void handle(DelayedMessage event) {
            trigger(event.getMessage(), network);
        }
    };
    Handler<DelayDropLinkMessage> handleMessage = new Handler<DelayDropLinkMessage>() {
        @Override
        public void handle(DelayDropLinkMessage event) {
//            logger.debug("Delivering msg={}", event.getDeliverEvent());
            trigger(event.getDeliverEvent(), flp2p);
        }
    };
}
