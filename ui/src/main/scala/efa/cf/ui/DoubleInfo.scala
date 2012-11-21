//package org.efa.cformat.ui
//
//import org.efa.cformat.formatting.{DoubleRegister, FormatProps}
//import org.efa.cformat.formatting.DoubleRegister.{DoubleBase, DoubleFormat}
//import org.efa.ui._
//import reactive.Signal
//
//class DoubleInfo(val baseFormat: Signal[DoubleRegister.DoubleBase])
//extends BaseFormatInfo[DoubleRegister.type] {
//  def basePanel (base: DoubleBase) = new DoubleBasePanel(base)
//  def childPanel (format: DoubleFormat) = new DoubleFormatPanel(format)
//  val propsToSingle: FormatProps => DoubleFormat = fp => 
//  DoubleRegister.DefaultFormat formatString_= 
//    baseFormat.now.formatString props_= fp
//  val baseSetter: List[DoubleBase] => AllFormats = AllFormatsSignal.now doubles_= _
//  val baseGetter: AllFormats => List[DoubleBase] = _.doubles
//  val childSetter: DoubleBase => List[DoubleFormat] => DoubleBase =
//    b => bs => b formats_= bs
//}
//
//import DoubleRegister._
//class DoubleBasePanel(bb: DoubleBase)
//extends WithPropsPanel[DoubleBase](bb, _.props, _ props_= _) {
//  import DoubleRegister._
//  val cString = new CTextField(formatStringDbMut)
//  (elems above (formatStringDbMut beside cString)).add()
//  preferredSize = (450, preferredSize.height)
//}
//
//class DoubleFormatPanel(bb: DoubleFormat) 
//extends WithPropsPanel[DoubleFormat](bb, _.props, _ props_= _) {
//  import DoubleRegister._
//  val cString = new CTextField(formatStringDfMut)
//  val cTerm = new CTextField(termMut convert TermConverter)
//  (elems above (termMut beside cTerm) above (formatStringDfMut beside cString)).add()
//  preferredSize = (450, preferredSize.height)
//}

// vim: set ts=2 sw=2 et:
