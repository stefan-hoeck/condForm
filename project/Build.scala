import sbt._
import Keys._

object BuildSettings {
  val sv = "2.10.0"
  val buildOrganization = "efa.cf"
  val buildVersion = "2.1.0-SNAPSHOT"
  val buildScalaVersion = sv
  val netbeansRepo = "Netbeans" at "http://bits.netbeans.org/maven2/"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    resolvers += netbeansRepo,
    scalacOptions ++= Seq ("-deprecation", "-feature",
      "-language:postfixOps", "-language:higherKinds"),
    publishArtifact in (Compile, packageDoc) := false,
    publishArtifact in (Compile, packageSrc) := false
  )
} 

object Dependencies {
  import BuildSettings.sv

  val utilV = "0.2.1-SNAPSHOT"
  val reactV = "0.2.1-SNAPSHOT"
  val efaNbV = "0.2.1-SNAPSHOT"
  val nbV = "RELEASE71"
  val scalazV = "7.0.0-M8"

  val nb = "org.netbeans.api"
  val util = "efa"
  val react = "efa.react"
  val scalaz = "org.scalaz"

  val efaCore = util %% "efa-core" % utilV changing

  val efaIo = util %% "efa-io" % utilV changing

  val efaNb = "efa.nb" %% "efa-nb" % efaNbV changing

  val efaReact = react %% "react-core" % reactV changing

  val efaReactSwing = react %% "react-swing" % reactV changing

  val scalaSwing = "org.scala-lang" % "scala-swing" % sv

  val nbUtil = nb % "org-openide-util" % nbV
  val nbLookup = nb % "org-openide-util-lookup" % nbV
  val nbExplorer = nb % "org-openide-explorer" % nbV
  val nbWindows = nb % "org-openide-windows" % nbV
  val nbNodes = nb % "org-openide-nodes" % nbV
  val nbFilesystems = nb % "org-openide-filesystems" % nbV
  val nbLoaders = nb % "org-openide-loaders" % nbV
  val nbModules = nb % "org-openide-modules" % nbV
  val nbAwt = nb % "org-openide-awt" % nbV
  val nbSettings = nb % "org-netbeans-modules-settings" % nbV
  val nbActions = nb % "org-openide-actions" % nbV
  val nbDialogs = nb % "org-openide-dialogs" % nbV
  val nbOutline = nb % "org-netbeans-swing-outline" % nbV
  val nbOptions = nb % "org-netbeans-modules-options-api" % nbV
  val nbAutoupdateUi = nb % "org-netbeans-modules-autoupdate-ui" % nbV
  val nbAutoupdateServices = nb % "org-netbeans-modules-autoupdate-services" % nbV
  val nbModulesOptions = nb % "org-netbeans-modules-options-api" % nbV

  val shapeless = "com.chuusai" %% "shapeless" % "1.2.3"
  val scalaz_core = scalaz %% "scalaz-core" % scalazV
  val scalaz_effect = scalaz %% "scalaz-effect" % scalazV
  val scalaz_scalacheck = scalaz %% "scalaz-scalacheck-binding" % scalazV

  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.10.0"

  val coolness = Seq(scalaz_core, scalaz_effect, scalaz_scalacheck,
                     shapeless, scalacheck)
}

object UtilBuild extends Build {
  import Dependencies._
  import BuildSettings._

  def addDeps (ds: ModuleID*) =
    BuildSettings.buildSettings ++ 
    Seq(libraryDependencies ++= (ds ++ coolness))

  lazy val cf = Project (
    "cf",
    file("."),
    settings = buildSettings
  ) aggregate (editors, format, ui)
  
  lazy val editors = Project (
    "cf-editors",
    file("editors"),
    settings = addDeps(efaCore, efaIo, efaNb)
  ) dependsOn(format)
  
  lazy val format = Project (
    "cf-format",
    file("format"),
    settings =
    addDeps(efaCore, efaIo, efaReact, nbModules) :+ (
      parallelExecution in Test := false
    )
  )
  
  lazy val ui = Project (
    "cf-ui",
    file("ui"),
    settings = addDeps(efaCore, efaIo, efaNb, efaReact, nbOptions)
  ) dependsOn (format, editors)
}

// vim: set ts=2 sw=2 et nowrap:
