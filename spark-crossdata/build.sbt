
name := "Crossdata"

version := "1.2.1"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq (
  "com.stratio.crossdata" % "crossdata-core" % "1.2.1",
  "com.stratio.crossdata.connector" % "crossdata-cassandra" % "1.2.1",
  "com.stratio.crossdata.connector" % "crossdata-mongodb" % "1.2.1",
  "com.stratio.crossdata.connector" % "crossdata-elasticsearch" % "1.2.1"
)

spName := "stratio/Crossdata"

sparkVersion := "1.5.2"

sparkComponents ++= Seq("sql")


