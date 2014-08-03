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
      val validCredentials = BasicHttpCredentials("Admin", "password")
      Put("/employee/4", Employee("Peter", Some("4"), "Financial", 170000, 22)) ~>
        addCredentials(validCredentials) ~> myRoute ~> check {
          mediaType === `application/json`
          status === Created
          responseAs[String] must contain("Peter")
      }
    }

    "writes without providing credentials receive 401" in {
      Put("/employee/4", Employee("Pedro", Some("4"), "Financial", 170000, 22)) ~> myRoute ~> check {
        status === StatusCodes.Unauthorized
        responseAs[String] === "The resource requires authentication, which was not supplied with the request"
        header[HttpHeaders.`WWW-Authenticate`].get.challenges.head === HttpChallenge("Basic", "writes must be authenticated")
      }
    }

    "writes with bad credentials receive 401" in {
      val invalidCredentials = BasicHttpCredentials("Peter", "pan")
      Put("/employee/4", Employee("Pedro", Some("4"), "Financial", 170000, 22)) ~>
        addCredentials(invalidCredentials) ~> myRoute ~> check {
          status === StatusCodes.Unauthorized
          responseAs[String] === "The supplied authentication is invalid"
          header[HttpHeaders.`WWW-Authenticate`].get.challenges.head === HttpChallenge("Basic", "writes must be authenticated")
      }
    }

  }
}