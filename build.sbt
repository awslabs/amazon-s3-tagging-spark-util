name := "amazon-s3-tagging-spark-util"

ThisBuild / version := "2.0"

ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.18")

// Root Project
lazy val root = project
  .in(file("."))
  .settings(settings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(spark_24, spark_31, spark_33)

// Module for Spark 2.4
lazy val spark_24 = project
  .settings(
    name := "spark_24",
    scalaVersion := "2.11.12",
    libraryDependencies := Seq(
      "org.apache.spark" %% "spark-sql" % "2.4.3" % "provided",
      "com.amazonaws" % "aws-java-sdk-s3" % "1.11.683" % "provided",
      "org.apache.spark" %% "spark-avro" % "2.4.3", // % "provided",
      "org.apache.commons" % "commons-lang3" % "3.5" excludeAll(
        ExclusionRule(organization = "junit"),
        ExclusionRule(organization = "org.easymock"),
        ExclusionRule(organization = "commons-io"),
        ExclusionRule(organization = "org.hamcrest")
      )
    ),
    settings,
    assemblySettings
  )

// Module for Spark 3.1
lazy val spark_31 = project
  .settings(
    name := "spark_31",
    scalaVersion := "2.12.18",
    libraryDependencies := Seq(
      "org.apache.spark" %% "spark-sql" % "3.1.1" % "provided",
      "com.amazonaws" % "aws-java-sdk-s3" % "1.11.683" % "provided",
      "org.apache.spark" %% "spark-avro" % "3.1.1", // % "provided",
      "org.apache.commons" % "commons-lang3" % "3.10" excludeAll(
        ExclusionRule(organization = "junit"),
        ExclusionRule(organization = "org.easymock"),
        ExclusionRule(organization = "commons-io"),
        ExclusionRule(organization = "org.hamcrest")
      )
    ),
    settings,
    assemblySettings
  )

// Module for Spark 3.3
lazy val spark_33 = project
  .settings(
    name := "spark_33",
    scalaVersion := "2.12.18",
    libraryDependencies := Seq(
      "org.apache.spark" %% "spark-sql" % "3.3.0" % "provided",
      "com.amazonaws" % "aws-java-sdk-s3" % "1.11.683" % "provided",
      "org.apache.spark" %% "spark-avro" % "3.3.0", // % "provided",
      "org.apache.commons" % "commons-lang3" % "3.12.0" excludeAll(
        ExclusionRule(organization = "junit"),
        ExclusionRule(organization = "org.easymock"),
        ExclusionRule(organization = "commons-io"),
        ExclusionRule(organization = "org.hamcrest")
      )
    ),
    settings,
    assemblySettings
  )

lazy val spark_34 = project
  .settings(
    name := "spark_34",
    scalaVersion := "2.12.18",
    libraryDependencies := Seq(
      "org.apache.spark" %% "spark-sql" % "3.4.0" % "provided",
      "com.amazonaws" % "aws-java-sdk-s3" % "1.11.683" % "provided",
      "org.apache.spark" %% "spark-avro" % "3.4.0", // % "provided",
      "org.apache.commons" % "commons-lang3" % "3.12.0" excludeAll(
        ExclusionRule(organization = "junit"),
        ExclusionRule(organization = "org.easymock"),
        ExclusionRule(organization = "commons-io"),
        ExclusionRule(organization = "org.hamcrest")
      )
    ),
    settings,
    assemblySettings
  )

lazy val compilerOptions = Seq(
  "-encoding",
  "utf8"
)

lazy val settings = Seq(scalacOptions ++= compilerOptions)

lazy val assemblySettings = Seq(
  assembly / assemblyJarName :=
    s"amazon-s3-tagging-spark-util-${name.value}-scala-${scalaBinaryVersion.value}-lib-${version.value}.jar",

  assembly / assemblyOption :=
    (assembly / assemblyOption).value.withIncludeScala(false),

  assembly / assemblyMergeStrategy := {
    case PathList("org", "apache", "spark", "unused", _@_*) =>
      MergeStrategy.discard
    case PathList("META-INF", "services", _@_*) =>
      MergeStrategy.concat
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)
