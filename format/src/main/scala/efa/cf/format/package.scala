package efa.cf

import efa.cf.format.spi.{FormatLocal, CfPreferences}
import efa.core._, Efa._
import java.awt.{Color}
import org.scalacheck._, Arbitrary.arbitrary
import scalaz._, Scalaz._, effect._
import shapeless.{HNil, ::}

package object format {

  type BooleanBase = BaseFormat[BooleanFormat]
  type DoubleBase = BaseFormat[DoubleFormat]
  type Colors = IndexedSeq[Color]
  type FpBluePrint = FormatProps :: Boolean :: AllFormats :: HNil
  type GcBluePrint = GradientColors :: Boolean :: AllFormats :: HNil
  type StringBase = BaseFormat[StringFormat]
  type StringMap[+A] = Map[String,A]

  lazy val loc = Service.unique[FormatLocal](FormatLocal)

  private[cf] lazy val pref = Service.unique[CfPreferences](CfPreferences)

  final val formatProps = "efa_cf_formatProps"
  final val booleanFormat = "efa_cf_booleanFormat"
  final val booleanBase = "efa_cf_booleanBase"
  final val doubleBase = "efa_cf_doubleBase"
  final val doubleFormat = "efa_cf_doubleFormat"
  final val gradient = "efa_cf_gradient"
  final val gradientColors = "efa_cf_gradientColors"
  final val stringBase = "efa_cf_stringBase"
  final val stringFormat = "efa_cf_stringFormat"

  private[format] final val Version = "2.0.0"

  private[format] implicit val ColorEqual: Equal[Color] =
    Equal.equalBy(_.getRGB)

  private[format] implicit val ColorShow: Show[Color] =
    Show.shows(_.getRGB.toString)

  private[format] implicit val ColorRead: Read[Color] =
    Read.readV(_.read[Int] map (new Color(_, true)))

  private[format] implicit val ColorArbitrary: Arbitrary[Color] =
    Arbitrary(arbitrary[Int] map (new Color(_, true)))

  private[format] implicit val ColorToXml: ToXml[Color] = ToXml.readShow

  private[format] implicit val ColorsArbitrary: Arbitrary[Colors] =
    Arbitrary(Gen listOf arbitrary[Color] map (_.toIndexedSeq))

  private[format] implicit val ColorsShow: Show[Colors] =
    Show.shows(_ map (_.shows) mkString ";")

  private[format] implicit val ColorsRead: Read[Colors] =
    Read.readV(s ⇒ 
      if (s.isEmpty) IndexedSeq.empty[Color].success
      else
        (s split ";" toList) traverse (_.read[Color]) map (_.toIndexedSeq)
    )

  private[format] implicit val ColorsToXml: ToXml[Colors] = ToXml.readShow

  private[format] def formatted[A](l: A @> FormatProps)(i: A ⇒ String)
  : Formatted[A] = new Formatted[A] {
    def id (a: A) = i(a)
    def formatPropsL = l
  }

  private[format] def propsName[A](f: A ⇒ FormatProps)(i: A ⇒ String)
  : HasFormatProps[A] = new HasFormatProps[A] {
    def formatProps (a: A) = f(a)
    def id (a: A) = i(a)
  }

  def id[A:StringId](a: A): String = StringId[A] id a

  implicit def mapArb[A:Arbitrary:StringId]: Arbitrary[StringMap[A]] = 
    Arbitrary(
      for {
        i  ← Gen choose (1, 10)
        as ← Gen listOfN (i, arbitrary[A]) 
      } yield StringId[A] idMap as
    )
}

// vim: set ts=2 sw=2 et:
