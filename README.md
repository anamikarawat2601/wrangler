## ✨ Enhanced Unit Parsing in Wrangler: Byte Size & Time Duration

Wrangler has been enhanced to support **byte size** (e.g., `10MB`, `1.5GB`) and **time duration** (e.g., `200ms`, `2.5s`) unit parsing directly in recipes. This removes the need for complex manual conversions and enables more intuitive data processing.

---

### 📦 New Features

- ✅ Native support for `ByteSize` and `TimeDuration` tokens in recipes.
- 🛠️ Extended **grammar** and **parsing logic** for unit parsing.
- 📊 New directive `aggregate-stats` for aggregating and converting byte size and time duration values.
- 🔍 Comprehensive **unit tests** for grammar, parser, and directive logic.

---

### 📂 Project Changes & File Overview

| 📁 Module              | 📄 File Name / Path                                                                 | 🧾 Description                                                                                   |
|------------------------|--------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| wrangler-api           | `ByteSize.java`, `TimeDuration.java`                                               | New token classes to parse values like "10MB", "1.5GB", "2.5s". Returns canonical values.       |
| wrangler-api           | `TokenType.java`                                                                    | Added new types `BYTE_SIZE` and `TIME_DURATION`.                                                |
| wrangler-api           | `UsageDefinition.java`, `TokenDefinition.java`                                      | Updated to support the new token types for directive argument validation.                       |
| wrangler-core          | `Directives.g4`                                                                     | Lexer and parser rules added for `BYTE_SIZE` and `TIME_DURATION`.                              |
| wrangler-core          | `RecipeVisitor.java`                                                                | Added handling for `visitByteSizeArg` and `visitTimeDurationArg`.                              |
| wrangler-core          | `AggregateStats.java`                                                               | ✨ New directive to aggregate byte size and time duration values.                                |
| wrangler-core/test     | `ByteSizeTest.java`, `TimeDurationTest.java`                                        | Tests for parsing and canonical conversions of size/duration strings.                          |
| wrangler-core/test     | `AggregateStatsDirectiveTest.java`                                                 | End-to-end tests for the new directive logic and result correctness.                           |
| wrangler-core/test     | `GrammarBasedParserTest.java`, `RecipeCompilerTest.java`                           | Added recipe tests for new grammar tokens and edge case handling.                              |

---

### 🧪 Sample Usage: `aggregate-stats`

```wrangler
aggregate-stats : inputSize, inputTime, totalSizeMB, averageTimeSec, 'MB', 'seconds', 'average'
```

| 🔢 Argument         | 📌 Description                                                                 |
|---------------------|---------------------------------------------------------------------------------|
| `inputSize`         | Column name with values like `"10KB"`, `"5MB"`                                 |
| `inputTime`         | Column name with values like `"200ms"`, `"1.2s"`                               |
| `totalSizeMB`       | Name of output column storing aggregated size in MB                            |
| `averageTimeSec`    | Name of output column storing average time in seconds                          |
| `'MB'`, `'seconds'` | (Optional) Output units (supports: `B`, `KB`, `MB`, `GB`, `ms`, `s`, `m`)       |
| `'average'`         | (Optional) Aggregation type (`total` or `average`)                             |

---

### 🚀 How It Works

1. 🧠 **Lexer Rules**: Added `BYTE_SIZE` and `TIME_DURATION` tokens in ANTLR grammar.
2. 🧰 **Token Parsers**: `ByteSize` and `TimeDuration` classes normalize units to bytes and nanoseconds.
3. 📈 **Directive Logic**: `AggregateStats` reads, aggregates, and converts values into target units.
4. ✅ **Test Suite**: Ensures parser, logic, and conversions work across multiple scenarios.

---

### 🧪 Example Input Table

| inputSize | inputTime |
|-----------|-----------|
| 5MB       | 500ms     |
| 10MB      | 1.5s      |

### 🎯 Output (with `aggregate-stats`)

| totalSizeMB | averageTimeSec |
|-------------|----------------|
| 15.0        | 1.0            |

---

### 🛠️ Build & Test

```bash
# Build and compile grammar
mvn clean install

# Run tests
mvn test
```

---

### 📎 Icons Used

- ✨ Feature Highlight  
- 📦 Module Feature  
- 📂 File Paths  
- 📄 Java Classes  
- 🔍 Parser/Logic  
- 🧪 Tests  
- 🚀 Workflow  
- 📈 Results  
