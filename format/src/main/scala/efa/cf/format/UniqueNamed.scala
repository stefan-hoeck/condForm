package efa.cf.format

import efa.core.UniqueId
import scalaz._, Scalaz._

trait UniqueNamed[A] extends UniqueId[A,String] {
  def uniqueNameL: A @> String
  def id (a: A): String = uniqueNameL get a
  def setId (s: String): State[A,Unit] = uniqueNameL := s void
}

object UniqueNamed {
  def apply[A:UniqueNamed]: UniqueNamed[A] = implicitly
}

// vim: set ts=2 sw=2 et:
