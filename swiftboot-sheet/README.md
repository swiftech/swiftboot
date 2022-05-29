# SwiftBoot-Sheet

提供一种简单、直观但是灵活的方式从表单导入数据或导出数据至模版化的表格文件（xlsx, xls, csv)。只需要给导入或导出指定数据对应的位置，而无需关心导入或导出的表格样式。换句话说，用户可以任意的修改文件模版的样式而不会影响数据的导入导出。

SwiftBoot-Sheet provides a simple, intuitive but flexible way to import data from or export data to sheet files (including xlsx, xls, csv), regardless of the style of the sheet. In other words, users can edit the style of template sheet file whatever he/she likes without affecting the import or export of data.

### 支持的文件格式：

* Microsoft Excel 97-2003
* Microsoft Excel 2007
* CSV

### 依赖

* POI 4.1.x


### 使用

* 导入导出对象的定义

你只需要定义一个 Bean 并用 `@Mapping` 注解标注需要从表格中导入或者导出数据的行列，行列位置用[表达式](#表达式)来表示，例如：

```java
public class SheetEntity {

    @Mapping("B1")
    private String value;

    @Mapping("B2:F2")
    private List<String> line;

    @Mapping("B2:C3")
    private List<List<String>> matrix;

}
```

* 导出
  创建指定文件类型的 `Exporter` ，然后调用 `export()` 方法将实体对象中的数据导出到数据表格文件中，例如：


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

> 导出文件类型有 `TYPE_XLSX`，`TYPE_XLS` 和 `TYPE_CSV`。
> `templateFileInputStream` 是导出时指定的模版文件，在这个文件里你可以任意的修改表格的样式，SwiftBoot-Sheet 只处理数据，表格样式会被原封不动的复制到 `outputStream` 中
> 如果没有模版，那么可以调用 `exporter.export(exportEntity, outputStream);` 直接导出到一个新的无样式的表格文件中。

* 导入  
  实现方式和导出类似，数据会从表格文件导入到实体对象中，例如

```java
    Importer importer = new SwiftBootSheetFactory().createImporter(SheetFileType.TYPE_XLSX);
    SheetEntity result = importer.importFromStream(fileInputStream, SheetEntity.class);

```

* 导出图片  
  支持导出 JPG, PNG 图片至 Excel 表格中的特定位置，并按照给出的范围进行伸缩，如果
  * 指定导出至一个单元格，则图片充满这个单元格
  * 指定导出至一行，一列或矩阵，则图片充满这一行，一列或矩阵
  * 指定导出至起始单元格，但是行、列有任一是不定长度的，则图片锚定起始单元格，但是宽高不受单元格限制。

示例：

```java
public class SheetEntity {
  @Mapping("A1")
  PictureLoader pictureToExport;
}
```

```java
exportEntity.setPictureToExport(() -> {
  byte[]bytesPic=... // 加载图片
  return new Picture(Workbook.PICTURE_TYPE_JPEG, bytesPic);
});
exporter.export(templateFileInputStream, exportEntity, outputStream);
```

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

```java
    SheetMetaBuilder builder = new SheetMetaBuilder();
    builder
    .items(builder.itemBuilder()
        .newItem().key("cell_0").from(0, 0).value("This is title 1")) // 直接位置设定设定导出单元格（从0开始）
        .newItem().key("cell_0").parse("B1").value("This is title 2")) // 表达式设定导出单元格
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


### 多表单(Sheet)支持

导入和导出都支持 Excel 文档中的多表格，可以通过调用相应的 API 或者在表达式中指定表单，其表达式格式和 MS Excel 的一样：`$'<sheet name>'.`，例如

```java
public class SheetEntity {

    @Mapping("$'sheet0'.B2:F2")
    private List<String> line;

    @Mapping("$'sheet1'.B2:C3")
    private List<List<String>> matrix;

}
```

直接调用底层API：

```java
    SheetMetaBuilder builder = new SheetMetaBuilder();
    builder
    .sheet("sheet 1")
    .items(builder.itemBuilder()
        .newItem().key("cell_0").from(0, 0)).value("This is title 1"))
    .sheet("sheet 2")
    .items(builder.itemBuilder()
        .newItem().key("line_0").from(0, 0).to(0, 2).value(Arrays.asList("a", "b", "c"))
        .newItem().key("column_0").parse("A2:A4").value(Arrays.asList(10, 20, 30))
    )
    .fromAnnotatedObject(exportEntity);
    SheetMeta sheetMeta = builder.build();
    Exporter exporter = new SwiftBootSheetFactory().createExporter(fileType);
    exporter.export(templateFileInputStream, sheetMeta, outputStream);
```

> 不指定表单的情况下，默认是第一个表单，且名称默认为 "Sheet 1".
> 表达式中如果包含 sheet 名称，那么会忽略 `sheet()` 方法指向的表格，而加入到它自己的表格中.


### 复制样式（导出）

对于输出位置不确定但是需要给这个单元格设定样式的情况，使用 `MetaItemBuilder` 的 `copy()` 方法可以将其他区域的单元格样式复制过来。例如：

```java
builder.items(builder.itemBuilder()
        .newItem().parse("A1").copy("A0").value("output value")) // 从 `A0` 单元格复制样式到 `A1` 单元格。
```

### 动态长度区域的样式处理（导出）

虽然我们把数据处理和表单样式完全分离了，但是对于大小不固定的区域，无法预先在模版中设置好整个区域的样式，那么我们可以用 `copy()` 来从其他单元格复制样式。
例如我们需要导出一行长度不固定的数值到区域 `A0:?0`，那么只需要设定第一个单元格 `A0` 的样式（也可以是区域之外的任意单元格），例如：

```java
builder.items(builder.itemBuilder()
        .newItem().parse("A0:?0").insert().copy("A0").value(Arrays.asList("foo", "bar"))) // 从 `A0` 单元格复制样式到第一行的若干（长度由数据长度决定）单元格
```

> 如果对于单元格样式有更灵活的要求的情况（例如根据值改变单元格的背景颜色等等），可以使用 `MetaItemBuilder.onCell()` 来直接操作 POI 的 `Cell` 对象（参考 [自定义表格处理](#自定义表格处理) ）.
> 注意：对于动态长度的 `copy()`，会与` merge()` 产生冲突，因为 `merge()` 的 value 有可能是单个值


### 自定义表格处理

虽然 SwiftBoot-Sheet 提供了简洁而强大的功能，但是有些情况下用户可能还是需要自行操作表格，例如，
```java
SheetMetaBuilder builder = new SheetMetaBuilder();
builder.sheet(0, "my first sheet")
    .handler((SheetHandler<ExcelSheetInfo>) sheetInfo -> {
        System.out.println(sheetInfo.getSheet());
    }).
    items(builder.itemBuilder()
        .newItem().key("customized").parse("B2:F5").value(matrix).onCell((ExcelCellInfo info) -> {
            System.out.printf("%s - %s - %s (%s.%s)%n", info.getWorkbook(), info.getSheet(), info.getCell(), info.getRowIdx(), info.getColIdx());
        }))
    ;
```


### Maven:

```xml
<dependency>
  <groupId>com.github.swiftech</groupId>
  <artifactId>swiftboot-sheet</artifactId>
  <version>2.2.1</version>
</dependency>
```
