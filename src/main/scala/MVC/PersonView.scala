package MVC

import scala.io.StdIn.readLine

class PersonView {

  val personController = new PersonController


  def newPerson(): Int = {
    println("Adınızı Giriniz : ")
    val name = readLine()
    println("Telefon Numaranızı Giriniz : ")
    val phone = readLine()
    println("Şifrenizi Giriniz : ")
    val password = readLine()
    personController.newPerson(name,phone,password) match {
      case -1 => println("Doğrulanmamış Person Parametreleri"); -1

      case 0  => println("Yeni Kayıt Başarıyla Oluşturuldu.") 0

      case 1  => println("Girdiğiniz Şifre Yanlış.") 1

      case 2  => println("! Daha Önce Bu numara ile uygulamamıza kayıt yapmış olduğunuz tespit edildi.\nEski Profilinizi Güncelleyerek Yeniden aktifleştirdik.") 2

      case 3  => println(" ! Bu kullanıcı kayıtlı lütfen giriş yapınız.") 3
    }
  }

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
