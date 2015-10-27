package com.stratio.tests.utils;

/**
 * Created by hdominguez on 13/10/15.
 */
public class XDContextUtil {
    private static XDContextUtil instance = new XDContextUtil();
    private final XDContextUtils cUtils = new XDContextUtils();

    private XDContextUtil() {
    }

    public static XDContextUtil getInstance() {
        return instance;
    }

    public XDContextUtils getXdContext() {
        return cUtils;
    }

}
