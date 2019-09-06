package MVC

class PersonController {

  def validatePerson(person:PersonModel):Boolean={
    /*regex*/
    true
  }
  def updatePerson(person_id:Int, name:String, phone:String, password:String): Boolean = {
    val con = ConnectionManager.getConnection()
    try {
      val person = new PersonModel(person_id, name, phone, password)
      if(validatePerson(person))
        person.updatePerson(person,con)
      else
        false
    }
    finally{
       ConnectionManager.closeConnection(con)
      }


  }

}
