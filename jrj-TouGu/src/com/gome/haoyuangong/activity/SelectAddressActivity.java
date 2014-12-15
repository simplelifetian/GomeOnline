package com.gome.haoyuangong.activity;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.tencent.android.tpush.horse.p;

public class SelectAddressActivity extends ListViewActivity {
	List<String> items;
	String city;
	static Map<String, List<String>> provinceMap;
	List<String> provincItems;
	List<String> cityItems;
	int flag;
	String province="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		provincItems = new ArrayList<String>();
		cityItems = new ArrayList<String>();
		provinceMap = new HashMap<String, List<String>>();
		flag = getIntent().getIntExtra("flag", 1);
		if (getIntent().getStringExtra("province") != null){
			province = getIntent().getStringExtra("province");
		}
		if (provinceMap.size() == 0)
			addAddress();	
		
//		addListViewHeadView(getHeadView());
		
		if (flag == 1){
			getProvinceData();
			setPinYinDatas(provincItems);
			addItems(provincItems);
		}
		else{
			getCityData(province);
			setPinYinDatas(provincItems);
			addItems(cityItems);
		}
		reFresh();
		setTitle(getIntent().getStringExtra("title"));
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		setDividerHeight(0);
	}
	private void getProvinceData() {
		provincItems.clear();
		for(String key:provinceMap.keySet()){
			provincItems.add(key);
		}
	}
	private void getCityData(String province) {
		cityItems.clear();
		for(String value:provinceMap.get(province)){
			cityItems.add(value);
		}
	}
	private LinearLayout getHeadView() {
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(0xe9e9e9);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 66));
		TextView tView = new TextView(this);
		tView.setText("热门城市");
		tView.setBackgroundColor(getResources().getColor(R.color.background_ECECEC));
		tView.setPadding(Function.getFitPx(this, 42), 0, 0, 0);
		tView.setTextColor(getResources().getColor(R.color.font_727272));
		tView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 36));
		tView.setGravity(Gravity.CENTER_VERTICAL);
		layout.addView(tView,params);
		
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 120));
		params.setMargins(0, 0, 0, 1);
		BarItem item = createBarItem("北京");
		layout.addView(item,params);
		item = createBarItem("上海");
		layout.addView(item,params);
		item = createBarItem("广州");
		layout.addView(item,params);
		item = createBarItem("重庆");
		layout.addView(item,params);
		item = createBarItem("杭州");
		layout.addView(item,params);
		item = createBarItem("深圳");
		layout.addView(item,params);
		item = createBarItem("天津");
		layout.addView(item,params);
		item = createBarItem("南京");
		layout.addView(item,params);
		return layout;
	}
	private BarItem createBarItem(String title){
		final BarItem item = new BarItem(getContext());	
		item.setTitleFontSize(46);
		item.setTitle(title);
		item.setRightArrowVisible(View.INVISIBLE);
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent newIntent = new Intent();
				
				newIntent.putExtra("returnvalue", item.getTitle());
				setResult(RESULT_OK, newIntent);
				finish();
			}
		});
		return item;
	}
	private void addItems(List<String> list){
		for(int i=0;i<list.size();i++){			
			LinearLayout layout = new LinearLayout(this);
			layout.setBackgroundColor(0xffe9e9e9);
			final BarItem item = new BarItem(this);
			item.setRightArrowVisible(View.INVISIBLE);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
			item.setTitle(list.get(i));
			item.setTitleFontColor(getResources().getColor(R.color.font_595959));
			p.setMargins(0, 0, 0, 1);
			layout.addView(item,p);
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent newIntent = new Intent();
					if (flag == 1){
						newIntent.putExtra("title", "选择城市");
						newIntent.putExtra("flag", 2);
						newIntent.putExtra("province", item.getTitle());
						newIntent.setClass(SelectAddressActivity.this, SelectAddressActivity.class);
						startActivityForResult(newIntent, 1001);
					}
					else{
//						newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						newIntent.putExtra("province", province);
						newIntent.putExtra("addressok", true);
						newIntent.putExtra("city", item.getTitle());
						newIntent.putExtra("returnvalue", province+" "+item.getTitle());
						setResult(RESULT_OK, newIntent);
						finish();
					}
				}
			});
			addItem(layout);
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null && arg0 == 1001){
			setResult(RESULT_OK, arg2);
			finish();
		}
	}
	private void addAddress() {
		items = new ArrayList<String>();
		items.add("北京");
		provinceMap.put("北京", items);
		items = new ArrayList<String>();
		items.add("上海");
		provinceMap.put("上海", items);
		items = new ArrayList<String>();
		items.add("天津");
		provinceMap.put("天津", items);
		items = new ArrayList<String>();
		items.add("重庆");
		provinceMap.put("重庆", items);
		items = new ArrayList<String>();
		items.add("哈尔滨市");
		items.add("齐齐哈尔市");
		items.add("鸡西市");
		items.add("鹤岗市");
		items.add("双鸭山市");
		items.add("大庆市");
		items.add("伊春市");
		items.add("佳木斯市");
		items.add("七台河市");
		items.add("牡丹江市");
		items.add("黑河市");
		items.add("绥化市");
		items.add("大兴安岭地区");
		provinceMap.put("黑龙江省", items);
		
		items = new ArrayList<String>();
		items.add("长春市");
		items.add("吉林市");
		items.add("四平市");
		items.add("辽源市");
		items.add("通化市");
		items.add("白山市");
		items.add("松原市");
		items.add("白城市");
		items.add("延边朝鲜族自治州");
		provinceMap.put("吉林省", items);
		
		items = new ArrayList<String>();
		items.add("沈阳市");
		items.add("大连市");
		items.add("鞍山市");
		items.add("抚顺市");
		items.add("本溪市");
		items.add("丹东市");
		items.add("锦州市");
		items.add("营口市");
		items.add("阜新市");
		items.add("辽阳市");
		items.add("盘锦市");
		items.add("铁岭市");
		items.add("朝阳市");
		items.add("葫芦岛市");
		provinceMap.put("辽宁省", items);
		
		items = new ArrayList<String>();
		items.add("济南市");
		items.add("青岛市");
		items.add("淄博市");
		items.add("枣庄市");
		items.add("东营市");
		items.add("烟台市");
		items.add("潍坊市");
		items.add("济宁市");
		items.add("泰安市");
		items.add("威海市");
		items.add("日照市");
		items.add("莱芜市");
		items.add("临沂市");
		items.add("德州市");
		items.add("聊城市");
		items.add("滨州市");
		items.add("菏泽市");
		provinceMap.put("山东省", items);
		
		items = new ArrayList<String>();
		items.add("太原市");
		items.add("大同市");
		items.add("阳泉市");
		items.add("长治市");
		items.add("晋城市");
		items.add("朔州市");
		items.add("晋中市");
		items.add("运城市");
		items.add("忻州市");
		items.add("临汾市");
		items.add("吕梁市");
		provinceMap.put("山西省", items);
		
		items = new ArrayList<String>();
		items.add("西安市");
		items.add("铜川市");
		items.add("宝鸡市");
		items.add("咸阳市");
		items.add("渭南市");
		items.add("延安市");
		items.add("汉中市");
		items.add("榆林市");
		items.add("安康市");
		items.add("商洛市");
		provinceMap.put("陕西省", items);
		
		items = new ArrayList<String>();
		items.add("石家庄市");
		items.add("唐山市");
		items.add("秦皇岛市");
		items.add("邯郸市");
		items.add("邢台市");
		items.add("保定市");
		items.add("张家口市");
		items.add("承德市");
		items.add("沧州市");
		items.add("廊坊市");
		items.add("衡水市");
		provinceMap.put("河北省", items);
		
		items = new ArrayList<String>();
		items.add("郑州市");
		items.add("开封市");
		items.add("洛阳市");
		items.add("平顶山市");
		items.add("安阳市");
		items.add("鹤壁市");
		items.add("新乡市");
		items.add("焦作市");
		items.add("济源市");
		items.add("濮阳市");
		items.add("许昌市");
		items.add("漯河市");
		items.add("三门峡市");
		items.add("南阳市");
		items.add("商丘市");
		items.add("信阳市");
		items.add("周口市");
		items.add("驻马店市");
		provinceMap.put("河南省", items);
		
		items = new ArrayList<String>();
		items.add("武汉市");
		items.add("黄石市");
		items.add("十堰市");
		items.add("宜昌市");
		items.add("襄樊市");
		items.add("鄂州市");
		items.add("荆门市");
		items.add("孝感市");
		items.add("荆州市");
		items.add("黄冈市");
		items.add("咸宁市");
		items.add("随州市");
		items.add("恩施土家族苗族自治州");
		items.add("仙桃市");
		items.add("潜江市");
		items.add("天门市");
		items.add("神农架林区");
		provinceMap.put("湖北省", items);
		
		items = new ArrayList<String>();
		items.add("长沙市");
		items.add("株洲市");
		items.add("湘潭市");
		items.add("衡阳市");
		items.add("邵阳市");
		items.add("岳阳市");
		items.add("常德市");
		items.add("张家界市");
		items.add("益阳市");
		items.add("郴州市");
		items.add("永州市");
		items.add("怀化市");
		items.add("娄底市");
		items.add("湘西土家族苗族自治州");
		provinceMap.put("湖南省", items);
		
		items = new ArrayList<String>();
		items.add("海口市");
		items.add("三亚市");
		items.add("五指山市");
		items.add("琼海市");
		items.add("儋州市");
		items.add("文昌市");
		items.add("万宁市");
		items.add("东方市");
		items.add("定安县");
		items.add("屯昌县");
		items.add("澄迈县");
		items.add("临高县");
		items.add("白沙黎族自治县");
		items.add("昌江黎族自治县");
		items.add("乐东黎族自治县");
		items.add("陵水黎族自治县");
		items.add("保亭黎族苗族自治县");
		items.add("琼中黎族苗族自治县");
		provinceMap.put("海南省", items);
		
		items = new ArrayList<String>();
		items.add("南京市");
		items.add("无锡市");
		items.add("徐州市");
		items.add("常州市");
		items.add("苏州市");
		items.add("南通市");
		items.add("连云港市");
		items.add("淮安市");
		items.add("盐城市");
		items.add("扬州市");
		items.add("镇江市");
		items.add("泰州市");
		items.add("宿迁市");
		provinceMap.put("江苏省", items);
		
		items = new ArrayList<String>();
		items.add("南昌市");
		items.add("景德镇市");
		items.add("萍乡市");
		items.add("九江市");
		items.add("新余市");
		items.add("鹰潭市");
		items.add("赣州市");
		items.add("吉安市");
		items.add("宜春市");
		items.add("抚州市");
		items.add("上饶市");
		provinceMap.put("江西省", items);
		
		items = new ArrayList<String>();
		items.add("广州市");
		items.add("韶关市");
		items.add("深圳市");
		items.add("珠海市");
		items.add("汕头市");
		items.add("佛山市");
		items.add("江门市");
		items.add("湛江市");
		items.add("茂名市");
		items.add("肇庆市");
		items.add("惠州市");
		items.add("梅州市");
		items.add("汕尾市");
		items.add("河源市");
		items.add("阳江市");
		items.add("清远市");
		items.add("东莞市");
		items.add("中山市");
		items.add("潮州市");
		items.add("揭阳市");
		items.add("云浮市");
		provinceMap.put("广东省", items);
		
		items = new ArrayList<String>();
		items.add("南宁市");
		items.add("柳州市");
		items.add("桂林市");
		items.add("梧州市");
		items.add("北海市");
		items.add("防城港市");
		items.add("钦州市");
		items.add("贵港市");
		items.add("玉林市");
		items.add("百色市");
		items.add("贺州市");
		items.add("河池市");
		items.add("来宾市");
		items.add("崇左市");
		provinceMap.put("广西省", items);
		
		items = new ArrayList<String>();
		items.add("昆明市");
		items.add("曲靖市");
		items.add("玉溪市");
		items.add("保山市");
		items.add("昭通市");
		items.add("丽江市");
		items.add("思茅市");
		items.add("临沧市");
		items.add("楚雄彝族自治州");
		items.add("红河哈尼族彝族自治州");
		items.add("文山壮族苗族自治州");
		items.add("西双版纳傣族自治州");
		items.add("大理白族自治州");
		items.add("德宏傣族景颇族自治州");
		items.add("怒江傈僳族自治州");
		items.add("迪庆藏族自治州");
		provinceMap.put("云南省", items);
		
		items = new ArrayList<String>();
		items.add("贵阳市");
		items.add("六盘水市");
		items.add("遵义市");
		items.add("安顺市");
		items.add("铜仁地区");
		items.add("黔西南布依族苗族自治州");
		items.add("毕节地区");
		items.add("黔东南苗族侗族自治州");
		items.add("黔南布依族苗族自治州");
		provinceMap.put("贵州省", items);
		
		items = new ArrayList<String>();
		items.add("成都市");
		items.add("自贡市");
		items.add("攀枝花市");
		items.add("泸州市");
		items.add("德阳市");
		items.add("绵阳市");
		items.add("广元市");
		items.add("遂宁市");
		items.add("内江市");
		items.add("乐山市");
		items.add("南充市");
		items.add("眉山市");
		items.add("宜宾市");
		items.add("广安市");
		items.add("达州市");
		items.add("雅安市");
		items.add("巴中市");
		items.add("资阳市");
		items.add("阿坝藏族羌族自治州");
		items.add("甘孜藏族自治州");
		items.add("凉山彝族自治州");
		provinceMap.put("四川省", items);
		
		items = new ArrayList<String>();
		items.add("呼和浩特市");
		items.add("包头市");
		items.add("乌海市");
		items.add("赤峰市");
		items.add("通辽市");
		items.add("鄂尔多斯市");
		items.add("呼伦贝尔市");
		items.add("巴彦淖尔市");
		items.add("乌兰察布市");
		items.add("兴安盟");
		items.add("锡林郭勒盟");
		items.add("阿拉善盟");
		provinceMap.put("内蒙古", items);
		
		items = new ArrayList<String>();
		items.add("银川市");
		items.add("石嘴山市");
		items.add("吴忠市");
		items.add("固原市");
		items.add("中卫市");
		provinceMap.put("宁夏", items);
		
		items = new ArrayList<String>();
		items.add("兰州市");
		items.add("嘉峪关市");
		items.add("金昌市");
		items.add("白银市");
		items.add("天水市");
		items.add("武威市");
		items.add("张掖市");
		items.add("平凉市");
		items.add("酒泉市");
		items.add("庆阳市");
		items.add("定西市");
		items.add("陇南市");
		items.add("临夏回族自治州");
		items.add("甘南藏族自治州");
		provinceMap.put("甘肃省", items);
		
		items = new ArrayList<String>();
		items.add("西宁市");
		items.add("海东地区");
		items.add("海北藏族自治州");
		items.add("黄南藏族自治州");
		items.add("海南藏族自治州");
		items.add("果洛藏族自治州");
		items.add("玉树藏族自治州");
		items.add("海西蒙古族藏族自治州");
		provinceMap.put("青海省", items);
		
		items = new ArrayList<String>();
		items.add("拉萨市");
		items.add("昌都地区");
		items.add("山南地区");
		items.add("日喀则地区");
		items.add("那曲地区");
		items.add("阿里地区");
		items.add("林芝地区");
		provinceMap.put("西藏", items);
		
		items = new ArrayList<String>();
		items.add("乌鲁木齐市");
		items.add("克拉玛依市");
		items.add("吐鲁番地区");
		items.add("哈密地区");
		items.add("昌吉回族自治州");
		items.add("博尔塔拉蒙古自治州");
		items.add("巴音郭楞蒙古自治州");
		items.add("阿克苏地区");
		items.add("克孜勒苏柯尔克孜自治州");
		items.add("喀什地区");
		items.add("和田地区");
		items.add("伊犁哈萨克自治州");
		items.add("塔城地区");
		items.add("阿勒泰地区");
		items.add("石河子市");
		items.add("阿拉尔市");
		items.add("图木舒克市");
		items.add("五家渠市");
		provinceMap.put("新疆", items);
		
		items = new ArrayList<String>();
		items.add("合肥市");
		items.add("芜湖市");
		items.add("蚌埠市");
		items.add("淮南市");
		items.add("马鞍山市");
		items.add("淮北市");
		items.add("铜陵市");
		items.add("安庆市");
		items.add("黄山市");
		items.add("滁州市");
		items.add("阜阳市");
		items.add("宿州市");
		items.add("巢湖市");
		items.add("六安市");
		items.add("亳州市");
		items.add("池州市");
		items.add("宣城市");
		provinceMap.put("安徽省", items);
		
		items = new ArrayList<String>();
		items.add("杭州市");
		items.add("宁波市");
		items.add("温州市");
		items.add("嘉兴市");
		items.add("湖州市");
		items.add("绍兴市");
		items.add("金华市");
		items.add("衢州市");
		items.add("舟山市");
		items.add("台州市");
		items.add("丽水市");
		provinceMap.put("浙江省", items);
		
		items = new ArrayList<String>();
		items.add("福州市");
		items.add("厦门市");
		items.add("莆田市");
		items.add("三明市");
		items.add("泉州市");
		items.add("漳州市");
		items.add("南平市");
		items.add("龙岩市");
		items.add("宁德市");
		provinceMap.put("福建省", items);
		
		items = new ArrayList<String>();
		items.add("台北市");
		items.add("高雄市");
		items.add("基隆市");
		items.add("台中市");
		items.add("台南市");
		items.add("新竹市");
		items.add("嘉义市");
		provinceMap.put("台湾", items);
		
		items = new ArrayList<String>();
		items.add("中西区");
		items.add("湾仔区");
		items.add("东区");
		items.add("南区");
		items.add("油尖旺区");
		items.add("深水埗区");
		items.add("九龙城区");
		items.add("黄大仙区");
		items.add("观塘区");
		items.add("荃湾区");
		items.add("葵青区");
		items.add("沙田区");
		items.add("西贡区");
		items.add("大埔区");
		items.add("北区");
		items.add("元朗区");
		items.add("屯门区");
		items.add("离岛区");
		provinceMap.put("香港", items);
		
		items = new ArrayList<String>();
		items.add("澳门");
		provinceMap.put("澳门", items);
	}
}
