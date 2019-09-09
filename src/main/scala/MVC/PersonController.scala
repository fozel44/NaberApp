package MVC
import sun.security.util.Password

import scala.util.matching.Regex

class PersonController {

  def validateNewPerson(person: PersonModel): Boolean ={

    if(true){
      true
    }else false

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
    val Pattern = "^[0-9]{11}$".r
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



  def newPerson(name:String,phone:String,password:String): Int ={
    val con=ConnectionManager.getConnection()
    try{
      val person = new PersonModel(name,phone,password)
      if(validateNewPerson(person)==true) {
        var arr = person.signUpModel(phone, con)
        (arr(0), arr(1), arr(2)) match {
          case (0, _, _) => person.newPerson(person, con) // Yeni Kullanıcı kaydı
            person.personId = person.getPersonIdWherePhone(person,con)
            0

          case (1, 0, 0) => 1   // view.passwordfalse password yanlışsa return yapacak

          case (1, 0, 1) => person.newPerson(person, con)   // password doğruysa
            person.personId = person.getPersonIdWherePhone(person,con)
            2

          case (1, 1, _) => 3   // bu telefon kayıtlı ve aktif return yapacak
        }
      }else{
        -1 // doğrulanmamış person parametreleri
      }
    }finally {
      ConnectionManager.closeConnection(con)
    }

  } // MVC Hazır

  def updatePerson(person_id:Int, name:String, phone:String, password:String): Int = {
    val con = ConnectionManager.getConnection()
    try {
      val person = new PersonModel(name, phone, password)
      person.personId = person.getPersonIdWherePhone(person,con)
      person.updatePerson(person,con)
    }
    finally{
      ConnectionManager.closeConnection(con)
    }


  } // MVC Hazır Validate Hariç

}
