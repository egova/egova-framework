package com.egova.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CacheMessage implements Serializable
{
	private static final long serialVersionUID = 3079301717369779108L;

	private String cacheName;

	private Object key;

	private Integer sender;


}
