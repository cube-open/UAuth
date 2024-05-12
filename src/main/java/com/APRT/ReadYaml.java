package com.APRT;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class ReadYaml {
    public static String readYamlString(String path, String yamlLocation) {
        Yaml yaml = new Yaml();

        try {
            FileInputStream input = new FileInputStream(path);
            Map<String, Object> data = yaml.load(input);

            String[] keys = yamlLocation.split("\\.");
            Map<String, Object> current = data;

            for (String key : keys) {
                if (current.containsKey(key)) {
                    Object value = current.get(key);
                    if (value instanceof Map) {
                        current = (Map<String, Object>) value;
                    } else if (value instanceof String) {
                        return (String) value;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object readYamlValue(String path, String yamlLocation) {
        Yaml yaml = new Yaml();

        try {
            FileInputStream input = new FileInputStream(path);
            Map<String, Object> data = yaml.load(input);

            String[] keys = yamlLocation.split("\\.");
            Map<String, Object> current = data;

            for (String key : keys) {
                if (current.containsKey(key)) {
                    Object value = current.get(key);
                    if (value instanceof Map) {
                        current = (Map<String, Object>) value;
                    } else {
                        return value;
                    }
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean readYamlBoolean(String path, String yamlLocation) {
        Object value = readYamlValue(path, yamlLocation);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return false;
        }
    }
    /*
    //如何调用？
    public static void main(String[] args) {
        String stringValue = readYamlString("config/config.yml", "Config.autoBackup.Enable");
        System.out.println("String value: " + stringValue);

        Object value = readYamlValue("config/config.yml", "Config.autoBackup.time");
        System.out.println("Value: " + value);

        boolean booleanValue = readYamlBoolean("config/config.yml", "Config.autoBackup.Enable");
        System.out.println("Boolean value: " + booleanValue);

     */
    }
