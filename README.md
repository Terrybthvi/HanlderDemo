# HanlderDemo
This is a demo for our to understand Handler.
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
