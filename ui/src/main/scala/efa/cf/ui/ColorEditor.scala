package efa.cf.ui

import efa.cf.format.{loc ⇒ fLoc}
import efa.nb.node.{NbNode, NodeOut}
import java.awt.{Color, Rectangle, Graphics}
import java.beans.PropertyEditorSupport
import scala.swing.{BorderPanel, Label}

class ColorEditor (c: Color) extends PropertyEditorSupport {
  private lazy val pnl = new Label("   "){opaque = true; background = c}

  override def isPaintable = true

  override def paintValue(g: Graphics, r: Rectangle) {
    pnl.peer.setBounds(r)
    pnl.peer.paint(g)
  }
}

object ColorEditor {
  lazy val color: NodeOut[Color,Nothing] =
    NbNode.writeProp[Color,Color](
      fLoc.colorL.name, identity, Some(c ⇒ new ColorEditor(c))
    )

  def colorA[A] (f: A ⇒ Color): NodeOut[A,Nothing] =
    color contramap f
}

// vim: set ts=2 sw=2 et:
