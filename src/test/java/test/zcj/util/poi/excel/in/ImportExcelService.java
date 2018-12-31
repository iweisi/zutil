package test.zcj.util.poi.excel.in;


public class ImportExcelService {

//	public ServiceResult importSitem(final String absoluteFile) {
//		// 读取数据
//		List<ImportExcelDto> dtoList;
//		try {
//			dtoList = ExcelUtil.getInstance().readExcelToObjsByPath(absoluteFile, ImportExcelDto.class, 0, 2, 1);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ServiceResult.initError("读取Excel失败");
//		}
//
//		// 验证数据
//		ServiceResult sr = this.dtoListVerifi(dtoList);
//		if (!sr.success()) {
//			return sr;
//		}
//
//		// 转换数据
//		List<TestUser> sList = ImportExcelDto.toEntityList(dtoList);
//
//		// 保存数据
//		return insertList(sList);
//	}
//	
//	private ServiceResult dtoListVerifi(final List<ImportExcelDto> dtoList) {
//
//		// 记录错误信息的字符串
//		String result = "";
//
//		StringBuilder sb = new StringBuilder();
//		int count = 1;
//		for (ImportExcelDto d : dtoList) {
//			
//			sb.append("第" + count + "行数据：错误。");
//			
//			count++;
//		}
//		
//		result = sb.toString();
//
//		if (StringUtils.isBlank(result)) {
//			return ServiceResult.initSuccess(null);
//		} else {
//			return ServiceResult.initError(result);
//		}
//	}
//
//	private ServiceResult insertList(List<TestUser> list) {
//		int count = (list == null ? 0 : list.size());
//		if (count > 0) {
//			for (TestUser t : list) {
//				insert(t);
//			}
//		}
//		return ServiceResult.initSuccess("新增" + count + "条。" );
//	}
	
}
