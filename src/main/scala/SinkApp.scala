import akka.actor.{Actor, Props}
import akka.stream.actor.ActorSubscriberMessage.{OnComplete, OnError, OnNext}
import akka.stream.actor.{ActorSubscriber, RequestStrategy, WatermarkRequestStrategy}
import akka.stream.scaladsl.{Sink, Source}

/**
  * Created by lukasz.drygala on 24/11/16.
  */
object SinkApp extends App with AkkaConfig {

  val source = Source(1 to 10)

  //  source.runWith(Sink.head).foreach(println)
  //
  //  source.runWith(Sink.fold(1)(_ * _)).foreach(println)
  //
  //  source.runWith(Sink.ignore).foreach(println)
  //
  //  source.runWith(Sink.foreach(println)).foreach(println)
  //
  //  source.runWith(Sink.actorRef(system.actorOf(Props(new PrintActor), "printActor"), "stop"))

  Source(List(1,2,3,0,1,2,3)).map(10 / _).runWith(Sink.actorSubscriber(Props(new PrintSubscriberActor)))

}

class PrintActor extends Actor {
  override def receive: Receive = {
    case n: Int => println(n)
    case "stop" => {
      println("time to die")
      context.stop(self)
    }
  }
}

class PrintSubscriberActor extends ActorSubscriber {
  override protected def requestStrategy: RequestStrategy = WatermarkRequestStrategy(2)

  override def receive: Receive = {
    case OnNext(msg) => println(msg)
    case OnComplete => context.stop(self)
    case OnError(cause) => println(cause)
  }
}