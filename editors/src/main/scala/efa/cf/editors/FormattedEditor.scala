package efa.cf.editors

import efa.cf.format._
import efa.io.{LogDisIO, LogDisIOFunctions, LoggerIO}
import efa.react.Events
import java.awt.{Rectangle, Graphics}
import java.beans.PropertyEditorSupport
import scala.swing.Component
import scalaz._, Scalaz._, effect.IO

abstract class FormattedEditor[A,F](
    name: String,
    val value: A,
    description: String)(
    implicit f: Formatter[A,F],
    h: Formatted[F]) 
  extends PropertyEditorSupport
  with LogDisIOFunctions {

  type Comp <: Component
  type BF = BaseFormat[F]
  
  protected def register (f: AllFormats): Map[String,BF]

  protected def createComponent: LogDisIO[Comp]

  protected def displayUnformatted(comp: Comp): LogDisIO[Unit]

  protected def displayBaseFormatted(format: BF, comp: Comp)
    : LogDisIO[Unit]

  protected def displayFormatted(format: F, comp: Comp): LogDisIO[Unit]

  override protected def getAsText =
    if (description.isEmpty) null else description
  
  override def isPaintable = true

  override def paintValue(g: Graphics, r: Rectangle) {
    pref.cfLogger >>= (_ logDisZ doPaint(g, r)) unsafePerformIO
  }
   
  final private[editors] def doPaint(g: Graphics, r: Rectangle)
  : LogDisIO[Unit] = {
    def notFound (c: Comp) = for {
      _ ← warning("No base format found for property " + name)
      _ ← displayUnformatted(c)
    } yield ()

    def dispBase (c: Comp)(bf: BF) = for {
       _     ← trace("Base format used for property " + name)
       _     ← point(c.foreground = bf.props.foreground)
       _     ← point(c.background = bf.props.background)
       _     ← displayBaseFormatted(bf, c)
    } yield ()

    def dispF (c: Comp)(form: F) = for {
       _     ← trace("Format %s used for %s" format (form.toString, name))
       props = h formatProps form
       _     ← point(c.foreground = props.foreground)
       _     ← point(c.background = props.background)
       _     ← displayFormatted(form, c)
    } yield ()
    
    for {
      c     ← createComponent
      m     ← liftIO (Formats.now map register)
      _     ← m get name cata (
                f.format(_, value) fold (dispBase(c), dispF(c)),
                notFound(c)
              )
       _    ← point (c.peer.setBounds(r))
       _    ← point (c.peer.paint(g))
    } yield ()
  }
}

// vim: set ts=2 sw=2 et:
