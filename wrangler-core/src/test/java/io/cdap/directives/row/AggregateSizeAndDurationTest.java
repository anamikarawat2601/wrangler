/*
 * Copyright © 2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.directives.row;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.*;
import io.cdap.wrangler.internal.executor.DefaultExecutorContext;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AggregateSizeAndDurationTest {

    @Test
    public void testTotalAggregationMBandSeconds() throws Exception {
        List<Row> rows = Arrays.asList(
            new Row("size", "1MB").add("duration", "1s"),
            new Row("size", "512KB").add("duration", "500ms")
        );

        List<Argument> args = Arrays.asList(
            new ColumnName("size"),
            new ColumnName("duration"),
            new ColumnName("totalSize"),
            new ColumnName("totalDuration"),
            new Text("MB"),
            new Text("s"),
            new Text("total")
        );

        Directive directive = new AggregateSizeAndDuration(args);
        ExecutorContext context = new DefaultExecutorContext(true); // simulate last stage
        List<Row> result = directive.execute(rows, context);

        assertEquals(1, result.size());
        Row output = result.get(0);

        assertEquals(1.5, (double) output.getValue("totalSize"), 0.01);
        assertEquals(1.5, (double) output.getValue("totalDuration"), 0.01);
    }

    @Test
    public void testAverageAggregation() throws Exception {
        List<Row> rows = Arrays.asList(
            new Row("size", "2MB").add("duration", "4s"),
            new Row("size", "4MB").add("duration", "2s")
        );

        List<Argument> args = Arrays.asList(
            new ColumnName("size"),
            new ColumnName("duration"),
            new ColumnName("avgSize"),
            new ColumnName("avgDuration"),
            new Text("MB"),
            new Text("s"),
            new Text("average")
        );

        Directive directive = new AggregateSizeAndDuration(args);
        ExecutorContext context = new DefaultExecutorContext(true);
        List<Row> result = directive.execute(rows, context);

        Row output = result.get(0);
        assertEquals(3.0, (double) output.getValue("avgSize"), 0.01);      // (2+4)/2
        assertEquals(3.0, (double) output.getValue("avgDuration"), 0.01);  // (4+2)/2
    }
}
