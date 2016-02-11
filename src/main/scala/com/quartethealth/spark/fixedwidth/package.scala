package com.quartethealth.spark


import com.databricks.spark.csv.util.TextFile
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext}

package object fixedwidth {
  implicit class CsvContext(sqlContext: SQLContext) extends Serializable {
    def flatFile(
        filePath: String,
        fixedWidths: Array[Int],
        schema: StructType = null,
        useHeader: Boolean = true,
        mode: String = "PERMISSIVE",
        comment: Character = null,
        ignoreLeadingWhiteSpace: Boolean = false,
        ignoreTrailingWhiteSpace: Boolean = false,
        charset: String = TextFile.DEFAULT_CHARSET.name(),
        inferSchema: Boolean = false): DataFrame = {
      val fixedwidthRelation = new FixedwidthRelation(
        () => TextFile.withCharset(sqlContext.sparkContext, filePath, charset),
        location = Some(filePath),
        useHeader = useHeader,
        comment = comment,
        parseMode = mode,
        fixedWidths = fixedWidths,
        ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace,
        ignoreTrailingWhiteSpace = ignoreTrailingWhiteSpace,
        userSchema = schema,
        inferSchema = inferSchema,
        treatEmptyValuesAsNulls = false)(sqlContext)
      sqlContext.baseRelationToDataFrame(fixedwidthRelation)
    }
  }
}
