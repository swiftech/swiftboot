# SwiftBoot-Sheet

SwiftBoot-Sheet provides a simple, intuitive but flexible way to import data from or export data to sheet files (including xlsx, xls, csv), regardless of the style of the sheet. In other words, users can edit the style of template sheet file whatever he/she likes without affecting the import or export of data.

### Supported file types：

* Microsoft Excel 97-2003
* Microsoft Excel 2007
* CSV

### 特性
* 多表单支持，表达式支持。
* 通过注解（Annotation）对象的属性来实现针对数据对象的导入和导出。
* 通过Builder API实现动态位置导入和导出。
* 支持图片的导入和导出，并且图片可以在导入过程中进行格式转换。
* 导入：
  * 从符合自定义条件的单元格开始导入数据。
  * 导入行数可以不指定（不能和自定义条件功能一起使用）
* 导出
  * 支持表格样式复制，无需代码实现样式写入。

### Dependencies

* POI 5.2.x


### Usage

* 导入导出对象的定义

你只需要定义一个 Bean 并用 `@Mapping` 注解标注需要从表格中导入或者导出数据的行列，例如：

```java
public class SheetEntity {

    @Mapping("B1")
    private String value;

    @Mapping("B2:F2")
    private List<String> line;

    @Mapping("B2:C3")
    private List<List<String>> matrix;

    ...

}
```

* 导出
  创建指定文件类型的 `Exporter` 并将实体对象中的数据导出到数据表格文件中，例如：

```java
    Exporter exporter = new SwiftBootSheetFactory().createExporter(SheetFileType.TYPE_XLSX);
    SheetEntity exportEntity = new SheetEntity();
    exportEntity.setValue("single cell value");
    exportEntity.setLine(Arrays.asList(1, 2));
    ...
    try {
        exporter.export(templateFileInputStream, exportEntity, outputStream);
    } catch (IOException e) {
        e.printStackTrace();
    }
```
> `templateFileInputStream` 是导出时指定的模版文件，在这个文件里你可以任意的修改表格的样式，SwiftBoot-Sheet 只处理数据，表格样式会被原封不动的复制到 `outputStream` 中
> 如果没有模版，那么可以调用 `exporter.export(exportEntity, outputStream);` 直接导出到一个新的无样式的表格文件中。

* 导入  
  实现方式和导出类似，数据会从表格文件导入到实体对象中，例如

```java
    Importer importer = new SwiftBootSheetFactory().createImporter(SheetFileType.TYPE_XLSX);
    SheetEntity result = importer.importFromStream(templateFileInputStream, SheetEntity.class, outputStream);

```

* 导出图片  
  可以导出 JPG, PNG 图片至 Excel 表格中，如果
  * 指定一个单元格，则图片充满这个单元格
  * 指定一行，一列或矩阵，则图片充满这一行，一列或矩阵
  * 指定起始单元格，但是行、列有任一是不定长度的，则图片锚定起始单元格，但是宽高不受单元格限制。

示例：

```java
public class SheetEntity {
    @Mapping("A1")
    PictureLoader pictureToExport;
}
```

```java
exportEntity.setPictureToExport(() -> {
    byte[] bytesPic = ... // 加载图片
    return new Picture(Workbook.PICTURE_TYPE_JPEG, bytesPic);
});
```

> 导出图片只支持 Excel，不支持 CSV


### 表达式
* 表达式中字母表示列，数字表示行，例如 `E2` 表示第二行第五列
* 表达式字母和数字顺序可颠倒，例如`E2` 和 `2E` 是一样的。
* 表达式中字母可为大写或小写，例如`E2` 和 `e2` 是一样的。
* 用 '-' 或 '|' 加上数字代表行或列
* 用 '?' 表示不确定长度
* 起始点和终止点的顺序可以颠倒，也就是说终止点写在起始点前面效果是一样的，最终都是由小至大。
* 导出：如果表达式的范围小于给出的数据大小，则只导出表达式给定的范围的数据。
* 对于合并单元格，只需要给出合并范围内的任一单元格位置即可。

| 表达式示例                                                      | 解释                                       |
| --------------------------------------------------------------- | ------------------------------------------ |
| `E2` 或 `2E`                                                    | 第二行第五单元格                           |
| `E2:E3`，`2:E3`，`E2:3`，`2E:3`，`2:3E`，E2&#124;3 或 2E&#124;3 | 第二行至第三行的第五个单元格               |
| `E2:H2`, `2E:H`，`E:H2`，`E2-3` 或 `2E-3`                                | 第二行的第五至第八个单元格                 |
| `E2:H3`                                                         | 第二行至第三行，第五列至第八列的所有单元格 |
| `E2:E?`，`E2:?3`, `E2:?`                                      | （仅用于导出）不定长度数据范围，取决于实际导出的数据集合的大小                                           |


### 底层 API


如果你想导入或者导出的表格位置是动态的(例如位置保存在配置文件或者数据库中），那么也可以直接调用底层的 API 来实现，例如：

* 导出

```java
    SheetMetaBuilder builder = new SheetMetaBuilder();
    builder
    .items(builder.itemBuilder()
        .newItem().key("cell_0").from(0, 0).value("This is title 1") // 直接位置设定设定导出单元格（从0开始）
        .newItem().key("cell_0").parse("B1").value("This is title 2") // 表达式设定导出单元格
        .newItem().key("line_0").from(1, 0).to(1, 2).value(Arrays.asList("a", "b", "c"))  // 位置设定导出一行数据
        .newItem().key("column_0").parse("A2:A4").value(Arrays.asList(10, 20, 30))  // 表达式设定导出一列数据
        .newItem().key("picture").parse("A5").value(pictureLoader) // 表达式设定导出图片
    )
    .fromAnnotatedObject(exportEntity);
    SheetMeta sheetMeta = builder.build();
    Exporter exporter = new SwiftBootSheetFactory().createExporter(fileType);
    exporter.export(templateFileInputStream, sheetMeta, outputStream);
```

> 默认导出的表格名称为 "Sheet 1"

* 导入
```java
builder.items(builder.itemBuilder()
    .newItem().key("GET-B2-D5").from("B2").to("D5")
    .newItem().key("GET-HEADER").predicate((Predicate<BaseCellInfo>) cellInfo ->
    "Header".equals(cellInfo.getValue()), 1, 10) // read header row
    .newItem().key("GET-CONTENT").predicate((Predicate<BaseCellInfo>) cellInfo ->
    "R001".equals(cellInfo.getValue()), null, 10) // read from predicated row to last row
    .withImages().imageConverter((Function<Picture, ?>) pic -> CryptoUtils.md5(pic.getData())) // read images anchrored to cells
);
Map<String, Object> result = importer.importFromStream(url.openStream(), builder.build());
```
>

### Maven:

  ```xml
  <dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-sheet</artifactId>
    <version>1.3-beta3</version>
  </dependency>
  ```


### 限制

* 目前只支持导入和导出表格文件的第一个工作表。
