package com.APRT.utmLogin;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.logging.Logger;

public class WatchDog {

    private static volatile boolean running = true; // 监控线程是否运行
    private static volatile boolean handshaked = false; // 主线程是否握手
    private static Thread watchdogThread = new Thread(() -> {
        Long startTime = System.currentTimeMillis();

        while (running) {

            if (System.currentTimeMillis() - startTime > 1000) {
                // 判断主线程是否超时
                int behindTime = Math.toIntExact(System.currentTimeMillis() - startTime);
                if (behindTime >= 20000) {
                    System.out.println("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    System.out.println("OOPS!Server was running behind over 20000ms");
                    System.out.println("Making dump......");
                    LLogger.LogRec("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    LLogger.LogRec("OOPS!Server was running behind over 20000ms");
                    printThreadSnapshot();
                    System.out.println(">");
                    System.out.println("Stopping server......");
                    LLogger.LogRec("Stopping server......");
                    Main.Status = false;
                    System.exit(-1);
                    return;
                }
                if (behindTime >= 10000) {
                    System.out.println("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    System.out.println("OOPS!Server was running behind over 10000ms");
                    System.out.println("Making dump......");
                    LLogger.LogRec("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    LLogger.LogRec("OOPS!Server was running behind over 10000ms");
                    printThreadSnapshot();
                    System.out.println(">");
                    return;
                }
                if (behindTime >= 5000) {
                    System.out.println("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    System.out.println("OOPS!Server was running behind over 5000ms");
                    LLogger.LogRec("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    LLogger.LogRec("OOPS!Server was running behind over 5000ms");
                    System.out.println(">");
                    return;
                }

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (handshaked) { // 如果主线程握手
                startTime = System.currentTimeMillis();
                handshaked = false;
            }

            try {
                Thread.sleep(100); // 调整睡眠时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public static void WatchDog() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping WatchDog......");
            watchdogThread.stop();

        }));
        long pid = Main.getCurrentProcessId();
        watchdogThread.start();

    }

    public static void handshake() {
        handshaked = true; // 握手操作
    }
    public static void printThreadSnapshot() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);

        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println(threadInfo.toString());
            LLogger.LogRec(threadInfo.toString());
        }
    }
    public void stop() {
        running = false; // 停止监控线程
    }


}