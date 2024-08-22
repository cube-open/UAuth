package com.cubeStudio.UAuth;

public  class Color {

    private static String output = "";
    /**
     * 应用指定的颜色或样式到给定的文本上，并返回。
     *
     * @param text  要着色的文本内容。
     * @param color 指定的颜色或样式名称，支持以下选项：
     *              <ul>
     *                  <li>RED</li>
     *                  <li>GREEN</li>
     *                  <li>YELLOW</li>
     *                  <li>BLUE</li>
     *                  <li>PURPLE</li>
     *                  <li>CYAN</li>
     *                  <li>WHITE</li>
     *                  <li>RESET</li>
     *                  <li>BOLD</li>
     *                  <li>UNDERLINE</li>
     *                  <li>ITALIC</li>
     *                  <li>INVERT</li>
     *                  <li>STRIKE</li>
     *                  <li>BLINK</li>
     *                  <li>CLEAR</li>
     *                  <li>CLEAR_SCREEN</li>
     *                  <li>CLEAR_LINE</li>
     *              </ul>
     *              如果提供了不支持的颜色或样式，则会抛出运行时异常。
     * @return
     */
    public  static String color(String text, String color){
        switch (color){
            case("RED")-> output = "\u001B[31m"+text+"\u001B[0m";
            case("GREEN")-> output = "\u001B[32m"+text+"\u001B[0m";
            case("YELLOW")-> output = "\u001B[33m"+text+"\u001B[0m";
            case("BLUE")-> output = "\u001B[34m"+text+"\u001B[0m";
            case("PURPLE")-> output = "\u001B[35m"+text+"\u001B[0m";
            case("CYAN")-> output = "\u001B[36m"+text+"\u001B[0m";
            case("WHITE")-> output = "\u001B[37m"+text+"\u001B[0m";
            case ("RESET")-> output = "\u001B[0m"+text+"\u001B[0m";
            case ("BOLD")-> output = "\u001B[1m"+text+"\u001B[0m";
            case ("UNDERLINE")-> output = "\u001B[4m"+text+"\u001B[0m";
            case ("ITALIC")-> output = "\u001B[3m"+text+"\u001B[0m";
            case ("INVERT")-> output = "\u001B[7m"+text+"\u001B[0m";
            case ("STRIKE")-> output = "\u001B[9m"+text+"\u001B[0m";
            case ("BLINK")-> output = "\u001B[5m"+text+"\u001B[0m";
            case ("CLEAR"), ("CLEAR_SCREEN") -> output = "\u001B[2J\u001B[H"+text+"\u001B[0m";
            case ("CLEAR_LINE")-> output = "\u001B[2K"+text+"\u001B[0m";
            default -> {
                try {
                    throw new Exception("ColorNotFoundException");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return output;
    }
}
