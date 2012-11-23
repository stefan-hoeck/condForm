package efa.cf.ui

import efa.core.{UniqueId, ValSt, Default}
import efa.cf.format._
import efa.cf.format.{AllFormats ⇒ AF, BaseFormat ⇒ BF,
                      FullBase ⇒ FB, FullFormat ⇒ FF}
import efa.nb.dialog.DialogEditable
import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
import scalaz._, Scalaz._

object BaseFormatNode extends NbNodeFunctions with NbChildrenFunctions {
  lazy val allOut: NodeOut[AF,ValSt[AF]] = children(
    singleF(FormatPropNode.rootOut),
    singleF(booleanOut),
    singleF(doubleOut)
  )

  lazy val booleanOut: NodeOut[AF,ValSt[AF]] =
    baseFormatOut[BooleanFormat](loc.booleanFormats, AF.boolsM)

  lazy val doubleOut: NodeOut[AF,ValSt[AF]] =
    baseFormatOut[DoubleFormat](loc.doubleFormats, AF.doublesM)

  def baseFormatOut[A:Formatted:FFEditable:Equal:Default](
    locName: String, l: AF @> Map[String,BF[A]]
  ): NodeOut[AF,ValSt[AF]] = {
    def adjFs (ff: FF[A], f: List[A] ⇒ List[A]): State[AF,Unit] =
      (l at ff.baseId).formats mods_ f

    def delete(f: FF[A]) = adjFs(f, _ filterNot (f.format ≟))

    def update (f: FF[A], a: A) = delete(f) >> adjFs(f, a :: _)

    val out: NodeOut[FF[A],ValSt[AF]] = 
      destroyEs(delete) ⊹
      (editDialog withIn update map (_.success)) ⊹
      name(Formatted[A] locName _.format) ⊹
      contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
      iconBaseA("efa/cf/ui/single.png")

    val fbOut: NodeOut[FB[A],ValSt[AF]] =
      (children(uniqueIdF(out)) ∙ ((_: FB[A]).baseFormat.fullList)) ⊹
      name[FB[A]](_.locName) ⊹ 
      contextRootsA(List("ContextActions/BaseFormatNode")) ⊹
      iconBaseA("efa/cf/ui/base.png")

    (children(uniqueIdF(fbOut)) ∙ ((_: AF) fullBases l)) ⊹
    nameA(locName) ⊹
    iconBaseA("efa/cf/ui/format.png")
  }
}

//  override def templates = AllFormatsSignal.now.bluePrints map (bp => info.propsToSingle(bp))
//      }
//    }

// vim: set ts=2 sw=2 et:
