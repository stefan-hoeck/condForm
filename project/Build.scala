import sbt._
import Keys._

object BuildSettings {
  import Resolvers._

  val sv = "2.10.0"
  val buildOrganization = "efa.cf"
  val buildVersion = "2.0.0-SNAPSHOT"
  val buildScalaVersion = sv

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    resolvers ++= repos,
    scalacOptions ++= Seq ("-deprecation", "-feature",
      "-language:postfixOps", "-language:higherKinds"),
    publishArtifact in (Compile, packageDoc) := false,
    publishArtifact in (Compile, packageSrc) := false
  )
} 

object Resolvers {
 val netbeansRepo = "Netbeans" at "http://bits.netbeans.org/maven2/"
 val scalatoolsRepo = "Scala-Tools Maven2 Repository Releases" at
   "http://scala-tools.org/repo-releases"
 val sonatypeRepo = "releases" at
   "http://oss.sonatype.org/content/repositories/releases"
 val mavenLocal = "Local Maven Repository" at
   "file://"+Path.userHome+"/.m2/repository"

 val repos = Seq (netbeansRepo, scalatoolsRepo, sonatypeRepo, mavenLocal)
}

object Dependencies {
  import BuildSettings.sv

  val utilVersion = "0.1.0-SNAPSHOT"
  val reactVersion = "0.1.0-SNAPSHOT"
  val util = "efa"
  val react = "efa.react"

  val efaCore = util %% "core" % utilVersion changing

  val efaIo = util %% "io" % utilVersion changing

  val efaNb = util %% "nb" % utilVersion changing

  val efaReact = react %% "core" % reactVersion changing

  val efaReactSwing = react %% "swing" % reactVersion changing

  val nbV = "RELEASE71"

  val scalaSwing = "org.scala-lang" % "scala-swing" % sv

  val nbUtil = "org.netbeans.api" % "org-openide-util" % nbV
  val nbLookup = "org.netbeans.api" % "org-openide-util-lookup" % nbV
  val nbExplorer = "org.netbeans.api" % "org-openide-explorer" % nbV
  val nbWindows = "org.netbeans.api" % "org-openide-windows" % nbV
  val nbNodes = "org.netbeans.api" % "org-openide-nodes" % nbV
  val nbFilesystems = "org.netbeans.api" % "org-openide-filesystems" % nbV
  val nbLoaders = "org.netbeans.api" % "org-openide-loaders" % nbV
  val nbModules = "org.netbeans.api" % "org-openide-modules" % nbV
  val nbAwt = "org.netbeans.api" % "org-openide-awt" % nbV
  val nbSettings = "org.netbeans.api" % "org-netbeans-modules-settings" % nbV
  val nbActions = "org.netbeans.api" % "org-openide-actions" % nbV
  val nbDialogs = "org.netbeans.api" % "org-openide-dialogs" % nbV
  val nbOutline = "org.netbeans.api" % "org-netbeans-swing-outline" % nbV
  val nbOptions = "org.netbeans.api" % "org-netbeans-modules-options-api" % nbV
  val nbAutoupdateUi = "org.netbeans.api" % "org-netbeans-modules-autoupdate-ui" % nbV
  val nbAutoupdateServices = "org.netbeans.api" % "org-netbeans-modules-autoupdate-services" % nbV
  val nbModulesOptions = "org.netbeans.api" % "org-netbeans-modules-options-api" % nbV

  val scalaz_core = "org.scalaz" %% "scalaz-core" % "7.0.0-M7"
  val scalaz_effect = "org.scalaz" %% "scalaz-effect" % "7.0.0-M7"
  val scalaz_scalacheck =
    "org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.0-M7"
  val scalaz_scalacheckT = scalaz_scalacheck % "test"

  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.10.0"
  val scalacheckT = scalacheck % "test"
  val scalazCheckT = Seq(scalaz_core, scalaz_scalacheckT, scalacheckT)
  val scalazCheckET = scalazCheckT :+ scalaz_effect
}

object UtilBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  def addDeps (ds: Seq[ModuleID]) =
    BuildSettings.buildSettings ++ Seq (libraryDependencies ++= ds)

  lazy val cf = Project (
    "cf",
    file("."),
    settings = buildSettings
  ) aggregate (editors, format, ui)
  
  lazy val editors = Project (
    "editors",
    file("editors"),
    settings = addDeps (scalazCheckET ++ Seq(efaCore, efaIo, efaNb))
  ) dependsOn (format)
  
  lazy val format = Project (
    "format",
    file("format"),
    settings =
    addDeps (scalazCheckET ++ Seq(efaCore, efaIo, efaReact, nbModules)) :+ (
      parallelExecution in Test := false
    )
  )
  
  lazy val ui = Project (
    "ui",
    file("ui"),
    settings = addDeps (scalazCheckET ++ Seq(efaCore, efaIo, efaNb, efaReact, nbOptions))
  ) dependsOn (format, editors)
}

// vim: set ts=2 sw=2 et:
