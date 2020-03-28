package com.egova.json.databind.std;

import com.egova.json.databind.PropertyNaming;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class PropertyNamingIntrospector extends JacksonAnnotationIntrospector implements Versioned
{
	@Override
	public Version version()
	{
		return VersionUtil.versionFor(getClass());
	}


	@Override
	public PropertyName findNameForSerialization(Annotated a)
	{
		PropertyNaming n = _findAnnotation(a, PropertyNaming.class);
		if(n != null && n.access() != PropertyNaming.Access.Read)
		{
			return null;
		}
		return super.findNameForSerialization(a);
	}


	@Override
	public PropertyName findNameForDeserialization(Annotated a)
	{
		PropertyNaming n = _findAnnotation(a, PropertyNaming.class);
		if(n != null && n.access() != PropertyNaming.Access.Write)
		{
			return PropertyName.construct(n.value());
		}
		return super.findNameForDeserialization(a);
	}

}
