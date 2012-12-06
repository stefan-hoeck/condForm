package efa.cf.ui.tc

import efa.cf.format._
import efa.nb.tc.OptionsController
import scalaz._, Scalaz._, effect.IO

class FormatOptionsPanelController extends OptionsController[AllFormats,FormatPanel] (
  FormatPanel.create, _.peer, FormatPanel.in, Formats.set
)

// vim: set ts=2 sw=2 et:
