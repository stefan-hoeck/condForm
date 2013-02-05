package efa.cf.format

trait Formatter[-T,-S] {
  def matches(s: S, t: T): Boolean
}

// vim: set ts=2 sw=2 et:
