package efa.cf.ui.spi

trait UiLocal {
  def background: String
  def bluePrints: String
  def doubleTerm: String
  def foreground: String
  def formatString: String
}

object UiLocal extends UiLocal {
  def background = "Background"
  def bluePrints = "Formats"
  def doubleTerm = "Condition"
  def foreground = "Text"
  def formatString = "Format"
}

// vim: set ts=2 sw=2 et:
