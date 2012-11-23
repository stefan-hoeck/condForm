package efa.cf.ui

import efa.core.ValSt
import efa.cf.format._
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._, effect._

object FormatPropNode extends NbNodeFunctions with NbChildrenFunctions {
  type FF[A] = FullFormat[A]

  lazy val l = Lens.self[AllFormats]

  lazy val rootOut: NodeOut[AllFormats,ValSt[AllFormats]] =
    (children(uniqueIdF(fpOut)) ∙ ((_: AllFormats).bluePrintsFF)) ⊹
    clearNt ⊹ 
    (addNtDialogEs(l.addBluePrint) ∙ (_.fpBluePrint)) ⊹
    nameA(loc.bluePrints) ⊹
    contextRootsA(List("ContextActions/BaseFormatNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")

  lazy val fpOut: NodeOut[FF[FormatProps],ValSt[AllFormats]] =
    destroyEs(l.delBluePrint) ⊹ 
    name(_.format.name) ⊹
    contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
    iconBaseA("efa/cf/ui/format.png") ⊹
    editDialogEs(l.addBluePrint)
}

// vim: set ts=2 sw=2 et:
