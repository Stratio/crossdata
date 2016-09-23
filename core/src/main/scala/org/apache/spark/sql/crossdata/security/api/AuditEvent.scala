
package org.apache.spark.sql.crossdata.security.api

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JNull
import org.json4s.JsonAST.JString

case class AuditEvent(time: Time, userId: String, resource: Resource, action: Action, result: Result,
                      auditAddresses: AuditAddresses, policy: Option[String] = None,
                      impersonation: Option[Boolean] = None)

case class AuditAddresses(sourceIp: String, destIp: String)

case class Time(date: Date)

object Time {

  def getIsoFormat: SimpleDateFormat = {
    val isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    isoFormat.setTimeZone(TimeZone.getDefault)
    isoFormat
  }
}

object TimeSerializer extends CustomSerializer[Time](format =>
  ( {
    case JString(s) => {
      val isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
      isoFormat.setTimeZone(TimeZone.getDefault)
      Time(Time.getIsoFormat.parse(s))
    }
    case JNull => Time(new Date())
  }, {
    case x: Time => {
      JString(Time.getIsoFormat.format(x.date))
    }
  }
    )
)