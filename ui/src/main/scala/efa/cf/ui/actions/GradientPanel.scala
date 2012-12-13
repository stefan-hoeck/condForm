package efa.cf.ui.actions

import efa.cf.format.{Gradient, ColumnInfo, Formats, AllFormats, loc ⇒ fLoc}
import efa.cf.ui.{loc ⇒ uLoc}
import efa.core.ValRes
import efa.nb.{LookupResultWrapper, VSIn}
import efa.nb.dialog.DialogPanel
import efa.react._, swing.Swing
import org.openide.util.{Utilities, Lookup}
import scala.swing.{Button, TextField, Component, ComboBox}
import scalaz._, Scalaz._, effect.IO

class GradientPanel (
  lrw: LookupResultWrapper[ColumnInfo],
  gradNames: Seq[String]
)
   extends DialogPanel {

  import GradientPanel._
  
  val tLower = new TextField(0.0D.toString, 5)
  val tUpper = new TextField(0.0D.toString, 5)
  val tNod = new TextField(1.toString, 3)
  val cGradient = new ComboBox[String](gradNames){peer.setEditable(true)}
  val setB = new Button(uLoc.set)
  val removeB = new Button(uLoc.remove)
   
  (fLoc.lower beside tLower beside fLoc.upper beside tUpper beside
   uLoc.nod beside tNod beside fLoc.gradient beside cGradient beside
   setB beside removeB).add()

  //name of selected column
  private[this] lazy val nameS: SIn[Option[String]] =
    eTrans in lrw map (_.headOption map (_.name)) hold none

  //gradient for selected column
  private[this] lazy val gradS: SIn[OGrad] =
    ^(nameS, Formats.formats)(_ >>= _.gradientsM.get)

  //validated user input
  private[this] lazy val in: VSIn[Gradient] = Apply[VSIn].apply5(
    nameS map (_ toSuccess "".wrapNel),
    intIn(tNod, Gradient.nodVal),
    doubleIn(tLower),
    doubleIn(tUpper),
    comboBox(cGradient)
  )(Gradient.apply)

  //user input
  private def behavior: EIn[Unit] = {
    def remove (o: OGrad) = o map Formats.removeGradient orZero

    def set (v: VGrad) = v fold (_ ⇒ IO.ioUnit, Formats.addGradient)
    val vOut: Out[VGrad] = Swing enabled setB contramap (_.isSuccess)

    val gOut: Out[Gradient] = 
      (Swing.text(tNod) ∙ ((_: Gradient).nod.toString)) ⊹
      (Swing.text(tLower) ∙ (_.lower.toString)) ⊹
      (Swing.text(tUpper) ∙ (_.upper.toString)) ⊹
      (Swing.item(cGradient) ∙ (_.colors))

    val colorsOut: Out[AllFormats] =
      Swing model cGradient contramap (_.colorNames)

    val oOut: Out[OGrad] =
      (Swing.enabled(removeB) ∙ ((_: OGrad).nonEmpty)) ⊹
      (_ map (gOut apply _) orZero)

    (Formats.formats.to(colorsOut).events map (_ ⇒ ())) ⊹
    (gradS to oOut on Swing.clicks(removeB) mapIO remove) ⊹ 
    (in to vOut on Swing.clicks(setB) mapIO set)
  }

  private var connectors: Connectors = DList()

  private[ui] def cleanUp = connectors.toList foldMap (_.disconnect)
}

object GradientPanel {
   
  type VGrad = ValRes[Gradient]
  type OGrad = Option[Gradient]

  def create: IO[Component] = createLkp(Utilities.actionsGlobalContext)

  private[actions] def createLkp (l: Lookup) : IO[GradientPanel] = for {
    lrw ← LookupResultWrapper[ColumnInfo](l)
    af  ← Formats.now
    p   ← IO(new GradientPanel(lrw, af.gradientColors map (_.name)))
    cs  ← p.behavior.go
    _   = p.connectors = cs._1
  } yield p
}

// vim: set ts=2 sw=2 et:
