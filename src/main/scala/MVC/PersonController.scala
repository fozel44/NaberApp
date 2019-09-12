package MVC
import java.sql.SQLException

import javax.sound.midi.Receiver
import sun.security.util.Password

import scala.util.matching.Regex

class PersonController {

  def validateNewPerson(person: PersonModel): Boolean ={

    if(true){
      true
    }else false

  }

  def validateId(personId:Int):Boolean={
    val Pattern = new Regex("^[0-9]+$")
    val a = Pattern findAllIn(personId.toString)

    if (a.isEmpty == true){
      false // Id için => Sadece Int. Boş Bırakılmaz.
    }else true
  }

  def validateName(name:String): Boolean ={
    val Pattern = new Regex("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$") // Name
    val a = Pattern findAllIn(name)

    if (a.isEmpty == true || name.length>30){
      false // Name için => 30 karakterden uzun olmamak şartıyla kelimeler arasında 1 boşluk bırakarak name tanımlanabilir.
      // Boş Bırakılamaz.
    }else true

  }

  def validatePhone(phone:String): Boolean ={
    val Pattern = "^[0-9]+$".r
    val a = Pattern findAllIn(phone)
    if(a.isEmpty==true || phone.length != 11) false
    else true     // Phone için => Boş bırakılamaz başında 0 kullanılarak yazılır.
  }

  def validatePassword(password: String): Boolean ={
    val Pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".r
    val a = Pattern findAllIn(password)
    if(a.isEmpty==true || password.length<8 || password.length >10) false
    else true
  }

  def validateMessage(message:String):Boolean={
    if(message.length>256){
      false
    }else true
  }

  def isThereIdInMyChats(arr:Array[Int], id:Int ) : Boolean ={
    for (i <- 0 until arr.length){
      if(arr(i) == id ) return true
    }
  false
  }

  def getPersonIdWherePhoneForRedirect(phone:String): Int ={
    val con = ConnectionManager.getConnection()
    try{
      val person = new PersonModel("","","")
      if(validatePhone(phone)==true){
        person.getPersonIdWherePhoneForRedirect(phone,con)
      }else -1
    }catch {
      case ex :SQLException =>
        System.out.println(ex.getMessage)
        -2
    }

  }




  def newPerson(name:String,phone:String,password:String): Int ={
    val con=ConnectionManager.getConnection()
    try{
      val person = new PersonModel(name,phone,password)

        var arr = person.signUpModel(phone, password ,con)
        (arr(0), arr(1), arr(2)) match {
          case (false, _, _) => person.newPerson(person, con) // Yeni Kullanıcı kaydı
            person.personId = person.getPersonIdWherePhone(person,con)
            0

          case (true, false, false) => 1   // view.passwordfalse password yanlışsa return yapacak

          case (true, false, true) => person.newPerson(person, con)   // password doğruysa
            person.personId = person.getPersonIdWherePhone(person,con)
            2

          case (true, true, _) => 3   // bu telefon kayıtlı ve aktif return yapacak
        }

    }finally {
      ConnectionManager.closeConnection(con)
    }

  } // MVC Hazır

  def login(phone:String,password:String): PersonModel ={
    val con = ConnectionManager.getConnection()
    var person =new PersonModel("","","")
    try{
      (person.isTherePhone(phone,con),person.isThisPhoneActive(phone,con)) match {
        case (true, true) => person =new PersonModel("",phone,password)
          person.loginModel(person,con)
        case (false,_) =>person.personId = -3; person
        case (true, false) =>person.personId = -1; person // adam 'N' ise -1 donderır viev bunu sorgular
      }




    }
    }



  def updatePerson(person:PersonModel,name:String,phone:String,password:String): PersonModel = {
    val con = ConnectionManager.getConnection()
    try {

      person.updatePerson(person,name,phone,password,con)
    }
    finally{
       ConnectionManager.closeConnection(con)
    }


  } // MVC Hazır

  def deletePerson(person:PersonModel,enterYourPassword: String): Boolean ={
    val con = ConnectionManager.getConnection()
    try{
      if (person.getPassword() == enterYourPassword) {
        person.deletePerson(person,con)
        true
      }else false

    }finally {

      ConnectionManager.closeConnection(con)

    }

  } // MVC Hazır

  def sendMessage(person:PersonModel,receiverPhone:String,message:String): Int = {
    val con = ConnectionManager.getConnection()

    try{
      if(person.isTherePhone(receiverPhone,con)==false){
        -1
      }else{

        person.sendMessage(person,receiverPhone,message,con) match {

          case true => 0 // Tamamen başarılı
          case false => 1 // Sql Exception almıştır


        }

      }



    }finally {
      ConnectionManager.closeConnection(con)
    }




  } // MVC Hazır

  def sendMessageWithId(person:PersonModel,receiverId:Int,message:String): Int = {
    val con = ConnectionManager.getConnection()

    try{
      if(isThereIdInMyChats(person.getPersonIdAllChats(person,con),receiverId) == false){
        -1
      }else{

        person.sendMessageWithId(person,receiverId,message,con) match {

          case true => 0 // Tamamen başarılı
          case false => 1 // Sql Exception almıştır


        }

      }



    }finally {
      ConnectionManager.closeConnection(con)
    }




  } // MVC Hazır

  def getPersonIdAllChats(person: PersonModel): Array[PersonModel] = {
    val con = ConnectionManager.getConnection()
    try {
      person.idToPersonModel(person,person.getPersonIdAllChats(person,con),con)
        }finally {
      ConnectionManager.closeConnection(con)
    }
  } // MVC Hazır




}
