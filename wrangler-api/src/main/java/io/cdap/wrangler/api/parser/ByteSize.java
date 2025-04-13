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

public class ByteSize implements Token {
    private long bytes;

    // Constructor to parse the token (e.g., "10KB", "1.5MB")
    public ByteSize(String token) {
        parseByteSize(token);
    }

    // Method to parse the byte size
    private void parseByteSize(String token) {
        String numberPart = token.replaceAll("[^0-9.]", "");
        String unitPart = token.replaceAll("[0-9.]", "").toUpperCase();

        double number = Double.parseDouble(numberPart);

        switch (unitPart) {
            case "B":
                bytes = (long) number;
                break;
            case "KB":
                bytes = (long) (number * 1024);
                break;
            case "MB":
                bytes = (long) (number * 1024 * 1024);
                break;
            case "GB":
                bytes = (long) (number * 1024 * 1024 * 1024);
                break;
            case "TB":
                bytes = (long) (number * 1024L * 1024 * 1024 * 1024);
                break;
            case "PB":
                bytes = (long) (number * 1024L * 1024 * 1024 * 1024 * 1024);
                break;
            default:
                throw new IllegalArgumentException("Unknown unit: " + unitPart);
        }
    }

    public long getBytes() {
        return bytes;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "BYTE_SIZE");
        object.addProperty("value", bytes);
        return object;
    }

    @Override
public TokenType type() {
    return TokenType.BYTE_SIZE;
}


    @Override
    public String value() {
        return String.valueOf(bytes);
    }
}
