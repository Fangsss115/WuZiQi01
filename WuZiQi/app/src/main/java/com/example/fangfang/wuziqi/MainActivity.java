package com.example.fangfang.wuziqi;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private WuZiQiActivity activity;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = (WuZiQiActivity) findViewById(R.id.wzq_main);
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("游戏结束");
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.restartGame();
            }
        });
        activity.setOnGameListener(new WuZiQiActivity.onGameListener() {

            @Override
            public void onGameOver(int i) {
                String str = "";
                if (i == WuZiQiActivity.WHITE_WIN) {
                    str = "白棋胜";
                }else if(i == WuZiQiActivity.BLACK_WIN){
                    str = "黑棋胜";
                }
                builder.setMessage(str);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                Window win = dialog.getWindow();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.x = 0;
                params.y = 0;
                win.setAttributes(params);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }
}
