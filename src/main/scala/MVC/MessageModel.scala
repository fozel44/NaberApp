package MVC

import java.sql.{CallableStatement, Connection, SQLException}

class MessageModel(message_id:Int,sender_id:Int,receiver_id:Int,message:String,send_date:String) {

  // show my all chats , get messages
  def MessageModel(){

  }



  def showMyAllChats(personId:Int): Array[(Int,String,String)] = {
    val connection =ConnectionManager.getConnection()

    try{
      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "{call show_mychats (?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,personId)
      rs = stmt.executeQuery() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      var arr:Array[Int]=Array()
      while (rs.next) {
        val senderId = rs.getInt("sender_id")
        val receiverId = rs.getInt("receiver_id")

        if(senderId!=personId)arr=arr++Array(senderId)
        if(receiverId!=personId)arr=arr++Array(receiverId)

      }
      val a = arr.distinct
      query=  "select person_id,name ,phone from person where person_id= ? ;"
      var arr2:Array[(Int,String,String)]=Array()
      for(i <-  0 to a.length-1){
        val c=connection.prepareStatement(query)
        c.setInt(1,a(i))
        val rs2 = c.executeQuery()
        rs2.next()

        var personID = rs2.getInt("person_id")
        var name =     rs2.getString("name")
        var phone =    rs2.getString("phone")
        arr2 = arr2 ++ Array((personID,name,phone))
      }
      arr2
    }
    catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        Array()
    }
  }






}
