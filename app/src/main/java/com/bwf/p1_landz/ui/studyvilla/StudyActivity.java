package com.bwf.p1_landz.ui.studyvilla;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.framework.base.BaseActivity;
import com.bwf.framework.http.HttpHelper;
import com.bwf.framework.http.HttpRequestAsyncTask;
import com.bwf.framework.image.ImageLoader;
import com.bwf.framework.utils.ToastUtil;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.StudyResultBean;
import com.bwf.p1_landz.view.StudyPopupWindow;
import com.bwf.p1_landz.view.test.RefreshListView_Bwf;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 豪宅研究
 */
public class StudyActivity extends BaseActivity {
    private ImageButton head_left_bt;
    private TextView head_tx;
    private RefreshListView_Bwf refresh_ListView;
    private View rl_no_house;
    private StudyPopupWindow popupWindow;
    private StudyResultBean studyResultBean;
    private StudyAdapter studyAdapter;
    @Override
    public int getContentViewId() {
        return R.layout.activity_study;
    }

    @Override
    public void beforInitView() {

    }

    @Override
    public void initView() {
        head_left_bt = findViewByIdNoCast(R.id.head_left_bt);
        head_tx = findViewByIdNoCast(R.id.head_tx);
        head_tx.setText("各楼盘市场研究\t▼");
        refresh_ListView = findViewByIdNoCast(R.id.seacher_list);
        rl_no_house = findViewByIdNoCast(R.id.rl_no_house);
        setOnClick(head_left_bt,head_tx);
    }

    @Override
    public void initData() {
        studyAdapter = new StudyAdapter();
        refresh_ListView.setAdapter(studyAdapter);
        refresh_ListView.setRefreh_listViewListener(new RefreshListView_Bwf.Refreh_ListViewListener() {
            @Override
            public void onRefresh() {
                getNetWorkData(null);
            }

            @Override
            public void onLoadMore() {
                getNetWorkData(null);
            }
        });
    }

    @Override
    public void afterInitView() {
        getNetWorkData(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_left_bt:
                finish();
                break;
            case R.id.head_tx:
                showPopupWindow();
                break;
        }
    }

    private void showPopupWindow() {
        if (popupWindow == null) {
            popupWindow = new StudyPopupWindow(this, itemsOnClick);
        }
        popupWindow.showAsDropDown(head_left_bt);
    }

    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            popupWindow.setSelectItem(position);
            popupWindow.dismiss();
            head_tx.setText(popupWindow.getStudyTypeList().get(position).name
                    + "\t▼");
            getNetWorkData(popupWindow.getStudyTypeList().get(position).key);
        }
    };

    private void getNetWorkData(String reportType){
        showProgressBarDialog();
        HttpHelper.getStudy(this, reportType,new HttpRequestAsyncTask.CallBack() {

            @Override
            public void OnSuccess(String result) {
                dismissProgressBarDialog();
                //解析数据
                studyResultBean = new Gson().fromJson(result,StudyResultBean.class);
                if(studyResultBean.getResult().getReportList() !=null){
                    studyAdapter.setDatas(studyResultBean.getResult().getReportList());
                    studyAdapter.notifyDataSetChanged();
                }
                //加载数据刷新适配器之后，只要发现适配器里面没有数据 就显示暂无数据页面
                if(studyAdapter.getCount() == 0){
                    rl_no_house.setVisibility(View.VISIBLE);
                }else{
                    rl_no_house.setVisibility(View.GONE);
                }
                //如果是适配器里面有数据，且在上拉加载更多的时候，发现加载不到新的数据
                if(studyResultBean == null &&studyAdapter.getCount() > 0){
                    ToastUtil.showToast("没有更多的数据了");
                }
                refresh_ListView.setOnComplete();
            }

            @Override
            public void OnFailed(String errMsg) {
                dismissProgressBarDialog();
                ToastUtil.showToast(errMsg);
                if(studyAdapter.getCount() == 0){
                    rl_no_house.setVisibility(View.VISIBLE);
                }else{
                    rl_no_house.setVisibility(View.GONE);
                }
                refresh_ListView.setOnComplete();
            }
        });
    }



    class StudyAdapter extends BaseAdapter {
        private List<StudyResultBean.ResultBean.ReportListBean> datas = new ArrayList<StudyResultBean.ResultBean.ReportListBean>();

        public void setDatas(List<StudyResultBean.ResultBean.ReportListBean> datas) {
            this.datas = datas;
        }
        public void updateDatas(List<StudyResultBean.ResultBean.ReportListBean> datas) {
            this.datas.addAll(datas);
        }
        @Override
        public int getCount() {
            if (datas != null) {
                return datas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(StudyActivity.this,R.layout.study_item, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.study_top_img);
            TextView tx = (TextView) convertView.findViewById(R.id.study_top_tx);
            TextView title_tx = (TextView) convertView.findViewById(R.id.study_title_tx);
            TextView des_tx = (TextView) convertView.findViewById(R.id.study_des_tx);
            StudyResultBean.ResultBean.ReportListBean reportListBean = datas.get(position);
            if (position == 0)
            {
                tx.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                tx.setText(reportListBean.getTitle());
                if(studyResultBean.getResult().getShowImgPath() != null){
                    new ImageLoader().displayImg(studyResultBean.getResult().getShowImgPath(), img);
                }
            }
            else
            {
                title_tx.setVisibility(View.VISIBLE);
                des_tx.setVisibility(View.VISIBLE);
                title_tx.setText(datas.get(position).getTitle());
                des_tx.setText(datas.get(position).getDescribe());
            }

            convertView.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view){
                    Intent intent = new Intent(StudyActivity.this, StudyDetailActivity.class);
                    intent.putExtra("ReportListBean", datas.get(position));
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }
}
