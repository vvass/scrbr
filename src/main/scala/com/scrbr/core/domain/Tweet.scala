package com.scrbr.core.domain

/**
  * Created by vvass on 4/17/16.
  */


case class User (
  id: String,
  lang: String,
  screenName: String,
  followersCount: Int
)

case class Place (
  country: String,
  name: String
) {
  override lazy val toString = s"$name, $country"
}

case class Tweet(
  id: String,
  user: User,
  text: String,
  lang: String,
  place: Option[Place]
)