package efa.cf

import java.awt.{Color}
import efa.cf.format.spi.FormatLocal
import efa.core._, Efa._
import org.scalacheck._, Arbitrary.arbitrary
import scalaz._, Scalaz._, effect._

package object format {
  lazy val loc = Service.unique[FormatLocal](FormatLocal)

  val formatProps = "efa_cf_formatProps"
  val booleanFormat = "efa_cf_booleanFormat"
  val booleanBase = "efa_cf_booleanBase"
  val doubleBase = "efa_cf_doubleBase"
  val doubleFormat = "efa_cf_doubleFormat"
  val stringBase = "efa_cf_stringBase"
  val stringFormat = "efa_cf_stringFormat"

  private[format] implicit val ColorEqual: Equal[Color] =
    Equal.equalBy(_.getRGB)

  private[format] implicit val ColorShow: Show[Color] =
    Show.shows(_.getRGB.toString)

  private[format] implicit val ColorRead: Read[Color] =
    Read.readV(_.read[Int] map (new Color(_, true)))

  private[format] implicit val ColorArbitrary: Arbitrary[Color] =
    Arbitrary(arbitrary[Int] map (new Color(_, true)))

  private[format] implicit val ColorToXml: ToXml[Color] = ToXml.readShow

  private[format] def fromProps[A](f: A ⇒ FormatProps): Formatted[A] =
    new Formatted[A] {
      def formatProps (a: A) = f(a)
      def id (a: A) = formatProps(a).name
    }

    private[format] def propsName[A](f: A ⇒ FormatProps, n: A ⇒ String)
    : Formatted[A] = new Formatted[A] {
      def formatProps (a: A) = f(a)
      def id (a: A) = n(a)
    }

  type BooleanBase = BaseFormat[BooleanFormat]
  type StringBase = BaseFormat[StringFormat]
  type DoubleBase = BaseFormat[DoubleFormat]

  implicit lazy val BooleanFormatter = new Formatter[Boolean,BooleanFormat] {
      def matches (f: BooleanFormat, b: Boolean) = f matches b
      def formatPropsS (f: BooleanFormat) = f.props
    }

  implicit lazy val StringFormatter = new Formatter[String,StringFormat] {
      def matches (f: StringFormat, b: String) = f matches b
      def formatPropsS (f: StringFormat) = f.props
    }

  implicit lazy val DoubleFormatter = new Formatter[Double,DoubleFormat] {
      def matches (f: DoubleFormat, b: Double) = f matches b
      def formatPropsS (f: DoubleFormat) = f.props
    }

  def mapGen[A:Arbitrary:Formatted]: Gen[Map[String,A]] = for {
    i  ← Gen choose (1, 10)
    as ← Gen listOfN (i, arbitrary[A]) 
  } yield as map (a ⇒ Formatted[A].id(a) → a) toMap
}

// vim: set ts=2 sw=2 et:
