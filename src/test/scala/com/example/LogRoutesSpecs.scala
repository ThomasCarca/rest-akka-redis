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

    "should return a Logs type as json when no parameter given" in {
      val request = HttpRequest(uri = "/logs")

      request ~> routes ~> check {
        status              should be (StatusCodes.OK)
        contentType         should be (ContentTypes.`application/json`)
        entityAs[Logs]
      }
    }

    "should return a Logs type as json when limit parameter given" in {
      val request = HttpRequest(uri = "/logs?limit=10")

      request ~> routes ~> check {
        status              should be (StatusCodes.OK)
        contentType         should be (ContentTypes.`application/json`)
        entityAs[Logs]
      }
    }

    "should return a Logs type as json when level parameter given" in {
      val request = HttpRequest(uri = "/logs?level=WARNING")

      request ~> routes ~> check {
        status              should be (StatusCodes.OK)
        contentType         should be (ContentTypes.`application/json`)
        entityAs[Logs]
      }
    }

    "should return a Logs type as json when limit and level parameters given" in {
      val request = HttpRequest(uri = "/logs?limit=5&level=WARNING")

      request ~> routes ~> check {
        status              should be (StatusCodes.OK)
        contentType         should be (ContentTypes.`application/json`)
        entityAs[Logs]
      }
    }

    "should return a Logs type as json when incorrect limit or level parameters given" in {
      val request = HttpRequest(uri = "/logs?limit=incorrect&level=999")

      request ~> Route.seal(routes) ~> check {
        status              should be (StatusCodes.BadRequest)
        responseAs[String]  should be ("The query parameter 'limit' was malformed:\n'incorrect' is not a valid 32-bit signed integer value")
      }
    }
  }

  "GET /logs/number" should {

    "should return the number of logs" in {
      val request = HttpRequest(uri = "/logs/number")

      request ~> routes ~> check {
        status                                should be (StatusCodes.OK)
        entityAs[String].toInt                should be >= 0
      }
    }

  }

}