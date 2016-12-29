package com.bwf.p1_landz.ui.onlinevilla;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bwf.framework.base.BaseActivity;
import com.bwf.framework.utils.LogUtils;
import com.bwf.p1_landz.MyApplication;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.OnLineTypeResultBean;
import com.bwf.p1_landz.entity.OnlineTypeBean;
import com.bwf.p1_landz.entity.ParamListBean;
import com.bwf.p1_landz.request.OnLineHouseRequest;
import com.bwf.p1_landz.ui.onlinevilla.adapter.LocationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 更多 筛选页面
 */
public class SelectMoreActivity1 extends BaseActivity {

    private ListView lv_location, lv_location2;

    private OnlineTypeBean onlineTypeBean;

    private OnLineTypeResultBean resultBean;//整个更多的数据

    private List<ParamListBean> mianji_paramList;//面积数据

    private LocationAdapter adapter,adapter2;

    //请求对象
    private OnLineHouseRequest onLineHouseRequest;

    //设置状态的面积选择内容
    private String mianji[] = {"0.0","100.0","140.0","180.0","220.0","280.0","360.0","420.0","500.0"};

    @Override
    public int getContentViewId() {
        return R.layout.activity_select_more;
    }

    @Override
    public void beforInitView() {
        /*********start**********初始化数据******************************/
        //获取面积列表
        onlineTypeBean = MyApplication.getApplication().getOnlineTypeBean();
        if (onlineTypeBean != null) {
            for (OnLineTypeResultBean bean : onlineTypeBean.result) {
                if ("1004".equals(bean.paramType)) {//1004就是面积
                    mianji_paramList = bean.paramList;
//                    mianji_paramList.get(0).isSelect = true;
                }
            }
        }

        resultBean = new OnLineTypeResultBean();
        List<ParamListBean> paramList = new ArrayList<>();
        paramList.add(new ParamListBean("排序", true, getPaiXu()));
        paramList.add(new ParamListBean("面积", false, mianji_paramList));
        paramList.add(new ParamListBean("特色", false, getTeSe()));
        paramList.add(new ParamListBean("一手/二手", false, getYiShou()));
        resultBean.paramList = paramList;
        /*********end**********初始化数据******************************/

        /*********start**********设置回之前一次选择的状态******************************/
        //获得传递过来的请求对象
        onLineHouseRequest = (OnLineHouseRequest) this.getIntent().getSerializableExtra("OnLineHouseRequest");
        //设置之前选中效果
        if(onLineHouseRequest != null){
            //排序对象
            if(onLineHouseRequest.sort !=null) {
                ParamListBean paramListBean = resultBean.paramList.get(0);
                if (onLineHouseRequest.sort.equals("-1")) {
                    paramListBean.childList.get(0).isSelect = true;
                } else {
                    paramListBean.childList.get(Integer.valueOf(onLineHouseRequest.sort)).isSelect = true;
                }
            }else{
                resultBean.paramList.get(0).childList.get(0).isSelect = true;
            }
            LogUtils.e("msg",resultBean.paramList.get(0).childList.toString());
            //面积
            ParamListBean paramListBean1 = resultBean.paramList.get(1);
            if(onLineHouseRequest.buildSizeBegin != null){
                for(int j = 0;j < mianji.length;j++){
                    if(onLineHouseRequest.buildSizeBegin.equals(mianji[j])){
                        paramListBean1.childList.get(j+1).isSelect = true;
                    }
                }
            }else {
                paramListBean1.childList.get(0).isSelect = true;
            }

            //特色
            ParamListBean paramListBean2 = resultBean.paramList.get(2);
            if(onLineHouseRequest.feature != null){
                if(onLineHouseRequest.feature.equals("0")){
                    paramListBean2.childList.get(0).isSelect = true;
                }
                if(onLineHouseRequest.feature.equals("1")){
                    paramListBean2.childList.get(1).isSelect = true;
                }
                if(onLineHouseRequest.feature.equals("2")){
                    paramListBean2.childList.get(2).isSelect = true;
                }
                if(onLineHouseRequest.feature.equals("1,2")){
                    paramListBean2.childList.get(1).isSelect = true;
                    paramListBean2.childList.get(2).isSelect = true;
                }
            }else{
                paramListBean2.childList.get(0).isSelect = true;
            }

            //一手 二手
            ParamListBean paramListBean3 = resultBean.paramList.get(3);
            if(onLineHouseRequest.onlyLook != null){
                paramListBean3.childList.get(Integer.valueOf(onLineHouseRequest.onlyLook)).isSelect = true;
            }else{
                paramListBean3.childList.get(0).isSelect = true;
            }
            /*********end**********设置回之前一次选择的状态******************************/
        }
    }
    /**
     * 0默认排序 1面积从大到小（降序） 2面积从小到大（升序） 3总价从低到高（升序） 4总价从高到低（降序） 5关注度从高到低（降序）
     */
    public List<ParamListBean> getPaiXu() {
        List<ParamListBean> paramListBeen = new ArrayList<>();
        paramListBeen.add(new ParamListBean("默认排序", "-1"));
        paramListBeen.add(new ParamListBean("面积从大到小", "1"));
        paramListBeen.add(new ParamListBean("面积从小到大", "2"));
        paramListBeen.add(new ParamListBean("总价从低到高", "3"));
        paramListBeen.add(new ParamListBean("总价从高到低", "4"));
        paramListBeen.add(new ParamListBean("关注度从高到低", "5"));
        return paramListBeen;
    }
    /** 特色类型：1今日可看房 2是否钥匙房 */
    public List<ParamListBean> getTeSe() {
        List<ParamListBean> paramListBeen = new ArrayList<>();
        paramListBeen.add(new ParamListBean("不限", "0"));
        paramListBeen.add(new ParamListBean("今日可看房", "1"));
        paramListBeen.add(new ParamListBean("钥匙房", "2"));
        return paramListBeen;
    }

    public List<ParamListBean> getYiShou() {
        List<ParamListBean> paramListBeen = new ArrayList<>();
        paramListBeen.add(new ParamListBean("不限", "0"));
        paramListBeen.add(new ParamListBean("只看一手房", "1"));
        paramListBeen.add(new ParamListBean("只看二手房", "2"));
        return paramListBeen;
    }

    @Override
    public void initView() {
        lv_location = findViewByIdNoCast(R.id.lv_location);
        lv_location2 = findViewByIdNoCast(R.id.lv_location2);
    }

    @Override
    public void initData() {
        //设置监听
        setOnClick(R.id.tv_clear,R.id.btn_ok);

        //设置一级列表适配器
        //构造方法  false第一列  false 不显示打钩图片
        adapter = new LocationAdapter(this,false,false);
        lv_location.setAdapter(adapter);

        //设置二级列表适配器
        adapter2 = new LocationAdapter(this,true,true);
        lv_location2.setAdapter(adapter2);
        //给适配器加载数据
        setParamListBean(resultBean.paramList,false);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_clear://清空
                //重新设置适配器数据
                setParamListBean(resultBean.paramList,true);
                break;
            case R.id.btn_ok://确定
                //退出当前Activity
                LogUtils.i("msg",onLineHouseRequest.toString());
                Intent result = new Intent();
                result.putExtra("OnLineHouseRequest",onLineHouseRequest);
                setResult(100,result);
                finish();
                break;
        }
    }

    //当前第一列点击的item 下标
    int currentClickfristItem = 0;
    /**
     * 给两个个适配器设置初始化数据
     * @param bean
     */
    public void setParamListBean(final List<ParamListBean> bean,boolean isCelar){
        if(bean == null || bean.isEmpty()){
            return;
        }

        //给第一列适配器加载数据
        adapter.setItems(bean);
        //设置选中的效果  默认的需要一次
        if(isCelar){//清空的时候执行
            setSelectDatas(bean,0);
        }
        adapter.notifyDataSetChanged();

        //给第二列适配器加载数据  默认选中了第一项
        adapter2.setItems(bean.get(0).childList);
        //设置选中的效果  默认的需要一次
        if(isCelar){//清空的时候执行
            setSelectDatas(bean.get(0).childList,0);
            setSelectDatas(bean.get(1).childList,0);
            setSelectDatas(bean.get(2).childList,0);
            setSelectDatas(bean.get(3).childList,0);
        }
        adapter2.notifyDataSetChanged();

        //点击第一列item监听
        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //可以加一个不重复点击一个item操作
               if(currentClickfristItem == i){
                   return;
               }
                currentClickfristItem = i;
                //设置第一列选中状态
                setSelectDatas(bean,i);
                adapter.notifyDataSetChanged();
                //获得第二列数据并加载
                adapter2.setItems(bean.get(i).childList);
                //设置第二列数据默认选中状态
//                setSelectDatas(bean.get(i).childList,0);
                adapter2.notifyDataSetChanged();
            }
        });
        lv_location2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //设置选中状态，并储存数据
                //设置第二列数据默认选中状态  排除特色

                if(bean.get(currentClickfristItem).name.equals("特色")){
                    setSelectDataMore(bean.get(currentClickfristItem).childList,i);
                }else{
                    setSelectDatas(bean.get(currentClickfristItem).childList,i);
                }
                adapter2.notifyDataSetChanged();

                //储存数据到请求对象中
                if(onLineHouseRequest != null){
                    //排序
                    //currentClickfristItem 第一列数据
                   if(currentClickfristItem == 0){
                       onLineHouseRequest.sort = bean.get(0).childList.get(i).value;
                   }

                    //面积
                    if(currentClickfristItem == 1){
                        onLineHouseRequest.buildSizeBegin = bean.get(1).childList.get(i).minValue;
                        onLineHouseRequest.buildSizeEnd = bean.get(1).childList.get(i).maxValue;
                    }

                    //特色
                    if(currentClickfristItem == 2){
                        //多项选择
                        if(bean.get(2).childList.get(0).isSelect){
                            onLineHouseRequest.feature = "0";
                        }else{
                           if(bean.get(2).childList.get(1).isSelect){
                               onLineHouseRequest.feature = "1";
                           }
                            if(bean.get(2).childList.get(2).isSelect){
                                onLineHouseRequest.feature = "2";
                            }
                            if(bean.get(2).childList.get(1).isSelect &&bean.get(2).childList.get(2).isSelect){
                                onLineHouseRequest.feature = "1,2";
                            }
                        }
                    }

                    //一手 二手
                    if(currentClickfristItem == 3){
                        onLineHouseRequest.onlyLook = bean.get(3).childList.get(i).value;
                    }
                }

            }
        });
    }

    /**
     * 设置选中的状态
     * @param bean
     * @param position
     */
    public void setSelectDatas(List<ParamListBean> bean,int position){
        for(int i = 0;i<bean.size();i++){
            if(position == i){
                bean.get(i).isSelect = true;
            }else{
                bean.get(i).isSelect = false;
            }
        }
    }

    /**
     * 特色多选
     * @param bean
     * @param position
     */
    public void setSelectDataMore(List<ParamListBean> bean,int position){
       if(position == 0){
           bean.get(0).isSelect = true;
           bean.get(1).isSelect = false;
           bean.get(2).isSelect =false;
       }else{
           if(bean.get(position).isSelect){
               bean.get(position).isSelect = false;
           }else {
               bean.get(position).isSelect = true;
           }
           bean.get(0).isSelect =false;
       }
    }
}
