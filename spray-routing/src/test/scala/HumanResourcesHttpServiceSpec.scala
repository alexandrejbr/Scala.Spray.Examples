import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import oi.serviceEnablers.HumanResourcesHttpService
//import org.junit.runner.RunWith
//import org.specs2.runner.JUnitRunner
import oi.serviceEnablers.MyJsonProtocol._
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import oi.serviceEnablers.Employee
import MediaTypes._

//@RunWith(classOf[JUnitRunner])
class HumanResourcesHttpServiceSpec extends Specification with Specs2RouteTest with HumanResourcesHttpService {
  def actorRefFactory = system
  
  val futureContext = system.dispatcher

  "Human Resources service" should {

    "return all employees when receives a GET request to /employees" in {
      Get("/employees") ~> myRoute ~> check {
        mediaType === `application/json`
        status === OK
        responseAs[String] must contain("Alice")
      }
    }

    "return a greeting for GET requests to the root path" in {
      Put("/employee/4", Employee("Pedro", Some("4"), "SAPO", 170000, 22)) ~> myRoute ~> check {
        mediaType === `application/json`
        status === Created
        responseAs[String] must contain("Pedro")
      }
    }

    //    "leave GET requests to other paths unhandled" in {
    //      Get("/kermit") ~> myRoute ~> check {
    //        handled must beFalse
    //      }
    //    }
    //
    //    "return a MethodNotAllowed error for PUT requests to the root path" in {
    //      Put() ~> sealRoute(myRoute) ~> check {
    //        status === MethodNotAllowed
    //        responseAs[String] === "HTTP method not allowed, supported methods: GET"
    //      }
    //    }
  }
}