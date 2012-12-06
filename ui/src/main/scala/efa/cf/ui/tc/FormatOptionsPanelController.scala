package efa.cf.ui.tc

import efa.cf.format._
import efa.nb.tc.OptionsController
import scalaz._, Scalaz._, effect.IO

class FormatOptionsPanelController extends OptionsController[AllFormats,FormatPanel] (
  FormatPanel.create, identity, FormatPanel.in, Formats.set
)

//import efa.cf.format._
//import java.beans.{PropertyChangeSupport, PropertyChangeListener}
//import javax.swing.JComponent
//import org.netbeans.spi.options.OptionsPanelController
//import org.openide.util.Lookup
//import scalaz._, Scalaz._, effect.IO
//
//class FormatOptionsPanelController extends OptionsPanelController {
//  private[this] val pcs = new PropertyChangeSupport(this)
//
//  private[this] var p: Option[FormatPanel] = None
//
//  private def getPnl: IO[FormatPanel] = for {
//    op   ← IO(p)
//    res  ← op fold (IO(_), for {
//           newP ← FormatPanel.create
//           _    ← IO (p = Some(newP))
//         } yield newP)
//  } yield res
//
//  private def clearPnl: IO[Unit] = for {
//    op ← IO(p)
//    _  ← op fold (_.clear >> IO(p = None), IO.ioUnit)
//  } yield ()
//
//  private[this] var c = false
//  private[this] def hasChanged = IO(c)
//  private[this] def setChanged (b: Boolean) = IO{
//    if (c != b) {
//      c = b
//      pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, c, b)
//      pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null)
//    }
//  }
//  
//  override def update() {setChanged(false).unsafePerformIO}
//
//  private[this] def applyCs: IO[Unit] = for {
//    op ← IO(p)
//    _  ← op fold (_.applyChanges, IO.ioUnit)
//    _  ← clearPnl
//    _  ← setChanged(false)
//  } yield ()
//
//  override def applyChanges() {applyCs.unsafePerformIO}
//
//  private[this] def cncl: IO[Unit] = clearPnl >> setChanged(false)
//
//  override def cancel() {cncl.unsafePerformIO}
//
//  override def isValid = true
//  override def isChanged = hasChanged.unsafePerformIO
//  override def getHelpCtx = null
//  override def getComponent(lkp: Lookup): JComponent =
//    getPnl.unsafePerformIO
//
//  override def addPropertyChangeListener(l: PropertyChangeListener) {
//    pcs.addPropertyChangeListener(l)
//  }
//
//  override def removePropertyChangeListener(l: PropertyChangeListener) {
//    pcs.removePropertyChangeListener(l);
//  }
//}

// vim: set ts=2 sw=2 et:
