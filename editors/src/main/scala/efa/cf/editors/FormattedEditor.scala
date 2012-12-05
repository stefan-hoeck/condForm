package efa.cf.editors

import efa.cf.format._
import efa.io.{ValLogIO, ValLogIOFunctions, LoggerIO}
import efa.react.Events
import java.awt.{Rectangle, Graphics}
import java.beans.PropertyEditorSupport
import scala.swing.Component
import scalaz._, Scalaz._, effect.IO

abstract class FormattedEditor[A,F](
  name: String, val value: A, description: String
)(implicit f: Formatter[A,F]) 
  extends PropertyEditorSupport with ValLogIOFunctions {

  type Comp <: Component
  type BF = BaseFormat[F]
  
  protected def register (f: AllFormats): Map[String,BF]

  protected def createComponent: ValLogIO[Comp]

  protected def displayUnformatted(comp: Comp): ValLogIO[Unit]

  protected def displayBaseFormatted(format: BF, comp: Comp)
    : ValLogIO[Unit]

  protected def displayFormatted(format: F, comp: Comp): ValLogIO[Unit]

  override protected def getAsText =
    if (description.isEmpty) null else description
  
  override def isPaintable = true

  override def paintValue(g: Graphics, r: Rectangle) {
    pref.cfLogger >>= (_ logValM doPaint(g, r)) unsafePerformIO
  }
   
  final private[editors] def doPaint(g: Graphics, r: Rectangle)
  : ValLogIO[Unit] = {
    def notFound (c: Comp) = for {
      _ ← warning("No base format found for property " + name)
      _ ← displayUnformatted(c)
    } yield ()

    def dispBase (bf: BF, c: Comp) = for {
       _     ← trace("Base format used for property " + name)
       _     ← point(c.foreground = bf.props.foreground)
       _     ← point(c.background = bf.props.background)
       _     ← displayBaseFormatted(bf, c)
    } yield ()

    def dispF (form: F, c: Comp) = for {
       _     ← trace("Format %s used for %s" format (form.toString, name))
       props = f formatPropsS form
       _     ← point(c.foreground = props.foreground)
       _     ← point(c.background = props.background)
       _     ← displayFormatted(form, c)
    } yield ()
    
    for {
      c     ← createComponent
      m     ← liftIO (Formats.now map register)
      _     ← m get name fold (
                p ⇒ p.formats find (f matches (_, value)) fold (
                  dispF (_, c),
                  dispBase (p, c)
                ),
                notFound(c)
              )
       _    ← point (c.peer.setBounds(r))
       _    ← point (c.peer.paint(g))
    } yield ()
  }
}

// vim: set ts=2 sw=2 et:
