package com.example.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.example.models.{Log, Logs}
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, RootJsonFormat, deserializationError}

trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._

  implicit object LocalDateTimeFormat extends JsonFormat[LocalDateTime] {

    def write(dateTime: LocalDateTime) = JsString(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

    def read(value: JsValue): LocalDateTime = value match {
      case JsString(dateTime) => LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
      case _ => deserializationError("LocalDateTime expected.")
    }

  }

  implicit val logJsonFormat: RootJsonFormat[Log] = jsonFormat5(Log)
  implicit val logsJsonFormat: RootJsonFormat[Logs] = jsonFormat1(Logs)


}
