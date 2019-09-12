package MVC

import java.sql.{CallableStatement, Connection, PreparedStatement, SQLException, Timestamp}

class MessageModel(message_id:Int,sender_id:Int,receiver_id:Int,message:String,send_date:Timestamp) {


  def getMessageId(): Int = {
    message_id
  }

  def getSenderId(): Int = {
    sender_id
  }

  def getReceiverId(): Int = {
    receiver_id
  }

  def getMessage():String={
    message
  }

  def getSendDate():Timestamp ={
    send_date
  }


  def getNameWhereId(message: MessageModel,connection: Connection): String ={
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select name from person where person_id=?"
    val stmt: PreparedStatement = connection.prepareStatement(query)
    stmt.setInt(1, message.getSenderId())
    rs = stmt.executeQuery()
    var name:String=""
    while(rs.next()) {
       name = rs.getString("name")
    }
    name
  }


  def getMessages(person: PersonModel, whoid: Int, connection: Connection): Array[MessageModel] = {
    try{
      val statement = connection.createStatement
      var query = "use naberaqq;"
      var rs = statement.executeQuery(query)
      query = "{call get_messages(?,?)}"
      val stmt: CallableStatement = connection.prepareCall(query)
      stmt.setInt(1, person.personId)
      stmt.setInt(2, whoid)
      rs = stmt.executeQuery() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      var arr: Array[MessageModel] = Array()
      while (rs.next()) {
        // val name = rs.getString("name")
        val messageId = rs.getInt("message_id")
        val senderId = rs.getInt("sender_id")
        val receiverId = rs.getInt("receiver_id")
        val message = rs.getString("message")
        val sendDate = rs.getTimestamp("send_date")
        val messageModel = new MessageModel(messageId, senderId, receiverId, message, sendDate)
        arr = arr ++ Array(messageModel)
      }
      arr
    }
    catch{
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        Array()
    }

}

  def deleteMessage(person:PersonModel,message: MessageModel,connection: Connection): Boolean ={

    try{
      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "{call delete_message (?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,message.getMessageId())
      stmt.setInt(2,person.personId)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      true
    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false
    }
  }







}
