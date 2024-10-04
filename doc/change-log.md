# v2.2.0 [2024-03-05]
## Improvement
- 引入 `com.lucendar.common.types.Validatable`
- 引入 `DefaultValidateResult`
- `BeijingConv` 增加 `isValidStr`, `validateStr`, `nowString` 方法
- 引入 `KVList`。`KV` 增加 `of` 方法
- `com.lucendar.common.utils.DateTimeUtils.BeijingConv.strToMillis` 变更，如果输入的字符串为空字符串，将返回 null
- 引入 `UpdateKind` 枚举
- `CommonUtils`增加 `wrapIndex` 方法
## Fixed
- 将 `LinesValidateResultReceiver.invalidField` 抛出的异常代码修正为 INVALID_CONFIG，而非 INVALID_PARAM
- 修正`Reply.empty()`方法返回的 `Reply` 无 `count` 属性的 bug.
- 修正 `ErrorWithCode.invalidConfigFmt()` 方法创建不正确的错误码和信息格式。
- `DateTimeUtils` 的函数补充 @Nullable, @NonNull 注解
- `DateTimeUtils.CachedZoneOffset.cachedZoneOffset` 改为私有类成员，增加 `get` 方法
- 引入 `DateTimeUtils.BeijingConv.tsToOdt`
- `StringUtils` 增加 `emptyAsNull` 方法

# v2.1.0
## Improvement
- 新增 `Errors.OBJECT_NOT_FOUND`

# v2.0.0
## Improvement
- Maven group changed to `com.lucendar`
- Move NetUtils from `server-common` to this bundle and translate to java.
- `com.lucendar.common.utils.DateTimeUtils.BeijingConv` added `millisToOdt`, `millisToOdtBoxed`
- Introduce `Ref`
- Introduce `KV`
- Introduce `IoUtils`

# v1.0.0
- Return type of Reply.ofUpdateResult() changed to `Reply[Void]` 
