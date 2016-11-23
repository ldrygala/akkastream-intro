import java.time.LocalDateTime

import akka.actor.Props
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

  val printSink = Sink.foreach(println)

  val single = Source.single("Hello World")
  val iterable = Source(1 to 10)
  val iterator = Source.fromIterator(() => Iterator.from(1))
  val future = Source.fromFuture(Future(1 to 10))
  val tick = Source.tick(1.second, 2.second, LocalDateTime.now())
  val cycle = Source.cycle(() => (1 to 5).iterator)
  val actor = Source.actorPublisher[String](Props(classOf[StringPublisherActor]))

  //  cycle.to(printSink).run()

  val actorRef = actor.to(printSink).run()
  actorRef ! "Msg1"
  actorRef ! "Msg2"
  actorRef ! "Msg3"
  actorRef ! "Msg4"
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
