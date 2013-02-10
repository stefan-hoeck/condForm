package efa.cf.ui

import efa.cf.format._
import efa.core.{Efa, Validators, loc ⇒ cLoc}, Efa._
import efa.nb.VSIn
import efa.nb.dialog.DialogPanel
import scalaz._, Scalaz._, effect.IO

class GradientColorsPanel(gcp: GcPath, isCreate: Boolean) extends DialogPanel {
  private def gc = gcp.head

  val nameC = textField(gc.name)

  lazy val in: VSIn[GradientColors] =
    ^(
      stringIn(nameC, Validators uniqueString (invalidNames, cLoc.name)),
      gc.colors.η[VSIn]
    )(GradientColors.apply)

  cLoc.name beside nameC add()

  private def invalidNames = isCreate ? names | (names - gc.name)
  private def names = gcp.last.gradientColorsM.keySet

  setWidth(400)
}

object GradientColorsPanel {
  def create (f: GcPath, ic: Boolean): IO[GradientColorsPanel] =
    IO(new GradientColorsPanel(f, ic))
}

// vim: set ts=2 sw=2 et:
