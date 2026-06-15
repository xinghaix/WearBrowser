# Zoom

## 范围

`50% ~ 300%`，精度 `1%`。

## 输入方式

- 双指 Pinch。
- 右侧边缘上下滑。
- 菜单 +/-。
- 双击恢复 100%。
- Fit Width。

## 状态

```kotlin
data class ZoomState(
    val percent: Int,
    val min: Int = 50,
    val max: Int = 300
)
```

## 站点记忆

每个 domain 可保存独立 zoom。

例如：

- github.com → 120%。
- wikipedia.org → 160%。
- router.local → 90%。

## 产品要求

- 缩放必须平滑。
- 不应破坏页面当前位置。
- Overlay 显示当前倍率。
- 后续支持惯性和吸附点。
