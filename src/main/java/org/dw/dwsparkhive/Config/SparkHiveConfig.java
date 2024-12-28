package org.dw.dwsparkhive.Config;

import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkHiveConfig {

    private static final Logger logger = LoggerFactory.getLogger(SparkHiveConfig.class);

    @Value("${hive.metastore.uris}")
    private String hiveMetastoreUris;

    @Value("${spark.master}")
    private String sparkMaster;

    @Bean
    public SparkSession sparkSession() {
        SparkSession spark = SparkSession.builder()
                .appName("DW-Spark-Hive")
                .master(sparkMaster)
                .config("hive.metastore.uris", hiveMetastoreUris)
                .enableHiveSupport()
                .getOrCreate();
        spark.sql("USE dw");
        System.out.println("SparkSession initialized successfully.");
        return spark;
    }
}