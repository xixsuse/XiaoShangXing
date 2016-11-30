package com.xiaoshangxing.setting.personalinfo.showheadimg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.Network.netUtil.NS;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.setting.utils.headimg_set.CommonUtils;
import com.xiaoshangxing.setting.utils.headimg_set.FileUtil;
import com.xiaoshangxing.setting.utils.headimg_set.ToastUtils;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.FileUtils;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.IntentStatic;
import com.xiaoshangxing.utils.image.SaveImageTask;
import com.xiaoshangxing.yujian.IM.cache.NimUserInfoCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by tianyang on 2016/7/11.
 */
public class ShowHeadimgFragment extends BaseFragment implements View.OnClickListener, IBaseView {
    public static final String TAG = BaseFragment.TAG + "-ShowHeadimgFragment";
    public static final int ACTIVITY_ALBUM_REQUESTCODE = 2000;
    public static final int ACTIVITY_CAMERA_REQUESTCODE = 2001;
    public static final int ACTIVITY_MODIFY_PHOTO_REQUESTCODE = 2002;
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
    @Bind(R.id.setting_showhead_img)
    ImageView settingShowheadImg;
    private View mView;
    private ActionSheet mActionSheet;
    private PersonalInfoActivity mActivity;
    private IBaseView iBaseView = this;
    private Observer<List<NimUserInfo>> observer;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_personalinfo_showheadimg, container, false);
        ButterKnife.bind(this, mView);
        mActivity = (PersonalInfoActivity) getActivity();
        title.setText("头像");
        oberverUserInfo(true);
        initHead();
        return mView;
    }

    private void initHead() {
        UserInfoCache.getInstance().getHeadIntoImage(TempUser.getId(), settingShowheadImg);
    }

    private void oberverUserInfo(boolean is) {
        if (observer == null) {
            observer = new Observer<List<NimUserInfo>>() {
                @Override
                public void onEvent(List<NimUserInfo> nimUserInfos) {
                    for (NimUserInfo userInfo : nimUserInfos) {
                        if (userInfo.getAccount().equals(String.valueOf(TempUser.id))) {
                            initHead();
                        }
                    }
                }
            };
        }
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(observer, is);
    }

    private void showMenu() {
        if (mActionSheet == null) {
            mActionSheet = new ActionSheet(getActivity());
            mActionSheet.addMenuItem(getResources().getString(R.string.takephoto))
                    .addMenuItem(getResources().getString(R.string.fromalbum))
                    .addMenuItem(getResources().getString(R.string.savetophone));
        }
        mActionSheet.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mActionSheet.getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        mActionSheet.getWindow().setAttributes(lp);
        mActionSheet.setMenuListener(new ActionSheet.MenuListener() {
            @Override
            public void onItemSelected(int position, String item) {
                if (position == 0) photo();
                else if (position == 1) album();
                else if (position == 2) saveToPhone();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void saveToPhone() {
        String savePath = FileUtils.getXsxSaveIamge() + UUID.randomUUID().toString() + ".jpg";
        SaveImageTask s = new SaveImageTask(getContext(), savePath, "保存成功:" + savePath, "保存失败");
        s.execute(NimUserInfoCache.getInstance().getHeadImage(TempUser.getId()));
    }

    private void photo() {
        IntentStatic.openCamera(getActivity(), Uri.fromFile(FileUtil.getHeadPhotoFileRaw()), ACTIVITY_CAMERA_REQUESTCODE);
    }

    private void album() {
        Intent i = new Intent(Intent.ACTION_PICK, null);// 调用android的图库
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(i, ACTIVITY_ALBUM_REQUESTCODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_ALBUM_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() == null) {
                        ToastUtils.toast(getActivity(), getString(R.string.pic_not_valid));
                        return;
                    }
                    CommonUtils.cutPhoto(getActivity(), data.getData(), true,
                            mActivity.getImagCoverWidth(), mActivity.getImagCoverHeight());
                }
                break;
            case ACTIVITY_CAMERA_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    CommonUtils.cutPhoto(getActivity(), Uri.fromFile(FileUtil.getHeadPhotoFileRaw()), true,
                            mActivity.getImagCoverWidth(), mActivity.getImagCoverHeight());
                }
                break;
            case ACTIVITY_MODIFY_PHOTO_REQUESTCODE:

                if (resultCode != Activity.RESULT_OK) {
                    return;
                }

                String coverPath = FileUtil.getHeadPhotoDir() + FileUtil.HEADPHOTO_NAME_TEMP;

                ProgressSubscriberOnNext<ResponseBody> onNext = new ProgressSubscriberOnNext<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody e) throws JSONException {
                        try {
                            JSONObject jsonObject = new JSONObject(e.string());
                            if (jsonObject.getString(NS.CODE).equals("200")) {
                                showToast("头像修改成功");
                                FileUtil.deleteTempAndRaw();
                                initHead();
                                File file = new File(FileUtils.getTempImage());
                                file.delete();
                            } else {
                                showToast("头像修改失败");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                };

                ProgressSubsciber<ResponseBody> progressSubsciber = new ProgressSubsciber<>(onNext, iBaseView);

                File file = new File(coverPath);
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file/*SendImageHelper.getLittleImage(coverPath, getContext())*/);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                InfoNetwork.getInstance().setUserImage(progressSubsciber, TempUser.id, body, getContext());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        oberverUserInfo(false);
    }

    @OnClick({R.id.back, R.id.more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.more:
                showMenu();
                break;
        }
    }
}
