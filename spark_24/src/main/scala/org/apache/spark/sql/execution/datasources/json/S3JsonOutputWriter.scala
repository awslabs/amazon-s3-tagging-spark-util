package org.apache.spark.sql.execution.datasources.json

import com.amazonaws.s3.utils.S3TaggingHelper
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.sql.catalyst.json.JSONOptions
import org.apache.spark.sql.types.StructType

private[json] class S3JsonOutputWriter(path: String,
                                       tags: Map[String, String],
                                       options: JSONOptions,
                                       dataSchema: StructType,
                                       context: TaskAttemptContext)
  extends JsonOutputWriter(path, options, dataSchema, context) {

  override def close(): Unit = {
    super.close()
    // set the tag
    S3TaggingHelper.setTags(path, tags)
  }

}