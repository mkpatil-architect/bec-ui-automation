

package com.benchmark.core.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

public class BaseJsonFunctions {

	/**
	 * This method will convert the JSON object to MAP.
	 * 
	 * @param jsonObject
	 * @param columnKey
	 * @return -> LinkedHashMap<String, Object>
	 */
	public static LinkedHashMap<String, Object> convertJSONObjectToMap(JsonObject jsonObject, String columnKey) {
		String jsonString = getJSONDataKeyValue(jsonObject, columnKey);
		Type mapType = new TypeToken<LinkedHashMap<String, Object>>() {
		}.getType();
		return new Gson().fromJson(jsonString, mapType);
	}

	/**
	 * This method will convert the JSON array to MAP.
	 * 
	 * @param jsonObject
	 * @param columnKey
	 * @return -> LinkedHashMap<String, Object>
	 */
	public static LinkedHashMap<String, Object> convertJsonArrayToMap(JsonObject jsonObject, String columnKey) {
		JsonElement jsonElement = jsonObject.get(columnKey);
		Type mapType = new TypeToken<LinkedHashMap<String, Object>>() {
		}.getType();
		return new Gson().fromJson(jsonElement, mapType);
	}

	/**
	 * This method will convert the JSONArray to
	 * LinkedHashSet<LinkedHashMap<String, Object>>
	 * 
	 * @param jsonElement
	 * @return -> LinkedHashSet<LinkedHashMap<String, Object>>
	 */
	public static LinkedHashSet<LinkedHashMap<String, Object>> convertJsonArrayToMap(JsonElement jsonElement) {
		Type mapType = new TypeToken<LinkedHashSet<LinkedHashMap<String, Object>>>() {
		}.getType();
		return new Gson().fromJson(jsonElement, mapType);
	}

	/**
	 * This method will convert JSON Element to MAP
	 * 
	 * @param jsonElement
	 * @return -> LinkedHashMap<String, Object>
	 */
	public static LinkedHashMap<String, Object> convertJsonElementToMap(JsonElement jsonElement) {
		Type mapType = new TypeToken<LinkedHashMap<String, Object>>() {
		}.getType();
		return new Gson().fromJson(jsonElement, mapType);
	}

	

	/**
	 * This method will convert JSON array items to String Array items
	 * 
	 * @param items
	 * @return -> String[]
	 */
	public static String[] convertToStringArray(JsonArray items) {
		String[] convertedItems = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
			convertedItems[i] = items.get(i).getAsString().toString();
		}
		return convertedItems;
	}

	/**
	 * This method will return Data key as JSON Array from the response.
	 * 
	 * @param content
	 * @return -> JsonArray
	 */
	public static JsonArray getJSONArrayObject(String content) {
		JsonObject jsonDataObject = getJSONObject(content);
		JsonElement dataElement = jsonDataObject.get("data");
		return dataElement.getAsJsonArray();
	}

	/**
	 * This method will process the JSON string and returns Data element as JSON
	 * Object
	 * 
	 * @param content
	 * @return -> JsonObject
	 */
	public static JsonObject getJSONDataObject(String content) {
		JsonObject jsonDataObject = getJSONObject(content);
		JsonElement dataElement = jsonDataObject.get("data");
		return dataElement.getAsJsonObject();
	}

	/**
	 * This method will return the Key value for a particular key in JSON
	 * Object.
	 * 
	 * @param jsonObject
	 * @param columnKey
	 * @return -> String
	 */
	public static String getJSONDataKeyValue(JsonObject jsonObject, String columnKey) {
		JsonElement dataElement = jsonObject.get(columnKey);
		return dataElement.toString().replace("\"", "");
	}

	/**
	 * This will process the JSON object and return JSON array.
	 * 
	 * @param jsonObject
	 * @param columnKey
	 * @return -> JsonArray
	 */
	public static JsonArray getJSONDataArray(JsonObject jsonObject, String columnKey) {
		JsonElement dataElement = jsonObject.get(columnKey);
		return dataElement.getAsJsonArray();
	}

	/**
	 * This method will parse the JSON content to JSON object.
	 * 
	 * @param content
	 * @return -> JsonObject
	 */
	public static JsonObject getJSONObject(String content) {
		JsonElement jsonElement = parseJSON(content);
		return jsonElement.getAsJsonObject();
	}

	/**
	 * This method will gets child JsonObject from the parent JsonObject.
	 * 
	 * @param jObject
	 * @param key
	 * @return JsonObject
	 */
	public static JsonObject getJSONObject(JsonObject jObject, String key) {
		return jObject.get(key).getAsJsonObject();
	}

	/**
	 * This method will parse JSON content and gets JsonElement.
	 * 
	 * @param content
	 * @return -> JsonElement
	 */
	public static JsonElement parseJSON(String content) {
		return new JsonParser().parse(content);
	}

	/**
	 * Method to convert the content into JsonArray. This method will convert
	 * the content into JsonElement and then converts to JsonArray.
	 * 
	 * @param content
	 * @return -> JsonArray.
	 */
	public static JsonArray getJsonArrayFromContent(String content) {
		JsonElement jsonElement = parseJSON(content);
		return jsonElement.getAsJsonArray();
	}

	// Generic functions

	/**
	 * Method will return the key values from data JsonObject. It will return
	 * only single values.
	 * 
	 * @param content
	 * @param keys
	 * @return
	 */
	public static Map<String, Object> getMultipleValuesFromDataObject(String content, String[] keys) {
		JsonObject jsonObject = getJSONDataObject(content);
		Map<String, Object> values = new HashMap<String, Object>();
		for (String key : keys) {
			values.put(key, getJSONDataKeyValue(jsonObject, key));
		}
		return values;
	}

	/**
	 * Method to get the GsonObject.
	 * 
	 * @return -> GsonObject.
	 */
	public static Gson gson() {
		if (m_gson == null) {
			buildGsonObject();
		}
		return m_gson;
	}

	/**
	 * Method to build the GsonObject. This method will build the GsonObject
	 * with date de-serialization.
	 * 
	 * @return -> GsonObject.
	 */
	private static void buildGsonObject() {
		try {
			m_gson = Converters.registerDateTime(new GsonBuilder()).create();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, BaseJsonFunctions.class.getName(), e.toString());
		}
	}

	/**
	 * This method will return the Key value for a particular key in JSON
	 * Object.
	 * 
	 * @param jsonObject
	 * @param columnKey
	 * @return -> String
	 */
	public static String getJsonDataKeyValue(JsonObject jsonObject, String columnKey) {
		if (jsonObject.has(columnKey)){
			return getJSONDataKeyValue(jsonObject, columnKey);
		}
		return "";
	}

	/**
	 * Holds the Gson Object.
	 */
	private static Gson m_gson = null;

	/*
	 * Back up code. Don't delete --------------------------------------------
	 * // GsonBuilder builder = new GsonBuilder(); //
	 * builder.registerTypeAdapter(Date.class, // new JsonDeserializer<Date>() {
	 * // public Date deserialize(JsonElement json, Type typeOfT, //
	 * JsonDeserializationContext context) // throws JsonParseException { //
	 * SimpleDateFormat format = new SimpleDateFormat( // "yyyy-MM-dd"); //
	 * String date = json.getAsJsonPrimitive() // .getAsString(); // try { //
	 * return format.parse(date); // } catch (ParseException e) { // return
	 * null; // } // } // }); // m_gson = builder.create();
	 * ----------------------
	 * ------------------------------------------------------ import
	 * com.google.gson.JsonParseException;import java.text.ParseException;
	 * import java.text.SimpleDateFormat; import java.util.Date; import
	 * com.google.gson.JsonDeserializationContext; import
	 * com.google.gson.JsonDeserializer;
	 */
}
