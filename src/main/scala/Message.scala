import java.sql.{CallableStatement, Connection, DriverManager, SQLException}

class Message extends Person{

  def showMyAllChats(uid:Int): Unit = {
    val url = "jdbc:mysql://localhost:3333/mysql?useSSL=false&serverTimezone=Turkey"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection: Connection = null

    try{
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username,password)
      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "{call show_mychats (?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,uid)
      rs = stmt.executeQuery() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      var arr2:Array[Int]=Array()
      while (rs.next) {
        val senderId = rs.getInt("sender_id")
        val receiverId = rs.getInt("receiver_id")

        if(senderId!=uid)arr2=arr2++Array(senderId)
        if(receiverId!=uid)arr2=arr2++Array(receiverId)

      }
      val a = arr2.distinct
      query=  "select person_id,name ,phone from person where person_id= ? ;"

      for(i <-  0 to a.length-1){
        val c=connection.prepareStatement(query)
        c.setInt(1,a(i))
        val rs2 = c.executeQuery()
        rs2.next()

        var personID = rs2.getInt("person_id")
        var name =     rs2.getString("name")
        var phone =    rs2.getString("phone")
        println("ID : %d -- Name : %s ----- Phone : %s".format(personID,name,phone))
      }
    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
    }finally {
      if (connection != null) connection.close()

    }
  }
  //mesajlar
  def getMessages(mid:Int,msid:Int): Unit = {
    val url = "jdbc:mysql://localhost:3333/mysql?useSSL=false&serverTimezone=Turkey"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection: Connection = null

    try{
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username,password)
      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "{call get_messages(?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,mid)
      stmt.setInt(2,msid)
      rs = stmt.executeQuery() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      println("Sender Name - Message ID | Messages")
      while (rs.next) {
        val senderandmsg = rs.getString("Sender Name - Message ID")
        val message = rs.getString("message")


        println("%s \t\t\t\t   %s".format(senderandmsg,message))
      }

    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
    }finally {
      if (connection != null) connection.close()

    }
  }
}
