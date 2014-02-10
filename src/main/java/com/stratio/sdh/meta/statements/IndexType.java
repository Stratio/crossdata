package com.stratio.sdh.meta.statements;

/**
 * Types of indexes supported by META.
 * <ul>
 *  <li> HASH: Default index created by Cassandra.
 *  <li> FULLTEXT: Full text index supported by Lucene.
 * </ul>
 */
public enum IndexType {
	/**
	 * Default index created by Cassandra.
	 */
	HASH,
	/**
	 * Full text index supported by Lucene.
	 */
	FULLTEXT
}
