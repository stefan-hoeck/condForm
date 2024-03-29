package efa.cf.ui

import efa.core.Efa._
import efa.react.swing.{GbPanel, Swing}, Swing._
import efa.nb.VSIn
import java.awt.Color
import org.efa.gui.ColorPicker
import scala.swing.{Button, BorderPanel}
import scala.swing.GridBagPanel.Fill
import scalaz._, Scalaz._, effect.IO

class ColorPanel (c: Color) extends BorderPanel {
  val btn = new Button("...")

  opaque = true

  lazy val colorIn: VSIn[Color] =
    backgroundS(this) on btn mapIO ColorPanel.pickColor hold c to 
    Swing.background(this) map (_.success)

  add(btn, BorderPanel.Position.East)
}

object ColorPanel {
  private[ui] def pickColor (c: Color): IO[Color] =
    IO(ColorPicker.showDialog(null, c, true))
}

// vim: set ts=2 sw=2 et:
