package cn.lx.dz.module;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.lx.dz.R;
import cn.lx.dz.support.utils.ScreenUtils;
import cn.lx.dz.support.view.SystemBarTintManager;

@SuppressLint("ResourceAsColor")
@TargetApi(Build.VERSION_CODES.KITKAT)
public abstract class BaseActivity extends AppCompatActivity {
	public FrameLayout main;
	private View content;
	public Toolbar bar;
	private SystemBarTintManager manager;
	private int statusBarHeight = 0;
	private TextView centerText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		main = (FrameLayout) findViewById(R.id.main);
		centerText = (TextView) findViewById(R.id.centerText);
		bar = (Toolbar) findViewById(R.id.toolbar);
		if (setContentView() != 0) {
			content = getLayoutInflater()
					.inflate(setContentView(), null, false);
		}
		View contentView = getContentView();
		if (contentView != null) {
			content = contentView;
		}
		if (content != null) {
			main.addView(content, 0, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// statusBarHeight = ScreenUtils.getStatusBarHeight(this);
		// Window window = getWindow();
		// window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// manager = new SystemBarTintManager(this);
		// manager.setStatusBarTintEnabled(true);
		// manager.setStatusBarTintResource(R.color.blue);
		// }
		setLayoutMargin(statusBarHeight,
				statusBarHeight + ScreenUtils.dip2px(this, 48));
		bar.setTitle("");
		bar.setTitleTextAppearance(this, R.style.ToolBarTitle);
		bar.setBackgroundResource(R.color.blue);
		resetToolBar();
		setToolBar(bar);
		setSupportActionBar(bar);
		registerButtonListener();
		init();
	}

	public void registerButtonListener() {
		bar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onNavigationClick();
			}
		});
		centerText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCenterTextClick();
			}
		});
	}

	public void addView(View child, int index) {
		main.addView(child, index);
	}

	public void setStatusBarColor(int color) {
		if (manager != null) {
			// manager.setStatusBarTintColor(color);
		}
	}

	public void setStatusBarAlpha(float alpha) {
		if (manager != null) {
			// manager.setStatusBarAlpha(alpha);
		}
	}

	public void setBackgroundAlpha(float alpha) {
		Drawable drawable = bar.getBackground();
		drawable.setAlpha((int) alpha);
	}

	public void setLayoutMargin(int barMargin, int contentMargin) {
		MarginLayoutParams params = (MarginLayoutParams) bar.getLayoutParams();
		params.topMargin = barMargin;
		bar.setLayoutParams(params);
		if (content != null) {
			MarginLayoutParams cparams = (MarginLayoutParams) content
					.getLayoutParams();
			cparams.topMargin = contentMargin;
			content.setLayoutParams(cparams);
		}
	}

	public void setFullscreen() {
		setLayoutMargin(0, 0);
		setStatusBarColor(android.R.color.transparent);
		bar.setVisibility(View.GONE);
	}

	public void setCenterTextView(int resId) {
		setCenterTextView(getResources().getString(resId));
	}

	public void setCenterTextView(String text) {
		centerText.setVisibility(View.VISIBLE);
		centerText.setText(text);
	}

	public void setCenterTextRightDrawble(int resId) {
		Drawable drawable = getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		centerText.setCompoundDrawables(null, null, drawable, null);
	}

	public void addCenterView(View view) {
		bar.removeAllViews();
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		// ((FrameLayout)view).setGravity(Gravity.CENTER_VERTICAL);
		// LayoutParams parmas = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// view.setLayoutParams(parmas);
		bar.addView(view);
	}

	public void resetToolBar() {
		setStatusBarColor(R.color.blue);
		bar.setBackgroundResource(R.color.blue);
		setBackgroundAlpha(255);
		setStatusBarAlpha(1f);
		setLayoutMargin(statusBarHeight,
				statusBarHeight + ScreenUtils.dip2px(this, 48));
	}

	public TextView getCenterText() {
		return centerText;
	}

	public int getStatusBarHeight() {
		return statusBarHeight;
	}

	public Toolbar getToolBar() {
		return bar;
	}

	// public void setRightTextButtonStyle(int res) {
	// setRightTextButtonStyle(getResources().getString(res));
	// }

	// public void setRightTextButtonStyle(String text) {
	// int tb = ScreenUtils.dip2px(this, 5);
	// int lr = ScreenUtils.dip2px(this, 16);
	// rightText.setTextColor(getResources().getColor(R.color.blue));
	// rightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
	// rightText.setBackgroundResource(R.drawable.button_white);
	// rightText.setPadding(lr, tb, lr, tb);
	// rightText.setText(text);
	// rightText.setVisibility(View.VISIBLE);
	// }

	public void onNavigationClick() {
	}

	public void onRightButtonClick() {
	}

	public void onCenterTextClick() {
	};

	public abstract void setToolBar(Toolbar bar);

	public abstract int setContentView();

	public abstract void init();

	public View getContentView() {
		return null;
	}
	// @Override
	// protected void attachBaseContext(Context newBase) {
	// super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	// }

}
