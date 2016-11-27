import akka.stream.scaladsl.Source
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}

/**
  * Created by lukasz.drygala on 27/11/16.
  */
object StageApp extends App with AkkaConfig {

  Source(1 to 10)
    .via(new PrintStage[Int](
      elem => s"$elem on ${Thread.currentThread().getName}"
    ))
    .runForeach(println)

}

class PrintStage[T](printF: T => String) extends GraphStage[FlowShape[T, T]] {

  val inlet = Inlet[T]("printIn")
  val outlet = Outlet[T]("printOut")

  @scala.throws[Exception](classOf[Exception])
  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    setHandler(inlet, new InHandler {
      @scala.throws[Exception](classOf[Exception])
      override def onPush(): Unit = {
        val elem = grab(inlet)
        println(printF(elem))
        push(outlet, elem)
      }
    })

    setHandler(outlet, new OutHandler {
      @scala.throws[Exception](classOf[Exception])
      override def onPull(): Unit = {
        pull(inlet)
      }
    })
  }

  override def shape: FlowShape[T, T] = FlowShape.of(inlet, outlet)
}