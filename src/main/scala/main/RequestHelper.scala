package main

import org.scalajs.dom
import org.scalajs.dom.XMLHttpRequest

object RequestHelper {

  def sendRequest(url: String, method: String = "GET", params: Map[String, String] = Map.empty): XMLHttpRequest = {
    val questionRequest = new dom.XMLHttpRequest()
    questionRequest.open(method, url + "?" + params.map(b => s"${b._1}=${b._2}").mkString("&"))
    questionRequest.send()
    questionRequest
  }
}
