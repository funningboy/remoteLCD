package com.example.remote1602lcd;
import org.json.JSONObject;
import org.json.JSONException;


// pack context table as json stream
public class JsonUtil {
	public static String toJSon(Context context) {
		try {
			JSONObject jsonTable = new JSONObject();
			jsonTable.put("STR1", context.getStr1().getStr());
			jsonTable.put("STR2", context.getStr2().getStr());
			return jsonTable.toString();
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}