package com.rejoice.blog.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
	public static ExecutorService executorService = Executors.newFixedThreadPool(10);
}
