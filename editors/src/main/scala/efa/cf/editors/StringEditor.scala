package efa.cf.editors

import efa.core.Localization
import efa.cf.format._
import scala.swing.{Label, Alignment}

private[editors] class StringEditor(name: String, value: String, desc: String) 
extends FormattedEditor[String,StringFormat](name, value, desc) {
  override type Comp = Label
  override def createComponent =
    point(new Label{
      opaque = true
      horizontalAlignment = Alignment.Leading
    })

  override def register (f: AllFormats) = f.stringsM

  override protected def displayUnformatted(c: Comp) =
    point(c.text = value)

  override protected def displayFormatted(f: StringFormat, c: Comp) =
    point(c.text = value)

  override protected def displayBaseFormatted(f: BF, c: Comp) =
    point(c.text = value)
}

object StringEditor {
  def apply[A](l: Localization, d: A ⇒ String, f: A ⇒ String)
  : Option[A ⇒ StringEditor] =
    Some(a ⇒ new StringEditor(l.name, f(a), d(a)))
}

// vim: set ts=2 sw=2 et:
