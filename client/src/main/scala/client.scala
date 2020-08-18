import com.typesafe.config.ConfigFactory
import akka.actor.{ActorSystem, Props}

object Client extends App {

  val system = ActorSystem("MapReduce", ConfigFactory.load.getConfig("remotelookup"))
  val master = system.actorOf(Props[MasterActor], name = "master")

  master ! Book("The Pickwick Papers", "http://reed.cs.depaul.edu/lperkovic/gutenberg/pg580.txt")
  master ! Book("Life And Adventures of Martin Chuzzlewit", "http://reed.cs.depaul.edu/lperkovic/gutenberg/pg968.txt")
  master ! Book("Hunted Down", "http://reed.cs.depaul.edu/lperkovic/gutenberg/pg807.txt")
  master ! Book("A Tale of Two Cities", "http://reed.cs.depaul.edu/lperkovic/gutenberg/pg98.txt")
  master ! Book("The Cricket on the Hearth", "http://reed.cs.depaul.edu/lperkovic/gutenberg/pg20795.txt")
  master ! Book("Oliver Twist", "http://reed.cs.depaul.edu/lperkovic/gutenberg/pg730.txt")

  master ! Flush

}
