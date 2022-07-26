package myLectures.part2af

object Monads extends App {

  // our own Try monad

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  class Lazy[+A](value: => A) { // by-name prevents the value from being evaluated when the object is constructed
    def flatMap[B](f: A => Lazy[B]): Lazy[B] = f(value)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }
}