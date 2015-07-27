//package cn.lx.dz.support.utils;
//
//import java.util.HashMap;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.os.Handler.Callback;
//import android.os.Message;
//import cn.lx.dz.R;
//import cn.lx.dz.config.AppConfig;
//import cn.lx.dz.config.Constans;
//import cn.lx.dz.support.http.HttpResponseHandler;
//import cn.lx.dz.support.http.HttpSecret;
//import cn.lx.dz.support.http.RequestParams;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//
//import com.google.gson.Gson;
//import com.mob.tools.utils.UIHandler;
//
//
//public class LoginUtil{
//	public static final int LOGIN = 1;
//	public static final int REGISTER = 2;
//	public static final int AUTHOR_LOGIN = 3;
//	public static final int GET_VERIFY = 4;
//	public static final int CHECK_VERIFY = 5;
//	public static final int RESET_PASSWORD = 6;
//	private Context context;
//	private String  provider;
//	
//	public LoginUtil(Context context) {
//		ShareSDK.initSDK(context);
//		HttpSecret.initHttpSecret(context);
//		this.context = context;
//	}
//	public void weixinLogin(Platform weixin){
//		provider = AppConfig.weixin_provider;
//		login(weixin);
//	}
//	public void sinaLogin(Platform sina){
//		provider = AppConfig.sina_provider;
//		login(sina);
//	}
//	public void qqLogin(Platform qq){
//		provider = AppConfig.qq_provider;
//		login(qq);
//	}
//	/**
//	* @Title: login 
//	* @error: TODO(shareSSO登录) 
//	* @param @param plat    设定文件 
//	* @return void    返回类型 
//	* @throws
//	 */
//	public void login(Platform plat){
//		plat.setPlatformActionListener(new myLoginLinstener());
//		plat.SSOSetting(false);
//		plat.showUser(null);
//		LogUtils.i("login--------"+plat.getName());
//	}
//	public class myLoginLinstener implements PlatformActionListener {
//		@Override
//		public void onCancel(Platform arg0, int arg1) {
//			LogUtils.i("onCancel--------"+arg0.getName());
//			if(linstener!=null)linstener.onCancel();
//		}
//
//		@Override
//		public void onComplete(Platform arg0, int arg1,
//				HashMap<String, Object> arg2) {
//			Message msg = new Message();
//			msg.what = 1;
//			msg.obj = arg0;
//			LogUtils.i("onComplete--------"+arg0.getName());
//			UIHandler.sendMessage(msg, new logingCallBack(context));
//		}
//
//		@Override
//		public void onError(Platform arg0, int arg1, Throwable arg2) {
//			Message msg = new Message();
//			msg.what = 2;
//			msg.obj = arg2;
//			LogUtils.i("onError--------"+arg0.getName());
//			UIHandler.sendMessage(msg, new logingCallBack(context));
//			arg2.printStackTrace();
//		}
//	}
//	
//	/** 登录回调*/
//	class logingCallBack implements Callback{
//		Context context;
//		public logingCallBack(Context context) {
//			this.context = context;
//		}
//		@Override
//		public boolean handleMessage(Message msg) {
//			if(msg.what == 1){
//				Platform plat = (Platform) msg.obj;
//				RequestParams params = new RequestParams();
//				params.put("name", plat.getDb().getUserName().trim());
//				params.put("portrait", plat.getDb().getUserIcon());
//				params.put("uid", plat.getDb().getUserId());
//				params.put("provider", provider);
//				params.put("app_source", AppConfig.APP_SOURCE);
//				InitHelper.getRequest().post(Constans.oauthLogin, params, new HttpResponseHandler(){
//					@Override
//					public void onSuccess(String data) {
//						try {
//							JSONObject json = new JSONObject(data);
//							saveUserInfo(json);
//							if(linstener!=null)linstener.onSuccess(AUTHOR_LOGIN,data);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//					@Override
//					public void onFailure(int code) {
//						if(linstener!=null)linstener.onFail(AUTHOR_LOGIN,getString(R.string.register_fail));
//					}
//				});
//			}else if(msg.what == 2){
//				if(linstener!=null)linstener.onFail(AUTHOR_LOGIN,getString(R.string.author_fail));
//			}
//			return false;
//		}
//	}
//	public void userExists(String mobile){
//		RequestParams params = new RequestParams();
//		params.put("mobile", mobile);
//		
//		InitHelper.getRequest().get(Constans.userIsExist, params, new HttpResponseHandler(){
//			@Override
//			public void onSuccess(String data) {
//				
//			}
//			@Override
//			public void onFailure(int code) {
//				
//			}
//		});
//	}
//	/**
//	 * 手机登录
//	 * @param mobile
//	 * @param password
//	 */
//	public void mobileLogin(String mobile,String password){
//		RequestParams params = new RequestParams();
//		params.put("mobile", mobile);
//		params.put("password", password);
//		
//		InitHelper.getRequest().post(Constans.login, params, new HttpResponseHandler(){
//			@Override
//			public void onSuccess(String data) {
//				try {
//					JSONObject json = new JSONObject(data);
//					if(json.optInt("status") == 0){
//						saveUserInfo(json);
//						if(linstener!=null)linstener.onSuccess(LOGIN,data);
//					}else if(json.optInt("status") == -2){
//						if(linstener!=null)linstener.onFail(LOGIN,json.optString("error"));
//					}else{
//						if(linstener!=null)linstener.onFail(LOGIN,getString(R.string.login_fail));
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int code) {
//				if(linstener!=null)linstener.onFail(LOGIN,getString(R.string.login_fail));
//			}
//		});
//	}
//	/**
//	 * 手机注册
//	 * @param mobile
//	 * @param password
//	 */
//	public void mobileRegister(String mobile,String password){
//		RequestParams params = new RequestParams();
//		params.put("mobile", mobile);
//		params.put("password", password);
//		InitHelper.getRequest().post(Constans.register, params,new HttpResponseHandler() {
//			@Override
//			public void onSuccess(String data) {
//				try {
//					JSONObject json = new JSONObject(data);
//					if(json.optInt("status") == 0){
//						saveUserInfo(json);
//						if(linstener!=null)linstener.onSuccess(REGISTER,data);
//					}else if(json.optInt("status") == -2){
//						if(linstener!=null)linstener.onFail(REGISTER,json.optString("error"));
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int code) {
//				if(linstener!=null)linstener.onFail(REGISTER,getString(R.string.register_fail));
//			}
//		});
//	}
//	/** 
//	 * 获取验证码
//	 * @param phoneNum
//	 */
//	public void getVerifyCode(String phoneNum){
//		RequestParams params = new RequestParams();
//		params.put("mobile", phoneNum);
//		InitHelper.getRequest().get(Constans.getVeriftNum, params,new HttpResponseHandler() {
//			@Override
//			public void onSuccess(String data) {
//				if(linstener!=null)linstener.onSuccess(GET_VERIFY,data);
//			}
//
//			@Override
//			public void onFailure(int code) {
//				if(linstener!=null)linstener.onFail(GET_VERIFY,getString(R.string.get_verifty_fail));
//			}
//		});
//	}
//	/**
//	 * 验证 验证码
//	 * @param mobile
//	 * @param verify
//	 */
//	public void checkVerifyCode(String mobile,String verify){
//		RequestParams params = new RequestParams();
//		params.put("mobile", mobile);
//		params.put("verify_code", verify);
//		InitHelper.getRequest().get(Constans.checkValNum, params, new HttpResponseHandler(){
//			@Override
//			public void onSuccess(String data) {
//				try {
//					JSONObject json = new JSONObject(data);
//					int status = json.optInt("status");
//					if(status == 0){
//						if(linstener!=null)linstener.onSuccess(CHECK_VERIFY,data);
//					}else if(status == -2){
//						if(linstener!=null)linstener.onFail(CHECK_VERIFY,json.optString("error"));
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int code) {
//				if(linstener!=null)linstener.onFail(CHECK_VERIFY,getString(R.string.verifty_fail));
//			}
//		});
//	}
//	/**
//	 * 重置密码
//	 * @param phoneNum
//	 * @param password
//	 */
//	public void resetPassword(String phoneNum,String password){
//		RequestParams params = new RequestParams();
//		params.put("mobile", phoneNum);
//		params.put("password", password);
//		InitHelper.getRequest().post(Constans.resetPassword, params, new HttpResponseHandler(){
//			@Override
//			public void onSuccess(String data) {
//				try {
//					JSONObject json = new JSONObject(data);
//					int status = json.optInt("status");
//					if(status == 0){
//						if(linstener!=null)linstener.onSuccess(RESET_PASSWORD,data);
//					}else if(status == -2){
//						if(linstener!=null)linstener.onFail(RESET_PASSWORD,json.optString("error"));
//					}else{
//						if(linstener!=null)linstener.onFail(RESET_PASSWORD,getString(R.string.reset_password_fail));
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int code) {
//				if(linstener!=null)linstener.onFail(RESET_PASSWORD,getString(R.string.reset_password_fail));
//			}
//		});
//	}
//	public String getString(int res){
//		return context.getResources().getString(res);
//	}
//	public interface OnLoginLinstener{
//		void onSuccess(int actionCode,String data);
//		void onFail(int actionCode,String info);
//		void onCancel();
//	}
//	public OnLoginLinstener linstener;
//	public void setOnLoginLinstener(OnLoginLinstener linstener){
//		this.linstener = linstener;
//	}
//	public static void saveUserInfo(JSONObject data){
//		User user = new Gson().fromJson(data.optString("data"), User.class);
//		LogUtils.i(user.getName());
//		InitHelper.saveUser(user);
//	}
//}
