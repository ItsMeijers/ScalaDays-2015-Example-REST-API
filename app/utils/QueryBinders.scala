package utils

import java.util.NoSuchElementException

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.mvc.QueryStringBindable

import scala.util.Try

/**
 * Created by ThomasWorkBook on 26/05/15.
 */
object QueryBinders {

  implicit def dateTimeBinder: QueryStringBindable[DateTime] = new QueryStringBindable[DateTime] {

    private lazy val ISODateTimeFormatter = ISODateTimeFormat.dateTime

    private lazy val ISODateTimeParser = ISODateTimeFormat.dateTimeParser

    override def unbind(key: String, value: DateTime): String = {
      ISODateTimeFormatter.print(value)
    }

    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, DateTime]] = {
      val dateTimeString: Option[Seq[String]] = params.get(key)

      Try{
        Some(Right(ISODateTimeParser.parseDateTime(dateTimeString.get.head)))
      } recover {
        case _ : NoSuchElementException => None
        case _ : IllegalArgumentException => Option(Left(dateTimeString.get.head))
      } getOrElse None
    }

  }

}
