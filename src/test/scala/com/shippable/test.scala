package com.shippable

import org.scalatra.test.scalatest._

class Tests extends ScalatraFunSuite {
  val db = DbConnection.connect
  addServlet(new SlickApp(db), "/*")

  test("Schema creation") {
    get("/schema") {
      status should equal (200)
    }
  }

  test("Populating and retrieving data") {
    get("/schema") {
      get("/") {
        status should equal (200)
        body should include ("Hello world, 1234!")
      }
    }
  }
}
