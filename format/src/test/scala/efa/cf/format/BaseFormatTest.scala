package efa.cf.format

import org.scalacheck._, Prop._
import scalaz._, Scalaz._

object BaseFormatTest extends Properties("BaseFormat") {
  type BBool = BaseFormat[BooleanFormat]

  property("name") = forAll{b: BBool ⇒ b.fullList ∀ (_.baseId ≟ b.id) }

  property("isNew") = forAll{b: BBool ⇒ b.fullList ∀ (f ⇒ !(f.isNew)) }

  property("invalidNames") = forAll{b: BBool ⇒
    def valid (f: FullFormat[BooleanFormat]) = {
      val invalid = f.invalidNames

      ! invalid(f.format.props.name)
    }

    b.fullList ∀ valid
  }
}

// vim: set ts=2 sw=2 et:
