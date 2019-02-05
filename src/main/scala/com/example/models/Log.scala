package com.example.models

import java.time.{Instant, LocalDateTime}

case class Log(id: String, date: LocalDateTime, level: String, name: String, message: String) {
  import Log._
  def levelInt: Int = levelToInt(level)
  def timeStamp: Long = Instant.now.getEpochSecond
}

object Log {
  private def levelToInt(level: String): Int = {
    level match {
      case "DEBUG" => 0
      case "INFO" => 10
      case "WARNING" => 20
      case "ERROR" => 30
      case "CRITICAL" => 40
      case _ => 50
    }
  }
}