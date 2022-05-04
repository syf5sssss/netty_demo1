package com.demo.util;

import java.util.UUID;

public class UUIDUtil {
	
	public static String GetId() {
		return UUID.randomUUID().toString().split("-")[0];
	}

}
