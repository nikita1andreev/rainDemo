# rainDemo
To run you need to install javaFX, copy org.fxyz3d.samples to you project, and create build.sbt like that:
```
lazy val proto = (Project("proto", file("proto"))
  enablePlugins(AssemblyPlugin)
  settings(
  javaOptions in Test += "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:1044 -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false",
  inThisBuild(List(
    organization    := "com.example"
    , scalaVersion    := "2.13.4"
  )),
  name := "proto",
  resolvers ++= Seq("Typesafe" at "https://repo.typesafe.com/typesafe/maven-releases/"
    ,("jzy3d" at "http://maven.jzy3d.org/releases").withAllowInsecureProtocol(true)    
  ),
  libraryDependencies ++= Seq(
    "org.scalafx" %% "scalafx" % "15.0.1-R21"
,"org.fxyz3d" % "fxyz3d" % "0.5.2",
"org.fxyz3d" % "fxyz3d-client" % "0.5.2",
"org.fxyz3d" % "fxyz3d-importers" % "0.5.2",
"org.controlsfx" % "controlsfx" % "11.0.3",
"org.jfxtras" % "jfxtras-controls" % "15-r1",
"org.fxmisc.easybind" % "easybind" % "1.0.3",
"org.reactfx" % "reactfx" % "2.0-M5")
)))
```
