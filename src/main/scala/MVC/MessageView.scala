package MVC

class MessageView {
  var msgController= new MessageController
  def showMyAllChats(arr:Array[(Int, String, String)]): Unit ={


    for(i <- 0 until arr.length){
      val a = arr(i)
      println("ID : %d -- Name : %s ----- Phone : %s".format(a._1,a._2,a._3))
    }

  }


}
