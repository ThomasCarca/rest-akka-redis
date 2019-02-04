package com.example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import spray.json.{JsValue, JsonFormat, _}


trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit object LocalDateTimeFormat extends JsonFormat[LocalDateTime] {

    def write(dateTime: LocalDateTime) = JsString(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

    def read(value: JsValue) = value match {
      case JsString(dateTime) => LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
      case _ => deserializationError("LocalDateTime expected.")
    }

  }

  implicit val logJsonFormat = jsonFormat5(Log)
  implicit val logsJsonFormat = jsonFormat1(Logs)


}
