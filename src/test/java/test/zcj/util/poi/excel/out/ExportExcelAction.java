package test.zcj.util.poi.excel.out;

import java.io.InputStream;

/**
@ParentPackage(value="business_default")
@Namespace(value="/")
@Scope("prototype")
@Component("userAction")
**/
public class ExportExcelAction {

	// 导出
	private String path;// Excel文件相对项目的路径
	private String fileName;// 下载后显示的文件名
	private InputStream excelStream;
	
	/**
	function _export_user() {
		$.ajax({url:"<%=request.getContextPath() %>/3003",data:{},type:"post",dataType:"json", success: function(data){
	        if(data.s!=1){
	          alert(data.d);
	          return;
	        }
	        var url = "<%=request.getContextPath() %>/file_downloadExcel?path="+encodeURI(data.d.url)+"&fileName="+encodeURI(data.d.original);
	        window.location.href=url;
		}});
	}
	**/
	
	/** 生成用户的Excel */
	/**
	@Action(value = "3003")
	public String export_user() {
		// 查询数据
		Map<String, Object> qbuilder = new HashMap<String, Object>();
		List<User> stList = userService.find(null, qbuilder, null);
		List<UserDto> dtoList = UserDto.byEntityList(stList);
		
		// 通过模板生成Excel
		String absoluteFile = ServletActionContext.getServletContext().getRealPath("") + File.separator + "template" + File.separator + UserDto.FILENAME_TEMPLATE;
		String path = File.separator + "download" + File.separator + UtilString.getUUID() + ".xls";
		String downloadFile = ServletActionContext.getServletContext().getRealPath("") + path;
		try {
			ExcelUtil.getInstance().exportObjToExcelByTemplate(null, absoluteFile, downloadFile, dtoList, UserDto.class);
			jsonString = ServiceResult.initSuccessJson(new UtilFile.SuccessResult(path, UserDto.FILENAME_EXPORT, null, null));
		} catch (Exception e) {
			e.printStackTrace();
			jsonString = ServiceResult.initErrorJson("生成Excel失败");
		}
		return JSON_RESULT;
	}
	**/
	
	/** 导出Excel */
	/**
	@Action(value = "file_downloadExcel", results = { @Result(name = SUCCESS, type = "stream" , params = {
			"contentType", "application/vnd.ms-excel",
			"inputName", "excelStream",
			"contentDisposition", "attachment;filename=${fileName}",
			"bufferSize", "4096" })})
	public String downloadExcel() {
		if (StringUtils.isBlank(path) || StringUtils.isBlank(fileName)) {
			return ERROR;
		}
		try {
			excelStream = new FileInputStream(new File(ServletActionContext.getServletContext().getRealPath("")+path));
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ERROR;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	**/

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	
}
