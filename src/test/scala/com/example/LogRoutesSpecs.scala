package com.example

import akka.actor.ActorRef
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.Route
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import com.example.actors.LogRegistryActor
import com.example.models.Logs
import com.example.routes.LogRoutes

class LogRoutesSpecs extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest
  with LogRoutes {

  override val logRegistryActor: ActorRef =
    system.actorOf(LogRegistryActor.props, "logRegistryActor")

  lazy val routes: Route = logRoutes

  "GET /logs" should {

    "should return a Logs type as json" in {
      val request = HttpRequest(uri = "/logs")

      request ~> routes ~> check {
        status              should be (StatusCodes.OK)
        contentType         should be (ContentTypes.`application/json`)
        entityAs[Logs]
      }
    }
  }

}