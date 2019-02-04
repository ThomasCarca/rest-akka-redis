package com.example

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import akka.pattern.ask
import akka.util.Timeout
import com.example.LogRegistryActor.GetLogs

trait LogRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[LogRoutes])

  def logRegistryActor: ActorRef

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val logRoutes: Route =
    pathPrefix("logs") {
      pathEndOrSingleSlash {
        get {
          val logs: Future[Logs] =
            (logRegistryActor ? GetLogs).mapTo[Logs]
          complete(logs)
        }
      }
    }
}