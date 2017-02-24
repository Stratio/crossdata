package org.apache.spark.sql.crossdata.utils

import org.apache.spark.util.Utils

import scala.reflect.ClassTag
import scala.util.control.NonFatal

/**
  * This object collects dynamic class loading helper methods
  * found across Spark SQL
  */
object Reflect {


  /**
    * Helper method to create an instance of [[T]] using a single-arg constructor that
    * accepts an [[Arg]].
    */
  def reflect[T, Arg <: AnyRef](
                                         className: String,
                                         ctorArg: Arg)(implicit ctorArgTag: ClassTag[Arg]): T = {
    try {
      val clazz = Utils.classForName(className)
      val ctor = clazz.getDeclaredConstructor(ctorArgTag.runtimeClass)
      ctor.newInstance(ctorArg).asInstanceOf[T]
    } catch {
      case NonFatal(e) =>
        throw new IllegalArgumentException(s"Error while instantiating '$className':", e)
    }
  }

  /**
    * Helper method to create an instance of [[T]] using a single-arg constructor that
    * accepts an [[Arg1]] and an [[Arg2]].
    */
  def reflect[T, Arg1 <: AnyRef, Arg2 <: AnyRef](
                                                  className: String,
                                                  ctorArg1: Arg1,
                                                  ctorArg2: Arg2)(
                                                  implicit ctorArgTag1: ClassTag[Arg1],
                                                  ctorArgTag2: ClassTag[Arg2]): T = {
    try {
      val clazz = Utils.classForName(className)
      val ctor = clazz.getDeclaredConstructor(ctorArgTag1.runtimeClass, ctorArgTag2.runtimeClass)
      val args = Array[AnyRef](ctorArg1, ctorArg2)
      ctor.newInstance(args: _*).asInstanceOf[T]
    } catch {
      case NonFatal(e) =>
        throw new IllegalArgumentException(s"Error while instantiating '$className':", e)
    }
  }

}
