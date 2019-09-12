package MVC
import scala.util.matching.Regex


class MessageView {
 var msgController= new MessageController


  def validateMessage(message:String): Boolean ={
    msgController.validateMessage(message)

  }


  def validateIndex(index:Int,arrLength:Int): Boolean ={
    if(!msgController.validateIndex(index,arrLength)){
      println("Silmeye Çalıştığınız Mesaj Sistemimizde Mevcut Değil.")
      false
    }else true
  }

    def getMessages(person: PersonModel,message: MessageModel,whoid:Int): Array[(MessageModel,String)] = {
    val arrMsg = msgController.getMessages(person,message,whoid)
    if(arrMsg.isEmpty==true) {
      println("Bir Hata Oluştu.Lütfen Programdan Çıkış Yapıp Tekrar Deneyiniz.")
       Array()
    }else {
      for( i <- 0 until arrMsg.length ){
        val name = arrMsg(i)._2
        val messageId = arrMsg(i)._1.getMessageId()
        val senderId = arrMsg(i)._1.getSenderId()
        val receiverId = arrMsg(i)._1.getReceiverId()
        val messageText = arrMsg(i)._1.getMessage()
        val sendDate = arrMsg(i)._1.getSendDate()
        println("%d. %s  =>  %s \t\t %s".format(i+1,name,messageText,sendDate.toString))
      }
      arrMsg
    }
  }


  def deleteMessage(person:PersonModel,message: MessageModel): Unit ={
     // burada message modelinin içine silmek istedğimiz mesajı yerleştirip öyle yolllamalıyız
    if(msgController.deleteMessage(person,message)){
      println("Mesajınız Başarıyla Silindi.")
    }else println("Mesajı Silerken Bir Hatayla Karşılaştık.Silmek İstediğiniz Mesaj Size Ait Olmayabilir.")
  }

}
