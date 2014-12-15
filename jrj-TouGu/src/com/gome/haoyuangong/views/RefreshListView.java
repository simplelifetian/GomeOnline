package com.gome.haoyuangong.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.interfaces.OnDataRefreshListener;
import com.gome.haoyuangong.interfaces.OnListViewItemClickListener;
import com.gome.haoyuangong.layout.self.CharacterParser;
import com.gome.haoyuangong.layout.self.ClearEditText;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.GroupMemberBean;
import com.gome.haoyuangong.layout.self.PinyinComparator;
import com.gome.haoyuangong.layout.self.SideBar;
import com.gome.haoyuangong.layout.self.SideBar.OnTouchingLetterChangedListener;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.views.PinYinListView.Adapter.ViewHolder;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class RefreshListView extends LinearLayout {
	public interface OnSendDataListener{
		public void OnSendData(List<GroupMemberBean> list);
	}
	
	public String moduleTag;
	private XListView _listView;
	private LinearLayout head;
	private Adapter _adapter;
	protected List<Object> _items;
	private OnDataRefreshListener onDateRefreshListener;
	private OnListViewItemClickListener onListViewItemClickListener;
	private OnSendDataListener onSendDataListener;
	
	
	private SideBar sideBar;
	private List<GroupMemberBean> SourceDateList;
	private PinyinComparator pinyinComparator;
	private CharacterParser characterParser;
	private ClearEditText mClearEditText;
	private TextView dialog;

	private LinearLayout titleLayout;
	private TextView title;
	private TextView tvNofriends;
	private boolean isPinYinList;
	private SharedPreferences sharedPreferences;
	
	public RefreshListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		sharedPreferences = getContext().getSharedPreferences("freshtime",Context.MODE_PRIVATE);
		_items = new ArrayList<Object>();
		this.setOrientation(VERTICAL);		
		initComponent();		
	}	
	public void setFreshTime(Date dt) {
		String dateString = DateUtils.format(dt, "yyyy-MM-dd HH:mm:ss");
		if (sharedPreferences != null && !TextUtils.isEmpty(moduleTag))
			sharedPreferences.edit().putString(moduleTag, dateString).commit();
		_listView.setRefreshTime(dateString);
	}
	private void getFreshTime() {
		if (sharedPreferences == null || TextUtils.isEmpty(moduleTag))
			return;
		String dateString = sharedPreferences.getString(moduleTag, "");
		_listView.setRefreshTime(dateString);
	}
	public void setPinYinList(boolean isPinYinList) {
		this.isPinYinList = isPinYinList;
		if (!this.isPinYinList)
			sideBar.setVisibility(View.GONE);
		else
			sideBar.setVisibility(View.VISIBLE);
	}

	public XListView getListView(){
		return _listView;
	}
	public SideBar getSideBar() {
		return sideBar;
	}
	public void setOnSendDataListener(OnSendDataListener onSendDataListener) {
		this.onSendDataListener = onSendDataListener;
	}
	public void setModuleTag(String moduleTag) {
		this.moduleTag = moduleTag;
		getFreshTime();
	}
	private void initComponent(){
		sideBar = new SideBar(getContext());
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = _adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					_listView.setSelection(position+_listView.getHeaderViewsCount());
				}

			}
		});
		sideBar.setVisibility(View.GONE);
		characterParser = CharacterParser.getInstance();
		SourceDateList = new ArrayList<GroupMemberBean>();
		pinyinComparator = new PinyinComparator();
		head = new LinearLayout(getContext());
		_listView = new XListView(getContext());
		_listView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (onDateRefreshListener != null)
					onDateRefreshListener.OnStartRefresh();
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (onDateRefreshListener != null)
					onDateRefreshListener.OnStartLoadMore();;
			}
		});
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		addView(head,p);
		
		RelativeLayout layout = new RelativeLayout(getContext());
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(layout,p);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.addView(_listView,rp);
		rp = new RelativeLayout.LayoutParams(Function.getFitPx(getContext(), 90), LayoutParams.MATCH_PARENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout.addView(sideBar,rp);
		_adapter = new Adapter(getContext(),SourceDateList);
		_listView.setAdapter(_adapter);
//		_listView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				if (onListViewItemClickListener != null){
//					onListViewItemClickListener.OnListViewItemClick(parent, view, position, id);
//				}
//			}
//		});
	}
	
	private List<GroupMemberBean> filledData(List<String> date) {
		List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

		for (int i = 0; i < date.size(); i++) {
			GroupMemberBean sortModel = new GroupMemberBean();
			sortModel.setName(date.get(i));
			// ����ת����ƴ��
			String pinyin = characterParser.getSelling(date.get(i));
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}
	public void setDatas(List<String> items){
		SourceDateList = filledData(items);
		_adapter.updateListView(SourceDateList);
		if (onSendDataListener != null)
			onSendDataListener.OnSendData(SourceDateList);
		Collections.sort(SourceDateList, pinyinComparator);
		items.clear();
		for(GroupMemberBean item:SourceDateList){
			items.add(item.getName());
		}
		
		_adapter.notifyDataSetChanged();
	}
	private void filterData(String filterStr) {
		List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
			tvNofriends.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (GroupMemberBean sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// ���a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		_adapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}
	
	public int indexOf(Object obj) {
		return _items.indexOf(obj);
	}
	public Object getItemAt(int index) {
		if (_items.size() == 0)
			return null;
		else
			return _items.get(index);
	}
	public void hideHead(){
		if (head != null)
			head.setVisibility(GONE);
	}
	public int getCount(){
		if (_items == null)
			return 0;
		return _items.size();
	}
	public void addHeadView(View view){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		head.addView(view,p);
	}
	public void addListViewHeadView(View view) {
		_listView.addHeaderView(view);
	}
	public void setPullRefreshEnable(boolean enable){
		_listView.setPullRefreshEnable(enable);
	}
	public void setPullLoadEnable(boolean visible){
		_listView.setPullLoadEnable(visible);
	}
	public void reFresh(){
		_adapter.notifyDataSetChanged();
	}
	
	public void addItem(Object obj){
		_items.add(obj);
	}
	public void remove(int index){
		_items.remove(index);
	}
	public void remove(Object obj){
		_items.remove(obj);
	}
	public void addItem(Object obj,int index){
		_items.add(index, obj);;
	}
	public void clear() {
		_items.clear();
	}
	public void stopFresh() {
		setFreshTime(new Date());
		_listView.stopRefresh();
	}
	public void stopLoadMore() {
		_listView.stopLoadMore();
	}
	public void setDividerHeight(int height){
		_listView.setDividerHeight(height);
	}
	public void setOnDateRefreshListener(OnDataRefreshListener onDateRefreshListener) {
		this.onDateRefreshListener = onDateRefreshListener;
	}
	
	public void setOnListViewItemClickListener(
			OnListViewItemClickListener onListViewItemClickListener) {
		this.onListViewItemClickListener = onListViewItemClickListener;
	}

	public void setAdapter(BaseAdapter adapter){
		if (adapter instanceof Adapter){
			_adapter = (Adapter)adapter;
			_listView.setAdapter(_adapter);
		}
		else
			_listView.setAdapter(adapter);
	}

	class Adapter extends BaseAdapter {
		private List<GroupMemberBean> list = null;
		private Context mContext;
		private LayoutInflater mInflater;

		public Adapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}
		public Adapter(Context mContext, List<GroupMemberBean> list) {
			this.mContext = mContext;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return _items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (isPinYinList)
				return getPinYinView(position, convertView, parent);
			else
				return getNormalView(position, convertView, parent);
		}	

		final class ViewHolder {
			TextView tvLetter;
//			TextView tvTitle;
			View view;
		}		
		private View getNormalView(final int position, View convertView, ViewGroup parent){
			convertView = (View)_items.get(position);
			return convertView;
		}
		private View getPinYinView(final int position, View convertView, ViewGroup parent){
			ViewHolder viewHolder = null;
			final GroupMemberBean mContent = list.get(position);
//			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_group_member_item, null);
				viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
				LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.infocontent);
				viewHolder.view = (View)_items.get(position);
				if (viewHolder.view.getParent() != null)
					((LinearLayout)viewHolder.view.getParent()).removeAllViews();
				LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layout.addView(viewHolder.view,p);
				convertView.setTag(viewHolder);
//			} else {
				viewHolder = (ViewHolder) convertView.getTag();
//				view = viewHolder.view;
//			}

			// ���position��ȡ���������ĸ��Char asciiֵ
			int section = getSectionForPosition(position);

			// ���ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
			if (position == getPositionForSection(section)) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getSortLetters());
			} else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}		
//			viewHolder.infoItem.doLayout();		
//			viewHolder.infoItem.setName(this.list.get(position).getName());

			return convertView;
		}
		public int getSectionForPosition(int position) {
			return list.get(position).getSortLetters().charAt(0);
		}

		/**
		 * ��ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
		 */
		public int getPositionForSection(int section) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = list.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}

			return -1;
		}
		public void updateListView(List<GroupMemberBean> list) {
			this.list = list;
			notifyDataSetChanged();
		}
	}

}
