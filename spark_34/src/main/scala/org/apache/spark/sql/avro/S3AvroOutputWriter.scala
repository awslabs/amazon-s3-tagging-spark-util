package org.apache.spark.sql.avro

import com.amazonaws.s3.utils.S3TaggingHelper
import org.apache.avro.Schema
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.sql.types.StructType

private[avro] class S3AvroOutputWriter(path: String,
                                       tags: Map[String, String],
                                       context: TaskAttemptContext,
                                       schema: StructType,
                                       positionalFieldMatching: Boolean,
                                       avroSchema: Schema)
  extends AvroOutputWriter(path, context, schema, positionalFieldMatching, avroSchema) {

  override def close(): Unit = {
    super.close()
    // set the tag
    S3TaggingHelper.setTags(path, tags)
  }
}
