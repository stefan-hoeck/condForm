package efa.cf.ui

import efa.core.{UniqueId, ValSt, Default}
import efa.cf.format._
import efa.cf.format.{AllFormats ⇒ AF, BaseFormat ⇒ BF, FullFormat ⇒ FF}
import efa.nb.dialog.DialogEditable
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._

object BaseFormatNode extends NbNodeFunctions with NbChildrenFunctions {
  lazy val allOut: NodeOut[AF,ValSt[AF]] = ??? //children(
//    singleF(FormatPropNode.rootOut),
//    singleF(GradientColorsNode.rootOut),
//    singleF(booleanOut),
//    singleF(doubleOut)
//  )
//
//  lazy val booleanOut: NodeOut[AF,ValSt[AF]] =
//    baseFormatOut[BooleanFormat](loc.booleanFormats, AF.boolsM)
//
//  lazy val doubleOut: NodeOut[AF,ValSt[AF]] =
//    baseFormatOut[DoubleFormat](loc.doubleFormats, AF.doublesM)
//
//  def baseFormatOut[A:Formatted:FFEditable:Equal:Default](
//    locName: String, l: AF @> Map[String,BF[A]]
//  ): NodeOut[AF,ValSt[AF]] = {
//    def adjFs (ff: FF[A], f: List[A] ⇒ List[A]): State[AF,Unit] =
//      (l at ff.baseId).formats mods_ f
//
//    def delete(f: FF[A]) = adjFs(f, _ filterNot (f.format ≟))
//
//    def update (f: FF[A], a: A) = delete(f) >> adjFs(f, a :: _)
//
//    def nts: NodeOut[FB[A],ValSt[AF]] = {
//      val singleNt = addNtDialog[FF[A],A] withIn update map (_.success)
//
//      NodeOut((o,n) ⇒ _.bluePrints foldMap singleNt.run(o,n))
//    }
//
//    val out: NodeOut[FF[A],ValSt[AF]] = 
//      destroyEs(delete) ⊹
//      (editDialog[FF[A],A] withIn update map (_.success)) ⊹
//      name(Formatted[A] locName _.format) ⊹
//      contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
//      iconBaseA("efa/cf/ui/single.png") ⊹
//      ColorEditor.colorA(Formatted[A] background _.format)
//
//    val fbOut: NodeOut[FB[A],ValSt[AF]] =
//      (children(uniqueIdF(out)) ∙ ((_: FB[A]).baseFormat.fullList)) ⊹
//      editDialogEs((bf: BF[A]) ⇒ l += (bf.id → bf) void)⊹
//      name[FB[A]](_.locName) ⊹ 
//      clearNt ⊹
//      nts ⊹
//      contextRootsA(List("ContextActions/BaseFormatNode")) ⊹
//      iconBaseA("efa/cf/ui/base.png") ⊹ 
//      ColorEditor.colorA(_.baseFormat.props.background)
//
//    (children(uniqueIdF(fbOut)) ∙ ((_: AF) fullBases l)) ⊹
//    nameA(locName) ⊹
//    iconBaseA("efa/cf/ui/format.png")
//  }
}

// vim: set ts=2 sw=2 et:
