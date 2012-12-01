package efa.cf.format

import efa.core.UniqueId
import scalaz.Show

case class FullFormat[A] (
  baseId: String,
  format: A,
  names: Set[String],
  isNew: Boolean
) {
  def invalidNames (implicit F:StringId[A]): Set[String] =
    if (isNew) names else names - F.id(format)
}

object FullFormat {
  implicit def FullFormatFormatted[A](implicit F:HasFormatProps[A]) =
    propsName[FullFormat[A]](F formatProps _.format)(F id _.format)

  implicit lazy val FFGradientColorsShow = 
  new Show[FullFormat[GradientColors]]
  with UniqueId[FullFormat[GradientColors],String] {
    override def shows (f: FullFormat[GradientColors]) = f.format.name
    def id (f: FullFormat[GradientColors]) = f.format.name
  }
}

// vim: set ts=2 sw=2 et:
