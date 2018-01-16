import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

name := """infoshareapi"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, DebianPlugin, DockerPlugin,JavaServerAppPackaging,SystemdPlugin)

scalaVersion := "2.12.4"

scalacOptions += "-Ypartial-unification"

val PhantomVersion =  "2.18.0"
val PlayFrameWorkVersion = "2.6.11"
val catsVersion = "1.0.1"

maintainer := "Boniface Kabaso <boniface@kabaso.com>"
packageSummary in Linux := "Infoshare REST API"
packageDescription := "Infoshare For Clients REST API "

dockerCommands := Seq(
  Cmd("FROM", "anapsix/alpine-java:jdk8"),
  Cmd("MAINTAINER", maintainer.value),
  Cmd("WORKDIR", "/opt/docker"),
  Cmd("ADD", "/opt /opt"),
  ExecCmd("RUN", "chown", "-R", "daemon:daemon", "."),
  Cmd("USER", "daemon"),
  ExecCmd("ENTRYPOINT", "bin/infoshareapi"),
  Cmd("VOLUME", "/opt/docker"),
  Cmd("EXPOSE", "9000"),
  Cmd("EXPOSE", "9999"),
  Cmd("EXPOSE", "8888")

)

javaOptions in Universal ++= Seq(
  // JVM memory tuning
  "-J-Xms4g",
  "-J-Xmx4g",
  "-J-Xmn2g"
)

libraryDependencies += guice
libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" % "scalatestplus-play_2.12" % "3.1.2" % "test"
libraryDependencies += "org.scalactic" % "scalactic_2.12" % "3.0.4"

libraryDependencies += "javax.mail" % "javax.mail-api" % "1.5.6"
libraryDependencies += "org.typelevel" % "cats-core_2.12" % catsVersion



libraryDependencies += "com.github.romix.akka" % "akka-kryo-serialization_2.12" % "0.5.2"
libraryDependencies += "com.esotericsoftware" % "kryo" % "4.0.1"
libraryDependencies += "com.github.t3hnar" % "scala-bcrypt_2.12" % "3.0"
libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "3.9.0"
libraryDependencies += "com.squareup.moshi" % "moshi" % "1.5.0"



libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.7"
libraryDependencies += "org.apache.commons" % "commons-email" % "1.4"
libraryDependencies += "commons-io" % "commons-io" % "2.5"

libraryDependencies += "com.roundeights" % "hasher_2.12" % "1.2.0"
libraryDependencies += "com.sendgrid" % "sendgrid-java" % "4.1.2"
libraryDependencies += "org.bitbucket.b_c" % "jose4j" % "0.6.2"
libraryDependencies += "com.rometools" % "rome" % "1.9.0"
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.2"
libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.0.2"
libraryDependencies += "com.squareup.moshi" % "moshi" % "1.5.0"
libraryDependencies += "com.squareup.moshi" % "moshi-adapters" % "1.5.0"



libraryDependencies += "com.typesafe.play" % "play-iteratees_2.12" % "2.6.1"
libraryDependencies += "com.typesafe.play" % "play-iteratees-reactive-streams_2.12" % "2.6.1"

libraryDependencies += "com.outworkers" % "phantom-dsl_2.12" % PhantomVersion
libraryDependencies += "com.outworkers" % "phantom-connectors_2.12" % PhantomVersion
libraryDependencies += "com.outworkers" % "phantom-streams_2.12" % PhantomVersion
libraryDependencies += "com.outworkers" % "phantom-jdk8_2.12" % PhantomVersion


libraryDependencies += "com.typesafe.play" % "play-json_2.12" % "2.6.8"
libraryDependencies += "com.typesafe.play" % "play-akka-http-server_2.12" % PlayFrameWorkVersion
libraryDependencies += "com.typesafe.play" % "play-guice_2.12" % PlayFrameWorkVersion
libraryDependencies += "com.typesafe.play" % "play-ws_2.12" % PlayFrameWorkVersion




resolvers ++= Seq(
  "Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "softprops-maven" at "http://dl.bintray.com/content/softprops/maven",
  "Brando Repository" at "http://chrisdinn.github.io/releases/",
  "Sonatype repo" at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype staging" at "http://oss.sonatype.org/content/repositories/staging",
  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
  "Twitter Repository" at "http://maven.twttr.com",
  "Websudos releases" at "https://dl.bintray.com/websudos/oss-releases/",
  "Goose Updates " at "https://dl.bintray.com/raisercostin/maven/",
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("public")
)

