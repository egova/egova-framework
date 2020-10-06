package com.egova.json.databind.std;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.flagwind.lang.CodeType;

import java.io.IOException;

/**
 * CodeType序列化
 */
public class TextValueJsonSerializer extends JsonSerializer<CodeType> {


    @Override
    public void serialize(CodeType value, JsonGenerator gen, SerializerProvider serializers) throws IOException
    {
        gen.writeStartObject();                                      //   "{"
        gen.writeStringField("value",value.getValue());
        gen.writeStringField("text",value.getText());
        gen.writeEndObject();                                        //
    }
}
