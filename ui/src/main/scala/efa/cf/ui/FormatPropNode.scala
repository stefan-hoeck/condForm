package efa.cf.ui

import efa.core.ValSt
import efa.cf.format._
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._, effect._

object FormatPropNode extends NbNodeFunctions with NbChildrenFunctions {
  //lazy val l = Lens.self[AllFormats]

  //lazy val rootOut: NodeOut[List[FormatProps],ValSt[AllFormats]] =
  //  children(uniqueIdF(fpOut)) ⊹
  //  clearNt ⊹ 
  //  (addNtDialogEs(l.addBluePrint) ∙ (_ ⇒ FormatProps.bluePrint)) ⊹
  //  nameA(loc.bluePrints) ⊹
  //  contextRootsA(List("ContextActions/BaseFormatNode")) ⊹
  //  iconBaseA("efa/cf/ui/format.png")

  //lazy val fpOut: NodeOut[FormatProps,ValSt[AllFormats]] =
  //  destroyEs(l.delBluePrint) ⊹ 
  //  name[FormatProps](_.name) ⊹
  //  contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
  //  iconBaseA("efa/cf/ui/format.png") ⊹
  //  editDialogEs(l.addBluePrint)
}

// vim: set ts=2 sw=2 et:
