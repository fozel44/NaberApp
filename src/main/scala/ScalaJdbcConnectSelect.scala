package tests
import java.util.Formatter
import java.sql.{Connection,DriverManager,PreparedStatement,
  ResultSet,SQLException,Statement}

object ScalaJdbcConnectSelect extends App {
  // connect to the database named "mysql" on port 8889 of localhost
  val url = "jdbc:mysql://78.142.210.52:3306/harbiyei_naberaqq?useSSL=false&serverTimezone=Turkey"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "harbiyei_milid"
  val password = "mlyt440300"
  var connection:Connection = null
  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username,password)
    val statement = connection.createStatement
    var str="use tomusene_naberaqq;"
    var rs = statement.executeQuery(str)
    //str =  "select * from message;"
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
