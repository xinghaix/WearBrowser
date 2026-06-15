# Watch Layout Engine

Watch Layout Engine，简称 WLE，是 WearBrowser 的核心创新模块。

## 目标

不是简单缩放网页，而是将普通网页转化为适合手表阅读和操作的布局。

## 管线

```text
DOM
    ↓
Block Detection
    ↓
Content Classification
    ↓
Importance Score
    ↓
Layout Optimization
    ↓
Safe Area Mapping
    ↓
Render / CSS Injection
```

## Block Detection

识别页面块：

- Article。
- Header。
- Navigation。
- Sidebar。
- Advertisement。
- Footer。
- Comment。
- Form。
- Main Content。

## Content Classification

为每个块计算类型和权重。

可能特征：

- 文本密度。
- 链接密度。
- 图片比例。
- 位置。
- 标签语义。
- class / id 关键词。

## Layout Optimization

针对手表屏幕进行：

- 正文居中。
- 限制列宽。
- 隐藏低价值块。
- 增加行距。
- 增大字号。
- 圆屏 Safe Area 约束。

## Kotlin 抽象

```kotlin
interface WatchLayoutEngine {
    fun optimize(request: LayoutRequest): LayoutResult
}
```

## 实施方式

初期通过 JavaScript + CSS Injection 实现。

后续可以增加站点规则和机器学习规则，但默认保持本地运行。
