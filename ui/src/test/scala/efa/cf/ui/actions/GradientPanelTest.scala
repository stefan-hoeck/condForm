package efa.cf.ui.actions

import efa.cf.format.{Gradient, Formats, ColumnInfo, AllFormats}
import efa.nb.PureLookup
import org.scalacheck._, Prop._
import scalaz._, Scalaz._, effect.IO

object GradientPanelTest extends Properties("GradientPanel") {

//  property("display_empty") = forAll {g: Gradient ⇒ 
//    val res = for {
//      _  ← Formats addGradient g
//      pl ← PureLookup.apply
//      p  ← GradientPanel createLkp pl.l
//      _  ← p.cleanUp
//    } yield
//      (p.tLower.text ≟ "0.0") :| ("tLower: " + p.tLower.text) &&
//      (p.tUpper.text ≟ "0.0") :| ("tUpper: " + p.tUpper.text) &&
//      (p.tNod.text ≟ "1") :| ("tNod: " + p.tNod.text) &&
//      (! p.setB.enabled) :| "setB" &&
//      (! p.removeB.enabled) :| "removeB"
//        
//    res.unsafePerformIO
//  }
//
//  property("display") = forAll {g: Gradient ⇒ 
//    val res = for {
//      _  ← Formats addGradient g
//      pl ← PureLookup.apply
//      p  ← GradientPanel createLkp pl.l
//      _  ← pl add ColumnInfo(g.name)
//      _  ← p.cleanUp
//    } yield
//      (p.tLower.text ≟ g.lower.toString) :| ("tLower: " + p.tLower.text) &&
//      (p.tUpper.text ≟ g.upper.toString) :| ("tUpper: " + p.tUpper.text) &&
//      (p.tNod.text ≟ g.nod.toString) :| ("tNod: " + p.tNod.text) &&
//      (p.setB.enabled) :| "setB" &&
//      (p.removeB.enabled) :| "removeB"
//        
//    res.unsafePerformIO
//  }
//
//  property("display after unselecting column") = forAll {g: Gradient ⇒ 
//    val res = for {
//      _  ← Formats addGradient g
//      pl ← PureLookup.apply
//      p  ← GradientPanel createLkp pl.l
//      _  ← pl add ColumnInfo(g.name)
//      _  ← pl remove ColumnInfo(g.name)
//      _  ← p.cleanUp
//    } yield
//      (p.tLower.text ≟ g.lower.toString) :| ("tLower: " + p.tLower.text) &&
//      (p.tUpper.text ≟ g.upper.toString) :| ("tUpper: " + p.tUpper.text) &&
//      (p.tNod.text ≟ g.nod.toString) :| ("tNod: " + p.tNod.text) &&
//      (! p.setB.enabled) :| "setB" &&
//      (! p.removeB.enabled) :| "removeB"
//        
//    res.unsafePerformIO
//  }
//
//  property("display column without gradient") = forAll {p: (Gradient, String) ⇒ 
//    val (g,n) = p
//
//    val res = for {
//      _  ← Formats addGradient g
//      pl ← PureLookup.apply
//      p  ← GradientPanel createLkp pl.l
//      _  ← pl add ColumnInfo(g.name)
//      _  ← pl remove ColumnInfo(g.name)
//      _  ← pl add ColumnInfo(n)
//      _  ← p.cleanUp
//    } yield
//      (p.tLower.text ≟ g.lower.toString) :| ("tLower: " + p.tLower.text) &&
//      (p.tUpper.text ≟ g.upper.toString) :| ("tUpper: " + p.tUpper.text) &&
//      (p.tNod.text ≟ g.nod.toString) :| ("tNod: " + p.tNod.text) &&
//      (p.setB.enabled) :| "setB" &&
//      (p.removeB.enabled ≟ (n ≟ g.name)) :| "removeB"
//        
//    res.unsafePerformIO
//  }
//
//  lazy val pairGen = for {
//    af ← Arbitrary.arbitrary[AllFormats]
//    n  ← Gen oneOf (af.colorNames)
//    g  ← Arbitrary.arbitrary[Gradient]
//  } yield (af, g copy (name = n))
//
//  property("set") = forAll (pairGen) {p ⇒ 
//    val (af, g) = p
//
//    val res = for {
//      _  ← Formats set af
//      pl ← PureLookup.apply
//      p  ← GradientPanel createLkp pl.l
//      _  ← pl add ColumnInfo(g.name)
//      _  ← IO{
//             p.tLower.text = g.lower.toString
//             p.tUpper.text = g.upper.toString
//             p.tNod.text = g.nod.toString
//             p.cGradient.selection.item = g.colors
//           }
//      _  ← IO(p.setB.doClick)
//      og ← Formats.now map (_.gradientsM get g.name)
//      _  ← p.cleanUp
//    } yield (og ≟ Some(g)) :| ("Exp %s but was %s" format (Some(g), og))
//        
//    res.unsafePerformIO
//  }

}

// vim: set ts=2 sw=2 et:
