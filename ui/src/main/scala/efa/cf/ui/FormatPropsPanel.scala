package efa.cf.ui

import efa.cf.format._, efa.cf.format.{FullFormat ⇒ FF}
import efa.cf.format.logic.Term
import efa.core.{Efa, Validators, loc ⇒ cLoc, Named}, Efa._
import efa.nb.VSIn
import efa.nb.dialog.DialogPanel
import scalaz._, Scalaz._, effect.IO

abstract class FPPanel[A:Formatted](
    val a: A,
    names: Set[String],
    isCreate: Boolean)
  extends DialogPanel {

  val nameC = textField(aName)
  val fore = new ColorPanel(Formatted[A] foreground a)
  val back = new ColorPanel(Formatted[A] background a)

  lazy val fpIn: VSIn[FormatProps] = ^^(
    fore.colorIn,
    back.colorIn,
    stringIn(nameC, Validators uniqueString (invalidNames, cLoc.name))
  )(FormatProps.apply)

  def in: VSIn[A]

  private def invalidNames = isCreate ? names | (names - aName)
  private def aName = Formatted[A] name a
}

abstract class FFPanel[A:Formatted](ff: FF[A], ic: Boolean)
  extends FPPanel[A](
    ff.head,
    ff.tail.head.formats map Named[A].name toSet,
    ic)

class FormatPropsPanel (fp: FpPath, isCreate: Boolean)
    extends FPPanel(fp.head, fp.last.bluePrintsM.keySet, isCreate) {
  lazy val in = fpIn
    
  (efa.core.loc.name beside nameC) above
  (loc.foreground beside fore) above
  (loc.background beside back) add()

  setWidth(400)
}

class BooleanFP (ff: FF[BooleanFormat], ic: Boolean) extends FFPanel(ff, ic) {
  val valueC = comboBox(a.value, List(true, false))
  lazy val in = ^(fpIn, comboBox(valueC))(BooleanFormat.apply)
    
  (efa.core.loc.name beside nameC) above
  (efa.core.loc.value beside valueC) above
  (loc.foreground beside fore) above
  (loc.background beside back) add()

  setWidth(400)
}

class DoubleFP (ff: FF[DoubleFormat], ic: Boolean) extends FFPanel(ff, ic) {
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
  def formatPropsP (f: FpPath, ic: Boolean): IO[FormatPropsPanel] =
    IO(new FormatPropsPanel(f, ic))

  def booleanP (f: FF[BooleanFormat], ic: Boolean): IO[BooleanFP] =
    IO(new BooleanFP(f, ic))

  def doubleP (f: FF[DoubleFormat], ic: Boolean): IO[DoubleFP] =
    IO(new DoubleFP(f, ic))
}

// vim: set ts=2 sw=2 et:
