package efa.cf

import efa.cf.format.spi.{FormatLocal, CfPreferences}
import efa.core._, Efa._
import java.awt.{Color}
import org.scalacheck._, Arbitrary.arbitrary
import scalaz._, Scalaz._, effect._
import shapeless.{HNil, ::}

package object format {

  /** A `Color` together with a unique identifier */
  type IdColor = (Int, Color)

  /** Type alias for the root of all AllFormats-related paths */
  type AfRoot = AllFormats :: HNil

  /** Bundled formats for a single Boolean property */
  type BooleanBase = BaseFormat[BooleanFormat]

  /** Bundled formats for a single Double property */
  type DoubleBase = BaseFormat[DoubleFormat]

  type Colors = List[IdColor]

  /** Complete path to an instance of FormatProps */
  type FpPath = FormatProps :: AfRoot

  /** Path to an instance of BaseFormat */
  type BasePath[A] = BaseFormat[A] :: AfRoot

  /** Complete path to an single format instance */
  type FullFormat[A] = A :: BasePath[A]

  /** Complete path to an instance of GradientColors */
  type GcPath = GradientColors :: AfRoot

  /** Bundled formats for a single String property */
  type StringBase = BaseFormat[StringFormat]

  /** A Map with String keys */
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
    Arbitrary(Gen listOf arbitrary[Color] map idColors)

  private[format] implicit val ColorsShow: Show[Colors] =
    Show.shows(_ map (_._2.shows) mkString ";")

  private[format] implicit val ColorsRead: Read[Colors] =
    Read.readV(s ⇒ 
      if (s.isEmpty) Nil.success
      else
        (s split ";" toList) traverse (_.read[Color]) map idColors 
    )

  private[format] implicit val ColorsToXml: ToXml[Colors] = ToXml.readShow

  implicit def mapArb[A:Arbitrary:StringId]: Arbitrary[StringMap[A]] = 
    Arbitrary(
      for {
        i  ← Gen choose (1, 10)
        as ← Gen listOfN (i, arbitrary[A]) 
      } yield StringId[A] idMap as
    )

  implicit def IdColorsUid: UniqueIdL[IdColor,Int] =
    new UniqueIdL[IdColor,Int]{val idL = Lens.firstLens[Int,Color]}

  private[format] def idColors(cs: List[Color]): Colors =
    IdColorsUid generateIds (cs strengthL 0)
}

// vim: set ts=2 sw=2 et:
