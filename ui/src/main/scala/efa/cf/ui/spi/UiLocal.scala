package efa.cf.ui.spi

trait UiLocal {
  def bad: String
  def booleanFormats: String
  def background: String
  def bluePrints: String
  def colors: String
  def delete: String
  def doubleFormats: String
  def doubleTerm: String
  def edit: String
  def editFormattingsAction: String
  def foreground: String
  def formatString: String
  def good: String
  def gradients: String
  def nod: String
  def remove: String
  def set: String
  def stringFormats: String
}

object UiLocal extends UiLocal {
  def bad = "Bad"
  def booleanFormats = "Boolean Formats"
  def background = "Background"
  def bluePrints = "Formats"
  def colors = "Colors"
  def delete = "Delete"
  def doubleFormats = "Numeric Formats"
  def doubleTerm = "Condition"
  def edit = "Edit"
  def editFormattingsAction = "Edit Conditional Formattings"
  def foreground = "Text"
  def formatString = "Format"
  def good = "Good"
  def gradients = "Gradients"
  def nod = "NoD"
  def remove = "Remove"
  def set = "Set"
  def stringFormats = "String Formats"
}

// vim: set ts=2 sw=2 et:
