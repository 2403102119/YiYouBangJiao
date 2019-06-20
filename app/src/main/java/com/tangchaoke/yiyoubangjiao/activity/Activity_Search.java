package com.tangchaoke.yiyoubangjiao.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.utils.RecordSQLiteOpenHelper;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
* @author hg
* create at 2019/1/2
* description: 搜索
*/
public class Activity_Search extends BaseActivity {

    /**
     * 搜索  历史
     */
    private RecordSQLiteOpenHelper mRecordSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    @BindView(R.id.edit_search)
    EditText mEditSearch;

    @BindView(R.id.tv_history)
    TextView mTvHistory;

    String tempName;

    @BindView(R.id.recycler_history)
    RecyclerView mRecyclerHistory;

    SearchHistoryAdapter mSearchHistoryAdapter;

    @OnClick({R.id.tv_top_right, R.id.img_search_delete})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_top_right:
                Intent mIntent = new Intent();
                mIntent.putExtra("mSearchText", "");
                setResult(201, mIntent);
                finish();
                break;

            case R.id.img_search_delete:
                deleteData();
                queryData("");
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    public void initTitleBar() {

    }

    @Override
    protected void initData() {
        mRecordSQLiteOpenHelper = new RecordSQLiteOpenHelper(Activity_Search.this);
        // 第一次进入查询所有的历史记录
        queryData("");
        // 搜索框的键盘搜索键点击回调
        mEditSearch.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) Activity_Search.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            Activity_Search.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(mEditSearch.getText().toString().trim());
                    if (!hasData) {
                        insertData(mEditSearch.getText().toString().trim());
                        queryData("");
                    }
                    if (!HGTool.isEmpty(tempName)) {
                        Intent mIntent = new Intent();
                        mIntent.putExtra("mSearchText", tempName);
                        setResult(201, mIntent);
                        finish();
                    } else {
                        IToast.show(Activity_Search.this, "请输入搜索内容");
                    }
                }
                return false;
            }
        });

        // 搜索框的文本变化实时监听
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    mTvHistory.setText("历史搜索");
                } else {
                    mTvHistory.setText("搜索结果");
                }
                tempName = mEditSearch.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);
            }
        });
    }

    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        mSQLiteDatabase = mRecordSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.execSQL("insert into records(name) values('" + tempName + "')");
        mSQLiteDatabase.close();
    }

    Cursor mCursor;

    List<String> mListString = new ArrayList<String>();

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
        mCursor = mRecordSQLiteOpenHelper.getReadableDatabase()
                .rawQuery("select id as _id,name from records where name like '%" + tempName + "%' order by id desc ",
                        null);
        mListString.clear();
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            mListString.add(mCursor.getString(mCursor.getColumnIndex("name")));
        }
        mRecyclerHistory.setHasFixedSize(true);
        mRecyclerHistory.setNestedScrollingEnabled(false);
        mRecyclerHistory.setLayoutManager(new GridLayoutManager(Activity_Search.this, 3));
        mRecyclerHistory.setHasFixedSize(true);
        mRecyclerHistory.setItemAnimator(new DefaultItemAnimator());
        mSearchHistoryAdapter = new SearchHistoryAdapter(Activity_Search.this, mListString,
                new SearchHistoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent mIntent = new Intent();
                        mIntent.putExtra("mSearchText", mListString.get(position));
                        setResult(201, mIntent);
                        finish();
                    }
                });
        mRecyclerHistory.setAdapter(mSearchHistoryAdapter);
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        mCursor = mRecordSQLiteOpenHelper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return mCursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        mSQLiteDatabase = mRecordSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.execSQL("delete from records");
        mSQLiteDatabase.close();
    }

    public static class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHold> {

        private LayoutInflater mLayoutInflater;
        private Context mContext;
        private OnItemClickListener mOnItemClickListener;
        List<String> mListString;

        public void setOnItemCilckLisener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        public SearchHistoryAdapter(Context mContext, List<String> mListString, OnItemClickListener mOnItemClickListener) {
            this.mContext = mContext;
            this.mListString = mListString;
            this.mOnItemClickListener = mOnItemClickListener;
            this.mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHold(mLayoutInflater.inflate(R.layout.item_search_history, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHold holder, int position) {
            //遍历Cursor对象，取出数据并打印
            holder.mTvSearchHistory.setText(mListString.get(position));
        }

        @Override
        public int getItemCount() {
            return mListString.size();
        }

        public class MyViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

            LinearLayout mLlOnclick;

            TextView mTvSearchHistory;

            public MyViewHold(View itemView) {
                super(itemView);
                mLlOnclick = itemView.findViewById(R.id.ll_onclick);
                mLlOnclick.setOnClickListener(this);
                mTvSearchHistory = itemView.findViewById(R.id.tv_search_history);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    case R.id.ll_onclick:
                        mOnItemClickListener.onItemClick(getLayoutPosition(), view);
                        break;

                }
            }
        }

        public interface OnItemClickListener {
            public void onItemClick(int position, View view);
        }

    }

}
