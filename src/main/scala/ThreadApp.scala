import akka.stream.scaladsl.{Sink, Source}

/**
  * Created by lukasz.drygala on 26/11/16.
  */
object ThreadApp extends App with AkkaConfig {
  println(Thread.currentThread().getName)

  val thread = Source
    .single("Hello World !")
    .map { s =>
      println(s"${Thread.currentThread().getName} => $s")
      s
    }
    .map { s =>
      println(s"${Thread.currentThread().getName} => $s")
      s
    }
    .to(Sink.foreach { s =>
      println(s"${Thread.currentThread().getName} => $s")
    })
    .run()
}
