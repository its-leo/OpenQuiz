package model

import upickle.default.{macroRW, ReadWriter => RW}

case class QuestionResponse(response_code: Int, results: Array[Question]) extends Response

object QuestionResponse {
  implicit val rw: RW[QuestionResponse] = macroRW
}
