package com.bwf.p1_landz.ui.onlinevilla.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.framework.base.BaseFragment;
import com.bwf.framework.image.ImageLoader;
import com.bwf.framework.utils.IntentUtils;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.HouseDetailResultBean;
import com.bwf.p1_landz.ui.onlinevilla.LookPhotoActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 一手房源  logo图片和房源描述
 * //    ButterKnife  注解
 */
public class Fragment_detail_fragment01 extends BaseFragment {
    @Bind(R.id.img_titlePic)
    ImageView imgTitlePic;
    @Bind(R.id.tv_house_desc)
    TextView tvHouseDesc;//房源描述
    @Bind(R.id.img_down)
    ImageView imgDown;

    private HouseDetailResultBean resultBean;
    private ImageLoader imageLoader;

    private int lineNum = 0;//房源描述的行数
    /**
     * 获得数据的方法
     * @param resultBean  数据对象
     * @param imageLoader 图片下载器对象
     */
    public void setResultBean(HouseDetailResultBean resultBean, ImageLoader imageLoader){
        this.resultBean = resultBean;
        this.imageLoader = imageLoader;
        initData();
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_detail_fragment_1;
    }

    @Override
    public void beforInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        if(resultBean != null){
            //给logo图片下载
            imageLoader.displayImg(resultBean.result.titlepicImg,imgTitlePic);
            //房源描述
            tvHouseDesc.setText(resultBean.result.roomDescripe);
            //设置房源描述的行数  100毫秒执行
            tvHouseDesc.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lineNum = tvHouseDesc.getLineCount();
                    if(lineNum > 5){
                        tvHouseDesc.setLines(5);
                    }else{
                        tvHouseDesc.setLines(lineNum);
                    }
                }
            },100);
        }
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    boolean isUp = false;//房源描述默认关闭
    @OnClick({R.id.img_titlePic, R.id.img_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_titlePic://点击图片跳转到查看预览图Activity
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("imgUrlArr", (ArrayList<? extends Parcelable>) resultBean.result.imgUrlArr);
                IntentUtils.openActivity(getContext(), LookPhotoActivity.class,bundle);
                break;
            case R.id.img_down://房源描述的打开与关闭
                if(isUp){//关闭
                    isUp = false;
                    imgDown.setImageResource(R.mipmap.first_down);
                    if(lineNum > 5){
                        tvHouseDesc.setLines(5);
                    }else{
                        tvHouseDesc.setLines(lineNum);
                    }
                }else{//打开
                    isUp = true;
                    tvHouseDesc.setLines(lineNum);
                    imgDown.setImageResource(R.mipmap.content_up);
                }
                break;
        }
    }
}
