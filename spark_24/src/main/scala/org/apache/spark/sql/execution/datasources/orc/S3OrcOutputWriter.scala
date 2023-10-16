package org.apache.spark.sql.execution.datasources.orc

import com.amazonaws.s3.utils.S3TaggingHelper
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.sql.types.StructType

private[orc] class S3OrcOutputWriter(path: String,
                                     tags: Map[String, String],
                                     dataSchema: StructType,
                                     context: TaskAttemptContext)
  extends OrcOutputWriter(path, dataSchema, context) {

  override def close(): Unit = {
    super.close()
    // set the tag
    S3TaggingHelper.setTags(path, tags)
  }
}