package efa.cf.format

import efa.core.{UniqueId, Default}
import scalaz._, Scalaz._

case class FullBase[A](
  baseFormat: BaseFormat[A], templates: List[FormatProps]
) {
  def locName = baseFormat.props.name

  def bluePrints (implicit D:Default[A], F:Formatted[A])
  : List[FullFormat[A]] = {
    def toA(f: FormatProps): A = F.formatPropsL set (D.default, f)
    
    baseFormat fullFormats (templates map toA, true)
  }
}

object FullBase {
  implicit def FullBaseEqual[A:Equal]: Equal[FullBase[A]] =
    Equal.equalBy (f ⇒ (f.baseFormat, f.templates))

  def baseFormat[A]: FullBase[A] @> BaseFormat[A] =
    Lens.lensu((a,b) ⇒ a copy (baseFormat = b), _.baseFormat)

  def templates[A]: FullBase[A] @> List[FormatProps] =
    Lens.lensu((a,b) ⇒ a copy (templates = b), _.templates)
  
  implicit class Lenses[A,B](val l: Lens[A,FullBase[B]]) extends AnyVal {
    def baseFormat = l andThen FullBase.baseFormat
    def templates = l andThen FullBase.templates
  }

  implicit def FullBaseUniqueId[A] =
    new UniqueId[FullBase[A],String] with Show[FullBase[A]]{
      def id (a: FullBase[A]) = a.baseFormat.id
      override def shows (a: FullBase[A]) = a.locName
    }
  
}

// vim: set ts=2 sw=2 et:
