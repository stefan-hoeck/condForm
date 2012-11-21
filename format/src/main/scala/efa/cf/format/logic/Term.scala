package efa.cf.format.logic

import efa.core.{Read, ToXml}
import org.scalacheck.{Gen, Arbitrary}, Arbitrary.arbitrary
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

abstract class Term[T] extends Function1[T,Boolean]{
  def apply(t: T) = matches(t)
  def matches(t: T): Boolean
}

private[logic] trait Binary[A] extends Term[A]{
  def t1: Term[A]
  def t2: Term[A]
  protected def symbol: String

  final override def toString = "(%s) %s (%s)" format (t1, symbol, t2)
}

case class And[A](t1: Term[A], t2: Term[A]) 
   extends Term[A] with Binary[A] {
  override def matches(t: A) = (t1 matches t) && (t2 matches t)
  protected override def symbol = "AND"
}

case class Or[A](t1: Term[A], t2: Term[A]) 
   extends Term[A] with Binary[A] {
  override def matches(t: A) = (t1 matches t) || (t2 matches t)
  protected override def symbol = "OR"
}

case class Unary[A <% Ordered[A]](comp: Comp, value: A)
   extends Term[A] {
  override def matches(v: A) = comp.compare(v, value)
  override def toString = "%s %s" format (comp.symbol, value)
}

case object Nan extends Term[Double] {
  override def matches(d: Double) = d.isNaN
  override def toString = "NaN"
}

case class True[A]() extends Term[A] {
  override def matches(t: A) = true
  override def toString = "True"
}

object Term {
  implicit def TermEqual[A:Equal] = new Equal[Term[A]] {
    def equal (a: Term[A], b: Term[A]): Boolean = (a,b) match {
      case (True(), True())           ⇒ true
      case (Nan, Nan)                 ⇒ true
      case (And(a, b), And(c, d))     ⇒ equal(a,c) && equal(b, d)
      case (Or(a, b), Or(c, d))       ⇒ equal(a,c) && equal(b, d)
      case (Unary(a, b), Unary(c, d)) ⇒ (a, b) ≟ (c, d)
      case _                          ⇒ false
    }
  }

  def termGen[A<%Ordered[A]](g: Gen[A]): Gen[Term[A]] = {
    val unaryG = ^(arbitrary[Comp], g)(Unary[A])

    val trueG: Gen[Term[A]] = Gen value True()

    lazy val orG: Gen[Term[A]] = for {
      _ ← arbitrary[Unit]
      a ← termGen
      b ← termGen
    } yield Or(a, b)

    lazy val andG: Gen[Term[A]] = for {
      _ ← arbitrary[Unit]
      a ← termGen
      b ← termGen
    } yield And(a, b)

    lazy val termGen: Gen[Term[A]] = Gen frequency (
      (50, unaryG), (10, trueG), (10, orG), (10, andG)
    )

    termGen
  }

  implicit lazy val DoubleTermArbitrary: Arbitrary[Term[Double]] =
    Arbitrary (
      Gen frequency (
        (5, Gen value Nan),
        (95, termGen[Double](arbitrary[Double]))
      )
    )

  implicit lazy val DoubleTermShow: Show[Term[Double]] =
    Show.shows(_.toString)

  implicit lazy val DoubleTermRead: Read[Term[Double]] = 
    Read.readV(DoubleParser.apply)

  implicit lazy val DoubleTermToXml: ToXml[Term[Double]] = ToXml.readShow

}

// vim: set ts=2 sw=2 et:
