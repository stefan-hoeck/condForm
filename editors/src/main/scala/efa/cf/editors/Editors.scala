package org.efa.cformat.editors
//
//import org.efa.cformat.formatting.DoubleRegister.DoubleBase
//import org.efa.cformat.formatting.StringRegister.StringBase
//import org.efa.cformat.formatting.BooleanRegister.BooleanBase
//import org.efa.cformat.formatting.{DoubleRegister, FormatProps, StringRegister,
//                                   BooleanRegister}
//import org.efa.mvc.react.nodes.PropertyController
//import org.efa.util.prop.Accessor
//
//trait Editors {
//  def registerBool(acc: Accessor[_, Boolean]): Unit = {
//    if (BooleanRegister.formatMap.get(acc.name).isEmpty)
//    BooleanRegister register BooleanBase(acc.name, 
//                                         FormatProps.Default name_= acc.locName, Nil)
//  }
//  def registerDouble(acc: Accessor[_, Double], formatString: String = "%.f")
//  : Unit = {
//    if (DoubleRegister.formatMap.get(acc.name).isEmpty)
//    DoubleRegister register DoubleBase(acc.name, 
//                                       FormatProps.Default name_= acc.locName, 
//                                       Nil, formatString)
//  }
//  def registerString(acc: Accessor[_, String])
//  : Unit = {
//    if (StringRegister.formatMap.get(acc.name).isEmpty)
//    StringRegister register StringBase(acc.name, 
//                                       FormatProps.Default name_= acc.locName, Nil)
//  }
//  
//  import PropertyController._
//  def formattedBool[D](acc: Accessor[D, Boolean]): Factory[D, Boolean] =
//    pc => new pc.NodePropController[Boolean](
//      acc.name, acc, None, Some(b => new BooleanEditor(acc.name, b, "")), None
//    )
//  def formattedDouble[D](acc: Accessor[D, Double]): Factory[D, Double] =
//    pc => new pc.NodePropController[Double](
//      acc.name, acc, None, Some(b => new DoubleEditor(acc.name, b, "")),None
//    )
//  def formattedString[D](acc: Accessor[D, String]): Factory[D, String] =
//    pc => new pc.NodePropController[String](
//      acc.name, acc, None, Some(b => new StringEditor(acc.name, b, "")), None)
//}
//
//object Editors extends Editors

// vim: set ts=2 sw=2 et:
