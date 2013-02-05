package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class GradientColors (name: String, colors: Colors)

object GradientColors {
  lazy val default = GradientColors(loc.default, defaultColors)

  lazy val bluePrint = GradientColors(loc.gradient, defaultColors)

  implicit lazy val FormatPropsDefault: Default[GradientColors] =
    Default default default

  implicit lazy val FormatPropsEqual: Equal[GradientColors] = Equal.equalBy(
    f ⇒ (f.name, f.colors.toList))

  implicit lazy val FormatPropsArbitrary = Arbitrary(
    ^(Gen.identifier, arbitrary[Colors])(GradientColors.apply)
  )

  implicit lazy val GradientColorsToXml = new ToXml[GradientColors] {
    def toXml (f: GradientColors) =
      ("name" xml f.name) ++
      ("colors" xml f.colors)

    def fromXml (ns: Seq[Node]) = ^(
      ns.readTag[String]("name"), ns.readTag[Colors]("colors")
    )(GradientColors.apply)
  }

  implicit lazy val GradientColorsUniqueNamed =
    new UniqueIdL[GradientColors,String] {
      def idL = GradientColors.name
    }
  
  val name: GradientColors @> String =
    Lens.lensu((a,b) ⇒ a.copy(name = b), _.name)

  val colors: GradientColors @> Colors =
    Lens.lensu((a,b) ⇒ a.copy(colors = b), _.colors)
  
  private[format] lazy val goodC = new Color(0, 255, 0)
  private[format] lazy val okC = new Color(255, 255, 0)
  private[format] lazy val badC = new Color(255, 0, 0)

  lazy val defaultColors = IndexedSeq(badC, okC, goodC)

  lazy val transparentColors = IndexedSeq(
    FormatProps.badC, FormatProps.okC, FormatProps.goodC
  )

  lazy val defaultMap: Map[String,GradientColors] = Map (
    loc.default → default,
    loc.transparent → GradientColors(loc.transparent, transparentColors)
  )
}

// vim: set ts=2 sw=2 et:
