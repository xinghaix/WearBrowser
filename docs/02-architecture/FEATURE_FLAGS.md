# Feature Flags

实验功能必须通过 Feature Flag 控制。

## 目标

- 便于灰度。
- 便于排查问题。
- 避免未成熟功能影响稳定体验。

## 示例

```kotlin
object FeatureFlags {
    const val EDGE_ZOOM = true
    const val PIE_MENU = true
    const val WATCH_LAYOUT_ENGINE = false
    const val READER_MODE = true
    const val TAB_SLEEP = true
}
```

## 规则

- 新实验功能默认应可关闭。
- 文档中必须说明功能状态：Stable / Beta / Experimental。
- Release 版本不应默认开启高风险功能。
