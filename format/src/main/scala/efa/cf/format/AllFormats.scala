package efa.cf.format

import efa.core.Default
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
  stringsM: Map[String,StringBase]
) {
  import AllFormats._

  lazy val bluePrints = m2s(bluePrintsM) sortBy (_.name)

  lazy val bluePrintsFF =
    bluePrints ∘ (FullFormat("", _, bluePrintsM.keySet, false))

  def fpBluePrint: FullFormat[FormatProps] = FullFormat(
    "", FormatProps.bluePrint, bluePrintsM.keySet, true
  )

  private def m2s[A](m: Map[String,A]) = m.toList map (_._2)
  
  def fullBases[A](l: AllFormats @> Map[String,BaseFormat[A]]) =
    (l get this).toList map (p ⇒ FullBase(p._2, bluePrints))
}

object AllFormats {
  private def e[A,B]: Map[A,B] = Map.empty

  val default = AllFormats(e, e, e, e)

  implicit val AllFormatsDefault = Default default default

  implicit val AllFormatsEqual = Equal.equalA[AllFormats]

  implicit val AllFormatsArbitrary: Arbitrary[AllFormats] = Arbitrary(
    ^^^(
      mapGen[FormatProps],
      mapGen[BooleanBase],
      mapGen[DoubleBase],
      mapGen[StringBase]
    )(AllFormats.apply)
  )

  val bluePrintsM: AllFormats @> Map[String,FormatProps] =
    Lens.lensu((a,b) ⇒ a copy (bluePrintsM = b), _.bluePrintsM)

  val boolsM: AllFormats @> Map[String,BooleanBase] =
    Lens.lensu((a,b) ⇒ a copy (boolsM = b), _.boolsM)

  val doublesM: AllFormats @> Map[String,DoubleBase] =
    Lens.lensu((a,b) ⇒ a copy (doublesM = b), _.doublesM)
  
  val stringsM: AllFormats @> Map[String,StringBase] =
    Lens.lensu((a,b) ⇒ a copy (stringsM = b), _.stringsM)
  
  implicit def allFormatsLenses[A] (l: Lens[A,AllFormats]) = new {
    lazy val bluePrintsM = l andThen AllFormats.bluePrintsM
    lazy val boolsM = l andThen AllFormats.boolsM
    lazy val doublesM = l andThen AllFormats.doublesM
    lazy val stringsM = l andThen AllFormats.stringsM

    def delBluePrint (f: FullFormat[FormatProps]) =
      bluePrintsM -= f.format.name void

    def addBluePrint (f: FormatProps) = bluePrintsM += (f.name → f) void
  }
}

// vim: set ts=2 sw=2 et:
