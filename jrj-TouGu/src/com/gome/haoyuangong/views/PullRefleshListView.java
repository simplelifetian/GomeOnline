package com.gome.haoyuangong.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.haoyuangong.AppOper;
import com.gome.haoyuangong.R;

/**
 * 自定义listview,下拉更新数据，底部点更多加载新数据
 * @author guohuiz
 * **/
@SuppressLint("SimpleDateFormat")
public class PullRefleshListView extends ListView implements OnScrollListener {

  private static final String TAG = "listview";
  private boolean isMoreFlag = true;
  private final static int RELEASE_To_REFRESH = 0;//松手状态
  private final static int PULL_To_REFRESH = 1;//回弹状态
  private final static int REFRESHING = 2;//刷新状态
  private final static int DONE = 3;//完成状态
  private final static int LOADING = 4;//等待状态

  // 实际的padding的距离与界面上偏移距离的比例
  private final static int RATIO = 3;
  private LayoutInflater inflater;//加载布局对象
  private LinearLayout headView;//下拉刷新头部
  private View moreView;// ListView底部View
  private TextView mAddMoreDataTextView;//添加更多数据文字对象
  private ProgressBar mAddMoreDataProgressBar;//添加更多数据进度条
  private int mCurPage;//当前页码

  private TextView tipsTextview;//头部提示文字
  private TextView lastUpdatedTextView;//头部上次更新时间
  private ImageView arrowImageView;//头部箭头图标
  private ProgressBar progressBar;//头部等待进度条


  private RotateAnimation animation;//箭头下拉动画
  private RotateAnimation reverseAnimation;//箭头回弹动画

  // 用于保证startY的值在一个完整的touch事件中只被记录一次
  private boolean isRecored;

//  private int headContentWidth;//头部高度
  private int headContentHeight;//头部宽度
  private int startY;//记录下拉起始高度
  private int firstItemIndex;//显示第一项数据索引

  private int state;//当前列表状态

  private boolean isBack;//是否回弹

  private OnRefreshListener refreshListener;//刷新监听回调对象

  private boolean isRefreshable;//是否刷新完成
  
  private AppOper m_listener;//回调监听对象
  
  public void setListener(AppOper appOper){
    m_listener = appOper;
  }

  public PullRefleshListView(Context context) {
    this(context,null);
  }

  public PullRefleshListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  /**
   * 初始列表相关数据
   * **/
  private void init(Context context,AttributeSet attrs) {
    mCurPage=1;
    setCacheColorHint(context.getResources().getColor(R.color.transparent));
    inflater = LayoutInflater.from(context);

    headView = (LinearLayout) inflater.inflate(R.layout.list_head, null);

    arrowImageView = (ImageView) headView
        .findViewById(R.id.head_arrowImageView);
    arrowImageView.setMinimumWidth(70);
    arrowImageView.setMinimumHeight(50);
    progressBar = (ProgressBar) headView
        .findViewById(R.id.head_progressBar);
    tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
    lastUpdatedTextView = (TextView) headView
        .findViewById(R.id.head_lastUpdatedTextView);

    measureView(headView);
    headContentHeight = headView.getMeasuredHeight();
//    headContentWidth = headView.getMeasuredWidth();

    headView.setPadding(0, -1 * headContentHeight, 0, 0);
    headView.invalidate();

//    Log.v("size", "width:" + headContentWidth + " height:"
//        + headContentHeight);
//    TypedArray a　=　context.obtainStyledAttributes(attrs,R.styleable.MyView);
    TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.RefreshList);
    int topHeadHeight = a.getDimensionPixelSize(R.styleable.RefreshList_top_head_height, -1);
    if(topHeadHeight != -1){
    	View topView = new View(context);
    	AbsListView.LayoutParams l = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,topHeadHeight);
    	topView.setLayoutParams(l);
    	addHeaderView(topView);
    }
    a.recycle();
    
    addHeaderView(headView);
    setOnScrollListener(this);
    
    // 实例化底部布局
    moreView = inflater.inflate(R.layout.moredata, null);
    addFooterView(moreView);
    mAddMoreDataTextView = (TextView) moreView.findViewById(R.id.bt_load);
    mAddMoreDataProgressBar = (ProgressBar) moreView.findViewById(R.id.pg);
    
    mAddMoreDataTextView.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
        mAddMoreDataProgressBar.setVisibility(View.VISIBLE);// 将进度条可见
        mAddMoreDataTextView.setVisibility(View.GONE);// 按钮不可见
        
        if(m_listener!=null){
          m_listener.OnAction(0, ++mCurPage);
        }
      }
    });

    animation = new RotateAnimation(0, -180,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    animation.setInterpolator(new LinearInterpolator());
    animation.setDuration(250);
    animation.setFillAfter(true);

    reverseAnimation = new RotateAnimation(-180, 0,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    reverseAnimation.setInterpolator(new LinearInterpolator());
    reverseAnimation.setDuration(200);
    reverseAnimation.setFillAfter(true);

    state = DONE;
    isRefreshable = false;
  }
  public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
      int arg3) {
    firstItemIndex = firstVisiableItem;
  }

  public void onScrollStateChanged(AbsListView view, int scrollState) {
    // 当不滚动时
    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
      //判断是否滚动到底部
      if (view.getLastVisiblePosition() == view.getCount() - 1) {
        if(isMoreFlag){
          mAddMoreDataTextView.setVisibility(View.VISIBLE);
          mAddMoreDataProgressBar.setVisibility(View.VISIBLE);// 将进度条可见
          if(m_listener!=null){
            m_listener.OnAction(0, ++mCurPage);
          }
        }
      }
    }
  }

  public boolean onTouchEvent(MotionEvent event) {

    if (isRefreshable) {
      switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (firstItemIndex == 0 && !isRecored) {
          isRecored = true;
          startY = (int) event.getY();
//          Log.v(TAG, "在down时候记录当前位置‘");
        }
        break;

      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:

        if (state != REFRESHING && state != LOADING) {
          if (state == DONE) {
            // 什么都不做
          }
          if (state == PULL_To_REFRESH) {
            state = DONE;
            changeHeaderViewByState();

//            Log.v(TAG, "由下拉刷新状态，到done状态");
          }
          if (state == RELEASE_To_REFRESH) {
            state = REFRESHING;
            changeHeaderViewByState();
            onRefresh();

//            Log.v(TAG, "由松开刷新状态，到done状态");
          }
        }

        isRecored = false;
        isBack = false;

        break;

      case MotionEvent.ACTION_MOVE:
        int tempY = (int) event.getY();

        if (!isRecored && firstItemIndex == 0) {
//          Log.v(TAG, "在move时候记录下位置");
          isRecored = true;
          startY = tempY;
        }

        if (state != REFRESHING && isRecored && state != LOADING) {

          // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

          // 可以松手去刷新了
          if (state == RELEASE_To_REFRESH) {

            setSelection(0);

            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
            if (((tempY - startY) / RATIO < headContentHeight)
                && (tempY - startY) > 0) {
              state = PULL_To_REFRESH;
              changeHeaderViewByState();

//              Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
            }
            // 一下子推到顶了
            else if (tempY - startY <= 0) {
              state = DONE;
              changeHeaderViewByState();

//              Log.v(TAG, "由松开刷新状态转变到done状态");
            }
            // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
            else {
              // 不用进行特别的操作，只用更新paddingTop的值就行了
            }
          }
          // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
          if (state == PULL_To_REFRESH) {

            setSelection(0);

            // 下拉到可以进入RELEASE_TO_REFRESH的状态
            if ((tempY - startY) / RATIO >= headContentHeight) {
              state = RELEASE_To_REFRESH;
              isBack = true;
              changeHeaderViewByState();

//              Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
            }
            // 上推到顶了
            else if (tempY - startY <= 0) {
              state = DONE;
              changeHeaderViewByState();

//              Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
            }
          }
          // done状态下
          if (state == DONE) {
//            Log.v(TAG, "pos="+(tempY - startY));
            if (tempY - startY > 0) {
              state = PULL_To_REFRESH;
              changeHeaderViewByState();
            }
          }

          // 更新headView的size
          if (state == PULL_To_REFRESH) {
            headView.setPadding(0, -1 * headContentHeight
                + (tempY - startY) / RATIO, 0, 0);

          }

          // 更新headView的paddingTop
          if (state == RELEASE_To_REFRESH) {
            headView.setPadding(0, (tempY - startY) / RATIO
                - headContentHeight, 0, 0);
          }

        }
        
        break;
      }
    }

    return super.onTouchEvent(event);
  }

  /**
   * 当状态改变时候，调用该方法，以更新界面
   */
  public void changeHeaderViewByState() {
    switch (state) {
    case RELEASE_To_REFRESH:
      arrowImageView.setVisibility(View.VISIBLE);
      progressBar.setVisibility(View.GONE);
      tipsTextview.setVisibility(View.VISIBLE);
      lastUpdatedTextView.setVisibility(View.VISIBLE);

      arrowImageView.clearAnimation();
      arrowImageView.startAnimation(animation);

      tipsTextview.setText("松开刷新");

      Log.v(TAG, "当前状态，松开刷新");
      break;
    case PULL_To_REFRESH:
      progressBar.setVisibility(View.GONE);
      tipsTextview.setVisibility(View.VISIBLE);
      lastUpdatedTextView.setVisibility(View.VISIBLE);
      arrowImageView.clearAnimation();
      arrowImageView.setVisibility(View.VISIBLE);
      // 是由RELEASE_To_REFRESH状态转变来的
      if (isBack) {
        isBack = false;
        arrowImageView.clearAnimation();
        arrowImageView.startAnimation(reverseAnimation);

        tipsTextview.setText("下拉刷新");
      } else {
        tipsTextview.setText("下拉刷新");
      }
//      Log.v(TAG, "当前状态，下拉刷新");
      break;

    case REFRESHING:

      headView.setPadding(0, 0, 0, 0);

      progressBar.setVisibility(View.VISIBLE);
      arrowImageView.clearAnimation();
      arrowImageView.setVisibility(View.GONE);
      tipsTextview.setText("正在刷新...");
      lastUpdatedTextView.setVisibility(View.VISIBLE);

//      Log.v(TAG, "当前状态,正在刷新...");
      break;
    case DONE:
      headView.setPadding(0, -1 * headContentHeight, 0, 0);

      progressBar.setVisibility(View.GONE);
      arrowImageView.clearAnimation();
      arrowImageView.setImageResource(R.drawable.goicon);
      tipsTextview.setText("下拉刷新");
      lastUpdatedTextView.setVisibility(View.VISIBLE);

//      Log.v(TAG, "当前状态，done");
      break;
    }
  }

  /**
   * 更多按钮恢复正常状态
   * **/
  public void RefreshMoreDataButton(){
    mAddMoreDataTextView.setVisibility(View.VISIBLE);
    mAddMoreDataProgressBar.setVisibility(View.GONE);
    state = DONE;
  }
  
  /**
   *隐藏更多按钮
   * **/
  public void HideMoreDataButton(){
    try {
      if(isMoreFlag){
        isMoreFlag = false;
        if(moreView==null){return;};
        boolean result = removeFooterView(moreView);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   *隐藏更多按钮
   * **/
  public void ShowMoreDataButton(){
    if(!isMoreFlag){
      mCurPage=1;
      isMoreFlag = true;
      addFooterView(moreView);
    }
  }
  
  public void setonRefreshListener(OnRefreshListener refreshListener) {
    this.refreshListener = refreshListener;
    isRefreshable = true;
  }

  public interface OnRefreshListener {
    public void onRefresh();
  }

  public void onRefreshComplete(String str) {
    state = DONE;
//    lastUpdatedTextView.setText(str);//("最近更新:" + new Date().toLocaleString());
//    changeHeaderViewByState();
    Message msg = stateUpdateHandler.obtainMessage(0);
    msg.obj = str;
    stateUpdateHandler.sendMessage(msg);
  }

  private void onRefresh() {
    if (refreshListener != null) {
      refreshListener.onRefresh();
    }
  }
  
  public void ReflashData(){
    if(state!=REFRESHING){
//      setSelection(0);
      state = REFRESHING;  //将标量设置为，正在刷新
      changeHeaderViewByState();
//      prepareForRefresh();  //准备刷新
      onRefresh();   //刷新
    }
  }

  /**
   * 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
   * @param child
   */
  private void measureView(View child) {
    ViewGroup.LayoutParams p = child.getLayoutParams();
    if (p == null) {
      p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
    int lpHeight = p.height;
    int childHeightSpec;
    if (lpHeight > 0) {
      childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
          MeasureSpec.EXACTLY);
    } else {
      childHeightSpec = MeasureSpec.makeMeasureSpec(0,
          MeasureSpec.UNSPECIFIED);
    }
    child.measure(childWidthSpec, childHeightSpec);
  }
  SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm");
  public void setAdapter(BaseAdapter adapter) {
    mCurPage=1;
    lastUpdatedTextView.setText("最近更新:" + format.format(new Date()));
    super.setAdapter(adapter);
  }
//  public void setIsMoreFlag(boolean a_isMoreFlag){
//    if(a_isMoreFlag==isMoreFlag){
//      return;
//    }
//    isMoreFlag = a_isMoreFlag;
//    if(!isMoreFlag){
//      mAddMoreDataTextView.setVisibility(View.GONE);
//      mAddMoreDataProgressBar.setVisibility(View.GONE);// 将进度条隐藏
//      HideMoreDataButton();
//    }else{
//      mAddMoreDataTextView.setVisibility(View.INVISIBLE);
//      mAddMoreDataProgressBar.setVisibility(View.VISIBLE);// 将进度条显示
//      ShowMoreDataButton();
//    }
//  }
  
  Handler stateUpdateHandler = new Handler(){
	  @Override
	  public void handleMessage(Message msg){
		  
		    lastUpdatedTextView.setText((String)msg.obj);//("最近更新:" + new Date().toLocaleString());
		    changeHeaderViewByState();
	  }
  };
}
