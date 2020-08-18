import akka.actor.{Actor, ActorRef}
import akka.routing.Broadcast
import scala.collection.mutable.HashSet
import scala.io.Source

class MapActor(hashRouter: ActorRef) extends Actor {

  println(self.path)

  Thread sleep 2000

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be", "do", "get", "if", "in", "is", "it", "of", "on", "the", "to")

  def receive = {
    case Book(title, url) =>
      process(title, url)
    case Flush =>
      hashRouter ! Broadcast(Flush)
  }

  def process(title: String, url: String) = {
    val content = getContent(url)
    var namesFound = HashSet[String]()
    for(word <- content.split("[\\p{Punct}\\s]+")) {
      if((!STOP_WORDS_LIST.contains(word)) && word(0).isUpper && !namesFound.contains(word)) {
        hashRouter ! Name(word, title)
        namesFound += word
      }
    }
  }

  def getContent(url: String) = {
    try {
      Source.fromURL(url).mkString
    } catch {
      case e: Exception => ""
    }
  }
}
