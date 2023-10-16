package org.apache.spark.sql.avro

import com.amazonaws.s3.utils.S3TaggingExtractor
import org.apache.avro.Schema
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.sql.execution.datasources.OutputWriter
import org.apache.spark.sql.types.StructType

private[avro] class S3AvroOutputWriterFactory(catalystSchema: StructType,
                                              avroSchemaAsJsonString: String,
                                              tagString: String)
  extends AvroOutputWriterFactory(catalystSchema, avroSchemaAsJsonString) {

  private lazy val avroSchema = new Schema.Parser().parse(avroSchemaAsJsonString)

  override def newInstance(path: String,
                           dataSchema: StructType,
                           context: TaskAttemptContext): OutputWriter = {
    val tags: Map[String, String] = S3TaggingExtractor.extractS3Tags(Option(tagString))
    new S3AvroOutputWriter(path, tags, context, catalystSchema, avroSchema)
  }
}