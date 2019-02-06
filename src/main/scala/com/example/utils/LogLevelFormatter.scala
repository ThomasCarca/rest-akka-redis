package com.example.utils



object LogLevelFormatter {
  val DEBUG = "DEBUG"
  val INFO = "INFO"
  val WARNING = "WARNING"
  val ERROR = "ERROR"
  val CRITICAL = "CRITICAL"
  val UNDEFINED = "UNDEFINED"

  def stringLevelToInt(level: String): Int = {
    level match {
      case DEBUG => 0
      case INFO => 10
      case WARNING => 20
      case ERROR => 30
      case CRITICAL => 40
      case _ => 50
    }
  }

  def intLevelToString(level: Int): String = {
    level match {
      case 0 => DEBUG
      case 10 => INFO
      case 20 => WARNING
      case 30 => ERROR
      case 40 => CRITICAL
      case _ => UNDEFINED
    }
  }

}