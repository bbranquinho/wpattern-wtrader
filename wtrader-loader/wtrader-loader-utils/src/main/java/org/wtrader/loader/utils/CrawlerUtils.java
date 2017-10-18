package org.wtrader.loader.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import org.apache.log4j.Logger;

public final class CrawlerUtils {

	private static final Logger LOGGER = Logger.getLogger(CrawlerUtils.class);

	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String FROM_CONTENT_TYPE = "application/x-www-form-urlencoded";

	private CrawlerUtils() {
	}

	public static StringBuffer getRequestContent(String link) {
		return getRequestContent(link, null);
	}

	public static StringBuffer getRequestContent(String link, Proxy proxy) {
		StringBuffer response = null;

		try {
			HttpURLConnection connection = buildHttpConnection(link, proxy);

			// Optional, default is GET.
			connection.setRequestMethod("GET");

			// Add request header.
			connection.setRequestProperty("User-Agent", USER_AGENT);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);;
		}

		return response;
	}

	public static StringBuffer postRequestContent(String link, String body) throws IOException {
		return postRequestContent(link, body, null);
	}

	public static StringBuffer postRequestContent(String link, String body, Proxy proxy) throws IOException {
		HttpURLConnection con = buildHttpConnection(link, proxy);

		// Add request header.
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.addRequestProperty("Content-Type", FROM_CONTENT_TYPE);

		if (body != null) {
			con.setDoOutput(true);
			con.setRequestProperty("Content-Length", Integer.toString(body.length()));
			con.getOutputStream().write(body.getBytes("UTF8"));
		}

		// Send post request.
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response;
	}

	private static HttpURLConnection buildHttpConnection(String link, Proxy proxy) throws IOException {
		URL obj = new URL(link);

		if (proxy != null) {
			return (HttpURLConnection) obj.openConnection(proxy);
		} else {
			return (HttpURLConnection) obj.openConnection();
		}
	}

}
