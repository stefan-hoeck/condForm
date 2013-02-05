package efa.cf.ui

//import efa.core.ValSt
//import efa.cf.format._
//import efa.nb.node.{NbNodeFunctions, NodeOut, NbChildrenFunctions}
//import scalaz._, Scalaz._, effect._
//
//object FormatPropNode extends NbNodeFunctions with NbChildrenFunctions {
//  type FFF = FullFormat[FormatProps]
//
//  lazy val l = Lens.self[AllFormats]
//
//  lazy val rootOut: NodeOut[AllFormats,ValSt[AllFormats]] =
//    (children(uniqueIdF(fpOut)) ∙ ((_: AllFormats).bluePrintsFF)) ⊹
//    clearNt ⊹ 
//    (addNtDialogEs(l.addBluePrint) ∙ (_.fpBluePrint)) ⊹
//    nameA(loc.bluePrints) ⊹
//    contextRootsA(List("ContextActions/BluePrintsNode")) ⊹
//    iconBaseA("efa/cf/ui/format.png")
//
//  lazy val fpOut: NodeOut[FFF,ValSt[AllFormats]] =
//    destroyEs(l.delBluePrint) ⊹ 
//    name(_.format.name) ⊹
//    contextRootsA(List("ContextActions/SingleFormatNode")) ⊹
//    iconBaseA("efa/cf/ui/format.png") ⊹
//    (editDialog[FFF,FormatProps] withIn (l.updateBluePrint(_,_).success)) ⊹
//    ColorEditor.colorA(_.format.background)
//}

// vim: set ts=2 sw=2 et:
