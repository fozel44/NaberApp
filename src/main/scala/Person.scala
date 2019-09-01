import java.sql.{CallableStatement, Connection, DriverManager, SQLException}
import java.util.concurrent.TimeUnit

import scala.io.StdIn._

class Person {

  var person_id:Int=0
  var name:String=""
  var phone:String=""
  var password:String=""

  def deletePerson(str:String,pswrd:String): Boolean= {
    val url = "jdbc:mysql://localhost:3333/mysql?useSSL=false&serverTimezone=Turkey"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection: Connection = null

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      var query = "use naberaqq;"
      var rs = statement.executeQuery(query)
      query = "{call delete_person(?,?)}"
      val stmt: CallableStatement = connection.prepareCall(query)
      stmt.setString(1, str)
      stmt.setString(2, pswrd)
      println("Silmek istediğinizden emin misiniz? (Y/N)")
      val x = readLine()
      if(x.toLowerCase == "y" ) {
        stmt.execute()
        val a = stmt.getUpdateCount
        if(a==0) return false
      }else if (x.toLowerCase == "n" ){
        connection.close()
        return false
      }
      true
    } catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        System.out.println(" ! Hatalı Bilgi Girişi")
        TimeUnit.SECONDS.sleep(2)
        false
    } finally {
      if (connection != null) connection.close()


    }

  }

  def deleteMessage(mid:Int,id:Int): Boolean = {
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
      query =  "{call delete_message (?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,mid)
      stmt.setInt(2,id)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      val a= stmt.getUpdateCount
      if(a==0){
        println(" ! Silmeye Çalıştığınız Mesaj Size Ait Değil.")
        TimeUnit.SECONDS.sleep(2)
        return false
      }
      println(" °°° Mesajınız Başarıyla Silindi.")
      TimeUnit.SECONDS.sleep(2)
      true
    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false

    }finally {
      if (connection != null) connection.close()

    }
  }

  def updatePerson(dism:String,dtel:String,pswrd:String,id:Int): Boolean = {
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
      query =  "{call update_person (?,?,?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setString(1,dism)
      stmt.setString(2,dtel)
      stmt.setString(3,pswrd)
      stmt.setInt(4,id)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      val a:Int = stmt.getUpdateCount

      if (a==0) {println(" ! Hatalı bir bilgi girdiniz.Lütfen Tekrar Deneyin!")
        TimeUnit.SECONDS.sleep(2)
        return false
      }

      true
    }catch {
      case ex: SQLException =>
        System.out.println("HATA")

        false
    }finally {
      if (connection != null) connection.close()


    }
  }


  def newPerson(nism:String,ntel:String,pswrd:String): Boolean = {
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
      query="select if( exists(select  phone from person where phone=?),1,0) as 'sonuc' "
      val stmt1  = connection.prepareStatement(query)
      stmt1.setString(1,ntel)
      rs=stmt1.executeQuery()
      rs.next
      var a = rs.getString("sonuc")
      if (a =="1") {
        query = "select if( exists(select  phone from person where phone=? and is_active='Y'),1,0) as 'sonuc' "
        val stmt1 = connection.prepareStatement(query)
        stmt1.setString(1, ntel)
        rs = stmt1.executeQuery()
        rs.next
        a = rs.getString("sonuc")
        if (a == "1") {
          println(" ! Bu kullanıcı kayıtlı lütfen giriş yapınız.")
          TimeUnit.SECONDS.sleep(3)
          return false
        } else {
          println(" ! Daha Önce Bu numara ile uygulamamıza kayıt yapmış olduğunuz tespit edildi.\nEski Profilinizi Güncelleyerek Yeniden aktifleştirmek için sizi yönlendiriyoruz")
          TimeUnit.SECONDS.sleep(4)
          query = "select if( exists ( select phone from person where phone=? and password=?),1,0) as 'sonuc'"
          val stmt2 = connection.prepareStatement(query)
          stmt2.setString(1, ntel)
          stmt2.setString(2, pswrd)
          rs = stmt2.executeQuery()
          rs.next
          a = rs.getString("sonuc")
          if (a == "0") {
            println(" ! Girdiğiniz şifre eski şifrenizden farklı lütfen tekrar deneyiniz.")
            TimeUnit.SECONDS.sleep(3)
            return false
          }
        }
      }
      query =  "{call new_person (?,?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setString(1,nism)
      stmt.setString(2,ntel)
      stmt.setString(3,pswrd)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası

      true

    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false
    }finally {
      if (connection != null) connection.close()


    }
  }


  def sendMessage(sid:Int,rtel:String,msg:String): Boolean = {
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
      query =  "{call send_message (?,?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,sid)
      stmt.setString(2,rtel)
      stmt.setString(3,msg)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      println(" °°° Mesajınız Başarıyla Gönderildi.")
      TimeUnit.SECONDS.sleep(2)
      true
    }catch {
      case ex: SQLException =>
        System.out.println(" ! Girdiğiniz Telefon numarası sistemde kayıtlı olmayabilir.")
        TimeUnit.SECONDS.sleep(2)
        false
    }finally {
      if (connection != null) connection.close()


    }
  }

  def sendMessageWithId(sid:Int,rid:Int,msg:String): Boolean = {
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
      query =  "{call send_message_with_id (?,?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,sid)
      stmt.setInt(2,rid)
      stmt.setString(3,msg)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      println(" °°° Mesajınız Başarıyla Gönderildi.")
      TimeUnit.SECONDS.sleep(2)
      true
    }catch {
      case ex: SQLException =>
        System.out.println(" °°° Girdiğiniz Telefon numarası sistemde kayıtlı olmayabilir.")
        TimeUnit.SECONDS.sleep(2)
        false
    }finally {
      if (connection != null) connection.close()


    }
  }


}




