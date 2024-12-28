package org.dw.dwsparkhive.Controller;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SparkHiveController {

    private static final Logger logger = LoggerFactory.getLogger(SparkHiveController.class);

    @Autowired
    private SparkSession sparkSession;

    @GetMapping("/testSparkHive")
    public void testSparkHive() {
        Dataset<Row> result = sparkSession.sql("SELECT * FROM movie LIMIT 10");
        result.show();
    }
}
