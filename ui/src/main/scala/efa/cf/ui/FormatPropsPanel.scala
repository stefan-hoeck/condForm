package efa.cf.ui

import efa.cf.format._, efa.cf.format.{FullFormat ⇒ FF}
import efa.cf.format.logic.Term
import efa.core.{Efa, Validators, loc ⇒ cLoc}, Efa._
import efa.nb.VSIn
import efa.nb.dialog.DialogPanel
import scalaz._, Scalaz._, effect.IO

abstract class FPPanel[A:Formatted] (ff: FF[A]) extends DialogPanel {
  protected def a: A = ff.format

  val nameC = textField(Formatted[A] locName a)
  val fore = new ColorPanel(Formatted[A] foreground a)
  val back = new ColorPanel(Formatted[A] background a)

  def in: VSIn[A]

  lazy val fpIn: VSIn[FormatProps] = ^^(
    fore.colorIn,
    back.colorIn,
    stringIn(nameC, Validators uniqueString (ff.invalidNames, cLoc.name))
  )(FormatProps.apply)
}

class FormatPropsPanel (ff: FF[FormatProps]) extends FPPanel(ff) {
  lazy val in = fpIn
    
  (efa.core.loc.name beside nameC) above
  (loc.foreground beside fore) above
  (loc.background beside back) add()

  setWidth(400)
}

class BooleanFP (ff: FF[BooleanFormat]) extends FPPanel(ff) {
  val valueC = comboBox(a.value, List(true, false))
  lazy val in = ^(fpIn, comboBox(valueC))(BooleanFormat.apply)
    
  (efa.core.loc.name beside nameC) above
  (efa.core.loc.value beside valueC) above
  (loc.foreground beside fore) above
  (loc.background beside back) add()

  setWidth(400)
}

class DoubleFP (ff: FF[DoubleFormat]) extends FPPanel(ff) {
  val termC = textField(a.term.shows)
  val formatC = textField(a.formatString)
  lazy val in = ^^(
    fpIn,
    textIn[Term[Double]](termC),
    stringIn(formatC)
  )(DoubleFormat.apply)
    
  (efa.core.loc.name beside nameC) above
  (loc.doubleTerm beside termC) above
  (loc.formatString beside formatC) above
  (loc.foreground beside fore) above
  (loc.background beside back) add()

  setWidth(400)
}

object FPPanel {
  def formatPropsP (f: FF[FormatProps]): IO[FormatPropsPanel] =
    IO(new FormatPropsPanel(f))

  def booleanP (f: FF[BooleanFormat]): IO[BooleanFP] = IO(new BooleanFP(f))

  def doubleP (f: FF[DoubleFormat]): IO[DoubleFP] = IO(new DoubleFP(f))
}

// vim: set ts=2 sw=2 et:
