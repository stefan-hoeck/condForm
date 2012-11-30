package efa.cf.ui.actions

import efa.cf.ui.tc.FormatPanel
import efa.nb.dialog.Input
import efa.nb.actions.NbSystemAction
import scalaz._, Scalaz._, effect.IO

class EditFormattingAction
   extends NbSystemAction (efa.cf.ui.loc.editFormattingsAction) {

  def run = for {
    p ← FormatPanel.create
    b ← Input(p)
    _ ← if (b) p.applyChanges else IO.ioUnit
    _ ← p.clear
  } yield ()

  override def iconResource = "efa/cf/ui/tc/format.png"
}

// vim: set ts=2 sw=2 et:
