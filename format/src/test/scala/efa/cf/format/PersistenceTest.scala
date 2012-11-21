package efa.cf.format

import org.scalacheck._, Prop._
import scalaz._, Scalaz._, effect.IO

object PersistenceTest extends Properties("Persistence") {

  property("formatProps") = forAll {af: AllFormats ⇒
    val res = for {
      _    ← Formats storeAll af
      all  ← Formats.loadAll()
    } yield (all ≟ af) :| "Exp: %s, but was :%s".format(af, all)

    res.unsafePerformIO
  }

}

// vim: set ts=2 sw=2 et:
