package com.example

import akka.actor.{Actor, ActorLogging, Props}
import java.time.LocalDateTime

final case class Log(id: String, date: LocalDateTime, level: String, name: String, message: String)
final case class Logs(logs: Seq[Log])

object LogRegistryActor {
  final case object GetLogs
  final case class Add(log: Log)

  def props: Props = Props[LogRegistryActor]
}

class LogRegistryActor extends Actor {
  import LogRegistryActor._

  var logs = Set.empty[Log]

  def receive: Receive = {
    case GetLogs =>
      sender() ! Logs(logs.toSeq)
    case Add(log) =>
      logs = logs + log
  }
}