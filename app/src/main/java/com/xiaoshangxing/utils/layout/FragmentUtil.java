package com.xiaoshangxing.utils.layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by FengChaoQun
 * on 2016/10/14
 */

public class FragmentUtil {
    private HashMap<String, Class<?>> fragmentHashMap;
    private FragmentManager fragmentManager;

    public FragmentUtil(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentUtil addFragment(String tag, Class<?> fragment) {
        fragmentHashMap.put(tag, fragment);
        return this;
    }

    public FragmentUtil removeFragment(String tag) {
        fragmentHashMap.remove(tag);
        return this;
    }

    public void showFragment(String tag) {
        if (fragmentHashMap.containsKey(tag)) {
            if (fragmentManager.findFragmentByTag(tag) != null) {

            }
        }
    }

    private void showFragmentByTag(String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        fragmentManager.beginTransaction().show(fragment);
    }


}
