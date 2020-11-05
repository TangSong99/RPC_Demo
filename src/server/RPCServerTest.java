package server;

import center.HelloService;
import center.Server;
import center.ServerCenter;
import center.SleepService;

/**
 * @author CTX
 * 将写好的方法注册到服务（注册）中心
 */
@SuppressWarnings("unchecked")
public class RPCServerTest {
    public static void main(String[] args) {
        new Thread(() -> {
            //服务中心
            Server server = new ServerCenter(9999);
            //将HelloService接口和实现类注册到服务中心
            server.register(HelloService.class, HelloServiceImpl.class);
            server.register(SleepService.class, SleepServiceImpl.class);
            server.start();
        }).start();
    }
}
