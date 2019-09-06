package MVC

import java.sql.{CallableStatement, Connection, SQLException}

/*dao-domain object*/

class PersonModel(personId:Int,name:String,phone:String,password:String) {
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




