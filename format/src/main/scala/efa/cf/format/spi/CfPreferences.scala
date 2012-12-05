package efa.cf.format.spi

import efa.io.LoggerIO
import scalaz.effect.IO

trait CfPreferences {
  def cfLogger: IO[LoggerIO]
}

object CfPreferences extends CfPreferences {
  def cfLogger = IO(LoggerIO.consoleLogger)
}

// vim: set ts=2 sw=2 et:
