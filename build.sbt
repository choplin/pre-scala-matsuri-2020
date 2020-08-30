ThisBuild / scalaVersion := "2.13.3"

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .in(file("scala"))
  .settings(
    version := "0.1-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.endpoints4s" %%% "algebra" % "1.1.0",
      "org.endpoints4s" %%% "json-schema-generic" % "1.1.0"
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % "0.21.0",
      "org.http4s" %% "http4s-blaze-server" % "0.21.0",
      "org.http4s" %% "http4s-circe" % "0.21.0",
      "org.endpoints4s" %% "http4s-server" % "2.0.0",
      "org.typelevel" %% "cats-core" % "2.1.1",
      "org.typelevel" %% "cats-effect" % "2.1.1",
      "org.scala-js" %% "scalajs-stubs" % "1.0.0" % "provided"
    )
  )
  .jsSettings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    libraryDependencies ++= Seq(
      "org.endpoints4s" %%% "xhr-client" % "1.1.0"
    )
  )
