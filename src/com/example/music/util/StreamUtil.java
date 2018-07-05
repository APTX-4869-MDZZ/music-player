package com.example.music.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StreamUtil {
	public static String getStrFromStream(InputStream is) {
		String result = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuilder builder = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			result = builder.toString();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static byte[] getBytesFromStream(InputStream is) {
		ByteArrayOutputStream os = null;
		byte[] datas=null;
		try {
			os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer,0,len);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
				is.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		datas=os.toByteArray();
		return datas;
	}
}
