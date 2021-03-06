package storm.bolts

import org.apache.storm.topology.{BasicOutputCollector, OutputFieldsDeclarer}
import org.apache.storm.topology.base.BaseBasicBolt
import org.apache.storm.tuple.{Fields, Tuple}
import spray.json.DefaultJsonProtocol._
import spray.json._
import recengines.CounterRecommender
import storage.cassandra.CassandraStorage
import storage.Event

class CounterBolt extends BaseBasicBolt {

  override def execute(input: Tuple, collector: BasicOutputCollector): Unit = {
    implicit val eventFormat = jsonFormat6(Event)
    val event = new String(input.getBinary(0)).parseJson.convertTo[Event]
    val recommender = new CounterRecommender(CassandraStorage)
    recommender.trackEvent(event)
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("word"))
  }
}