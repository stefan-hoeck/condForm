package efa.cf.ui

import efa.core.ValSt
import efa.cf.format._, efa.cf.format.{loc ⇒ fLoc}
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import java.awt.Color
import scalaz._, Scalaz._, effect._

object GradientColorsNode extends NbNodeFunctions with NbChildrenFunctions {
//  type FFG = FullFormat[GradientColors]
//  type CSI = (Color,String,Int)
//
//  lazy val l = Lens.self[AllFormats]
//
//  lazy val rootOut: NodeOut[AllFormats,ValSt[AllFormats]] =
//    children(uniqueIdF(ffgOut)((_: AllFormats).gradientColorsFF)) ⊹
//    clearNt ⊹ 
//    (addNtDialogEs(l.addGCs) ∙ (_.gcBluePrint)) ⊹
//    nameA(loc.gradients) ⊹
//    contextRootsA(List("ContextActions/GradientsNode")) ⊹
//    iconBaseA("efa/cf/ui/format.png")
//
//  lazy val ffgOut: NodeOut[FFG,ValSt[AllFormats]] =
//    children(leavesF(colorOut)(colorNameIdx)) ⊹
//    destroyEs(l delGCs _.format.name) ⊹ 
//    (editDialog[FFG,GradientColors] withIn (l.updateGCs(_,_).success)) ⊹
//    name(_.format.name) ⊹
//    contextRootsA(List("ContextActions/SingleGradientNode")) ⊹
//    iconBaseA("efa/cf/ui/format.png") ⊹
//    clearNt ⊹
//    addColor
//
  lazy val colorOut: AfOut[FullColor] =
    (destroyP: AfOut[FullColor]) ⊹ 
    ColorEditor.colorA[FullColor](_.head._2) ⊹
    contextRootsA(List("ContextActions/SingleColorNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")
}

// vim: set ts=2 sw=2 et:
