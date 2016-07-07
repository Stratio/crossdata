/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata

import java.util.Map.Entry

import com.hazelcast.core.IMap
import com.stratio.crossdata.util.CacheInvalidator
import org.apache.spark.sql.SQLConf

class HazelcastSQLConf(hazelcastMap: IMap[String, String], invalidator: CacheInvalidator) extends SQLConf {

  import HazelcastSQLConf._

  def invalidateLocalCache: Unit = localMap.clear

  private val localMap =  java.util.Collections.synchronizedMap(new java.util.HashMap[String, String]())

  override protected[spark] val settings = {
    new ChainedJavaMapWithWriteInvalidation[String, String](Seq(localMap, hazelcastMap), invalidator)
  }

}

object HazelcastSQLConf {

  private case class NullBuilder[T]() {
    private var pNull: T = _
    lazy val nullval = pNull
  }

  class ChainedJavaMapWithWriteInvalidation[K,V](
                                                  private val delegatedMaps: Seq[java.util.Map[K,V]],
                                                  private val invalidator: CacheInvalidator
                                                )

    extends java.util.Map[K,V] {

    require(!delegatedMaps.isEmpty)

    import scala.collection.JavaConversions._

    override def values(): java.util.Collection[V] = (Set.empty[V] /: delegatedMaps) {
      case (values, delegatedMap) => values ++ delegatedMap.values
    }

    override def get(key: scala.Any): V = delegatedMaps.view.map(_.get(key)).find(_ != null).getOrElse {
      NullBuilder[V]().nullval
    }

    override def entrySet(): java.util.Set[Entry[K, V]] = (Set.empty[Entry[K,V]] /: delegatedMaps) {
      case (values, delegatedMap) => values ++ delegatedMap.entrySet()
    }


    override def put(key: K, value: V): V = {
      invalidator.invalidateCache
      (Option.empty[V] /: delegatedMaps) {
        case (prev, delegatedMap) =>
          val newRes = delegatedMap.put(key, value)
          prev orElse Option(newRes)
      } getOrElse(NullBuilder[V]().nullval)
    }

    override def clear(): Unit = {
      invalidator.invalidateCache
      delegatedMaps foreach(_.clear)
    }

    override def size(): Int = delegatedMaps.maxBy(_.size).size

    override def remove(key: scala.Any): V = {
      invalidator.invalidateCache
      delegatedMaps.map(_.remove(key)).head
    }

    override def containsKey(key: scala.Any): Boolean = delegatedMaps.view exists (_.containsKey(key))

    override def containsValue(value: scala.Any): Boolean = delegatedMaps.view exists (_.containsValue(value))

    override def isEmpty: Boolean = delegatedMaps forall (_.isEmpty)

    override def putAll(m: java.util.Map[_ <: K, _ <: V]): Unit = {
      invalidator.invalidateCache
      delegatedMaps foreach (_.putAll(m))
    }

    override def keySet(): java.util.Set[K] = (Set.empty[K] /: delegatedMaps) {
      case (keys, delegatedMap) => keys ++ delegatedMap.keySet()
    }

  }

}
