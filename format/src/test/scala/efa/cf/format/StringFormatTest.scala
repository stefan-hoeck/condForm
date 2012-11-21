package efa.cf.format

import efa.core.{ToXmlSpecs}
import org.scalacheck._, Prop._

object StringFormatTest extends Properties("StringFormat") with ToXmlSpecs {
  property("toXml") = Prop forAll writeReadXml[StringFormat]
}

// vim: set ts=2 sw=2 et:
