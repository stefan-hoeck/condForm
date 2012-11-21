package efa.cf.format

trait StringFormatted[-T] {
  def format(t: T): String
}

object StringFormatted {
  def apply[A:StringFormatted]: StringFormatted[A] = implicitly
}

// vim: set ts=2 sw=2 et:
