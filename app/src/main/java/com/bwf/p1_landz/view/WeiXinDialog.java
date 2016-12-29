package com.bwf.p1_landz.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bwf.p1_landz.R;


public class WeiXinDialog extends Dialog implements View.OnClickListener
{
	private TextView tv_content = null;
	private String content;
	private View.OnClickListener onClick = null;

	public WeiXinDialog(Context context, int theme, View.OnClickListener onClickListener)
	{
		super(context, theme);
		onClick = onClickListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weixin);
		setCanceledOnTouchOutside(true);
		findViewById(R.id.rl_weixin).setOnClickListener(this);
		findViewById(R.id.rl_pengyouquan).setOnClickListener(this);
		findViewById(R.id.rl_duanxin).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.rl_weixin:
				onClick.onClick(v);
				break;
			case R.id.rl_pengyouquan:
				onClick.onClick(v);
				break;
			case R.id.rl_duanxin:
				onClick.onClick(v);
				break;
		}
	}
}
