import akka.stream.scaladsl.{Flow, Source}

/**
  * Created by lukasz.drygala on 25/11/16.
  */
object FlowApp extends App with AkkaConfig {
  val source = Source(1 to 20)

  //    val map = source
  //      .via(Flow[Int]
  //        .map(_ * 2))
  //      .runForeach(println)

  //    val filter = source
  //      .via(Flow[Int]
  //        .filter(_ % 2 == 0))
  //      .runForeach(println)

  //    val mapConcat = source
  //      .via(Flow[Int]
  //        .mapConcat(n => 1 to n))
  //      .runForeach(println)

  //  val throttle = source
  //    .via(Flow[Int]
  //      .map { n =>
  //        println(s"sent $n at ${System.currentTimeMillis()}")
  //        n
  //      })
  //    .throttle(1, 2.seconds, 4, ThrottleMode.shaping).async
  //    .runForeach { n =>
  //      println(s"received $n at ${System.currentTimeMillis()}")
  //    }

  //  val expend = source.throttle(1, 1.second, 1, ThrottleMode.shaping)
  //    .via(Flow[Int]
  //      .expand(n => Iterator.continually(n)))
  //    .runForeach(println)

  val groupByAndConflate = Source((1 to 100)
    .map(_ % 5))
    .via(Flow[Int]
      .groupBy(5, identity)
      .map { n =>
        println(s"sent $n")
        n
      }
      .conflate((oldElem, newElem) => newElem)
      .via(Flow[Int]
        .map { n =>
          Thread.sleep(1000)
          n
        }
        .async)
      .mergeSubstreams)
    .runForeach(println)
}
