package com.example.utils

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import spray.json._
import scalaj.http._

object LogMessageGenerator {
  def message: Future[String] = {
    val response: Future[String] = Future { Http("http://api.icndb.com/jokes/random").asString.body }
    response.map{
      r => r.parseJson.asJsObject.fields("value").asJsObject.fields("joke").toString.replaceAll("^\"|\"$", "")
    }
  }
}
