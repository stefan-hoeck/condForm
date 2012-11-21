package efa.cf.format

import efa.core.{ReadSpecs, ToXmlSpecs}
import java.awt.Color
import org.scalacheck._, Prop._
import scalaz._, Scalaz._

object ColorTest
   extends Properties("Color")
   with ReadSpecs 
   with ToXmlSpecs {
  property("showRead") = Prop forAll showRead[Color]

  property("readAll") = Prop forAll readAll[Color]

  property("toXml") = Prop forAll writeReadXml[Color]
}

// vim: set ts=2 sw=2 et:
