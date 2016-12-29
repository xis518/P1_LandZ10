package com.bwf.p1_landz.ui.onlinevilla.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.framework.base.BaseFragment;
import com.bwf.framework.image.ImageLoader;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.ImgUrlArrBean;
import com.bwf.p1_landz.ui.onlinevilla.LookPhotoActivity;
import com.bwf.p1_landz.ui.onlinevilla.adapter.ImagePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * ViewPager 二手房源的titleView，一手房源的样板间
 */
public class Fragment_detail_fragment02 extends BaseFragment {
    private static final int MSG_DELAY = 3000;//循环时间
    private int currentItem = -1;//当前ViewPager的位置
    private boolean canAutoScroll ;//自动与手动冲突解决
    private boolean canAuto = true;//是否可以自动滚动
    @Bind(R.id.yangbanjian)
    TextView yangbanjian;
    @Bind(R.id.detail_viewPager)
    ViewPager detailViewPager;

    private List<ImgUrlArrBean> imgUrlArr;
    private ImageLoader imageLoader;
    public void setImgUrlArr(List<ImgUrlArrBean> imgUrlArr,boolean isYangbanjian,ImageLoader imageLoader){

        this.imageLoader = imageLoader;
        if(isYangbanjian){
            yangbanjian.setVisibility(View.VISIBLE);
            this.imgUrlArr = new ArrayList<>();
            for(ImgUrlArrBean imgUrlArrBean : imgUrlArr){
                if(imgUrlArrBean.picType.equals("5")) {//判断是否是样板间图片
                    this.imgUrlArr.add(imgUrlArrBean);
                }
            }
        }else{
            this.imgUrlArr = imgUrlArr;
        }
        ImagePagerAdapter adapter = new ImagePagerAdapter(this.getContext(),this.imgUrlArr,imageLoader);
        detailViewPager.setAdapter(adapter);
        if (canAuto){
            //启动VIewPager自动滑动
            handler.sendEmptyMessageDelayed(1,MSG_DELAY);
        }

    }

    public void setImgUrlArr(List<ImgUrlArrBean> imgUrlArr,boolean isYangbanjian,boolean canAuto,ImageLoader imageLoader){
        this.canAuto = canAuto;
        setImgUrlArr(imgUrlArr,isYangbanjian,imageLoader);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_detail_fragment_2;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://切换到自动
                    currentItem++;
                    detailViewPager.setCurrentItem(currentItem);
                    handler.sendEmptyMessageDelayed(1,MSG_DELAY);
                    break;
                case 2://自动播放停止
                    handler.removeMessages(1);
                    break;
            }
        }
    };


    @Override
    public void beforInitView() {

    }

    @Override
    public void initView() {
        detailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //滑动中执行
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //选中执行
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if(onPageSelectListener != null){
                    onPageSelectListener.onPageSelected(position);
                }
            }

            //滑动状态改变执行
            @Override
            public void onPageScrollStateChanged(int state) {

                if (!canAuto)//不能自动播放了
                    return;

                if(state == ViewPager.SCROLL_STATE_IDLE){//ViewPager滑动停止  自动
                    if(canAutoScroll){
                        canAutoScroll = false;
                        handler.sendEmptyMessageDelayed(1,MSG_DELAY);
                    }
                }

                //dragging
                if(state == ViewPager.SCROLL_STATE_DRAGGING){//ViewPager拉拽  手动
                    handler.sendEmptyMessage(2);
                    canAutoScroll = true;
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(handler != null){
            if (canAuto) {
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

    @Override
    public void initData() {
        //如果是查看大图页面  给select赋值
        if (getActivity() != null && getActivity() instanceof LookPhotoActivity) {
            LookPhotoActivity activity = (LookPhotoActivity) getActivity();
        }
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void onClick(View view) {

    }




    /************************以下是注解的绑定与解除******************************/
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

    /**
     * ViewPager滚动到指定位置
     * @param currentItem
     */
    public void setCurrentItem(int currentItem){
        detailViewPager.setCurrentItem(currentItem);
    }

    private  OnPageSelectListener onPageSelectListener;
    public  void setOnPageSelectListener( OnPageSelectListener onPageSelectListener){
        this.onPageSelectListener = onPageSelectListener;
    }
    //写一个接口 用来回调
    public interface OnPageSelectListener{
        void onPageSelected(int position);
    }
}
