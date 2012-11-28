package efa.cf.format

import efa.core.UniqueId
import scalaz.@>

trait UniqueNamed[A] extends UniqueId[A,String] {
  def uniqueNameL: A @> String
  def id (a: A): String = uniqueNameL get a
  def setId (a: A, s: String): A = uniqueNameL set (a, s)
}

object UniqueNamed {
  def apply[A:UniqueNamed]: UniqueNamed[A] = implicitly
}

// vim: set ts=2 sw=2 et:
