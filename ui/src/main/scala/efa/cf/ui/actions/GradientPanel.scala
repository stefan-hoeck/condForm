package efa.cf.ui.actions

import efa.cf.format.{Gradient, ColumnInfo, Formats}
import efa.cf.ui.{loc ⇒ uLoc}
import efa.core.ValRes
import efa.nb.{LookupResultWrapper, VSIn}
import efa.nb.dialog.DialogPanel
import efa.react._, swing.Swing
import org.openide.util.Utilities
import scala.swing.{Button, TextField, Component}
import scalaz._, Scalaz._, effect.IO

class GradientPanel (lrw: LookupResultWrapper[ColumnInfo])
   extends DialogPanel {

  import GradientPanel._
  
  val tBad = new TextField(5)
  val tGood = new TextField(5)
  val tNod = new TextField(3)
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
    val oOut: Out[OGrad] =
      (Swing.enabled(removeB) ∙ ((_: OGrad).nonEmpty)) ⊹
      (Swing.text(tNod) ∙ (_ fold (_.nod.toString, ""))) ⊹
      (Swing.text(tBad) ∙ (_ fold (_.bad.toString, ""))) ⊹
      (Swing.text(tGood) ∙ (_ fold (_.good.toString, "")))


    (gradS to oOut on Swing.clicks(removeB) mapIO remove) ⊹ 
    (in to vOut on Swing.clicks(setB) mapIO set)
  }
}

object GradientPanel {
   
  type VGrad = ValRes[Gradient]
  type OGrad = Option[Gradient]

  def create: IO[Component] = for {
    lrw ← LookupResultWrapper[ColumnInfo](Utilities.actionsGlobalContext)
    p   ← IO(new GradientPanel(lrw))
    _   ← p.behavior.go
  } yield p
}

// vim: set ts=2 sw=2 et:
