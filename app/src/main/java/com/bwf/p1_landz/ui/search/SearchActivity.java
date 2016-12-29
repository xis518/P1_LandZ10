package com.bwf.p1_landz.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bwf.framework.base.BaseActivity;
import com.bwf.framework.http.HttpHelper;
import com.bwf.framework.http.HttpRequestAsyncTask;
import com.bwf.framework.utils.IntentUtils;
import com.bwf.framework.utils.ToastUtil;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.SearchKeyWordBean;
import com.bwf.p1_landz.ui.onlinevilla.OnlineBuildActivity_Bwf;
import com.google.gson.Gson;

/**
 *
 * 搜索房源
 */
public class SearchActivity extends BaseActivity {
    private EditText et_search;
    private ListView lv_think_data;
    private SearchKeyWordAdapter adapter;
    private SearchKeyWordBean bean;
    /**
     * type=1 地图过来的 type=2,二手楼盘过来的 type=3为买卖 type=4 租赁列表过来 type=5 首页过来
     */
    private int type;
    @Override
    public int getContentViewId() {
        return R.layout.search;
    }

    @Override
    public void beforInitView() {
        type = getIntent().getIntExtra("insert_type", 0);
    }

    @Override
    public void initView() {
        et_search = findViewByIdNoCast(R.id.et_search);
        lv_think_data = findViewByIdNoCast(R.id.lv_history_data);
    }

    @Override
    public void initData() {
        setEditTextHit();
        adapter = new SearchKeyWordAdapter(this);
        lv_think_data.setAdapter(adapter);
    }

    @Override
    public void afterInitView() {
        //EditText添加字符改变监听
        et_search.addTextChangedListener(watcher);
        lv_think_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 隐藏键盘
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(SearchActivity.this
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String resblockName = "";
                String circleTypeCode = "";
                if (type == 3 || type == 5) {//判断是否跳转到在线房源
                    if(bean != null){//medel  搜索请求到的数据
                        if(bean.result.get(position).type == 0 ||bean.result.get(position).type == 1){
                            resblockName = bean.result.get(position).name;
                        }else if(bean.result.get(position).type == 2 ){
                            circleTypeCode = bean.result.get(position).id;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("resblockName", resblockName);
                        bundle.putString("circleTypeCode", circleTypeCode);
                        IntentUtils.openActivity(SearchActivity.this, OnlineBuildActivity_Bwf.class, bundle);
                        finish();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

    }



    private void setEditTextHit() {
        switch (type) {
            case 1:
            case 2:
                et_search.setHint("请输入楼盘或商圈名称...");
                break;
            case 3:
                et_search.setHint("请输入房源或商圈名称...");
                break;
            case 4:
                et_search.setHint("请输入房源名称...");
                break;
            case 5:
                et_search.setHint("请输入楼盘名称或房源特征...");
                break;
        }
    }

    int count = 0;
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (type == 1) {
                //地图
            } else if (type == 2 || type == 5) {
            count = 0;

            } else if (type == 3) {
            count = 1;
        } else if (type == 4) {
            count = 2;
        }
            String content = et_search.getText().toString().trim();
            if(!TextUtils.isEmpty(content)){
                HttpHelper.getResblockListByKeyWord(SearchActivity.this,et_search.getText().toString(),
                        count,callBack);
            }else{
                bean.result.clear();
                adapter.setData(bean.result);
                adapter.notifyDataSetChanged();
            }

        }

    };

    private HttpRequestAsyncTask.CallBack callBack = new HttpRequestAsyncTask.CallBack() {
        @Override
        public void OnSuccess(String result) {
            bean = new Gson().fromJson(result, SearchKeyWordBean.class);
            if(bean.result !=null){
                adapter.setData(bean.result);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void OnFailed(String errMsg) {
            ToastUtil.showToast(errMsg);
        }
    };
}
