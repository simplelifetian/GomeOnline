package com.gome.haoyuangong.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.ViewSignUserInfoActivity;
import com.gome.haoyuangong.layout.self.CharacterParser;
import com.gome.haoyuangong.layout.self.ClearEditText;
import com.gome.haoyuangong.layout.self.GroupMemberBean;
import com.gome.haoyuangong.layout.self.PinyinComparator;
import com.gome.haoyuangong.layout.self.SideBar;
import com.gome.haoyuangong.layout.self.SideBar.OnTouchingLetterChangedListener;
import com.gome.haoyuangong.sortlistview.SortGroupMemberAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PinYinListView extends RelativeLayout {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private Adapter adapter;
	private ClearEditText mClearEditText;

	private LinearLayout titleLayout;
	private TextView title;
	private TextView tvNofriends;

	private int lastFirstVisibleItem = -1;

	private CharacterParser characterParser;
	private List<GroupMemberBean> SourceDateList;

	private PinyinComparator pinyinComparator;
	private FragmentActivity activity;
	protected List<Object> items;
	
	public PinYinListView(Context context) {
		super(context);		
		activity = (FragmentActivity)context;
		items = new ArrayList<Object>();
		// TODO Auto-generated constructor stub
		initViews();
	}
	public void initViews() {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_add_friends, null);
		addView(view,p);
		titleLayout = (LinearLayout) view.findViewById(R.id.title_layout);
		title = (TextView) view.findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) view.findViewById(R.id.title_layout_no_friends);
		// ʵ����תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);

		SourceDateList = new ArrayList<GroupMemberBean>();
		adapter = new Adapter(getContext(), SourceDateList);
		sortListView.setAdapter(adapter);
		sortListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (SourceDateList.size() == 0)
					return;
				int section = getSectionForPosition(firstVisibleItem);
				int nextSection = getSectionForPosition(firstVisibleItem + 1);
				int nextSecPosition = getPositionForSection(+nextSection);
				if (firstVisibleItem != lastFirstVisibleItem) {
					MarginLayoutParams params = (MarginLayoutParams) titleLayout
							.getLayoutParams();
					params.topMargin = 0;
					titleLayout.setLayoutParams(params);
					title.setText(SourceDateList.get(
							getPositionForSection(section)).getSortLetters());
				}
				if (nextSecPosition == firstVisibleItem + 1) {
					View childView = view.getChildAt(0);
					if (childView != null) {
						int titleHeight = titleLayout.getHeight();
						int bottom = childView.getBottom();
						MarginLayoutParams params = (MarginLayoutParams) titleLayout
								.getLayoutParams();
						if (bottom < titleHeight) {
							float pushedDistance = bottom - titleHeight;
							params.topMargin = (int) pushedDistance;
							titleLayout.setLayoutParams(params);
						} else {
							if (params.topMargin != 0) {
								params.topMargin = 0;
								titleLayout.setLayoutParams(params);
							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
			}
		});
	}
	public int indexOf(Object obj) {
		return items.indexOf(obj);
	}
	public void addItem(Object obj){
		items.add(obj);
	}
	public void addItem(Object obj,int index){
		items.add(0, obj);;
	}
	public void clear() {
		items.clear();
	}
	public void reFresh(){
		adapter.notifyDataSetChanged();
	}
	/**
	 * ΪListView������
	 * 
	 * @param date
	 * @return
	 */
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
		adapter.updateListView(SourceDateList);
		Collections.sort(SourceDateList, pinyinComparator);
		adapter.notifyDataSetChanged();
	}
	public void setDividerHeight(int height){
		sortListView.setDividerHeight(height);
	}

	/**
	 * ���������е�ֵ��������ݲ�����ListView
	 * 
	 * @param filterStr
	 */
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
		adapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * ���ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return SourceDateList.get(position).getSortLetters().charAt(0);
	}

	/**
	 * ��ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < SourceDateList.size(); i++) {
			String sortStr = SourceDateList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	
	public class Adapter extends BaseAdapter implements SectionIndexer {
		private List<GroupMemberBean> list = null;
		private Context mContext;

		public Adapter(Context mContext, List<GroupMemberBean> list) {
			this.mContext = mContext;
			this.list = list;
		}

		/**
		 * ��ListView��ݷ���仯ʱ,���ô˷���������ListView
		 * 
		 * @param list
		 */
		public void updateListView(List<GroupMemberBean> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		public int getCount() {
			return this.list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			final GroupMemberBean mContent = list.get(position);
			if (view == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(R.layout.activity_group_member_item, null);
				viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
				LinearLayout layout = (LinearLayout)view.findViewById(R.id.infocontent);
				viewHolder.view = (View)items.get(position);
				if (viewHolder.view.getParent() != null)
					((LinearLayout)viewHolder.view.getParent()).removeAllViews();
				LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				layout.addView(viewHolder.view,p);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
//				view = viewHolder.view;
			}

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

			return view;

		}

		final class ViewHolder {
			TextView tvLetter;
//			TextView tvTitle;
			View view;
		}

		/**
		 * ���ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
		 */
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

		/**
		 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
		 * 
		 * @param str
		 * @return
		 */
		private String getAlpha(String str) {
			String sortStr = str.trim().substring(0, 1).toUpperCase();
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if (sortStr.matches("[A-Z]")) {
				return sortStr;
			} else {
				return "#";
			}
		}

		@Override
		public Object[] getSections() {
			return null;
		}
	}
}
