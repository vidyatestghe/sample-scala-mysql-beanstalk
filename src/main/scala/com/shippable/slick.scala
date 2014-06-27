package com.shippable

import org.scalatra.ScalatraServlet
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

case class SlickApp(db: Database) extends ScalatraServlet with SlickRoutes

trait SlickRoutes extends ScalatraServlet {
  class Scores(tag: Tag) extends Table[(Int)](tag, "SCORES") {
    def score = column[Int]("SCORE")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = score
  }
  val scores = TableQuery[Scores]

  val db: Database

  get("/schema") {
    db withSession { implicit session =>
      if (!MTable.getTables("SCORES").list().isEmpty) scores.ddl.drop
      scores.ddl.create
    }
  }

  get("/") {
    db withSession { implicit session =>
     scores += (1234)
     val query = for {
        s <- scores
      } yield (s.score.asColumnOf[Int])

      contentType = "text/html"
      query.list.map { case (s) => s"Hello world, ${s}!" } mkString "<br />"
    }
  }
}

object DbConnection {
  val dbName = System.getProperty("RDS_DB_NAME")
  val userName = System.getProperty("RDS_USERNAME")
  val password = System.getProperty("RDS_PASSWORD")
  val hostname = System.getProperty("RDS_HOSTNAME")
  val port = Integer.parseInt(System.getProperty("RDS_PORT"))
  val databaseUrl = s"jdbc:mysql://${hostname}:${port}/${dbName}"

  def connect = Database.forURL(
    url = databaseUrl, user = userName, password = password, driver = "com.mysql.jdbc.Driver")
}
