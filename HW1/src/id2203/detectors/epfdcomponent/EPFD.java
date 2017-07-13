
package id2203.detectors.epfdcomponent;

import id2203.ports.epfd.EPFDPort;
import id2203.ports.epfd.Restore;
import id2203.ports.epfd.Suspect;
import id2203.detectors.messages.CheckTimeout;
import id2203.detectors.messages.HBMsg;
import id2203.detectors.messages.HBTimeout;
import id2203.link.pp2p.PerfectPointToPointLink;
import id2203.link.pp2p.Pp2pSend;
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
public class EPFD extends ComponentDefinition {

    Negative<EPFDPort> ep = provides(EPFDPort.class);
    Positive<Timer> timer = requires(Timer.class);
    Positive<PerfectPointToPointLink> pp2pl = requires(PerfectPointToPointLink.class);
    
    private Set<Address> neighbors;
    private Set<Address> correct;
    private Set<Address> suspected;
    private int delta;
    private int delay;
    private int period;
    private Address myAddress;

    public EPFD() {
        subscribe(hInit, control);
        subscribe(hHBMessage, pp2pl);
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
                trigger(new Pp2pSend(node, new HBMsg(myAddress)), pp2pl);
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
    Handler<HBMsg> hHBMessage = new Handler<HBMsg>() {
        @Override
        public void handle(HBMsg e) {
            correct.add(e.getSource());
        }
    };
}
