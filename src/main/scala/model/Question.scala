package model

import upickle.default.{ReadWriter => RW, macroRW}

case class Question(category: String, `type`: String, difficulty: String, question: String, correct_answer: String, incorrect_answers: Array[String])

object Question{
  implicit val rw: RW[Question] = macroRW
}
