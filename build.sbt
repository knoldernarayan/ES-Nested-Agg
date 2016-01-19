name := "ES-Nested-Agg"

scalaVersion :=  "2.11.4"

libraryDependencies  ++= {
                          Seq(
                                "org.elasticsearch" % "elasticsearch" % "1.5.2",
                                "ch.qos.logback"       %     "logback-classic"          %      "1.0.13"
                             )
                        }
