ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "OpenQuiz",
    scalaJSUseMainModuleInitializer := true,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
  ).enablePlugins(ScalaJSPlugin)

// https://mvnrepository.com/artifact/org.scala-js/scalajs-dom
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.1.0"
//
//// https://mvnrepository.com/artifact/com.lihaoyi/requests
//libraryDependencies += "com.lihaoyi" %%% "requests" % "0.7.0"

// https://mvnrepository.com/artifact/com.lihaoyi/upickle
libraryDependencies += "com.lihaoyi" %%% "upickle" % "1.4.4"