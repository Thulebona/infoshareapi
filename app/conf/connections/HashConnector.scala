package conf.connections

import java.net.InetAddress

import com.datastax.driver.core.SocketOptions
import com.datastax.driver.core.policies.{DCAwareRoundRobinPolicy, TokenAwarePolicy}
import com.outworkers.phantom.connectors.{ContactPoint, ContactPoints}
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

/**
  * Created by hashcode on 2017/01/29.
  */
object Config {
  val config = ConfigFactory.load()

  def PORT = 9042

  def connectionTimeoutMillis = 700000

  // Default is 5000
  def readTimeoutMillis = 1500000

  // Default is 12000
  val hosts: Seq[String] = config.getStringList("cassandra.host").asScala.toList
  val dataCenter = config.getString("cassandra.dataCenter")
  val inets = hosts.map(InetAddress.getByName)
  val keySpace: String = config.getString("cassandra.keySpace")
  val clusterName: String = config.getString("cassandra.clusterName")
}

//object DefaultsConnector {
//  val connector = ContactPoint.local.noHeartbeat().keySpace(Config.keySpace)
//}

object DataConnection {

  lazy val connector = ContactPoints(Config.hosts, Config.PORT)
    .withClusterBuilder(
      _.withCredentials(
        Config.config.getString("cassandra.username"),
        Config.config.getString("cassandra.password"))
        .withClusterName(Config.clusterName)
        .withSocketOptions(new SocketOptions()
          .setReadTimeoutMillis(Config.readTimeoutMillis)
          .setConnectTimeoutMillis(Config.connectionTimeoutMillis))
          .withLoadBalancingPolicy(new TokenAwarePolicy(new DCAwareRoundRobinPolicy.Builder()
            .withUsedHostsPerRemoteDc(1)
            .withLocalDc(Config.dataCenter).build()))
    ).noHeartbeat().keySpace(Config.keySpace)


  /**
    * Create an embedded connector, used for testing purposes
    */
  lazy val testConnector = ContactPoint.embedded.noHeartbeat().keySpace(Config.keySpace)
}
