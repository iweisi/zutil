package test.zcj.util.poi.excel.in;

import java.io.File;

//@ParentPackage(value = "business_default")
//@Namespace(value = "/")
//@Scope("prototype")
//@Component("sitemAction")
public class ImportExcelAction {

	private File excelFile;// 文件
	private String excelFileContentType;// 文件类型
	private String excelFileFileName;// 文件名
	
//	<form id="saveform" method="post" enctype="multipart/form-data">
//	<input type="file" name="excelFile">
	
//	@Action("sitem3004")
//	public String importExcel() {
//		// 上传Excel文件
//		String absoluteFile = ServletActionContext.getServletContext().getRealPath("") + File.separator + "upload" + File.separator + UtilString.getSoleCode() + "-" + excelFileFileName;
//		try {
//			FileUtils.copyFile(excelFile, new File(absoluteFile));
//		} catch (IOException e) {
//			e.printStackTrace();
//			jsonString = ServiceResult.initErrorJson("文件上传失败");
//			return JSON_RESULT;
//		}
//
//		// 导入Excel数据
//		ServiceResult sr_import = sitemService.importSitem(absoluteFile);
//		jsonString = ServiceResult.GSON_DT.toJson(sr_import);
//		return JSON_RESULT;
//	}

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public String getExcelFileContentType() {
		return excelFileContentType;
	}

	public void setExcelFileContentType(String excelFileContentType) {
		this.excelFileContentType = excelFileContentType;
	}

	public String getExcelFileFileName() {
		return excelFileFileName;
	}

	public void setExcelFileFileName(String excelFileFileName) {
		this.excelFileFileName = excelFileFileName;
	}
	
}
