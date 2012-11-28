package efa.cf.editors

import efa.core.Localization
import efa.cf.format._
import efa.io.ValLogIO
import java.awt.{Rectangle, Graphics}
import scala.swing.{Label, Alignment}
import scalaz._, Scalaz._

private[editors] class DoubleEditor(name: String, v: Double, desc: String) 
   extends FormattedEditor[Double,DoubleFormat](name, v, desc) {
  override type Comp = Label
  override def createComponent =
    point(new Label{horizontalAlignment = Alignment.Trailing})

  override def register(f: AllFormats) = f.doublesM

  override protected def displayUnformatted(c: Comp) =
    point(c.text = v.toString)

  override protected def displayFormatted(f: DoubleFormat, c: Comp) =
    point(c.text = f.formatString format v)

  override protected def displayBaseFormatted(f: BF, c: Comp) =
    point(c.text = f.fString format v)

  override def paintValue(g: Graphics, r: Rectangle) {
    Formats.logger logValM doPaintG(g, r) unsafePerformIO
  }

  private def doPaintG (g: Graphics, r: Rectangle): ValLogIO[Unit] = for {
    og    ← liftIO (Formats.now map (_.gradientsM get name))
    _     ← og fold (
              gr ⇒ createComponent >>= (c ⇒ point {
                c.text = gr.formatString format v
                c.background = gr colorFor v
                c.peer.setBounds(r)
                c.peer.paint(g)
              }),
              doPaint(g, r)
            )
  } yield ()
}

object DoubleEditor {
  def apply[A](l: Localization, d: A ⇒ String, f: A ⇒ Double)
  : Option[A ⇒ DoubleEditor] =
    Some(a ⇒ new DoubleEditor(l.name, f(a), d(a)))
}

// vim: set ts=2 sw=2 et:
