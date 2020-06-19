package net.judgeglass.vproxy.server.utils;

public class Logger {
    public static void info(String text){
        System.out.println("[INFO: ] " + text);
    }

    public static void warning(String text){
        System.out.println("[WARN: ] " + text);
    }

    public static void error(String text){
        System.out.println("[ERROR: ] " + text);
    }
}
