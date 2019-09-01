package tests
import java.util.Formatter
import java.sql.{Connection,DriverManager,PreparedStatement,
  ResultSet,SQLException,Statement}

object ScalaJdbcConnectSelect extends App {
  // connect to the database named "mysql" on port 8889 of localhost
  val url = "jdbc:mysql://localhost:3333/mysql?useSSL=false"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "root"
  var connection:Connection = null
  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username,password)
    val statement = connection.createStatement
    var str="use naberaqq;"
    var rs = statement.executeQuery(str)
    str =  "select * from person;"
    rs = statement.executeQuery(str)

    while (rs.next) {
      val id = rs.getInt("person_id")
      val name = rs.getString("name")
      val phone = rs.getString("phone")
      println("person_id = %d, name = %s, phone = %s".format(id,name,phone))
    }

  } catch {
    case e: Exception => e.printStackTrace
  }
  connection.close



}
