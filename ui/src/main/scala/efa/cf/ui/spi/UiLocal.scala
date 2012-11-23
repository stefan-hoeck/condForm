package efa.cf.ui.spi

trait UiLocal {
  def booleanFormats: String
  def background: String
  def bluePrints: String
  def doubleFormats: String
  def doubleTerm: String
  def foreground: String
  def formatString: String
  def stringFormats: String
}

object UiLocal extends UiLocal {
  def booleanFormats = "Boolean Formats"
  def background = "Background"
  def bluePrints = "Formats"
  def doubleFormats = "Numeric Formats"
  def doubleTerm = "Condition"
  def foreground = "Text"
  def formatString = "Format"
  def stringFormats = "String Formats"
}

// vim: set ts=2 sw=2 et:
