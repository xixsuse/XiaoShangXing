package com.xiaoshangxing.setting.personalinfo.showheadimg;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoshangxing.Network.InfoNetwork;
import com.xiaoshangxing.Network.NS;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubsciber;
import com.xiaoshangxing.Network.ProgressSubscriber.ProgressSubscriberOnNext;
import com.xiaoshangxing.R;
import com.xiaoshangxing.data.TempUser;
import com.xiaoshangxing.data.User;
import com.xiaoshangxing.data.UserInfoCache;
import com.xiaoshangxing.setting.personalinfo.PersonalInfoActivity;
import com.xiaoshangxing.setting.utils.ActionSheet;
import com.xiaoshangxing.setting.utils.headimg_set.CommonUtils;
import com.xiaoshangxing.setting.utils.headimg_set.FileUtil;
import com.xiaoshangxing.setting.utils.headimg_set.ToastUtils;
import com.xiaoshangxing.utils.BaseFragment;
import com.xiaoshangxing.utils.IBaseView;
import com.xiaoshangxing.utils.image.MyGlide;
import com.xiaoshangxing.yujian.ChatActivity.SendImageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;
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
    private View mView;
    private ImageView img, bigImg;
    private ActionSheet mActionSheet;
    private PersonalInfoActivity mActivity;
    private TextView back;
    private IBaseView iBaseView = this;
    private Realm realm;

    @Override
    public void setmPresenter(@Nullable Object presenter) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_setting_personalinfo_showheadimg, container, false);
        mActivity = (PersonalInfoActivity) getActivity();
        img = (ImageView) mView.findViewById(R.id.showhead_threepoint);
        bigImg = (ImageView) mView.findViewById(R.id.setting_showhead_img);
        back = (TextView) mView.findViewById(R.id.showheadimg_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        img.setOnClickListener(this);
        realm = Realm.getDefaultInstance();
        initHead();
        return mView;
    }

    private void initHead() {
        String path = realm.where(User.class).equalTo(NS.ID, TempUser.id).findFirst().getUserImage();
        if (!TextUtils.isEmpty(path)) {
            MyGlide.with(this, path, bigImg);
        }
    }

    @Override
    public void onClick(View v) {
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

    }

    private void photo() {
        if (CommonUtils.isExistCamera(getActivity())) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
            Uri imageUri = Uri.fromFile(FileUtil.getHeadPhotoFileRaw());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, ACTIVITY_CAMERA_REQUESTCODE);
        } else {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.user_no_camera),
                    Toast.LENGTH_SHORT).show();
        }
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
                if (requestCode != Activity.RESULT_OK) {
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
                                UserInfoCache.getInstance().reload(new UserInfoCache.ReloadCallback() {
                                    @Override
                                    public void callback(JSONObject jsonObject) throws JSONException {
                                        MyGlide.with(getContext(), jsonObject.getString("userImage"), bigImg);
                                    }
                                }, TempUser.id);
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
                        RequestBody.create(MediaType.parse("multipart/form-data"), SendImageHelper.getLittleImage(coverPath, getContext()));
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                InfoNetwork.getInstance().setUserImage(progressSubsciber, TempUser.id, body, getContext());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = getActivity().getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        Log.d("uri2", "" + uri);
        return uri;
    }

}
