# BrowserEngine

BrowserEngine 是浏览器内核抽象。

## 目标

UI 和业务逻辑不直接绑定 Android WebView，为未来替换 GeckoView、Chromium 或其他内核留下空间。

## 建议接口

```kotlin
interface BrowserEngine {
    fun load(url: String)
    fun goBack(): Boolean
    fun goForward(): Boolean
    fun reload()
    fun stopLoading()
    fun setZoom(percent: Int)
    fun resetZoom()
    fun evaluateJavascript(script: String, callback: ((String?) -> Unit)? = null)
    fun canGoBack(): Boolean
    fun canGoForward(): Boolean
    fun currentUrl(): String?
    fun currentTitle(): String?
}
```

## 实现

- `AndroidWebViewEngine`：基于系统 WebView。
- 后续可选 `GeckoViewEngine`。

## 规则

- BrowserEngine 不负责 UI。
- BrowserEngine 不持有 Compose 状态。
- BrowserEngine 只暴露浏览能力，不暴露具体 WebView 对象。
