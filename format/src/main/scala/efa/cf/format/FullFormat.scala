package efa.cf.format

import efa.core.UniqueId

case class FullFormat[A] (
  baseName: String,
  format: A,
  names: Set[String],
  isNew: Boolean
) {
  def invalidNames (implicit F:Formatted[A]): Set[String] =
    if (isNew) names else names - F.locName(format)
}

object FullFormat {
  implicit def FullFormatFormatted[A](implicit F:Formatted[A]) =
    fromProps[FullFormat[A]](F formatProps _.format)
}

// vim: set ts=2 sw=2 et:
