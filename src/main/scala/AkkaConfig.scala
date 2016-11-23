import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

/**
  * Created by lukasz.drygala on 22/11/16.
  */
trait AkkaConfig {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
}
