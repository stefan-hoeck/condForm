package efa.cf.editors

import efa.cf.format._
import scala.swing.CheckBox

private[editors] class DoubleEditor(name: String, v: Double, desc: String) 
extends FormattedEditor[Double,DoubleFormat](name, v, desc) {
  override type Comp = CheckBox
  override def createComponent = point(new CheckBox)
  override def register(f: AllFormats) = f.doublesM

  override protected def displayUnformatted(c: Comp) =
    point(c.text = v.toString)

  override protected def displayFormatted(f: DoubleFormat, c: Comp) =
    point(c.text = f.formatString format v)

  override protected def displayBaseFormatted(f: BF, c: Comp) =
    point(c.text = f.fString format v)
}

// vim: set ts=2 sw=2 et:
