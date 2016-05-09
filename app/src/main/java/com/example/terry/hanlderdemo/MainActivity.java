package com.example.terry.hanlderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;
/**
 * Demo描述:
 *
 * 示例步骤如下:
 * 1 子线程给子线程本身发送消息
 * 2 收到1的消息后,子线程给主线程发送消息
 * 3 收到2的消息后,主线程给子线程发送消息
 *
 * 为实现子线程给自己本身发送消息,关键还是在于构造Handler时传入的Looper.
 * 在此就传入该子线程自己的Looper即调用Looper.myLooper(),代码如下:
 * Looper.prepare();
 * mHandlerTest1=new HandlerTest1(Looper.myLooper());
 * Looper.loop();
 *
 * 所以当mHandlerTest1.sendMessage(message);发送消息时
 * 当然是发送到了它自己的消息队列.
 *
 * 当子线程中收到自己发送的消息后,可继续发送消息到主线程.此时只要注意构造
 * Handler时传入的Handler是主线程的Handler即可,即getMainLooper().
 * 其余没啥可说的.
 *
 *
 * 在主线程处理消息后再发消息到子线程
 *
 *
 * 其实这些线程间发送消息,没有什么;关键还是在于构造Handler时传入谁的Looper.
 *
 */
public class MainActivity extends Activity {
    private TextView mTextView;
    private HandlerTest1 mHandlerTest1;
    private HandlerTest2 mHandlerTest2;
    private int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mTextView = (TextView) findViewById(R.id.textView);

        //1 子线程发送消息给本身
        new Thread() {
            public void run() {
                Looper.prepare();
                mHandlerTest1=new HandlerTest1(Looper.myLooper());
                Message message = new Message();
                message.obj = "子线程发送的消息Hi~Hi";
                mHandlerTest1.sendMessage(message);
                Looper.loop();
            }
        }.start();

    }

    private class HandlerTest1 extends Handler {

        private HandlerTest1(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.println("子线程收到:" + msg.obj);

            //2  收到消息后可再发消息到主线程
            mHandlerTest2=new HandlerTest2(getMainLooper());
            Message message = new Message();
            message.obj = "O(∩_∩)O";
            mHandlerTest2.sendMessage(message);
        }
    }

    private class HandlerTest2 extends Handler {

        private HandlerTest2(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTextView.setText("在主线程中,收到子线程发来消息:" + msg.obj);
            //3  收到消息后再发消息到子线程
            if (counter==0) {
                Message message = new Message();
                message.obj = "主线程发送的消息Xi~Xi";
                mHandlerTest1.sendMessage(message);
                counter++;
            }

        }
    }

}
