package com.example.routes

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.event.Logging
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout

import scala.concurrent.duration._
import com.example.actors.LogRegistryActor._
import com.example.models.Logs
import com.example.utils.JsonSupport

import scala.concurrent.Future

trait LogRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[LogRoutes])

  def logRegistryActor: ActorRef

  implicit lazy val timeout: Timeout = Timeout(5.seconds)

  val getLogs: Route =
    get {
      parameters('limit.as[Int].? , 'level.as[String].?) { (limit, level) =>
        val logs: Future[Logs] = (logRegistryActor ? GetLogs(limit, level)).mapTo[Logs]
        complete(logs)
      }
  }

  val getLogNumber: Route =
    get {
        val number: Future[String] = (logRegistryActor ? GetLogNumber).mapTo[String]
        complete(number)
    }

  lazy val logRoutes: Route =
    pathPrefix("logs") {
      pathEndOrSingleSlash {
        getLogs
      } ~
      path("number") {
        getLogNumber
      }
    }
}
