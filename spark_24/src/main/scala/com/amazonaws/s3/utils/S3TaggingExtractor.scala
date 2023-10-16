package com.amazonaws.s3.utils

import com.fasterxml.jackson.databind.ObjectMapper

import scala.collection.JavaConverters._

/**
 * This standard alone object used to deserialize the JSON objects into Map[String, String].
 */
object S3TaggingExtractor {

  private lazy val mapper: ObjectMapper = new ObjectMapper()

  /**
   * extract the json string into key value tags
   *
   * @param givenString Example: {"Key":"Value"}
   * @return
   */
  def extractS3Tags(givenString: Option[String]): Map[String, String] =
    givenString match {
      case Some(tagString) => mapper.readValue(tagString, classOf[java.util.Map[String, String]]).asScala.toMap
      case None => Map.empty[String, String]
    }

}