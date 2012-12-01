package efa.cf.ui

import efa.core.ValSt
import efa.cf.format._
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._, effect._

object GradientColorsNode extends NbNodeFunctions with NbChildrenFunctions {
  type FFG = FullFormat[GradientColors]

  lazy val l = Lens.self[AllFormats]

  lazy val rootOut: NodeOut[AllFormats,ValSt[AllFormats]] =
    (children(uniqueIdF(fpOut)) ∙ ((_: AllFormats).gradientColorsFF)) ⊹
    clearNt ⊹ 
    //(addNtDialogEs(l.addGCs) ∙ (_.gcBluePrint)) ⊹
    nameA(loc.gradients) ⊹
    contextRootsA(List("ContextActions/BaseFormatNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")

  lazy val fpOut: NodeOut[FFG,ValSt[AllFormats]] =
    destroyEs(l.delGCs) ⊹ 
    name(_.format.name) ⊹
    contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")
}

// vim: set ts=2 sw=2 et:
