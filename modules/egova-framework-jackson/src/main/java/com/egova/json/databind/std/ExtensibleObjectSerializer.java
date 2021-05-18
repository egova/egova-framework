package com.egova.json.databind.std;


import com.egova.associative.AssociativeExecutor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.flagwind.lang.ExtensibleObject;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 对ExtensibleObject实体序列化处理
 *
 * @author hbche
 * @date 2016年11月16日 上午9:20:56
 */
public class ExtensibleObjectSerializer extends BeanSerializerBase {

    /**
     *
     */
    private static final long serialVersionUID = 8223368352666403213L;

    /*
     * /********************************************************** /*
     * Life-cycle: constructors
     * /**********************************************************
     */


    private boolean enableAssociative = true;

    /**
     * @param builder Builder object that contains collected information
     *   that may be needed for serializer
     * @param properties Property writers used for actual serialization
     */
    public ExtensibleObjectSerializer(JavaType type, BeanSerializerBuilder builder,
                          BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, boolean enableAssociative)
    {
        super(type, builder, properties, filteredProperties);
        this.enableAssociative = enableAssociative;
    }



    protected ExtensibleObjectSerializer(BeanSerializerBase src,
                             ObjectIdWriter objectIdWriter, boolean enableAssociative) {
        super(src, objectIdWriter);
        this.enableAssociative = enableAssociative;
    }

    protected ExtensibleObjectSerializer(BeanSerializerBase src,
                             ObjectIdWriter objectIdWriter, Object filterId, boolean enableAssociative) {
        super(src, objectIdWriter, filterId);
        this.enableAssociative = enableAssociative;
    }

    protected ExtensibleObjectSerializer(BeanSerializerBase src, Set<String> toIgnore, Set<String> toInclude, boolean enableAssociative) {
        super(src, toIgnore, toInclude);
        this.enableAssociative = enableAssociative;
    }

    // @since 2.11.1
    protected ExtensibleObjectSerializer(BeanSerializerBase src,
                             BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, boolean enableAssociative) {
        super(src, properties, filteredProperties);
        this.enableAssociative = enableAssociative;
    }

    /**
     * 构造函数
     *
     * @param src 原解析对象
     */
    public ExtensibleObjectSerializer(BeanSerializerBase src, boolean enableAssociative) {
        super(src);
        this.enableAssociative = enableAssociative;
    }

    public static BeanSerializer createDummy(JavaType forType, BeanSerializerBuilder builder)
    {
        return new BeanSerializer(forType, builder, NO_PROPS, null);
    }


    @Override
    public JsonSerializer<Object> unwrappingSerializer(NameTransformer unwrapper) {
        return new UnwrappingBeanSerializer(this, unwrapper);
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
        return new ExtensibleObjectSerializer(this, objectIdWriter, _propertyFilterId, this.enableAssociative);
    }


    @Override
    public BeanSerializerBase withFilterId(Object filterId) {
        return new ExtensibleObjectSerializer(this, _objectIdWriter, filterId, this.enableAssociative);
    }


    /**
     * Implementation has to check whether as-array serialization is possible
     * reliably; if (and only if) so, will construct a
     * {@link BeanAsArraySerializer}, otherwise will return this serializer as
     * is.
     */
    @Override
    protected BeanSerializerBase asArraySerializer() {
        /*
         * Can not: - have Object Id (may be allowed in future) - have
         * "any getter" - have per-property filters
         */
        if ((_objectIdWriter == null) && (_anyGetterWriter == null) && (_propertyFilterId == null)) {
            return new BeanAsArraySerializer(this);
        }
        // already is one, so:
        return this;
    }

    /*
     * /********************************************************** /*
     * JsonSerializer implementation that differs between impls
     * /**********************************************************
     */

    /**
     * Main serialization method that will delegate actual output to configured
     * {@link BeanPropertyWriter} instances.
     */
    @Override
    public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (_objectIdWriter != null) {
            gen.setCurrentValue(bean);
            _serializeWithObjectId(bean, gen, provider, true);
            return;
        }
        gen.writeStartObject();

        gen.setCurrentValue(bean);
        if (_propertyFilterId != null) {
            serializeFieldsFiltered(bean, gen, provider);
        } else {
            serializeFields(bean, gen, provider);
        }

        ExtensibleObject entity = (ExtensibleObject) bean;

        Map<String, Object> extras = this.enableAssociative ? AssociativeExecutor.getExtrasMap(entity) : entity.getExtras();

        // 为null则序列化失败
        if (!CollectionUtils.isEmpty(extras)) {
            for (String key : extras.keySet()) {
                gen.writeObjectField(key, extras.get(key));
            }
        }

        gen.writeEndObject();
    }

    @Override // @since 2.12
    protected BeanSerializerBase withByNameInclusion(Set<String> toIgnore, Set<String> toInclude) {
        return new ExtensibleObjectSerializer(this, toIgnore, toInclude,this.enableAssociative);
    }

    @Override // @since 2.11.1
    protected BeanSerializerBase withProperties(BeanPropertyWriter[] properties,
                                                BeanPropertyWriter[] filteredProperties) {
        return new ExtensibleObjectSerializer(this, properties, filteredProperties,this.enableAssociative);
    }

    /*
     * /********************************************************** /* Standard
     * methods /**********************************************************
     */
    @Override
    public String toString() {
        return "BeanSerializer for " + handledType().getName();
    }



}
