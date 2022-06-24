package com.example.timertask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Timer timer;//可用來在指定時間執行任務，也就是排程(schedule)，並可設重複執行
   private task1 task1;
   task2 task2;
   TimerTask task3;
   uihandler uihandler=new uihandler();
   TextView textView[]=new TextView[3];
   int intres[]={R.id.textView,R.id.textView2,R.id.textView3};
  int i1,i2,i3;//控制計時
    int record1,record2,record3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i=0;i<textView.length;i++){
       textView[i]=findViewById(intres[i]);
        }
    }

    @Override
    protected void onStart() {//生命週期開始狀態
        timer=new Timer();
        super.onStart();//當程式暫停後再度執行時可以在創建一個timer
    }

    @Override
    protected void onPause() {
        if(timer!=null){//如果timer裡有資料則把timer裡的資料全部清掉
            timer.cancel();//丟棄所有當前已安排的任務。
            timer.purge();//計時器的任務隊列中移除所有已取消的任務。
            timer=null;
        }
        super.onPause();
    }
    //java最小時間單位為毫秒(1/1000)
///////////////////////////////////////////此方法在安卓6以下無法使用  因為TIMERTASK是獨立於主執行序的,因此(在安卓六以下)無法從TIMERTASK直接改變主執行續的VIEW
    public void timer_btn1(View view) {
        if(record1==1){return;}//如果點擊兩次以上則不予回應
        record1++;
     task1=new task1();
     timer.schedule(task1,1*1000,2*1000);//一秒後執行 並且每兩秒執行一次
    }
    public void timer_btn1_clear(View view) {
        if(task1!=null){
            i1= record1=0;
        task1.cancel();//取消此計時器任務。
        textView[0].setText("");
        }
    }
    class task1 extends TimerTask{//Timer如字面意思就是一個計時器，可設定要執行的時間和重複執行的間隔。被執行的任務則為TimerTask要執行的任務繼承TimerTask類別並實作run()方法
        @Override
        public void run() {
            i1++;
     textView[0].setText(i1+"");//把i轉乘String
        }
    }
//////////////////////////////

    public void hamdle_btn1(View view) {
        if(record2==1){return;}
        record2++;
        task2=new task2();
         timer.schedule(task2,2*1000,1*1000);//延遲兩秒後執行 之後每一秒執行一次
    }
    class task2 extends TimerTask{//因為TimerTask獨立於主執行序因此無法直接改變主執行序的VIEW,需要使用Handler來連接 主執行序
        @Override
        public void run() {
            i2++;
            Message message=new Message();//因為handler的傳入型態是msg
            Bundle bundle=new Bundle();//Bundle是一个可以儲存任何混合類型的列表 可傳送複數資料
            bundle.putInt("i2",i2);
       message.setData(bundle);
       uihandler.sendMessage(message);
        }
    }
    class uihandler extends Handler{
        @Override/// 通過複寫handlerMessage() 從而確定更新UI的操作
        public void handleMessage(@NonNull Message msg) {//// 執行UI操作
          int data=msg.getData().getInt("i2");
          textView[1].setText(data+"");
        }
    }
    public void hamdle_btn1_clear(View view) {
        if(task2!=null){
            i2=record2=0;
            task2.cancel();//取消此計時器任務。
            textView[1].setText("");
        }
    }
//////////////////////
public void hamdle_btn2(View view) {//handler的第二種寫法
        if(record3==1){return;} record3++;//如果點擊兩次以上則不予回應
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==0){
                textView[2].setText(i3+"");}
            }
        };
        timer.schedule(task3=new TimerTask() {
            @Override
            public void run() {
                   i3++;
                   handler.sendEmptyMessage(0);//msg.obj：用來存放資料,資料可以是任何類型；msg.what：只能放數字(可以在if用做判斷)
                 }
            },2*1000,5*1000);
}

public void hamdle_btn2_clear(View view) {
        if(task3!=null){
         record3=i3=0;
         task3.cancel();//取消此計時器任務。
          textView[2].setText("");
        }
}


}

