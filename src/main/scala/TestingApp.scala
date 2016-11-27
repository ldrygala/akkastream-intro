import akka.stream.scaladsl.{Flow, Keep}
import akka.stream.testkit.scaladsl.{TestSink, TestSource}

import scala.concurrent.Future

/**
  * Created by lukasz.drygala on 27/11/16.
  */
object TestingApp extends App with AkkaConfig {
  //  val sourceTest = Source(1 to 5)
  //    .via(Flow[Int]
  //      .map(_ * 2)
  //    )
  //    .toMat(TestSink.probe)(Keep.right)
  //    .run()
  //    .request(2)
  //    .expectNext(2, 4)

  //  val (sourceProbe, result) = TestSource.probe[Int]
  //    .toMat(
  //      Sink.fold(0)(_ + _)
  //    )(Keep.both)
  //    .run()
  //
  //  sourceProbe
  //    .sendNext(1)
  //    .sendNext(2)
  //    .sendComplete()
  //
  //  import scala.concurrent.duration._
  //
  //  assert(Await.result(result, 3.seconds) == 3)

  val flowUnderTest = Flow[Int]
    .mapAsyncUnordered(2) { n =>
      println(s"Element $n on ${Thread.currentThread().getName}")
      Future.successful(n * 2)
    }

  val (pub, sub) = TestSource.probe[Int]
    .via(flowUnderTest)
    .toMat(TestSink.probe[Int])(Keep.both)
    .run()

  sub.request(2)
  pub.sendNext(1)
  pub.sendNext(2)
  sub.request(2)
  pub.sendNext(3)
  pub.sendNext(4)
  private val cause = new Exception("Boom")
  pub.sendError(cause)
  sub.expectNext(2, 4, 6, 8).expectError(cause)

}
