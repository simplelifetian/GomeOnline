
package com.gome.haoyuangong.update;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.utils.next.Directories;
import com.gome.haoyuangong.utils.next.FileUtils;
import com.gome.haoyuangong.views.DotsView;

public class UpdateDescriptionActivity extends Activity implements OnClickListener {

    private View mDefaultView;
    private View mTipsView;
    private View mTextTipsView;

    private ViewPager mViewPager;
    private DotsView mDotsView;

    public static final String KEY_UPDATE_INFO = "update_info";
    private UpdateInfo mUpdateInfo;

    private boolean mIsDownloaded;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_update_description);

        mUpdateInfo = (UpdateInfo) getIntent().getSerializableExtra(KEY_UPDATE_INFO);
        if (mUpdateInfo == null) {
            finish();
            return;
        }
        initView();

        final String picTips = mUpdateInfo.picTips;
        if (TextUtils.isEmpty(picTips) || !picTips.contains(".zip")) {
            fillTextTipsView();
        } else {
            final String fileName = FileUtils.getFileNameForUrl(picTips);
            final File zipFile = new File(Directories.getApkDir(), fileName);
            if (zipFile.exists()) {
                fillTipsView(zipFile);
            } else {
                fillTextTipsView();
            }
        }
        File file = new File(Directories.getApkPath(mUpdateInfo.versionName));
        if (file.exists()) {
            mIsDownloaded = true;
            ((TextView) findViewById(R.id.ok)).setText("确认安装");
        } else {
            if (!TextUtils.isEmpty(mUpdateInfo.patchUrl)) {
                setBottomTipsView();
            }
        }

    }

    private void fillTextTipsView() {
        if (TextUtils.isEmpty(mUpdateInfo.description)) {
            mTipsView.setVisibility(View.GONE);
            mTextTipsView.setVisibility(View.GONE);
            mDefaultView.setVisibility(View.VISIBLE);
            return;
        }
        mTipsView.setVisibility(View.GONE);
        mTextTipsView.setVisibility(View.VISIBLE);
        mDefaultView.setVisibility(View.GONE);
        TextView tv = (TextView) findViewById(R.id.textTv);
        tv.setText(mUpdateInfo.description);
    }

    private void fillTipsView(File zipFile) {
        ArrayList<String> picFileNameList = getUnZipFileNames(zipFile);
        if (picFileNameList == null || picFileNameList.isEmpty()) {
            fillTextTipsView();
            return;
        }
        Collections.sort(picFileNameList);

        mTipsView.setVisibility(View.VISIBLE);
        mTextTipsView.setVisibility(View.GONE);
        mDefaultView.setVisibility(View.GONE);

        mViewPager.setAdapter(new DescPageAdapter(this, zipFile, picFileNameList));
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mDotsView.setSelected(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mDotsView.init(picFileNameList.size(), 0, 7,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher);
    }

    private void initView() {
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);

        mDefaultView = findViewById(R.id.default_layout);
        mTipsView = findViewById(R.id.tips_layout);
        mTextTipsView = findViewById(R.id.text_tips_layout);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mDotsView = (DotsView) findViewById(R.id.dotsView);
    }
    

    private void setBottomTipsView() {
        float originSize = mUpdateInfo.sizeOriginal;
        float patchSize = mUpdateInfo.sizePatch;
        String originSizeUnit = "B";
        if (originSize > 1024) {
            originSize = originSize / 1024;
            originSizeUnit = "K";
        }
        if (originSize > 1024) {
            originSize = originSize / 1024;
            originSizeUnit = "M";
        }
        String patchSizeUnit = "B";
        if (patchSize > 1024) {
            patchSize = patchSize / 1024;
            patchSizeUnit = "K";
        }
        if (patchSize > 1024) {
            patchSize = patchSize / 1024;
            patchSizeUnit = "M";
        }
        String originSizeStr = String.format("%.1f%s", originSize, originSizeUnit);
        String patchSizeStr = String.format("%.1f%s", patchSize, patchSizeUnit);

        String tip = getString(R.string.update_incremental_update_tip, patchSizeStr,
                originSizeStr);
        SpannableStringBuilder ssb = new SpannableStringBuilder(tip);
        int patchIndex = tip.indexOf(patchSizeStr);
        int orginIndex = tip.indexOf(originSizeStr);
        ssb.setSpan(new ForegroundColorSpan(0xff4dad1d), patchIndex,
                patchIndex + patchSizeStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StrikethroughSpan(), orginIndex, orginIndex + originSizeStr.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView bottomTipTv = (TextView) findViewById(R.id.bottom_tip);
        bottomTipTv.setText(ssb);
        bottomTipTv.setVisibility(View.VISIBLE);
    }

    private ArrayList<String> getUnZipFileNames(File file) {
        ArrayList<String> fileNameList = new ArrayList<String>();
        ZipInputStream inZip = null;
        try {
            inZip = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    fileNameList.add(zipEntry.getName());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inZip != null) {
                try {
                    inZip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.sort(fileNameList);
        return fileNameList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                onOk();
                break;
            case R.id.cancel:
                onCancel();
                break;

            default:
                break;
        }
    }

    private void onCancel() {
        UpdateManager.getInstance().onCancelClick(mUpdateInfo);
        finish();
    }

    private void onOk() {
        UpdateManager.getInstance().onOkClick(mUpdateInfo);
        finish();
    }

    static class DescPageAdapter extends PagerAdapter {

        private Context mContext;
        private File mPicZipFile;
        private ArrayList<String> mPicFileNameList;

        DescPageAdapter(Context context, File picZipFile, ArrayList<String> picFileNameList) {
            mContext = context;
            mPicZipFile = picZipFile;
            mPicFileNameList = picFileNameList;
        }

        @Override
        public int getCount() {
            return mPicFileNameList == null ? 0 : mPicFileNameList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object != null && (object instanceof View)) {
                container.removeView((View) object);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_update_desc, container,
                    false);

            UpdateDescView descView = (UpdateDescView) view.findViewById(R.id.descView);

            Bitmap bm = getBitmap(mPicFileNameList.get(position));

            descView.setDescBitmap(bm);

            container.addView(view);
            return view;
        }

        @SuppressWarnings("resource")
        private Bitmap getBitmap(String picFileName) {
            ZipInputStream inZip = null;
            try {
                inZip = new ZipInputStream(new FileInputStream(mPicZipFile));
                ZipEntry zipEntry;
                while ((zipEntry = inZip.getNextEntry()) != null) {
                    if (!zipEntry.isDirectory() && picFileName.equals(zipEntry.getName())) {
                        return BitmapFactory.decodeStream(inZip);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inZip != null) {
                    try {
                        inZip.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onCancel();
        }
        return super.onKeyUp(keyCode, event);
    }

}
