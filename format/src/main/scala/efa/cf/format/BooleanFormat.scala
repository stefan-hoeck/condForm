package efa.cf.format

import efa.core._, Efa._
import org.scalacheck.Arbitrary
import scala.xml.Node
import scala.swing.Label
import scalaz._, Scalaz._
import shapeless.Iso

case class BooleanFormat (props: FormatProps, value: Boolean) {
  def matches (b: Boolean) = b ≟ value
}

object BooleanFormat {
  implicit val BfIso = Iso.hlist(BooleanFormat.apply _, BooleanFormat.unapply _)
  implicit val BfEqual: Equal[BooleanFormat] = Shapeless.ccEqual
  implicit val BfArb: Arbitrary[BooleanFormat] = Shapeless.ccArbitrary
  implicit val BfDefault = Default default BooleanFormat(!!!, true)

  implicit lazy val BooleanFormatFormatted =
    new Formatted[BooleanFormat]
    with UniqueIdL[BooleanFormat,String] 
    with Formatter[Boolean,BooleanFormat] {
      val idL = BooleanFormat.props.name
      val formatPropsL = BooleanFormat.props
      def matches (f: BooleanFormat, b: Boolean) = f matches b
    }

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
  
  implicit class Lenses[A](val l: Lens[A,BooleanFormat]) extends AnyVal {
    def props = l andThen BooleanFormat.props
    def value = l andThen BooleanFormat.value
  }
}

// vim: set ts=2 sw=2 et:
