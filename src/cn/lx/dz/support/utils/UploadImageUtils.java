package cn.lx.dz.support.utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.umeng.analytics.MobclickAgent;

public class UploadImageUtils {
	private Context context;
	private String uploadToken;
	private UploadManager manager;

	private double width = 800;
	private double height = 800;
	private int quality = 80;

	private List<String> paths = new ArrayList<String>();
	private List<String> keys = new ArrayList<String>();

	public UploadImageUtils(Context context) {
		this.context = context;
		this.manager = new UploadManager();
	}

	// 设置压缩质量大小 1-100
	public void setCompressQuality(int quality) {
		if (quality >= 1 && quality <= 100) {
			this.quality = quality;
		}
	}

	public void setImageList(List<String> imgs) {
		if (imgs == null && imgs.size() <= 0) {
			LogUtils.i("-----imgs is null");
			return;
		}
		clear();
		paths.addAll(imgs);
	}

	public void clear() {
		keys.clear();
		paths.clear();
	}

	public boolean isUpload() {
		return !TextUtils.isEmpty(uploadToken);
	}

	/**
	 * @Title: startUpload
	 * @Description: TODO(开始上传)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void startUpload(OnUploadImageListener listener) {
		this.listener = listener;
		getUpToken();
	}

	/**
	 * @Title: getImageByte
	 * @Description: TODO(压缩图片并转换成bytes)
	 * @param @param imgs 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private UploadImage getImageByte(String imgs) {
		int in = imgs.indexOf("file://");
		if (in != -1) {
			imgs = imgs.substring(in + 7, imgs.length());
		}
		Bitmap bitmap = ImageUtils.getSmallBitmap(imgs, width, height);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		String key = "images/qcbv2/" + UUID.randomUUID() + "_"
				+ bitmap.getWidth() + ":" + bitmap.getHeight() + ".jpeg";
		return new UploadImage(key, baos.toByteArray());
	}

	/**
	 * @Title: doUpload
	 * @Description: TODO(上传图片到七牛)
	 * @param @param bs 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void doUpload(final UploadImage upi) {
		manager.put(upi.getImage(), upi.getKey(), uploadToken,
				new UpCompletionHandler() {
					@Override
					public void complete(String arg0, ResponseInfo arg1,
							JSONObject arg2) {
						if (arg1.isOK()) {
							MobclickAgent.onEvent(context, "upload_image");
							keys.add(upi.getKey());
							if (listener != null && keys.size() >= paths.size()) {
								listener.onSuccess(keys);
								keys.clear();
							}
						} else {
							keys.clear();
							LogUtils.i("---code----" + arg1.statusCode
									+ "---error---" + arg1.error);
							getUpToken();
							listener.onFail();
						}
					}
				}, new UploadOptions(null, null, false,
						new UpProgressHandler() {
							@Override
							public void progress(String arg0, double arg1) {
								if (listener != null) {
									int curs = 100 / paths.size();
									LogUtils.i("-----正在上传---"
											+ (int) ((arg1 * curs) + keys
													.size() * curs) + "%"
											+ "-----" + upi.getKey());
									listener.onProgress((int) ((arg1 * curs) + keys
											.size() * curs));
								}
							}
						}, null));
	}

	/**
	 * @Title: getUpToken
	 * @Description: TODO(获取七牛上传Token)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getUpToken() {
		// InitHelper.getRequest()
		// .get(Constans.getQiniuToken, new HttpResponseHandler() {
		// @Override
		// public void onSuccess(String data) {
		// try {
		// JSONObject obj = new JSONObject(data);
		// int status = obj.optInt("status");
		// if (status == 0) {
		// uploadToken = obj.optString("data");
		// LogUtils.i("uploadToken-------"+uploadToken);
		// new CompressTask().execute(paths);
		// }else{
		// listener.onFail();
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public void onFailure(int code) {
		// listener.onFail();
		// }
		// });
	}

	class CompressTask extends
			AsyncTask<List<String>, Integer, List<UploadImage>> {
		@Override
		protected void onPostExecute(List<UploadImage> result) {
			LogUtils.i("-----压缩完成,开始上传");
			for (int i = 0; i < result.size(); i++) {
				doUpload(result.get(i));
			}
		}

		@Override
		protected List<UploadImage> doInBackground(List<String>... params) {
			LogUtils.i("-------压缩图片");
			List<UploadImage> result = new ArrayList<UploadImage>();
			for (String path : params[0]) {
				result.add(getImageByte(path));
			}
			return result;
		}

	}

	private OnUploadImageListener listener;

	public void setOnUploadImageListener(OnUploadImageListener listener) {
		this.listener = listener;
	}

	public interface OnUploadImageListener {
		void onProgress(int progress);

		void onSuccess(List<String> keys);

		void onFail();
	}

	class UploadImage {
		private String key;
		private byte[] image;

		public UploadImage(String key, byte[] image) {
			super();
			this.key = key;
			this.image = image;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public byte[] getImage() {
			return image;
		}

		public void setImage(byte[] image) {
			this.image = image;
		}

	}
}
