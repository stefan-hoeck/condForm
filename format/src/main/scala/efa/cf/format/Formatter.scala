package efa.cf.format

import scalaz._, Scalaz._

trait Formatter[-T,S] {
  def matches(s: S, t: T): Boolean
  def format(bf: BaseFormat[S], t: T): BaseFormat[S] \/ S =
    bf.formats find { matches(_, t) } toRightDisjunction bf
}

// vim: set ts=2 sw=2 et:
