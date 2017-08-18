package com.stratio.qa.utils;

public class XDContextUtil {

    private static XDContextUtil instance = new XDContextUtil();
    private final XDContextUtils cUtils = new XDContextUtils();
    private final XDJavaDriver xdDriver = new XDJavaDriver();

    private XDContextUtil() {
    }

    public static XDContextUtil getInstance() {
        return instance;
    }

    public XDContextUtils getXdContext() {
        return cUtils;
    }

    public XDJavaDriver getXdDriver() {
        return xdDriver;
    }

}
