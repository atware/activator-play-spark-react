package database

import com.typesafe.config.{ConfigFactory, Config}
import net.ceedubs.ficus.Ficus._
import org.scalatest._
/**
 * Developer: jeffusan
 * Date: 10/30/15
 */
class DatabaseSpec extends FlatSpec with MustMatchers {

  "implicit readers" should "be available" in {
    val config: Config = ConfigFactory.load()
    val years = 1 to config.as[Int]("application.years")
    val depths = config.as[List[Depth]]("application")
    val latlongs = config.as[List[LatLong]]("application")
    latlongs.size mustEqual 2
  }
}
