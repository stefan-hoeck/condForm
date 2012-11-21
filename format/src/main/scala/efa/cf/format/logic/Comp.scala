package efa.cf.format.logic

import org.scalacheck.{Arbitrary, Gen}
import scalaz._, Scalaz._

sealed abstract class Comp {
  val name: String
  val symbol: String
  def compare[T <% Ordered[T]](t1: T, t2: T): Boolean
}

object Comp {
  case object Greater extends Comp {
    override val name = "greater"
    override val symbol = ">"
    override def compare[T <% Ordered[T]](t1: T, t2: T) = t1 > t2
  }

  case object GreaterEq extends Comp {
    override val name = "greaterEq"
    override val symbol = ">="
    override def compare[T <% Ordered[T]](t1: T, t2: T) = t1 >= t2
  }

  case object Equals extends Comp {
    override val name = "equals"
    override val symbol = "=="
    override def compare[T <% Ordered[T]](t1: T, t2: T) = t1 == t2
  }

  case object Smaller extends Comp {
    override val name = "smaller"
    override val symbol = "<"
    override def compare[T <% Ordered[T]](t1: T, t2: T) = t1 < t2
  }

  case object SmallerEq extends Comp {
    override val name = "smallerEq"
    override val symbol = "<="
    override def compare[T <% Ordered[T]](t1: T, t2: T) = t1 <= t2
  }

  val values = List[Comp](Greater, GreaterEq, Equals, Smaller, SmallerEq)
  val fromSymbol: Map[String, Comp] = 
    (values map (_.symbol) zip values).toMap
  val fromName: Map[String, Comp] = 
    (values map (_.name) zip values).toMap

  implicit lazy val CompEqual: Equal[Comp] = Equal.equalA
  implicit lazy val CompArbitrary = Arbitrary(Gen oneOf values)
}

// vim: set ts=2 sw=2 et:
