package efa.cf.ui

import efa.core.Efa._
import efa.react.swing.{GbPanel, Swing}, Swing._
import efa.nb.VSIn
import java.awt.Color
import org.efa.gui.ColorPicker
import scala.swing.{Button, TextField}
import scala.swing.GridBagPanel.Fill
import scalaz._, Scalaz._, effect.IO

class ColorPanel (c: Color) extends GbPanel {
  val btn = new Button("...")
  val nameC = new TextField("", 15){editable = false}

  private def editColor (c: Color): IO[Color] =
    IO(ColorPicker.showDialog(null, c, true))

  lazy val colorIn: VSIn[Color] =
    backgroundS(nameC) on btn mapIO editColor hold c to 
    Swing.background(nameC) map (_.success)

  nameC beside Single(btn, f = Fill.None) add()

  setWidth(400)

}

// vim: set ts=2 sw=2 et:
