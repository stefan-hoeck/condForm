package efa.cf.format

import efa.core._, Efa._
import efa.io._, valLogIO._
import efa.react.{Var, Signal}
import java.util.prefs.Preferences
import org.openide.util.NbPreferences
import scala.xml.{XML, Node}
import scalaz._, Scalaz._, effect.IO

object Formats {

  implicit lazy val StringBaseToXml =
    BaseFormat.baseFormatToXml[StringFormat](stringFormat)

  implicit lazy val BooleanBaseToXml =
    BaseFormat.baseFormatToXml[BooleanFormat](booleanFormat)

  implicit lazy val DoubleBaseToXml =
    BaseFormat.baseFormatToXml[DoubleFormat](doubleFormat)

  lazy val logger = LoggerIO.consoleLogger

  private[format] lazy val version = "2.0.0-SNAPSHOT"

  private[this] lazy val formatsVar: IOCached[Var[AllFormats]] =
    IOCached(loadAll() >>= (Signal newVar _))

  def formats: IO[Signal[AllFormats]] = formatsVar.get

  def now: IO[AllFormats] = formats >>= (_.now)

  def set (af: AllFormats): IO[Unit] = formatsVar.get >>= (_ put af)

  private[format] def loadAll (
    pref: ValLogIO[Preferences] = prefs
  ): IO[AllFormats] = logger logValZ (
    ^^^(load[FormatProps](formatProps, pref),
      load[BooleanBase](booleanBase, pref),
      load[DoubleBase](doubleBase, pref),
      load[StringBase](stringBase, pref))(AllFormats.apply)
  )

  private[format] def load[A:ToXml:Formatted](
    label: String, 
    pref: ValLogIO[Preferences] = prefs
  ): ValLogIO[Map[String, A]] = for {
      _     ← info("Loading formattings for %s" + label)
      ps    ← pref
      p     ← liftDis (fromPrefs[A](label, ps).disjunction)
    } yield p

  private[format] def store(
    pref: ValLogIO[Preferences] = prefs
  ): IO[Unit] = now >>= (storeAll(_, pref))

  private[format] def storeAll(
    af: AllFormats,
    pref: ValLogIO[Preferences] = prefs
  ): IO[Unit] = for {
    _  ← storeMap(formatProps, pref, af.bluePrintsM)
    _  ← storeMap(booleanBase, pref, af.boolsM)
    _  ← storeMap(doubleBase, pref, af.doublesM)
    _  ← storeMap(stringBase, pref, af.stringsM)
  } yield ()

  private[format] def storeMap[A:ToXml](
    label: String, pref: ValLogIO[Preferences], m: Map[String,A]
  ): IO[Unit] = logger logValZ (for {
      _  ← info("Persisting formattings for " + label)
      ps ← pref
      _  ← point(toPrefs[A](label, m, ps))
    } yield ())

  private[format] lazy val prefs: ValLogIO[Preferences] =
    point(NbPreferences.forModule(BaseFormat.getClass))

  private[this] def toPrefs[A:ToXml] (
    label: String,
    m: Map[String,A],
    p: Preferences
  ) {
    p.putInt(countLbl(label), m.size)

    (m.toList map (_._2) zipWithIndex) foreach {case (a,i) ⇒ 
      p.put(itemLbl(label, i), "value".xml(a).toString)
    }
  }

  private[this] def fromPrefs[A:ToXml:HasFormatProps] (
    label: String, p: Preferences
  ): ValRes[Map[String,A]] = {
    def load (i: Int): ValRes[A] =
      ToXml[A] fromXml (XML loadString p.get(itemLbl(label, i), ""))

    def loadPair (i: Int): ValRes[(String,A)] =
      load(i) map (a ⇒ HasFormatProps[A].id(a) → a)

    val i = p.getInt(countLbl(label), 0)

    (0 until i).toList traverse loadPair map (_.toMap)
  }

  private[this] def countLbl(l: String) = l + version + "Count"

  private[this] def itemLbl(l: String, i: Int) =
    l + version + "Item" + i.toString
}

// vim: set ts=2 sw=2 et:
