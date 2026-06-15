# Project Philosophy

WearBrowser 是一个开源项目，但它同时也是一个工程化产品。

## 不可妥协的标准

### 1. Watch First

禁止以“手机浏览器缩小版”的方式设计功能。

### 2. Gesture First

按钮是辅助，手势是主路径。

### 3. Content First

网页内容永远优先于浏览器 UI。

### 4. Performance is a Feature

任何新增功能都必须考虑 CPU、内存、功耗和动画流畅度。

### 5. Every Pixel Matters

手表屏幕极小，所有 UI 元素都必须有存在理由。

### 6. Architecture Before Features

新功能必须接入正确抽象，而不是直接写在 UI 层。

### 7. Privacy by Default

不上传、不跟踪、不默认同步用户浏览数据。

### 8. Open, Composable, Replaceable

BrowserEngine、GestureEngine、LayoutEngine、Repository 都应保持可替换。

## 合并标准

- 有设计说明。
- 有 Feature Flag。
- 有基础测试。
- 不破坏架构边界。
- 不引入明显性能退化。
- UI 符合 Design System。
