import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Source}

/**
  * Created by lukasz.drygala on 25/11/16.
  */
object BuffersApp extends App with AkkaConfig {

  val fastSource = Source(1 to 100)
    // buffer for async boundary
    .buffer(10, OverflowStrategy.dropHead)
    .async

  val slowMap = Flow[Int].map { n =>
    Thread.sleep(1000)
    n * 2
  }

  fastSource.via(slowMap).runForeach(println)
}
