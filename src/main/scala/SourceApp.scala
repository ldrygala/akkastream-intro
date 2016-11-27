import java.time.LocalDateTime

import akka.actor.Props
import akka.stream.OverflowStrategy
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.{Cancel, Request}
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future

/**
  * Created by lukasz.drygala on 22/11/16.
  */
object SourceApp extends App with AkkaConfig {

  import system.dispatcher

  import scala.concurrent.duration._

  Source.single("Hello World").runForeach(println)

  Source(1 to 10).runForeach(println)

  Source.fromIterator(() => Iterator.from(1)).runForeach(println)

  Source.fromFuture(Future(1 to 10)).runForeach(println)

  Source.tick(1.second, 2.second, LocalDateTime.now()).runForeach(println)

  Source.cycle(() => (1 to 5).iterator).runForeach(println)

  val actorRef = Source.actorRef(3, OverflowStrategy.dropNew).to(Sink.foreach(println)).run()
  actorRef ! "Msg1"
  actorRef ! "Msg2"
  actorRef ! "Msg3"
  actorRef ! "Msg4"
  actorRef ! "Msg5"
  actorRef ! "Msg6"
  actorRef ! "Msg7"

  val actorPublisher = Source.actorPublisher[String](Props(new StringPublisherActor)).to(Sink.foreach(println)).run()

  actorPublisher ! "Msg1"
  actorPublisher ! "Msg2"
  actorPublisher ! "Msg3"
  actorPublisher ! "Msg4"
}

class StringPublisherActor extends ActorPublisher[String] {
  val buffer = Vector.newBuilder[String]

  override def receive: Receive = {
    case msg: String => if (totalDemand > 0) onNext(msg) else buffer += msg
    case Request(_) => {
      buffer.result().take(totalDemand.toInt).foreach(onNext(_))
      buffer.clear()
    }
    case Cancel => context.stop(self)
  }
}