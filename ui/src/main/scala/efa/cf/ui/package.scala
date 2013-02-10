package efa.cf

import efa.cf.format._
import efa.cf.ui.spi.UiLocal
import efa.core.{Service, ValSt, Efa}, Efa._
import efa.nb.dialog.{DialogEditable ⇒ DE}
import efa.nb.node.NodeOut
import efa.react.{AsSignal}
import java.awt.Color
import java.beans.{PropertyChangeListener, PropertyChangeEvent}
import org.efa.gui.ColorPicker
import scalaz.effect.IO

package object ui {
  lazy val loc = Service.unique[UiLocal](UiLocal)

  type AfOut[A] = NodeOut[A,ValSt[AllFormats]]

  implicit val FormatPropsEditable = DE.io(FPPanel.formatPropsP)(_.in)

  implicit val BooleanFormatEditable = DE.io(FPPanel.booleanP)(_.in)

  implicit val DoubleFormatEditable = DE.io(FPPanel.doubleP)(_.in)

  implicit val GradientColorsEditable = DE.io(GradientColorsPanel.create)(_.in)

  implicit def BasePathEditable[A] = DE.io1(BaseFormatPanel.create[A])(_.in)

  private[ui] def pickColor (c: Color): IO[Color] =
    IO(ColorPicker.showDialog(null, c, true))

  private[ui] lazy val pickColorWhite: IO[Color] = pickColor(Color.WHITE)

  private val ColCh = ColorPicker.SELECTED_COLOR_PROPERTY
  private val OpCh = ColorPicker.OPACITY_PROPERTY

  implicit val ColorPanelAsSignal = AsSignal.unit[ColorPicker,Color] (
    (cp, f) ⇒  cp.addPropertyChangeListener(
      new PropertyChangeListener {
        def propertyChange(e: PropertyChangeEvent) {
          e.getPropertyName match {
            case ColCh ⇒ f(cp.getColor)
            case OpCh  ⇒ f(cp.getColor)
            case _     ⇒ ()
          }
        }
      }
    ), _.getColor
  )
}

// vim: set ts=2 sw=2 et:
