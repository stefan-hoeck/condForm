package efa.cf.ui

//import efa.cf.format._, efa.cf.format.{FullFormat ⇒ FF, GradientColors ⇒ GC}
//import efa.core.{Efa, Validators, loc ⇒ cLoc}, Efa._
//import efa.nb.VSIn
//import efa.nb.dialog.DialogPanel
//import scalaz._, Scalaz._, effect.IO
//
//class GradientColorsPanel(ff: FF[GC]) extends DialogPanel {
//  private def gc = ff.format
//
//  val nameC = textField(gc.name)
//
//  lazy val in: VSIn[GC] =
//    ^(
//      stringIn(nameC, Validators uniqueString (ff.invalidNames, cLoc.name)),
//      gc.colors.η[VSIn]
//    )(GC.apply)
//
//  cLoc.name beside nameC add()
//
//  setWidth(400)
//}
//
//object GradientColorsPanel {
//  def create (f: FF[GC]): IO[GradientColorsPanel] =
//    IO(new GradientColorsPanel(f))
//}

// vim: set ts=2 sw=2 et:
