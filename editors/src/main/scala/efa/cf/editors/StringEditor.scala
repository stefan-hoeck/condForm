package efa.cf.editors

import efa.cf.format._
import scala.swing.CheckBox

private[editors] class StringEditor(name: String, value: String, desc: String) 
extends FormattedEditor[String,StringFormat](name, value, desc) {
  override type Comp = CheckBox
  override def createComponent = point(new CheckBox)
  override def register (f: AllFormats) = f.stringsM

  override protected def displayUnformatted(c: Comp) =
    point(c.text = value)

  override protected def displayFormatted(f: StringFormat, c: Comp) =
    point(c.text = value)

  override protected def displayBaseFormatted(f: BF, c: Comp) =
    point(c.text = value)
}

// vim: set ts=2 sw=2 et:
