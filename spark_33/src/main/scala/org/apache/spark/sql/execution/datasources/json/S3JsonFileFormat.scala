package org.apache.spark.sql.execution.datasources.json

import com.amazonaws.s3.utils.S3TaggingExtractor
import org.apache.hadoop.mapreduce.{Job, TaskAttemptContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.json.JSONOptions
import org.apache.spark.sql.catalyst.util.CompressionCodecs
import org.apache.spark.sql.execution.datasources.{CodecStreams, OutputWriter, OutputWriterFactory}
import org.apache.spark.sql.types.StructType

class S3JsonFileFormat extends JsonFileFormat {

  override val shortName: String = "s3.json"

  override def toString: String = "S3.Json"

  override def prepareWrite(sparkSession: SparkSession,
                            job: Job,
                            options: Map[String, String],
                            dataSchema: StructType): OutputWriterFactory = {
    val conf = job.getConfiguration
    val parsedOptions = new JSONOptions(
      options,
      sparkSession.sessionState.conf.sessionLocalTimeZone,
      sparkSession.sessionState.conf.columnNameOfCorruptRecord)
    parsedOptions.compressionCodec.foreach { codec =>
      CompressionCodecs.setCodecConfiguration(conf, codec)
    }

    new OutputWriterFactory {
      override def newInstance(path: String,
                               dataSchema: StructType,
                               context: TaskAttemptContext): OutputWriter = {
        val tags: Map[String, String] = S3TaggingExtractor.extractS3Tags(options.get("tags"))
        new S3JsonOutputWriter(path, tags, parsedOptions, dataSchema, context)
      }

      override def getFileExtension(context: TaskAttemptContext): String = {
        ".json" + CodecStreams.getCompressionExtension(context)
      }
    }
  }

}
