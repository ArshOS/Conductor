package com.park.conductor.common.utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public static String getStatusFromResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("status"); // Extracts the "status" value
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }
}
