package efa.cf.ui

import efa.core.{ValSt, Efa}, Efa._
import efa.cf.format._
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._, effect._

object FormatPropNode extends NbNodeFunctions with NbChildrenFunctions {

  lazy val rootOut: AfOut[AfRoot] =
    children(parentNamedF(fpOut)) ⊹
    clearNt ⊹
    addNtDialogP {_ ⇒ FormatProps.bluePrint} ⊹
    nameA(loc.bluePrints) ⊹
    contextRootsA(List("ContextActions/BluePrintsNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")

  lazy val fpOut: AfOut[FpPath] =
    (named[FpPath]: AfOut[FpPath]) ⊹
    destroyP ⊹ 
    contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
    iconBaseA("efa/cf/ui/format.png") ⊹
    editDialogP ⊹
    ColorEditor.colorA(_.head.background)
}

// vim: set ts=2 sw=2 et:
