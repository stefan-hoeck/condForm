package efa.cf.format.spi

import efa.cf.format.logic.Term
import efa.core.{DisRes, ValRes}
import scalaz._, Scalaz._

trait FormatLocal {
  def base: String
  def bad: String
  def bluePrint: String
  def default: String
  def good: String
  def invalidDoubleTermFormat(s: String): String
  def invalidRegex: String
  def ok: String
  
  final def failDoubleTerm (s: String): ValRes[Term[Double]] = 
    invalidDoubleTermFormat(s).failureNel

  final def invalidRegexFail: DisRes[String] = invalidRegex.wrapNel.left
}

object FormatLocal extends FormatLocal {
  def base = "Formats"
  def bad = "Bad"
  def bluePrint = "Blueprint"
  def default = "Default"
  def good = "Good"
  def invalidDoubleTermFormat(s: String) = "Unknown format for term: " + s

  def invalidRegex = "Unknown format for regular expression." ++
    "\nSee http://en.wikipedia.org/wiki/Regular_expression for further " ++
    "information."

  def ok = "OK"
}

// vim: set ts=2 sw=2 et:
