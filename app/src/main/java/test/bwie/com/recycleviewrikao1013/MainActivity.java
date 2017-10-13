package test.bwie.com.recycleviewrikao1013;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 添加
     */
    private Button mButton;
    /**
     * 删除
     */
    private Button mButton2;
    /**
     * list
     */
    private Button mButton3;
    /**
     * grid
     */
    private Button mButton4;
    /**
     * flow
     */
    private static final int DOWNLOAD_IMG = 1;
    private Button mButton5;
    private RecyclerView mRv;
   private List<Mydata.DataBean> dataBeen = new ArrayList<>();
    int page = 1;
    private Mydata my;

    private  String url = "http://api.expoon.com/AppNews/getNewsList/type/1/p/";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Mydata m  = (Mydata) msg.obj;
            dataBeen.addAll(m.getData());
            if(msg.what == DOWNLOAD_IMG) {
                myadater = new Myadater(MainActivity.this, dataBeen);
                mRv.setAdapter(myadater);

                myadater.setOnItemClickLitener(new Myadater.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    };
    private Myadater myadater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       if(isNetworkConnected(this)){
           initView();
           InitDstas(page);


       }else{
           Toast.makeText(this, "没交网费", Toast.LENGTH_SHORT).show();
       }
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private void InitDstas(int p0) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(5, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(5,TimeUnit.SECONDS);
        client.newBuilder().readTimeout(5,TimeUnit.SECONDS);


        Request request = new Request.Builder().url(url+p0).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 if(response.isSuccessful()){
                      my =  new Gson().fromJson(response.body().string(),Mydata.class);
                //     dataBeen.addAll(my.getData());
//                     Myadater myadater = new Myadater(MainActivity.this,dataBeen);
//                     mRv.setAdapter(myadater);

                     Message message= Message.obtain();
                     message.obj = my;
                     message.what = DOWNLOAD_IMG;;
                     handler.sendMessage(message);

                 }else{
                     Toast.makeText(MainActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                 }
            }

        });

    }

    private void initView() {
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton2.setOnClickListener(this);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton3.setOnClickListener(this);
        mButton4 = (Button) findViewById(R.id.button4);
        mButton4.setOnClickListener(this);
        mButton5 = (Button) findViewById(R.id.button5);
        mButton5.setOnClickListener(this);
        mRv = (RecyclerView) findViewById(R.id.rv);

        mRv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                page +=1;
                InitDstas(page);
                dataBeen.addAll(my.getData());

                myadater.Add(dataBeen,page,1);

                break;
            case R.id.button2:
                myadater.Remove(1);
                break;
            case R.id.button3:
                mRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                break;
            case R.id.button4:
                mRv.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                break;
            case R.id.button5:
                mRv.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
                break;
        }
    }
}
