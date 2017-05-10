package com.baijiahulian.live.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.AliCloudImageUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.path;

/**
 * Created by Shubo on 2017/3/23.
 */

public class ChatPictureViewFragment extends BaseDialogFragment {

    private ImageView imageView;
    private TextView tvLoading;
//    private Button btnSave;

    public static ChatPictureViewFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        ChatPictureViewFragment fragment = new ChatPictureViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_big_picture;
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        super.hideBackground().contentBackgroundColor(ContextCompat.getColor(getContext(), R.color.live_transparent));
        String url = arguments.getString("url");
        imageView = (ImageView) contentView.findViewById(R.id.lp_dialog_big_picture_img);
        tvLoading = (TextView) contentView.findViewById(R.id.lp_dialog_big_picture_loading_label);
//        btnSave = (Button) view.findViewById(R.id.lp_dialog_big_picture_save);
        Picasso.with(getContext())
                .load(AliCloudImageUtil.getScreenScaledUrl(getContext(), url))
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        tvLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        tvLoading.setText(getString(R.string.live_image_loading_fail));
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(btnSave.getVisibility() == View.VISIBLE){
//                    btnSave.setVisibility(View.GONE);
//                }else{
                dismissAllowingStateLoss();
//                }
            }
        });
//        imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                btnSave.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveImageToGallery(getContext(), ((BitmapDrawable)imageView.getDrawable()).getBitmap());
//                Toast.makeText(getContext(), "图片成功保存到本地", Toast.LENGTH_SHORT).show();
//                btnSave.setVisibility(View.GONE);
//            }
//        });
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    protected void setWindowParams(WindowManager.LayoutParams windowParams) {
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.dimAmount = 0.85f;
//        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.windowAnimations = R.style.ViewBigPicAnim;
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "bjhl_image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
}
