package efa.cf.ui

import efa.core.ValSt
import efa.cf.format._, efa.cf.format.{loc ⇒ fLoc}
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import java.awt.Color
import scalaz._, Scalaz._, effect._

object GradientColorsNode extends NbNodeFunctions with NbChildrenFunctions {
  type FFG = FullFormat[GradientColors]
  type CSI = (Color,String,Int)

  lazy val l = Lens.self[AllFormats]

  lazy val rootOut: NodeOut[AllFormats,ValSt[AllFormats]] =
    (children(uniqueIdF(ffgOut)) ∙ ((_: AllFormats).gradientColorsFF)) ⊹
    clearNt ⊹ 
    (addNtDialogEs(l.addGCs) ∙ (_.gcBluePrint)) ⊹
    nameA(loc.gradients) ⊹
    contextRootsA(List("ContextActions/GradientsNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")

  lazy val ffgOut: NodeOut[FFG,ValSt[AllFormats]] =
    (children(seqF(colorOut)) ∙ colorNameIdx) ⊹
    destroyEs(l delGCs _.format.name) ⊹ 
    (editDialog[FFG,GradientColors] withIn (l.updateGCs(_,_).success)) ⊹
    name(_.format.name) ⊹
    contextRootsA(List("ContextActions/SingleGradientNode")) ⊹
    iconBaseA("efa/cf/ui/format.png") ⊹
    clearNt ⊹
    addColor

  lazy val colorOut: NodeOut[CSI,ValSt[AllFormats]] =
    destroyEs{ t: CSI ⇒ l delColor (t._2, t._3) } ⊹ 
    ColorEditor.colorA[(Color,String,Int)](_._1) ⊹
    contextRootsA(List("ContextActions/SingleColorNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")

  private def colorNameIdx (f: FFG): List[CSI] =
    f.format.colors
     .zipWithIndex
     .map {case (c,i) ⇒ (c,f.format.name,i)}
     .toList

  private lazy val addColor: NodeOut[FFG,ValSt[AllFormats]] = {
    def addFFG: NodeOut[FFG,FFG] = addNt[FFG] ∙ ((_,fLoc.color))

     addFFG mapIO (f ⇒
       pickColorWhite map (l addColor (f.format.name, _) success))
  }
}

// vim: set ts=2 sw=2 et:
