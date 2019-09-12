import java.util.concurrent.TimeUnit

import MVC.{MessageModel, MessageView, PersonModel, PersonView}
import scala.io.StdIn.{readInt, readLine}
import scala.util.control.Breaks
import java.sql.Timestamp
object NaberAqqMVC {
  val personView  = new PersonView
  var person = new PersonModel("","","")
  val messageView = new MessageView


  def clear (): Unit ={
    /*System.out.println("\u001b[H\u001b[2J");
    System.out.flush()*/
    for(i <- 1 to 100) {
      println("")
    }
  }
  def main(args: Array[String]): Unit = {

    val x = new Breaks
    val y = new Breaks
    val z = new Breaks
    val w = new Breaks
    while(true) {
      clear
      println("1.Giriş Yap")
      println("2.Kaydol")
      println("3.Programı Kapat")
      y.breakable {
        readInt match {
          case 1 =>
            x.breakable{
              while (true) {
                clear
                val login = personView.Login()
                if (login.personId == 0) {
                  println(" ! Bir Database Sorgusu Sorunu Yaşandı Lütfen Tekrar Deneyiniz.")
                  TimeUnit.SECONDS.sleep(2)
                  if(personView.repeatQueryForMain()== true) { personView.Login()};
                  else y.break
                }else if (login.personId == -1) {
                  println(" ! Profiliniz Tarafınızca Daha Önce Dondurulmuştur. Yeniden Aktüfleştirmek İçin Kaydol Sekmesine Gidiniz.")
                  TimeUnit.SECONDS.sleep(2)
                  if(personView.repeatQueryForMain()== true) { personView.Login()};
                  else y.break
                }else if (login.personId == -3) {
                  println(" ! Bu Numaraa Uygulamamızda Kayıtlı Değildir.")
                  TimeUnit.SECONDS.sleep(2)
                  if(personView.repeatQueryForMain()== true) { personView.Login()};
                  else y.break
                }else if (login.personId == -2) {
                  y.break
                }else  {
                  person=login
                  x.break
                }
              }
            }

            while (true) {
              clear
              println("Hoşgeldin %s. ".format(person.name))
              println("1.Profilimi Güncelle")
              println("2.Profilimi Sil")
              println("3.Konuşma Başlat")
              println("4.Konuşmalarımı Göster")
              println("5.Çıkış Yap")
              w.breakable {
                var t = readInt()
                t  match {
                  case 1 =>
                    z.breakable {
                      while (true) {
                        clear
                        val updtperson = personView.updatePerson(person)
                        if (updtperson._1 == true) {
                          person = updtperson._2
                          TimeUnit.SECONDS.sleep(2)
                          z.break()
                        } else if (updtperson._1==false){
                          TimeUnit.SECONDS.sleep(2)
                          clear
                          w.break


                        }
                      }
                    }
                  case 2 =>
                    while (true) {
                      clear

                      if (personView.deletePerson(person) == true) {
                        TimeUnit.SECONDS.sleep(2)
                        clear
                        y.break
                      } else {
                        TimeUnit.SECONDS.sleep(2)
                        w.break
                      }
                    }

                  //Profilimi Sil

                  case 3 =>
                    while(true) {
                      clear
                      val tup = personView.sendMessage(person)

                      if(tup._1==0){
                        val whoid = personView.getPersonIdWherePhoneForRedirect(tup._2)
                        val a = new  Timestamp(2000,1,1,1,0,0,0)
                         while (true) {
                           clear
                           val arrMsg = messageView.getMessages(person, new MessageModel(0, 0, 0, "", a), whoid)

                           println("Mesajınız : ")
                           println("Geriye Dönmek İçin 'Back' Yazınız. Mesaj Silmek İçin sil yazıp 'boşluk' mesajın numarası. 'Örn; sil 83'")

                           val str = readLine()
                           if (str.toLowerCase == "back") w.break
                           else if (messageView.validateMessage(str.toLowerCase) == true) {
                             var splited = str.split(" ")
                             if(messageView.validateIndex(splited(1).toInt,arrMsg.length)==false){
                               TimeUnit.SECONDS.sleep(2)
                             }else {
                               val messageDelete = arrMsg(splited(1).toInt - 1)._1
                               messageView.deleteMessage(person, messageDelete)
                             }
                           }else if (personView.sendMessageWithId(person, whoid, str) == -5) {
                             println("Mesajınız 256 Karakterden Fazla Olamaz.")
                           }
                         }
                      }else w.break
                    } //Konuşma Başlat

                  case 4 =>
                    while (true) {
                      clear
                      personView.getPersonIdAllChats(person)
                      println("ID' yi  Yazarak Konuşmayı Sürdür : ")
                      println("'quit' Yazarak Anasayfaya Dön.")
                      val J = readLine()
                      if(J.toLowerCase=="quit") w.break()
                      z.breakable {
                        while(true) {
                          clear
                          val b = new  Timestamp(2000,1,1,1,0,0,0)
                          val messages = messageView.getMessages(person,new MessageModel(0,0,0,"",b), J.toInt)
                          println("Mesajınız : ")
                          println("Geriye Dönmek İçin 'Back' Yazınız. Mesaj Silmek İçin sil yazıp 'boşluk' mesajın numarası. (Örn; sil 83)")
                          val mesaj = readLine()
                          if (mesaj.toLowerCase == "back") z.break
                          else if (messageView.validateMessage(mesaj.toLowerCase)==true) {
                            var splited = mesaj.split(" ")
                            if(messageView.validateIndex(splited(1).toInt,messages.length)==false){
                              TimeUnit.SECONDS.sleep(2)
                            }else{
                              val messageDelete = messages(splited(1).toInt-1)._1
                              messageView.deleteMessage(person,messageDelete)
                              TimeUnit.SECONDS.sleep(2)
                            }
                          }else if(personView.sendMessageWithId(person, J.toInt, mesaj) == -5){
                            println("Mesajınız 256 Karakterden Fazla Olamaz.")
                          }
                           // messageView.getMessages(person,new MessageModel(0,0,0,"",b), J.toInt)
                        }
                      }
                    }


                  //Konuşmalarımı Göster

                  case 5 =>
                    clear
                    println(" °°° Güle Güle %s Arkadaşların Seni Özleyecek :( ".format(person.name))
                    TimeUnit.SECONDS.sleep(2)
                    y.break

                  //Çıkış Yap


                }
              }
            }
          case 2 =>

              clear
              personView.newPerson()
              TimeUnit.SECONDS.sleep(2)
              y.break



          case 3 =>
            clear()
            println(" °°° Hoşçakal!")
            TimeUnit.SECONDS.sleep(2)
            return
        }
      }

    }






















  }
}
