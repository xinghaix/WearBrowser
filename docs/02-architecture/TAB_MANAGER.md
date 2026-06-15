# Tab Manager

## 目标

在手表有限内存中提供可用的多标签体验。

## Tab 状态

```text
Active
Warm
Sleeping
SnapshotOnly
Destroyed
```

### Active

当前可交互 WebView。

### Warm

后台短时间保留，快速恢复。

### Sleeping

暂停 JS、暂停加载、降低资源占用。

### SnapshotOnly

释放 WebView，仅保留截图和元数据。

### Destroyed

完全释放，仅保留 URL 和历史状态。

## 策略

- 默认最多 5 个标签。
- 后台标签按时间降级。
- 内存压力下优先释放最久未访问标签。
- 切换标签优先显示 Snapshot，再异步恢复。
