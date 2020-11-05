package center;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务（注册）中心
 *
 * @author CTX
 */
public class ServerCenter implements Server {
    /**
     * Map: 服务端所有可供客户端访问的接口都注册到Map中
     * Key: 接口的名字用String"HelloService"; Value: 真正的HelloService实现
     */
    private static HashMap<String, Class> serviceRegister = new HashMap<>();
    private static int port;
    /**
     * 创建连接池，允许多个连接对象，每个连接对象可以处理一个客户请求
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static boolean isRunning = false;

    public ServerCenter(int port) {
        ServerCenter.port = port;
    }

    @Override
    public void start() {
        //创建线程对象，启动通信
        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(port));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //服务已启动
        isRunning = true;
        while (isRunning) {
            //执行具体的服务内容：接收请求、处理请求、返回请求
            //多线程执行，客户端每请求一次连接，服务端就从连接池中取出一个线程去处理
            System.out.println("Start Server ...");
            Socket socket = null;
            try {
                //等待客户端连接
                socket = server.accept();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            //启动线程去处理客户请求
            executor.execute(new ServiceTask(socket));
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    @Override
    public void register(Class service, Class serviceImpl) {
        serviceRegister.put(service.getName(), serviceImpl);
    }

    private static class ServiceTask implements Runnable {
        private Socket socket;

        public ServiceTask() {
        }

        public ServiceTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                //接收客户端的连接及请求
                input = new ObjectInputStream(socket.getInputStream());
                //必须按照发送顺序逐个接收
                String serviceName = input.readUTF();
                String methodName = input.readUTF();
                Class[] parameterTypes = (Class[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                //根据客户端请求，找到map中对应的实现接口
                Class ServiceClass = serviceRegister.get(serviceName);
                Method method = ServiceClass.getMethod(methodName, parameterTypes);

                //ServiceClass已经获得足够多的条件可以生成HelloServiceImpl对象了并且知道哪个方法，只需要传参就能执行
                //执行该方法 person.say(arg)
                Object result = method.invoke(ServiceClass.newInstance(), arguments);

                //将方法执行后的返回值还给客户端
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
