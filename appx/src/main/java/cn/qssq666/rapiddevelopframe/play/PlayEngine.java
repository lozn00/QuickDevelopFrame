/*
 *
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::  by qssq666@foxmail.com
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 *
 */

package cn.qssq666.rapiddevelopframe.play;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.SoftReference;

import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.utils.AppUtils;
import cn.qssq666.rapiddevelopframe.utils.Prt;
import cn.qssq666.rapiddevelopframe.utils.ToastUtils;

/**
 * Created by luozheng on 2016/7/6.  qssq.space
 * see MediaPlayerDemo_Audio.java by every
 */
public class PlayEngine {

    private static final int MSG_VIEW_PLAY_ANIM = 1;
    private static final int MSG_VIEW_STOP_ANIM = 2;
    private static final int MSG_ERROR_TEXT = 3;
    private static MediaPlayer mMediaPlayer;

    public static PlayListener getPlayListener() {
        return mPlayListener;
    }

    private static PlayListener mPlayListener;

    public static void setBindAnimView(AnimInterface mBindAnimView) {
        setAnimView(mBindAnimView);
    }

    private static void setAnimView(AnimInterface mBindAnimView) {
        PlayEngine.mBindAnimViewRef = new SoftReference<AnimInterface>(mBindAnimView);
    }

    public static AnimInterface getBindAnimView() {
        return mBindAnimViewRef == null ? null : mBindAnimViewRef.get();
    }

    public static String getPlayurl() {
        return playurl;
    }


    private static String playurl;
    private static SoftReference<AnimInterface> mBindAnimViewRef;
    private static String TAG = "PlayEngine";

    /**
     * 无需暂停 还是需要暂停? 但是界面销毁还是需要暂停的
     *
     * @param url
     */
    public static boolean play(String url) {
        return play(url, null);
    }

    public static boolean play(String url, AnimInterface bindView) {
        return play(url, bindView, null);
    }

    /**
     * @param url
     * @param bindView     绑定的动画view
     * @param playListener 停止的回调 true表示 是 正常停止的
     * @return
     */

    public static boolean play(String url, AnimInterface bindView, PlayListener playListener) {
        return play(url, bindView, PlayRecourceType.URL, playListener);
    }

    public interface PlayRecourceType {
        int URL = 1;
        int ASSETS = 2;
        int URI = 4;
        int RAW = 3;
    }

    public static boolean play(String url, AnimInterface bindView, int playType) {
        return play(url, bindView, playType, null);
    }

    /**
     * @param url
     * @param bindView
     * @param playType     @{@link PlayRecourceType}
     * @param playListener
     * @return
     */
    public static boolean play(String url, AnimInterface bindView, int playType, PlayListener playListener) {
        return play(url, bindView, playType, false, playListener);
    }

    public static boolean play(String url, AnimInterface bindView, int playType, boolean sinleLoop, PlayListener playListener) {
        return play(url,bindView,playType,true,sinleLoop,playListener);
    }
    public static boolean play(String url, AnimInterface bindView, int playType, final boolean requestForcus, boolean sinleLoop, PlayListener playListener) {
        PlayEngine.mPlayListener = playListener;
        if (getBindAnimView() != null) {
            getBindAnimView().stopAnim();
        }
        setAnimView(bindView);
        if (playurl != null && playurl.equals(url) && mMediaPlayer != null && mMediaPlayer.getDuration() > 0) {//说明已经加载完毕了呗

            boolean b = continuePlay();
            if (mPlayListener != null) {

                mPlayListener.onStart(true);
            }
            if (!b) {
                if (mPlayListener != null) {
                    mPlayListener.onPause(FLAG_HANDLE_PLAY_END, true);
                }
                pausePlay();
            } else {
            }
            return true;
        }
        destoryMedia();//管它咋的先销毁 绑定的view当然也一样

        /*
        AssetFileDescriptor fileDescriptor = assetManager.openFd("a2.mp3");
mediaPlayer = new MediaPlayer();
    mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                              fileDescriptor.getStartOffset(),
                              fileDescriptor.getLength());
    mediaPlayer.prepare();
         */
        try {

            switch (playType) {
                case PlayRecourceType.URL:
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(url);
                    break;
                case PlayRecourceType.URI://Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(SuperAppContext.getInstance(), Uri.parse(url));

                    break;

                case PlayRecourceType.ASSETS:
                    AssetFileDescriptor fileDescriptor = SuperAppContext.getInstance().getAssets().openFd(url);
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
                    break;
                case PlayRecourceType.RAW:
                    mMediaPlayer = MediaPlayer.create(SuperAppContext.getInstance(), Integer.parseInt(url));
//                    mMediaPlayer = MediaPlayer.create(SuperAppContext.getInstance(), Integer.parseInt(url));
                    break;
            }
            mMediaPlayer.setLooping(sinleLoop);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            playurl = url;
        } catch (IOException e) {

            Prt.e(TAG, "播放失败,设置数据源错误" + e.toString() + ",播放地址:" + playurl);
            toastMsg("播放地址有错");
            e.printStackTrace();
            if (mPlayListener != null) {
                mPlayListener.onPause(FLAG_HANDLE_PLAY_END, false);

            }
            return false;
        }


        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Prt.i(TAG, "缓存进度:" + percent);
                if (mPlayListener != null) {
                    mPlayListener.onBufferingUpdate(mp, percent);
                }
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                startAnim();
                if(requestForcus){
                AppUtils.requestAudioFocus();

                }
                mMediaPlayer.start();
                if (mPlayListener != null) {
                    mPlayListener.onStart(false);
                }
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAnim();
                if (mPlayListener != null) {
                    mPlayListener.onPause(FLAG_PLAY_END, true);
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mPlayListener != null) {
                    mPlayListener.onPause(FLAG_ERROR_END, false);
                }
                switch (extra) {
                    case MediaPlayer.MEDIA_ERROR_IO:
                        Prt.d(TAG, "文件流错误" + what + "," + extra);
                        toastMsg("文件流错误");
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        toastMsg("MEDIA_ERROR_MALFORMED");
                        Prt.d(TAG, "MEDIA_ERROR_MALFORMED" + what + "," + extra);
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        toastMsg("MEDIA_ERROR_UNSUPPORTED");
                        Prt.d(TAG, "MEDIA_ERROR_UNSUPPORTED" + what + "," + extra);
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        Prt.d(TAG, "MEDIA_ERROR_TIMED_OUT");
                        toastMsg("播放时间超出");
                        break;
                    default:
                        Prt.d(TAG, "未知错误 " + what + "," + extra);
                        toastMsg("未知错误 waht:" + what + ",extra:" + extra);
                        break;
                }
                stopAnim();
                return false;
            }
        });
        mMediaPlayer.prepareAsync();
        return true;
    }


    public static boolean pausePlay(boolean needCallBack, int flag) {
        if (getBindAnimView() != null) {
            getBindAnimView().stopAnim();
        }

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            AppUtils.abandonAudioFocus();
            if (needCallBack && mPlayListener != null) {
                mPlayListener.onPause(flag, true);
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean pausePlay() {
        return pausePlay(false, FLAG_HANDLE_PLAY_END);
    }


    static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_VIEW_PLAY_ANIM:

                    if (getBindAnimView() != null) {
                        getBindAnimView().startAnim();
                    }
                    break;
                case MSG_VIEW_STOP_ANIM:
                    if (getBindAnimView() != null) {
                        getBindAnimView().stopAnim();
                        clearAnimView();
                    }
                    break;
                case MSG_ERROR_TEXT:
                    ToastUtils.showToast("" + msg.obj.toString());
                    break;
            }
        }
    };

    private static void clearAnimView() {
        if (mBindAnimViewRef != null) {
            mBindAnimViewRef.clear();

        } else {
        }
    }

    public static void startAnim() {
        handler.sendEmptyMessage(MSG_VIEW_PLAY_ANIM);
    }

    public static void stopAnim() {
        handler.sendEmptyMessage(MSG_VIEW_STOP_ANIM);
    }

    public static void toastMsg(String msg) {
        handler.obtainMessage(MSG_ERROR_TEXT, msg).sendToTarget();
    }

    public static boolean continuePlay() {
        return continuePlay(false);
    }

    /**
     * @param needCallBack 是否回调
     * @return
     */
    public static boolean continuePlay(boolean needCallBack) {

        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            AppUtils.requestAudioFocus();
            mMediaPlayer.start();
            if (needCallBack && mPlayListener != null) {
                mPlayListener.onStart(true);
            }
            startAnim();
            return true;
        }
        return false;
    }

    /**
     * 不会有任何回调  如果要修改 状态 请先调用 pause方法
     *
     * @return
     */
    public static boolean destory() {
        stopAnim();
        PlayEngine.mPlayListener = null;
        return destoryMedia();

    }

    public static boolean isStop() {
        return mMediaPlayer == null || !mMediaPlayer.isPlaying();
    }

    public static boolean isDestory() {
        return mMediaPlayer == null;
    }

    public static boolean destoryMedia() {
        playurl = null;
        if (mMediaPlayer != null) {
//            mMediaPlayer.reset();
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                AppUtils.abandonAudioFocus();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
            return true;
        }
        return false;
    }

    public interface PlayListener {
        /**
         * 说明已经开始缓存了,就绪完毕了
         */
        void onStart(boolean fromCache);

        /**
         * @param flag  @see {FLAG_ERROR_END,FLAG_HANDLE_PLAY_END,FLAG_PLAY_END} 错我的停止 了 手动的停止了 ，还是一首歌曲的正常结束
         * @param pause
         */
        void onPause(int flag, boolean pause);

        void onBufferingUpdate(MediaPlayer mp, int percent);

    }

    public interface AnimInterface {
        void stopAnim();

        void startAnim();
    }

    public static abstract class SimplePlayListener implements PlayListener {
        public void setPosition(int position) {
            this.position = position;
        }

        public int position;

        public SimplePlayListener() {
        }

        public SimplePlayListener(int position) {
            this.position = position;
        }


        @Override
        public void onPause(int flag, boolean pause) {
            if (flag == FLAG_ERROR_END) {
                //非正常涨停
                onError();
            }
        }

        public abstract void onError();

        public void onBufferingUpdate(MediaPlayer mp, int percent) {

        }
    }

    public static final int FLAG_PLAY_END = 1;//一曲结束了
    public static final int FLAG_HANDLE_PLAY_END = 2;//手动的点击播放结束了 这个不会 关闭 播放控制台
    public static final int FLAG_HANDLE_FOREVER_PLAY_END = 3;//手动的点击播放结束了  永久关闭 就需要关闭控制台了
    public static final int FLAG_ERROR_END = 4;//错误的结束了
}
