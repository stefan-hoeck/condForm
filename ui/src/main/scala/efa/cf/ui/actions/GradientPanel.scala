package efa.cf.ui.actions

import efa.cf.format.{Gradient, ColumnInfo, Formats}
import efa.cf.ui.{loc ⇒ uLoc}
import efa.core.ValRes
import efa.nb.{LookupResultWrapper, VSIn}
import efa.nb.dialog.DialogPanel
import efa.react._, swing.Swing
import org.openide.util.{Utilities, Lookup}
import scala.swing.{Button, TextField, Component}
import scalaz._, Scalaz._, effect.IO

class GradientPanel (lrw: LookupResultWrapper[ColumnInfo])
   extends DialogPanel {

  import GradientPanel._
  
  val tBad = new TextField(0.0D.toString, 5)
  val tGood = new TextField(0.0D.toString, 5)
  val tNod = new TextField(1.toString, 3)
  val setB = new Button(uLoc.set)
  val removeB = new Button(uLoc.remove)
   
  (uLoc.bad beside tBad beside uLoc.good beside tGood beside
   uLoc.nod beside tNod beside setB beside removeB).add()

  //name of selected column
  private[this] lazy val nameS: SIn[Option[String]] =
    eTrans in lrw map (_.headOption map (_.name)) hold none

  //gradient for selected column
  private[this] lazy val gradS: SIn[OGrad] =
    ^(nameS, Formats.formats)(_ >>= _.gradientsM.get)

  //validated user input
  private[this] lazy val in: VSIn[Gradient] = Apply[VSIn].apply4(
    nameS map (_ toSuccess "".wrapNel),
    intIn(tNod, Gradient.nodVal),
    doubleIn(tBad),
    doubleIn(tGood)
  )(Gradient.apply)

  //user input
  private def behavior: EIn[Unit] = {
    def remove (o: OGrad) = o fold (Formats.removeGradient, IO.ioUnit)
    def set (v: VGrad) = v fold (_ ⇒ IO.ioUnit, Formats.addGradient)
    val vOut: Out[VGrad] = Swing enabled setB contramap (_.isSuccess)
    val gOut: Out[Gradient] = 
      (Swing.text(tNod) ∙ ((_: Gradient).nod.toString)) ⊹
      (Swing.text(tBad) ∙ (_.bad.toString)) ⊹
      (Swing.text(tGood) ∙ (_.good.toString))

    val oOut: Out[OGrad] =
      (Swing.enabled(removeB) ∙ ((_: OGrad).nonEmpty)) ⊹
      (_ fold (gOut apply _, IO.ioUnit))

    (gradS to oOut on Swing.clicks(removeB) mapIO remove) ⊹ 
    (in to vOut on Swing.clicks(setB) mapIO set)
  }
}

object GradientPanel {
   
  type VGrad = ValRes[Gradient]
  type OGrad = Option[Gradient]

  def create: IO[Component] = createLkp(Utilities.actionsGlobalContext)

  private[actions] def createLkp (l: Lookup) : IO[GradientPanel] = for {
    lrw ← LookupResultWrapper[ColumnInfo](l)
    p   ← IO(new GradientPanel(lrw))
    _   ← p.behavior.go
  } yield p
}

// vim: set ts=2 sw=2 et:
