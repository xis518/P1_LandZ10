package com.bwf.p1_landz.ui.onlinevilla.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.GalleyBean;

import java.util.List;


/**
 *
 * Description: galley
 */
public class TextViewAdapter extends BaseAdapter {

    private Context mContext;

    private List<GalleyBean> galleyBeen;

    public TextViewAdapter(Context c) {
        mContext = c;
    }

    public void setDatas(List<GalleyBean> galleyBeen) {
        this.galleyBeen = galleyBeen;
    }

    public int getCount() {
        if (galleyBeen != null) {
            return galleyBeen.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        return galleyBeen.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gallery_text_item, null);
            TextView tv_house_type = (TextView) convertView.findViewById(R.id.tv_house_type);
            tv_house_type.setTextSize(20);
            tv_house_type.setText(galleyBeen.get(position).typeName);
        }
        return convertView;
    }


}

