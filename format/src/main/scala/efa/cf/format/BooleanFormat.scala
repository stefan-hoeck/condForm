package efa.cf.format

import efa.core._, Efa._
import java.awt.{Color}
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scala.swing.Label
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class BooleanFormat (props: FormatProps, value: Boolean) {
  def matches (b: Boolean) = b ≟ value
}

object BooleanFormat {

  lazy val default = BooleanFormat(!!!, true)

  implicit lazy val BooleanFormatDefault = Default default default

  implicit lazy val BooleanFormatFormatted = formatted(props)(_.props.name)

  implicit lazy val BooleanFormatEqual: Equal[BooleanFormat] =
    Equal.equalBy(f ⇒ (f.props, f.value))

  implicit lazy val BooleanFormatArbitrary = Arbitrary(
    arbitrary[FormatProps] ⊛ 
    arbitrary[Boolean] apply BooleanFormat.apply
  )

  implicit lazy val BooleanFormatToXml = new ToXml[BooleanFormat] {
    def toXml (f: BooleanFormat) =
      (formatProps xml f.props) ++ ("value" xml f.value)

    def fromXml (ns: Seq[Node]) =
      ns.readTag[FormatProps](formatProps) ⊛
      ns.readTag[Boolean]("value") apply BooleanFormat.apply
  }
  
  //Lenses

  val props: BooleanFormat @> FormatProps =
    Lens.lensu((a,b) ⇒ a copy (props = b), _.props)

  val value: BooleanFormat @> Boolean =
    Lens.lensu((a,b) ⇒ a copy (value = b), _.value)
  
  implicit def BooleanFormatLenses[A](l: Lens[A,BooleanFormat]) = new {
    lazy val props = l andThen BooleanFormat.props
    lazy val value = l andThen BooleanFormat.value
  }
}

// vim: set ts=2 sw=2 et:
