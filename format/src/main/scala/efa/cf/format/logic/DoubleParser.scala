package efa.cf.format.logic

import efa.core._, Efa._
import java.util.Locale
import scala.util.parsing.combinator._
import scalaz._, Scalaz._

object DoubleParser extends JavaTokenParsers {
  private[logic] val binSymbStr = "%s|%s|%s|%s" format 
  ("\\Q&&\\E", "\\Q||\\E", "[Aa][Nn][Dd]", "[Oo][Rr]")
  private[logic] val BinSymb = binSymbStr.r
  private[logic] val unSymbStr = "<=?|==|>=?"
  private[logic] val UnarySymb = unSymbStr.r
  private[logic] val nanSymbStr = "[Nn][Aa][Nn]"
  private[logic] val NanSymb = nanSymbStr.r
  
  def apply(input: String): ValRes[Term[Double]] =
    try {
      parseAll(expr, input) match {
        case Success(t, _) ⇒ t.success
        case x             ⇒ efa.cf.format.loc failDoubleTerm x.toString
      }
    } catch {case x: Throwable ⇒ efa.cf.format.loc failDoubleTerm x.toString}

  private[logic] def expr: Parser[Term[Double]] = 
    trueTerm | nanTerm | term~rep(BinSymb~term) ^^ {
    p =>
    def combine(t1: Term[Double], rest: ~[String, Term[Double]]): Term[Double] =
      rest._1.toUpperCase(Locale.ENGLISH) match {
        case "&&" => And(t1, rest._2)
        case "AND" => And(t1, rest._2)
        case "||" => Or(t1, rest._2)
        case "OR" => Or(t1, rest._2)
      }
    (p._1 /: p._2)(combine(_, _))
  }
  private[logic] def term: Parser[Term[Double]] = single | "("~>expr<~")"
  private[logic] def single: Parser[Term[Double]] = 
    UnarySymb~floatingPointNumber ^^ (a => Unary(Comp.fromSymbol(a._1), 
                                                       a._2.toDouble))
  private[logic] def trueTerm: Parser[Term[Double]] = "True" ^^ (s => True())
  private[logic] def nanTerm: Parser[Term[Double]] = 
    NanSymb ^^ (_ => Nan)
}

// vim: set ts=2 sw=2 et:
