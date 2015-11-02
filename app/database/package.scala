import net.ceedubs.ficus.readers.ValueReader
import net.ceedubs.ficus.Ficus._

/**
 * Developer: jeffusan
 * Date: 10/30/15
 */
package object database {

  case class LatLong(lat: String, long: String)
  case class Depth(value: String, factor: String)
  case class Month(value: String, factor: String)

  implicit val latLongReader: ValueReader[List[LatLong]] = ValueReader.relative { config =>
    val entries = config.as[List[String]]("latlongs")
    entries.map { ll =>
      val digits = ll.split(":")
      LatLong(digits(0), digits(1))
    }
  }

  implicit val depthReader: ValueReader[List[Depth]] = ValueReader.relative { config =>
    val entries = config.as[List[String]]("depths")
    entries.map { d =>
      val ds = d.split(":")
      Depth(ds(0), ds(1))
    }
  }

  implicit val monthReader: ValueReader[List[Month]] = ValueReader.relative { config =>
    val entries = config.as[List[String]]("months")
    entries.map { d =>
      val ds = d.split(":")
      Month(ds(0), ds(1))
    }
  }
}
