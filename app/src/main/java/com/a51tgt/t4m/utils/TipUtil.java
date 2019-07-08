package com.a51tgt.t4m.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.text.Html;
import android.util.Log;


import com.a51tgt.t4m.R;

import org.json.JSONObject;

public class TipUtil {
	public interface OnAlertSelected {
		void onClick(DialogInterface dialog, int whichButton);
	}

	private static ProgressDialog dialog = null;

	public static void showProgress(Context context, String message,
                                    Boolean isCancelable) {
		closeProgress();
		dialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		// 设置进度条风格，风格为圆形，旋转的
		dialog.setMessage(message);
		dialog.setCancelable(isCancelable);
		dialog.setCanceledOnTouchOutside(isCancelable);
		dialog.setOnCancelListener(null);
		dialog.show();
	}

	public static void showAlert(Context context, String title, String message, String button0, final OnAlertSelected alertSelected) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT).setNegativeButton(
				button0 == null || button0.isEmpty() ? context.getResources().getText(R.string.commit_button) : button0,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (alertSelected != null) {
							alertSelected.onClick(dialog, which);
						}
					}
				}).create();
		alertDialog.setCancelable(false);
		alertDialog.setTitle(title);
		alertDialog.setMessage(Html.fromHtml(message.replace("\n", "<br />")));
		alertDialog.show();
	}
	

	public static void closeProgress() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
