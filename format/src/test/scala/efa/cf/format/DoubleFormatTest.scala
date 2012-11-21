package efa.cf.format

import efa.core.{ToXmlSpecs}
import org.scalacheck._, Prop._

object DoubleFormatTest extends Properties("DoubleFormat") with ToXmlSpecs {
  property("toXml") = Prop forAll writeReadXml[DoubleFormat]
}

// vim: set ts=2 sw=2 et:
