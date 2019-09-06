package MVC

import java.sql
import java.sql.DriverManager
import java.sql.Connection

object ConnectionManager {

  val url = "jdbc:mysql://localhost:3333/mysql?useSSL=false&serverTimezone=Turkey"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "root"
  var connection: sql.Connection = null

  def getConnection():Connection= {
    DriverManager.getConnection(url, username, password)
    //Class.forName(driver)
  }
  def closeConnection(c:Connection)={
    c.close()
  }

}
