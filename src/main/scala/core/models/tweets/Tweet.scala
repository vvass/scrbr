package models.tweet

/**
  * Created by vvass on 3/29/16.
  */
case class Tweet(id: String, user: User, text: String, place: Option[Place])

case class User(id: String, lang: String, followersCount: Int)

case class Place(country: String, name: String) {
  override lazy val toString = s"$name, $country"
}

