package efa.cf.format.logic

import Comp._
import efa.core.{ReadSpecs, ToXmlSpecs}
import org.scalacheck._, Prop._
import scalaz._, Scalaz._

object DoubleTermTest
   extends Properties("DoubleTerm")
   with ReadSpecs 
   with ToXmlSpecs {
  property("showRead") = Prop forAll showRead[Term[Double]]

  property("readAll") = Prop forAll readAll[Term[Double]]

  property("toXml") = Prop forAll writeReadXml[Term[Double]]

  property("matches") = Prop forAll {p: (Term[Double], Double) ⇒ 
    val (t, d) = p

    matches(t, d) ≟ t.matches(d)
  }

  private def matches (t: Term[Double], d: Double): Boolean = t match {
    case Nan ⇒ d.isNaN
    case True() ⇒ true
    case Unary(Greater, v) ⇒ d > v
    case Unary(GreaterEq, v) ⇒ d >= v
    case Unary(Equals, v) ⇒ d == v
    case Unary(Smaller, v) ⇒ d < v
    case Unary(SmallerEq, v) ⇒ d <= v
    case And (a, b) ⇒ (a matches d) && (b matches d)
    case Or (a, b) ⇒ (a matches d) || (b matches d)
  }
}

// vim: set ts=2 sw=2 et:
