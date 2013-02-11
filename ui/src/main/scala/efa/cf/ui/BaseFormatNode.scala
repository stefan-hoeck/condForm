package efa.cf.ui

import efa.core.{UniqueId, ValSt, Default, ParentL, Efa}, Efa._
import efa.cf.format._
import efa.cf.format.{BaseFormat ⇒ BF, FullFormat ⇒ FF}
import efa.nb.dialog.DialogEditable
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._
import shapeless.{HNil}

object BaseFormatNode extends NbNodeFunctions with NbChildrenFunctions {
  lazy val allOut: AfOut[AllFormats] = children(
    singleF(FormatPropNode.rootOut),
    singleF(GradientColorsNode.rootOut),
    singleF(booleanOut),
    singleF(doubleOut)
  ) ∙ { _ :: HNil }

  lazy val booleanOut: AfOut[AfRoot] =
    baseFormatOut[BooleanFormat](loc.booleanFormats)

  lazy val doubleOut: AfOut[AfRoot] =
    baseFormatOut[DoubleFormat](loc.doubleFormats)

  def baseFormatOut[A:Formatted:Equal:Default]
    (locName: String)
    (implicit P: ParentL[StringMap, AllFormats, BF[A], AfRoot],
      ADE: DialogEditable[FF[A],A],
      UID: UniqueId[A,String])
    : AfOut[AfRoot] = {

    implicit val AP = P mplusLensed BF.formats[A]

    def bluePrints(p: BasePath[A]): List[A] = {
      val d = Default.!!![A]

      p.last.fpBluePrints map { Formatted[A].formatPropsL set (d, _) }
    }

    val out: AfOut[FF[A]] =
      (named: AfOut[FF[A]]) ⊹
      editDialogP ⊹ 
      destroyP ⊹
      contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
      iconBaseA("efa/cf/ui/single.png") ⊹
      ColorEditor.colorA(Formatted[A] background _.head)

    val fbOut: AfOut[BasePath[A]]=
      (named: AfOut[BasePath[A]]) ⊹ 
      children(parentNamedF(out)) ⊹ 
      editDialogP ⊹ 
      clearNt ⊹
      addNtDialogsP(bluePrints) ⊹
      contextRootsA(List("ContextActions/BaseFormatNode")) ⊹
      iconBaseA("efa/cf/ui/base.png") ⊹ 
      ColorEditor.colorA(_.head.props.background)

    (nameA(locName): AfOut[AfRoot]) ⊹
    children(parentNamedF(fbOut)) ⊹
    iconBaseA("efa/cf/ui/format.png")
  }
}

// vim: set ts=2 sw=2 et:
