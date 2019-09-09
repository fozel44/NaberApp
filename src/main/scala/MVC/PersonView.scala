package MVC

import scala.io.StdIn._

class PersonView {

  val personController = new PersonController

  def setNameWithNotification(): String ={
    println("Yeni İsmi Giriniz : ")
    val name = readLine()
    if(personController.validateName(name) == false) {
      println("Adınız;\nEn fazla 30 karakterden oluşmalıdır.")
      println("Kelimeler Arasında 1 Boşluk Bırakabilirsiniz.")
      println("[',. -] karakterlerini ilk kelimeden sonra kullanabilirsiniz. ")
      if(repeatQueryForMain()==true){
        setNameWithNotification()
      }
    }
    name
  }

  def setPhoneWithNotification(): String ={
    println("Yeni Telefon Numarasını Giriniz : ")
    val phone = readLine()
    if(personController.validatePhone(phone) == false) {
      println("Telefonunuz;\n11 karakterden oluşmalıdır.")
      println("Örneğin : 05123456789 vb. bir numara giriniz.")
      println("Sadece rakam kullanınız.")
      if(repeatQueryForMain()==true){
        setPhoneWithNotification()
      }
    }
    phone
  }

  def setPasswordWithNotification(): String ={
    println("Yeni Şifrenizi Giriniz : ")
    val password = readLine()
    if(personController.validatePassword(password) == false) {
      println("Şifreniz;\nEn az 1 rakam içermelidir.")
      println("En az 1 küçük harf içermelidir.")
      println("En az 1 özel karakter içermelidir. ( @ # $ % ^ & + = ) ")
      println("Boşluk içermemelidir.")
      println("En az 8 karakter uzunluğunda olmalıdır.")
      println("En uzun 10 karakter olmalıdır.")
      if(repeatQueryForMain()==true){
        setPasswordWithNotification()
      }
    }
    password
  }

  def repeatQueryForMain(): Boolean = {
    println("Tekrar Denemek için '1'e Basınız. Çıkmak İçin '0'a Basınız.")
    val input = readInt()
    if(input == 1){
      return true
    }
    else {
      return false
    }

  }

  def newPerson(): Int = {
    val name = setNameWithNotification()
    val phone = setPhoneWithNotification()
    val password = setPasswordWithNotification()
    personController.newPerson(name,phone,password) match {
      case -1 => println("Doğrulanmamış Person Parametreleri");
        if(repeatQueryForMain()==true){
          newPerson()
        }else -1

      case 0  => println("Yeni Kayıt Başarıyla Oluşturuldu."); 0

      case 1  => println("Girdiğiniz Şifre Yanlış.")
        if(repeatQueryForMain()==true){
          newPerson()
        }else 1

      case 2  => println("! Daha Önce Bu numara ile uygulamamıza kayıt yapmış olduğunuz tespit edildi.\nEski Profilinizi Güncelleyerek Yeniden aktifleştirdik."); 2

      case 3  => println(" ! Bu kullanıcı kayıtlı lütfen giriş yapınız."); 3
    }
  } // MVC OK -- Geriye Dönderdiği Int e Göre Döngü kurulacak

  def updatePerson(person_id:Int) : Boolean ={
    val name = setNameWithNotification()
    val phone = setPhoneWithNotification()
    val password = setPasswordWithNotification()
    val validate = personController.updatePerson(person_id, name, phone, password)
    if(validate==0){
      println("Update İşlemi Başarıyla Gerçekleştirildi.")
      true
    }else if (validate == 1){
      println("Database de bir sorun var.Lütfen daha sonra tekrar deneyiniz.")
      if(repeatQueryForMain()==true){
        updatePerson(person_id)
      }else false
    }else if (validate==2){
      println("Girdiğiniz telefon numarası başka birisi sistemimizde bu numara ile kayıtlı. işlem başarısız. ")
      if(repeatQueryForMain()==true){
        updatePerson(person_id)
      }else false
    }else false
  } // Mainde geriye dönderdiği boolean'a göre while döngüsü kurulacak.


}
