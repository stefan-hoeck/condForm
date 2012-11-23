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

  lazy val bluePrint = FormatProps(fore, transparent, loc.bluePrint)

  implicit lazy val FormatPropsDefault: Default[FormatProps] =
    Default default default

  implicit lazy val FormatPropsUniqueId =
    UniqueId.get[FormatProps,String](_.name)

  implicit lazy val FormatPropsEqual: Equal[FormatProps] = Equal.equalBy(
    f ⇒ (f.foreground, f.background, f.name))

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
    formatted(Lens.self[FormatProps])(_.name)
  
  private lazy val transparent: Color = new Color(0, 0, 0, 0)
  private lazy val fore: Color = new Label().foreground
  private lazy val goodC = new Color(0, 255, 0, 100)
  private lazy val okC = new Color(255, 255, 0, 100)
  private lazy val badC = new Color(255, 0, 0, 100)
  private lazy val defaults = List(
    FormatProps(fore, goodC, loc.good),
    FormatProps(fore, okC, loc.ok),
    FormatProps(fore, badC, loc.bad)
  )
  
  //Lenses

  val name: FormatProps @> String =
    Lens.lensu((a,b) ⇒ a copy (name = b), _.name)
  val foreground: FormatProps @> Color =
    Lens.lensu((a,b) ⇒ a copy (foreground = b), _.foreground)
  val background: FormatProps @> Color =
    Lens.lensu((a,b) ⇒ a copy (background = b), _.background)
  
  implicit def FormatPropsLenses[A](l: Lens[A,FormatProps]) = new {
    lazy val name = l andThen FormatProps.name
    lazy val foreground = l andThen FormatProps.foreground
    lazy val background = l andThen FormatProps.background
  }
}

//object FormatProps extends Loggable with NbBundled {
//  val Label = "efa_cf_formatProps"
//  
//  def now = bluePrints.now
//  def setFormats (fs: List[FormatProps]): Unit = {
//    require((fs map (_.name)).distinct.size == fs.size)
//    sig() = fs
//  }
//  
//  private[formatting] def load = {
//    info("Loading formatting blue prints")
//    val prefs = NbPreferences.forModule(getClass)
//    prefs.get(Label + StoredRegister.version, "") match {
//      case "" => defaults
//      case x => (XML.loadString(x) \ Label map (new FormatProps(_))).toList
//    }
//  }
//  
//  private[formatting] def store() = {
//    info("Persisting formatting blue prints")
//    val prefs = NbPreferences.forModule(getClass)
//    prefs.put(Label + StoredRegister.version, 
//              <value>{bluePrints.now map (_.toXml)}</value>.toString)
//  }
//  
//}

// vim: set ts=2 sw=2 et:
