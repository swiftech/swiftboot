# SwiftBoot-Sheet

SwiftBoot-Sheet provides a simple, intuitive but flexible way to import data from or export data to sheet files (including xlsx, xls, csv), regardless of the style of the sheet. In other words, users can edit the style of template sheet file whatever he/she likes without affecting the import or export of data.


### Supported file types：

* Microsoft Excel 97-2003
* Microsoft Excel 2007
* CSV

### Features
* Supports multiple sheets import and export.
* Supports locating cells(s) by expression.
* Mapping data object to cells(s) by annotation on object's attributes.
* Mapping cells dynamically by Builder API.
* Supports image import&export. 
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
> `templateFileInputStream` is the template file for export, you can edit style as you want, SwiftBoot-Sheet only deal with data, style will be copied as it is.
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

| Expression example                                        | Explain                                                                       |
|-----------------------------------------------------------|-------------------------------------------------------------------------------|
| `E2` 或 `2E`                                               | The fifth cell in second row                                                  |
| `E2:E3`，`2:E3`，`E2:3`，`2E:3`，`2:3E`，E2&#124;3 或 2E&#124;3 | The fifth cells from second row to third row                                  |
| `E2:H2`, `2E:H`，`E:H2`，`E2-3` 或 `2E-3`                    | The fifth cell to eighth cell in the second row                               |
| `E2:H3`                                                   | All cells from second row to third row, and fifth column to eighth column     |
| `E2:E?`，`E2:?3`, `E2:?`                                   | (for export only) uncertain size range, depends on actual size of data matrix |


### Low Level API

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

> Default sheet name in exported file is `Sheet 1`

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
> Dynamic position and reading uncertain rows can't be applied at the same time, because any read empty row is ambiguous to proceed.
> Use `imageConverter()` to define converter for read pictures, if absent, binary data (byte[]) will be read and return.

### Multiple Sheets Support

Both import and export are support multiple sheets for MS Excel document, you can use the relevant API or specify sheet in expression, the format of expression is like the one in MS Excel `$'<sheet name>'.`, eg:

```java
public class SheetEntity {

    @Mapping("$'sheet0'.B2:F2")
    private List<String> line;

    @Mapping("$'sheet1'.B2:C3")
    private List<List<String>> matrix;

}
```

Use low level API to handle multiple sheets：

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

> If no sheet specified, then first sheet will be used, and the default name is `Sheet 1`.
> If there is sheet name provided in expression, the one from `sheet()` method will be ignored.


### Copy styles（for Export）

To set styles for specific cell(s) whose position is uncertain, use  `copy()` method of `MetaItemBuilder` to copy styles from other cell. eg：

```java
builder.items(builder.itemBuilder()
        .newItem().parse("A1").copy("A0").value("output value")) // copy style from cell `A0` to cell `A1`。
```

### Styles for uncertain size area for Export）

Despite the separation of data handling and style formatting, we still can't set up styles for uncertain size cell's area, also use `copy()` to copy styles from some cell. 
For example if you need export uncertain size data arrays to a row like `A0:?0`, just set the style for first cell `A0` (or another cell out of this area):

```java
builder.items(builder.itemBuilder()
        .newItem().parse("A0:?0").insert().copy("A0").value(Arrays.asList("foo", "bar"))) // copy style from cell `A0` to cells of the first row(the size of data array determines how many columns)
```

> If more specific cases needs to be fulfilled(such as changing cell background color as per it's value), use `MetaItemBuilder.onCell()` to directly manipulate `Cell` object from POI(reference: [Custom Handler](#Custom Handler) )
> Notice: It might have conflict between methods `copy()` and `merge()` for uncertain size cells, since the value to `merge()` might be single.


### Custom Handler

Despite the SwiftBoot-Sheet provides powerful and flexible features to import/export, but still there might be some cases that users need to manipulate cells directly(through POI), if so, do it like this:

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
  <version>2.4.3</version>
</dependency>
```
