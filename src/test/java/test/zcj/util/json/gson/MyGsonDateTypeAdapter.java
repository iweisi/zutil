package test.zcj.util.json.gson;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson 时间类型适配器
 * 
 * @author ZCJ
 * @data 2012-11-8
 */
public class MyGsonDateTypeAdapter implements JsonSerializer<Date>,
		JsonDeserializer<Date> {
	private DateFormat format;

	public MyGsonDateTypeAdapter() {
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public MyGsonDateTypeAdapter(String pattern) {
		format = new SimpleDateFormat(pattern);
	}

	public JsonElement serialize(Date date, Type t, JsonSerializationContext jsc) {
		String dfString = format.format(date);
		return new JsonPrimitive(dfString);
	}

	public Date deserialize(JsonElement json, Type t,
			JsonDeserializationContext jsc) throws JsonParseException {
		if (!(json instanceof JsonPrimitive)) {
			throw new JsonParseException("The date should be a string value");
		}
		try {
			Date date = format.parse(json.getAsString());
			return date;
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}
}