package com.example

import java.time.LocalDateTime

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.example.actors.LogGeneratorActor.GenerateLog
import com.example.actors.{LogGeneratorActor, LogRegistryActor}
import com.example.routes.LogRoutes

import scala.concurrent.duration._

object QuickstartServer extends App with LogRoutes {
  import system.dispatcher

  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val logRegistryActor: ActorRef = system.actorOf(LogRegistryActor.props, "logRegistryActor")
  val logGeneratorActor: ActorRef = system.actorOf(LogGeneratorActor.props(logRegistryActor), "logGeneratorActor")

  lazy val routes: Route = logRoutes

  Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

  system.scheduler.schedule(2.second, 2.seconds) {
    logGeneratorActor ! GenerateLog
    println(s"[${LocalDateTime.now()}] A new log has been generated...")
  }

  Await.result(system.whenTerminated, Duration.Inf)

}
