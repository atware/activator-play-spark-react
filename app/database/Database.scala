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

    val temps = Seq(18, 19, 20, 21, 22, 23, 24, 25, 26)
    val config: Config = ConfigFactory.load()
    val years = 1995 to (1995 + config.as[Int]("application.years"))
    val months = config.as[List[Month]]("application")
    val depths = config.as[List[Depth]]("application")
    val latlongs = config.as[List[LatLong]]("application")
    val a = config.as[Int]("application.appearances")
    val r = depths.length * months.length * latlongs.length - a
    val appearances = Random.shuffle(List.fill(r)(false) ++ List.fill(a)(true))

    sql"""
    CREATE TABLE depth_measurements (
      id SERIAL PRIMARY KEY NOT NULL,
      year INT NOT NULL,
      month INT NOT NULL,
      depth INT NOT NULL,
      latitude VARCHAR(100) NOT NULL,
      longitude VARCHAR(100)  NOT NULL,
      temperature VARCHAR(100)NOT NULL)
    """.execute.apply()

    def calculateTemperature(temps: Seq[Int], depth: Depth, month: Month, isGodzilla: Boolean): Double = {
      var t = temps(Random.nextInt(temps.size)) / depth.factor.toFloat / month.factor.toFloat
      if(isGodzilla) {
        t = t * 4.5.toFloat
      }
      t
    }

    for( year <- years) {
      val iter = appearances.toIterator
      for( depth <- depths) {
        for( month <- months ) {
          for( latlong <- latlongs ) {

            val temp = calculateTemperature(temps, depth, month, iter.next())

            sql"""insert into depth_measurements
                  (year, month, depth, latitude, longitude, temperature)
                  values
                  (${year}, ${month.value}, ${depth.value}, ${latlong.lat}, ${latlong.long}, ${temp})""".update.apply()
          }
        }
      }
    }
  }
}
