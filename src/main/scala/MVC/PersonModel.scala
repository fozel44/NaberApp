package MVC

import java.sql.{CallableStatement, Connection, DriverManager, PreparedStatement, SQLException, SQLIntegrityConstraintViolationException}
import java.util.concurrent.TimeUnit




 /*dao-domain object*/

class PersonModel(var name:String, var phone:String,var password:String) {
  var personId: Int = 0

  def getName(): String = {
    name
  }

  def setName(newname:String):Unit= {
    name=newname
  }

  def getPhone(): String = {
    phone
  }

  def setPhone(newphone:String):Unit= {
    phone=newphone
  }


  def getPassword(): String = {
    password
  }

  def setPassword(newPassword:String):Unit = {
    password=newPassword
  }

  def idToPersonModel(person: PersonModel,arr:Array[Int],connection: Connection): Array[PersonModel] ={
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query =  "select person_id,name ,phone from person where person_id= ? ;"
    var arr2:Array[PersonModel]=Array()
    for(i <-  0 to arr.length-1){

      var stmt:PreparedStatement = connection.prepareStatement(query)
      stmt.setInt(1,arr(i))
      rs = stmt.executeQuery()
      rs.next()

      var personID = rs.getInt("person_id")
      var name =     rs.getString("name")
      var phone =    rs.getString("phone")
      val person= new PersonModel(name,phone,"")
      person.personId=personID
      arr2 = arr2 ++ Array(person)
    }
    arr2
  }

  def getPersonIdWherePhone(person: PersonModel, connection: Connection): Int = {
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select person_id from person where phone=?"
    val stmt: PreparedStatement = connection.prepareStatement(query)
    stmt.setString(1, person.getPhone())
    rs = stmt.executeQuery()
    var id:Int=0
    while(rs.next()){
     id = rs.getInt("person_id")

    }
  id
  }


  def getPersonIdWherePhoneForRedirect(phone:String, connection: Connection): Int = {
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select person_id from person where phone=?"
    val stmt: PreparedStatement = connection.prepareStatement(query)
    stmt.setString(1,phone)
    rs = stmt.executeQuery()
    rs.next()
    rs.getInt("person_id")
  }


  def isTherePhone(phone: String, connection: Connection): Boolean = {
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select if( exists(select  phone from person where phone=?),1,0) as 'sonuc' "
    val stmt1 = connection.prepareStatement(query)
    stmt1.setString(1, phone)
    rs = stmt1.executeQuery()
    rs.next
    var a = rs.getString("sonuc").toInt
    if (a == 1) {
      true
    } else false
  }


  /*def isThereId(personId:Int, connection: Connection): Boolean = {
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select if( exists(select  person_id from person where person_id=?),1,0) as 'sonuc' "
    val stmt1 = connection.prepareStatement(query)
    stmt1.setInt(1, personId)
    rs = stmt1.executeQuery()
    rs.next
    var a = rs.getString("sonuc").toInt
    if (a == 1) {
      true
    } else false
  }
*/ // isThereId();

  def isThisPhoneActive(phone: String, connection: Connection): Boolean = {
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select if( exists(select  phone from person where phone=? and is_active='Y'),1,0) as 'sonuc' "
    val stmt1 = connection.prepareStatement(query)
    stmt1.setString(1, phone)
    rs = stmt1.executeQuery()
    rs.next
    var a = rs.getString("sonuc").toInt

    if (a == 1) {
      true
    } else false
  }

  def isThisPasswordTrue(phone: String,password:String, connection: Connection): Boolean = {
    val statement = connection.createStatement
    var query = "use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select if( exists ( select phone from person where phone=? and password=?),1,0) as 'sonuc'"
    val stmt2 = connection.prepareStatement(query)
    stmt2.setString(1, phone)
    stmt2.setString(2, password)
    rs = stmt2.executeQuery()
    rs.next
    val a = rs.getString("sonuc")
    if (a.toInt == 1){
      true
    } else false
  }

  def signUpModel(phone: String, password:String,connection: Connection): Array[Boolean] = {
    var arr = Array(isTherePhone(phone, connection))
    arr = arr ++ Array(isThisPhoneActive(phone, connection))
    arr = arr ++ Array(isThisPasswordTrue(phone,password, connection))
    arr
  } // Telefon Kontrolünden sonra kullanıcının aktifliği sorgulanıp Şifresini doğru girip girmediğinin verilerini bir arrayle geri dönderir.

  def loginModel(person:PersonModel,connection: Connection): PersonModel ={
    try{

      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "select person_id,name,phone,password from person where ((phone=?) and (password=?)) and (is_active='Y');"
      val stmt= connection.prepareStatement(query)
      stmt.setString(1,phone)
      stmt.setString(2,password)
      rs = stmt.executeQuery() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      while(rs.next()){
      val personId = rs.getInt("person_id")
      val name = rs.getString("name")
      person.setName(name)
      person.personId=personId
      }
      person
    }catch {
      case ex : SQLException => System.out.println(ex.getMessage)
        new PersonModel("","","")
    }

  }

  def newPerson(person: PersonModel, connection: Connection): Boolean = {

    try {
      val statement = connection.createStatement
      var query = "use naberaqq;"
      statement.executeQuery(query)
      query = "{call new_person (?,?,?)}"
      val stmt: CallableStatement = connection.prepareCall(query)
      stmt.setString(1, person.getName())
      stmt.setString(2, person.getPhone())
      stmt.setString(3, person.getPassword())
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      true
    } catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false
    }
  } // MVC Hazır

  def updatePerson(person: PersonModel,name:String,phone:String,password:String, connection: Connection): PersonModel = {
    try {
      val statement = connection.createStatement
      var query = "use naberaqq;"
      var rs = statement.executeQuery(query)
      query = "{call update_person (?,?,?,?)}"
      val stmt: CallableStatement = connection.prepareCall(query)
      stmt.setString(1, name)
      stmt.setString(2, phone)
      stmt.setString(3, password)
      stmt.setInt(4, person.personId)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      person.setName(name)
      person.setPhone(phone)
      person.setPassword(password)
      person
    } catch {
      case ex: SQLIntegrityConstraintViolationException =>
        System.out.println(ex.getMessage)
        val emptyPerson=new PersonModel("","","")
        emptyPerson.personId = -2
        emptyPerson// Sistemde kayıtlı bir telefon numarasıyla update

      case ex: SQLException =>
        System.out.println(ex.getMessage)
        val emptyPerson=new PersonModel("","","")
        emptyPerson.personId = -1
        emptyPerson// Herhangi bir exception

    }
  } // MVC Hazır

  def deletePerson(person: PersonModel, connection: Connection): Boolean = {

    try {
      val statement = connection.createStatement
      var query = "use naberaqq;"
      var rs = statement.executeQuery(query)
      query = "{call delete_person(?,?)}"
      val stmt: CallableStatement = connection.prepareCall(query)
      stmt.setString(1, phone)
      stmt.setString(2, password)
      stmt.execute()
      true
    } catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false // Herhangi bir exception
    }
  } // MVC Hazır

  def sendMessage(person:PersonModel,receiverPhone:String,message:String,connection: Connection): Boolean ={
    try {
      val statement = connection.createStatement
      var query = "use naberaqq;"
      var rs = statement.executeQuery(query)
      query = "{call send_message (?,?,?)}"
      val stmt: CallableStatement = connection.prepareCall(query)
      stmt.setInt(1, person.personId)
      stmt.setString(2, receiverPhone)
      stmt.setString(3, message)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      true
    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false
    }
  } // MVC Hazır

  def sendMessageWithId(person:PersonModel,receiverId:Int,message:String,connection: Connection): Boolean ={
    try {
      val statement = connection.createStatement
      var query = "use naberaqq;"
      var rs = statement.executeQuery(query)
      query = "{call send_message_with_id (?,?,?)}"
      val stmt: CallableStatement = connection.prepareCall(query)
      stmt.setInt(1, person.personId)
      stmt.setInt(2, receiverId)
      stmt.setString(3, message)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      true
    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false
    }
  } // MVC Hazır

  def getPersonIdAllChats(person:PersonModel,connection: Connection): Array[Int] = {

    try{
      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "{call show_mychats (?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setInt(1,person.personId)
      rs = stmt.executeQuery() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      var arr:Array[Int]=Array()
      while (rs.next) {
        val senderId = rs.getInt("sender_id")
        val receiverId = rs.getInt("receiver_id")

        if(senderId!=person.personId)arr=arr++Array(senderId)
        if(receiverId!=person.personId)arr=arr++Array(receiverId)

      }
      arr.distinct
    }
    catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        Array()
    }
  } // MVC Hazır


}
