
package id2203.application;

import id2203.ports.pfd.Crash;
import id2203.ports.pfd.PFDPort;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 * @author M&M
 */
public final class Application1a extends ComponentDefinition {
    Positive<PFDPort> pfd = requires(PFDPort.class);
    Positive<Timer> timer = requires(Timer.class);
    
    private String[] commands;
    private int lastCommand;
    
    private static final Logger logger = LoggerFactory.getLogger(Application1a.class);


    public Application1a() {
        subscribe(hInit, control);
        subscribe(hStart, control);
        subscribe(hContinue, timer);
        subscribe(hCrash, pfd);

    }
    
    Handler<ApplicationInit> hInit = new Handler<ApplicationInit>() {
        @Override
        public void handle(ApplicationInit e) {
            commands = e.getCommandScript().split(":");
            lastCommand = -1;
        }
    };
    
    Handler<ApplicationContinue> hContinue = new Handler<ApplicationContinue>() {
        @Override
        public void handle(ApplicationContinue event) {
            doNextCommand();
        }
    };
    
    Handler<Start> hStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            doNextCommand();
        }
    };
    
    Handler<Crash> hCrash = new Handler<Crash>() {
        @Override
        public void handle(Crash e) {
            logger.info("A crash detected. crashed Node Address:{}", e.getDetected().toString());
        }
    };

    private void doNextCommand() {
        lastCommand++;

        if (lastCommand > commands.length) {
            return;
        }
        if (lastCommand == commands.length) {
            logger.info("DONE ALL OPERATIONS");
            Thread applicationThread = new Thread("ApplicationThread") {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(System.in));
                    while (true) {
                        try {
                            String line = in.readLine();
                            doCommand(line);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            applicationThread.start();
            return;
        }
        String op = commands[lastCommand];
        doCommand(op);
    }

    private void doCommand(String cmd) {
        if (cmd.startsWith("S")) {
            doSleep(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("X")) {
            doShutdown();
        } else if (cmd.equals("help")) {
            doHelp();
            doNextCommand();
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
            doNextCommand();
        }
    }

    private void doHelp() {
        logger.info("Available commands: S<n>, help, X");
        logger.info("Sn: sleeps 'n' milliseconds before the next command");
        logger.info("help: shows this help message");
        logger.info("X: terminates this process");
    }

    private void doSleep(long delay) {
        logger.info("Sleeping {} milliseconds...", delay);

        ScheduleTimeout st = new ScheduleTimeout(delay);
        st.setTimeoutEvent(new ApplicationContinue(st));
        trigger(st, timer);
    }

    private void doShutdown() {
        System.out.println("2DIE");
        System.out.close();
        System.err.close();
        Kompics.shutdown();
    }
}
