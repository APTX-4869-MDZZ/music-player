package com.example.music.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.music.util.StreamUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder.OutputFormat;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class MyImageLoader {
	/**
	 * �������Ҫ����һ��ר��ͼƬ�������ȴ��ڴ��л�ȡ ����ڴ���û��Ҫʹ�õ�ͼƬ�������ٴ��ļ������� ��ȡ������ļ�������Ҳû�У�˵����ͼƬ����û��
	 * �������ϼ��ع�����ô���Ǿʹ��������첽���أ����� ����֮���ٽ�ͼƬ�ֱ𻺴����ڴ���ļ��С������ ʹ�õ�ʱ��Ϳ������û����е������ˡ�
	 */
	public static LruCache<String, Bitmap> lruCacheMemory = null;
	static {
		int maxsize = 1024 * 1024 * 4;// 4MB
		lruCacheMemory = new LruCache<String, Bitmap>(maxsize) {
			// �������ÿ����Ŀ�Ĵ�С
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * ��ͼ�ӻ����л��ͼƬӦ�õ�ͼƬ�ؼ���
	 * 
	 * @param context
	 * @param imageview
	 *            ����ͼƬ�Ŀؼ�
	 * @param imageurl
	 *            ͼƬ��·��
	 */
	public static void setBitmapFromCache(Context context, ImageView imageview, String imageurl) {

		Bitmap bitmap = null;// �ӻ������õ���ͼƬ
		// �ж��ṩ��ͼƬ��·���Ƿ����
		// ���������ֱ�ӷ���
		if (TextUtils.isEmpty(imageurl)) {
			return;
		}
		// �ȴ��ڴ滺���л��ͼƬ
		bitmap = getBitmapFromMemory(imageurl);
		if (bitmap != null) {
			// �ڴ����������ֱ��ʹ��
			imageview.setImageBitmap(bitmap);
			return;
		}
		// ����ڴ���û���ҵ�ͼƬ
		// �ٴ��ļ������в���ͼƬ
		bitmap = getBitmapFromFile(context, imageurl);
		if (bitmap != null) {
			// ֱ��ʹ��ͼƬ
			imageview.setImageBitmap(bitmap);
			return;
		}
		// ����ļ�������Ҳû��Ҫʹ�õ�ͼƬ
		// ����Ҫ���������첽����
		loadBitmapFromHttp(context, imageview, imageurl);
	}

	private static void loadBitmapFromHttp(
			Context context, 
			ImageView imageview,
			String imageurl) {	
		//�첽����
		MyAsyncTask task=new MyAsyncTask(context, imageview);
		task.execute(imageurl);
	}

	public static class MyAsyncTask extends AsyncTask<String, Void, Bitmap>{
		Context context=null;
		ImageView imageview=null;
		
		public MyAsyncTask(Context context,ImageView imageview) {
			this.context=context;
			this.imageview=imageview;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			//�첽����ͼƬ
			Bitmap bitmap=null;
			String path=params[0];
			Log.i("TAG:url", path);
			HttpURLConnection connection=null;
			try {
				URL url=new URL(path);
				try {
					connection=(HttpURLConnection) url.openConnection();
					
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					
					connection.setDoInput(true);
					connection.connect();
					
					int statuscode=connection.getResponseCode();
					if(statuscode==200){
						InputStream is=connection.getInputStream();
						bitmap=compressBitmap(is);
						//����������ת����һ��bitmap
						//bitmap=BitmapFactory.decodeStream(is);
						Log.i("TAG:bitmap", bitmap+"");
						if(bitmap!=null){
							//��ͼƬ���浽�ڴ���
							lruCacheMemory.put(path, bitmap);
							//��ͼƬ���浽�ļ���
							saveBitmaptoFile(context,bitmap,path);
							return bitmap;
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			Log.i("TAG:bitmap", result+"");
			imageview.setImageBitmap(result);
		}
	}
	
	
	private static Bitmap getBitmapFromFile(Context context, String imageurl) {
		Bitmap bitmap=null;
		String filename = imageurl.substring(imageurl.lastIndexOf("/") + 1);
		// �Ȼ��Ӧ�õĻ���Ŀ¼
		File cacheDir = context.getCacheDir();
		// ������Ŀ¼�����ܵ��ļ�
		// �ж����Ƿ���Ҫ���ҵ��ļ�
		// ����ǵĻ��Ѹ��ļ�ת����һ��bitmap
		if (cacheDir != null) {
			File[] files = cacheDir.listFiles();
			for (File file : files) {
				if(file.isFile()){
					String name=file.getName();
					if(name.equals(filename)){
						//���ҵ������ļ���
						//�Ѹ��ļ�ת����bitmap����
						bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
						return bitmap;
					}
				}
			}
		}
		return null;
	}
	public static Bitmap compressBitmap(InputStream is) {
		
		byte[] datas=StreamUtil.getBytesFromStream(is);
		BitmapFactory.Options opts=new BitmapFactory.Options();
		//��ѡ������Ϊֻ����ͼƬ�ı߽���
		opts.inJustDecodeBounds=true;
		BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
		//���ͼƬ�߽�Ŀ�Ⱥ͸߶�
		int outHeight=opts.outHeight;
		int outWidth=opts.outWidth;
		//����ͼƬ��ѹ����Ŀ��Ŀ��
		int targetHeight=60;//Ŀ��߶�
		int targetWidth=60;//Ŀ��Ŀ��
		
		//�������
		int blw=outWidth/targetWidth;//��ȷ����ϵı���
		int blh=outHeight/targetHeight;//�߶ȷ����ϵı���
		
		int bl=blw>blh?blw:blh;
		if(bl<=0){
			bl=1;
		}
		opts.inSampleSize=bl;
		
		opts.inJustDecodeBounds=false;
		Bitmap bitmap=BitmapFactory.decodeByteArray(datas, 0, datas.length,opts);
		return bitmap;
	}

	/**
	 * ��������ػ�õ�ͼƬ���浽�ļ���
	 * @param context
	 * @param bitmap
	 * @param path
	 */
	public static void saveBitmaptoFile(
			Context context, 
			Bitmap bitmap, 
			String path) {
		try {
			File cacheDir = context.getCacheDir();
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			//���Ҫ������ļ�������
			String filename = path.substring(path.lastIndexOf("/") + 1);
			//���建���ļ�����
			File file = new File(cacheDir, filename);
			OutputStream os = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ڴ��л��ͼƬ
	 * 
	 * @param imageurl
	 * @return
	 */
	private static Bitmap getBitmapFromMemory(String imageurl) {
		Bitmap bitmap = lruCacheMemory.get(imageurl);
		return bitmap;
	}
}
