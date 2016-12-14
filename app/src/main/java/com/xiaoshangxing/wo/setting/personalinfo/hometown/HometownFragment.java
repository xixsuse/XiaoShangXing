package com.xiaoshangxing.wo.setting.personalinfo.hometown;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.xiaoshangxing.network.InfoNetwork;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.wo.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.wo.setting.utils.city_choosing.ArrayWheelAdapter;
import com.xiaoshangxing.wo.setting.utils.city_choosing.OnWheelChangedListener;
import com.xiaoshangxing.wo.setting.utils.city_choosing.WheelView;
import com.xiaoshangxing.utils.baseClass.BaseFragment;
import com.xiaoshangxing.utils.baseClass.IBaseView;
import com.xiaoshangxing.utils.normalUtils.SPUtils;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by tianyang on 2016/7/9.
 */
public class HometownFragment extends BaseFragment implements OnWheelChangedListener, IBaseView {
    @Bind(R.id.left_image)
    ImageView leftImage;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.more)
    ImageView more;
    @Bind(R.id.title_bottom_line)
    View titleBottomLine;
    @Bind(R.id.title_lay)
    RelativeLayout titleLay;
    @Bind(R.id.hometown_text)
    TextView hometownText;
    @Bind(R.id.hometown_rightarrow)
    ImageView hometownRightarrow;
    @Bind(R.id.id_province)
    WheelView idProvince;
    @Bind(R.id.id_city)
    WheelView idCity;
    @Bind(R.id.complete)
    TextView complete;
    private View mView;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private String[] mProvinceDatas;
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    private String mCurrentProviceName;
    private String mCurrentCityName;
    private TextView textView;
    private PersonalInfoActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_personinfo_hometown, container, false);
        ButterKnife.bind(this, mView);
        title.setText("故乡");
        more.setVisibility(View.GONE);
        mActivity = (PersonalInfoActivity) getActivity();
        mViewProvince = (WheelView) mView.findViewById(R.id.id_province);
        mViewCity = (WheelView) mView.findViewById(R.id.id_city);
        textView = (TextView) mView.findViewById(R.id.hometown_text);
        mViewCity.setShadowColor(0xefefeff5, 0xdfefeff5, 0x0fefeff5);
        mViewProvince.setShadowColor(0xefefeff5, 0xdfefeff5, 0x0fefeff5);
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        setUpData();
        idCity.setDrawShadows(false);
        idCity.setWheelBackground(R.color.w0);
        idProvince.setDrawShadows(false);
        idProvince.setWheelBackground(R.color.w0);
        UserInfoCache.getInstance().getExIntoTextview(TempUser.getId(), NS.HOMETOWN, textView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    private void ChangeInfo(String hometown) {
        ProgressSubscriberOnNext<ResponseBody> next = new ProgressSubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) {
                try {
                    JSONObject jsonObject = new JSONObject(e.string());
                    if (jsonObject.getString(NS.CODE).equals("200")) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };

        ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(next, this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NS.ID, (Integer) SPUtils.get(getContext(), SPUtils.ID, SPUtils.DEFAULT_int));
        jsonObject.addProperty(NS.HOMETOWN, hometown);
        jsonObject.addProperty(NS.TIMESTAMP, NS.currentTime());

        InfoNetwork.getInstance().ModifyInfo(progressSubsciber, jsonObject, getContext());
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        }
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mProvinceDatas));
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    private void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getActivity().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                }
            }
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    cityNames[j] = cityList.get(j).getName();
                }
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void setText(String text) {
        textView.setText(text);
    }


    @OnClick(R.id.complete)
    public void onClick() {
        String hometown = mCurrentProviceName + " " + mCurrentCityName;
        textView.setText(hometown);
        ChangeInfo(hometown);
    }
}
