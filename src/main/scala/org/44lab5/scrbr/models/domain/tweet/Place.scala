package tweet

/**
  * Created by vvass on 3/23/16.
  */
case class Place(country: String, name: String) {
  override lazy val toString = s"$name, $country"
}
