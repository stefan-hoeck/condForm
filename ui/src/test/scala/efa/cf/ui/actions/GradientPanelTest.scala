package efa.cf.ui.actions

import efa.cf.format.{Gradient, Formats, ColumnInfo}
import efa.nb.PureLookup
import org.scalacheck._, Prop._
import scalaz._, Scalaz._, effect.IO

object GradientPanelTest extends Properties("GradientPanel") {

  property("display_empty") = forAll {g: Gradient ⇒ 
    val res = for {
      _  ← Formats addGradient g
      pl ← PureLookup.apply
      p  ← GradientPanel createLkp pl.l
    } yield
      (p.tBad.text ≟ "0.0") :| ("tBad: " + p.tBad.text) &&
      (p.tGood.text ≟ "0.0") :| ("tGood: " + p.tGood.text) &&
      (p.tNod.text ≟ "1") :| ("tNod: " + p.tNod.text) &&
      (! p.setB.enabled) :| "setB" &&
      (! p.removeB.enabled) :| "removeB"
        
    res.unsafePerformIO
  }

  property("display") = forAll {g: Gradient ⇒ 
    val res = for {
      _  ← Formats addGradient g
      pl ← PureLookup.apply
      p  ← GradientPanel createLkp pl.l
      _  ← pl add ColumnInfo(g.name)
    } yield
      (p.tBad.text ≟ g.bad.toString) :| ("tBad: " + p.tBad.text) &&
      (p.tGood.text ≟ g.good.toString) :| ("tGood: " + p.tGood.text) &&
      (p.tNod.text ≟ g.nod.toString) :| ("tNod: " + p.tNod.text) &&
      (p.setB.enabled) :| "setB" &&
      (p.removeB.enabled) :| "removeB"
        
    res.unsafePerformIO
  }

  property("display after unselecting column") = forAll {g: Gradient ⇒ 
    val res = for {
      _  ← Formats addGradient g
      pl ← PureLookup.apply
      p  ← GradientPanel createLkp pl.l
      _  ← pl add ColumnInfo(g.name)
      _  ← pl remove ColumnInfo(g.name)
    } yield
      (p.tBad.text ≟ g.bad.toString) :| ("tBad: " + p.tBad.text) &&
      (p.tGood.text ≟ g.good.toString) :| ("tGood: " + p.tGood.text) &&
      (p.tNod.text ≟ g.nod.toString) :| ("tNod: " + p.tNod.text) &&
      (! p.setB.enabled) :| "setB" &&
      (! p.removeB.enabled) :| "removeB"
        
    res.unsafePerformIO
  }

  property("display column without gradient") = forAll {p: (Gradient, String) ⇒ 
    val (g,n) = p

    val res = for {
      _  ← Formats addGradient g
      pl ← PureLookup.apply
      p  ← GradientPanel createLkp pl.l
      _  ← pl add ColumnInfo(g.name)
      _  ← pl remove ColumnInfo(g.name)
      _  ← pl add ColumnInfo(n)
    } yield
      (p.tBad.text ≟ g.bad.toString) :| ("tBad: " + p.tBad.text) &&
      (p.tGood.text ≟ g.good.toString) :| ("tGood: " + p.tGood.text) &&
      (p.tNod.text ≟ g.nod.toString) :| ("tNod: " + p.tNod.text) &&
      (p.setB.enabled) :| "setB" &&
      (p.removeB.enabled ≟ (n ≟ g.name)) :| "removeB"
        
    res.unsafePerformIO
  }

  property("set") = forAll {g: Gradient ⇒ 
    val res = for {
      pl ← PureLookup.apply
      p  ← GradientPanel createLkp pl.l
      _  ← pl add ColumnInfo(g.name)
      _  ← IO{
             p.tBad.text = g.bad.toString
             p.tGood.text = g.good.toString
             p.tNod.text = g.nod.toString
           }
      _  ← IO(p.setB.doClick)
      og ← Formats.now map (_.gradientsM get g.name)
    } yield (og ≟ Some(g)) :| ("Exp %s but was %s" format (Some(g), og))
        
    res.unsafePerformIO
  }

}

// vim: set ts=2 sw=2 et:
