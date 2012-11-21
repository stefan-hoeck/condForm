package efa.cf.format

import efa.core.{ToXmlSpecs}
import org.scalacheck._, Prop._

object FormatPropsTest extends Properties("FormatProps") with ToXmlSpecs {
  property("toXml") = Prop forAll writeReadXml[FormatProps]
}

// vim: set ts=2 sw=2 et:
