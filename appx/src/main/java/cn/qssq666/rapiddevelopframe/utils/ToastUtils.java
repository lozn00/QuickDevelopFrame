package cn.qssq666.rapiddevelopframe.utils;

import android.content.Context;
import android.widget.Toast;

import cn.qssq666.rapiddevelopframe.global.SuperAppContext;


public class ToastUtils {
	public static void showToast(Context context, CharSequence text)
	{
				toast(context,text,0,0);
	}
	public static void showToast(CharSequence text)
	{
		toast(SuperAppContext.getInstance(),text,0,0);
	}

//	public static void showToastErr(Context context, CharSequence text)
//	{
//		toast(context,text,1);
//	}
//
//	/**
//	 * 正常传递 为 0
//	 * @param context
//	 * @param text
//	 * @param type
//	 */
//	public static void toast(Context context, CharSequence text, int type)
//	{		toast(context,text,type, type==1?R.layout.view_toast_err:R.layout.view_toast);
//	}


	/**
	 * 指定布局资源id
	 * @param context
	 * @param text
	 * @param type
	 * @param resouceId 布局资源id
	 */
	public static void toast(Context context, CharSequence text, int type, int resouceId)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
//		Toast toast=new Toast(context);
//		if(type==1){
//			toast.setDuration(Toast.LENGTH_LONG);
//		}
//		View view= LayoutInflater.from(context).inflate(resouceId, null);
////		TextView tvText=(TextView) view.findViewById(R.id.tv_toast_text);//R.id.tv_toast_text
//		tvText.setText(text+"");
//		toast.setView(view);
//		toast.show();
	}
}
