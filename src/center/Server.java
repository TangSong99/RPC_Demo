package center;

/**
 * 服务（注册）中心
 *
 * @author CTX
 */
public interface Server {
    /**
     * 启动服务
     */
    public void start();

    /**
     * 关闭服务
     */
    public void stop();

    /**
     * 注册服务
     *
     * @param service     服务接口
     * @param serviceImpl 服务实现类
     */
    public void register(Class service, Class serviceImpl);
}
