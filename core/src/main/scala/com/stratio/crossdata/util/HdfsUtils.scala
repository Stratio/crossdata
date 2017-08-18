package com.stratio.crossdata.util

import java.io.{BufferedInputStream, File, FileInputStream, InputStream}

import akka.event.slf4j.SLF4JLogging
import com.typesafe.config.Config
import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileStatus, FileSystem, Path}

import scala.util.Try

case class HdfsUtils(dfs: FileSystem, userName: String) {

  def getFiles(path: String): Array[FileStatus] = dfs.listStatus(new Path(path))

  def getFile(filename: String): InputStream = dfs.open(new Path(filename))

  def fileExist(fileName:String): Boolean = dfs.exists(new Path(fileName))

  def delete(path: String): Unit = {
    dfs.delete(new Path(path), true)
  }

  def write(path: String, destPath: String, overwrite: Boolean = false): Int = {
    val file = new File(path)
    val out = dfs.create(new Path(s"$destPath${file.getName}"))
    val in = new BufferedInputStream(new FileInputStream(file))
    val bytesCopied = Try(IOUtils.copy(in, out))
    IOUtils.closeQuietly(in)
    IOUtils.closeQuietly(out)
    bytesCopied.get
  }
}

object HdfsUtils extends SLF4JLogging {

  private final val DefaultFSProperty = "fs.defaultFS"
  private final val HdfsDefaultPort = 9000

  def apply(user: String, namenode:String): HdfsUtils = {
    val conf = new Configuration()
    conf.set(DefaultFSProperty, namenode)
    log.debug(s"Configuring HDFS with master: ${conf.get(DefaultFSProperty)} and user: $user")
    val defaultUri = FileSystem.getDefaultUri(conf)
    new HdfsUtils(FileSystem.get(defaultUri, conf, user), user)
  }

  def apply(config: Config): HdfsUtils = {
    val namenode=config.getString("namenode")
    val user = config.getString("user")
    apply(user, namenode)
  }
}