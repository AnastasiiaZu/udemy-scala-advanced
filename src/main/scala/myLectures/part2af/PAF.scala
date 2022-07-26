package myLectures.part2af

object PAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =  y => x => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 + y

  println(add3(5)) // = 8
  println(superAdder(3)(5)) // curried function

  // Method!
  def curriedAdder(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = curriedAdder(4)
  // lifting = ETA-Expansion

  // functions =! methods (JVM limitation)
  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) // ETA-Expansion
  List(1,2,3).map(x => inc(x)) // the same

  // partial functions application
  val add5 = curriedAdder(5) _ // tells the compiler to turn this expression into Int => Int

  // exercise
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  val add7 = (x: Int) => simpleAddFunction(7, x)
  val add7_2 = simpleAddFunction.curried(7)

  val add7_3 = curriedAddMethod(7) _ // PAF
  val add7_4 = curriedAddMethod(7)(_) // also PAF

  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into functions
  val add7_6 = simpleAddFunction(7, _: Int)

  // underscores are powerful
  def concatenator(a: String,b: String,c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?")
  println(insertName("Anastasiia"))

  // exercise 2
  val numbers = List(3.14159265359, 6.28318530718, 12.5663706144)

  def formatter(formatter: String)(a: Double) = formatter.format(a)
  val format_42f = formatter("%4.2f") _ // lift
  val format_86f = formatter("%8.6f") _
  val format_1412f = formatter("%14.12f") _

  println(numbers.map(format_42f))
  println(numbers.map(format_86f))
  println(numbers.map(format_1412f))

  // exercise 3
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenthesesMethod(): Int = 42

/*  byName(23) // ok
  byName(method) // ok
  byName(parenthesesMethod()) // ok
  byName(parenthesesMethod) // not ok
  byName(() => 42) // not ok
  byName((() => 42)()) // becomes a value
  byName(parenthesesMethod _) // not ok

  byFunction(45) // not ok
  byFunction(method) // not ok!!!
  byFunction(parenthesesMethod)
  byFunction(() => 46) // ok
  byFunction(parenthesesMethod _) */
}
