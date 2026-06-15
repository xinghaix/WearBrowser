# CI/CD

## CI 内容

- Gradle build。
- Unit test。
- Detekt。
- ktlint 或格式检查。
- 文档链接检查。

## 质量门禁

PR 不应绕过：

- 编译。
- 静态检查。
- 核心单测。

## Current CI quality gate

The current required gate is defined in `.github/workflows/android.yml` and mirrored locally by `scripts/quality-gate.sh`.

Required commands:

```bash
./gradlew detekt
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

Any pull request that changes runtime behavior should pass these checks before review.
