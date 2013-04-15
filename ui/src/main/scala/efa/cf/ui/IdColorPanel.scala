package efa.cf.ui

import efa.cf.format.{IdColor, FullColor}
import efa.nb.VSIn
import efa.nb.dialog.DialogPanel
import java.awt.Color
import java.awt.BorderLayout.CENTER
import org.efa.gui.ColorPicker
import scala.swing.BorderPanel
import scalaz._, Scalaz._, effect.IO

class IdColorPanel(fc: FullColor) extends DialogPanel {
  val cp = new ColorPicker(true, true)
  cp.setColor(c._2)

  (new BorderPanel{peer.add(cp, CENTER)} fillH 1 fillV 1) add()

  def in: VSIn[IdColor] = sin(cp) ∘ { (c._1, _) }

  private def c = fc.head
}

object IdColorPanel {
  def create(c: FullColor): IO[IdColorPanel] = IO(new IdColorPanel(c))
}

// vim: set ts=2 sw=2 et:
