package efa.cf.format.spi

import efa.cf.format.logic.Term
import efa.core.{DisRes, ValRes, Localization}
import scalaz._, Scalaz._

trait FormatLocal {
  def base: String
  def bad: String
  def bluePrint: String
  def color: String
  def default: String
  def good: String
  def gradient: String
  def invalidDoubleTermFormat(s: String): String
  def invalidRegex: String
  def ok: String
  def transparent: String
  
  final def failDoubleTerm (s: String): ValRes[Term[Double]] = 
    invalidDoubleTermFormat(s).failureNel

  final def invalidRegexFail: DisRes[String] = invalidRegex.wrapNel.left

  lazy val colorL = new Localization("color", color)
}

object FormatLocal extends FormatLocal {
  def base = "Formats"
  def bad = "Bad"
  def bluePrint = "Blueprint"
  def color = "Color"
  def default = "Default"
  def good = "Good"
  def gradient = "Gradient"
  def invalidDoubleTermFormat(s: String) = "Unknown format for term: " + s

  def invalidRegex = "Unknown format for regular expression." ++
    "\nSee http://en.wikipedia.org/wiki/Regular_expression for further " ++
    "information."

  def ok = "OK"
  def transparent = "Default Transparent"
}

// vim: set ts=2 sw=2 et:
