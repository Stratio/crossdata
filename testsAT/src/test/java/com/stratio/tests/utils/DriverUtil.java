package com.stratio.tests.utils;

public class DriverUtil {
	private static DriverUtil instance = new DriverUtil();
	private final Driver cUtils = new Driver();

	private DriverUtil() {
	}

	public static DriverUtil getInstance() {
		return instance;
	}

	public Driver getDriver() {
		return cUtils;
	}
}
