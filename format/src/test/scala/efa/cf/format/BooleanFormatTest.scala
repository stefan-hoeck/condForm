package efa.cf.format

import efa.core.{ToXmlSpecs}
import org.scalacheck._, Prop._

object BooleanFormatTest extends Properties("BooleanFormat") with ToXmlSpecs {
  property("toXml") = Prop forAll writeReadXml[BooleanFormat]
}

// vim: set ts=2 sw=2 et:
