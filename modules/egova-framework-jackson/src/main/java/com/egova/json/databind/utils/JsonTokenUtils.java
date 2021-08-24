package com.egova.json.databind.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

public class JsonTokenUtils {

    public static String getParseValue(JsonParser parser, String name) throws IOException {
        String value = null;
        JsonToken jsonToken = parser.currentToken();
        if (JsonToken.START_OBJECT.equals(jsonToken)) {

            // 解析 object
            while ((!parser.isClosed()) && (!JsonToken.END_OBJECT.equals(jsonToken))) {
                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = parser.getCurrentName();
                    if (fieldName.equalsIgnoreCase(name)) {

                        jsonToken = parser.nextToken();
                        if (JsonToken.VALUE_NUMBER_INT.equals(jsonToken)
                                || JsonToken.VALUE_NUMBER_FLOAT.equals(jsonToken)
                                || JsonToken.VALUE_TRUE.equals(jsonToken)
                                || JsonToken.VALUE_FALSE.equals(jsonToken)
                                || JsonToken.VALUE_STRING.equals(jsonToken)
                        ) {
                            value = parser.getText();
                        }
                    }
                }
                jsonToken = parser.nextToken();
            }
        } else if (JsonToken.VALUE_NUMBER_INT.equals(jsonToken)
                || JsonToken.VALUE_NUMBER_FLOAT.equals(jsonToken)
                || JsonToken.VALUE_TRUE.equals(jsonToken)
                || JsonToken.VALUE_FALSE.equals(jsonToken)
                || JsonToken.VALUE_STRING.equals(jsonToken)

        ) {
            value = parser.getText();
        } else if (JsonToken.START_ARRAY.equals(jsonToken)) {
            value = "";
            while ((!parser.isClosed()) && (!JsonToken.END_ARRAY.equals(jsonToken))) {
                if (!"[".equalsIgnoreCase(value) && value.length() > 1) {
                    value += ",";
                }
                value += "\""+parser.getText()+"\"";
                jsonToken = parser.nextToken();
            }
            value += "]";
        }
        return value;
    }
}
