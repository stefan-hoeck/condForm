package efa.cf.ui.tc

import efa.cf.format._
import efa.cf.ui.BaseFormatNode
import efa.core.ValRes
import efa.nb.controller.StateTransFunctions
import efa.nb.node.NbNode
import efa.react.{Signal, Connectors, SIn}
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.text.DefaultEditorKit
import org.openide.explorer.{ExplorerManager, ExplorerUtils}
import org.openide.explorer.view.OutlineView
import org.openide.util.Lookup
import scalaz._, Scalaz._, effect.IO

class FormatPanel (val n: NbNode) extends JPanel 
   with ExplorerManager.Provider 
   with Lookup.Provider {

  private val mgr = new ExplorerManager
  private val outlineView = new OutlineView(efa.core.loc.name)
          
  getActionMap.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(mgr))
  getActionMap.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(mgr))
  getActionMap.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(mgr))
  getActionMap.put("delete", ExplorerUtils.actionDelete(mgr, true))
  private val lookup = ExplorerUtils.createLookup (mgr, getActionMap)
  
  mgr.setRootContext(n)
  outlineView.getOutline.setRootVisible(false)
  setLayout(new BorderLayout)
  add(outlineView, BorderLayout.CENTER)        

  override def getExplorerManager = mgr
  override def getLookup = lookup
}

object FormatPanel extends StateTransFunctions {
  def create: IO[FormatPanel] = for {
    n ← NbNode.apply
  } yield new FormatPanel(n)

  def in (p: FormatPanel): SIn[ValRes[AllFormats]] =
    basicIn (BaseFormatNode.allOut set p.n)(Formats.now) ∘ (_.success)

}

// vim: set ts=2 sw=2 et:
