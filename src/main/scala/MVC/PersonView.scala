package MVC

import scala.io.StdIn.readLine

class PersonView {

  val personController = new PersonController

   def updatePerson(person_id:Int) {


     println("Yeni İsmi Giriniz : ")
     val name = readLine()
     println("Yeni Telefon Numarasını Giriniz : ")
     val phone = readLine()
     println("Yeni Şifrenizi Giriniz : ")
     val password = readLine()

     personController.updatePerson(person_id, name, phone, password)
   }
}
