package cn.qssq666.rapiddevelopframe.engine;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * create by luozheng  time 2016-3-**
 * modify 2016-4-8
 * 非单选实例
 * <p/>
 * 用法  只要设置 事件到我这个类就行了 然后用getFlag就可以获取下标 暂时用tag的方式 new的时候必须new实现了我的findById
 * <p/>
 * 默认 lastChooseCheckBoxId =-1  checkIndex 也就是index=-1页就是什么都没选，如何设置默认选中一个呢？ 选中之后再设置进行。
 * ((CheckBox) ((ViewGroup) tableLayout.getChildAt(0)).getChildAt(0)).setChecked(true);
 * <p/>
 * <p/>
 * final RadioBoxCheckBoxListener checkBoxListener = new RadioBoxCheckBoxListener(new RadioBoxCheckBoxListener.GetViewInterface() {
 *
 * @Override public View getView(int id) {
 * return window.findViewById(id);
 * }
 * });
 * View tabLayout = decorView.findViewById(R.id.tableLayout);
 * checkBoxListener.initViewCheckEvent((ViewGroup) tabLayout);
 * checkBoxListener.setCheckedChangeCallBack(new RadioBoxCheckBoxListener.CallBack() {
 * @Override public void onChange() {
 * int checkIndex = checkBoxListener.getCheckIndex();
 * int money = 0;
 * switch (checkIndex) {
 * case 0:
 * money = 0;
 * break;
 * case 1:
 * money = 1;
 * break;
 * case 2:
 * money = 3;
 * break;
 * case 3:
 * money = 5;
 * break;
 * case 4:
 * money = 10;
 * break;
 * }
 * notify.onNotify(money);
 * dialog.dismiss();
 * }
 * });
 * dialog.setOnCancelListener(onCancelListener);
 * checkBoxListener.checked((ViewGroup) tabLayout, 0);
 *
 * 2016-9-9 17:58:16  增加 是否单选功能 已经优化选择后重复回调问题 。
 */
public class RadioBoxCheckBoxListener implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MyCheckBoxListener";
    private final GetViewInterface getViewInterface;
    public static final int NOT_SET = -1;

    public void setRadioCheck(boolean radioCheck) {
        this.radioCheck = radioCheck;
    }

    private boolean radioCheck;

    /**
     * 要想 弄索引 必须设置tag
     *
     * @return
     */
    public int getCheckIndex() {
        return checkIndex;
    }
//    this.platForm = MyShareClick.getPlatFrormStrByID(v.getId());

    private int checkIndex = NOT_SET;

    public String getStr() {
        return str;
    }

    private String str;

    /**
     * 要想获取最后选中的checkboxid必须给参与的checkbox都设置id
     *
     * @return
     */
    public int getLastChooseCheckBoxId() {
        return lastChooseCheckBoxId;
    }

    private int lastChooseCheckBoxId = NOT_SET;

    /**
     * 传递一个viewgroup
     *
     * @param getViewInterface
     */
    public RadioBoxCheckBoxListener(GetViewInterface getViewInterface) {
        this.getViewInterface = getViewInterface;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i(TAG, "lastChooseCheckBoxId:" + lastChooseCheckBoxId + ",CURRENT:" + isChecked + ",current:" + buttonView.getId() + ",tag:" + checkIndex);
        //没有选中还是给它选中
        if (radioCheck && !isChecked && lastChooseCheckBoxId != NOT_SET && lastChooseCheckBoxId == buttonView.getId()) {
            buttonView.setOnCheckedChangeListener(null);
            buttonView.setChecked(true);
            buttonView.setOnCheckedChangeListener(this);
            if (callBack != null) {
                callBack.onChange();
            }
            return;
        }
        CheckBox lastCheckBox = null;
        if (lastChooseCheckBoxId != NOT_SET && lastChooseCheckBoxId != buttonView.getId()) {
            lastCheckBox = (CheckBox) getViewInterface.getView(lastChooseCheckBoxId);

        }
        if (isChecked) {
            lastChooseCheckBoxId = buttonView.getId();
            Object tag = buttonView.getTag();
            if (tag != null) {
                checkIndex = Integer.parseInt((String) buttonView.getTag());
            }
            str = buttonView.getText().toString();
//                                            lastChooseCheckBoxId=isChecked?checkBox.getId():-1//不能这样写因为原来选中的会被取消最后还不是没选到
            //除非就是它自己本身就可以
        } else if (buttonView.getId() == lastChooseCheckBoxId) {
            lastChooseCheckBoxId = NOT_SET;
            checkIndex = NOT_SET;
        }
        if (lastCheckBox != null) {//我自己都搞不懂为何要这么写了晕头转向的各种bug不这么写的话那么会产生混乱
            lastCheckBox.setOnCheckedChangeListener(null);
            lastCheckBox.setChecked(false);
            lastCheckBox.setOnCheckedChangeListener(this);
        }
        if (callBack != null) {
            callBack.onChange();
        }
    }

    public interface CallBack {
        void onChange();
    }

    /**
     * 如果你还想监听到底设置了哪个请设置
     * 当发生了改变的时候返回给你. 也就是一些str,一些 checkIndex选择id都可以进行获取了。
     */
    public void setCheckedChangeCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    CallBack callBack = null;

    public interface GetViewInterface {
        /**
         * 每当选中的时候 回调这个方法  回调有几次,取消原来的也需要回调,选中现在的也需要回调,所以你必须返回一个属于添加事件的那个viewgroup
         */
        View getView(int id);
    }

    /**
     * 您可以手动设置也可以自动设置  这句话的方法是 ，不管你怎么布局的，你给我一个根目录viewgroup,然后里面只要有checkbox就会相互排斥,radiobutton有bug所以里面的布局 建议用checkbox
     *
     * @param viewGroup
     */
    public void initViewCheckEvent(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                initViewCheckEvent(((ViewGroup) childAt));
            } else if (childAt instanceof CheckBox) {
                ((CheckBox) childAt).setOnCheckedChangeListener(this);
            }
        }
    }

    /**
     * 根据下标选择默认 不过有多少层级都可以选中你想要的 失败也就是没有找到 返回false
     *
     * @param viewGroup
     * @param checkIndex 要选中的子view索引
     */
    public boolean checked(ViewGroup viewGroup, int checkIndex) {
        return checked(viewGroup, checkIndex, 0);
    }

    /**
     * 2016-9-9 17:36:14 补充 ，我去我已经看不懂我以前写的东西了，简直牛逼
     *
     * @param viewGroup  viewgroup 根 也就是从哪个根开始查找
     * @param checkIndex 0开始 index 里面勿乱多少层，您告诉我要找第几个，多少层不在同一层我度可以找出来
     * @param startValue 外部调用者必须为0,如果找的是 3如果你 传递了一个 2 那么当发现一个checkbox的时候就判断是不是此时2！=3 然后 也就是找到第2个checkbox就返回了，所以肯定是错误的index,开始值 是指找到里面的viewgroup后  其实是用来记录的，这个东西不能修改 ，你不能进行传递其他值，这里是记录已经找了 多少次checkbox了 只能放在方法里面记录
     * @return
     */
    private boolean checked(ViewGroup viewGroup, int checkIndex, int startValue) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                boolean result = checked((ViewGroup) childAt, checkIndex, startValue);
                if (result) {
                    return true;//一层一层往上面传递 true
                }
            } else if (childAt instanceof CheckBox) {
                if (startValue == checkIndex) {//如果 找到了某个viewgroup里面的第一个 如果  我已经不知道自己咋写的了。
                    Log.i(TAG, "找到checkbox," + childAt);
                    ((CheckBox) childAt).setChecked(true);
                    return true;
//                    break;//break只能地柜这一层,没法递归其它终止!
                } else {
                    startValue++;
                }
            }
        }
        return false;
    }
}

