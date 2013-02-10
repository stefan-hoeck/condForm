package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import math.{ceil, round}
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class Gradient (
  name: String, nod: Int, lower: Double, upper: Double, colors: String
) {
  import Gradient._

  lazy val formatString = nod match {
    case 0 => "%f"
    case x => "%." + x + "f"
  }
  
  def colorFor(d: Double, af: AllFormats): Color = {
    val gcs = af.gradientColorsM getOrElse (colors, GradientColors.default)

    cFor (d, gcs.colorsIs, lower, upper)
  }
}

object Gradient {
  
  private def cFor (d: Double, cs: IndexedSeq[Color], l: Double, u: Double)
  : Color = d match {
      case _ if (u < l) ⇒ cFor (d, cs.reverse, u, l)
      case x if (x < l) ⇒ cs.head
      case x if (x > u) ⇒ cs.last
      case x ⇒ {
        val maxIdx = cs.length - 1 //max index of cs
        val sect = (x - l)/(u - l) //ratio of d between l and u
        val indexPosition = sect * maxIdx.toDouble //position on index scale
        val uIdx = ceil(indexPosition).toInt //upper color index
        val lIdx = (uIdx - 1) max 0 //lower color index
        val (c1, c2) = (cs(lIdx), cs(uIdx))
        val sectBetweenIndices = indexPosition - lIdx.toDouble

        mixCs(c1, c2, sectBetweenIndices)
      }
    }
  
  //mix two numbers taking x of l and (1 - x) of u
  private def mix (l: Int, u: Int, x: Double): Int =
    if (l > u) mix (u, l, 1.0 - x)
    else l + round((u - l).toDouble * x).toInt
    
  //mix two Colors taking x of l and (1 - x) of u
  private def mixCs (l: Color, u: Color, x: Double) = new Color(
    mix(l.getRed, u.getRed, x),
    mix(l.getGreen, u.getGreen, x),
    mix(l.getBlue, u.getBlue, x),
    mix(l.getAlpha, u.getAlpha, x)
  )
  val nodVal = Validators interval (0, 20)

  lazy val default = Gradient(loc.gradient, 1, 0D, 100D, "")

  implicit lazy val GradientDefault: Default[Gradient] =
    Default default default

  implicit lazy val GradientUniqueId = new UniqueIdL[Gradient,String]{
    val idL = Gradient.name
  }

  implicit lazy val GradientEqual: Equal[Gradient] = Equal.equalA

  implicit lazy val GradientArbitrary = Arbitrary(
    Gen.identifier ⊛ 
    Gen.choose(2, 20) ⊛ 
    Gen.choose(-1000D, 1000D) ⊛ 
    Gen.choose(-1000D, 1000D) ⊛
    Gen.identifier apply Gradient.apply
  )

  implicit lazy val GradientToXml = new ToXml[Gradient] {
    def toXml (g: Gradient) =
      ("name" xml g.name) ++
      ("nod" xml g.nod) ++
      ("lower" xml g.lower) ++
      ("upper" xml g.upper) ++
      ("colors" xml g.colors)

    def fromXml (ns: Seq[Node]) =
      ns.readTag[String]("name") ⊛
      ns.readTag[Int]("nod") ⊛
      ns.readTag[Double]("lower") ⊛
      ns.readTag[Double]("upper") ⊛
      ns.readTag[String]("colors") apply Gradient.apply
  }

  val name: Gradient @> String =
    Lens.lensu((a,b) ⇒ a copy (name = b), _.name)

  val nod: Gradient @> Int =
    Lens.lensu((a,b) ⇒ a copy (nod = b), _.nod)

  val lower: Gradient @> Double =
    Lens.lensu((a,b) ⇒ a copy (lower = b), _.lower)

  val upper: Gradient @> Double =
    Lens.lensu((a,b) ⇒ a copy (upper = b), _.upper)

  val colors: Gradient @> String =
    Lens.lensu((a,b) ⇒ a.copy(colors = b), _.colors)
  
}

// vim: set ts=2 sw=2 et:
