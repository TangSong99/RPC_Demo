package center;

/**
 * @author CTX
 * 这是被服务端暴露出去的HelloService接口，在服务端的Impl中实现了。
 * 客户端通过引用该接口来知道需要调用的方法
 * SOA的话是通过对接（后端）人员编写接口文档给前端   （PS：应该迁移是这样，新的项目就反过来）
 */
public interface HelloService {
    /**
     * 这是服务端提供的类下的SayHello方法，如果客户端要调用到它就得传一个唯一标识符（如：JSON的字符串）
     *
     * @param name 名字
     * @return String
     */
    String sayHello(String name);
}
