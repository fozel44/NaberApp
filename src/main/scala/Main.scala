import tests.ScalaJdbcConnectSelect
import java.util.Formatter
import java.sql.SQLWarning
import java.sql.{CallableStatement, Connection, DriverManager, PreparedStatement, ResultSet, SQLException, Statement}

import sys.process._
import java.io.IOException._

import scala.io.StdIn._
import scala.util.control._
import java.util.concurrent.TimeUnit

object Main {
  val msg = new Message
  val user = new Person





  def login(tel:String,pswrd:String): Boolean ={
    val url = "jdbc:mysql://localhost:3333/mysql?useSSL=false&serverTimezone=Turkey"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection: Connection = null

    try{
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username,password)
      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "select person_id,name,phone,password from person where ((phone=?) and (password=?)) and (is_active='Y');"
      val stmt= connection.prepareStatement(query)
      stmt.setString(1,tel)
      stmt.setString(2,pswrd)
      rs = stmt.executeQuery() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası

      if (!rs.isBeforeFirst() ) {
        System.out.println(" ! Girdiğiniz bilgiler Hatalıdır. Lütfen Tekrar Deneyiniz.")
        connection.close()
        return false
      }
      while (rs.next) {
        user.person_id = rs.getInt("person_id")
        user.name=rs.getString("name")
        user.phone = rs.getString("phone")
        user.password = rs.getString("password")
        println(" °°° ✓✓✔✔✓✓Hoşgeldin %s. Başarıyla Giriş Yapıldı.".format(user.name))
      }
      true
    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false
    }finally {
      if (connection != null) connection.close()


    }

  }

  def clear (): Unit ={
    /*System.out.println("\u001b[H\u001b[2J");
    System.out.flush()*/
    for(i <- 1 to 100) {
      println("")
    }
  }
  def main(args: Array[String]): Unit = {
    println("NaberAqq")
    TimeUnit.SECONDS.sleep(2)
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
                println("Telefon Numaranızı Giriniz : ")
                val a = readLine()
                println("Şifrenizi Giriniz : ")
                val b = readLine()

                if (!login(a, b)) {
                  println(" ! Telefon Numaranızı veya Şifreniz Yanlış Girdiniz.")
                  TimeUnit.SECONDS.sleep(2)
                  println("Tekrar Denemek için '1'e Basınız. Çıkmak İçin '0'a Basınız.")
                  val c = readInt()
                  if (c == 0) y.break
                } else x.break
              }
            }

            while (true) {
              clear
              println("Hoşgeldin %s. ".format(user.name))
              println("1.Profilimi Güncelle")
              println("2.Profilimi Sil")
              println("3.Mesaj Sil")
              println("4.Konuşma Başlat")
              println("5.Konuşmalarımı Göster")
              println("6.Çıkış Yap")
              w.breakable{
                var t = readInt()
                t match {
                  case 1 =>
                    z.breakable {
                      while (true) {
                        clear
                        println("Yeni İsmi Giriniz : ")
                        val a = readLine()
                        println("Yeni Telefon Numarasını Giriniz : ")
                        val b = readLine()
                        println("Yeni Şifrenizi Giriniz : ")
                        val c = readLine()
                        if (user.updatePerson(a, b, c, user.person_id)) {
                          user.name = a
                          user.phone = b
                          user.password = c
                          z.break()
                        } else {
                          clear
                          println(" ! Hatalı Bir İşlem Yaptınız Lütfen Tekrar Deneyiniz.")
                          TimeUnit.SECONDS.sleep(2)
                        }
                      }
                    }
                  case 2 =>
                    while(true) {
                      clear
                      println("Şifrenizi Giriniz : ")
                      val a = readLine
                      if(user.deletePerson(user.phone, a)){
                        println("Profiliniz Başarıyla Silindi.")
                        TimeUnit.SECONDS.sleep(2)
                        y.break
                      }else{
                        println("Opss! Bir Sorun Var.")
                        TimeUnit.SECONDS.sleep(1)
                        println("Tekrar Denemek için '1'e Basınız. Çıkmak İçin '0'a Basınız.")
                        val c = readLine()
                        if(c=="0"){
                          w.break
                        }

                      }
                    }

                  //Profilimi Sil
                  case 3 =>
                    clear
                    println("Silmek İstediğiniz Mesajın ID'sini Giriniz.")
                    val a = readInt()
                    user.deleteMessage(a, user.person_id)
                  //Mesaj Sil

                  case 4 =>
                    while(true) {
                      clear
                      println("Alıcının Telefon Numarasını Giriniz : ")
                      val a = readLine
                      println("Mesajınız : ")
                      val b = readLine
                      if (!user.sendMessage(user.person_id, a, b)) {
                        clear
                        println("Tekrar Denemek için '1'e Basınız. Çıkmak İçin '0'a Basınız.")
                        val d = readInt()
                        if (d == 0) w.break
                      }
                      else w.break()
                    } //Konuşma Başlat

                  case 5 =>
                    while (true) {
                      clear
                      msg.showMyAllChats(user.person_id)
                      println("ID Yazarak Konuşmayı Sürdür : ")
                      println("'quit' Yazarak Anasayfaya Dön.")
                      val J = readLine()
                      if(J.toLowerCase=="quit")w.break()
                      z.breakable {
                        while(true) {
                          clear
                          msg.getMessages(user.person_id, J.toInt)
                          println("Mesajınız : ")
                          println("Geriye Dönmek İçin 'Back' Yazınız.")
                          val a = readLine
                          if (a.toLowerCase == "back") z.break else user.sendMessageWithId(user.person_id, J.toInt, a)
                        }
                      }
                    }


                  //Konuşmalarımı Göster

                  case 6 =>
                    clear
                    println(" °°° Güle Güle %s Arkadaşların Seni Özleyecek :( ".format(user.name))
                    TimeUnit.SECONDS.sleep(2)
                    y.break

                  //Çıkış Yap


                }
              }
            }
          case 2 =>
            while(true) {
              clear
              println("Adınızı Giriniz : ")
              val a = readLine()
              println("Telefon Numaranızı Giriniz : ")
              val b = readLine()
              println("Şifrenizi Giriniz : ")
              val c = readLine()
              if (a.trim==""  || !user.newPerson(a ,b, c)) {
                clear
                println(" ! Bir Şeyler Yanlış Gitmiş Olmalı :( .")
                TimeUnit.SECONDS.sleep(1)
                println("Tekrar Denemek İçin '1'e Basınız. Çıkmak İçin '0'a Basınız.")
                val d = readInt()
                if (d == 0) y.break
              }else {
                clear
                println(" °°° Başarıyla Kaydoldunuz. Giriş Yapmak İçin Yönlendiriliyorsunuz...")
                TimeUnit.SECONDS.sleep(2)
                y.break
              }
            }


          case 3 =>
            clear()
            println(" °°° Hoşçakal!")
            TimeUnit.SECONDS.sleep(2)
            return
        }
      }

    }


    //println("Telefon Numaranızı giriniz:")

    //user.deletePerson(readLine)

    //println("silmek istediğiniz mesajın id'sini giriniz.")

    //user.deleteMessage(readInt)


    //println("Sırasıyla Adınızı, Telefon Numaranızı, İD'nizi Giriniz.")
    //user.updatePerson(readLine,readLine,readLine,readInt)

    //println("Sırasıyla Adınızı ve Telefon Numaranızı Giriniz.")
    //user.newPerson(readLine,readLine)

    //println("Sırasıyla Gönderen ID, Alıcı ID, Mesajınızı Giriniz.")
    //user.sendMessage(readInt,readInt,readLine)

    //msg.showMyAllChats(4)

    //msg.getMessages(4,3)


  }
}

