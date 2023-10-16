package org.apache.spark.sql.execution.datasources.text

import com.amazonaws.s3.utils.S3TaggingHelper
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.sql.types.StructType

class S3TextOutputWriter(path: String,
                         tags: Map[String, String],
                         dataSchema: StructType,
                         lineSeparator: Array[Byte],
                         context: TaskAttemptContext)
  extends TextOutputWriter(path, dataSchema, lineSeparator, context) {

  override def close(): Unit = {
    super.close()
    // set the tag
    S3TaggingHelper.setTags(path, tags)
  }
}