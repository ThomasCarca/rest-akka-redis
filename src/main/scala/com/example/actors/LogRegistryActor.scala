package com.example.actors

import akka.actor.{Actor, Props}
import com.example.models.{Log, Logs}
import com.redis._
import spray.json._
import com.example.utils.JsonSupport

object LogRegistryActor {
  final case object GetLogs
  final case class Add(log: Log)

  def props: Props = Props[LogRegistryActor]
}

class LogRegistryActor extends Actor with JsonSupport {
  import LogRegistryActor._

  var redis = new RedisClient("localhost", 6379)

  def receive: Receive = {
    case GetLogs =>
      val logs = redis.zrange("logs_by_date")
      logs match {
        case Some(logList) =>
          val parsedLogList = logList.map(_.parseJson.convertTo[Log])
          sender() ! Logs(parsedLogList)
        case None => sender() ! Logs(Seq.empty)
      }

    case Add(log) =>
      redis.zadd("logs_by_date", log.timeStamp, log.toJson)
      redis.zadd("logs_by_level", log.levelInt, log.toJson)
  }
}