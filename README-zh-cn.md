# Ktor-Koin-Example  &nbsp;&nbsp;&nbsp;&nbsp; [English](https://github.com/BongleXD/ktor-koin-example/tree/main)  |  简体中文 (当前页面)

这是一个示例项目，展示了如何使用 [Ktor](https://ktor.io), [Google KSP](https://github.com/google/ksp), 和 [Koin](https://insert-koin.io/docs/reference/koin-ktor/ktor/) 来用 Kotlin 语言创建一个 Web 应用。

## 什么是 Ktor？

Ktor 是一个用于构建异步服务器和客户端的框架，使用强大的 Kotlin 编程语言。Ktor 提供了一个 DSL（领域特定语言）来轻松地配置和定制你的应用。你可以使用 Ktor 来创建 RESTful API、websocket、静态内容、认证等。

## 什么是 Koin？

Koin 是一个实用的 Kotlin 依赖注入框架。Koin 使用一个简单和简洁的 DSL 或注解（KSP）来声明和注入依赖，Koin 支持 Android、Ktor 和 Kotlin 多平台项目。

## 什么是 Google KSP？

Google KSP（Kotlin Symbol Processing）是一个提供了一个 API 来构建轻量级编译器插件的库。KSP 直接分析 Kotlin 代码，比使用注解处理器快 2 倍。KSP 也对 Kotlin 的语言结构有更好的理解，比如密封类、内联类和泛型。

## 功能

- Ktor 服务器使用 Netty 引擎
- Ktor 注解控制器
- Koin 依赖注入
- KSP 注解处理器
- JWT 认证
- 使用 Jackson 进行 JSON 序列化和反序列化
- 使用 SLF4J 进行日志记录（@Slf4j KSP 注解）
- 使用 Exposed ORM 进行数据库访问

## 结构

项目有以下结构：

- `src/main/kotlin` 包含了应用的 Kotlin 源代码。
- `src/main/resources` 包含了应用的配置文件。
- `processor` 包含了 Google KSP 的注解和符号处理器。
- `build/generated/ksp` 包含了 Google KSP 生成的数据类。

## 接口

项目列举了一下接口:

- `POST /login`：用户登录。
- `POST /register`：用户注册。

## 如何运行

要运行这个项目，你需要安装 JDK 17 或更高版本。

要从命令行运行这个项目，使用以下命令：

```bash
# 克隆仓库
git clone https://github.com/BongleXD/ktor-koin-example.git

# 切换到项目文件夹
cd ktor-koin-example

# 运行应用
./gradlew run
```

要从 IntelliJ IDEA 运行这个项目，把项目导入为一个 Gradle 项目，然后运行 `Application.kt` 文件。

应用会在 8080 端口启动。你可以从浏览器访问 http://localhost:8080。

## 许可

这个项目使用 Apache-2.0 许可。详情请看 [LICENSE] 文件。
