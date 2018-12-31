package test.zcj.util.json.json;

import com.zcj.util.json.json.JSONException;
import com.zcj.util.json.json.JSONObject;


public class MyJsonTest {

	public static void main(String[] args) throws JSONException {
		String jsonResult = "{myname:['zouchongjin','fsdfsdf'],mysex:'男'}";
		String result = new JSONObject(jsonResult).get("mysex").toString();
		System.out.println(result);
	}
}
