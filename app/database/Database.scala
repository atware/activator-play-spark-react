package database

import com.typesafe.config.{Config, ConfigFactory}
import scalikejdbc.{AutoSession, ConnectionPool}
import net.ceedubs.ficus.Ficus._

/**
 * Developer: jeffusan
 * Date: 10/30/15
 */
object Database {

  def load(): Unit = {
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:japan_ocean", "user", "pass")
    implicit val session = AutoSession


    val config: Config = ConfigFactory.load()
    val years = 1 to config.as[Int]("application.years")
    val months = 1 to 12
    val depths = config.as[List[Depth]]("application")
    val latlongs = config.as[List[LatLong]]("application")

    for( year <- years) {
      for( depth <- depths) {
        for( month <- months ) {
          for( latlong <- latlongs ) {
            println("Year: " + year + " Month: " + month + " Depth: " + depth + " LatLong: " + latlong)
          }
        }
      }
    }

//    sql"""
//    CREATE TABLE metadata_region (
//      id SERIAL PRIMARY KEY NOT NULL,
//      name VARCHAR(200) NOT NULL)
//    """.execute.apply()
  }

}
