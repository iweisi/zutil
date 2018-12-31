package test.zcj.util.convertor;

import org.junit.Test;

import com.zcj.util.convertor.OfficeToPdf2;
import com.zcj.util.convertor.PdfToJpg;
import com.zcj.util.convertor.PdfToSwf;

public class TestConvertor {

	@Test
	public void toPdf() {
		System.out.println(OfficeToPdf2.toPdf("F:/AAA/p53_kjsch/Document/A.doc"));
	}

	@Test
	public void pdf2Jpg() {
		System.out.println(PdfToJpg.pdf2Jpg("F:/AAA/p53_kjsch/Document/A.pdf"));
	}

	@Test
	public void pdf2Swf() {
		System.out.println(PdfToSwf.pdf2swf("C:/Program Files (x86)/SWFTools", "F:/AAA/p53_kjsch/Document/A.pdf",
				"F:/AAA/p53_kjsch/Document/A.swf"));
	}

}
