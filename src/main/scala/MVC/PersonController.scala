package MVC
import sun.security.util.Password

import scala.util.matching.Regex

class PersonController {

  def validateNewPerson(person: PersonModel): Boolean ={

    if(true){
      true
    }else false

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






  def validateUpdatePerson(name:String,phone:String,password:String):Int={
    var Pattern = new Regex("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$") // Name
    var a = Pattern findAllIn(name)
    if (a.isEmpty==true){
      return 1
    }

    if()
      (person) match {
        case (name) => name.toLowerCase= [a-z]*
        case  =>
        case  =>
        case  =>
      }
    true
  }

  def updatePerson(person_id:Int, name:String, phone:String, password:String): Boolean = {
    val con = ConnectionManager.getConnection()
    try {
      val person = new PersonModel(name, phone, password)
      person.personId = person.getPersonIdWherePhone(person,con)
      if(validatePerson(name, phone, password))
        person.updatePerson(person,con)
      else
        false
    }
    finally{
      ConnectionManager.closeConnection(con)
    }


  } // MVC Hazır Validate Hariç

}
