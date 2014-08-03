package oi.serviceEnablers

import akka.actor.Actor
import spray.http._
import MediaTypes._
import spray.can._
import spray.json._
import HttpMethods._
import MyJsonProtocol._
import spray.http.Uri.Path
import spray.http.Uri.Path.Slash
import spray.http.Uri.Path.Segment
import spray.routing.HttpService
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutor
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class ServiceActor extends Actor with HumanResourcesHttpService {
  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  val futureContext = context.dispatcher

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

trait HumanResourcesHttpService extends HttpService {
  implicit val futureContext: ExecutionContextExecutor

  implicit val compactPrinter : JsonPrinter = CompactPrinter // http://spray.io/documentation/1.2.1/spray-httpx/spray-json-support/

  val myRoute =
    path("employee" / Segment) { id =>
      get {
        // Get resource by ID or return a 404
        complete {
          Employee.get(id)
        }
      } ~
        put {
          // Substitute the resource by the representation in the body. If a resource is created return 201
          requestUri { uri =>
            entity(as[Employee]) { employee =>
              Employee.put(employee).fold(complete(StatusCodes.Created, List(HttpHeaders.Location(uri)), employee))(_ => complete(employee))
            }
          }
        } ~
        delete {
          // Delete the resource if it exists, if it doesn't exist them return 404
          complete(Employee.delete(id))
        }

    } ~
      path("employees") {
        get {
          // search q parameter in employee name and return only the number of results specified in limit parameter
          complete(Employee.all)
        } ~
          post {
            // add an employee to the collection
            requestUri { uri =>
              entity(as[Employee]) { employee =>
                val count = Employee.all.count(_ => true) + 1
                complete(StatusCodes.Created, List(HttpHeaders.Location(uri.withPath(Uri.Path(s"/employee/$count")))), Employee.post(employee.copy(idNumber = Some(count.toString))))
              }
            }
          }
      }
}


