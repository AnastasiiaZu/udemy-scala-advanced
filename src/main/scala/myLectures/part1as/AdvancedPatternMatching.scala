package myLectures.part1as

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only elem is $head")
    case _ => "Nothing"
  }

  /*
  * constants
  * wildcards
  * case classes
  * touples
  * some special magic like above
  */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age))
  }

  def bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a yo"
  }

  println(greeting)
}
