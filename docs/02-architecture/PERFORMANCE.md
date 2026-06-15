# Performance Architecture

## 性能目标

- Edge Back 响应：< 16ms。
- Zoom Overlay 更新：60 FPS。
- Dock 显示动画：无明显掉帧。
- 切换 Tab：优先 < 120ms 显示 Snapshot。
- Reader Mode 初次处理：目标 < 300ms。
- 常规内存：尽量 < 300MB。

## 策略

- Tab Sleep。
- Snapshot Cache。
- Safe Area 仅在尺寸变化时重新计算。
- Reader 和 WLE 延迟执行。
- 历史和站点配置异步写入。
- 避免 Compose 高频重组。

## 预算意识

任何新功能需要说明：

- CPU 影响。
- 内存影响。
- 是否常驻后台。
- 是否影响首屏。
- 是否影响手势帧率。
