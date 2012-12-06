package efa.cf.ui

import efa.core.Efa._
import efa.react.swing.{GbPanel, Swing}, Swing._
import efa.nb.VSIn
import java.awt.Color
import scala.swing.{Button, BorderPanel}
import scala.swing.GridBagPanel.Fill
import scalaz._, Scalaz._

class ColorPanel (c: Color) extends BorderPanel {
  val btn = new Button("...")

  opaque = true

  lazy val colorIn: VSIn[Color] =
    backgroundS(this) on btn mapIO pickColor hold c to 
    Swing.background(this) map (_.success)

  add(btn, BorderPanel.Position.East)
}

// vim: set ts=2 sw=2 et:
