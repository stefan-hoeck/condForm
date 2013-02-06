package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import org.scalacheck.Arbitrary
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._
import shapeless.{::, HNil, Iso, Nat}, Nat._

/**
 * Contains all user-defined formattings as Maps from String
 * to the corresponding formats.
 * Provides sorted lists of these formats to be dispalyed in
 * the UI
 */
case class AllFormats (
  bluePrintsM: StringMap[FormatProps], 
  boolsM: StringMap[BooleanBase],
  doublesM: StringMap[DoubleBase], 
  gradientsM: StringMap[Gradient],
  gradientColorsM: StringMap[GradientColors],
  stringsM: StringMap[StringBase]
) {
  import AllFormats._

  lazy val fpBluePrints: List[FpPath] =
    mapValues(bluePrintsM) sortBy { _.name } map bluePrintPath(false)

  lazy val gcBluePrints: List[GcPath] =
    mapValues(gradientColorsM) sortBy { _.name } map bluePrintPath(false)

  lazy val gcBluePrint: GcPath = GradientColors.bluePrint :: true :: afRoot

  lazy val fpBluePrint: FpPath = FormatProps.bluePrint :: false :: afRoot

  private lazy val afRoot = this :: HNil

  private def bluePrintPath[A](isNew: Boolean)(a: A) = a :: isNew :: afRoot
}

object AllFormats {
  implicit val AfIso = Iso.hlist(AllFormats.apply _, AllFormats.unapply _)
  implicit val AfEqual: Equal[AllFormats] = Shapeless.ccEqual
  implicit val AfArb: Arbitrary[AllFormats] = Shapeless.ccArbitrary
  private val AfLens = SLens[AllFormats]

  private def e[A,B]: Map[A,B] = Map.empty

  implicit val AfDefault = 
    Default default AllFormats(FormatProps.defaults, e, e, e, e, e)
  
  implicit class Lenses[A] (val l: Lens[A,AllFormats]) extends AnyVal {
    def bluePrintsM = l andThen AfLens.at(_0)
    def boolsM = l andThen AfLens.at(_1)
    def doublesM = l andThen AfLens.at(_2)
    def gradientsM = l andThen AfLens.at(_3)
    def gradientColorsM = l andThen AfLens.at(_4)
    def stringsM = l andThen AfLens.at(_5)

    //def delBluePrint (f: FullFormat[FormatProps]) =
    //  bluePrintsM -= f.head.name void

    //def addBluePrint (f: FormatProps) = bluePrintsM += (f.name → f) void

    //def updateBluePrint (ff: FullFormat[FormatProps], f: FormatProps) =
    //  delBluePrint(ff) >> addBluePrint(f)

    //def delGCs (n: String) = gradientColorsM -= n void

    //def addGCs (f: GradientColors) = gradientColorsM += (f.name → f) void

    //def modGCs (n: String, f: GradientColors ⇒ GradientColors) = for {
    //  a   ← init[A]
    //  ogc = gradientColorsM get a get n
    //  _   ← delGCs(n)
    //  _   ← ogc.fold(init[A].void)(f andThen addGCs)
    //} yield ()

    //def updateGCs (ff: FullFormat[GradientColors], f: GradientColors) =
    //  modGCs (ff.head.name, _ ⇒ f)

    //def delColor (n: String, i: Int) =
    //  modGCs(n, GradientColors.colors mod (_.patch (i, Nil, 1), _))

    //def addColor (n: String, c: Color) =
    //  modGCs(n, GradientColors.colors mod (_ :+ c, _))
  }

  def registerBoolean (l: Localization)(a: AllFormats): AllFormats =
    tryRegister(L[AllFormats].boolsM, l)(a)

  def registerDouble (l: Localization)(a: AllFormats): AllFormats =
    tryRegister(L[AllFormats].doublesM, l)(a)

  def registerString (l: Localization)(a: AllFormats): AllFormats =
    tryRegister(L[AllFormats].stringsM, l)(a)

  def addGradient (g: Gradient)(a: AllFormats): AllFormats =
    (L[AllFormats].gradientsM += (g.name → g)) exec a

  private def tryRegister[A:Default:StringIdL:Formatted]
      (lens: AllFormats @> Map[String,A], loc: Localization)
      (a: AllFormats): AllFormats = {
    val id = loc.name
    def newA = 
      ((StringIdL[A].idL := id) >>
      Formatted[A].setName(loc.locName)) exec Default[A].default

    lens mod (m ⇒ m.get(id).fold(m + (id → newA))(_ ⇒ m), a)
  }
}

// vim: set ts=2 sw=2 et:
