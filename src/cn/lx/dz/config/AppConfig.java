package cn.lx.dz.config;

public class AppConfig {
	public static final String APP_NAME = "dz";
	public static final String HTTP_CACHE_NAME = "dz";
	public static final String PREFERENCE_NAME = "dz";
	public static final String DB_NAME = "dz.db";

	public static final String DEVICE_TOKEN = "device_token";
	public static final String SFS = "SFS";
	
	public static final String PROVINCE = "car_province";
	public static final String CITY = "car_city";
	//小米推送
	public static final String XMAppId = "2882303761517284579";
	public static final String XMAppKey = "5981728480579";
	//post appKey
	public static final String APP_KEY = "9928f154a524e090183471e0da863246";
	// 第三方登录标示
	public static final String sina_provider = "Weibo";
	public static final String qq_provider = "QQ";
	public static final String weixin_provider = "WeiXin";
	// 用户来源 第三方登录、手机注册
	public static final String APP_SOURCE = "Dz";
	// 图片前缀
	public static final String img = "http://7punpz.com1.z0.glb.clouddn.com";
	// 250px
	public static final String webp250 = "-s.4.7.webp";
	public static final String webp1080 = "-b.5.5.webp";
	public static final String webp540 = "-m.5.5.webp";
	public static final String thumbnail = "-thumbnail";

	// 照相后图片的存放地址
	public static final String cameraPath = "/Dz/cameraImage/";
	// 头像剪切后的图片存放地址
	public static final String cropperPath = "/Dz/croppeImage/";
	// http缓存地址
	public static final String httpCachePath = "/Dz/httpCache/";
	// 图片缓存地址
	public static final String imageCachePath = "/Dz/imageCache/";

	// 查违章
	public static final String cwz_province = "province";
	// 简称
	public static final String cwz_simpleName = "simpleName";
	// 查违章选择城市后
	public static final int selectProRequestCode = 84;
}
