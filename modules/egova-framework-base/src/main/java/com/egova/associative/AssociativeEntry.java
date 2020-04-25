//package com.egova.associative;
//
//import com.flagwind.application.Application;
//import com.flagwind.lang.ExtensibleObject;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.Arrays;
//
///**
// * @author chendb
// * @description: 联想元数据
// * @date 2020-04-17 08:46:29
// */
//@AllArgsConstructor
//@Data
//public class AssociativeEntry {
//
//    private String name;
//    private String provider;
//    private String extras;
//    private boolean required;
//
//
//
//    public AssociativeProvider getAssociativeProvider(){
//        return Application.resolve(this.provider);
//    }
//
//    public Object getAssociateValue(Object value) {
//        AssociativeProvider provider = Application.resolve(this.provider);
//        if (StringUtils.isEmpty(extras)) {
//            return provider.associate(value);
//        } else {
//            return provider.associate(Arrays.asList(value, this.extras).toArray());
//        }
//    }
//
//    public void execute(ExtensibleObject extensibleObject, Object value) {
//        extensibleObject.set(this.name, this.getAssociateValue(value));
//    }
//}