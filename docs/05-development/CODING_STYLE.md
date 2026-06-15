# Coding Style

## 规则

- UI 不直接操作 WebView。
- Compose 中不写复杂业务逻辑。
- Repository 不访问 UI。
- Android 实现细节放在 Infrastructure。
- 领域逻辑优先纯 Kotlin。
- Feature 必须可通过 FeatureFlag 关闭。
- 命名表达意图，避免缩写。

## 禁止

- 全局静态持有 Activity。
- 手势阈值散落在代码中。
- 颜色、间距、圆角硬编码。
- 未经文档说明的大型功能合并。
