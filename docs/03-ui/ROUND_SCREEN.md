# Round Screen

圆形屏幕是一等公民。

## 问题

矩形网页在圆屏上会出现四角裁切，导致按钮、文字、导航被遮挡。

## 策略

- 检测屏幕形态。
- 计算 Dynamic Safe Area。
- WebView 外层增加安全 Padding。
- Reader Mode 居中正文。
- Watch Layout Engine 针对圆屏优化列宽。

## 模式

- Auto：默认智能模式。
- Strict：保守安全边距。
- Reader：阅读优先。
- Fullscreen：最大面积，允许裁边。
