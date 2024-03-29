package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scala.swing.Label
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

/**
 * A collection of foramts for a given property.
 *
 * @param id: The (unique) name of the property
 * @param props: FormatProps used to format a value if no matchich
 *               format is found in @param formats
 * @param formats: Among these a format is chosen when formatting a value
 * @param fString: Used to format properties that are displayed as Strings
                   Again, this is only needed, if no suitable format is found
                   in formats for a given value
 */
case class BaseFormat[A] (
    id: String,
    props: FormatProps,
    formats: List[A],
    fString: String)

object BaseFormat {

  implicit def BaseFormatDefault[A] = Default default (
    BaseFormat[A](loc.base, !!!, Nil, ""))

  implicit lazy val DoubleBaseDefault = Default default (
    BaseFormat[DoubleFormat](loc.base, !!!, Nil, "%.3f"))

  implicit def BaseFormatFormatted[A] = 
    new Formatted[BaseFormat[A]]
    with UniqueIdL[BaseFormat[A],String] {
      def formatPropsL = BaseFormat.props[A]
      def idL = BaseFormat.id[A]
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
  
  implicit class Lenses[A,B](val l: Lens[A,BaseFormat[B]]) extends AnyVal {
    def id = l andThen BaseFormat.id
    def props = l andThen BaseFormat.props
    def formats = l andThen BaseFormat.formats
    def fString = l andThen BaseFormat.fString
  }
}

// vim: set ts=2 sw=2 et:
