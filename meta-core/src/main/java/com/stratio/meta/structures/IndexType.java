package com.stratio.meta.structures;

/**
 * Types of indexes supported by META.
 * <ul>
 *  <li> DEFAULT: Default index created by Cassandra.
 *  <li> LUCENE: Index supported by Lucene.
 * </ul>
 */
public enum IndexType {
	/**
	 * Default index created by Cassandra.
	 */
	DEFAULT,
	/**
	 * Lucene backed index.
	 */
	LUCENE
}
