package efa.cf.editors

import efa.cf.format._
import scala.swing.CheckBox

private[editors] class BooleanEditor(name: String, v: Boolean, desc: String) 
extends FormattedEditor[Boolean,BooleanFormat](name, v, desc) {
  override type Comp = CheckBox
  override def createComponent = point(new CheckBox)
  override def register(f: AllFormats) = f.boolsM

  override protected def displayUnformatted(c: Comp) = point(c.selected = v)

  override protected def displayFormatted(f: BooleanFormat, c: Comp) =
    displayUnformatted(c)

  override protected def displayBaseFormatted(f: BF, c: Comp) =
    displayUnformatted(c)
}

// vim: set ts=2 sw=2 et:
