package com.gome.haoyuangong.fragments;

/**
 * 股票选择Fragment
 */
import java.util.Vector;

import com.gome.haoyuangong.AppOper;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.db.QuoteDic;
import com.gome.haoyuangong.db.RecordStoreManager;
import com.gome.haoyuangong.keyboard.ChangeCodeLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class KeyBoardFragment extends BaseFragment implements OnItemClickListener, TextWatcher, AppOper {

	private Context ctx = null;

	ListView listView;// /显示查询结果列表控件
	private EditText editText;// /代码/拼音输入控件
	// 数据字典数据库
	private SQLiteDatabase dataBase;
	private View view;// 全局使用View
	private View clearView;
	private TextView lableTv;
	/**
	 * 自选股Vector容器
	 */
	public static Vector<Stock> myStockVec = new Vector<Stock>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// Bundle bundle =
		// Inquiry_Fragment.this.getActivity().getIntent().getExtras();
		// if (bundle != null)
		// {
		// mIsAsk = bundle.getBoolean("isask", false);
		// }
		View vg = super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.main_fragment_inquiry, container, false);
		setContent(view);
		// if(!mIsAsk&&savedInstanceState!=null){
		myStockVec = RecordStoreManager.getInstance().getAllReadRecord();
		// }
		clearView = view.findViewById(R.id.clearEditTextTv);
		clearView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OnAction(8, ChangeCodeLayout.BUTTON_FUNCTION_CLEAR);
			}
		});
		/*
		 * 输入框
		 */
		editText = (EditText) view.findViewById(R.id.StockInput);
		editText.addTextChangedListener(this);
		editText.requestFocus();
		// editText.setHint("代码/简拼");
		editText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int inType = editText.getInputType(); // backup the input type
				editText.setInputType(InputType.TYPE_NULL); // disable soft input
				editText.onTouchEvent(event); // call native handler
				editText.setInputType(inType); // restore input type
				editText.setSelection(editText.getText().length());
				return true;
			}
		});
		editText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),
				// 0);//隐藏软键盘
				m_keyboardLayout.setVisibility(View.VISIBLE);
			}
		});
		// editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		/** ------------------------------- ListView列表--------------------------- **/
		listView = (ListView) view.findViewById(R.id.inqu_ListView_Stock_LookUp);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
					if (m_keyboardLayout != null && m_keyboardLayout.getVisibility() == View.VISIBLE) {
						m_keyboardLayout.setVisibility(View.GONE);
					}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
		lableTv = (TextView) view.findViewById(R.id.searchLableTv);
		lableTv.setText("我的自选股");
		initMyKeyBoard();
		// stockRecoder = (Vector<Stock>) AppInfo.myStockVec.clone();
		stockRecoder.clear();
		for (int i = 0; i < myStockVec.size(); i++) {
			Stock stk = myStockVec.elementAt(i);
			if (stk.getType() != null && stk.getType().startsWith("s")) {
				stockRecoder.add(stk);
			}
		}
		listView.setAdapter(getListAdapter());

		return vg;
	}

	// @Override
	// public void onClick(View v) {
	// super.onClick(v);
	// if (v.getId() == R.id.leftBtnIV) {
	// getActivity().finish();
	// }
	// }

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		ctx = activity;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	/**
	 * 监听输入框内容
	 */
	@SuppressLint("SdCardPath")
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		try {
			String strContent = arg0.toString();

			if (strContent.length() > 0)// 大于2个字符的时候查找数据
			{
				lableTv.setText("搜索结果");
				clearView.setVisibility(View.VISIBLE);
				if (dataBase == null || !dataBase.isOpen()) {
					String path = "/data/data/" + ctx.getPackageName() + "/" + QuoteDic.DATA_NAME;
					dataBase = SQLiteDatabase.openOrCreateDatabase(path, null);
				}
				stockRecoder.removeAllElements();
				String sql = "SELECT * FROM " + QuoteDic.TABLE_NAME + " WHERE (" + QuoteDic.Property_StockCode + " like '" + strContent + "%'" + " OR " + QuoteDic.Property_StockPY + " like '" + strContent + "%') AND " + QuoteDic.Property_StockType + " like 's%' order by " + QuoteDic.Property_StockCode + " limit 0,60";// limit

				// System.out.println("sql is:["+sql+"]");
				Cursor oCursor1 = dataBase.rawQuery(sql, null);
				for (oCursor1.moveToFirst(); !oCursor1.isAfterLast(); oCursor1.moveToNext()) {
					Stock ss = new Stock();
					int market = oCursor1.getColumnIndex(QuoteDic.Property_MarketId);
					if (market > 0) {
						ss.setMarketID(oCursor1.getString(market));
					}
					int codeID = oCursor1.getColumnIndex(QuoteDic.Property_StockCode);
					if (codeID > 0) {
						ss.setStockCode(oCursor1.getString(codeID));
					}
					int nameID = oCursor1.getColumnIndex(QuoteDic.Property_StockName);
					if (nameID > 0) {
						ss.setStockName(oCursor1.getString(nameID));
					}
					int pinyin = oCursor1.getColumnIndex(QuoteDic.Property_StockPY);
					if (pinyin > 0) {
						ss.setStockPinyin(oCursor1.getString(pinyin));
					}
					int index = oCursor1.getColumnIndex(QuoteDic.Property_STOCKId);
					ss.setStid(oCursor1.getString(index));
					index = oCursor1.getColumnIndex(QuoteDic.Property_StockType);
					ss.setType(oCursor1.getString(index));
					stockRecoder.addElement(ss);
				}
				if (oCursor1.getCount() == 0) {
					sql = "SELECT * FROM " + QuoteDic.TABLE_NAME + " WHERE (" + QuoteDic.Property_StockCode + " like '%" + strContent + "'" + " OR " + QuoteDic.Property_StockPY + " like '%" + strContent + "') AND " + QuoteDic.Property_StockType + " like 's%' order by " + QuoteDic.Property_StockCode + " limit 0,30";
					oCursor1 = dataBase.rawQuery(sql, null);
					for (oCursor1.moveToFirst(); !oCursor1.isAfterLast(); oCursor1.moveToNext()) {
						Stock ss = new Stock();
						int market = oCursor1.getColumnIndex(QuoteDic.Property_MarketId);
						if (market > 0) {
							ss.setMarketID(oCursor1.getString(market));
						}
						int codeID = oCursor1.getColumnIndex(QuoteDic.Property_StockCode);
						if (codeID > 0) {
							ss.setStockCode(oCursor1.getString(codeID));
						}
						int nameID = oCursor1.getColumnIndex(QuoteDic.Property_StockName);
						if (nameID > 0) {
							ss.setStockName(oCursor1.getString(nameID));
						}
						int pinyin = oCursor1.getColumnIndex(QuoteDic.Property_StockPY);
						if (pinyin > 0) {
							ss.setStockPinyin(oCursor1.getString(pinyin));
						}
						int index = oCursor1.getColumnIndex(QuoteDic.Property_STOCKId);
						ss.setStid(oCursor1.getString(index));
						index = oCursor1.getColumnIndex(QuoteDic.Property_StockType);
						ss.setType(oCursor1.getString(index));
						stockRecoder.addElement(ss);
					}
				}
				listView.setAdapter(getListAdapter());
				oCursor1.close();
			} else {
				clearView.setVisibility(View.INVISIBLE);
				clearListHandler.sendEmptyMessage(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 股票查询结果 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class ImageViewAdapter extends BaseAdapter {
		// private boolean mIsShowPlusBtn=false;

		public Vector<Stock> getList() {
			return stockRecoder;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int a_position = position;
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = KeyBoardFragment.this.getActivity().getLayoutInflater();
				row = inflater.inflate(R.layout.item_list_querystock, parent, false);
			}
			TextView stockInfo = (TextView) row.findViewById(R.id.item_text);
			TextView stockCode = (TextView) row.findViewById(R.id.item_text_code);
			stockInfo.setText(((Stock) getItem(a_position)).getStockName());
			stockCode.setText(((Stock) getItem(a_position)).getStockCode());
			final ImageView plusBtn = (ImageView) row.findViewById(R.id.item_imageplus);
			// if(mIsShowPlusBtn){
			// plusBtn.setVisibility(View.VISIBLE);
			// }else{
			plusBtn.setVisibility(View.VISIBLE);//＋号
			// }
			 Stock sr = stockRecoder.elementAt(a_position);
			 if (false)//isMyStock(sr.getStid())
			 {
			 plusBtn.setImageResource(R.drawable.item_querystock_yes);
			 }
			 else
			 {
			 plusBtn.setImageResource(R.drawable.item_querystock_add_selector);
				plusBtn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (stockRecoder != null) {
							// Stock sr = stockRecoder.elementAt(a_position);
							// addToMystock(sr,plusBtn);
						}
					}
				});
			}

			return row;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return stockRecoder == null ? 0 : stockRecoder.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (stockRecoder == null) {
				return null;
			} else {
				return stockRecoder.elementAt(position);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	ImageViewAdapter simperAdapter;

	// ArrayList<HashMap<String, Object>> data;
	/**
	 * 初始化适配器
	 */
	private ImageViewAdapter getListAdapter() {
		simperAdapter = new ImageViewAdapter();
		// simperAdapter.SetIsShowPlusBtn(!mIsAsk);
		return simperAdapter;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// System.out.println("position is:["+position+"] id is:["+id+"]");
		if (stockRecoder != null) {
			if (position < stockRecoder.size()) {
				Stock sr = stockRecoder.elementAt(position);
				// goToInquiry(sr.getStockName(), sr.getStockCode(), sr.getMarketID(),
				// sr.getType());
				// if(mIsAsk){
				// gotoAsk(sr.getStockName(), sr.getStockCode());
				// }else{
				// gotoMinChart(sr.getStockName(), sr.getStockCode(), sr.getMarketID());
				// }
			} else {
				if (editText.getText().length() > 0) {
					// showErrorDialog("您输入的代码或拼音未找到对应的股票");
				} else {
					// showErrorDialog("请输入股票代码或拼音");
				}
			}
		}
	}

	/**
	 * 查询结果 临时Vector
	 */
	private Vector<Stock> stockRecoder = new Vector<Stock>();

	// 处理线程后的界面更新
	@SuppressLint("HandlerLeak")
	private Handler clearListHandler = new Handler() {
		public void handleMessage(Message msg) {
			if ("DataSetChanged".equals(msg.obj)) {
				simperAdapter.notifyDataSetChanged();
			} else if (simperAdapter != null) {
				// stockRecoder.removeAllElements();
				lableTv.setText("我的自选股");
				// stockRecoder = (Vector<Stock>) AppInfo.myStockVec.clone();
				stockRecoder.clear();
				for (int i = 0; i < myStockVec.size(); i++) {
					Stock stk = myStockVec.elementAt(i);
					if (stk.getType() != null && stk.getType().startsWith("s")) {
						stockRecoder.add(stk);
					}
				}
				simperAdapter.notifyDataSetChanged();
			}
		}
	};

	/**
	 * 自定义键盘回调
	 */
	@Override
	public void OnAction(int type, Object arg) {
		if (type == 8) {
			doKeyBoardAction(arg);
		}
	}

	// /自定义软键盘
	private ChangeCodeLayout m_ChangeCodeLayout = null;
	private LinearLayout m_keyboardLayout;

	private void initMyKeyBoard() {
		if (m_ChangeCodeLayout == null) {
			
			m_ChangeCodeLayout = new ChangeCodeLayout(ctx, getScreenW(), getScreenH() * 40 / 100, getScreenH() * 2 / 100);
		}
		m_keyboardLayout = (LinearLayout) view.findViewById(R.id.inqu_keyboard);
		m_keyboardLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		m_ChangeCodeLayout.setListener(this);
		m_ChangeCodeLayout.setLinearLayout(m_keyboardLayout);
		m_ChangeCodeLayout.setShow(true);// true模式是显示数字
		// m_keyboardLayout.setVisibility(View.GONE);
	}

	private void doKeyBoardAction(Object type) {
		int aType = (Integer) type;
		String str = "";

		if (aType == ChangeCodeLayout.BUTTON_FUNCTION_STRING || aType == ChangeCodeLayout.BUTTON_FUNCTION_STAR) {
			str = m_ChangeCodeLayout.getMessage();
			editText.setText(str);
			editText.setSelection(str.length());
		} else if (aType == ChangeCodeLayout.BUTTON_FUNCTION_SRARCH) {
			onItemClick(null, null, 0, 0);
		} else if (aType == ChangeCodeLayout.BUTTON_FUNCTION_HIDE) {
			if (m_keyboardLayout != null) {
				m_keyboardLayout.setVisibility(View.GONE);
			}
		} else if (aType == ChangeCodeLayout.BUTTON_FUNCTION_OK) {
			// Toast.makeText(this,"确定",Toast.LENGTH_SHORT).show();
		} else if (aType == ChangeCodeLayout.BUTTON_FUNCTION_CLEAR) {
			m_ChangeCodeLayout.setMessage("");
			editText.setText("");
			editText.setSelection("".length());
		} else {
			str = m_ChangeCodeLayout.getMessage();
			editText.setText(str);
			editText.setSelection(str.length());
		}
	}

}
