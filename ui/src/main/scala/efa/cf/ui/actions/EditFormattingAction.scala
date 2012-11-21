//package org.efa.cformat.ui.actions
////
////import java.awt.event.ActionEvent
////import org.efa.cformat.ui.AllFormatsSignal
////import org.efa.cformat.ui.tc.{FormatPanel, FormatOptionsPanelController}
////import org.efa.dialog.Input
////import org.efa.util.prop.NbBundled
////import org.openide.util.HelpCtx
////import org.openide.util.actions.SystemAction
////
////class EditFormattingAction 
////extends SystemAction
////with NbBundled {
////  override def getName = message("CTL_editFormattingAction")
////  override def getHelpCtx = HelpCtx.DEFAULT_HELP
////  override def actionPerformed(e: ActionEvent): Unit = {
////    val pnl = new FormatPanel
////    if (Input(pnl)) FormatOptionsPanelController.applyChanges()
////    else AllFormatsSignal.reset()
////  }
////  override def iconResource = "org/efa/cformat/ui/tc/format.png"
////}
//
//// vim: set ts=2 sw=2 et:
