package efa.cf.format

import org.scalacheck._, Prop._, Arbitrary.arbitrary
import scalaz._, Scalaz._, scalacheck.ScalaCheckBinding._

object FullFormatTest extends Properties("FullFormat") {
   type FF = FullFormat[FormatProps]

   val gen = ^^^(
     Gen.identifier,
     arbitrary[FormatProps],
     Gen.listOf(Gen.identifier) map (_.toSet),
     arbitrary[Boolean]
   )(FullFormat.apply)

   property("invalidName_new") = forAll(gen filter (_.isNew)){f ⇒ 
     f.invalidNames ≟ f.names
   }

   property("invalidName_notNew") = forAll(gen filter (! _.isNew)){f ⇒ 
     f.invalidNames ≟ (f.names - f.format.name)
   }
}

// vim: set ts=2 sw=2 et:
