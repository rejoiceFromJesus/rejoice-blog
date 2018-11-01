package com.rejoice.blog.config;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Throwables;
import com.rejoice.blog.common.util.JsonUtil;

/**
 * 
 * @ClassName: restTemplateConfig
 * @Description: enhance resttemplate support both sync and async
 * @author rejoice 948870341@qq.com
 * @date 2016年12月29日 下午6:08:50
 *
 */
@Configuration
public class RestTemplateConfig {

	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

	private static final int DEFAULT_CONNECT_TIMEOUT_MILLISECONDS = (5 * 1000);

	private static final int DEFAULT_SOCKET_TIMEOUT_MILLISECONDS = (30 * 1000);

	private static final int DEFAULT_CONNECTION_REQUEST_MILLISECONDS = (8 * 1000);

	/*
	 * @Autowired AsyncClientHttpRequestInterceptor
	 * loggingClientHttpRequestInterceptor;
	 */

	// ################################################### SYNC
	@Bean
	public ClientHttpRequestFactory httpRequestFactory() throws Exception {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient());
		clientHttpRequestFactory.setBufferRequestBody(false);
		return clientHttpRequestFactory;
	}

	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() throws Exception {
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
		// restTemplate.setInterceptors(Arrays.asList(new RestTemplateInterceptor()));
		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
				List<MediaType> mediaTypes = new ArrayList<>();
				mediaTypes.add(MediaType.TEXT_HTML);
				mediaTypes.addAll(jsonConverter.getSupportedMediaTypes());
				jsonConverter.setSupportedMediaTypes(mediaTypes);
				jsonConverter.setObjectMapper(JsonUtil.buildObjectMapper());
			} else if (converter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
			} else if (converter instanceof FormHttpMessageConverter) {
				((FormHttpMessageConverter) converter).setCharset(StandardCharsets.UTF_8);
				((FormHttpMessageConverter) converter).setMultipartCharset(StandardCharsets.UTF_8);

			}
		}
		return restTemplate;
	}

	@Bean
	public CloseableHttpClient httpClient() throws Exception {
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				return true;
			}
		};
		/*
		 * SSLContext sslContext = SSLContexts.custom() .loadTrustMaterial(null,
		 * acceptingTrustStrategy) .build();
		 */
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sf).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
		connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("facebook.com")), 20);
		connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("twitter.com")), 20);
		connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("linkedin.com")), 20);
		connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("viadeo.com")), 20);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLISECONDS)
				.setSocketTimeout(DEFAULT_SOCKET_TIMEOUT_MILLISECONDS)
				.setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_MILLISECONDS).build();
		// final HttpHost post =  new HttpHost("192.168.56.1", 8888);
		 CloseableHttpClient defaultHttpClient = HttpClientBuilder.create().setConnectionManager(connectionManager)
				.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
				.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE).setDefaultRequestConfig(config)
				.setUserAgent(
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36")
				//.setProxy(post)
				.build();
		return defaultHttpClient;
	}

	// ################################################### ASYNC
	@Bean
	public AsyncClientHttpRequestFactory asyncHttpRequestFactory() {
		HttpComponentsAsyncClientHttpRequestFactory asyncHttpRequestFactory = new HttpComponentsAsyncClientHttpRequestFactory(
				asyncHttpClient());
		asyncHttpRequestFactory.setBufferRequestBody(false);
		return asyncHttpRequestFactory;
	}

	@Bean(name = "asyncRestTemplate")
	public AsyncRestTemplate asyncRestTemplate() throws Exception {
		AsyncRestTemplate restTemplate = new AsyncRestTemplate(asyncHttpRequestFactory(), restTemplate());
		// restTemplate.setInterceptors(Arrays.asList(loggingClientHttpRequestInterceptor));
		return restTemplate;
	}

	@Bean
	public CloseableHttpAsyncClient asyncHttpClient() {
		try {

			// ssl support start
			TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			};
			SSLContext sslcontext = SSLContextBuilder.create().loadTrustMaterial(null, acceptingTrustStrategy).build();
			Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder
					.<SchemeIOSessionStrategy>create().register("http", NoopIOSessionStrategy.INSTANCE)
					.register("https", new SSLIOSessionStrategy(sslcontext, NoopHostnameVerifier.INSTANCE)).build();
			PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(
					new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT), sessionStrategyRegistry);
			connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
			connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
			connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("facebook.com")), 20);
			connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("twitter.com")), 20);
			connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("linkedin.com")), 20);
			connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("viadeo.com")), 20);
			RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLISECONDS)
					.setSocketTimeout(DEFAULT_SOCKET_TIMEOUT_MILLISECONDS)
					.setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_MILLISECONDS).build();

			CloseableHttpAsyncClient httpclient = HttpAsyncClientBuilder.create()
					.setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
			return httpclient;
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
}
