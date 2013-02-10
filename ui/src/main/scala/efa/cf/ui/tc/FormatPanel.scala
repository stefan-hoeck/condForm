package efa.cf.ui.tc

import efa.cf.format._, efa.cf.format.{loc ⇒ fLoc}
import efa.cf.ui.BaseFormatNode
import efa.core.ValRes
import efa.nb.controller.StateTransFunctions
import efa.nb.node.NbNode
import efa.nb.tc.OutlinePanel
import efa.react.{Signal, Connectors, SIn}
import scalaz._, Scalaz._, effect.IO

class FormatPanel (n: NbNode) extends OutlinePanel {
  override def rootNode = n
  override protected def localizations = List(fLoc.colorL)
}

object FormatPanel extends StateTransFunctions {
  def create: IO[FormatPanel] = for {
    n ← NbNode.apply
  } yield new FormatPanel(n)

  def in (p: FormatPanel): SIn[ValRes[AllFormats]] =
    basicIn(BaseFormatNode.allOut set p.rootNode)(Formats.now) ∘ (_.success)

}

// vim: set ts=2 sw=2 et:
