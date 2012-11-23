package efa.cf.format

import efa.core.UniqueId
import java.awt.Color
import scalaz.Show

trait HasFormatProps[A] extends UniqueId[A,String] with Show[A] {
  def background (a: A): Color = formatProps(a).background
  def foreground (a: A): Color = formatProps(a).foreground
  def formatProps (a: A): FormatProps
  def locName (a: A): String = formatProps(a).name
  override def shows (a: A) = locName (a)
}

object HasFormatProps {
  def apply[A:HasFormatProps]: HasFormatProps[A] = implicitly
}

// vim: set ts=2 sw=2 et:
