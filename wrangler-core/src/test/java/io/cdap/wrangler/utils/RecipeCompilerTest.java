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

package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.Recipe;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeCompilerTest {

    @Test
    public void testAggregateDirectiveParsing() throws Exception {
        Recipe recipe = RecipeCompiler.compile("v1",
            Collections.singletonList("aggregate-size-duration sizeCol durationCol totalSize totalDuration MB s total")
        );
        assertEquals(1, recipe.getDirectives().size());
        assertEquals("aggregate-size-duration", recipe.getDirectives().get(0).getName());
    }

    @Test
    public void testInvalidDirectiveFails() {
        assertThrows(IllegalArgumentException.class, () ->
            RecipeCompiler.compile("v1", Collections.singletonList("aggregate-size-duration a b"))
        );
    }
}
