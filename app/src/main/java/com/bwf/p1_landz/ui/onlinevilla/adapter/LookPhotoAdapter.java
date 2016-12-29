package com.bwf.p1_landz.ui.onlinevilla.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bwf.framework.image.ImageLoader;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.ImgUrlArrBean;

import java.util.List;

/**
 *
 * 查看预览图
 */
public class LookPhotoAdapter extends RecyclerView.Adapter<LookPhotoAdapter.ViewHolder>{
    private Context context;
    private List<ImgUrlArrBean> imgUrlArrBeanList;
    private ImageLoader imageLoader;

    public LookPhotoAdapter(Context context, List<ImgUrlArrBean> imgUrlArrBeanList, ImageLoader imageLoader) {
        this.context = context;
        this.imgUrlArrBeanList = imgUrlArrBeanList;
        this.imageLoader = imageLoader;
    }

    //创建ViewHolder对象
    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = View.inflate(context, R.layout.item_look_photo,null);
        ViewHolder viewHolder = new ViewHolder(itemview);
        //给控件赋值
        viewHolder.imageView = (ImageView) itemview.findViewById(R.id.img_photo);
        return viewHolder;
    }

    //绑定数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(R.mipmap.defult_twopic);
        if(imgUrlArrBeanList.get(position).picName != null){
            imageLoader.displayImg(imgUrlArrBeanList.get(position).picName,holder.imageView);
        }
    }

    //获得数据
    @Override
    public int getItemCount() {
        return imgUrlArrBeanList == null?0:imgUrlArrBeanList.size();
    }

    //先创建一个ViewHolder
    //一个ViewHolder代表一个Item
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
