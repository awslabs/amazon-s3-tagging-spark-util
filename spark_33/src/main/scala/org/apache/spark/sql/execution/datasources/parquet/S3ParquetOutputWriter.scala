package org.apache.spark.sql.execution.datasources.parquet

import com.amazonaws.s3.utils.S3TaggingHelper
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.internal.Logging

private[parquet] class S3ParquetOutputWriter(path: String,
                                             tags: Map[String, String],
                                             context: TaskAttemptContext)
  extends ParquetOutputWriter(path, context) with Logging {

  override def close(): Unit = {
    super.close()
    // set the tag
    logDebug(s"Close Function: $path with $tags.!")
    S3TaggingHelper.setTags(path, tags)
  }
}
