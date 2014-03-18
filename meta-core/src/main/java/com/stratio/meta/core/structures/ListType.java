package com.stratio.meta.core.structures;

/**
 * Types of list processes supported by META.
 * <ul>
 *  <li> PROCESS: List the existing running queries.
 *  <li> UDF: List the existing UDF in the system.
 *  <li> TRIGGER: List the existing triggers in the system.
 * </ul>
 */
public enum ListType {
	/**
	 * List all running processes. 
	 */
	PROCESS,
	/**
	 * List all known UDF.
	 */
	UDF,
	/**
	 * List all known triggers.
	 */
	TRIGGER;
}
