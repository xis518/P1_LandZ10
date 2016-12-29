package com.bwf.p1_landz.ui.onlinevilla;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwf.framework.base.BaseActivity;
import com.bwf.framework.http.HttpHelper;
import com.bwf.framework.http.HttpRequestAsyncTask;
import com.bwf.framework.image.ImageLoader;
import com.bwf.framework.utils.StringUtils;
import com.bwf.framework.utils.ToastUtil;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.GuWenResultBean;
import com.bwf.p1_landz.entity.HouseDetailBean;
import com.bwf.p1_landz.entity.HouseDetailResultBean;
import com.bwf.p1_landz.ui.onlinevilla.fragment.Fragment_detail_fragment01;
import com.bwf.p1_landz.ui.onlinevilla.fragment.Fragment_detail_fragment02;
import com.bwf.p1_landz.ui.onlinevilla.fragment.Fragment_detail_fragment03;
import com.bwf.p1_landz.ui.onlinevilla.fragment.Fragment_detail_fragment04;
import com.bwf.p1_landz.ui.onlinevilla.fragment.Fragment_detail_fragment05;
import com.bwf.p1_landz.ui.onlinevilla.fragment.Fragment_detail_fragment06;
import com.bwf.p1_landz.view.WeiXinDialog;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 一手房源详情
 */
public class HouseOneDetailActivity extends BaseActivity {
    @Bind(R.id.tv_title_detail)
    TextView tvTitleDetail;
    @Bind(R.id.tv_decription_detail)
    TextView tvDecriptionDetail;
    @Bind(R.id.share_img)
    ImageView shareImg;
    @Bind(R.id.shouchang_img)
    ImageView shouchangImg;
    @Bind(R.id.im_detail_photo)
    ImageView imDetailPhoto;
    @Bind(R.id.tv_huxing_name)
    TextView tvHuxingName;
    @Bind(R.id.tv_huxing_phone)
    TextView tvHuxingPhone;
    @Bind(R.id.im_detail_phone)
    ImageView imDetailPhone;
    @Bind(R.id.im_detail_sms)
    ImageView imDetailSms;

    //房源ID  楼盘ID
    private String houseOneId, resblockId;

    //图片下载器
    private ImageLoader imageLoader;
    //一手房源的logo图片和房源描述
    private Fragment_detail_fragment01 fragment01;

    //样板间
    private Fragment_detail_fragment02 fragment02;

    //本房顾问
    private Fragment_detail_fragment03 fragment03;

    //房源基本信息
    private Fragment_detail_fragment04 fragment04;

    //其他户型推荐
    private Fragment_detail_fragment05 fragment05;

    //楼盘概览  项目特点  交通配套
    private Fragment_detail_fragment06 fragment06;
    private GuWenResultBean.ShowArr show;// 最底下联系人对象

    private  HouseDetailResultBean resultBean;//详情信息对象


    // 分享
    private WeiXinDialog dialog;


    private String resourceId;
    // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
    String appID = "wx8c7eb83e476954ee";
    String appSecret = "3c1bec8d99b4e8ab4495866c3b4446b3";

    private IWXAPI api;

    @Override
    public int getContentViewId() {
        return R.layout.activity_house_one_detail;
    }

    @Override
    public void beforInitView() {
        houseOneId = getIntent().getStringExtra("houseOneId");
        resblockId = getIntent().getStringExtra("resblockId");
        resourceId = resblockId;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        fragment01 = (Fragment_detail_fragment01) getSupportFragmentManager().findFragmentById(R.id.detail_fragment01);
        fragment02 = (Fragment_detail_fragment02) getSupportFragmentManager().findFragmentById(R.id.detail_fragment02);
        fragment03 = (Fragment_detail_fragment03) getSupportFragmentManager().findFragmentById(R.id.detail_fragment03);
        fragment04 = (Fragment_detail_fragment04) getSupportFragmentManager().findFragmentById(R.id.detail_fragment04);
        fragment05 = (Fragment_detail_fragment05) getSupportFragmentManager().findFragmentById(R.id.detail_fragment05);
        fragment06 = (Fragment_detail_fragment06) getSupportFragmentManager().findFragmentById(R.id.detail_fragment06);
        //注册微信
        api = WXAPIFactory.createWXAPI(this,appID,true);
        api.registerApp(appID);
    }

    @Override
    public void initData() {
        imageLoader = new ImageLoader();
        getNetWorkData();
        getGuWenData();
    }

    @Override
    public void afterInitView() {

    }

    private void getNetWorkData() {
        if (TextUtils.isEmpty(houseOneId)) {
            finish();
            return;
        }
        showProgressBarDialog();
        HttpHelper.getDetail(this, houseOneId, new HttpRequestAsyncTask.CallBack() {
            @Override
            public void OnSuccess(String result) {
                dismissProgressBarDialog();
                resultBean = new Gson().fromJson(result, HouseDetailResultBean.class);

                if (resultBean.result != null) {
                    //设置标题内容
                    setTitle_Content(resultBean);
                    //设置Fragment01里面的数居  放图片和房源描述
                    fragment01.setResultBean(resultBean, imageLoader);
                    //设置样板间的内容
                    fragment02.setImgUrlArr(resultBean.result.imgUrlArr, true, imageLoader);
                    //设置房源基本信息
                    fragment04.setResult(resultBean.result);
                    //其他户型推荐
                    fragment05.setApartmentImgVos(resultBean.result.apartmentImgVos,imageLoader,findViewByIdNoCast(R.id.title_Bar_detail));
                    //楼盘概览  项目特点  交通配套
                    fragment06.setResult(resultBean.result,imageLoader);


                }
            }

            @Override
            public void OnFailed(String errMsg) {
                dismissProgressBarDialog();
                ToastUtil.showToast(errMsg);
            }
        });
    }


    @OnClick({R.id.share_img, R.id.shouchang_img, R.id.im_detail_phone,R.id.im_detail_sms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_img:
                showPopwindows();
                break;
            case R.id.shouchang_img:
                break;
            case R.id.im_detail_phone:
                if (show != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                            + show.phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "没有获取到联系人信息！！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.im_detail_sms:
                if (show != null) {
                    Uri smsToUri = Uri.parse("smsto:" + show.phone);
                    Intent mIntent = new Intent(
                            android.content.Intent.ACTION_SENDTO, smsToUri);
                   if(resultBean != null){
                       mIntent.putExtra("sms_body", setSmsContent(resultBean.result));
                   }else{
                       mIntent.putExtra("sms_body", "请问需要咨询什么内容");
                   }
                    startActivity(mIntent);
                } else {
                    Toast.makeText(this, "没有获取到联系人信息！！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_duanxin:
                break;
            case R.id.rl_pengyouquan:

                break;
            case R.id.rl_weixin:
                // 初始化一个WXTextObject对象
                WXTextObject textObject = new WXTextObject();
                textObject.text = setSmsContent(resultBean.result);
            // 用WXTextObject对象初始化一个WXMediaMessage对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObject;
                msg.description = setSmsContent(resultBean.result);

                // 构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;//分享到朋友
                // 调用api接口发送数据到微信
                api.sendReq(req);
//                finish();
                dialog.dismiss();
                break;
        }
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    /**
     * 设置标题内容
     *
     * @param resultBean
     */
    private void setTitle_Content(HouseDetailResultBean resultBean) {
        tvTitleDetail.setText(resultBean.result.resblockOneName);
        StringBuffer sb = new StringBuffer();
        sb.append(resultBean.result.totalprBegin)
                .append("-")
                .append(resultBean.result.totalprEnd)
                .append("万  ")
                .append((int) (resultBean.result.buildSize) + "平米  ")
                .append((int) resultBean.result.bedroomAmount + "室")
                .append((int) resultBean.result.parlorAmount + "厅")
                .append((int) resultBean.result.toiletAmount + "卫");
        tvDecriptionDetail.setText(sb.toString());
    }

    /**
     * 本方顾问列表
     */
    public void getGuWenData() {
        HttpHelper.getOneDetailLook(this, houseOneId, new HttpRequestAsyncTask.CallBack() {
            @Override
            public void OnSuccess(String result) {
                GuWenResultBean resultBean = new Gson().fromJson(result, GuWenResultBean.class);
                if (resultBean != null) {
                    if (resultBean.result.showArr != null && !resultBean.result.showArr.isEmpty()){
                        fragment03.setResult(resultBean.result, houseOneId, imageLoader);
                        //设置最底下的顾问信息
                        show = resultBean.result.showArr.get(0);
                        tvHuxingName.setText(show.createName);
                        tvHuxingPhone.setText(show.phone);
                        imageLoader.displayImg(show.photo,imDetailPhoto);
                    }
                }
            }

            @Override
            public void OnFailed(String errMsg) {
                ToastUtil.showToast(errMsg);
            }
        });
    }


    private String setSmsContent(HouseDetailBean model) {
        StringBuffer smsContent = new StringBuffer();
        // 我想咨询房源：
        // 房源名称：
        // 售价：
        // 面积：
        // 房型：
        // 请尽快与我联系！【丽兹豪宅网】
        smsContent.append("我想咨询房源:");
        smsContent.append("\n房源名称:").append(model.resblockOneName);
        smsContent.append("\n售价:")
                .append(StringUtils.doubleFormat(model.totalprBegin)).append("-")
                .append(StringUtils.doubleFormat(model.totalprEnd)).append("万");
        smsContent.append("\n面积:").append(StringUtils.doubleFormat(model.buildSize))
                .append("㎡");
        smsContent.append("\n房型:").append(model.resblockType);
        smsContent.append("\n请尽快与我联系！【丽兹豪宅网】");
        return smsContent.toString();
    }


    public void showPopwindows() {
        dialog = new WeiXinDialog(HouseOneDetailActivity.this,
                R.style.weixinDialog, HouseOneDetailActivity.this);
        dialog.show();
    }




}
