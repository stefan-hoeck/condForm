package efa.cf.format

import efa.core.{Default, Localization}
import org.scalacheck.Arbitrary
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

/**
 * Contains all user-defined formattings as Maps from String
 * to the corresponding formats.
 * Provides sorted lists of these formats to be dispalyed in
 * the UI
 */
case class AllFormats (
  bluePrintsM: Map[String,FormatProps], 
  boolsM: Map[String,BooleanBase],
  doublesM: Map[String,DoubleBase], 
  gradientsM: Map[String,Gradient],
  gradientColorsM: Map[String,GradientColors],
  stringsM: Map[String,StringBase]
) {
  import AllFormats._

  lazy val bluePrints = m2s(bluePrintsM) sortBy (_.name)

  lazy val bluePrintsFF =
    bluePrints ∘ (FullFormat("", _, bluePrintsM.keySet, false))

  lazy val gradientColors = m2s(gradientColorsM) sortBy (_.name)

  lazy val gradientColorsFF =
    gradientColors ∘ (FullFormat("", _, gradientColorsM.keySet, false))

  def gcBluePrint: FullFormat[GradientColors] = FullFormat (
    "", GradientColors.bluePrint, gradientColorsM.keySet, true
  )

  def fpBluePrint: FullFormat[FormatProps] = FullFormat(
    "", FormatProps.bluePrint, bluePrintsM.keySet, true
  )

  private def m2s[A](m: Map[String,A]) = m.toList map (_._2)
  
  def fullBases[A](l: AllFormats @> Map[String,BaseFormat[A]]) =
    (l get this).toList map (p ⇒ FullBase(p._2, bluePrints))
}

object AllFormats {
  private def e[A,B]: Map[A,B] = Map.empty

  val default = AllFormats(FormatProps.defaults, e, e, e, e, e)

  implicit val AllFormatsDefault = Default default default

  implicit val AllFormatsEqual = Equal.equalA[AllFormats]

  implicit val AllFormatsArbitrary: Arbitrary[AllFormats] = Arbitrary(
    ^^^^^(
      mapGen[FormatProps],
      mapGen[BooleanBase],
      mapGen[DoubleBase],
      mapGen[Gradient],
      mapGen[GradientColors],
      mapGen[StringBase]
    )(AllFormats.apply)
  )

  val bluePrintsM: AllFormats @> Map[String,FormatProps] =
    Lens.lensu((a,b) ⇒ a copy (bluePrintsM = b), _.bluePrintsM)

  val boolsM: AllFormats @> Map[String,BooleanBase] =
    Lens.lensu((a,b) ⇒ a copy (boolsM = b), _.boolsM)

  val doublesM: AllFormats @> Map[String,DoubleBase] =
    Lens.lensu((a,b) ⇒ a copy (doublesM = b), _.doublesM)
  
  val gradientsM: AllFormats @> Map[String,Gradient] =
    Lens.lensu((a,b) ⇒ a copy (gradientsM = b), _.gradientsM)
  
  val gradientColorsM: AllFormats @> Map[String,GradientColors] =
    Lens.lensu((a,b) ⇒ a copy (gradientColorsM = b), _.gradientColorsM)
  
  val stringsM: AllFormats @> Map[String,StringBase] =
    Lens.lensu((a,b) ⇒ a copy (stringsM = b), _.stringsM)
  
  implicit def allFormatsLenses[A] (l: Lens[A,AllFormats]) = new {
    lazy val bluePrintsM = l andThen AllFormats.bluePrintsM
    lazy val boolsM = l andThen AllFormats.boolsM
    lazy val doublesM = l andThen AllFormats.doublesM
    lazy val gradientsM = l andThen AllFormats.gradientsM
    lazy val gradientColorsM = l andThen AllFormats.gradientColorsM
    lazy val stringsM = l andThen AllFormats.stringsM

    def delBluePrint (f: FullFormat[FormatProps]) =
      bluePrintsM -= f.format.name void

    def addBluePrint (f: FormatProps) = bluePrintsM += (f.name → f) void

    def updateBluePrint (ff: FullFormat[FormatProps], f: FormatProps) =
      delBluePrint(ff) >> addBluePrint(f)

    def delGCs (f: FullFormat[GradientColors]) =
      gradientColorsM -= f.format.name void

    def addGCs (f: GradientColors) = gradientColorsM += (f.name → f) void

    def updateGCs (ff: FullFormat[GradientColors], f: GradientColors) =
      delGCs(ff) >> addGCs(f)
  }

  def registerBoolean (l: Localization)(a: AllFormats): AllFormats =
    tryRegister(boolsM, l)(a)

  def registerDouble (l: Localization)(a: AllFormats): AllFormats =
    tryRegister(doublesM, l)(a)

  def registerString (l: Localization)(a: AllFormats): AllFormats =
    tryRegister(stringsM, l)(a)

  def addGradient (g: Gradient)(a: AllFormats): AllFormats =
    (gradientsM += (g.name → g)) exec a

  private def tryRegister[A:Default:UniqueNamed:Formatted] (
    lens: AllFormats @> Map[String,A], loc: Localization
  )(a: AllFormats): AllFormats = {
    val id = loc.name
    def newA = 
      (UniqueNamed[A].setId(id) >>
      Formatted[A].setName(loc.locName)) exec Default[A].default

    lens mod (m ⇒ m get id fold (_ ⇒ m, m + (id → newA)), a)
  }
}

// vim: set ts=2 sw=2 et:
