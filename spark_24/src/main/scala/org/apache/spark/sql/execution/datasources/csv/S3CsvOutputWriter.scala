package org.apache.spark.sql.execution.datasources.csv

import com.amazonaws.s3.utils.S3TaggingHelper
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.sql.types.StructType

private[csv] class S3CsvOutputWriter(path: String,
                                     tags: Map[String, String],
                                     dataSchema: StructType,
                                     context: TaskAttemptContext,
                                     params: CSVOptions)
  extends CsvOutputWriter(path, dataSchema, context, params) {

  override def close(): Unit = {
    super.close()
    // set the tag
    S3TaggingHelper.setTags(path, tags)
  }
}