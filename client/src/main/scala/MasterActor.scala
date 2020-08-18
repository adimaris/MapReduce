import akka.actor.{Actor, ActorRef, Address, Props}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import akka.routing.{Broadcast, ConsistentHashingPool, RoundRobinPool}
import com.typesafe.config.ConfigFactory

class MasterActor extends Actor {

  val numberMappers = ConfigFactory.load.getInt("number-mappers")
  val numberReducers = ConfigFactory.load.getInt("number-reducers")
  var pending = numberReducers

  val addresses = Seq(
    Address("akka", "MapReduce", "127.0.0.1", 25520),
    Address("akka", "MapReduce"),
    Address("akka", "MapReduce", "127.0.0.1", 25530)
  )

  def hashMapping: ConsistentHashMapping = {
    case Name(word, title) => word
  }

  val hashRouter = context.actorOf(RemoteRouterConfig(ConsistentHashingPool(numberReducers, hashMapping = hashMapping), addresses).props(Props(classOf[ReduceActor])))
  val routerRemote = context.actorOf(RemoteRouterConfig(RoundRobinPool(numberMappers), addresses).props(Props(classOf[MapActor], hashRouter)))


  def receive = {
    case Book(title, url) =>
      routerRemote ! Book(title, url)
    case Flush =>
      routerRemote ! Broadcast(Flush)
    case Done =>
      pending -= 1
      if (pending == 0)
        context.system.terminate
  }
}
