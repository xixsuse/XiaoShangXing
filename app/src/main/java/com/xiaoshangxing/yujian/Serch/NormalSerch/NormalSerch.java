package com.xiaoshangxing.yujian.Serch.NormalSerch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoshangxing.Network.IMNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.utils.BaseActivity;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.LoadingDialog;
import com.xiaoshangxing.yujian.FriendActivity.LoveOrStartActivity;
import com.xiaoshangxing.yujian.IM.kit.string.StringUtil;
import com.xiaoshangxing.yujian.Serch.ToolBarOptions;
import com.xiaoshangxing.yujian.WatchMessagePicture.ReflectionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscription;

/**
 * Created by FengChaoQun
 * on 2016/10/1
 */

public class NormalSerch extends AppCompatActivity implements IBaseView {
    private ListView lvContacts;

    private SearchView searchView;

    private Handler handler = new Handler();
    protected LoadingDialog loadingDialog;
    private int current_type;
    private Normal_Serch_adpter adpter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.global_search_result);
        loadingDialog = new LoadingDialog(this);
        current_type = getIntent().getIntExtra(IntentStatic.TYPE, LoveOrStartActivity.LOVE);
        getData();

        ToolBarOptions options = new ToolBarOptions();
        setToolBar(R.id.toolbar, options);

        lvContacts = (ListView) findViewById(R.id.searchResultList);
        lvContacts.setVisibility(View.GONE);


        lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showKeyboard(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        findViewById(R.id.global_search_root).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showKeyboard(false);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    protected void onDestroy() {
        showKeyboard(false);
        super.onDestroy();
    }

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, NormalSerch.class);
        intent.putExtra(IntentStatic.TYPE, type);
        context.startActivity(intent);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.global_search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);

        handler.post(new Runnable() {
            @Override
            public void run() {
                MenuItemCompat.expandActionView(item);
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();
                return false;
            }
        });

        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setDividerDrawable(getResources().getDrawable(R.drawable.cursor_blue4));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                showKeyboard(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (StringUtil.isEmpty(query)) {
                    lvContacts.setVisibility(View.GONE);
                } else {
                    lvContacts.setVisibility(View.VISIBLE);
                    showResult();
                }
                return true;
            }
        });

        return true;
    }

    private void showResult() {
        if (users.size() < 1) {
            return;
        }
        List<User> showList = new ArrayList<>();
        for (User i : users) {
            if (i.getUsername().contains(searchView.getQuery().toString())) {
                showList.add(i);
            }
        }
        adpter = new Normal_Serch_adpter(this, 1, showList, current_type);
        lvContacts.setAdapter(adpter);
    }

    private void getData() {
        ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    switch (jsonObject.getInt(NS.CODE)) {
                        case 200:
                            Gson gson = new Gson();
                            users = gson.fromJson(jsonObject.getJSONArray(NS.MSG).toString(), new TypeToken<List<User>>() {
                            }.getType());
                            break;
                        default:
                            showToast(jsonObject.getString(NS.MSG));
                            break;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, this);
        if (current_type == LoveOrStartActivity.LOVE) {
            IMNetwork.getInstance().MyFavor(progressSubsciber, String.valueOf(TempUser.id), this);
        } else {
            IMNetwork.getInstance().MyStar(progressSubsciber, String.valueOf(TempUser.id), this);
        }
    }


    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private Toolbar toolbar;

    public void setToolBar(int toolBarId, ToolBarOptions options) {
        toolbar = (Toolbar) findViewById(toolBarId);
        if (options.titleId != 0) {
            toolbar.setTitle(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            toolbar.setTitle(options.titleString);
        }
        if (options.logoId != 0) {
            toolbar.setLogo(options.logoId);
        }
        setSupportActionBar(toolbar);

        if (options.isNeedNavigate) {
            toolbar.setNavigationIcon(options.navigateId);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    ReflectionUtil.invokeMethod(fm, "noteStateNotSaved", null);
                }
            });
        }
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Override
    public void showLoadingDialog(String text) {
        loadingDialog.setLoadText(text);
        loadingDialog.show();
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = getResources().getDimensionPixelSize(R.dimen.x360); //设置宽度
        lp.height = getResources().getDimensionPixelSize(R.dimen.y360); //设置宽度
        loadingDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void hideLoadingDialog() {
        loadingDialog.dismiss();
    }

    @Override
    public void setonDismiss(LoadingDialog.onDismiss on) {

    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
}
