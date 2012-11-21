package efa.cf.format

import scalaz._, Scalaz._

case class FullBase[A](
  baseFormat: BaseFormat[A], templates: List[FormatProps]
) {
  def locName = baseFormat.props.name
}

object FullBase {
  implicit def FullBaseEqual[A:Equal]: Equal[FullBase[A]] =
    Equal.equalBy (f ⇒ (f.baseFormat, f.templates))

  def baseFormat[A]: FullBase[A] @> BaseFormat[A] =
    Lens.lensu((a,b) ⇒ a copy (baseFormat = b), _.baseFormat)

  def templates[A]: FullBase[A] @> List[FormatProps] =
    Lens.lensu((a,b) ⇒ a copy (templates = b), _.templates)
  
  implicit def FullBaseLenses[A,B](l: Lens[A,FullBase[B]]) = new {
    lazy val baseFormat = l andThen FullBase.baseFormat
    lazy val templates = l andThen FullBase.templates
  }
  
}

// vim: set ts=2 sw=2 et:
