/*
 * Copyright © 2024 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.StepException;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.annotations.Name;
import io.cdap.wrangler.api.annotations.Description;
import io.cdap.wrangler.api.annotations.Taggable;
import io.cdap.wrangler.api.parser.*;
import io.cdap.wrangler.api.schema.Schema;
import io.cdap.wrangler.api.DirectiveContext;
import io.cdap.wrangler.api.Step;

import java.util.*;

@Name("aggregate-size-duration")
@Description("Aggregates byte sizes and time durations across rows, storing total or average in specified target columns.")
@Taggable(tags = {"aggregate", "bytes", "duration"})
public class AggregateSizeAndDuration implements Directive {
    private String sizeSourceCol;
    private String durationSourceCol;
    private String sizeTargetCol;
    private String durationTargetCol;
    private String sizeUnit = "B";      // Optional
    private String durationUnit = "ns"; // Optional
    private String aggregationType = "total"; // Optional
    private boolean isFinal = false;

    private transient long totalBytes = 0;
    private transient long totalDurationNs = 0;
    private transient int rowCount = 0;

    public AggregateSizeAndDuration(List<Argument> args) {
                                                               
        this.sizeSourceCol = ((ColumnName) args.get(0)).value();
        this.durationSourceCol = ((ColumnName) args.get(1)).value();
        this.sizeTargetCol = ((ColumnName) args.get(2)).value();
        this.durationTargetCol = ((ColumnName) args.get(3)).value();

        if (args.size() > 4) this.sizeUnit = ((Text) args.get(4)).value();
        if (args.size() > 5) this.durationUnit = ((Text) args.get(5)).value();
        if (args.size() > 6) this.aggregationType = ((Text) args.get(6)).value().toLowerCase();
    }

    @Override
    public void initialize(DirectiveContext ctx) {}

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws StepException {
        if (isFinal) return Collections.emptyList();

        for (Row row : rows) {
            Object sizeObj = row.getValue(sizeSourceCol);
            Object durationObj = row.getValue(durationSourceCol);

            long sizeInBytes = new ByteSize(sizeObj.toString()).getBytes();
            long durationInNs = new TimeDuration(durationObj.toString()).getNanoseconds();

            totalBytes += sizeInBytes;
            totalDurationNs += durationInNs;
            rowCount++;
        }

        isFinal = true;

        long finalBytes = aggregationType.equals("average") ? totalBytes / rowCount : totalBytes;
        long finalDurationNs = aggregationType.equals("average") ? totalDurationNs / rowCount : totalDurationNs;

        Row aggregatedRow = new Row();
        aggregatedRow.add(sizeTargetCol, convertBytes(finalBytes, sizeUnit));
        aggregatedRow.add(durationTargetCol, convertDuration(finalDurationNs, durationUnit));

        return Collections.singletonList(aggregatedRow);
    }
      
    private double convertBytes(long bytes, String unit) {
        return switch (unit.toLowerCase()) {
            case "kb" -> bytes / 1024.0;
            case "mb" -> bytes / (1024.0 * 1024);
            case "gb" -> bytes / (1024.0 * 1024 * 1024);
            default -> bytes;
        };
    }

    private double convertDuration(long ns, String unit) {
        return switch (unit.toLowerCase()) {
            case "ms" -> ns / 1_000_000.0;
            case "s", "sec", "second", "seconds" -> ns / 1_000_000_000.0;
            case "m", "min", "minutes" -> ns / (60.0 * 1_000_000_000);
            default -> ns;
        };
    }
}
