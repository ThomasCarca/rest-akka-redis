package com.example.actors

import akka.actor.{Actor, Props}
import com.example.models.{Log, Logs}
import com.redis._
import spray.json._
import com.example.utils.JsonSupport
import com.example.utils.LogLevelFormatter._

object LogRegistryActor {
  final case class GetLogs(limit: Option[Int], level: Option[String])
  final case class Add(log: Log)

  def props: Props = Props[LogRegistryActor]
}

class LogRegistryActor extends Actor with JsonSupport {
  import LogRegistryActor._

  var redis = new RedisClient("localhost", 6379)

  def sendBackLogs(maybeLogs: Option[List[String]]) = {
    maybeLogs match {
      case Some(logList) =>
        val parsedLogList = logList.map(_.parseJson.convertTo[Log])
        sender() ! Logs(parsedLogList)
      case None => sender() ! Logs(Seq.empty)
    }
  }

  def receive: Receive = {

    case GetLogs(Some(limit), Some(level)) =>
      val logs = redis.zrangebyscore("logs_by_level", stringLevelToInt(level), true, 50, true, Some(0, limit))
      sendBackLogs(logs)

    case GetLogs(Some(limit), None) =>
      val logs = redis.zrange("logs_by_date", 0, limit-1)
      sendBackLogs(logs)

    case GetLogs(None, Some(level)) =>
      val logs = redis.zrangebyscore("logs_by_level", stringLevelToInt(level), true, 50, true, None)
      sendBackLogs(logs)

    case GetLogs(None, None) =>
      val logs = redis.zrange("logs_by_date")
      sendBackLogs(logs)

    case Add(log) =>
      redis.zadd("logs_by_date", log.timeStamp, log.toJson)
      redis.zadd("logs_by_level", stringLevelToInt(log.level), log.toJson)
  }
}