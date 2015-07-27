package cn.lx.dz.support.location;

import android.content.Context;
import cn.lx.dz.config.InitHelper;
import cn.lx.dz.modle.location.Gps;
import cn.lx.dz.modle.location.Location;
import cn.lx.dz.support.utils.DateTimeUtils;
import cn.lx.dz.support.utils.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * @ClassName: BdLocation
 * @Description: TODO(百度定位)
 * @author LiYongChun
 * @date 2015年2月28日 上午11:44:38
 *
 */
public class BdLocation {
	private Context context;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private OnLocationLinstener linstener;
	/** 重试次数 */
	private int rNum = 0;
	/** 定位失败是否获取最后一次定位信息 */
	private boolean isGetCache = false;

	public BdLocation(Context context) {
		this.context = context;
		mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(2000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener);
	}

	/**
	 * @Title: requestLocation
	 * @Description: TODO(发起定位请求)
	 * @param @param isGetCache 定位失败，是否获取最后一次定位信息
	 * @param @param linstener 定位监听
	 * @return void 返回类型
	 * @throws
	 */
	public void requestLocation(boolean isGetCache,
			OnLocationLinstener linstener) {
		this.isGetCache = isGetCache;
		this.linstener = linstener;
		if (linstener != null)
			this.linstener.onStart();
		mLocationClient.start();// 启动SDK定位
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
	}

	public void requestLocation(OnLocationLinstener linstener) {
		requestLocation(isGetCache, linstener);
	}

	public void requestLocation() {
		requestLocation(false, null);
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// 定位不成功，重试两次
			if ((location == null || location.getCity() == null) && rNum < 2) {
				mLocationClient.requestLocation();
				rNum++;
				return;
			}
			rNum = 0;
			mLocationClient.stop();
			int code = location.getLocType();
			Location sl = InitHelper.getDb().findById(Location.class, 1);
			if (code == 61 || code == 161) {
				InitHelper.getDb().dropTable(Location.class);
				Location bsl = getSimpleLocation(location);
				InitHelper.getDb().save(bsl);
				if (linstener != null)
					linstener.onLocationSuccess(bsl);
			} else {
				if (sl != null && isGetCache) {
					InitHelper.getDb().dropTable(Location.class);
					sl.setTime(DateTimeUtils
							.getCurrentTimeInString(DateTimeUtils.DEFAULT_DATE_FORMAT));
					InitHelper.getDb().save(sl);
					if (linstener != null)
						linstener.onLocationSuccess(sl);
				} else {
					if (linstener != null)
						linstener.OnLocationFail(code);
				}
			}
		}
	}

	public interface OnLocationLinstener {
		/**
		 * 启动
		 */
		void onStart();

		/**
		 * 定位成功
		 */
		void onLocationSuccess(Location location);

		/**
		 * 定位失败
		 */
		void OnLocationFail(int errorCode);
	}

	public Location getSimpleLocation(BDLocation location) {
		Location sl = new Location();
		sl.setId(1);
		sl.setbCity(location.getCity());
		sl.setbProvince(location.getProvince());
		sl.setbLatitude(location.getLatitude());
		sl.setbLongitude(location.getLongitude());
		sl.setCity(TextUtils.getSimpleLocation(location.getCity()));
		sl.setProvince(TextUtils.getSimpleLocation(location.getProvince()));
		Gps gps = PositionUtil.gcj02ToGps84(location.getLatitude(),
				location.getLongitude());
		sl.setLatitude(gps.getWgLat());
		sl.setLongitude(gps.getWgLon());
		sl.setAddress(location.getAddrStr());
		sl.setDistrict(location.getDistrict());
		sl.setStreet(location.getStreet());
		sl.setStreetNumber(location.getStreetNumber());
		sl.setRadius(location.getRadius());
		sl.setTime(location.getTime());
		return sl;
	}
}
