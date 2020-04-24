//package com.egova.json.databind.std;
//
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.flagwind.lang.CodeType;
//
//import java.io.IOException;
//
///**
// * 枚举序列化
// */
//public class CodeTypeJsonSerializer extends JsonSerializer<CodeType> {
//    @Override
//    public void serialize(CodeType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            gen.writeString(value.getValue());
//    }
//}
