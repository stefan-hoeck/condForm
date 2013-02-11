package efa.cf.format

import efa.core._, Efa._
import java.awt.Color
import org.scalacheck.Arbitrary
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._
import shapeless.{::, HNil, Iso, Nat}, Nat._

/** Contains all user-defined formattings as Maps from String
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
  lazy val fpBluePrints: List[FormatProps] =
    mapValues(bluePrintsM) sortBy { _.name }

  lazy val gcBluePrints: List[GradientColors] =
    mapValues(gradientColorsM) sortBy { _.name }

  lazy val gradientNames: List[String] = gcBluePrints map { _.name }
}

object AllFormats {
  private val AfLens = SLens[AllFormats]

  implicit val AfIso = Iso.hlist(AllFormats.apply _, AllFormats.unapply _)
  implicit val AfEqual: Equal[AllFormats] = ccEqual
  implicit val AfArb: Arbitrary[AllFormats] = ccArbitrary

  private def e[A,B]: Map[A,B] = Map.empty

  implicit val AfDefault = 
    Default default AllFormats(FormatProps.defaults, e, e, e, e, e)

  type AFP[A] = ParentL[StringMap, AllFormats, A, AfRoot]

  implicit val FPParent: AFP[FormatProps] = ParentL mapRoot AfLens.at(_0)
  implicit val BoolParent: AFP[BooleanBase] = ParentL mapRoot AfLens.at(_1)
  implicit val DoubleParent: AFP[DoubleBase] = ParentL mapRoot AfLens.at(_2)
  implicit val GradientParent: AFP[Gradient] = ParentL mapRoot AfLens.at(_3)
  implicit val GcParent: AFP[GradientColors] = ParentL mapRoot AfLens.at(_4)
  implicit val StringParent: AFP[StringBase] = ParentL mapRoot AfLens.at(_5)

  def registerBoolean (l: Localization)(a: AllFormats): AllFormats =
    tryReg[BooleanBase](l, a)

  def registerDouble (l: Localization)(a: AllFormats): AllFormats =
    tryReg[DoubleBase](l, a)

  def registerString (l: Localization)(a: AllFormats): AllFormats =
    tryReg[StringBase](l, a)

  def addGradient (g: Gradient)(a: AllFormats): AllFormats =
    GradientParent add (a:: HNil, g) exec a

  def deleteGradient (g: Gradient)(a: AllFormats): AllFormats =
    GradientParent delete (g :: a:: HNil) exec a

  private def tryReg[A:Default:StringIdL:Formatted]
    (loc: Localization, a: AllFormats)
    (implicit P: AFP[A]): AllFormats = {
    val id = loc.name
    val path = a :: HNil

    def newA = 
      ((StringIdL[A].idL := id) >>
      Formatted[A].setName(loc.locName)) exec Default.!!!

    P childrenWithoutPath path get id cata (
      _ ⇒ a,
      P add (path, newA) exec a
    )
  }
}

// vim: set ts=2 sw=2 et:
