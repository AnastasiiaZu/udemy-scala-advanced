package myLectures.part2af

import lectures.part2afp.PartialFunctions.FunctionNotApplicableException

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // a normal function

  // implementation of {1,2,5} => Int == a partial function to Int
  val aFluffyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFluffyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2)) // 56

  // PF utilities
  val defined: Boolean = aPartialFunction.isDefinedAt(67)

  // lift
  val lifted: Int => Option[Int] = aPartialFunction.lift
  println(lifted(2)) // Some
  println(lifted(3456)) // None

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 56
    case 3 => 999
  }

  println(aMappedList) // will crash on 3

  // note: PF only can have ONE parameter type

  val newFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 56
      case 3 => 999
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi my name is HAL9000"
    case "bye" => "there is no return"
    case "call mom" => "unable to find contact"
  }

  scala.io.Source.stdin.getLines().foreach(line => println("chatbot says: " + chatbot(line)))
}
