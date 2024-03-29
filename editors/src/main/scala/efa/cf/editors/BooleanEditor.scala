package efa.cf.editors

import efa.core.Localization
import efa.cf.format._
import scala.swing.CheckBox

private[editors] class BooleanEditor(name: String, v: Boolean, desc: String) 
extends FormattedEditor[Boolean,BooleanFormat](name, v, desc) {
  override type Comp = CheckBox
  override def createComponent = point(new CheckBox{opaque = true})
  override def register(f: AllFormats) = f.boolsM

  override protected def displayUnformatted(c: Comp) = point(c.selected = v)

  override protected def displayFormatted(f: BooleanFormat, c: Comp) =
    displayUnformatted(c)

  override protected def displayBaseFormatted(f: BF, c: Comp) =
    displayUnformatted(c)
}

object BooleanEditor {
  def apply[A](l: Localization, d: A ⇒ String, f: A ⇒ Boolean)
  : Option[A ⇒ BooleanEditor] =
    Some(a ⇒ new BooleanEditor(l.name, f(a), d(a)))
}

// vim: set ts=2 sw=2 et:
