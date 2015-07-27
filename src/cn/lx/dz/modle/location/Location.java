package cn.lx.dz.modle.location;

import cn.lx.dz.support.database.db.annotation.Id;
import cn.lx.dz.support.database.db.annotation.Table;

/**
* @ClassName: Location 
* @Description: TODO(定位) 
* @author LiYongChun 
* @date 2015年2月28日 上午10:31:19 
*
 */
@Table(name="location")
public class Location {
	@Id
	private int id;
	/**百度定位城市*/
	private String bCity;
	/**百度定位省份*/
	private String bProvince;
	/**gcj02-纬度*/
	private double bLatitude;
	/**gcj02-经度*/
	private double bLongitude;
	/**简单城市名称*/
	private String city;
	/**简单省份名称*/
	private String province;
	/**详细地址*/
	private String address;
	/**区县信息*/
	private String district;
	/**街道信息*/
	private String street;
	/**街道号码*/
	private String streetNumber;
	/**WGS84-纬度*/
	private double latitude;
	/**WGS84-经度*/
	private double longitude;
	/**定位精度*/
	private float radius;
	/**定位时间*/
	private String time;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getbCity() {
		return bCity;
	}
	public void setbCity(String bCity) {
		this.bCity = bCity;
	}
	public String getbProvince() {
		return bProvince;
	}
	public void setbProvince(String bProvince) {
		this.bProvince = bProvince;
	}
	public double getbLatitude() {
		return bLatitude;
	}
	public void setbLatitude(double bLatitude) {
		this.bLatitude = bLatitude;
	}
	public double getbLongitude() {
		return bLongitude;
	}
	public void setbLongitude(double bLongitude) {
		this.bLongitude = bLongitude;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
