package com.gome.haoyuangong.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.GroupMemberBean;
import com.gome.haoyuangong.layout.self.SideBar;
import com.gome.haoyuangong.layout.self.SideBar.OnTouchingLetterChangedListener;
import com.gome.haoyuangong.views.RefreshListView.OnSendDataListener;

/**
 * 选择机构名称
 * @author Administrator
 *
 */
public class SelectOrgnizationActivity extends ListViewActivity {
	public static int RESULT_FLAG = 1001;
	List<String> items;
	Intent intent;
	String orgnizationName="";
	int multisel = 0;
	List<GroupMemberBean> mlist;
	MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		setDividerHeight(0);
		intent = getIntent();
		mlist = new ArrayList<GroupMemberBean>();
		setOnSendDataListener(new OnSendDataListener() {
			
			public void OnSendData(List<GroupMemberBean> list) {
				// TODO Auto-generated method stub
				mlist = list;
			}
		});
		multisel = intent.getIntExtra("multisel", 0);
		if (multisel > 0)
			addListViewHeadView(getHeadView());
		adapter = new MyAdapter();
		setAdapter(adapter);		
		
		if (intent.getStringExtra("name") != null)
			orgnizationName = intent.getStringExtra("name");
		items = new ArrayList<String>();
		addCompany();
		setPinYinDatas(items);
//		addItems();
//		reFresh();
		setTitle("机构名称");
		getSideBar().setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					getListView().setSelection(position+getListView().getHeaderViewsCount());
				}

			}
		});
	}
	@Override
	public void onClick(View v){
//		super.onClick(v);
		switch(v.getId()){
			case R.id.title_left1:	
				if (multisel == 0)
					finish();
				else{
					Intent newIntent = new Intent();
					newIntent.putExtra("returnvalue", getMultiResult());
					setResult(RESULT_OK, newIntent);
					finish();
				}
				break;
		}
	}
	private String getMultiResult(){
		StringBuilder stringBuilder = new StringBuilder();
		for(GroupMemberBean item:mlist){
			if (item.isSelected()){
				stringBuilder.append(item.getName());
				stringBuilder.append(",");
			}
		}
		return stringBuilder.toString();
	}
	private void selectAll(boolean sel) {
		for(GroupMemberBean item:mlist){
			item.setSelected(sel);
		}
		adapter.notifyDataSetChanged();
	}
	private LinearLayout getHeadView(){
		LinearLayout layout = new LinearLayout(SelectOrgnizationActivity.this);
		final BarItem item = new BarItem(SelectOrgnizationActivity.this);
		item.setTitle("全部");
		item.setHeadImage(R.drawable.round_uncheck);
		item.setTag(0);
		item.setHeadViewClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Integer.parseInt(item.getTag().toString()) == 0){					
					item.setHeadImage(R.drawable.round_check);
					item.setTag(1);
					selectAll(true);
				}
				else{					
					item.setHeadImage(R.drawable.round_uncheck);
					item.setTag(0);
					selectAll(false);
				}
			}
		});
		item.setRightArrowVisible(View.INVISIBLE);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
		layout.addView(item,p);
		layout.setTag(item);
		return layout;
	}
	private void addItems(){
		for(int i=0;i<items.size();i++){			
			LinearLayout layout = new LinearLayout(this);
			final BarItem item = new BarItem(this);
			item.setHeadImage(R.drawable.icon_v);
			item.setRightArrowVisible(View.INVISIBLE);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
			item.setTitle(items.get(i));
			layout.addView(item,p);
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
			addItem(layout);
		}
	}
	private void getCompanyFromFile(){
		try { 
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("zqgs") ); 
           BufferedReader bufReader = new BufferedReader(inputReader);
           String line="";
           while((line = bufReader.readLine()) != null)
               items.add(line);
       } catch (Exception e) { 
           e.printStackTrace(); 
       }
	}
	private void addCompany(){
		items.clear();
		getCompanyFromFile();
//		items.add("爱建证券有限责任公司");
//		items.add("安信证券股份有限公司");
//		items.add("北京高华证券有限责任公司");
//		items.add("渤海证券股份有限公司");
//		items.add("财达证券有限责任公司");
//		items.add("财富证券有限责任公司");
//		items.add("财通证券股份有限公司");
//		items.add("长城证券有限责任公司");
//		items.add("长江证券承销保荐有限公司");
//		items.add("长江证券股份有限公司");
//		items.add("诚浩证券有限责任公司");
//		items.add("川财证券有限责任公司");
//		items.add("大通证券股份有限公司");
//		items.add("大同证券经纪有限责任公司");
//		items.add("德邦证券有限责任公司");
//		items.add("第一创业摩根大通证券有限责任公司");
//		items.add("第一创业证券股份有限公司");
//		items.add("东北证券股份有限公司");
//		items.add("东方花旗证券有限公司");
//		items.add("东方证券股份有限公司");
//		items.add("东海证券股份有限公司");
//		items.add("东吴证券股份有限公司");
//		items.add("东兴证券股份有限公司");
//		items.add("东莞证券有限责任公司");
//		items.add("方正证券股份有限公司");
//		items.add("高盛高华证券有限责任公司");
//		items.add("光大证券股份有限公司");
//		items.add("广发证券股份有限公司");
//		items.add("广发证券资产管理（广东）有限公司");
//		items.add("广州证券股份有限公司");
//		items.add("国都证券有限责任公司");
//		items.add("国海证券股份有限公司");
//		items.add("国金证券股份有限公司");
//		items.add("国开证券有限责任公司");
//		items.add("国联证券股份有限公司");
//		items.add("国盛证券有限责任公司");
//		items.add("国泰君安证券股份有限公司");
//		items.add("国信证券股份有限公司");
//		items.add("国元证券股份有限公司");
//		items.add("海际大和证券有限责任公司");
//		items.add("海通证券股份有限公司");
//		items.add("航天证券有限责任公司");
//		items.add("恒泰长财证券有限责任公司");
//		items.add("恒泰证券股份有限公司");
//		items.add("宏信证券有限责任公司");
//		items.add("宏源证券股份有限公司");
//		items.add("爱建证券有限责任公司");
//		items.add("红塔证券股份有限公司");
//		items.add("华安证券股份有限公司");
//		items.add("华宝证券有限责任公司");
//		items.add("华创证券有限责任公司");
//		items.add("华福证券有限责任公司");
//		items.add("华林证券有限责任公司");
//		items.add("华龙证券有限责任公司");
//		items.add("华融证券股份有限公司");
//		items.add("华泰联合证券有限责任公司");
//		items.add("华泰证券股份有限公司");
//		items.add("华西证券股份有限公司");
//		items.add("华英证券有限责任公司");
//		items.add("华鑫证券有限责任公司");
//		items.add("江海证券有限公司");
//		items.add("金元证券股份有限公司");
//		items.add("安徽大时代投资咨询有限公司");
//		items.add("安徽华安新兴证券投资咨询有限责任公司");
//		items.add("北京博星投资顾问有限公司");
//		items.add("北京东方高圣投资顾问有限公司");
//		items.add("北京股商证券投资顾问有限公司");
//		items.add("北京海问咨询有限公司");
//		items.add("北京和众汇富咨询有限公司");
//		items.add("北京金美林投资顾问有限公司");
//		items.add("北京京放投资管理顾问有限责任公司");
//		items.add("北京盛世华商投资咨询有限公司");
//		items.add("北京首证投资顾问有限公司");
//		items.add("北京指南针科技发展股份有限公司");
//		items.add("北京中方信富投资管理咨询有限公司");
//		items.add("北京中富金石咨询有限公司");
//		items.add("北京中和应泰财务顾问有限公司");
//		items.add("北京中资北方投资顾问有限公司");
//		items.add("成都汇阳投资顾问有限公司");
//		items.add("大连北部资产经营有限公司");
//		items.add("大连华讯投资咨询有限公司");
//		items.add("鼎信汇金(北京)投资管理有限公司");
//		items.add("福建天信投资咨询顾问股份有限公司");
//		items.add("福建中讯证券研究有限责任公司");
//		items.add("广东博众证券投资咨询有限公司");
//		items.add("广东科德投资顾问有限公司");
//		items.add("广州广证恒生证券研究所有限公司");
//		items.add("广州汇正财经顾问有限公司");
//		items.add("广州市万隆证券咨询顾问有限公司");
//		items.add("广州越声理财咨询有限公司");
//		items.add("海南港澳资讯产业股份有限公司");
//		items.add("杭州顶点财经网络传媒有限公司");
//		items.add("河北源达证券投资咨询有限公司");
//		items.add("河南九鼎德盛投资顾问有限公司");
//		items.add("黑龙江省容维投资顾问有限责任公司");
//		items.add("湖南金证投资咨询顾问有限公司");
//		items.add("湖南巨景证券投资顾问有限公司");
//		items.add("江苏金百临投资咨询有限公司");
//		items.add("江苏天鼎投资咨询有限公司");
//		items.add("联合信用投资咨询有限公司");
//		items.add("辽宁弘历投资咨询有限公司");
//		items.add("宁波海顺投资咨询有限公司");
//		items.add("青岛市大摩投资咨询有限公司");
//		items.add("山东神光咨询服务有限责任公司");
//		items.add("山东英大投资顾问有限责任公司");
//		items.add("陕西巨丰投资资讯有限责任公司");
//		items.add("上海大智慧股份有限公司");
//		items.add("上海东方财富证券研究所有限公司");
//		items.add("上海海能证券投资顾问有限公司");
//		items.add("上海金汇信息系统有限公司");
//		items.add("上海凯石证券投资咨询有限公司");
//		items.add("上海迈步投资管理有限公司");
//		items.add("上海荣正投资咨询有限公司");
//		items.add("上海森洋投资咨询有限公司");
//		items.add("上海申银万国证券研究所有限公司");
//		items.add("上海世基投资顾问有限公司");
//		items.add("上海新兰德证券投资咨询顾问有限公司");
//		items.add("上海新资源证券咨询有限公司");
//		items.add("上海亚商投资顾问有限公司");
//		items.add("上海益盟软件技术股份有限公司");
//		items.add("上海涌金理财顾问有限公司");
//		items.add("上海证联投资咨询服务有限责任公司");
//		items.add("上海证券通投资资讯科技有限公司");
//		items.add("上海证券之星综合研究有限公司");
//		items.add("上海中广信息传播咨询有限公司");
//		items.add("深圳大德汇富咨询顾问有限公司");
//		items.add("深圳君银证券投资咨询顾问有限公司");
//		items.add("深圳市国诚投资咨询有限公司");
//		items.add("深圳市怀新企业投资顾问有限公司");
//		items.add("深圳市启富证券投资顾问有限公司");
//		items.add("深圳市新兰德证券投资咨询有限公司");
//		items.add("深圳市智多盈投资顾问有限公司");
//		items.add("深圳市中证投资资讯有限公司");
//		items.add("深圳市尊悦证券投资顾问有限公司");
//		items.add("深圳市珞珈投资咨询有限公司");
//		items.add("沈阳麟龙投资顾问有限公司");
//		items.add("四川大决策证券投资顾问有限公司");
//		items.add("四川省钱坤证券投资咨询有限公司");
//		items.add("天相投资顾问有限公司");
//		items.add("天一星辰（北京）科技有限公司");
//		items.add("厦门高能投资咨询有限公司");
//		items.add("厦门金相投资咨询有限公司");
//		items.add("厦门市新汇通投资咨询有限公司");
//		items.add("厦门市鑫鼎盛控股有限公司");
//		items.add("云南产业投资管理有限公司");
//		items.add("浙江同花顺云软件有限公司");
//		items.add("重庆东金投资顾问有限公司");
//		items.add("开源证券有限责任公司");
//		items.add("联讯证券股份有限公司");
//		items.add("民生证券股份有限公司");
//		items.add("爱建证券有限责任公司");
//		items.add("摩根士丹利华鑫证券有限责任公司");
//		items.add("南京证券股份有限公司");
//		items.add("平安证券有限责任公司");
//		items.add("齐鲁证券有限公司");
//		items.add("日信证券有限责任公司");
//		items.add("瑞信方正证券有限责任公司");
//		items.add("瑞银证券有限责任公司");
//		items.add("山西证券股份有限公司");
//		items.add("上海东方证券资产管理有限公司");
//		items.add("上海光大证券资产管理有限公司");
//		items.add("上海国泰君安证券资产管理有限公司");
//		items.add("上海海通证券资产管理有限公司");
//		items.add("上海华信证券有限责任公司");
//		items.add("上海证券有限责任公司");
//		items.add("申银万国证券股份有限公司");
//		items.add("世纪证券有限责任公司");
//		items.add("首创证券有限责任公司");
//		items.add("太平洋证券股份有限公司");
//		items.add("天风证券股份有限公司");
//		items.add("天源证券有限公司");
//		items.add("万和证券有限责任公司");
//		items.add("万联证券有限责任公司");
//		items.add("五矿证券有限公司");
//		items.add("西部证券股份有限公司");
//		items.add("西藏同信证券股份有限公司");
//		items.add("西南证券股份有限公司");
//		items.add("厦门证券有限公司");
//		items.add("湘财证券股份有限公司");
//		items.add("新时代证券有限责任公司");
//		items.add("信达证券股份有限公司");
//		items.add("兴业证券股份有限公司");
//		items.add("银泰证券有限责任公司");
//		items.add("英大证券有限责任公司");
//		items.add("招商证券股份有限公司");
//		items.add("浙江浙商证券资产管理有限公司");
//		items.add("浙商证券股份有限公司");
//		items.add("中德证券有限责任公司");
//		items.add("中国国际金融有限公司");
//		items.add("中国民族证券有限责任公司");
//		items.add("中国银河证券股份有限公司");
//		items.add("中国中投证券有限责任公司");
//		items.add("中航证券有限公司");
//		items.add("中山证券有限责任公司");
//		items.add("中天证券有限责任公司");
//		items.add("中信建投证券股份有限公司");
//		items.add("中信证券(浙江)有限责任公司");
//		items.add("中信证券（山东）有限责任公司");
//		items.add("中信证券股份有限公司");
//		items.add("中银国际证券有限责任公司");
//		items.add("中邮证券有限责任公司");
//		items.add("中原证券股份有限公司");
//		items.add("众成证券经纪有限公司");
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (convertView == null){
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(SelectOrgnizationActivity.this).inflate(R.layout.activity_group_member_item, null);
				viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
				LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.infocontent);
				viewHolder.view = createView(position);
				LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layout.addView(viewHolder.view,p);
				convertView.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.tvLetter.setText(mlist.get(position).getSortLetters());
			BarItem item = (BarItem)viewHolder.view.getTag();
			if (multisel>0){
				if (mlist.get(position).isSelected())
					item.setHeadImage(R.drawable.round_check);
				else
					item.setHeadImage(R.drawable.round_uncheck);
			}
			item.setTitle(mlist.get(position).getName());
			int section = getSectionForPosition(position);
			if (position == getPositionForSection(section)) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mlist.get(position).getSortLetters());
			} else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}
			return convertView;
		}
		
		private LinearLayout createView(int index) {
			LinearLayout layout = new LinearLayout(SelectOrgnizationActivity.this);
			final BarItem item = new BarItem(SelectOrgnizationActivity.this);
			item.setTag(mlist.get(index));
			if (multisel > 0){
				item.setHeadViewClick(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						GroupMemberBean bean = (GroupMemberBean)item.getTag();
						if (bean.isSelected()){
							bean.setSelected(false);
							item.setHeadImage(R.drawable.round_uncheck);
						}
						else{
							bean.setSelected(true);
							item.setHeadImage(R.drawable.round_check);
						}
					}
				});				
			}
			else{
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
			}
			item.setRightArrowVisible(View.INVISIBLE);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
			item.setTitle(mlist.get(index).getName());
			layout.addView(item,p);
			layout.setTag(item);
			return layout;
		}
		private int getSectionForPosition(int position) {
			return mlist.get(position).getSortLetters().charAt(0);
		}

		/**
		 * ��ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
		 */
		private int getPositionForSection(int section) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = mlist.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}

			return -1;
		}
	}
	final class ViewHolder {
		TextView tvLetter;
//		TextView tvTitle;
		LinearLayout view;
	}
}
