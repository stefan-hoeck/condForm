package efa.cf

import efa.cf.format.spi.{FormatLocal, CfPreferences}
import efa.core._, Efa._
import java.awt.{Color}
import org.scalacheck._, Arbitrary.arbitrary
import scalaz._, Scalaz._, effect._
import shapeless.{HNil, ::}

package object format {

  /**
    * Type alias for the root of all AllFormats-related paths
    */
  type AfRoot = AllFormats :: HNil

  /**
    * Bundled formats for a single Boolean property
    */
  type BooleanBase = BaseFormat[BooleanFormat]

  /**
    * Bundled formats for a single Double property
    */
  type DoubleBase = BaseFormat[DoubleFormat]

  type Colors = IndexedSeq[Color]

  /**
    * Complete path to an instance of FormatProps
    *
    * The Boolean flag in the path tells us, whether this
    * is an existing FormatProps or a new instance. This
    * information is needed for name validation
    */
  type FpPath = FormatProps :: Boolean :: AfRoot

  /**
    * Path to an instance of BaseFormat
    */
  type BasePath[A] = BaseFormat[A] :: AfRoot

  /**
    * Complete path to an single format instance
    *
    * The Boolean flag in the path tells us, whether this
    * is an existing or a new instance. This
    * information is needed for name validation
    */
  type FullFormat[A] = A :: Boolean :: BasePath[A]

  /**
    * Complete path to an instance of GradientColors
    *
    * The Boolean flag in the path tells us, whether this
    * is an existing FormatProps or a new instance. This
    * information is needed for name validation
    */
  type GcPath = GradientColors :: Boolean :: AfRoot

  /**
    * Bundled formats for a single String property
    */
  type StringBase = BaseFormat[StringFormat]

  /**
    * A Map with String keys
    */
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

  implicit def mapArb[A:Arbitrary:StringId]: Arbitrary[StringMap[A]] = 
    Arbitrary(
      for {
        i  ← Gen choose (1, 10)
        as ← Gen listOfN (i, arbitrary[A]) 
      } yield StringId[A] idMap as
    )
}

// vim: set ts=2 sw=2 et:
