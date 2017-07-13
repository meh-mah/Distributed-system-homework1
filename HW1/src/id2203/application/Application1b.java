/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package id2203.application;

import id2203.application.ApplicationContinue;
import id2203.application.ApplicationInit;
import id2203.ports.epfd.EPFDPort;
import id2203.ports.epfd.Restore;
import id2203.ports.epfd.Suspect;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sics.kompics.*;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author M&M
 */
public final class Application1b extends ComponentDefinition {
    
    Positive<EPFDPort> epfd = requires(EPFDPort.class);
    Positive<Timer> timer = requires(Timer.class);
   
    private String[] commands;
    private int lastCommand;
    
    private static final Logger logger = LoggerFactory.getLogger(Application1b.class);

    public Application1b() {
        subscribe(hInit, control);
        subscribe(hStart, control);
        subscribe(hContinue, timer);
        subscribe(hSuspect, epfd);
        subscribe(hRestore, epfd);
    }

    Handler<ApplicationInit> hInit = new Handler<ApplicationInit>() {
        @Override
        public void handle(ApplicationInit event) {
            commands = event.getCommandScript().split(":");
            lastCommand = -1;
        }
    };
    Handler<Start> hStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            doNextCommand();
        }
    };
    Handler<ApplicationContinue> hContinue = new Handler<ApplicationContinue>() {
        @Override
        public void handle(ApplicationContinue event) {
            doNextCommand();
        }
    };
    Handler<Suspect> hSuspect = new Handler<Suspect>() {
        @Override
        public void handle(Suspect event) {
            logger.info("period={}::: Node {} suspected", event.getPeriod(), event.getNode() );
        }
    };
    Handler<Restore> hRestore = new Handler<Restore>() {
        @Override
        public void handle(Restore event) {
            logger.info("period={}::: Node {} restored",  event.getPeriod(), event.getNode());
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
        } else if (cmd.startsWith("R")) {
			myRecovery();
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
        logger.info("R: Recovers this process");
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

    private void myRecovery() {
        logger.debug("process recoverd successfully....");
    }
}

