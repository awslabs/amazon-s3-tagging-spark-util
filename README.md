# Apache Spark based Amazon S3 object tagging 

![Arch](docs/s3_tagging_spark.jpg)

This is a library built on top of Apache Spark for tagging Amazon S3 objects. This library helps you to tag objects at table level or partition level. This library supports the following file format options 

* CSV - `s3.csv`
* JSON - `s3.json` 
* Parquet - `s3.parquet`
* ORC - `s3.orc` 
* text - `s3.text` 
* Avro - `s3.avro`

## Requirements

* Java 8
* SBT 1.x.x+ (for building)
* Scala 2.11.x, Scala 2.12.x (for building)

## How to build the library ?

The project is compiled using [SBT](https://www.scala-sbt.org/1.x/docs/Command-Line-Reference.html). The library depends on Java 8 and is known to work with Apache Spark versions 2.4.3

Spark 2.4:

* To compile the project, run `sbt spark_24/compile`
* To generate the connector jar run `sbt spark_24/compile`
* The above commands will generate the following JAR:
```
spark_24/target/scala-2.11/amazon-s3-tagging-spark-util-spark_24-scala-2.11-lib-2.0.jar
```

Spark 3.1:

* To compile the project, run `sbt spark_31/compile`
* To generate the connector jar run `sbt spark_31/compile`
* The above commands will generate the following JAR:
```
spark_31/target/scala-2.12/amazon-s3-tagging-spark-util-spark_31-scala-2.12-lib-2.0.jar
```

Spark 3.3:

* To compile the project, run `sbt spark_33/compile`
* To generate the connector jar run `sbt spark_33/compile`
* The above commands will generate the following JAR:
```
spark_33/target/scala-2.12/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar
```

This JAR includes the `spark-avro` and `commons-lang3` and its dependencies. They need to be put in Spark's extra classpath. 

Note:- The released JARs are available in the [releases](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases) page. 

## Configure AWS Glue ETL Job

Copy the JAR for the corresponding Scala version and spark version into Amazon S3 bucket
```shell
aws s3 cp spark_24/target/scala-2.11/amazon-s3-tagging-spark-util-spark_24-scala-2.11-lib-2.0.jar s3://$BUCKET/$PREFIX
```

Create a Glue ETL job with following special parameters. For more details on [AWS Glue Special Parameters](https://docs.aws.amazon.com/glue/latest/dg/aws-glue-programming-etl-glue-arguments.html).

### Glue 2.0 Configuration:
```yaml
"--extra-jars" : "s3://$BUCKET/$PREFIX/amazon-s3-tagging-spark-util-spark_24-scala-2.11-lib-2.0.jar" // change the jar for spark version
```
### Glue 3.0 Configuration:
```yaml
"--extra-jars" : "s3://$BUCKET/$PREFIX/amazon-s3-tagging-spark-util-spark_31-scala-2.12-lib-2.0.jar" // change the jar for spark version
"-enable-s3-parquet-optimized-committer": "false"
```
### Glue 4.0 Configuration:
```yaml
"--extra-jars" : "s3://$BUCKET/$PREFIX/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar" // change the jar for spark version
"-enable-s3-parquet-optimized-committer": "false"
```

## Configure AWS EMR ETL Job

Copy the JAR for the corresponding Scala version and spark version into Amazon S3 bucket
```shell
aws s3 cp spark_24/target/scala-2.11/amazon-s3-tagging-spark-util-spark_24-scala-2.11-lib-2.0.jar s3://$BUCKET/$PREFIX
```

### EMR Job Spark Configuration:
```yaml
"spark.jars": "s3://$BUCKET/$PREFIX/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar"
"spark.sql.parquet.fs.optimized.committer.optimization-enabled": "false"
"spark.sql.sources.commitProtocolClass": "org.apache.spark.sql.execution.datasources.SQLHadoopMapReduceCommitProtocol"
"spark.hadoop.mapreduce.output.fs.optimized.committer.enabled": "false"
```
Please update the jar file based on EMR version like below.

## Supported Glue and EMR Versions and Library

|  Glue Supported Version  |  Supported  | Library                                                                                                                                                                                                      |
|:------------------------:|:-----------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|         Glue 2.0         |      ✅      | [amazon-s3-tagging-spark-util-spark_24-scala-2.11-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_24-scala-2.11-lib-2.0.jar)  |
|         Glue 3.0         |      ✅      | [amazon-s3-tagging-spark-util-spark_31-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_31-scala-2.12-lib-2.0.jar)  |
|         Glue 4.0         |      ✅      | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |

|  EMR Supported Version  |  Supported   | Library                                                                                                                                                                                                      |
|:-----------------------:|:------------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|        EMR 6.8.0        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|        EMR 6.8.1        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|        EMR 6.9.0        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|        EMR 6.9.1        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|       EMR 6.10.0        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|       EMR 6.10.1        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|       EMR 6.11.0        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|       EMR 6.11.1        |      ✅       | [amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_33-scala-2.12-lib-2.0.jar)  |
|       EMR 6.12.0        |      ✅       | [amazon-s3-tagging-spark-util-spark_34-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_34-scala-2.12-lib-2.0.jar)  |
|       EMR 6.13.0        |      ✅       | [amazon-s3-tagging-spark-util-spark_34-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_34-scala-2.12-lib-2.0.jar)  |
|       EMR 6.14.0        |      ✅       | [amazon-s3-tagging-spark-util-spark_34-scala-2.12-lib-2.0.jar](https://github.com/awslabs/amazon-s3-tagging-spark-util/releases/download/v2.0/amazon-s3-tagging-spark-util-spark_34-scala-2.12-lib-2.0.jar)  |


## Sample Scala Spark Job

Sample Scala Spark Code: For this example, we assume that we work on some kind of `customer` data, where every it has customer id, name , street, city and country.

```scala
case class Customer(id: Long, name: String, street: String, city: String, country: String)
```
Our library is built on Apache Spark and is designed to work with very large datasets that typically live in a distributed filesystem. For the sake of simplicity in this example, we just generate a few records though.

```scala
val rdd = spark.parallelize(Seq(
  Customer(1, "James Butt", "627 Walford Ave", "Dallas", "Dallas"),
  Customer(2, "Gearldine Gellinger", "4 Bloomfield Ave", "Irving", "Dallas"),
  Customer(3, "Ozell Shealy", "8 Industry Ln", "New York", "New York"),
  Customer(4, "Haydee Denooyer", "25346 New Rd", "New York", "New York"),
  Customer(5, "Mirta Mallett", "7 S San Marcos Rd", "New York", "New York")))

val customerDataFrame = spark.createDataFrame(rdd)
```

Store the data into S3 and tag the objects using additional options.

```scala
// Option 1: Static Tagging
customerDataFrame
  .write
  .format("s3.parquet")
  .option("tags", "{\"ProjectTeam\": \"Team-A\", \"FileType\":\"parquet\"}")
  .save("s3://$DATA_BUCKET/$TABLE_NAME")

// Option 2: Dynamic Tagging using partition value
customerDataFrame
  .write
  .partitionBy("country")
  .format("s3.parquet")
  .option("tags", "{\"ProjectTeam\": \"Team-A\", \"Country\":\"${country}\"}")
  .save("s3://$DATA_BUCKET/$TABLE_NAME")
```

## Sample PySpark Job

Our library is built on Apache Spark and is designed to work with very large datasets that typically live in a distributed filesystem. For the sake of simplicity in this example, we just generate a few records though.

```python

rdd = spark.sparkContext.parallelize([
    Row(id=1, name="James Butt", street="627 Walford Ave", city="Dallas", country="USA"),
    Row(id=2, name="Gearldine Gellinger", street="4 Bloomfield Ave", city="Irving", country="USA"),
    Row(id=3, name="Ozell Shealy", street="8 Industry Ln", city="New York", country="USA"),
    Row(id=4, name="Haydee Denooyer", street="25346 New Rd", city="New York", country="USA"),
    Row(id=5, name="Mirta Mallett", street="7 S San Marcos Rd", city="New York", country="USA")
])

# Create a DataFrame from the RDD
customer_data_frame = spark.createDataFrame(rdd)
```

Store the data into S3 and tag the objects using additional options.

```python
# Option 1: Static Tagging
customer_data_frame \
    .write \
    .format("s3.parquet") \
    .option("tags", "{\"ProjectTeam\": \"Team-A\", \"FileType\":\"parquet\"}") \
    .save("s3://$DATA_BUCKET/$TABLE_NAME")

# Option 2: Dynamic Tagging using partition value
customer_data_frame \
    .write \
    .partitionBy("country") \
    .format("s3.parquet") \
    .option("tags", "{\"ProjectTeam\": \"Team-A\", \"Country\":\"${country}\"}") \
    .save("s3://$DATA_BUCKET/$TABLE_NAME")
```

## Contributing Guidelines

See [CONTRIBUTING](CONTRIBUTING.md) for more information.


## License

This library is licensed under the Apache 2.0 License.
