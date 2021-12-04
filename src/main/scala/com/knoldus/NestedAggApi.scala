package com.knoldus

import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.client.Client
import org.elasticsearch.search.aggregations.bucket.nested.Nested
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.aggregations.metrics.avg.Avg
import scala.collection.JavaConversions._

trait NestedAggApi {

  val aggregateByGenre = AggregationBuilders.nested("nested_doc").path("books")
    .subAggregation(
      AggregationBuilders
        .terms("genre_count").field("books.genre"))

  val avgAggregateByPrice = AggregationBuilders.nested("nested_doc").path("books")
    .subAggregation(
      AggregationBuilders.terms("genre_count").field("books.genre")
        .subAggregation(
          AggregationBuilders.avg("avg_price").field("books.price")))

  /**
   * This method returns a list of genres of authors with a number of document matches
   * @param client
   * @return the result
   */
  def getAggregateByGenre(client: Client) = {
    val searchResponse = client.prepareSearch().setSearchType("count").setIndices("authors")
      .addAggregation(aggregateByGenre).execute().get
    val agg: Nested = searchResponse.getAggregations().get("nested_doc")
    val listValues = (agg.getAggregations.get[Terms]("genre_count").getBuckets map { groupBucket =>
      ("gener" -> groupBucket.getKey.toString(), "Number of document" -> groupBucket.getDocCount)
    }).toList

    listValues
  }

  /**
   * getAvgAggregateByPrice method returns average over price on group of genre.
   * @param client
   * @return
   */

  def getAvgAggregateByPrice(client: Client) = {
    val searchResponse = client.prepareSearch().setSearchType("count").setIndices("authors")
      .addAggregation(avgAggregateByPrice).execute().get
    val agg: Nested = searchResponse.getAggregations().get("nested_doc")
    val listValues = (agg.getAggregations.get[Terms]("genre_count").getBuckets map { groupBucket =>
      val avgPrice = groupBucket.getAggregations.get[Avg]("avg_price").getValueAsString
      ("gener" -> groupBucket.getKey.toString(), "Number of document" -> groupBucket.getDocCount,
        "Average price" -> avgPrice)
    }).toList

    listValues
  }

}