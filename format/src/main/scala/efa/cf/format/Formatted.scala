package efa.cf.format

import scalaz._, Scalaz._

trait Formatted[A] extends HasFormatProps[A] {
  def formatPropsL: A @> FormatProps
  def formatProps (a: A) = formatPropsL get a
  def setName (s: String): State[A,Unit] = formatPropsL.name := s void
}

object Formatted {
  def apply[A:Formatted]: Formatted[A] = implicitly
}

// vim: set ts=2 sw=2 et:
