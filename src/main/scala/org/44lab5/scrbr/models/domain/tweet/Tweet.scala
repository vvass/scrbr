package tweet

case class Tweet(id: String, user: User, text: String, place: Option[Place])