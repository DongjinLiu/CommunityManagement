package layout;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jin.communitymanagement.CropOption;
import com.example.jin.communitymanagement.CropOptionAdapter;
import com.example.jin.communitymanagement.R;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MineFragment extends Fragment {

    private NiftyDialogBuilder dialogBuilder;
    Uri imgUri;
    ImageView profile_icon;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());
        profile_icon = (ImageView) view.findViewById(R.id.profile_icon);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.profile_icon:
                        dialogBuilder //构造选择头像的对话框
                                .withTitle("切换头像")
                                .withTitleColor("#FFcc00")
                                .withDividerColor("#11000000")
                                .withMessage("请选择照片路径")
                                .withMessageColor("#ffcc00")
                                .withDialogColor("#333333")
                                .withEffect(Effectstype.RotateLeft)
                                .withDuration(700)
                                .withButton1Text("拍照")
                                .withButton2Text("相册")
                                .isCancelableOnTouchOutside(true)
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PickImageFromCamera();
                                        dialogBuilder.cancel();
                                    }
                                })
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PickImageFromGallery();
                                        dialogBuilder.cancel();
                                    }
                                }).show();
                        break;

                }
            }
        };
        profile_icon.setOnClickListener(listener);
    }

    private void PickImageFromCamera() {    //使用相机拍摄获取头像
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "avatar_"
                + String.valueOf(System.currentTimeMillis())
                + ".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void PickImageFromGallery() {    //从相册中选择头像

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  //从图库中选择
        startActivityForResult(intent, PICK_FROM_FILE);

    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                doCrop();               //不管从哪里获取的头像都要进行裁剪
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                    setCropImg(data);   //设置头像
                }
                break;
        }
    }

    private void doCrop() {

        final ArrayList<CropOption> cropOptions = new ArrayList<>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "can't find crop app", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            intent.setData(imgUri);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            // 手机上只安装了一个裁剪功能的App时调用
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                //手机上安装了多个裁剪功能的App时调用
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getActivity().getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getActivity().getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("choose a app");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (imgUri != null) {
                            getContext().getContentResolver().delete(imgUri, null, null);
                            imgUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    /**
     * set the bitmap
     *
     * @param picdata
     */
    private void setCropImg(Intent picdata) {  //设置头像
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            profile_icon.setImageBitmap(toRoundCorner(mBitmap,100));  //将头像转换成圆角
            saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"
                    + System.currentTimeMillis() + ".png", mBitmap);
        }
    }

    /**
     * save the crop bitmap
     *
     * @param fileName
     * @param mBitmap
     */
    public void saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
                Toast.makeText(getActivity(), "save success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        final float roundPx = pixels;

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
