## RPC编写流程说明



### 整体架构

* `center`：服务（注册）中心。还包含了暴露出来的接口。
* `client`：客户端，可以获取到暴露出来的接口以及客户端代理对象。
* `proxy`：客户端的代理对象，将客户端传来的信息打包成网络消息发给服务端。
* `server`：服务端，实现了所提供接口的方法，并将它们注册到服务（注册）中心。



### 编写流程

先在`center`中定义一个`Server.java`服务（注册）中心的interface接口，并且`ServerCenter.java`实现它，里面包含了：Map(Service名称字符串,对应的Impl类)存储注册、端口号port、一个线程池去接受客户端（代理）请求并处理后返回给客户端（代理）。

然后在`proxy`中实现客户端代理`ClientProxy.java`，主要包含了一个方法去生成代理，接收客户端传来的Service字符串并解析成服务（注册）中心能识别的格式，打包成网络消息发给它。

接着编写了需要暴露的接口`HelloService.java`以及它的实现类`HelloServiceImpl.java`。再通过`RPCServerTest.java`将它们注册到服务（注册）中心。并且将需要暴露的接口`HelloService.java`暴露到`center`中。

最后编写客户端`RPCClientTest.java`去调用接口。

后续要添加接口只需要服务端编写Service和对应的Impl，再将它们写入服务（注册）中心并把接口暴露出去，客户端就可以通过拿到暴露出来的新Service去调用了。

