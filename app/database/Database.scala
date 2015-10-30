package database

import com.typesafe.config.{Config, ConfigFactory}
import scalikejdbc.{AutoSession, ConnectionPool}
import net.ceedubs.ficus.Ficus._
import scalikejdbc._
import scala.util.Random

/**
 * Developer: jeffusan
 * Date: 10/30/15
 */
object Database {

  def load(): Unit = {
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:japan_ocean", "user", "pass")
    implicit val session = AutoSession

    val tempList = List(18, 19, 20, 21, 22, 23, 24, 25, 26)
    val R = Random
    val config: Config = ConfigFactory.load()
    val years = 1 to config.as[Int]("application.years")
    val months = 1 to 12
    val depths = config.as[List[Depth]]("application")
    val latlongs = config.as[List[LatLong]]("application")

    sql"""
    CREATE TABLE depth_measurements (
      id SERIAL PRIMARY KEY NOT NULL,
      year INT NOT NULL,
      month INT NOT NULL,
      depth VARCHAR(50) NOT NULL,
      latitude VARCHAR(100) NOT NULL,
      longitude VARCHAR(100) NOT NULL,
      temperature DECIMAL NOT NULL)
    """.execute.apply()


    for( year <- years) {
      for( depth <- depths) {
        for( month <- months ) {
          for( latlong <- latlongs ) {
            println("Year: " + year + " Month: " + month + " Depth: " + depth + " LatLong: " + latlong)
            val temp = tempList(R.nextInt(tempList.size)) / depth.factor.toFloat
            println("Value: " + tempList(R.nextInt(tempList.size)) / depth.factor.toFloat)
            sql"""insert into depth_measurements
                  (year, month, depth, latitude, longitude, temperature)
                  values
                  (${year}, ${month}, ${depth.value}, ${latlong.lat}, ${latlong.long}, ${temp})""".update.apply()
          }
        }
      }
    }
  }
}
