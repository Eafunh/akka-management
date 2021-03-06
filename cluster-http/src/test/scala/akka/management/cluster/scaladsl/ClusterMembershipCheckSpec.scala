/*
 * Copyright (C) 2017-2018 Lightbend Inc. <http://www.lightbend.com>
 */

package akka.management.cluster.scaladsl

import akka.actor.{ ActorSystem, ExtendedActorSystem }
import akka.cluster.MemberStatus
import akka.testkit.TestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpecLike }

class ClusterMembershipCheckSpec
    extends TestKit(ActorSystem("ClusterHealthCheck"))
    with WordSpecLike
    with Matchers
    with ScalaFutures {

  val aes = system.asInstanceOf[ExtendedActorSystem]

  "Cluster Health" should {
    "be unhealthy if current state not one of healthy states" in {
      val chc = new ClusterMembershipCheck(aes, () => MemberStatus.joining,
        new ClusterMembershipCheckSettings(Set(MemberStatus.Up)))

      chc().futureValue shouldEqual false
    }
    "be unhealthy if current state is one of healthy states" in {
      val chc =
        new ClusterMembershipCheck(aes, () => MemberStatus.Up,
          new ClusterMembershipCheckSettings(Set(MemberStatus.Up)))

      chc().futureValue shouldEqual true
    }
  }
}
