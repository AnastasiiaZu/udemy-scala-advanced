package myLectures.part1as

import scala.util.Try

object SyntaxSugar extends App {

 // # 1 methods with single params
  def singleArgMethod(arg: Int): String = s" $arg little ducks..."

 val description = singleArgMethod {
   // write some code here
   42
 }
  println(description) // 42 little ducks...

  val aTryInstance = Try {
    throw new RuntimeException
  }

  List(1,2,3).map { x =>
    x * 3
  }

  // #2 single abstract method pattern
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1 // magic!

  // example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala")
  })

  val aSweeterThread = new Thread(() => println("sweet, Scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit // unimplemented
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("nice") // implements an abstract method

  // #3 the :: and #:: methods
  val prependedList = 2 :: List(3, 4)  // .:: method is right-assosiative -> List(3, 4) evaluates first, then .:: 2

  // #4 multi-word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so awesome!"

  // #5 infix types

  class Composite[A, B]
  val composite: Int Composite String = ??? // == Composite[Int, String]

  class -->[A, B]
  val towards: Int --> String = ???

  // #6 update(), much like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7 // ge tre-written to an anArray.update(2,7)
  // used in mutable collections

  // #7 setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member = internalMember // "getter"
    def member_=(value: Int): Unit =
      internalMember = value // "setter" with a _= in the end
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // only happens when a getter and a setter are declared



}
