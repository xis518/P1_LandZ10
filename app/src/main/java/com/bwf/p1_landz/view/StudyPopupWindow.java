package com.bwf.p1_landz.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.bwf.framework.tools.AppManager;
import com.bwf.p1_landz.MyApplication;
import com.bwf.p1_landz.R;
import com.bwf.p1_landz.entity.OnLineTypeResultBean;
import com.bwf.p1_landz.entity.OnlineTypeBean;
import com.bwf.p1_landz.entity.ParamListBean;

import java.util.List;

public class StudyPopupWindow extends PopupWindow {
	private Context mContext;
	private ListView listView;
	private List<ParamListBean> studyTypeList;
	private StudyAdapter studyAdapter;

	public List<ParamListBean> getStudyTypeList() {
		return studyTypeList;
	}

	public StudyPopupWindow(Context context, OnItemClickListener itemsOnClick){
		this.mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.seacher_popupwindow, null);
		this.setContentView(view);
		listView = (ListView) view.findViewById(R.id.seacher_pop_list1);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 点击window外面，window消失
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
		//获得数据
		OnlineTypeBean onlineTypeBean = MyApplication.getApplication().getOnlineTypeBean();
		//进行分类
		if(onlineTypeBean != null){
			for(OnLineTypeResultBean onLineTypeResultBean : onlineTypeBean.result){
				if(onLineTypeResultBean.paramType.equals("1009")){//地区
					studyTypeList = onLineTypeResultBean.paramList;
				}
			}
		}
		studyAdapter = new StudyAdapter();
		listView.setAdapter(studyAdapter);
		listView.setOnItemClickListener(itemsOnClick);
	}
	
	//修改标签位置方法
	public void setSelectItem(int mSelected){
		studyAdapter.setmSelected(mSelected);
		studyAdapter.notifyDataSetChanged();
	}
	
	class StudyAdapter extends BaseAdapter{
		private int mSelected = 0;
		
		public int getmSelected() {
			return mSelected;
		}

		public void setmSelected(int mSelected) {
			this.mSelected = mSelected;
		}

		@Override
		public int getCount() {
			if(studyTypeList != null){
				return studyTypeList.size();
			}
			return 0;
		}

		@Override
		public ParamListBean getItem(int position) {
			return studyTypeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(mContext,R.layout.popupwindow_item,null);
			if (mSelected >= 0 && position == mSelected){
				convertView.setBackgroundResource(R.mipmap.seacher_item_ed2);
				
			}else{
				convertView.setBackgroundResource(R.drawable.pop_item_bg);
			}
			convertView.setPadding((int) mContext.getResources().getDimension(R.dimen.pop_item_padding), (int) mContext.getResources()
			        .getDimension(R.dimen.pop_item_padding), (int) mContext.getResources().getDimension(R.dimen.pop_item_padding),
			        (int) mContext.getResources().getDimension(R.dimen.pop_item_padding));
			TextView tx = (TextView) convertView.findViewById(R.id.pop_tx);
			ParamListBean paramListBean = studyTypeList.get(position);
			tx.setText(paramListBean.name);

			return convertView;
		}
		
	}
	
}
