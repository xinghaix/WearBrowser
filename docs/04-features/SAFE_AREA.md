# Safe Area

## 目标

防止圆屏裁切网页核心内容。

## 输入

- 屏幕宽高。
- 是否圆屏。
- 当前模式。
- 当前缩放。

## 输出

- top padding。
- bottom padding。
- left padding。
- right padding。

## 模式

- Auto。
- Strict。
- Reader。
- Fullscreen。

## 规则

Safe Area 只在尺寸或模式变化时重新计算，避免性能浪费。
