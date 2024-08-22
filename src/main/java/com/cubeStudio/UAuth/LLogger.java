package com.cubeStudio.UAuth;

import javax.sql.rowset.spi.SyncFactoryException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.sql.rowset.spi.SyncFactory.getLogger;


public class LLogger {
    /**
     * 根据指定的日志级别记录日志信息。
     *
     * <p>此方法从配置文件读取日志路径，并根据提供的日志级别将日志内容写入到指定的日志文件中。
     * 如果日志文件不存在，则会尝试创建它。
     *
     * @param logContent 要记录的日志内容
     * @param level 日志级别，支持 "info", "warn", "error", "fatal", "debug"
     */
    public static void LogRec(String logContent,String level) {
        String logFilePath = ReadYaml.readYamlString("config/config.yml", "Config.logPath");
        switch(level) {
            case "info" -> info(logContent);

            case "warn" -> warn(logContent);

            case "error" -> error(logContent);

            case "fetal" -> fetal(logContent);

            case "debug" -> debug(logContent);

        }
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
                writer.write(timestamp +"[UNKNOW]"+ logContent + "\n");

                System.out.println(timestamp + "[UNKNOW]"+ Color.color(logContent,"WHITE") );
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
    /**
     * 记录信息级别的日志。
     *
     * <p>此方法从配置文件读取日志路径，并将带有 "[INFO]" 前缀的信息级别日志内容写入到指定的日志文件中。
     * 如果日志文件不存在，则会尝试创建它。
     *
     * @param logContent 要记录的日志内容
     */
    public static void info(String logContent) {
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

                writer.write(timestamp +"[INFO]"+ logContent + "\n");
                System.out.println(timestamp + Color.color("[INFO]","GREEN")+ Color.color(logContent,"RESET") );
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
    /**
     * 记录警告级别的日志。
     *
     * <p>此方法从配置文件读取日志路径，并将带有 "[WARNING]" 前缀的警告级别日志内容写入到指定的日志文件中。
     * 如果日志文件不存在，则会尝试创建它。
     *
     * @param logContent 要记录的日志内容
     */
    public static void warn(String logContent) {
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
                writer.write(timestamp +"[WARNING]"+ logContent + "\n");
                System.out.println(timestamp +Color.color("[WARNING]"+ logContent ,"YELLOW"));
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
    /**
     * 记录错误级别的日志。
     *
     * <p>此方法从配置文件读取日志路径，并将带有 "[ERROR]" 前缀的错误级别日志内容写入到指定的日志文件中。
     * 如果日志文件不存在，则会尝试创建它。
     *
     * @param logContent 要记录的日志内容
     */
    public static void error(String logContent) {
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
                writer.write(timestamp +"[ERROR]"+ logContent + "\n");
                System.out.println(timestamp +Color.color("[ERROR]"+ logContent ,"RED"));
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
    /**
     * 记录异常级别的日志。
     *
     * <p>此方法从配置文件读取日志路径，并将带有 "[EXCEPTION]" 前缀的异常级别日志内容写入到指定的日志文件中。
     * 如果日志文件不存在，则会尝试创建它。
     *
     * @param logContent 要记录的日志内容
     */
    public static void exception(String logContent) {
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
                writer.write(timestamp +"[EXCEPTION]"+ logContent + "\n");
                System.out.println(timestamp +Color.color("[EXCEPTION]"+ logContent ,"YELLOW"));
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
    /**
     * 记录致命级别的日志。
     *
     * <p>此方法从配置文件读取日志路径，并将带有 "!!![FATAL]!!!" 前缀的致命级别日志内容写入到指定的日志文件中。
     * 如果日志文件不存在，则会尝试创建它。
     *
     * @param logContent 要记录的日志内容
     */
    public static void fetal(String logContent) {
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
                writer.write(timestamp +"!!![FETAL]!!!"+ logContent + "\n");
                System.out.println(timestamp +Color.color("!!![FETAL]!!!"+ logContent ,"RED"));
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
    /**
     * 记录调试级别的日志。
     *
     * <p>此方法从配置文件读取日志路径，并将带有 "[DEBUG]" 前缀的调试级别日志内容写入到指定的日志文件中。
     * 如果日志文件不存在，则会尝试创建它。
     *
     * @param logContent 要记录的日志内容
     */

    public static void debug(String logContent) {
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
                writer.write(timestamp +"[DEBUG]"+ logContent + "\n");
                System.out.println(timestamp +Color.color("[DEBUG]"+ logContent ,"WHITE"));
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
    public  static void command(String command){
        System.out.println(command);
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

