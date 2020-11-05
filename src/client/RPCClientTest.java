package client;

import center.SleepService;
import proxy.ClientProxy;
import center.HelloService;

import java.net.InetSocketAddress;

/**
 * @author CTX
 * 这才是真正的客户端，啥都没有
 * 但是能够拿到暴露出来的接口因此知道可以用哪些方法，然后也可以用拿到的代理对象去代理执行这个方法
 * 虽然不知道Impl怎么实现的，但是就是跟自己家一样假装自己写完了Impl直接调用就完事了
 */
public class RPCClientTest {
    public static void main(String[] args) throws ClassNotFoundException {
        //可以拿到暴露出的任何方法HelloService、SleepService等等
        HelloService helloService = ClientProxy.getRemoteProxyObj(Class.forName("center.HelloService"), new InetSocketAddress("127.0.0.1", 9999));
        System.out.println(helloService.sayHello("唐宋丶"));
        SleepService sleepService = ClientProxy.getRemoteProxyObj(Class.forName("center.SleepService"), new InetSocketAddress("127.0.0.1", 9999));
        System.out.println(sleepService.syaSleeping());
    }
}
