package utils

import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.mvc.{Call, RequestHeader}

import scala.concurrent.duration.Duration

/**
 * Created by ThomasWorkBook on 30/05/15.
 */
trait JsonUtils {

  implicit def durationWrapper(d: Duration) = d.toSeconds.toInt

  def addSelfLink(json: JsValue, c: Call)(implicit request: RequestHeader) =
    json.as[JsObject] ++ Json.obj("link" -> Json.obj(
      "rel" -> "self",
      "href" -> c.absoluteURL()
    )
    )

  def errorJson(message: String) = Json.obj("error" -> message)

  def errorJson(errors: Seq[(JsPath, Seq[ValidationError])]) = errors.foldLeft(JsArray()) { (acc, c) =>
    acc :+ Json.obj("error" -> s"${c._1.toString.drop(1)} ${c._2.foldLeft("")((acc, c) => acc + c.message)}")
  }

  def createLink(rel: String, href: Call)(implicit request: RequestHeader) = Json.obj(
    "rel" -> rel,
    "href" -> href.absoluteURL()
  )

}