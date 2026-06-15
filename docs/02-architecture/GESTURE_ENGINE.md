# GestureEngine

GestureEngine 负责将原始触摸事件识别为产品动作。

## 流程

```text
Touch Event
    ↓
GestureDispatcher
    ↓
Recognizer
    ↓
GestureAction
    ↓
Animation
    ↓
Haptic Feedback
```

## Recognizer

- EdgeBackRecognizer。
- EdgeForwardRecognizer。
- EdgeZoomRecognizer。
- DoubleTapRecognizer。
- LongPressPieRecognizer。
- TopRevealRecognizer。
- BottomDockRecognizer。

## GestureAction

- Back。
- Forward。
- ZoomBy(delta)。
- ResetZoom。
- ShowAddressBar。
- ShowDock。
- ShowPieMenu。

## 设计规则

- 手势识别不直接操作浏览器。
- 手势只产生 Action。
- Action 由上层分发给 BrowserController 或 UI Controller。
- 所有阈值配置化。
