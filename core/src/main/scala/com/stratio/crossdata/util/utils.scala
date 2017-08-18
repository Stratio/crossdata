package com.stratio.crossdata.util

import scala.util.Try


object using {

  type AutoClosable = {def close(): Unit}

  def apply[A <: AutoClosable, B](resource: A)(code: A => B): B =
    try {
      code(resource)
    }
    finally {
      Try(resource.close())
    }

}

