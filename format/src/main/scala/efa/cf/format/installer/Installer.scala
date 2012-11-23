package efa.cf.format.installer

import efa.cf.format.Formats
import org.openide.modules.ModuleInstall

class Installer extends ModuleInstall {
  override def closing () = {
    Formats.store().unsafePerformIO
    true
  }
}

// vim: set ts=2 sw=2 et:
