package oi.serviceEnablers

import spray.json._
import DefaultJsonProtocol._

case class Employee(name: String, idNumber: Option[String], department: String, anualSalary: Double, vacationDays: Short)

object MyJsonProtocol extends DefaultJsonProtocol{
  implicit val bookFormat = jsonFormat5(Employee.apply)
}

object Employee {
  private var employees =
    List(
      Employee("Alice", Some("1"), "R & D", 100000, 22),
      Employee("Bob", Some("2"), "Human Resources", 200000, 25),
      Employee("Carl", Some("3"), "Managment", 170000, 22)
    )

  def all = employees

  def get(idNumber: String) = employees.find(e => e.idNumber == idNumber)

  def post(employee: Employee) = {
    employees = employee :: employees
    employee
  }

  def delete(idNumber: String) = {
    val employee = employees.find(e => e.idNumber == idNumber)
    employees = employees.filterNot(e => e.idNumber == idNumber)
    employee
  }

  def put(employee: Employee) = {
    val x = employees.find(e => e.idNumber == employee.idNumber)
    employees = employee :: employees.filterNot(e => e.idNumber == employee.idNumber)
    x
  }
}