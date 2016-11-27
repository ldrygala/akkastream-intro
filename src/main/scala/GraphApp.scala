import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink, Source, ZipWith}
import akka.stream.{ClosedShape, UniformFanInShape}

/**
  * Created by lukasz.drygala on 27/11/16.
  */
object GraphApp extends App with AkkaConfig {

  //  in0 =>
  //          zip1 => out1 => in0 =>
  //                                  zip2 => out2
  //                          in1 =>
  //  in1 =>
  val maxOfThree = GraphDSL.create() { implicit builder =>

    import GraphDSL.Implicits._

    val zip1 = builder.add(ZipWith[Int, Int, Int]((e1, e2) => math.max(e1, e2)))
    val zip2 = builder.add(ZipWith[Int, Int, Int]((e1, e2) => math.max(e1, e2)))

    zip1.out ~> zip2.in0

    UniformFanInShape(zip2.out, zip1.in0, zip1.in1, zip2.in1)
  }

  RunnableGraph.fromGraph(GraphDSL.create(Sink.foreach[Int](println)) { implicit b => sink =>
    import GraphDSL.Implicits._

    val max = b.add(maxOfThree)
    Source(List(1, 7, 8)) ~> max.in(0)
    Source(List(3, 6, 9)) ~> max.in(1)
    Source(List(5, 2, 2)) ~> max.in(2)
    max.out ~> sink
    ClosedShape
  }).run()
}
