package efa.cf.ui

import efa.nb.VSIn
import efa.nb.dialog.DialogPanel
import efa.cf.format._
import scalaz._, Scalaz._, effect.IO

class BaseFormatPanel[A] private (val bf: BaseFormat[A])
   extends DialogPanel {

  val fore: ColorPanel = new ColorPanel(bf.props.foreground)
  val back: ColorPanel = new ColorPanel(bf.props.background)
  val fStringC = textField(bf.fString)
    
  lazy val in: VSIn[BaseFormat[A]] = ^^^(
    bf.name.η[VSIn],
    ^^(fore.colorIn, back.colorIn, bf.props.name.η[VSIn])(FormatProps.apply),
    bf.formats.η[VSIn],
    stringIn(fStringC)
  )(BaseFormat.apply)

  (loc.formatString beside fStringC) above
  (loc.foreground beside fore) above
  (loc.background beside back) add()

  setWidth(400)
}

object BaseFormatPanel {
  def create[A] (f: BaseFormat[A]): IO[BaseFormatPanel[A]] =
    IO(new BaseFormatPanel(f))
}

// vim: set ts=2 sw=2 et:
