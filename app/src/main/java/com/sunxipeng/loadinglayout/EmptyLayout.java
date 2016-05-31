package com.sunxipeng.loadinglayout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class EmptyLayout {

	private Context mContext;
	private ViewGroup mLoadingView;
	private ViewGroup mEmptyView;
	private ViewGroup mErrorView;
	private Animation mLoadingAnimation;
	private ListView mListView;
	private int mErrorMessageViewId;
	private int mEmptyMessageViewId;
	private int mLoadingMessageViewId;
	private LayoutInflater mInflater;
	private boolean mViewsAdded;
	private int mLoadingAnimationViewId;
	private View.OnClickListener mLoadingButtonClickListener;
    private View.OnClickListener mEmptyButtonClickListener;
    private View.OnClickListener mErrorButtonClickListener;

	// ---------------------------
	// static variables 
	// ---------------------------
	/**
	 * The empty state
	 */
	public final static int TYPE_EMPTY = 1;
	/**
	 * The loading state
	 */
	public final static int TYPE_LOADING = 2;
	/**
	 * The error state
	 */
	public final static int TYPE_ERROR = 3;	

	// ---------------------------
	// default values
	// ---------------------------
	private int mEmptyType = TYPE_LOADING;
	private String mErrorMessage = "Oops! Something wrong happened";
	private String mEmptyMessage = "No items yet";
	private String mLoadingMessage = "Please wait";
	private int mLoadingViewButtonId = R.id.buttonLoading;
	private int mErrorViewButtonId = R.id.buttonError;
	private int mEmptyViewButtonId = R.id.buttonEmpty;
	private boolean mShowEmptyButton = true;
	private boolean mShowLoadingButton = true;
	private boolean mShowErrorButton = true;


    public void setErrorButtonClickListener(View.OnClickListener errorButtonClickListener) {
        this.mErrorButtonClickListener = errorButtonClickListener;
    }

    // ---------------------------
	// private methods
	// ---------------------------	

	private void changeEmptyType() {
		
		setDefaultValues();
		refreshMessages();

		// insert views in the root view
		if (!mViewsAdded) {
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			RelativeLayout rl = new RelativeLayout(mContext);
			rl.setLayoutParams(lp);
			if (mEmptyView!=null) rl.addView(mEmptyView);
			if (mLoadingView!=null) rl.addView(mLoadingView);
			if (mErrorView!=null) rl.addView(mErrorView);
			mViewsAdded = true;			

			ViewGroup parent = (ViewGroup) mListView.getParent();
			parent.addView(rl);
			mListView.setEmptyView(rl);
		}
		
		
		// change empty type
		if (mListView!=null) {
			View loadingAnimationView = null;
			if (mLoadingAnimationViewId > 0) loadingAnimationView = ((Activity) mContext).findViewById(mLoadingAnimationViewId); 
			switch (mEmptyType) {
			case TYPE_EMPTY:
				if (mEmptyView!=null) mEmptyView.setVisibility(View.VISIBLE);
				if (mErrorView!=null) mErrorView.setVisibility(View.GONE);
				if (mLoadingView!=null) {
					mLoadingView.setVisibility(View.GONE); 
					if (loadingAnimationView!=null && loadingAnimationView.getAnimation()!=null) loadingAnimationView.getAnimation().cancel();
				}
				break;
			case TYPE_ERROR:
				if (mEmptyView!=null) mEmptyView.setVisibility(View.GONE);
				if (mErrorView!=null) mErrorView.setVisibility(View.VISIBLE);
				if (mLoadingView!=null) {
					mLoadingView.setVisibility(View.GONE); 
					if (loadingAnimationView!=null && loadingAnimationView.getAnimation()!=null) loadingAnimationView.getAnimation().cancel();
				}
				break;
			case TYPE_LOADING:
				if (mEmptyView!=null) mEmptyView.setVisibility(View.GONE);
				if (mErrorView!=null) mErrorView.setVisibility(View.GONE);
				if (mLoadingView!=null) {
					mLoadingView.setVisibility(View.VISIBLE);
					if (mLoadingAnimation != null && loadingAnimationView!=null) {
						loadingAnimationView.startAnimation(mLoadingAnimation);
					}
					else if (loadingAnimationView!=null) {
						loadingAnimationView.startAnimation(getRotateAnimation());
					}
				}				
				break;
			default:
				break;
			}
		}
	}
	
	private void refreshMessages() {
		if (mEmptyMessageViewId>0 && mEmptyMessage!=null) ((TextView)mEmptyView.findViewById(mEmptyMessageViewId)).setText(mEmptyMessage);
		if (mLoadingMessageViewId>0 && mLoadingMessage!=null) ((TextView)mLoadingView.findViewById(mLoadingMessageViewId)).setText(mLoadingMessage);
		if (mErrorMessageViewId>0 && mErrorMessage!=null) ((TextView)mErrorView.findViewById(mErrorMessageViewId)).setText(mErrorMessage);
	}

	private void setDefaultValues() {
		if (mEmptyView==null) {
			mEmptyView = (ViewGroup) mInflater.inflate(R.layout.view_empty, null);
			if (!(mEmptyMessageViewId>0)) mEmptyMessageViewId = R.id.textViewMessage;
			if (mShowEmptyButton && mEmptyViewButtonId>0 && mEmptyButtonClickListener!=null) {
				View emptyViewButton = mEmptyView.findViewById(mEmptyViewButtonId);
				if (emptyViewButton != null) {
					emptyViewButton.setOnClickListener(mEmptyButtonClickListener);
					emptyViewButton.setVisibility(View.VISIBLE);
				}
			}
			else if (mEmptyViewButtonId>0) {
				View emptyViewButton = mEmptyView.findViewById(mEmptyViewButtonId);
				emptyViewButton.setVisibility(View.GONE);
			}
		}
		if (mLoadingView==null) {
			mLoadingView = (ViewGroup) mInflater.inflate(R.layout.view_loading, null);
			mLoadingAnimationViewId = R.id.imageViewLoading;
			if (!(mLoadingMessageViewId>0)) mLoadingMessageViewId = R.id.textViewMessage;
			if (mShowLoadingButton && mLoadingViewButtonId>0 && mLoadingButtonClickListener!=null) {
				View loadingViewButton = mLoadingView.findViewById(mLoadingViewButtonId);
				if (loadingViewButton != null) {
					loadingViewButton.setOnClickListener(mLoadingButtonClickListener);
					loadingViewButton.setVisibility(View.VISIBLE);
				}
			}
			else if (mLoadingViewButtonId>0) {
				View loadingViewButton = mLoadingView.findViewById(mLoadingViewButtonId);
				loadingViewButton.setVisibility(View.GONE);
			}
		}
		if (mErrorView==null) {
			mErrorView = (ViewGroup) mInflater.inflate(R.layout.view_error, null);
			if (!(mErrorMessageViewId>0)) mErrorMessageViewId = R.id.textViewMessage;
			if (mShowErrorButton && mErrorViewButtonId>0 && mErrorButtonClickListener!=null) {
				View errorViewButton = mErrorView.findViewById(mErrorViewButtonId);
				if (errorViewButton != null) {
					errorViewButton.setOnClickListener(mErrorButtonClickListener);
					errorViewButton.setVisibility(View.VISIBLE);
				}
			}
			else if (mErrorViewButtonId>0) {
				View errorViewButton = mErrorView.findViewById(mErrorViewButtonId);
				errorViewButton.setVisibility(View.GONE);
			}
		}
	}
	
	private static Animation getRotateAnimation() {
		final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		rotateAnimation.setDuration(1500);		
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setRepeatCount(Animation.INFINITE);		
		return rotateAnimation;
	}
	

	
	/**
	 * Constructor
	 * @param context the context (preferred context is any activity)
	 * @param listView the list view for which this library is being used
	 */
	public EmptyLayout(Context context, ListView listView) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mListView = listView;
	}
	
	
	/**
	 * Shows the empty layout if the list is empty
	 */
	public void showEmpty() {
		this.mEmptyType = TYPE_EMPTY;
		changeEmptyType();
	}

	/**
	 * Shows loading layout if the list is empty
	 */
	public void showLoading() {
		this.mEmptyType = TYPE_LOADING;
		changeEmptyType();
	}

	/**
	 * Shows error layout if the list is empty
	 */
	public void showError() {
		this.mEmptyType = TYPE_ERROR;
		changeEmptyType();
	}
	
	
}
