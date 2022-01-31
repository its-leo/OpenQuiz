package main

import model.{Question, QuestionResponse, TokenResponse}
import org.scalajs.dom
import org.scalajs.dom.document
import upickle.default._

import scala.collection.mutable
import scala.math.BigDecimal.RoundingMode
import scala.util.{Random, Try}

object Quiz {

  private val questionBatchSize = 3
  private var (answeredRight, answeredWrong) = (0, 0)
  private val questions: mutable.Stack[Question] = mutable.Stack[Question]()

  private def answeredTotal: Int = answeredRight + answeredWrong

  def main(args: Array[String]): Unit = {

    document.body.setAttribute("style", "font-family:verdana;")

    val infoNode = document.createElement("p")
    document.body.appendChild(infoNode)

    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>

      val tokenRequest = RequestHelper.sendRequest("https://opentdb.com/api_token.php",
        params = Map("command" -> "request"))

      var tokenResponse: Option[TokenResponse] = None

      tokenRequest.onload = (e: dom.Event) => {
        if (tokenRequest.status == 200) {
          val response = read[TokenResponse](tokenRequest.responseText)
          tokenResponse = Some(response)
        }
      }

      tokenRequest.onloadend = (e: dom.Event) => {

        def updateScore(): Unit = {
          val quoteRight = Try {
            if (answeredTotal > 0) {
              BigDecimal(answeredRight.toDouble / answeredTotal * 100).setScale(2, RoundingMode.HALF_UP)
            } else BigDecimal(0)
          }.getOrElse(BigDecimal(0))
          infoNode.textContent = s"Right: $answeredRight | Wrong: $answeredWrong | Quote: $quoteRight%"
        }

        updateScore()

        val hr = document.createElement("hr")
        hr.setAttribute("style", "width:50%;text-align:left;margin-left:0")
        document.body.appendChild(hr)

        def getQuestions(): Unit = if (tokenResponse.nonEmpty) {
          //infoNode.textContent = s"Getting $questionBatchSize new questions with ${tokenResponse.get.token}"
          val questionRequest = RequestHelper.sendRequest("https://opentdb.com/api.php",
            params = Map("amount" -> questionBatchSize.toString, "token" -> tokenResponse.get.token))
          questionRequest.onloadend = (e: dom.Event) => update()
          questionRequest.onload = (e: dom.Event) => {
            if (questionRequest.status == 200) {
              val response = read[QuestionResponse](questionRequest.responseText)
              questions.pushAll(Random.shuffle(response.results.toList))
            }
          }
        } else infoNode.textContent = "Token not found"

        getQuestions()

        def update(): Unit = {

          val questionDiv = document.createElement("div")
          document.body.appendChild(questionDiv)

          val question = questions.pop()

          val questionNode = document.createElement("h3")
          questionNode.textContent = question.question

          questionDiv.appendChild(questionNode)

          val answerDiv = document.createElement("btn-group")
          document.body.appendChild(answerDiv)

          val answers = Random.shuffle(question.incorrect_answers.appended(question.correct_answer).toList)

          answers.foreach {
            answer =>
              val button = document.createElement("button")
              button.textContent = answer
              answerDiv.appendChild(button)
              button.addEventListener("click", { (e: dom.MouseEvent) =>
                if (answer.equals(question.correct_answer)) {
                  answeredRight += 1
                } else {
                  answeredWrong += 1
                }

                questionDiv.remove
                answerDiv.remove
                updateScore()

                if (questions.isEmpty) {
                  getQuestions()
                } else update()

              })

          }
        }
      }

    })

  }

}