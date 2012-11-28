package efa.cf.editors

import efa.core.{Localization ⇒ Loc}
import efa.cf.format.Formats
import efa.nb.node.{NodeOut ⇒ NO, NbNode ⇒ N}
import scalaz._, Scalaz._, effect.IO

trait EditorFunctions {
  def fBool[A](
    l: Loc,
    get: A ⇒ Boolean,
    desc: A ⇒ String = (_: A) ⇒ ""
  ): NO[A,Nothing] =
    N.writeProp(l.name, get, BooleanEditor(l, desc, get)) ⊹
    register(Formats registerBoolean l)

  def fDouble[A](
    l: Loc,
    get: A ⇒ Double,
    desc: A ⇒ String = (_: A) ⇒ ""
  ): NO[A,Nothing] =
    N.writeProp(l.name, get, DoubleEditor(l, desc, get)) ⊹
    register(Formats registerDouble l)

  def fString[A](
    l: Loc,
    get: A ⇒ String,
    desc: A ⇒ String = (_: A) ⇒ ""
  ): NO[A,Nothing] =
    N.writeProp(l.name, get, StringEditor(l, desc, get)) ⊹
    register(Formats registerString l)

  private def register (io: IO[Unit]): NO[Any,Nothing] = NO((_, _) ⇒ _ ⇒ io)
}

object Editors extends EditorFunctions

// vim: set ts=2 sw=2 et:
