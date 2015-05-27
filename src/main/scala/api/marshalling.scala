package api


import spray.http.{ HttpEntity, StatusCode }
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.{ MetaMarshallers, CollectingMarshallingContext, ToResponseMarshaller, Marshaller }
import spray.json.DefaultJsonProtocol

/**
  * Case class that represents an Error inside the application
  * @param code Status code returned in the response
  * @param entity Response entity
  */
case class ErrorResponseException(code: StatusCode, entity: HttpEntity) extends Throwable

/**
  * Main trait for Marshalling support
  */
trait Marshalling extends DefaultJsonProtocol with SprayJsonSupport with MetaMarshallers {

  /**
    * Function for handling errors when API returns Left(ERROR) or Right(Response)
    * This implementation was inspired by Darek's spray seed: https://github.com/darek/spray-slick-seed
    *  who references http://www.cakesolutions.net/teamblogs/2012/12/10/errors-failures-im-a-teapot
    *  as a good explanation.
    */
  implicit def eitherCustomMarshaller[A, B](code: StatusCode)(implicit ma: Marshaller[A], mb: Marshaller[B]): ToResponseMarshaller[Either[A, B]] = Marshaller[Either[A, B]] { (value, context) =>
    value match {
      case Left(a) =>
        val mc = new CollectingMarshallingContext()
        ma(a, mc)
        context.handleError(ErrorResponseException(code, mc.entity))
      case Right(b) =>
        (200, mb(b, context))
    }
  }
}
