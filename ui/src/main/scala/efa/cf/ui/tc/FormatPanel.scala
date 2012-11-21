package efa.cf.ui.tc
//
//import java.awt.BorderLayout
//import javax.swing.JPanel
//import org.openide.explorer.{ExplorerManager, ExplorerUtils}
//import efa.cf.format._
//import org.openide.explorer.view.OutlineView
//import javax.swing.text.DefaultEditorKit
//import org.openide.util.{Lookup, NbBundle}
//
//class FormatPanel extends JPanel 
//   with ExplorerManager.Provider 
//   with Lookup.Provider {
//
//  private val mgr = new ExplorerManager
//  private val outlineView = new OutlineView(efa.core.loc.name)
//          
//  getActionMap.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(mgr))
//  getActionMap.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(mgr))
//  getActionMap.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(mgr))
//  getActionMap.put("delete", ExplorerUtils.actionDelete(mgr, true))
//  private val lookup = ExplorerUtils.createLookup (mgr, getActionMap)
//  
//  //mgr setRootContext new FormatRoot()
//  outlineView.getOutline.setRootVisible(false)
//  setLayout(new BorderLayout)
//  add(outlineView, BorderLayout.CENTER)        
//
//  override def getExplorerManager = mgr
//  override def getLookup = lookup
//
//  private[tc] def applyChanges(): Unit = sys.error("Not implemented")
//}
//
//// vim: set ts=2 sw=2 et:
