
package id2203.detectors.epfdcomponent;

import id2203.ports.epfd.EPFDPort;
import id2203.ports.epfd.Restore;
import id2203.ports.epfd.Suspect;
import id2203.detectors.messages.CheckTimeout;
import id2203.detectors.messages.HBMsg_FL;
import id2203.detectors.messages.HBTimeout;
import id2203.link.flp2p.FairLossPointToPointLink;
import id2203.link.flp2p.Flp2pSend;
import java.util.HashSet;
import java.util.Set;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author M&M
 */
public class FaiLossEPFD extends ComponentDefinition {

    Negative<EPFDPort> ep = provides(EPFDPort.class);
    Positive<FairLossPointToPointLink> flp2pl = requires(FairLossPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);

    private Address myAddress;
    private int period;
    private int delay;
    private int delta;
    private Set<Address> correct;
    private Set<Address> suspected;
    private Set<Address> neighbors;

    public FaiLossEPFD() {
        subscribe(hInit, control);
        subscribe(hHBMsg, flp2pl);
        subscribe(hTimeout, timer);
        subscribe(hHBTimeout, timer);
    }

    Handler<EPFDInit> hInit = new Handler<EPFDInit>() {
        @Override
        public void handle(EPFDInit e) {
            
            neighbors = e.getNeighbors();
            correct = new HashSet<>(e.getNeighbors());
            suspected = new HashSet<>();
            delay = e.getDelay();
            period = delay;
            delta = e.getDelta();
            myAddress = e.getMyAddress();

            ScheduleTimeout HBSchedule = new ScheduleTimeout(delay);
            HBSchedule.setTimeoutEvent(new HBTimeout(HBSchedule));
            trigger(HBSchedule, timer);

            ScheduleTimeout checkInterval = new ScheduleTimeout(period);
            checkInterval.setTimeoutEvent(new CheckTimeout(checkInterval));
            trigger(checkInterval, timer);
        }
    };
    Handler<HBTimeout> hHBTimeout = new Handler<HBTimeout>() {
        @Override
        public void handle(HBTimeout e) {
            for (Address node : neighbors) {
                trigger(new Flp2pSend(node, new HBMsg_FL(myAddress)), flp2pl);
            }

            ScheduleTimeout HBSchedule = new ScheduleTimeout(delay);
            HBSchedule.setTimeoutEvent(new HBTimeout(HBSchedule));
            trigger(HBSchedule, timer);
        }
    };
    Handler<CheckTimeout> hTimeout = new Handler<CheckTimeout>() {
        @Override
        public void handle(CheckTimeout e) {
            Set<Address> joint = new HashSet<>(correct);
            joint.retainAll(suspected);
            if (joint.size() > 0) {
                period += delta;
            }

            for (Address node : neighbors) {
                if ((!correct.contains(node)) && (!suspected.contains(node))) {
                    suspected.add(node);
                    trigger(new Suspect(node, period), ep);
                } else if ((correct.contains(node)) && (suspected.contains(node))) {
                    suspected.remove(node);
                    trigger(new Restore(node, period), ep);
                }
            }

            correct.clear();

            ScheduleTimeout checkInterval = new ScheduleTimeout(period);
            checkInterval.setTimeoutEvent(new CheckTimeout(checkInterval));
            trigger(checkInterval, timer);
        }
    };
    Handler<HBMsg_FL> hHBMsg = new Handler<HBMsg_FL>() {
        @Override
        public void handle(HBMsg_FL e) {
            correct.add(e.getSource());
        }
    };
}
