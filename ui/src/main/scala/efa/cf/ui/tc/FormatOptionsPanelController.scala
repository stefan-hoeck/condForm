package efa.cf.ui.tc
//
//import java.beans.{PropertyChangeSupport, PropertyChangeListener}
//import javax.swing.JComponent
//import efa.cf.format._
//import org.netbeans.spi.options.OptionsPanelController
//import org.openide.util.Lookup
//
//class FormatOptionsPanelController extends OptionsPanelController {
//  private val pcs = new PropertyChangeSupport(this)
//  private var pnl: Option[FormatPanel] = None
//
//  private def panel = {
//    if (pnl.isEmpty) pnl = Some (new FormatPanel)
//    pnl.get
//  }
//
//  private[this] var c = false
//  private def changed = c
//  private def changed_= (boo: Boolean) = {
//    if (c != boo) {
//      c = boo
//      pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, c, boo)
//      pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null)
//    }
//  }
//  
//  override def update() {changed = false}
//
//  override def applyChanges() {
//    pnl foreach (_.applyChanges())
//    pnl = None
//    changed = false
//  }
//
//  override def cancel() {
//    pnl = None
//    changed = false
//  }
//
//  override def isValid = true
//  override def isChanged = changed
//  override def getHelpCtx = null
//  override def getComponent(lkp: Lookup): JComponent = panel
//
//  override def addPropertyChangeListener(l: PropertyChangeListener) {
//    pcs.addPropertyChangeListener(l)
//  }
//
//  override def removePropertyChangeListener(l: PropertyChangeListener) {
//    pcs.removePropertyChangeListener(l);
//  }
//}
//
//// vim: set ts=2 sw=2 et:
