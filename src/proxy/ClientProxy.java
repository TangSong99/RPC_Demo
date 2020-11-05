package proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author CTX
 * 这里相当于是客户端存根（Client Stub）也就是客户端的代理对象，获取客户端给出的接口名，并将客户端传来的信息打包成网络消息发给服务端
 */
public class ClientProxy {
    /**
     * 获取代表服务端接口的动态代理对象（如：HelloService）
     * 一次获取一个动态代理对象
     * @param serviceInterface 请求的接口名
     * @param addr             请求服务的  Ip:端口
     * @param <T>              泛型接收
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRemoteProxyObj(Class serviceInterface, InetSocketAddress addr) {
        /**
         * newProxyInstance(a,b,c)
         * @param a 类加载器，选择需要到代理哪个类（如：HelloService），就写入到第一个参数
         * @param b 需要代理的对象具备哪些犯法（new String[]{"method1","method2"}）
         */

        return (T) java.lang.reflect.Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, (proxy, method, args) -> {
            //【疑惑点】应该是通过c代理获取到了method，args参数

            // 请求具体接口
            Socket socket = new Socket();
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                // 连接IP:端口
                socket.connect(addr);
                // 发送序列化对象流
                output = new ObjectOutputStream(socket.getOutputStream());
                // 写接口名、方法名
                output.writeUTF(serviceInterface.getName());
                output.writeUTF(method.getName());
                // 写方法参数类型、方法参数
                output.writeObject(method.getParameterTypes());
                output.writeObject((args));

                // 等待服务端处理

                //接受服务端处理后的返回值
                input = new ObjectInputStream(socket.getInputStream());
                return input.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
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
        });
    }
}
