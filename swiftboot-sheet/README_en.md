# SwiftBoot-Sheet

SwiftBoot-Sheet provides a simple, intuitive but flexible way to import data from or export data to sheet files (including xlsx, xls, csv), regardless of the style of the sheet. In other words, users can edit the style of template sheet file whatever he/she likes without affecting the import or export of data.


### Supported file types：

* Microsoft Excel 97-2003
* Microsoft Excel 2007
* CSV

### 特性
* Supports multiple sheets import and export.
* Supports locating cells(s) by expression.
* Mapping data object to cells(s) by annotation on object's attributes.
* Mapping cells dynamically by Builder API.
* Supports image import&export, and 
* Import：
  * Customizable condition to determine from which cell the import starts. 
  * Supports reading uncertain rows(not with the customizable condition) 
  * Image can be converted(by defining functional code block) during import.
* Export:
  * Supports cell style copy, which avoids coding for cell styles.

### Dependencies

* POI 5.2.x


### Usage

* Define data object for import&export

Define a Java Bean and mapping attributes to cell(s) by annotation `@Mapping`, [expression](#Expression)，eg: 

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

* Export sheet data
  Create `Exporter` by file type, call `export()` method to export data from bean object to sheet, eg: 

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

> File types: `TYPE_XLSX`，`TYPE_XLS` 和 `TYPE_CSV`。
> `templateFileInputStream` is the template file for export, you can edit style as you want, SwiftBoot-Sheet only deal with data, style will be copied as it is
> If no template file provided, call `exporter.export(exportEntity, outputStream);` to export data into a sheet file without any styles.

* Import from sheet  
  Like the export, the data in sheet can be read to a bean object, eg: 

```java
    Importer importer = new SwiftBootSheetFactory().createImporter(SheetFileType.TYPE_XLSX);
    SheetEntity result = importer.importFromStream(fileInputStream, SheetEntity.class);

```

* Image export  
  Export JPG, PNG image to specific cell(s), and it can be auto-scaled by the cells you specified, if
  * Specify a single cell, the image will fill in the cell.
  * Specify a row, a column or a matrix, the image will be scaled to cover a row, a column or a matrix.
  * Specify starting cell with uncertain rows or columns, the image will be anchored to the staring cell, but width/height will be as it is.

eg：

```java
public class SheetEntity {
  @Mapping("A1")
  PictureLoader pictureToExport;
}
```

```java
exportEntity.setPictureToExport(() -> {
  byte[] bytesPic = ... // load picture data as byte array
  return new Picture(Workbook.PICTURE_TYPE_JPEG, bytesPic);
});
exporter.export(templateFileInputStream, exportEntity, outputStream);
```

> Only supports MS Excel.

### Expression

* Like MS Excel, the letters in expression represents columns, the digits in expression represents rows, eg: `E2` means the second row and fifth column.
* The positions of letter and digit are exchangeable, eg: `E2` and `2E` are the same.
* Letter case is not sensitive，eg: `E2` and `e2` are the same.
* `-` or `|` with digits represents row or column
* `?` uncertain length.
* The positions of start and end are exchangeable, which means the end position can be set before start position in expression, and the result remains。
* For export: if the area determined by expression is smaller than the data matrix size, only the sub-matrix of data will be exported.  
* For merged cells, give any position of cell that within the merged cell in the expression. 

| Expression example                                        | Explain                         |
|-----------------------------------------------------------|---------------------------------|
| `E2` 或 `2E`                                               | 第二行第五单元格                        |
| `E2:E3`，`2:E3`，`E2:3`，`2E:3`，`2:3E`，E2&#124;3 或 2E&#124;3 | 第二行至第三行的第五个单元格                  |
| `E2:H2`, `2E:H`，`E:H2`，`E2-3` 或 `2E-3`                    | 第二行的第五至第八个单元格                   |
| `E2:H3`                                                   | 第二行至第三行，第五列至第八列的所有单元格           |
| `E2:E?`，`E2:?3`, `E2:?`                                   | （仅用于导出）不定长度数据范围，取决于实际导出的数据集合的大小 |


### Low level API

If the position that you want to export data to is dynamic(eg: the positions saved in files or database), the low level API is ready for you to get it done. 

* Export

```java
    SheetMetaBuilder builder = new SheetMetaBuilder();
    builder
    .items(builder.itemBuilder()
        .newItem().key("cell_0").from(0, 0).value("This is title 1") // setting cell(s) by position（from 0）
        .newItem().key("cell_0").parse("B1").value("This is title 2") // setting cells(s) by expression
        .newItem().key("line_0").from(1, 0).to(1, 2).value(Arrays.asList("a", "b", "c"))  // setting row(s) by position
        .newItem().key("column_0").parse("A2:A4").value(Arrays.asList(10, 20, 30))  // setting column(s) by expression
        .newItem().key("picture").parse("A5").value(pictureLoader) // setting where image export to by expression
    )
    .fromAnnotatedObject(exportEntity);
    SheetMeta sheetMeta = builder.build();
    Exporter exporter = new SwiftBootSheetFactory().createExporter(fileType);
    exporter.export(templateFileInputStream, sheetMeta, outputStream);
```

> 默认导出的表格名称为 "Sheet 1"

* Import (since v2.4)
```java
builder.items(builder.itemBuilder()
    .newItem().key("GET-B2-D5").from("B2").to("D5")
    .newItem().key("GET-HEADER").from((Predicate<BaseCellInfo>) cellInfo ->
    "Header".equals(cellInfo.getValue()), 1, 10) // read header row
    .newItem().key("GET-CONTENT").from((Predicate<BaseCellInfo>) cellInfo ->
    "R001".equals(cellInfo.getValue()), null, 10) // read from predicated row to last row
    .withImages().imageConverter((Function<Picture, ?>) pic -> CryptoUtils.md5(pic.getData())) // read images anchrored to cells
);
Map<String, Object> result = importer.importFromStream(url.openStream(), builder.build());
```
> 动态定位读取位置和不指定读取行数不能同时使用，因为读取过程有可能遇到中间的空行而无法进行下去。
> 图片可以用 imageConverter() 来定义转换器，如果不进行转换，则直接得到图片的二进制数据（byte[])

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
        .newItem().key("cell_0").from(0, 0).value("This is title 1"))
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
        .newItem().key("customized").parse("B2:F5").value(matrix)
        .onCell((ExcelCellInfo info) -> {
            System.out.printf("%s - %s - %s (%s.%s)%n", info.getWorkbook(), info.getSheet(), info.getCell(), info.getRowIdx(), info.getColIdx());
        }))
    ;
```


### Maven:

```xml
<dependency>
  <groupId>com.github.swiftech</groupId>
  <artifactId>swiftboot-sheet</artifactId>
  <version>2.4</version>
</dependency>
```
