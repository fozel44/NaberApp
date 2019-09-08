package MVC

import java.sql.{CallableStatement, Connection, DriverManager, PreparedStatement, SQLException}
import java.util.concurrent.TimeUnit

/*dao-domain object*/

class PersonModel(name:String,phone:String,password:String) {
  var personId:Int = 0

  def getPersonIdWherePhone(person:PersonModel,connection: Connection): Int ={
    val statement = connection.createStatement
    var query="use naberaqq;"
    var rs = statement.executeQuery(query)
    query =  "{select person_id from person where phone=?}"
    val stmt:PreparedStatement = connection.prepareStatement(query)
    stmt.setString(1,person.phone)
    rs=stmt.executeQuery()
    rs.next()
    rs.getInt("person_id")
  }

  def isTherePhone(phone:String,connection: Connection) : Boolean = {
    val statement = connection.createStatement
    var query="use naberaqq;"
    var rs = statement.executeQuery(query)
    query="select if( exists(select  phone from person where phone=?),1,0) as 'sonuc' "
    val stmt1  = connection.prepareStatement(query)
    stmt1.setString(1,phone)
    rs=stmt1.executeQuery()
    rs.next
    var a = rs.getString("sonuc").toInt
    if(a==1){
      true
    }else false
  }

  def isThisPhoneActive(phone:String,connection: Connection) : Boolean = {
    val statement = connection.createStatement
    var query="use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select if( exists(select  phone from person where phone=? and is_active='Y'),1,0) as 'sonuc' "
    val stmt1 = connection.prepareStatement(query)
    stmt1.setString(1, phone)
    rs = stmt1.executeQuery()
    rs.next
    var a = rs.getString("sonuc").toInt
    if(a==1){
      true
    }else false
  }

  def isThisPasswordTrue(phone:String,connection: Connection) : Boolean = {
    val statement = connection.createStatement
    var query="use naberaqq;"
    var rs = statement.executeQuery(query)
    query = "select if( exists(select  phone from person where phone=? and is_active='Y'),1,0) as 'sonuc' "
    val stmt1 = connection.prepareStatement(query)
    stmt1.setString(1, phone)
    rs = stmt1.executeQuery()
    rs.next
    var a = rs.getString("sonuc").toInt
    if(a==1){
      true
    }else false
  }

  def signUpModel(phone:String,connection: Connection): Array[Boolean] = {
    var arr = Array(isTherePhone(phone,connection))
    arr=arr ++ Array(isThisPhoneActive(phone,connection))
    arr=arr ++ Array(isThisPasswordTrue(phone,connection))
    arr
  }

  def newPerson(person: PersonModel,connection: Connection): Boolean = {

    try{
      val statement = connection.createStatement
      var query="use naberaqq;"
      statement.executeQuery(query)
      query =  "{call new_person (?,?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setString(1,name)
      stmt.setString(2,phone)
      stmt.setString(3,password)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      true
    }catch {
      case ex: SQLException =>
        System.out.println(ex.getMessage)
        false
    }
  } // MVC Hazır






  def updatePerson(person:PersonModel, connection:Connection): Boolean = {
    try{
      val statement = connection.createStatement
      var query="use naberaqq;"
      var rs = statement.executeQuery(query)
      query =  "{call update_person (?,?,?,?)}"
      val stmt:CallableStatement = connection.prepareCall(query)
      stmt.setString(1,person.name)
      stmt.setString(2,person.phone)
      stmt.setString(3,person.password)
      stmt.setInt(4,person.personId)
      stmt.execute() // burada yazan kod stored procedure de preparecall yapmak için gerekli olan execute kod parçası
      true
    }catch {
      case ex: SQLException =>
        System.out.println("HATA")
        false
    }
  }




}




