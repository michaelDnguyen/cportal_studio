package com.dlvn.mcustomerportal.services.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nn.tai
 * @create Nov 6, 2017
 */
public class BaseResponse {

	public static final int STATUS_OK = 200; 
	
	public static final String NO_INTERNET = "NO_INTERNET";

	//Sample generate object list with any type
//	private Object actuallyT;
//	public <T> List<T> magicalListGetter(Class<T> klazz) {
//		List<T> list = new ArrayList<>();
//		list.add(klazz.cast(actuallyT));
//		try {
//			list.add(klazz.getConstructor().newInstance()); // If default constructor
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		return list;
//	}
}

