package com.example.actors

import akka.actor.{Actor, Props}
import com.example.models.{Log, Logs}

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