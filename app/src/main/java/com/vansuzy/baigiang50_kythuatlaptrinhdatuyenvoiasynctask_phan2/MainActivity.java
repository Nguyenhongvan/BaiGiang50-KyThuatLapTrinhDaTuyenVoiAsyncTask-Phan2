package com.vansuzy.baigiang50_kythuatlaptrinhdatuyenvoiasynctask_phan2;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button btnTaiHinh;
    ImageView imgHinh;
    ProgressDialog dialog;

    String link = "https://s-media-cache-ak0.pinimg.com/564x/b4/28/6d/b4286dacf3edf82108a6d1253f4fc3e8.jpg";
    String link2 = "http://www.dramafever.com/st/img/wp/2013/06/201306270935771129_51cb8a68497ea.jpg";
    String link3 = "https://madne22.files.wordpress.com/2012/10/tumblr_mbcx1yud351r10bg2o2_r2_500.jpg";
    String link4 = "https://i0.wp.com/www.hellokpop.com/wp-content/uploads/2016/04/20160422_Suzy_3.jpg?fit=520%2C650";

    ArrayList<String> dsHinh;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnTaiHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyTaiHinh();
            }
        });
    }

    private void xuLyTaiHinh() {
        int n = random.nextInt(4);  // 0 -> 3
        ImageTask task = new ImageTask();
        task.execute(dsHinh.get(n));
    }

    // đối số 1 là đường dẫn hình, đối số 2 là quá trình xử lý (tương tác Internet thì không biết khi nào tiến trình kết thúc), đối số 3 là kết quả trả về (là 1 hình)
    class ImageTask extends AsyncTask<String,Void,Bitmap> {
        // kết nối Internet bắt buộc phải sử dụng đa tiến trình và bắt buộc phải có try... catch
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String link = params[0];
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(link).getContent());
                return bitmap;
            } catch (Exception ex) {
                Log.e("LOI", ex.toString());
            }
            return null;
        }

        // bắt đầu tải
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();  // hiển thị cửa sổ thông báo lên
        }

        // kết quả sau khi tải xong sẽ được 1 hình
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgHinh.setImageBitmap(bitmap);
            dialog.dismiss();   // dismiss() và cancel() giống nhau ở chỗ là cùng ẩn dialog, nhưng khác nhau ở chỗ: dismiss() là có thể tái sử dụng lại vòng đời của nó (tức là ẩn dialog đi nhưng mà vẫn dùng được), còn cancel() thì thường chúng ta tắt đi thì phải khởi tạo lại.
        }

        // vì đối số thứ hai của ImageTask là Void (chúng ta không biết khi nào tiến trình kết thúc) nên chúng ta không quan tâm đến onProgressUpdate()
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    private void addControls() {
        btnTaiHinh = (Button) findViewById(R.id.btnTaiHinh);
        imgHinh = (ImageView) findViewById(R.id.imgHinh);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Thông báo");
        dialog.setMessage("Đang tải hình vui lòng chờ...");
        dialog.setCanceledOnTouchOutside(false);    // mục đích của hàm này đó là nếu như chúng ta nhấn ra ngoài thì chương trình sẽ không tắt màn hình, nếu như chúng ta không có hàm này thi khi nhấn ra ngoài thì chương trình sẽ tắt màn hình trong khi tiến trình vẫn còn đang được thực thi.

        dsHinh = new ArrayList<>();
        dsHinh.add(link);
        dsHinh.add(link2);
        dsHinh.add(link3);
        dsHinh.add(link4);
    }
}
