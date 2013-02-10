package efa.cf.format

import efa.core.Named
import java.awt.Color
import scalaz._, Scalaz._

trait Formatted[A] extends Named[A] {
  def formatPropsL: A @> FormatProps
  def formatProps (a: A) = formatPropsL get a
  def setName (s: String): State[A,Unit] = formatPropsL.name := s void
  def background (a: A): Color = formatProps(a).background
  def foreground (a: A): Color = formatProps(a).foreground
  def name(a: A): String = formatProps(a).name
}

object Formatted {
  def apply[A:Formatted]: Formatted[A] = implicitly
}

// vim: set ts=2 sw=2 et:
