package com.JohnRichard.utmLogin;

import javax.sql.rowset.spi.SyncFactoryException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.sql.rowset.spi.SyncFactory.getLogger;


public class LLogger {

    public static void LogRec(String logContent) {
        String logFilePath = ReadYaml.readYamlString("config/config.yml", "Config.logPath");

        if (logFilePath != null) {
            File logFile = new File(logFilePath + "/kernel.log");
            if (!logFile.exists()) {
                try {
                    File logDir = new File(logFilePath);
                    logDir.mkdirs();
                    logFile.createNewFile();
                } catch (IOException e) {
                    try {
                        getLogger().warning("Error!");
                    } catch (SyncFactoryException ex) {
                        throw new RuntimeException(ex);
                    }
                    e.printStackTrace();
                }
            }

            try (FileWriter writer = new FileWriter(logFile, true)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy MM dd-HH mm ss] ");
                String timestamp = dateFormat.format(new Date());
                writer.write(timestamp + logContent + "\n");
            } catch (IOException e) {
                try {
                    getLogger().warning("Error!");
                } catch (SyncFactoryException ex) {
                    throw new RuntimeException(ex);
                }
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to get log file path from configuration.");
        }
    }

    /*
    //如何调用？
    public class MainClass {

    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.LogRec("This is a log message.");
    }
}
     */
}

