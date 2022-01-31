package model

import upickle.default.{macroRW, ReadWriter => RW}

case class TokenResponse(response_code: Int, response_message: String, token: String) extends Response

object TokenResponse {
  implicit val rw: RW[TokenResponse] = macroRW
}