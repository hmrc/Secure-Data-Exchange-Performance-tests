credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

val hmrcRepoHost = java.lang.System.getProperty("hmrc.repo.host", "https://nexus-preview.tax.service.gov.uk")

resolvers ++= Seq(
  "hmrc-snapshots" at hmrcRepoHost + "/content/repositories/hmrc-snapshots",
  "hmrc-releases" at hmrcRepoHost + "/content/repositories/hmrc-releases",
  Resolver.url("hmrc-sbt-plugin-releases",
    url(hmrcRepoHost + "/content/repositories/bintray-hmrc-sbt-plugin-releases"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "1.13.0")

addSbtPlugin("io.gatling" % "gatling-sbt" % "2.2.0")

