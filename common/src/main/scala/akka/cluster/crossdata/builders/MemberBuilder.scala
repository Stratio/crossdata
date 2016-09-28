package akka.cluster.crossdata.builders

import akka.cluster.{Member, MemberStatus, UniqueAddress}

object MemberBuilder {

  def extractUpNumber(member: Member): Int = member.upNumber

  def apply(uniqueAddress: UniqueAddress, upNumber: Int, status: MemberStatus, roles: Set[String]): Member =
    new Member(uniqueAddress, upNumber, status, roles)

}
