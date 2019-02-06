package com.example.models

import java.time.{Instant, LocalDateTime}

case class Log(id: String, date: LocalDateTime, level: String, name: String, message: String) {
  def timeStamp: Long = Instant.now.getEpochSecond
}