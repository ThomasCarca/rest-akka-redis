package com.example.actors

import java.time.LocalDateTime

import akka.actor.{Actor, ActorRef, Props}
import com.example.actors.LogRegistryActor.Add
import com.example.models.Log
import java.util.UUID.randomUUID
import scala.util.Random

object LogGeneratorActor {
  final case object GenerateLog

  def props(logRegistryActor: ActorRef): Props = Props(new LogGeneratorActor(logRegistryActor))
}

class LogGeneratorActor(logRegistryActor: ActorRef) extends Actor {
  import LogGeneratorActor.GenerateLog

  private val LEVELS = Set("DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL")
  private val NAMES = Set("store_app", "inventory_app", "transaction_app")

  private def generateLog: Log = {
    val generatedId = randomUUID.toString
    val generatedDate = LocalDateTime.now()
    val generatedLevel = LEVELS.toVector(Random.nextInt(LEVELS.size))
    val generatedName = NAMES.toVector(Random.nextInt(NAMES.size))
    val generatedMessage = "Constant message..."
    Log(generatedId, generatedDate,generatedLevel,generatedName,generatedMessage)
  }

  def receive: Receive = {
    case GenerateLog =>
      logRegistryActor ! Add(generateLog)
  }

}
