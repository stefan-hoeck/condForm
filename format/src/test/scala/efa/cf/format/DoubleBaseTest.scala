package efa.cf.format

import efa.core.{ToXmlSpecs}
import org.scalacheck._, Prop._

object DoubleBaseTest extends Properties("DoubleBase") with ToXmlSpecs {
  import Formats._

  property("toXml") = Prop forAll writeReadXml[DoubleBase]
}

// vim: set ts=2 sw=2 et:
