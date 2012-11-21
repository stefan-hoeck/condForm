package efa.cf.format

import efa.cf.format.logic.{Term, True}
import efa.core._, Efa._
import java.awt.{Color}
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scala.swing.Label
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class DoubleFormat (
  props: FormatProps, term: Term[Double], formatString: String
) {
  def matches (b: Double) = term matches b
}

object DoubleFormat {

  private lazy val defaultTerm: Term[Double] = 
    ">0 and <100".read[Term[Double]].toOption fold (identity, True())

  lazy val default = DoubleFormat(!!!, defaultTerm, "%.3f")

  implicit lazy val DoubleFormatDefault = Default default default

  implicit lazy val DoubleFormatEqual: Equal[DoubleFormat] =
    Equal.equalBy(f ⇒ (f.props, f.term, f.formatString))

  implicit lazy val DoubleFormatArbitrary = Arbitrary(
    arbitrary[FormatProps] ⊛ 
    arbitrary[Term[Double]] ⊛ 
    Gen.identifier apply DoubleFormat.apply
  )

  implicit lazy val DoubleFormatToXml = new ToXml[DoubleFormat] {
    def toXml (f: DoubleFormat) =
      (formatProps xml f.props) ++
      ("term" xml f.term) ++
      ("fString" xml f.formatString)

    def fromXml (ns: Seq[Node]) =
      ns.readTag[FormatProps](formatProps) ⊛
      ns.readTag[Term[Double]]("term") ⊛
      ns.readTag[String]("fString") apply DoubleFormat.apply
  }

  implicit lazy val DoubleFormatFormatted = fromProps[DoubleFormat](_.props)
  
  //Lenses

  val props: DoubleFormat @> FormatProps =
    Lens.lensu((a,b) ⇒ a copy (props = b), _.props)
  
  val term: DoubleFormat @> Term[Double] =
    Lens.lensu((a,b) ⇒ a copy (term = b), _.term)
  
  val formatString: DoubleFormat @> String =
    Lens.lensu((a,b) ⇒ a copy (formatString = b), _.formatString)
  
  implicit def DoubleFormatLenses[A](l: Lens[A,DoubleFormat]) = new {
    lazy val props = l andThen DoubleFormat.props
    lazy val term = l andThen DoubleFormat.term
    lazy val formatString = l andThen DoubleFormat.formatString
  }
}

// vim: set ts=2 sw=2 et:
