package efa.cf

import efa.core.{Service, ValSt}
import efa.nb.dialog.DialogEditable
import efa.cf.format._
import efa.cf.ui.spi.UiLocal
import java.awt.Color
import org.efa.gui.ColorPicker
import scalaz.effect.IO

package object ui {
  lazy val loc = Service.unique[UiLocal](UiLocal)

  type FFEditable[A] = DialogEditable[FullFormat[A],A]

  implicit val FormatPropsEditable: FFEditable[FormatProps] =
    DialogEditable.io(FPPanel.formatPropsP)(_.in)

  implicit val BooleanFormatEditable: FFEditable[BooleanFormat] =
    DialogEditable.io(FPPanel.booleanP)(_.in)

  implicit val DoubleFormatEditable: FFEditable[DoubleFormat] =
    DialogEditable.io(FPPanel.doubleP)(_.in)

  implicit val GradientColorsEditable: FFEditable[GradientColors] =
    DialogEditable.io(GradientColorsPanel.create)(_.in)

  implicit def FullBaseEditable[A] =
    DialogEditable.io(BaseFormatPanel.create[A])(_.in)

  private[ui] def pickColor (c: Color): IO[Color] =
    IO(ColorPicker.showDialog(null, c, true))

  private[ui] lazy val pickColorWhite: IO[Color] = pickColor(Color.WHITE)
}

// vim: set ts=2 sw=2 et:
