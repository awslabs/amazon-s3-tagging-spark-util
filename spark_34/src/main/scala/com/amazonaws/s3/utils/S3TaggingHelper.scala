package com.amazonaws.s3.utils

import java.util
import com.amazonaws.services.s3.model.{ObjectTagging, SetObjectTaggingRequest, Tag}
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder, AmazonS3URI}
import org.apache.commons.text.StringSubstitutor
import org.apache.spark.internal.Logging

import scala.collection.JavaConverters._
import scala.util.matching.Regex

/**
 * This standard alone object handles s3 tagging for given s3 object path and tags.
 * Additionally this object partition values into tag value fields using [StrSubstitutor].
 */
object S3TaggingHelper extends Logging {

  // S3 Client
  private lazy val s3Client: AmazonS3 = AmazonS3ClientBuilder.standard().build()

  // regex used to extract the partition values from given s3 object path
  private lazy val partitionValueFilter = new Regex("([a-zA-Z0-9-._*'()!]+=[a-zA-Z0-9-._*'()!]+)+")

  // mapper function
  private val stringToMapConverter: Regex.Match => (String, String) =
    (i: Regex.Match) => {
      val Array(k, v) = i.group(0).split("=")
      k -> v
    }

  /**
   * set the tags using S3 client
   *
   * @param path path
   * @param tags user tags
   */
  def setTags(path: String, tags: Map[String, String]): Unit = {
    logInfo(s"[S3Tagging] Starting to tag for Path `$path`")
    val newTags = getExtractedTags(path, tags)
    val s3Uri: AmazonS3URI = new AmazonS3URI(path)
    logInfo(s"[S3Tagging] Starting to tag for Path `$path` with ${newTags.toString} tags ")
    try {
      s3Client.setObjectTagging(
        new SetObjectTaggingRequest(
          s3Uri.getBucket,
          s3Uri.getKey,
          new ObjectTagging(newTags)
        )
      )
      logDebug(s"[S3Tagging] Successfully tagged for Path `$path` with ${newTags.toString} tags.")
    } catch {
      case e: Exception =>
        logError(s"[S3Tagging] Failed to tag for Path `$path` with ${newTags.toString} tags " +
          s"and error ${e.getMessage}.")
        println(s"[S3Tagging] Failed to tag for Path `$path` with ${newTags.toString} tags " +
          s"and error ${e.getMessage}.")
        e.printStackTrace()
        throw e
    }
  }

  /**
   * This function will substitute the partition values into tag value fields
   *
   * @param path s3 file path
   * @param tags given tags
   * @return
   */
  private def getExtractedTags(path: String, tags: Map[String, String]): util.List[Tag] = {
    // create string substitutor using partition values from path
    val partitionSubstitutor =
      new StringSubstitutor(
        partitionValueFilter
          .findAllMatchIn(path)
          .toList
          .map(stringToMapConverter)
          .toMap
          .asJava
      )

    // substitute the partition value in tags
    tags
      .map(kv => {
        val k = kv._1
        val v = kv._2
        k -> partitionSubstitutor.replace(v)
      })
      .map(t => new Tag(t._1, t._2))
      .toList
      .asJava
  }
}
