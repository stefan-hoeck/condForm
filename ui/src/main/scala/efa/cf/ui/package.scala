package efa.cf

import efa.core.{Service, ValSt}
import efa.nb.dialog.DialogEditable
import efa.cf.format._
import efa.cf.ui.spi.UiLocal

package object ui {
  lazy val loc = Service.unique[UiLocal](UiLocal)

  type FFEditable[A] = DialogEditable[FullFormat[A],A]

  implicit val FormatPropsEditable: FFEditable[FormatProps] =
    DialogEditable.io(FPPanel.formatPropsP)(_.in)

  implicit val BooleanFormatEditable: FFEditable[BooleanFormat] =
    DialogEditable.io(FPPanel.booleanP)(_.in)

  implicit val DoubleFormatEditable: FFEditable[DoubleFormat] =
    DialogEditable.io(FPPanel.doubleP)(_.in)
}

// vim: set ts=2 sw=2 et: