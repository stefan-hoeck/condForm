package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import math.{ceil, round}
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class Gradient (
  name: String, nod: Int, bad: Double, good: Double
) {
  import Gradient._

  lazy val formatString = nod match {
    case 0 => "%f"
    case x => "%." + x + "f"
  }
  
  def colorFor (d: Double): Color = colorFor(d, GradientColors.defaultColors)
  
  def colorFor (d: Double, cs: IndexedSeq[Color]): Color =
    cFor(d, cs, bad, good)
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

  lazy val default = Gradient(loc.gradient, 1, 0D, 100D)

  implicit lazy val GradientDefault: Default[Gradient] =
    Default default default

  implicit lazy val GradientUniqueId = new UniqueNamed[Gradient]{
    val uniqueNameL = Gradient.name
  }

  implicit lazy val GradientEqual: Equal[Gradient] = Equal.equalA

  implicit lazy val GradientArbitrary = Arbitrary(
    Gen.identifier ⊛ 
    Gen.choose(2, 20) ⊛ 
    Gen.choose(-1000D, 1000D) ⊛ 
    Gen.choose(-1000D, 1000D) apply Gradient.apply
  )

  implicit lazy val GradientToXml = new ToXml[Gradient] {
    def toXml (g: Gradient) =
      ("name" xml g.name) ++
      ("nod" xml g.nod) ++
      ("bad" xml g.bad) ++
      ("good" xml g.good)

    def fromXml (ns: Seq[Node]) =
      ns.readTag[String]("name") ⊛
      ns.readTag[Int]("nod") ⊛
      ns.readTag[Double]("bad") ⊛
      ns.readTag[Double]("good") apply Gradient.apply
  }

  val name: Gradient @> String =
    Lens.lensu((a,b) ⇒ a copy (name = b), _.name)

  val nod: Gradient @> Int =
    Lens.lensu((a,b) ⇒ a copy (nod = b), _.nod)

  val bad: Gradient @> Double =
    Lens.lensu((a,b) ⇒ a copy (bad = b), _.bad)

  val good: Gradient @> Double =
    Lens.lensu((a,b) ⇒ a copy (good = b), _.good)
}

// vim: set ts=2 sw=2 et:
