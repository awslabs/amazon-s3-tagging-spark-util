name := "amazon-s3-tagging-spark-util"

version := "1.0"

crossScalaVersions := Seq("2.11.10", "2.12.10")

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3" % "provided"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.11.683" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-avro" % "2.4.3" // % "provided"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.5" excludeAll(
  ExclusionRule(organization = "junit"),
  ExclusionRule(organization = "org.easymock"),
  ExclusionRule(organization = "commons-io"),
  ExclusionRule(organization = "org.hamcrest")
)

assemblyJarName in assembly := s"${name.value}-assembly_${scalaBinaryVersion.value}-${version.value}.jar"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", "spark", "unused", _@_*) =>
    MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
