package efa.cf.ui.actions

import efa.cf.format.Formats
import efa.cf.ui.tc.FormatPanel
import efa.nb.dialog.Input
import efa.nb.actions.NbSystemAction
import scalaz._, Scalaz._, effect.IO

class EditFormattingAction
   extends NbSystemAction (efa.cf.ui.loc.editFormattingsAction) {

  def run = for {
    p  ← FormatPanel.create
    cs ← FormatPanel in p runIO () 
    b ← Input(p)
    _ ← if (b) cs._2.now >>= (_ fold (_ ⇒ IO.ioUnit, Formats.set))
        else IO.ioUnit
    _ ← cs._1.toList foldMap (_.disconnect)
  } yield ()

  override def iconResource = "efa/cf/ui/tc/format.png"
}

// vim: set ts=2 sw=2 et:
