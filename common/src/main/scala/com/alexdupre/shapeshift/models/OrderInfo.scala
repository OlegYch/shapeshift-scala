package com.alexdupre.shapeshift.models

import com.alexdupre.shapeshift.models.OrderStatus.OrderStatus
import play.api.libs.json.{Format, JsError, JsValue, Json}

sealed trait OrderInfo {
  def status: OrderStatus
  def incomingType: Coin
  def incomingCoin: BigDecimal
  def incomingCoinInfo: CoinInfo
  def outgoingType: Coin
  def outgoingCoinInfo: CoinInfo
  def deposit: String
}

case class OpenOrderNoDeposit(
    status: OrderStatus,
    incomingType: Coin,
    incomingCoin: BigDecimal,
    incomingCoinInfo: CoinInfo,
    outgoingType: Coin,
    outgoingCoin: BigDecimal,
    outgoingCoinInfo: CoinInfo,
    deposit: String,
    withdrawal: String
) extends OrderInfo

case class FixedOrderNoDeposit(
    status: OrderStatus,
    incomingType: Coin,
    incomingCoin: BigDecimal,
    incomingCoinInfo: CoinInfo,
    outgoingType: Coin,
    outgoingCoin: BigDecimal,
    outgoingCoinInfo: CoinInfo,
    deposit: String,
    withdrawal: String,
    timeRemaining: BigDecimal,
    rate: BigDecimal
) extends OrderInfo

case class OrderExpired(
    status: OrderStatus,
    incomingType: Coin,
    incomingCoin: BigDecimal,
    incomingCoinInfo: CoinInfo,
    outgoingType: Coin,
    outgoingCoin: BigDecimal,
    outgoingCoinInfo: CoinInfo,
    deposit: String,
    withdrawal: String,
    timeRemaining: BigDecimal,
    rate: BigDecimal
) extends OrderInfo

case class OrderReceived(
    orderId: String,
    status: OrderStatus,
    incomingType: Coin,
    incomingCoin: BigDecimal,
    incomingCoinInfo: CoinInfo,
    outgoingType: Coin,
    outgoingCoin: BigDecimal,
    outgoingCoinInfo: CoinInfo,
    deposit: String
) extends OrderInfo

case class OrderComplete(
    orderId: String,
    status: OrderStatus,
    incomingType: Coin,
    incomingCoin: BigDecimal,
    incomingCoinInfo: CoinInfo,
    outgoingType: Coin,
    outgoingCoin: BigDecimal,
    outgoingCoinInfo: CoinInfo,
    deposit: String,
    withdraw: String,
    rate: BigDecimal,
    transaction: String,
    transactionURL: String
) extends OrderInfo

case class OrderContactSupport(
    orderId: String,
    status: OrderStatus,
    incomingType: Coin,
    incomingCoin: BigDecimal,
    incomingCoinInfo: CoinInfo,
    outgoingType: Coin,
    outgoingCoinInfo: CoinInfo,
    deposit: String,
    rate: BigDecimal
) extends OrderInfo

case class OrderResolved(
    orderId: String,
    status: OrderStatus,
    incomingType: Coin,
    incomingCoin: BigDecimal,
    incomingCoinInfo: CoinInfo,
    outgoingType: Coin,
    outgoingCoinInfo: CoinInfo,
    deposit: String,
    withdraw: String,
    transaction: String,
    transactionURL: String
) extends OrderInfo

object OrderInfo {

  implicit val openOrderNoDepositFormat  = Json.format[OpenOrderNoDeposit]
  implicit val fixedOrderNoDepositFormat = Json.format[FixedOrderNoDeposit]
  implicit val orderExpiredFormat        = Json.format[OrderExpired]
  implicit val orderReceivedFormat       = Json.format[OrderReceived]
  implicit val orderCompleteFormat       = Json.format[OrderComplete]
  implicit val orderContactSupportFormat = Json.format[OrderContactSupport]
  implicit val orderResolvedFormat       = Json.format[OrderResolved]

  implicit val format = new Format[OrderInfo] {

    def reads(json: JsValue) = ((json \ "status").as[OrderStatus], (json \ "type").as[Int]) match {
      case (OrderStatus.NoDeposits, 1)     => json.validate[OpenOrderNoDeposit]
      case (OrderStatus.NoDeposits, 2)     => json.validate[FixedOrderNoDeposit]
      case (OrderStatus.Expired, _)        => json.validate[OrderExpired]
      case (OrderStatus.Received, _)       => json.validate[OrderReceived]
      case (OrderStatus.Complete, _)       => json.validate[OrderComplete]
      case (OrderStatus.ContactSupport, _) => json.validate[OrderContactSupport]
      case (OrderStatus.Resolved, _)       => json.validate[OrderResolved]
      case _                               => JsError("Unexpected order status")
    }

    def writes(oi: OrderInfo): JsValue = oi match {
      case o: OpenOrderNoDeposit  => Json.toJson(o)
      case o: FixedOrderNoDeposit => Json.toJson(o)
      case o: OrderExpired        => Json.toJson(o)
      case o: OrderReceived       => Json.toJson(o)
      case o: OrderComplete       => Json.toJson(o)
      case o: OrderContactSupport => Json.toJson(o)
      case o: OrderResolved       => Json.toJson(o)
    }
  }

}
