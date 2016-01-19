package com.knoldus

import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.client.Client
import java.net.InetAddress
import org.slf4j.LoggerFactory

object NestedAggObject extends App with NestedAggApi {

  val log = LoggerFactory.getLogger(this.getClass)

  def getClient(): Client = {
    val settings = ImmutableSettings.settingsBuilder()
      .put("client.transport.sniff", true)
      .put("client.transport.ping_timeout", "6s")
      .put("cluster.name", "elasticsearch")
      .build()
    val client = new TransportClient(settings)
      .addTransportAddress(new InetSocketTransportAddress("localhost", 9300))
    client
  }
  val client = getClient
  val termAggResult = getAggregateByGenre(client)
  log.info("Term aggregation by genre ::::::::::::::: " + termAggResult)
  val avgAggResult = getAvgAggregateByPrice(client)
  log.info("Average aggregation by price :::::::::::::::::" + avgAggResult)
  client.close()
}