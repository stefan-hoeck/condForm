package efa.cf.format

import efa.core.{ToXmlSpecs}
import org.scalacheck._, Prop._

object GradientTest extends Properties("Gradient") with ToXmlSpecs {
  property("toXml") = Prop forAll writeReadXml[Gradient]
}

// vim: set ts=2 sw=2 et:
