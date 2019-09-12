package MVC

import scala.io.StdIn._

class PersonView {

  val personController = new PersonController

  def setIdWithNotification():(Int,Boolean)={
    println("ID Giriniz : ")
    val id = readInt()
    if(personController.validateId(id) == false) {
      println("ID;\nSadece Rakam Girebilirsiniz.")
      if(repeatQueryForMain()==true){
        setIdWithNotification()
      }else  (0,false)
    } else (id,true)
  }

  def setNameWithNotification(): (String,Boolean) ={
    println("İsmi Giriniz : ")
    val name = readLine()
    if(personController.validateName(name) == false) {
    println("Adınız;\nEn fazla 30 karakterden oluşmalıdır.")
    println("Kelimeler Arasında 1 Boşluk Bırakabilirsiniz.")
    println("[',. -] karakterlerini ilk kelimeden sonra kullanabilirsiniz. ")
      if(repeatQueryForMain()==true){
        setNameWithNotification()
      }else  ("",false)
  }else (name,true)
  }

  def setPhoneWithNotification(): (String,Boolean) ={
    println("Telefon Numarasını Giriniz : ")
    val phone = readLine()
    if(personController.validatePhone(phone) == false) {
      println("Telefonunuz;\n11 karakterden oluşmalıdır.")
      println("Örneğin : 05123456789 vb. bir numara giriniz.")
      println("Sadece rakam kullanınız.")
      if(repeatQueryForMain()==true){
        setPhoneWithNotification()
      } else ("",false)
    }else (phone,true)
  }

  def setPasswordWithNotification(): (String,Boolean) ={
    println("Şifrenizi Giriniz : ")
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
      }else  ("",false)
    }else (password,true)
  }

  def setMessageWithNotification(): (String,Boolean) ={
    println("Mesajınızı Giriniz : ")
    val message = readLine()
    if(personController.validateMessage(message) == false){

      println("Mesajınız;\nEn fazla 256 karakterden oluşmalıdır.")

      if(repeatQueryForMain() == true){
        setMessageWithNotification()
      }else return ("",false)
    }else (message,true)
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

  def getPersonIdWherePhoneForRedirect(phone:String): Int ={

    personController.getPersonIdWherePhoneForRedirect(phone)

  }


  def newPerson(): Int = {
    val name = setNameWithNotification()
    if(name._2==false){
       -2
    }else{
      val phone = setPhoneWithNotification()
      if(phone._2==false){
        -2
      }else{
        val password = setPasswordWithNotification()
        if(password._2==false) {
          -2
        }else{
          personController.newPerson(name._1,phone._1,password._1) match {
            case -1 => println("Doğrulanmamış Person Parametreleri")
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
        }

      }

    }
  } // MVC OK -- Geriye Dönderdiği Int e Göre Döngü kurulacak

  def Login(): PersonModel ={
    val phone=setPhoneWithNotification()
    if(phone._2==false){


      var a = new PersonModel("","","")
      a.personId= -2 // Loginden çıkmak istiyor
      a
    }else {
      val password=setPasswordWithNotification()
      if(password._2==false){
        val b = new PersonModel("","","")
        b.personId = -2 // Loginden çıkmak istiyor
        b
      }else {
        personController.login(phone._1,password._1)
      }
    }

  }

  def updatePerson(person:PersonModel) : (Boolean,PersonModel) = {
    val name = setNameWithNotification()
    if (name._2 == false) {
      (false,new PersonModel("","",""))
    } else {
      val phone = setPhoneWithNotification()
      if (phone._2 == false) {
        (false,new PersonModel("","",""))
      } else {
        val password = setPasswordWithNotification()
        if (password._2 == false) {
          (false,new PersonModel("","",""))
        } else {

          val validate = personController.updatePerson(person,name._1, phone._1, password._1)
          if (validate.personId == -1) {
            println("Database de bir sorun var.Lütfen daha sonra tekrar deneyiniz.")
            if (repeatQueryForMain() == true) {
              updatePerson(person)
            } else (false,new PersonModel("","",""))
          } else if (validate.personId == -2) {
            println("Girdiğiniz telefon numarası başka birisi sistemimizde bu numara ile kayıtlı. işlem başarısız. ")
            if (repeatQueryForMain() == true) {
              updatePerson(person)
            } else (false,new PersonModel("","",""))
          } else {
            println("Update İşlemi Başarıyla Gerçekleştirildi.")
            (true,validate)
          }


        }
      }
    } // Mainde geriye dönderdiği boolean'a göre while döngüsü kurulacak.
  }


  def deletePerson(person:PersonModel): Boolean = {

    val enterYourPassword = setPasswordWithNotification()
    if(enterYourPassword._2==false){
      false
    }else{
      if(personController.deletePerson(person,enterYourPassword._1)==true){
        println("Profiliniz Başarıyla Donduruldu.")
        println("Yeniden Aktifleştirmek İsterseniz Eski Bilgilerinizle Kaydol Sekmesinden Aktifleştirebilirsiniz.")
        println("Eğer 14 gün içerisinde aktifleştirmezseniz hesabınız tamamen silinecektir.")
        true
      }else {
        println("Oops! Bir sorunla karşılaştık.")
        if(repeatQueryForMain() == true) deletePerson(person) else false
      }
    }
  } // MVC Hazır

  def sendMessage(person:PersonModel): (Int,String) = {
    val receiverPhone = setPhoneWithNotification()
    if(receiverPhone._2==false){
      return (-1,receiverPhone._1)
    }else{
      val message = setMessageWithNotification()
      if(message._2==false){
        return (-1,receiverPhone._1)
      }else {
        val  veritification = personController.sendMessage(person,receiverPhone._1,message._1)
        veritification match {
          case -1 => println("Bu Telefon Numarası Sistemde Kayıtlı Değil.")
           if(repeatQueryForMain()==true) {
            sendMessage(person)
           }else (-1,receiverPhone._1)

          case 0 => println("Mesajınız Başarıyla Gönderildi."); (0,receiverPhone._1)

          case 1 =>  println("Girdiğiniz Telefon Numarası Doğrulandı Ancak Gönderme İşleminde Sorun Var.\nLütfen Daha Sonra Tekrar Deneyiniz..."); (1,receiverPhone._1)

        }

      }

    }






  } // MVC Hazır

  def sendMessageWithId(person:PersonModel,receiverId:Int,message:String): Int = {
    /*val receiverId = setIdWithNotification()
    if(receiverId._2==false){
      return -1
    }else{
      val message = setMessageWithNotification()
      if(message._2==false){
        return -1
      }*/
        if (personController.validateMessage(message)==false) -5 else {
          val veritification = personController.sendMessageWithId(person, receiverId, message)
          veritification match {
            case -1 => println("Bu Telefon Numarası Sistemde Kayıtlı Değil."); -1

            case 0 => println("Mesajınız Başarıyla Gönderildi."); 0

            case 1 => println("Girdiğiniz Telefon Numarası Doğrulandı Ancak Gönderme İşleminde Sorun Var.\nLütfen Daha Sonra Tekrar Deneyiniz..."); 1

          }

        }
    }// MVC Hazır

  def getPersonIdAllChats(person:PersonModel): Unit ={
    val arr = personController.getPersonIdAllChats(person)
    for(i <- 0 until  arr.length){
      val person_id = arr(i).personId
      val name = arr(i).getName()
      val phone = arr(i).getPhone()
      println("ID : %d ---- Name : %s ---- Phone : %s".format(person_id,name,phone))
    }
  } // MVC Hazır

}
