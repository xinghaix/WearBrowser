# Architecture Overview

WearBrowser 采用分层架构，目标是保证长期演进能力。

```text
Presentation
    ↓
Application
    ↓
Domain
    ↓
Infrastructure
```

## 层职责

### Presentation

- Compose UI。
- 手势入口。
- 页面状态展示。
- 不直接操作 WebView。

### Application

- UseCase。
- ViewModel。
- 状态编排。
- Feature Flag 判断。

### Domain

- BrowserEngine 接口。
- GestureEngine 模型。
- Tab 生命周期模型。
- Zoom、SafeArea、SiteProfile 领域逻辑。

### Infrastructure

- Android WebView 实现。
- Room 数据库。
- DownloadManager。
- Android 系统能力。

## 核心规则

- UI 不直接依赖 Android WebView。
- 数据层不依赖 UI。
- 实验功能必须通过 FeatureFlag 控制。
- 新功能必须先定义领域模型，再接入实现。
