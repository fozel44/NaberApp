package MVC

import java.sql.Timestamp

import scala.util.matching.Regex

class MessageController {


  def whoseMessage(person: PersonModel,message:MessageModel): Boolean ={

    if(message.getSenderId() == person.personId){
        true
    }else false
  }

  def validateMessage(messageText:String):Boolean= {
    val Pattern  = new Regex("^sil [0-9]+$")
    val check = Pattern findAllIn(messageText)
    if(check.isEmpty==true) false
    else true
  }

  def validateIndex(index:Int,arrLength:Int): Boolean ={
    if( index-1 > arrLength){
      false
    }else true
  }


  def deleteMessage(person:PersonModel,message: MessageModel): Boolean ={
    val con = ConnectionManager.getConnection()
    try{

        if (whoseMessage(person, message)){

          message.deleteMessage(person, message, con)
          true
        }
        else {
          false
        }

    }finally {
      ConnectionManager.closeConnection(con)
    }

  }


  def getMessages(person: PersonModel,message: MessageModel,whoid:Int): Array[(MessageModel,String)] ={
    val con = ConnectionManager.getConnection()
    try{
      val arr = message.getMessages(person,whoid,con)
      var arrName:Array[String] = Array()
      for(i <- 0 until arr.length){
        arrName = arrName ++ Array(message.getNameWhereId(arr(i),con))
      }
      var a = arr.zip(arrName)

      a
    }finally {
      ConnectionManager.closeConnection(con)
    }
  }
}
