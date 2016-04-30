package com.alexdupre.shapeshift.models

import com.alexdupre.shapeshift.models.Availability._
import play.api.libs.json.Json

case class CoinInfo(name: String, symbol: Coin, image: String, status: Availability,
                    specialReturn: Option[Boolean],
                    specialOutgoing: Option[Boolean],
                    specialIncoming: Option[Boolean],
                    fieldName: Option[String],
                    fieldKey: Option[String],
                    qrName: Option[String])

object CoinInfo {

  implicit val format = Json.format[CoinInfo]

}