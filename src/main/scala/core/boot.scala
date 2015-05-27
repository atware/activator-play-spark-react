package core

import java.util.concurrent.TimeUnit
import akka.actor.{ Props, ActorSystem }
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.can.Http
import scala.concurrent.Await

/**
  * This is the main application launcher.
  * It defines the actor system, creates a server instance, and adds a shutdown hook.
  * This is the only main function for the application.
  */
object Boot {

  implicit val system = ActorSystem("gds")

  def main(args: Array[String]) {
    class ApplicationServer(val actorSystem: ActorSystem) extends BootSystem with Api with ServerIO
    new ApplicationServer(system)

    sys.addShutdownHook(system.shutdown())
  }
}

/**
 * Server IO makes use of Akka IO - http://doc.akka.io/docs/akka/snapshot/scala/io.html
 * A joint design effort between Spray and Akka to build a scalable Rest API.
 * Note that this trait includes Api, which is part of api.scala.
 * Configuration is provided in /src/main/resources/application.conf
 * SparkConfig starts and initializes Spark.
 */
trait ServerIO {

  this: Api with BootSystem =>

  val config = ConfigFactory.load()

  // this starts spark
  SparkConfig.init()

  IO(Http) ! Http.Bind(routeService, config.getString("application.server.host"), config.getInt("application.server.port"))
}

/**
  * BootSystem boots the system...
  *  It provides a timeout, sends a StartUp message, and registers termination.
  *  See the ApplicationActor in application.scala for details of the startup.
  */
trait BootSystem {

  final val startupTimeout = 15

  implicit def actorSystem: ActorSystem
  implicit val timeout: Timeout = Timeout(startupTimeout, TimeUnit.SECONDS)

  val application = actorSystem.actorOf(Props[ApplicationActor], "gds")
  Await.ready(application ? Startup(), timeout.duration)

  actorSystem.registerOnTermination {
    application ! Shutdown()
  }
}
