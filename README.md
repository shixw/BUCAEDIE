# BUCAEDIE

可扩展业务用例抽象及事件驱动集成引擎

Business use case abstraction and event driven integration engine

抽象业务用例，然后每个业务用例的执行都会触发相应的事件

事件监听的扩展怎么做，需要配置监听关系及关系生效的条件

定义事件驱动业务用例的监听关系，

通过决策树的方式确定当前数据的业务身份，不同的业务身份对应不同的监听关系

业务身份->{事件---触发的用例}

转换形式

事件->{业务身份---触发的用例}

1.提供各种业务用例元数据及订阅关系展示的视图

2.使用步骤

创建自定义业务事件

创建业务用例执行过程拦截器，用来将执行结果转换为事件

定义业务事件存储的实现，用来存储相关事件

定义业务事件的发送和订阅实现


注解使用
@UseCaseService
@UseCase



bucaedie-component-ucede-apt
使用 Annotation Processor Tool ,解析注解 @UseCaseService 生成默认的路由实现，通过在默认路由实现上标注 @UseCaseServiceRouterExtension，使其被spring所管理，
通过spring aop 拦截 注解，UseCaseServiceRouterExtension 动态路由到实际的实现中
通过注解processer生成接口的实现类，内部方法，通过业务身份会寻找实际的实现,https://github.com/square/javapoet 生成java source文件


    