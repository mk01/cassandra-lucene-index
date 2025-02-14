/*
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.lucene.udt;

import static com.stratio.cassandra.lucene.builder.Builder.blobMapper;
import static com.stratio.cassandra.lucene.builder.Builder.booleanMapper;
import static com.stratio.cassandra.lucene.builder.Builder.floatMapper;
import static com.stratio.cassandra.lucene.builder.Builder.integerMapper;
import static com.stratio.cassandra.lucene.builder.Builder.stringMapper;
import static com.stratio.cassandra.lucene.util.CassandraUtils.builder;

import com.datastax.driver.core.exceptions.InvalidConfigurationInQueryException;
import com.stratio.cassandra.lucene.BaseTest;
import org.junit.jupiter.api.Test;

/**
 * @author Eduardo Alonso {@literal <eduardoalonso@stratio.com>}
 */

public class UDTValidationTest extends BaseTest {

    @Test
    public void testValidCreateIndex() {
        builder("udt_validation")
            .withTable("udt_validation_table")
            .withUDT("geo_point", "latitude", "float")
            .withUDT("geo_point", "longitude", "float")
            .withUDT("address", "street", "text")
            .withUDT("address", "city", "text")
            .withUDT("address", "zip", "int")
            .withUDT("address", "bool", "boolean")
            .withUDT("address", "height", "float")
            .withUDT("address", "point", "frozen<geo_point>")
            .withColumn("login", "text")
            .withColumn("first_name", "text")
            .withColumn("last_name", "text")
            .withColumn("address", "frozen<address>")
            .withIndexColumn("lucene")
            .withPartitionKey("login")
            .withMapper("address.city", stringMapper())
            .withMapper("address.zip", integerMapper())
            .withMapper("address.bool", booleanMapper())
            .withMapper("address.height", floatMapper())
            .withMapper("first_name", stringMapper())
            .withIndexName("valid_index")
            .build()
            .createKeyspace()
            .createUDTs()
            .createTable()
            .createIndex()
            .dropIndex().dropTable().dropKeyspace();
    }

    @Test
    public void testInvalidCreateIndex() {
        builder("udt_validation")
            .withTable("udt_validation_table")
            .withUDT("geo_point", "latitude", "float")
            .withUDT("geo_point", "longitude", "float")
            .withUDT("address", "street", "text")
            .withUDT("address", "city", "text")
            .withUDT("address", "zip", "int")
            .withUDT("address", "bool", "boolean")
            .withUDT("address", "height", "float")
            .withUDT("address", "point", "frozen<geo_point>")
            .withColumn("login", "text")
            .withColumn("first_name", "text")
            .withColumn("last_name", "text")
            .withColumn("address", "frozen<address>")
            .withIndexColumn("lucene")
            .withPartitionKey("login")
            .withMapper("address.non-existent.latitude", stringMapper())
            .build()
            .createKeyspace()
            .createUDTs()
            .createTable()
            .createIndex(InvalidConfigurationInQueryException.class,
                         "'schema' is invalid : No column definition 'address.non-existent' " +
                             "for field 'address.non-existent.latitude'")
            .dropTable()
            .dropKeyspace();
    }

    @Test
    public void testInvalidCreateIndex2() {
        builder("udt_validation")
            .withTable("udt_validation_table")
            .withUDT("geo_point", "latitude", "float")
            .withUDT("geo_point", "longitude", "float")
            .withUDT("address", "street", "text")
            .withUDT("address", "city", "text")
            .withUDT("address", "zip", "int")
            .withUDT("address", "bool", "boolean")
            .withUDT("address", "height", "float")
            .withUDT("address", "point", "frozen<geo_point>")
            .withColumn("login", "text")
            .withColumn("first_name", "text")
            .withColumn("last_name", "text")
            .withColumn("address", "frozen<address>")
            .withIndexColumn("lucene")
            .withPartitionKey("login")
            .withMapper("address.non-existent", stringMapper())
            .build()
            .createKeyspace()
            .createUDTs()
            .createTable()
            .createIndex(InvalidConfigurationInQueryException.class,
                         "'schema' is invalid : No column definition 'address.non-existent' " +
                             "for field 'address.non-existent'")
            .dropTable()
            .dropKeyspace();
    }

    @Test
    public void testInvalidCreateIndex3() {
        builder("udt_validation")
            .withTable("udt_validation_table")
            .withUDT("geo_point", "latitude", "float")
            .withUDT("geo_point", "longitude", "float")
            .withUDT("address", "street", "text")
            .withUDT("address", "city", "text")
            .withUDT("address", "zip", "int")
            .withUDT("address", "bool", "boolean")
            .withUDT("address", "height", "float")
            .withUDT("address", "point", "frozen<geo_point>")
            .withColumn("login", "text")
            .withColumn("first_name", "text")
            .withColumn("last_name", "text")
            .withColumn("address", "frozen<address>")
            .withIndexColumn("lucene")
            .withPartitionKey("login")
            .withMapper("address.city", stringMapper())
            .withMapper("address.zip", integerMapper())
            .withMapper("address.bool", booleanMapper())
            .withMapper("address.height", floatMapper())
            .withMapper("address.point.latitude", floatMapper())
            .withMapper("address.point.longitude", blobMapper())
            .withMapper("first_name", stringMapper())
            .build()
            .createKeyspace()
            .createUDTs()
            .createTable()
            .createIndex(InvalidConfigurationInQueryException.class,
                         "'schema' is invalid : Type 'org.apache.cassandra.db.marshal.FloatType' " +
                             "in column 'address.point.longitude' is not supported by mapper 'address.point.longitude'")
            .dropTable()
            .dropKeyspace();
    }

    @Test
    public void testInvalidCreateIndex4() {
        builder("udt_validation")
            .withTable("udt_validation_table")
            .withUDT("geo_point", "latitude", "float")
            .withUDT("geo_point", "longitude", "float")
            .withUDT("address", "street", "text")
            .withUDT("address", "city", "text")
            .withUDT("address", "zip", "int")
            .withUDT("address", "bool", "boolean")
            .withUDT("address", "height", "float")
            .withUDT("address", "point", "frozen<geo_point>")
            .withColumn("login", "text")
            .withColumn("first_name", "text")
            .withColumn("last_name", "text")
            .withColumn("address", "frozen<address>")
            .withIndexColumn("lucene")
            .withPartitionKey("login")
            .withMapper("address.city", stringMapper())
            .withMapper("address.zip", integerMapper())
            .withMapper("address.bool", booleanMapper())
            .withMapper("address.height", floatMapper())
            .withMapper("address.point.latitude", floatMapper())
            .withMapper("address.point.longitude.non-existent", floatMapper())
            .withMapper("first_name", stringMapper())
            .build()
            .createKeyspace()
            .createUDTs()
            .createTable()
            .createIndex(InvalidConfigurationInQueryException.class,
                         "'schema' is invalid : No column definition " +
                             "'address.point.longitude.non-existent' for field 'address.point.longitude.non-existent'")
            .dropTable()
            .dropKeyspace();
    }
}
