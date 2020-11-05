package server;

import center.HelloService;

/**
 * @author CTX
 * 真正的服务提供者（工具人）的方法，实现了接口的方法
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello , " + name;
    }
}
