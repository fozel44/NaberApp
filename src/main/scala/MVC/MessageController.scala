package MVC

class MessageController {
  val msg     = new MessageModel(message_id = 0,sender_id = 0,receiver_id = 0,message = "",send_date = "")
  val person  = new PersonModel()

  def validateShowMyAllChats(): Boolean ={
    if(true){
      return false
    }
    true
  }

  def showMyAllChats(personId:Int): Boolean = {
    val con = ConnectionManager.getConnection()
    try {
      if (validateShowMyAllChats()) {
        msg.showMyAllChats(personId)
        true
      } else false
    }finally {
      ConnectionManager.closeConnection(con)
    }
  }
}
