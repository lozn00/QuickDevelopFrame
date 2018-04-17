package cn.qssq666.rapiddevelopframe.interfaces;

/**
 * Created by luozheng on 2016/3/25.  qssq.space
 */
public interface ILoadProgress {
     void onLoadStart(String title, String message);
     void onLoadSucc();
     void onLoadFail(String title, String error);
}
