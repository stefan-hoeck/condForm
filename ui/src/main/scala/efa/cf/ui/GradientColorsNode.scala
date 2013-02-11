package efa.cf.ui

import efa.core.{ValSt, Efa}, Efa._
import efa.cf.format._, efa.cf.format.{loc ⇒ fLoc}
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import java.awt.Color
import scalaz._, Scalaz._, effect._

object GradientColorsNode extends NbNodeFunctions with NbChildrenFunctions {

  //@TODO add add
  lazy val rootOut: AfOut[AfRoot] =
    (clearNt: AfOut[AfRoot]) ⊹ 
    children(parentNamedF(gcOut)) ⊹
    //addNtDialogP ⊹
    nameA(loc.gradients) ⊹
    contextRootsA(List("ContextActions/GradientsNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")

  lazy val gcOut: AfOut[GcPath] =
    (named[GcPath]: AfOut[GcPath]) ⊹ 
    children(parentF(colorOut)) ⊹
    destroyP ⊹
    editDialogP ⊹
    contextRootsA(List("ContextActions/SingleGradientNode")) ⊹
    iconBaseA("efa/cf/ui/format.png") ⊹
    clearNt ⊹
    createNtDialogP[List,AllFormats,IdColor,GcPath,Int]{_ ⇒ (-1,Color.WHITE)}

  lazy val colorOut: AfOut[FullColor] =
    (destroyP: AfOut[FullColor]) ⊹ 
    ColorEditor.colorA[FullColor](_.head._2) ⊹
    contextRootsA(List("ContextActions/SingleColorNode")) ⊹
    iconBaseA("efa/cf/ui/format.png")
}

// vim: set ts=2 sw=2 et:
