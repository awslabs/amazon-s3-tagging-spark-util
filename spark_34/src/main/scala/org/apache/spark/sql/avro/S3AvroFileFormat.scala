package org.apache.spark.sql.avro

import org.apache.avro.Schema
import org.apache.avro.file.DataFileConstants.{BZIP2_CODEC, DEFLATE_CODEC, SNAPPY_CODEC, XZ_CODEC, ZSTANDARD_CODEC}
import org.apache.avro.mapred.AvroOutputFormat
import org.apache.avro.mapreduce.AvroJob
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.datasources.{FileFormat, OutputWriterFactory}
import org.apache.spark.sql.sources.DataSourceRegister
import org.apache.spark.sql.types.StructType


private[avro] class S3AvroFileFormat
  extends AvroFileFormat
    with FileFormat
    with DataSourceRegister
    with Logging
    with Serializable {

  override def shortName(): String = "s3.avro"

  override def toString: String = "S3.Avro"

  override def prepareWrite(spark: SparkSession,
                            job: Job,
                            options: Map[String, String],
                            dataSchema: StructType): OutputWriterFactory = {
    val parsedOptions = new AvroOptions(options, job.getConfiguration)
    val outputAvroSchema: Schema = parsedOptions.schema
      .getOrElse(SchemaConverters.toAvroType(dataSchema, nullable = false,
        parsedOptions.recordName, parsedOptions.recordNamespace))

    AvroJob.setOutputKeySchema(job, outputAvroSchema)

    if (parsedOptions.compression == "uncompressed") {
      job.getConfiguration.setBoolean("mapred.output.compress", false)
    } else {
      job.getConfiguration.setBoolean("mapred.output.compress", true)
      logInfo(s"Compressing Avro output using the ${parsedOptions.compression} codec")
      val codec = parsedOptions.compression match {
        case DEFLATE_CODEC =>
          val deflateLevel = spark.sessionState.conf.avroDeflateLevel
          logInfo(s"Avro compression level $deflateLevel will be used for $DEFLATE_CODEC codec.")
          job.getConfiguration.setInt(AvroOutputFormat.DEFLATE_LEVEL_KEY, deflateLevel)
          DEFLATE_CODEC
        case codec @ (SNAPPY_CODEC | BZIP2_CODEC | XZ_CODEC | ZSTANDARD_CODEC) => codec
        case unknown => throw new IllegalArgumentException(s"Invalid compression codec: $unknown")
      }
      job.getConfiguration.set(AvroJob.CONF_OUTPUT_CODEC, codec)
    }

    new S3AvroOutputWriterFactory(dataSchema, outputAvroSchema.toString,
      parsedOptions.positionalFieldMatching, options.getOrElse("tags", null))
  }
}
