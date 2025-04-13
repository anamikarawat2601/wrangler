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

package io.cdap.wrangler.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ByteSizeTest {

    @Test
    public void testByteSizeParsing() {
        assertEquals(10_240L, new ByteSize("10kb").getBytes());
        assertEquals(1_572_864L, new ByteSize("1.5MB").getBytes());
        assertEquals(1_073_741_824L, new ByteSize("1GB").getBytes());
        assertEquals(500L, new ByteSize("500B").getBytes());
    }

    @Test
    public void testInvalidByteSize() {
        assertThrows(IllegalArgumentException.class, () -> new ByteSize("xyz123"));
    }
}

