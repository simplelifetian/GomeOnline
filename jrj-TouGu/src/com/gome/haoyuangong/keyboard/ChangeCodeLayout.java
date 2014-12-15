package com.gome.haoyuangong.keyboard;

import com.gome.haoyuangong.AppInfo;
import com.gome.haoyuangong.AppOper;
import com.gome.haoyuangong.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;


/**
 * 自定义键盘布局
 * @author ghb
 * **/
@SuppressLint("ViewConstructor")
public class ChangeCodeLayout extends LinearLayout {
  public final static int BUTTON_FUNCTION_F3=          0;//按钮标签f3
  public final static int BUTTON_FUNCTION_F4=          1;//按钮标签f4
  public final static int BUTTON_FUNCTION_STAR=        2;//按钮标签*
  public final static int BUTTON_FUNCTION_BACK=        3;//按钮标签退格
  public final static int BUTTON_FUNCTION_ABC=         4;//按钮标签英语切换
  public final static int BUTTON_FUNCTION_HIDE=        5;//按钮标签隐藏当前键盘
  public final static int BUTTON_FUNCTION_OK=          6;//按钮标签确定
  public final static int BUTTON_FUNCTION_123=         7;//按钮标签数字切换
  public final static int BUTTON_FUNCTION_CLEAR=       8;//按钮标签清空按键
  public final static int BUTTON_FUNCTION_000=         9;//按钮标签000
  public final static int BUTTON_FUNCTION_300=         10;//按钮标签300
  public final static int BUTTON_FUNCTION_002=         11;//按钮标签002
  public final static int BUTTON_FUNCTION_600=         12;//按钮标签600
  public final static int BUTTON_FUNCTION_ST=          13;//按钮标签st
  public final static int BUTTON_FUNCTION_STARST=      14;//按钮标签*st
  public final static int BUTTON_FUNCTION_SRARCH=      15;//按钮标签搜索
  public final static int BUTTON_FUNCTION_601=         16;//按钮标签601
  public final static int BUTTON_FUNCTION_SHIFT=       17;//按钮标签shift
  public final static int BUTTON_FUNCTION_STRING=      999;//按钮标签字符串

  private static LayoutParams WAP_WAP_LAYOUTPARAMS = new LayoutParams(LayoutParams.WRAP_CONTENT,
      LayoutParams.WRAP_CONTENT);

  private Context context;
  private final int TextFontColor = Color.BLACK;

  LinearLayout mTotalNumLinearLayout;// 数字总布局
  LinearLayout mTotalCharLinearLayout;// 字母总布局

  LinearLayout[] NumLayoutArray;//每行数字按钮布局
  LinearLayout[][] NumButtonLayoutArray;//每个数字按钮布局
  LinearLayout[] CharLayoutArray;//每行字母按钮布局
  LinearLayout[][] CharButtonLayoutArray;//每个字母按钮布局

  MyButton[][] NumButtonArray;//数字按钮数组
  MyButton[][] CharButtonArray;//字母按钮数组

  MyButton searchNum;
  MyButton searchChar;
  ///  触摸软键盘数字按键
  private final String[][] INPUT_NUMBER_ARRAY = {
      { "600", "1", "2", "3","back"},
      { "601", "4", "5", "6","clear"},
      { "000", "7", "8", "9","hide"},
      { "002", "300","0", "abc","search"},
  };

  /// 触摸软键盘大写字母按键
  private final String[][] INPUT_UPPER_CASE_ARRAY = {
      { "Q", "W", "E", "R", "T","Y","U","I","O","P"},
      { "A", "S", "D", "F", "G","H","J","K","L"},
      { "ST", "Z", "X", "C", "V","B","N","M","back"},
      { "123"," ", "search"},
  };
  private int ChangeCode_width;//键盘宽度，由构造获得
  private int ChangeCode_height;//键盘高度，由构造获得
  private int ChangeCode_topGap;//键盘上下留边，由构造获得
  private int NumFontSize;//数字显示字符大小，由构造获得
//  private int CharFontSize;//字母显示字符大小，由构造获得
  private int OPTFontSize;//操作显示字符大小，由构造获得

  private int gap_x;//按钮左右间距，在构造中由键盘宽度计算得出，默认是2%
  private int gap_y;//按钮上下间距，在构造中由键盘高度计算得出，默认是2%

  private StringBuffer mStringBuff;//当前存储字符变量
//  private int mCurType;//当前显示类型：0 数字   1字母
  private AppOper m_appoper;//
  public ChangeCodeLayout(Context context,int w,int h,int topGap) {
    super(context);
    this.context=context;
    ChangeCode_width=w;
    ChangeCode_height=h;
    gap_x=(w*2)/100;
    gap_y=((int)(h*3.5f))/100;
    ChangeCode_topGap=topGap;

    switch(AppInfo.mScreenType){
    case AppInfo.SCREEN_SMALL:{
      this.NumFontSize=AppInfo.dip2px(context, 9);
      this.OPTFontSize = AppInfo.dip2px(context, 8);
      break;
    }
    case AppInfo.SCREEN_MEDIUM:{
      this.NumFontSize=AppInfo.dip2px(context, 9);
      this.OPTFontSize = AppInfo.dip2px(context, 8);
      break;
    }
    case AppInfo.SCREEN_LARGE:{
      this.NumFontSize=AppInfo.dip2px(context, 9);
      this.OPTFontSize = AppInfo.dip2px(context, 9);
      break;
    }
    case AppInfo.SCREEN_BIGLARGE:
    case AppInfo.SCREEN_QUADHD:
    {
      this.NumFontSize=AppInfo.dip2px(context, 5);
      this.OPTFontSize = AppInfo.dip2px(context, 4);
      break;
    }
    default:{
      this.NumFontSize=AppInfo.dip2px(context, 9);
      this.OPTFontSize = AppInfo.dip2px(context, 8);
      break;
    }
    }
    
//    if(mScreenWidth>=1080){
//      this.NumFontSize=AppInfo.dip2px(context, 5);
//      this.OPTFontSize = AppInfo.dip2px(context, 4);
//    }else if(mScreenWidth>=720){
//      this.NumFontSize=AppInfo.dip2px(context, 9);
//      this.OPTFontSize = AppInfo.dip2px(context, 9);
//    }else{
//      this.NumFontSize=AppInfo.dip2px(context, 9);
//      this.OPTFontSize = AppInfo.dip2px(context, 8);
//    }

    mStringBuff=new StringBuffer();
    initLayout();
    initButton();
    initView();
    setOrientation(LinearLayout.VERTICAL);
//    mCurType=0;


  }

  /**
   * 设置监听回调对象
   * **/
  public void setListener(AppOper appoper){
    this.m_appoper=appoper;
  }

  /**初始总布局和行布局**/
  private void initLayout() {
    mTotalNumLinearLayout = new LinearLayout(context);
    mTotalNumLinearLayout.setBackgroundColor(0xFFDEDFE3);
    mTotalNumLinearLayout.setOrientation(LinearLayout.VERTICAL);//布局垂直排列
    mTotalCharLinearLayout = new LinearLayout(context);
    mTotalCharLinearLayout.setBackgroundColor(0xFFDEDFE3);
    mTotalCharLinearLayout.setOrientation(LinearLayout.VERTICAL);//布局垂直排列
    mTotalCharLinearLayout.setLayoutParams(WAP_WAP_LAYOUTPARAMS);
    NumLayoutArray=new LinearLayout[INPUT_NUMBER_ARRAY.length];
    for(int i=0;i<INPUT_NUMBER_ARRAY.length;i++){
      NumLayoutArray[i] = new LinearLayout(context);
      NumLayoutArray[i].setOrientation(LinearLayout.HORIZONTAL);
      if(i==0){
        NumLayoutArray[i].setPadding(0, ChangeCode_topGap, 0, 0);
      } else if(i==(INPUT_NUMBER_ARRAY.length-1)){
        NumLayoutArray[i].setPadding(0, 0, 0, ChangeCode_topGap-gap_y);
      } 
    }

    CharLayoutArray=new LinearLayout[INPUT_UPPER_CASE_ARRAY.length];
    for(int i=0;i<INPUT_UPPER_CASE_ARRAY.length;i++){
      CharLayoutArray[i] = new LinearLayout(context);
      CharLayoutArray[i].setOrientation(LinearLayout.HORIZONTAL);
      if(i==0){
        CharLayoutArray[i].setPadding(0, ChangeCode_topGap, 0, 0);
      } else if(i==(INPUT_UPPER_CASE_ARRAY.length-1)){
        CharLayoutArray[i].setPadding(0, 0, 0, ChangeCode_topGap-gap_y);
      }
    }
  }

  public int getFontHeight(int fontSize) {
    Paint m_paint = new Paint();
    m_paint.setTextSize(fontSize);
    FontMetrics fm = m_paint.getFontMetrics();
    return (int) Math.ceil(fm.descent - fm.ascent) ;
  }

  /**得到字符宽度**/
  private int getColumnWidth(int fontSize,String data){
    Paint m_paint = new Paint();
    m_paint.setTextSize(fontSize);
    return (int) m_paint.measureText(data);

  }


  /**初始化所有按钮，放入布局进行排版**/
  private void initButton() {
    int NumButton_w=(ChangeCode_width-(INPUT_NUMBER_ARRAY[0].length+1)*gap_x)/INPUT_NUMBER_ARRAY[0].length;
    int NumButton_h=(ChangeCode_height-ChangeCode_topGap*2-(INPUT_NUMBER_ARRAY.length-1)*gap_y)/INPUT_NUMBER_ARRAY.length;
    LayoutParams NumParams = new LayoutParams(NumButton_w,NumButton_h);
    NumButtonArray=new MyButton[INPUT_NUMBER_ARRAY.length][INPUT_NUMBER_ARRAY[0].length];
    NumButtonLayoutArray=new LinearLayout[INPUT_NUMBER_ARRAY.length][INPUT_NUMBER_ARRAY[0].length];
    for (int i = 0; i < INPUT_NUMBER_ARRAY.length; i++) {// 上面的数字
      for (int j = 0; j < INPUT_NUMBER_ARRAY[i].length; j++) {
        NumButtonArray[i][j] = new MyButton(context);
        NumButtonLayoutArray[i][j] = new LinearLayout(context);
        if(j==INPUT_NUMBER_ARRAY[i].length-1){
          NumButtonLayoutArray[i][j].setPadding(gap_x, 0, gap_x, gap_y);
        } else {
          NumButtonLayoutArray[i][j].setPadding(gap_x, 0, 0, gap_y);
        }
        if( INPUT_NUMBER_ARRAY[i][j].equals("hide"))
        {
          NumButtonArray[i][j].setText("隐藏");
          NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          NumButtonArray[i][j].setId(BUTTON_FUNCTION_HIDE);
        }else if( INPUT_NUMBER_ARRAY[i][j].equals("search"))
        {
          NumButtonArray[i][j].setText("搜索");
          NumButtonArray[i][j].setId(BUTTON_FUNCTION_SRARCH);
          NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          searchNum = NumButtonArray[i][j];
        }else if( INPUT_NUMBER_ARRAY[i][j].equals("clear"))
        {
          NumButtonArray[i][j].setText("清空");
          NumButtonArray[i][j].setId(BUTTON_FUNCTION_CLEAR);
          NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
        }
        else if( INPUT_NUMBER_ARRAY[i][j].equals("abc"))
        {
          NumButtonArray[i][j].setId(BUTTON_FUNCTION_ABC);
          NumButtonArray[i][j].setText("ABC");
          NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
        }
        else
        if( INPUT_NUMBER_ARRAY[i][j].equals("back")){
          NumButtonArray[i][j].setText("");
          NumButtonArray[i][j].setId(BUTTON_FUNCTION_BACK);
          NumButtonArray[i][j].onSetBmp(R.drawable.keyboard_del, R.drawable.keyboard_del);
        } 
        else {
          if(INPUT_NUMBER_ARRAY[i][j].equals("600")){
            NumButtonArray[i][j].setId(BUTTON_FUNCTION_600);
            NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          }else if(INPUT_NUMBER_ARRAY[i][j].equals("601")){
            NumButtonArray[i][j].setId(BUTTON_FUNCTION_601);
            NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          }else if(INPUT_NUMBER_ARRAY[i][j].equals("300")){
            NumButtonArray[i][j].setId(BUTTON_FUNCTION_300);
            NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          }else if(INPUT_NUMBER_ARRAY[i][j].equals("000")){
            NumButtonArray[i][j].setId(BUTTON_FUNCTION_000);
            NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          }else if(INPUT_NUMBER_ARRAY[i][j].equals("002")){
            NumButtonArray[i][j].setId(BUTTON_FUNCTION_002);
            NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          }else{
            NumButtonArray[i][j].setId(BUTTON_FUNCTION_STRING);
            NumButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_n);
          }
          NumButtonArray[i][j].setText(INPUT_NUMBER_ARRAY[i][j]);
        }

        NumButtonArray[i][j].setLayoutParams(NumParams);
        if( INPUT_NUMBER_ARRAY[i][j].equals("search")||
            INPUT_NUMBER_ARRAY[i][j].equals("abc")||
            INPUT_NUMBER_ARRAY[i][j].equals("clear")||
            INPUT_NUMBER_ARRAY[i][j].equals("hide")
        )
        {
          NumButtonArray[i][j].setTextSize(OPTFontSize);
        }else{
          NumButtonArray[i][j].setTextSize(NumFontSize);
          NumButtonArray[i][j].setTypeface(Typeface.DEFAULT_BOLD);
        }
        NumButtonArray[i][j].setTextColor(TextFontColor);
        NumButtonArray[i][j].setOnClickListener(new ButtonClickEvent());
        NumButtonLayoutArray[i][j].addView(NumButtonArray[i][j]);
      }
    }
    int CharButton_wB=(ChangeCode_width-(INPUT_UPPER_CASE_ARRAY[INPUT_UPPER_CASE_ARRAY.length-1].length+1)*gap_x)/4;
    int CharButton_w=(ChangeCode_width-(INPUT_UPPER_CASE_ARRAY[0].length+1)*gap_x)/INPUT_UPPER_CASE_ARRAY[0].length;
    int CharButton_h=(ChangeCode_height-ChangeCode_topGap*2-(INPUT_UPPER_CASE_ARRAY.length-1)*gap_y)/INPUT_UPPER_CASE_ARRAY.length;
    LayoutParams CharParams = new LayoutParams(CharButton_w,CharButton_h);
    LayoutParams CharParams2 = new LayoutParams((int)(CharButton_w*1.5f+gap_x/2),CharButton_h);
    LayoutParams CharParams3 = new LayoutParams(CharButton_wB,CharButton_h);
    LayoutParams CharParams4 = new LayoutParams(CharButton_wB*2,CharButton_h);
    CharButtonArray=new MyButton[INPUT_UPPER_CASE_ARRAY.length][INPUT_UPPER_CASE_ARRAY[0].length];
    CharButtonLayoutArray=new LinearLayout[INPUT_UPPER_CASE_ARRAY.length][INPUT_UPPER_CASE_ARRAY[0].length];
    for (int i = 0; i < INPUT_UPPER_CASE_ARRAY.length; i++) {// 上面的数字
      for (int j = 0; j < INPUT_UPPER_CASE_ARRAY[i].length; j++) {
        CharButtonArray[i][j] = new MyButton(context);
        CharButtonLayoutArray[i][j] = new LinearLayout(context);
        if(j==0){
          CharButtonLayoutArray[i][j].setPadding(gap_x, 0, 0, gap_y);
        } else if(j==INPUT_UPPER_CASE_ARRAY[i].length-1){
          CharButtonLayoutArray[i][j].setPadding(gap_x, 0, gap_x, gap_y);
        } else {
          CharButtonLayoutArray[i][j].setPadding(gap_x, 0, 0, gap_y);
        }
        if(INPUT_UPPER_CASE_ARRAY[i][j].equals("search")){
          CharButtonArray[i][j].setTextSize(OPTFontSize);
          CharButtonArray[i][j].setText("搜索");
          CharButtonArray[i][j].setTextColor(TextFontColor);
          CharButtonArray[i][j].setId(BUTTON_FUNCTION_SRARCH);
          searchChar = CharButtonArray[i][j];
        }else if(INPUT_UPPER_CASE_ARRAY[i][j].equals(" ")){
          CharButtonArray[i][j].setTextSize(OPTFontSize);
          CharButtonArray[i][j].setText("空格");
          CharButtonArray[i][j].setTextColor(TextFontColor);
          CharButtonArray[i][j].setId(BUTTON_FUNCTION_STRING);
          CharButtonArray[i][j].onSetText(INPUT_UPPER_CASE_ARRAY[i][j],0,0,10,TextFontColor);
        }else if(INPUT_UPPER_CASE_ARRAY[i][j].equals("123")){
          CharButtonArray[i][j].setTextSize(OPTFontSize);
          CharButtonArray[i][j].setText("123");
          CharButtonArray[i][j].setTextColor(TextFontColor);
          CharButtonArray[i][j].setId(BUTTON_FUNCTION_123);
        }else{
          if(!INPUT_UPPER_CASE_ARRAY[i][j].equals("back")&&
              !INPUT_UPPER_CASE_ARRAY[i][j].equals("ST"))
          {
            int font =AppInfo.dip2px(context, 20);
            int off_x=(CharButton_w-getColumnWidth(font,INPUT_UPPER_CASE_ARRAY[i][j]))/2;
            int off_y=(CharButton_h-getFontHeight(font))+AppInfo.dip2px(context, 4);
            CharButtonArray[i][j].onSetText(INPUT_UPPER_CASE_ARRAY[i][j],off_x,off_y+0,font,TextFontColor);
            CharButtonArray[i][j].setId(BUTTON_FUNCTION_STRING);
          }
        }
        if(INPUT_UPPER_CASE_ARRAY[i][j].equals("back")||
            INPUT_UPPER_CASE_ARRAY[i][j].equals("123")||
            INPUT_UPPER_CASE_ARRAY[i][j].equals("ST")||
            INPUT_UPPER_CASE_ARRAY[i][j].equals("search")  
        ){
          if(INPUT_UPPER_CASE_ARRAY[i][j].equals("back")){
            CharButtonArray[i][j].setId(BUTTON_FUNCTION_BACK);
            CharButtonArray[i][j].setText("");
            CharButtonArray[i][j].onSetBmp(R.drawable.keyboard_del, R.drawable.keyboard_del);
          }else
          if(INPUT_UPPER_CASE_ARRAY[i][j].equals("ST")){
            CharButtonArray[i][j].setId(BUTTON_FUNCTION_ST);
            CharButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
            CharButtonArray[i][j].setText("ST");
            CharButtonArray[i][j].setTextColor(TextFontColor);
          }else{
            CharButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_s);
          }
        }else{
          CharButtonArray[i][j].onSetBmp(R.drawable.numbuttonbg, R.drawable.numbuttonbg_n);
        }
        
        if(i==INPUT_UPPER_CASE_ARRAY.length-1){
          if(INPUT_UPPER_CASE_ARRAY[i][j].equals(" ")){
            CharButtonArray[i][j].setLayoutParams(CharParams4);
          }else{
            CharButtonArray[i][j].setLayoutParams(CharParams3);
          }
//          CharButtonArray[i][j].setGravity(Gravity.CENTER_HORIZONTAL);
        }else if(INPUT_UPPER_CASE_ARRAY[i][j].equals("ST")||INPUT_UPPER_CASE_ARRAY[i][j].equals("back")){
          CharButtonArray[i][j].setLayoutParams(CharParams2);
        }else{
          if(INPUT_UPPER_CASE_ARRAY[i][j].equals("A")){
            //            CharParams.leftMargin = CharButton_w+gap_x;
            //            CharParams.setMargins(CharButton_w+gap_x, 0, 0, 0);
            CharButtonLayoutArray[i][j].setPadding((CharButton_w+gap_x)/2+gap_x, 0, 0, 0);
          }
          CharButtonArray[i][j].setLayoutParams(CharParams);
        }
//        CharButtonArray[i][j].setTextSize(CharFontSize);
        CharButtonArray[i][j].setOnClickListener(new ButtonClickEvent());
        CharButtonLayoutArray[i][j].addView(CharButtonArray[i][j]);
      }
    }
  }

  /**外部得到当前输入内容，每次接收到正确字符输入事件需要调这个方法得到当前实现总字符**/
  public String getMessage(){
    if(mStringBuff!=null){
      return mStringBuff.toString();
    }
    return "";
  }

  public void setMessage(String str){
    mStringBuff.delete(0, mStringBuff.length());
    mStringBuff.append(str);
  }

  /**按钮监听事件，处理文字输入，字符串组合以及限制，特殊按钮回调功能**/
  class ButtonClickEvent implements OnClickListener {
    public void onClick(View v) {
      MyButton curButton=(MyButton)v;
      int buttonID=curButton.getId();
      String str=curButton.getMyText();
      if(str==null){
        str = curButton.getText().toString();
      }
      if(buttonID==BUTTON_FUNCTION_ABC){
        setShow(false);
      } else if(buttonID==BUTTON_FUNCTION_123){
        setShow(true);
      } else if(buttonID==BUTTON_FUNCTION_BACK){
        if(mStringBuff.length()>0){
          mStringBuff.replace(mStringBuff.length()-1, mStringBuff.length(), "");
        }
        m_appoper.OnAction( 8, BUTTON_FUNCTION_STRING);
        //foo.NumClickOnCallBack(BUTTON_FUNCTION_STRING);
      } else {
        int strLength=0;
        if(str!=null){
          strLength=str.length();
        }
        if(mStringBuff.length()<6 && (strLength+mStringBuff.length())<7){
          if(buttonID==BUTTON_FUNCTION_STRING){
            mStringBuff.append(str);
          } else if(buttonID==BUTTON_FUNCTION_STAR){
            mStringBuff.append("*");
          }
          if(mStringBuff.length()<4||((buttonID==ChangeCodeLayout.BUTTON_FUNCTION_ST&&mStringBuff.length()<5))){
            if(buttonID==ChangeCodeLayout.BUTTON_FUNCTION_600)
            {
              mStringBuff.append("600");
            }else if(buttonID==ChangeCodeLayout.BUTTON_FUNCTION_601)
            {
              mStringBuff.append("601");
            }else if(buttonID==ChangeCodeLayout.BUTTON_FUNCTION_300){
              mStringBuff.append("300");
            }else if(buttonID==ChangeCodeLayout.BUTTON_FUNCTION_000){
              mStringBuff.append("000");
            }else if(buttonID==ChangeCodeLayout.BUTTON_FUNCTION_002){
              mStringBuff.append("002");
            }else if(buttonID==ChangeCodeLayout.BUTTON_FUNCTION_ST){
              mStringBuff.append("ST");
            }else if(buttonID==ChangeCodeLayout.BUTTON_FUNCTION_STARST){
              mStringBuff.append("*ST");
            }
          }
          m_appoper.OnAction(8, buttonID);
          //foo.NumClickOnCallBack(buttonID);
        }
        if(
            buttonID==BUTTON_FUNCTION_HIDE ||
            buttonID==BUTTON_FUNCTION_OK ||
            buttonID==BUTTON_FUNCTION_SRARCH||
            buttonID==BUTTON_FUNCTION_CLEAR
            ){
          m_appoper.OnAction(8, buttonID);
          //foo.NumClickOnCallBack(buttonID);
        }
      }
    }
  }

  /**将按钮布局依次加入行布局，之后将行布局加入总布局**/
  private void initView() {
    for (int i = 0; i < INPUT_NUMBER_ARRAY.length; i++) {// 数字
      for (int j = 0; j < INPUT_NUMBER_ARRAY[i].length; j++) {
        NumLayoutArray[i].addView(NumButtonLayoutArray[i][j]);
      }
      mTotalNumLinearLayout.addView(NumLayoutArray[i]);
    }

    for (int i = 0; i < INPUT_UPPER_CASE_ARRAY.length; i++) {// 字母
      for (int j = 0; j < INPUT_UPPER_CASE_ARRAY[i].length; j++) {
        CharLayoutArray[i].addView(CharButtonLayoutArray[i][j]);
      }
      mTotalCharLinearLayout.addView(CharLayoutArray[i]);
    }
  }

  /**设置是否显示数字**/
  LinearLayout myLayout;

  public void setShow(boolean showNum) {
    if (myLayout != null) {
      myLayout.removeAllViews();
      if (showNum) {
//        mCurType = 0;
        myLayout.addView(mTotalNumLinearLayout);
      } else {
//        mCurType = 1;
        myLayout.addView(mTotalCharLinearLayout);
      }
    }
  }

  public void setLinearLayout(LinearLayout ll){
    if(myLayout!=null){
      myLayout.removeAllViews();
    }
    myLayout=ll;
  }

  /**在退出应用的时候调用这个清理整个软键盘数据**/
  public void cleanAll() {
    for(int i=0;i<INPUT_NUMBER_ARRAY.length;i++){
      NumLayoutArray[i] = null;
    }
    for (int i = 0; i < INPUT_NUMBER_ARRAY.length; i++) {// 数字按钮数组
      for (int j = 0; j < INPUT_NUMBER_ARRAY[i].length; j++) {
        NumButtonArray[i][j] = null;
        NumButtonLayoutArray[i][j] = null;
      }
    }
    mTotalNumLinearLayout=null;

    for(int i=0;i<INPUT_NUMBER_ARRAY.length;i++){
      CharLayoutArray[i] = null;
    }
    for (int i = 0; i < INPUT_UPPER_CASE_ARRAY.length; i++) {// 上面的数字
      for (int j = 0; j < INPUT_UPPER_CASE_ARRAY[i].length; j++) {
        CharButtonArray[i][j] = null;
        CharButtonLayoutArray[i][j] = null;
      }
    }
    mTotalCharLinearLayout=null;
  }
  
  public void setSearchAvilable(boolean isAvilable){
  	if(searchChar!=null){
  		searchChar.setEnabled(isAvilable);
  	}
  	if(searchNum!=null){
  		searchNum.setEnabled(isAvilable);
  	}
  }
}
