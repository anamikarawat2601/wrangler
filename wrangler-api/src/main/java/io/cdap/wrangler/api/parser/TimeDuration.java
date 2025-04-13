/*
 * Copyright 2025 [Your Name or Organization]
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

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonObject;

public class TimeDuration implements Token {
    private long milliseconds;

    // Constructor to parse the token (e.g., "150ms", "2s")
    public TimeDuration(String token) {
        parseTimeDuration(token);
    }

    // Method to parse the time duration
    private void parseTimeDuration(String token) {
        String numberPart = token.replaceAll("[^0-9.]", "");
        String unitPart = token.replaceAll("[0-9.]", "").toLowerCase();

        double number = Double.parseDouble(numberPart);
        
        switch (unitPart) {
            case "ms":
                milliseconds = (long) number;
                break;
            case "s":
                milliseconds = (long) (number * 1000);
                break;
            case "m":
                milliseconds = (long) (number * 1000 * 60);
                break;
            case "h":
                milliseconds = (long) (number * 1000 * 60 * 60);
                break;
            case "d":
                milliseconds = (long) (number * 1000 * 60 * 60 * 24);
                break;
            default:
                throw new IllegalArgumentException("Unknown unit: " + unitPart);
        }
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "TIME_DURATION");
        object.addProperty("value", milliseconds);
        return object;
    }

   
    @Override
public TokenType type() {
    return TokenType.TIME_DURATION;
}



    @Override
    public String value() {
        return String.valueOf(milliseconds);
    }
}
