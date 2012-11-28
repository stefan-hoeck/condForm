package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scala.swing.Label
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class BaseFormat[A] (
  id: String, props: FormatProps, formats: List[A], fString: String
) {

  def fullList (implicit N:Formatted[A]): List[FullFormat[A]] = {
    val names = formats map N.locName toSet

    formats sortBy N.locName map (FullFormat(id, _, names, false))
  }
   
}

object BaseFormat {

  implicit def BaseFormatDefault[A] = Default default (
    BaseFormat[A](loc.base, !!!, Nil, ""))

  implicit lazy val DoubleBaseDefault = Default default (
    BaseFormat[DoubleFormat](loc.base, !!!, Nil, "%.3f"))

  implicit def BaseFormatFormatted[A] = 
    new Formatted[BaseFormat[A]] with UniqueNamed[BaseFormat[A]] {
      def formatPropsL = BaseFormat.props[A]
      def uniqueNameL = BaseFormat.id[A]
    }

  implicit def BaseFormatEqual[A:Equal]: Equal[BaseFormat[A]] =
    Equal.equalBy(f ⇒ (f.id, f.props, f.formats, f.fString))

  implicit def BaseFormatArbitrary[A:Arbitrary] = Arbitrary(
    Gen.identifier ⊛
    arbitrary[FormatProps] ⊛ 
    (Gen.choose(1,10) >>= (Gen.listOfN(_, arbitrary[A]))) ⊛
    Gen.identifier apply BaseFormat.apply[A]
  )

  def baseFormatToXml[A:ToXml](lbl: String) =
    new ToXml[BaseFormat[A]] {
    private lazy val listToXml = ToXml.listToXml[A](lbl)

    def toXml (f: BaseFormat[A]) =
      ("name" xml f.id) ++
      (formatProps xml f.props) ++
      listToXml.toXml(f.formats) ++
      ("fString" xml f.fString)

    def fromXml (ns: Seq[Node]) =
      ns.readTag[String]("name") ⊛
      ns.readTag[FormatProps](formatProps) ⊛
      listToXml.fromXml(ns) ⊛
      ns.readTag[String]("fString") apply BaseFormat.apply
  }
  
  //Lenses

  def id[A]: BaseFormat[A] @> String =
    Lens.lensu((a,b) ⇒ a copy (id = b), _.id)

  def props[A]: BaseFormat[A] @> FormatProps =
    Lens.lensu((a,b) ⇒ a copy (props = b), _.props)

  def formats[A]: BaseFormat[A] @> List[A] =
    Lens.lensu((a,b) ⇒ a copy (formats = b), _.formats)

  def fString[A]: BaseFormat[A] @> String =
    Lens.lensu((a,b) ⇒ a copy (fString = b), _.fString)
  
  implicit def BaseFormatLenses[A,B](l: Lens[A,BaseFormat[B]]) = new {
    lazy val id = l andThen BaseFormat.id
    lazy val props = l andThen BaseFormat.props
    lazy val formats = l andThen BaseFormat.formats
    lazy val fString = l andThen BaseFormat.fString
  }
}

// vim: set ts=2 sw=2 et:
