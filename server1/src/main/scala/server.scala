import com.typesafe.config.ConfigFactory
import akka.actor.{Actor, ActorSystem}

object Server extends App {
  val system = ActorSystem("MapReduce", ConfigFactory.load.getConfig("server"))
  println("Server ready")
}
