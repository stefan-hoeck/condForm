package efa.cf.ui.tc

import efa.cf.format._
import efa.cf.ui.BaseFormatNode
import efa.nb.controller.StateTransFunctions
import efa.nb.node.NbNode
import efa.react.{Signal, Connectors}
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.text.DefaultEditorKit
import org.openide.explorer.{ExplorerManager, ExplorerUtils}
import org.openide.explorer.view.OutlineView
import org.openide.util.Lookup
import scalaz._, Scalaz._, effect.IO

class FormatPanel (
  n: NbNode, s: Signal[AllFormats], cs: Connectors
) extends JPanel 
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

  private[ui] def applyChanges: IO[Unit] = s.now >>= Formats.set

  private[ui] def clear: IO[Unit] = cs.toList foldMap (_.disconnect)
}

object FormatPanel extends StateTransFunctions {
  def create: IO[FormatPanel] = for {
    n ← NbNode.apply
    p ← basicIn (BaseFormatNode.allOut set n)(Formats.now) go
  } yield new FormatPanel(n, p._2, p._1)
}

// vim: set ts=2 sw=2 et:
