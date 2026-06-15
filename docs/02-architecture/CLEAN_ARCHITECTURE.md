# Clean Architecture

## 目标

降低模块耦合，保证浏览引擎、手势系统、布局优化、数据存储可以独立演进。

## 依赖方向

```text
UI → Application → Domain ← Infrastructure
```

Infrastructure 实现 Domain 接口，但 Domain 不知道 Android WebView、Room、系统 DownloadManager 的存在。

## 禁止事项

- Compose 中直接调用 `webView.goBack()`。
- Repository 引用 Activity 或 Context 生命周期对象。
- WebViewClient 中写 UI 状态逻辑。
- 手势识别和业务动作混在一起。
