package com.lzx.compareTest.pojo;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Detail {
	public static Logger _log = LogManager.getLogger("errorFile");
	private String _url;
	private String _pc;
	private String name;
	private String price;
	private List<String> quan;
	private List<String> zhekou;
	private String $num;
	private float _unitprice;
	private float _price;
	private int _num;
	public String get_pc() {
		return _pc;
	}
	public String getName() {
		return name;
	}
	public String getPrice() {
		return price;
	}
	public List<String> getQuan() {
		return quan;
	}
	public List<String> getZhekou() {
		return zhekou;
	}
	public float get_unitprice() {
		return _unitprice;
	}
	public void set_pc(String _pc) {
		this._pc = _pc;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setQuan(List<String> quan) {
		this.quan = quan;
	}
	public void setZhekou(List<String> zhekou) {
		this.zhekou = zhekou;
	}
	public void set_unitprice(float _unitprice) {
		this._unitprice = _unitprice;
	}
	public String get_url() {
		return _url;
	}
	public void set_url(String _url) {
		this._url = _url;
	}
	
	public void calculate() {
		try {
			_price=Float.parseFloat(price);
			_num=$num==null?1:Integer.parseInt($num);
			_unitprice=_price/_num;
		}catch (Exception e) {
			_log.error(_url+":"+name+"     价格："+price+"    个数："+$num);
			throw new IllegalArgumentException("参数错误");
		}
	}
	public float get_price() {
		return _price;
	}
	public int get_num() {
		return _num;
	}
	public void set_price(float _price) {
		this._price = _price;
	}
	public void set_num(int _num) {
		this._num = _num;
	}
	public String get$num() {
		return $num;
	}
	public void set$num(String $num) {
		this.$num = $num;
	}
	
	
}
