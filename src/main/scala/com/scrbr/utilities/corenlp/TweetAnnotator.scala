package com.scrbr.utilities.corenlp

import edu.arizona.sista.processors.Processor
import edu.arizona.sista.processors.corenlp.CoreNLPProcessor
import edu.arizona.sista.processors.fastnlp.FastNLPProcessor

/**
  * Created by vvass on 5/20/16.
  */
trait TweetAnnotator {
  val tweetCoreProccessor:Processor = new CoreNLPProcessor(withDiscourse = true)
  val tweetFastProccessor:Processor = new FastNLPProcessor(withDiscourse = true)
}
