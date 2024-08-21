package com.JohnRichard.utmLogin;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;

public class YamlEditor {

    public static void editYamlValue(String filePath, String key, Object value) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(YamlEditor.class.getResourceAsStream(filePath));

            data.put(key, value);

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);

            FileWriter writer = new FileWriter(filePath);
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listAdd(String filePath, String listKey, String value) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(new File(filePath));

            Map<String, Object> data = yaml.load(inputStream);
            if (data != null) {
                String[] keys = listKey.split("\\.");
                Map<String, Object> currentMap = data;
                for (int i = 0; i < keys.length - 1; i++) {
                    currentMap = (Map<String, Object>) currentMap.get(keys[i]);
                }

                List<String> list = (List<String>) currentMap.get(keys[keys.length - 1]);
                list.add(value);

                FileWriter writer = new FileWriter(filePath);
                yaml.dump(data, writer);

                writer.close();
                inputStream.close();
            } else {
                System.out.println("Empty YAML file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listDel(String filePath, String listKey, String value) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(new File(filePath));

            Map<String, Object> data = yaml.load(inputStream);
            if (data != null) {
                String[] keys = listKey.split("\\.");
                Map<String, Object> currentMap = data;
                for (int i = 0; i < keys.length - 1; i++) {
                    currentMap = (Map<String, Object>) currentMap.get(keys[i]);
                }

                List<String> list = (List<String>) currentMap.get(keys[keys.length - 1]);
                if (list.contains(value)) {
                    list.remove(value);

                    FileWriter writer = new FileWriter(filePath);
                    yaml.dump(data, writer);

                    writer.close();
                    inputStream.close();
                } else {
                    System.out.println("Value not found in the list.");
                }
            } else {
                System.out.println("Empty YAML file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
//如何调用？
    public static void main(String[] args) {
        // 调用editYamlValue方法来编辑YAML文件中的值
        editYamlValue("sample.yml", "listKey", new String[]{"value1", "value2"});
        editYamlValue("sample.yml", "booleanKey", true);
        editYamlValue("sample.yml", "integerKey", 123);
        editYamlValue("sample.yml", "doubleKey", 3.14);

        listAdd("path/to/your/file.yml", "example.example.list", "newValue");
        listDel("path/to/your/file.yml", "example.example.list", "valueToRemove");
    }

 */
}