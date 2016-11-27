import akka.stream.Supervision.{Resume, Stop}
import akka.stream.scaladsl.Source
import akka.stream.{ActorAttributes, Supervision}

/**
  * Created by lukasz.drygala on 26/11/16.
  */
object SupervisioningApp extends App with AkkaConfig {

  val decider: Supervision.Decider = {
    case _: ArithmeticException => Resume
    case _ => Stop
  }

  Source(List(1, 2, 3, 0, 3, 2, 1))
    .map(10 / _)
    .withAttributes(ActorAttributes.supervisionStrategy(decider))
    .runForeach(println)
}
