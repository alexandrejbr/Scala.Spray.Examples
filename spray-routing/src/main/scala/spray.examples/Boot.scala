package spray.examples

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.routing.RoundRobinPool
import akka.routing.DefaultResizer
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot extends App with ServerSslConfiguration {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[ServiceActor], "human-resources-service")

  //val resizer = DefaultResizer(lowerBound = 1, upperBound = 8)
  
  //val service = system.actorOf(RoundRobinPool(4, Some(resizer)).props(Props[ServiceActor]), "router")
  
  val interface = system.settings.config.getString("app.interface")
  val port = system.settings.config.getInt("app.port")
  
  implicit val timeout = Timeout(5 seconds)

  IO(Http) ? Http.Bind(service, interface, port)

  def close(args: Array[String]) = {
   system.shutdown()
  }
}