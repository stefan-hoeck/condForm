package efa.cf.format

import efa.cf.format.logic.{Term, True}
import efa.core._, Efa._
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class StringFormat (props: FormatProps, regex: String) {
  lazy val r = regex.r
  def matches (s: String) = r findFirstIn s nonEmpty
}

object StringFormat {

  lazy val default = StringFormat(!!!, ".*")

  lazy val regexV: EndoVal[String] = Kleisli(
    s ⇒ try{s.r; s.right} catch {case _: Throwable ⇒ loc.invalidRegexFail}
  )

  implicit lazy val StringFormatDefault = Default default default

  implicit lazy val StringFormatFormatted =
    new Formatted[StringFormat]
    with UniqueIdL[StringFormat,String] 
    with Formatter[String,StringFormat] {
      val idL = StringFormat.props.name
      val formatPropsL = StringFormat.props
      def matches (f: StringFormat, b: String) = f matches b
    }

  implicit lazy val StringFormatEqual: Equal[StringFormat] =
    Equal.equalBy(f ⇒ (f.props, f.regex))

  implicit lazy val StringFormatArbitrary = Arbitrary(
    arbitrary[FormatProps] ⊛ 
    Gen.identifier apply StringFormat.apply
  )

  implicit lazy val StringFormatToXml = new ToXml[StringFormat] {
    def toXml (f: StringFormat) =
      (formatProps xml f.props) ++ ("regex" xml f.regex)

    def fromXml (ns: Seq[Node]) =
      ns.readTag[FormatProps](formatProps) ⊛
      (ns.readTagV[String]("regex")(regexV)) apply
      StringFormat.apply
  }
  
  //Lenses
  
  val props: StringFormat @> FormatProps =
    Lens.lensu((a,b) ⇒ a copy (props = b), _.props)

  val regex: StringFormat @> String =
    Lens.lensu((a,b) ⇒ a copy (regex = b), _.regex)
  
  implicit class Lenses[A](val l: Lens[A,StringFormat]) extends AnyVal {
    def props = l andThen StringFormat.props
    def regex = l andThen StringFormat.regex
  }
}

// vim: set ts=2 sw=2 et:
