package efa.cf.format

import efa.core.{ToXmlSpecs}
import org.scalacheck._, Prop._

object GradientColorsTest extends Properties("GradientColors") with ToXmlSpecs {
  property("toXml") = Prop forAll writeReadXml[GradientColors]
}

// vim: set ts=2 sw=2 et:
