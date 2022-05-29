# swiftboot-fileconvert（开发中）
文档格式转换，支持各种常见文档（MS Word，PDF，HTML，图片）的互相转换

### 依赖
* pdfbox >= 2.0.x
* iText >= 5.5.x
* pdf2dom >= 1.7.x
* POI >= 4.1.x


### 特性
* 支持多种常见文档格式互转
* 无论何种转换都使用统一的接口和调用方式
* 目前支持
	* HTML 转换为 PDF
	* PDF 转换为图片（JPEG）
	* MS Word(docx) 转换为 PDF 或图片

### Maven

```xml
<dependencies>
	<dependency>
		<groupId>com.github.swiftech</groupId>
		<artifactId>swiftboot-fileconvert</artifactId>
		<version>2.1</version>
	</dependency>
</dependencies>

```
	
### 使用方法

例如，将一个 PDF 文件转换为图片

```java
File srcDir = new File("/src/dir/");
File targetDir = new File("/target/dir/");
File srcFile = new File(srcDir, "original.pdf");
FileConverter fileConverter = new FileConverter();
// 定义转换输入源
Source src = new Source("pdf", new FileInputStream(srcFile))
// 定义转换目标（为每个转换得到的文档创建一个输出流）
Target target = new Target("jpg", new ConvertCallback() {
	@Override
	public OutputStream onPage(int i) throws Exception {
		String targetFileName = i + ".jpg";
		File targetFile = new File(targetDir, targetFileName);
		FileOutputStream outputStream = new FileOutputStream(targetFile);
		return outputStream;
	}
});
fileConverter.convert(src, target);

```