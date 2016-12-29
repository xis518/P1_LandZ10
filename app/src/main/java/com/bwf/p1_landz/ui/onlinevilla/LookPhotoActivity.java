package com.bwf.p1_landz.ui.onlinevilla;


import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bwf.framework.base.BaseActivity;
import com.bwf.framework.image.ImageLoader;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.GalleyBean;
import com.bwf.p1_landz.entity.ImgUrlArrBean;
import com.bwf.p1_landz.ui.onlinevilla.adapter.LookPhotoAdapter;
import com.bwf.p1_landz.ui.onlinevilla.adapter.TextViewAdapter;
import com.bwf.p1_landz.ui.onlinevilla.fragment.Fragment_detail_fragment02;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * 查看缩略图
 */
public class LookPhotoActivity extends BaseActivity {
    private ArrayList<ImgUrlArrBean> imgUrlArrBeen;//缩略图对象
    private ImageLoader imageLoader;

    private Fragment_detail_fragment02 detail_fragment2;
    //画廊
    private Gallery  gallery_str;

    //装画廊数据的集合
    private List<GalleyBean> galleyBeen;
    //画廊的适配器
    private TextViewAdapter textViewAdapter;

    //参数  记录前一次Gallery的位置
    int gallery_current = -1;
    int num;//ViewPager滑动的倍数\
    //Gallery与ViewPager滑动关联冲突解决
    private boolean isPageSelect = true,isItemSelect;

    private ImageView img_list;

    private LinearLayout ll_list;//表格列表

    private ScrollView scrollView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_look_photo;
    }

    @Override
    public void beforInitView() {
        imgUrlArrBeen = getIntent().getParcelableArrayListExtra("imgUrlArr");

    }

    @Override
    public void initView() {
        detail_fragment2 = (Fragment_detail_fragment02) getSupportFragmentManager().findFragmentById(R.id.detail_fragment2);
        gallery_str = findViewByIdNoCast(R.id.gallerg_str);
        img_list = findViewByIdNoCast(R.id.img_list);
        setOnClick(img_list);
        ll_list = findViewByIdNoCast(R.id.ll_list);
        scrollView = findViewByIdNoCast(R.id.scrollView);
    }
    @Override
    public void initData() {
        imageLoader = new ImageLoader();
        //给ViewPage设置数据
        detail_fragment2.setImgUrlArr(imgUrlArrBeen,false,false,imageLoader);
        //实例化Gallery的适配器
        textViewAdapter = new TextViewAdapter(this);
        gallery_str.setAdapter(textViewAdapter);
        //设置高亮效果，与ViewPager关联
        gallery_str.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if( gallery_str.getChildAt(i) != null){
                   //设置高亮
                   RelativeLayout rl = (RelativeLayout) gallery_str.getChildAt(i);
                   TextView tv1 = (TextView) rl.getChildAt(0);
                   TextPaint tp = tv1.getPaint();
                   tp.setFakeBoldText(true);//设置加粗
                   tv1.setTextSize(22);
                }

                if(gallery_current != -1 && gallery_str.getChildAt(gallery_current) != null){
                    RelativeLayout rl1 = (RelativeLayout) gallery_str.getChildAt(gallery_current);
                    TextView tv2 = (TextView) rl1.getChildAt(0);
                    TextPaint tp1 = tv2.getPaint();
                    tp1.setFakeBoldText(false);//设置不加粗
                    tv2.setTextSize(20);
                }

                gallery_current = i;

                //Gallery滑动的时候，ViewPager不能滑动
                isItemSelect = true;
                //控制ViewPager的滑动
                if(!isPageSelect){
                    int pos = galleyBeen.get(i).pos+num*imgUrlArrBeen.size();
                    detail_fragment2.setCurrentItem(pos);
                }
                //Gallery滑动完成 ViewPager就可以滑动了
                isItemSelect = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Fragment给ViewPager给回调对象赋值
        detail_fragment2.setOnPageSelectListener(onPageSelectListener);
    }

    //Fragment里面ViewPager的回调
    private Fragment_detail_fragment02.OnPageSelectListener onPageSelectListener = new Fragment_detail_fragment02.OnPageSelectListener() {
        //ViewPager滑动  这个方法会响应
        @Override
        public void onPageSelected(int position) {
            num = position / imgUrlArrBeen.size();
            isPageSelect = true;//当我滑动的时候

           if(!isItemSelect){
               //控制Gallery
               ImgUrlArrBean imgUrlArrBean = imgUrlArrBeen.get(position%imgUrlArrBeen.size());
               String tpye = imgUrlArrBean.picType;
               for(int i = 0;i< galleyBeen.size();i++){
                   if(tpye.equals(galleyBeen.get(i).picType)){
                       gallery_str.setSelection(i);
                       break;
                   }
               }
           }

            isPageSelect = false;//滑动完了
        }
    };

    @Override
    public void afterInitView() {
        //给galley添加数据
        addGallleyBeans();
    }

    @Override
    public void onClick(View view) {
        if(view == img_list){
            if(scrollView.getVisibility() == View.GONE){
                scrollView.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_fragment).setVisibility(View.GONE);
                gallery_str.setVisibility(View.GONE);
                img_list.setImageResource(R.mipmap.thum_close);
            }else{
                scrollView.setVisibility(View.GONE);
                findViewById(R.id.ll_fragment).setVisibility(View.VISIBLE);
                gallery_str.setVisibility(View.VISIBLE);
                img_list.setImageResource(R.mipmap.list_icon);
            }
        }

    }

    /**
     * 给galley添加数据
     */
    public void addGallleyBeans(){
        galleyBeen = new ArrayList<>();
        for (int i = 0;i < imgUrlArrBeen.size(); i++){
            //去重
                       boolean falg = true;
            for(GalleyBean galleyBean : galleyBeen){
                if(galleyBean.picType.equals(imgUrlArrBeen.get(i).picType)){
                    falg = false;
                }
            }

            if(falg){//没有重复
                GalleyBean galleyBean = new GalleyBean(i,imgUrlArrBeen.get(i).picType,getTypeName(imgUrlArrBeen.get(i).picType));
                galleyBeen.add(galleyBean);
                addView(i);//有几个Gallery对象 就添加表格几行
            }

        }
        //添加数据到适配器和刷新适配器
        textViewAdapter.setDatas(galleyBeen);
        textViewAdapter.notifyDataSetChanged();
    }

    public String getTypeName(String type){
        if(type.equals("1")){
            return "外景图";
        }
        if(type.equals("2")){
            return "地理位置图";
        }
        if(type.equals("3")){
            return "座栋分布图";
        }
        if(type.equals("4")){
            return "户型图";
        }
        if(type.equals("5")){
            return "样板间";
        }
        if(type.equals("6")){
            return "实勘图";
        }
        return  "";
    }


    public void addView(int pos){
        //添加TextView
        TextView textView = new TextView(this);
        textView.setPadding(10,10,10,10);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(24);
        //设置样式内容
        textView.setText(getTypeName(imgUrlArrBeen.get(pos).picType));
        ll_list.addView(textView);
        //添加ImageView

        //当前这一列的ImageView的数据
        List<ImgUrlArrBean> new_imgs = new ArrayList<>();
        for(ImgUrlArrBean imgUrlArrBean : imgUrlArrBeen){
            if(imgUrlArrBean.picType.equals(imgUrlArrBeen.get(pos).picType)){
                new_imgs.add(imgUrlArrBean);
            }
        }
        //使用RecyclerView
        //1.实例化RecyclerView
        RecyclerView recyclerView = new RecyclerView(this);
        //2.为RecyclerView设置LayoutManger
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        //3.为RecyclerView设置Adapater
        LookPhotoAdapter adapter = new LookPhotoAdapter(this,new_imgs,imageLoader);
        recyclerView.setAdapter(adapter);

        //把RecyclerView添加到LinearLayout
        ll_list.addView(recyclerView);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(scrollView.getVisibility() == View.VISIBLE){
                scrollView.setVisibility(View.GONE);
                findViewById(R.id.ll_fragment).setVisibility(View.VISIBLE);
                gallery_str.setVisibility(View.VISIBLE);
                img_list.setImageResource(R.mipmap.list_icon);
                return  true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
