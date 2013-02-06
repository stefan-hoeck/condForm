package efa.cf.ui

import efa.core.{ValSt, Efa}, Efa._
import efa.cf.format._
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._, effect._

object FormatPropNode extends NbNodeFunctions with NbChildrenFunctions {
//
//  lazy val rootOut: NodeOut[AllFormats,ValSt[AllFormats]] =
//    (children(uniqueIdF(fpOut)) ∙ ((_: AllFormats).bluePrintsFF)) ⊹
//    clearNt ⊹ 
//    (addNtDialogEs(l.addBluePrint) ∙ (_.fpBluePrint)) ⊹
//    nameA(loc.bluePrints) ⊹
//    contextRootsA(List("ContextActions/BluePrintsNode")) ⊹
//    iconBaseA("efa/cf/ui/format.png")

  lazy val fpOut: NodeOut[FpPath,ValSt[AllFormats]] =
//    destroyEs(l.delBluePrint) ⊹ 
    named[FpPath] ⊹
    contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
    iconBaseA("efa/cf/ui/format.png") ⊹
//    (editDialog[FFF,FormatProps] withIn (l.updateBluePrint(_,_).success)) ⊹
    ColorEditor.colorA(_.head.background)
}

// vim: set ts=2 sw=2 et:
