package efa.cf

import efa.cf.format._
import efa.cf.ui.spi.UiLocal
import efa.core.{Service, ValSt, Efa}, Efa._
import efa.nb.dialog.{DialogEditable ⇒ DE}
import efa.nb.node.NodeOut
import efa.react.{AsSignal}
import java.awt.Color
import java.beans.{PropertyChangeListener ⇒ PCL, PropertyChangeEvent ⇒ PCE}
import org.efa.gui.ColorPicker
import scalaz.effect.IO

package object ui {
  lazy val loc = Service.unique[UiLocal](UiLocal)

  type AfOut[A] = NodeOut[A,ValSt[AllFormats]]

  private val ColCh = ColorPicker.SELECTED_COLOR_PROPERTY
  private val OpCh = ColorPicker.OPACITY_PROPERTY

  implicit val FormatPropsEditable = DE.io(FPPanel.formatPropsP)(_.in)
  implicit val BooleanFormatEditable = DE.io(FPPanel.booleanP)(_.in)
  implicit val DoubleFormatEditable = DE.io(FPPanel.doubleP)(_.in)
  implicit val GradientColorsEditable = DE.io(GradientColorsPanel.create)(_.in)
  implicit def BasePathEditable[A] = DE.io1(BaseFormatPanel.create[A])(_.in)

  implicit val IdColorEditable = new DE[FullColor,IdColor] {
    type Comp = IdColorPanel

    def component(fc: FullColor, ic: Boolean) = IdColorPanel create fc
    def signalIn(c: Comp) = c.in
    override def name(c: FullColor) = efa.cf.format.loc.color
  }

  implicit val ColorPanelAsSignal = AsSignal.unit[ColorPicker,Color,PCL](
    (cp, f) ⇒  {
      val pcl = new PCL {
        def propertyChange(e: PCE) {
          e.getPropertyName match {
            case ColCh ⇒ f(cp.getColor)
            case OpCh  ⇒ f(cp.getColor)
            case _     ⇒ ()
          }
        }
      }

      cp.addPropertyChangeListener(pcl)

      pcl
    }, _.getColor
  )((cp, pcl) ⇒ cp.removePropertyChangeListener(pcl))
}

// vim: set ts=2 sw=2 et:
