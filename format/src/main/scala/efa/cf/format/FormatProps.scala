package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scala.xml.Node
import scala.swing.Label
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

case class FormatProps(foreground: Color, background: Color, name: String) 

object FormatProps {

  lazy val default = FormatProps(fore, transparent, loc.default)

  lazy val bluePrint = FormatProps(fore, goodC, loc.bluePrint)

  implicit lazy val FormatPropsDefault: Default[FormatProps] =
    Default default default

  implicit lazy val FormatPropsEqual = Equal.equalA[FormatProps]

  implicit lazy val FormatPropsArbitrary = Arbitrary(
    arbitrary[Color] ⊛ 
    arbitrary[Color] ⊛ 
    Gen.identifier apply FormatProps.apply
  )

  implicit lazy val FormatPropsToXml = new ToXml[FormatProps] {
    def toXml (f: FormatProps) =
      ("fore" xml f.foreground) ++
      ("back" xml f.background) ++
      ("name" xml f.name)

    def fromXml (ns: Seq[Node]) =
      ns.readTag[Color]("fore") ⊛
      ns.readTag[Color]("back") ⊛
      ns.readTag[String]("name") apply FormatProps.apply
  }

  implicit lazy val FormatPropsFormatted =
    new Formatted[FormatProps] with UniqueIdL[FormatProps,String] {
      val idL = FormatProps.name
      val formatPropsL = Lens.self[FormatProps]
    }
  
  private lazy val transparent: Color = new Color(0, 0, 0, 0)
  private lazy val fore: Color = new Label().foreground
  lazy val goodC = new Color(0, 255, 0, 100)
  lazy val okC = new Color(255, 255, 0, 100)
  lazy val badC = new Color(255, 0, 0, 100)

  lazy val defaults = Map(
    loc.good → FormatProps(fore, goodC, loc.good),
    loc.ok → FormatProps(fore, okC, loc.ok),
    loc.bad → FormatProps(fore, badC, loc.bad)
  )
  
  //Lenses

  val name: FormatProps @> String =
    Lens.lensu((a,b) ⇒ a copy (name = b), _.name)
  val foreground: FormatProps @> Color =
    Lens.lensu((a,b) ⇒ a copy (foreground = b), _.foreground)
  val background: FormatProps @> Color =
    Lens.lensu((a,b) ⇒ a copy (background = b), _.background)
  
  implicit class Lenses[A](val l: Lens[A,FormatProps]) extends AnyVal {
    def name = l andThen FormatProps.name
    def foreground = l andThen FormatProps.foreground
    def background = l andThen FormatProps.background
  }
}

// vim: set ts=2 sw=2 et:
