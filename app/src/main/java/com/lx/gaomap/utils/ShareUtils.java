package com.lx.gaomap.utils;

import android.app.Activity;
import android.util.Log;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

/**
 * Created by kxn on 2018/5/9 0009.
 */

public class ShareUtils {
    private Activity activity;
    UMShareListener mShareListener;
    ShareAction mShareAction;

    public ShareUtils(Activity activity) {
        this.activity = activity;
        mShareListener = new CustomShareListener(activity);
    }

    public void share(final String url, final String title, final String des, final String imagUrl) {
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(activity).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
        )
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(url);
                        UMImage image = new UMImage(activity, imagUrl);
                        web.setTitle(title);
                        web.setThumb(image);  //缩略图
                        web.setDescription(des);
                        //web.setThumb(new UMImage(activity, R.mipmap.logo));
                        web.setThumb(new UMImage(activity,imagUrl));
                        new ShareAction(activity)
                                .withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
        mShareAction.open();
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<Activity> mActivity;

        private CustomShareListener(Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.e("share", "onStart");
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.e("share", "onResult");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.e("share", "onError");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.e("share", "onCancel");
        }
    }


}
