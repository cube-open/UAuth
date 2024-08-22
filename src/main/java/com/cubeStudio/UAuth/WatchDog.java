package com.cubeStudio.UAuth;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

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
                    LLogger.fetal("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    LLogger.fetal("OOPS!Server was running behind over 20000ms");
                    LLogger.error("Making dump......");
                    LLogger.error("==========Please don't report it to Cube-Open,it's not a bug or a crash!==========");

                    printThreadSnapshot();
                    System.out.println(">");
                    LLogger.info("Stopping server......");
                    Main.Status = false;
                    System.exit(-1);
                    return;
                }
                if (behindTime >= 10000) {
                    LLogger.warn("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");
                    LLogger.warn("OOPS!Server was running behind over 10000ms");
                    LLogger.warn("Making dump......");
                    LLogger.warn("==========Please don't report it to Cube-Open,it's not a bug or a crash!==========");

                    printThreadSnapshot();
                    System.out.println(">");
                    return;
                }
                if (behindTime >= 1000) {
                    LLogger.warn("Can't keep up!Is server over load? Running " + behindTime + "ms behind!");

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
            LLogger.info("Stopping WatchDog......");
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
            LLogger.warn(threadInfo.toString());
            LLogger.LogRec(threadInfo.toString(),"warn");
        }
    }
    public void stop() {
        running = false; // 停止监控线程
    }


}