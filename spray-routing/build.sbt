import AssemblyKeys._

name := "human-resources-oi"

organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaV = "2.3.2"
  val sprayV = "1.3.1"
  Seq(
    "io.spray"            %%   "spray-can"     % sprayV,
    "io.spray"            %%   "spray-routing" % sprayV,
    "io.spray"            %%   "spray-testkit" % sprayV % "test",
    "io.spray"            %%  "spray-json" % "1.2.6",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV % "test",
    "com.github.nscala-time" %% "nscala-time" % "1.2.0",
    "org.specs2"          %%  "specs2"   % "2.3.13" % "test",
    "junit" % "junit" % "4.11" % "test"
  )
}

Revolver.settings

assemblySettings
