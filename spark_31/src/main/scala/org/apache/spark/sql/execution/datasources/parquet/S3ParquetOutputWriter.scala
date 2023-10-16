package org.apache.spark.sql.execution.datasources.parquet

import com.amazonaws.s3.utils.S3TaggingHelper
import org.apache.hadoop.mapreduce.TaskAttemptContext

private[parquet] class S3ParquetOutputWriter(path: String,
                                             tags: Map[String, String],
                                             context: TaskAttemptContext)
  extends ParquetOutputWriter(path, context) {

  override def close(): Unit = {
    super.close()
    // set the tag
    S3TaggingHelper.setTags(path, tags)
  }
}
