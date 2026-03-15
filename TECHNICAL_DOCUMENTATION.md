# Hope外卖系统技术文档

## 目录

1. [项目概述](#项目概述)
2. [技术栈](#技术栈)
3. [系统架构](#系统架构)
4. [功能模块](#功能模块)
5. [数据库设计](#数据库设计)
6. [API接口文档](#api接口文档)
7. [安全机制](#安全机制)
8. [缓存策略](#缓存策略)
9. [部署说明](#部署说明)
10. [扩展功能](#扩展功能)

---

## 项目概述

### 项目介绍
Hope外卖系统是一个基于Spring Boot 3.5.5开发的综合性外卖平台,集成了用户管理、商家管理、订单处理、优惠券、支付、AI智能客服等核心功能。系统采用前后端分离架构,支持多角色权限管理,为用户提供便捷的外卖订购体验。

### 主要特性
- **多角色支持**: 普通用户、商家、大管理员三种角色
- **JWT双Token认证**: 支持AccessToken和RefreshToken刷新机制
- **邮箱验证**: 完整的注册、登录、找回密码流程
- **智能缓存**: Redis多级缓存策略,防止缓存穿透、击穿、雪崩
- **AI集成**: 支持OpenAI和Ollama模型,提供智能客服功能
- **文件存储**: 阿里云OSS对象存储
- **支付集成**: 支付宝沙箱环境支付
- **日志监控**: 完整的AOP日志记录
- **数据校验**: Jakarta Validation参数校验

### 版本信息
- 项目名称: takeaway
- 版本号: 0.0.1-SNAPSHOT
- Java版本: 17
- Spring Boot版本: 3.5.5

---

## 技术栈

### 后端框架
- **Spring Boot 3.5.5**: 核心框架
- **Spring Web**: Web服务支持
- **Spring Data Redis**: Redis缓存支持
- **Spring AOP**: 面向切面编程
- **Spring Validation**: 数据校验
- **Spring Mail**: 邮件发送

### 持久层
- **MyBatis Plus 3.5.14**: ORM框架
- **MyBatis Spring Boot Starter 3.0.5**: MyBatis集成
- **MySQL Connector**: MySQL数据库连接

### 安全与认证
- **JWT (jjwt 0.11.5)**: JSON Web Token认证
- **BCrypt**: 密码加密
- **Spring Boot Starter Security** (已集成BCrypt)

### AI集成
- **Spring AI 1.0.3**: AI框架集成
  - OpenAI模型支持
  - Ollama本地模型支持
  - PostgreSQL向量存储 (pgvector)

### 工具库
- **Lombok 1.18.34**: 简化Java代码
- **Hutool 5.8.22**: Java工具类库
- **AspectJ Weaver**: AOP切面支持

### 支付与存储
- **支付宝SDK 4.9.28.ALL**: 支付功能
- **阿里云OSS 3.17.4**: 对象存储

### 消息队列
- **Spring AMQP**: RabbitMQ支持

### 数据库
- **MySQL**: 关系型数据库
- **Redis**: 缓存数据库

---

## 系统架构

### 项目结构
```
takeaway/
├── src/
│   ├── main/
│   │   ├── java/com/hope/
│   │   │   ├── TakeawayApplication.java          # 启动类
│   │   │   ├── anno/                              # 自定义注解
│   │   │   │   └── MyLog.java                    # 日志注解
│   │   │   ├── aspect/                            # 切面
│   │   │   │   └── LoggingAspect.java            # 日志切面
│   │   │   ├── config/                            # 配置类
│   │   │   │   ├── AliPayConfig.java             # 支付宝配置
│   │   │   │   ├── CommonConfiguration.java     # 通用配置
│   │   │   │   ├── LoginInterceptor.java         # 登录拦截器
│   │   │   │   └── WebConfig.java                 # Web配置
│   │   │   ├── controller/                        # 控制器
│   │   │   │   ├── AddressController.java
│   │   │   │   ├── AdverBannerController.java
│   │   │   │   ├── AliPayController.java
│   │   │   │   ├── CartController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ChatController.java
│   │   │   │   ├── DishController.java
│   │   │   │   ├── MerchantController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── SmartChatController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── VoucherController.java
│   │   │   │   └── VoucherOrderController.java
│   │   │   ├── domain/                            # 实体类
│   │   │   │   ├── dto/                           # 数据传输对象
│   │   │   │   ├── pojo/                          # 持久化对象
│   │   │   │   └── vo/                            # 视图对象
│   │   │   ├── exception/                         # 异常处理
│   │   │   │   ├── BusinessException.java
│   │   │   │   └── GlobalExceptionaHandler.java
│   │   │   ├── mapper/                            # 数据访问层
│   │   │   │   ├── AddressMapper.java
│   │   │   │   ├── AdverBannerMapper.java
│   │   │   │   ├── CartMapper.java
│   │   │   │   ├── CategoryMapper.java
│   │   │   │   ├── DishMapper.java
│   │   │   │   ├── MerchantMapper.java
│   │   │   │   ├── OrderItemMapper.java
│   │   │   │   ├── OrderMapper.java
│   │   │   │   ├── SysSocialUserMapper.java
│   │   │   │   ├── UserMapper.java
│   │   │   │   ├── VoucherMapper.java
│   │   │   │   └── VoucherOrderMapper.java
│   │   │   ├── service/                           # 业务逻辑层
│   │   │   │   ├── impl/                          # 实现类
│   │   │   │   ├── AddressService.java
│   │   │   │   ├── AdverBannerService.java
│   │   │   │   ├── CartService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── DishService.java
│   │   │   │   ├── IVoucherOrderService.java
│   │   │   │   ├── MerchantService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── PayService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── VoucherService.java
│   │   │   └── util/                              # 工具类
│   │   │       ├── AliOSSProperties.java
│   │   │       ├── AliOSSUtils.java
│   │   │       ├── CacheClient.java               # 缓存客户端
│   │   │       ├── CryptoUtil.java
│   │   │       ├── JwtUtil.java                   # JWT工具
│   │   │       ├── MD5Utils.java
│   │   │       ├── OrderNumberUtil.java
│   │   │       ├── PayUtil.java
│   │   │       ├── PwdUtil.java                   # 密码加密
│   │   │       ├── RedisConstant.java
│   │   │       ├── RedisData.java
│   │   │       ├── RedisIdWorker.java             # ID生成器
│   │   │       ├── RegexPatterns.java
│   │   │       ├── RegexUtil.java                 # 正则工具
│   │   │       ├── SystemConstants.java
│   │   │       ├── ThreadLocalUtil.java
│   │   │       ├── VectorDistanceUtils.java
│   │   │       └── VerificationCode.java          # 验证码生成
│   │   └── resources/
│   │       ├── application.yml                    # 配置文件
│   │       └── logback.xml                         # 日志配置
│   └── test/                                        # 测试
└── pom.xml                                          # Maven配置
```

### 分层架构
```
┌─────────────────────────────────────┐
│         Controller Layer             │  控制器层
│  (接收请求、参数校验、返回响应)        │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│          Service Layer               │  业务逻辑层
│  (业务处理、事务管理、缓存操作)        │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│           Mapper Layer               │  数据访问层
│  (SQL映射、数据库操作)                │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│       Database Layer                │  数据存储层
│  (MySQL、Redis)                      │
└─────────────────────────────────────┘
```

---

## 功能模块

### 1. 用户模块 (UserController)

#### 1.1 用户注册
- **接口**: `POST /user/register`
- **功能**: 邮箱验证码注册
- **验证**: 邮箱格式、用户名唯一性、验证码校验
- **密码加密**: BCrypt加密

#### 1.2 用户登录
- **接口**: `POST /user/login`
- **功能**: 账号密码登录
- **返回**: 双Token (AccessToken + RefreshToken)
- **Token机制**: 
  - AccessToken: 有效期较短,用于API访问
  - RefreshToken: 有效期较长,用于刷新AccessToken

#### 1.3 邮箱验证码
- **接口**: `POST /user/send-code`
- **功能**: 发送注册/重置密码验证码
- **防刷机制**: 60秒内同一邮箱不能重复发送
- **有效期**: 5分钟

#### 1.4 忘记密码
- **接口**: `POST /user/forget`
- **功能**: 通过邮箱验证码重置密码
- **验证**: 用户名、邮箱匹配性、验证码

#### 1.5 修改密码
- **接口**: `POST /user/update-password`
- **功能**: 已登录用户修改密码
- **验证**: 旧密码正确性、新密码一致性

#### 1.6 刷新Token
- **接口**: `POST /user/refreshToken`
- **功能**: 使用RefreshToken获取新的双Token
- **机制**: 验证RefreshToken有效性,返回新的Token对

#### 1.7 用户信息
- **接口**: `GET /user/userinfo`
- **功能**: 获取当前登录用户信息
- **权限**: 需要登录

#### 1.8 编辑用户信息
- **接口**: `POST /user/edit`
- **功能**: 修改用户昵称、邮箱等信息
- **验证**: 用户名、邮箱唯一性

#### 1.9 更换头像
- **接口**: `POST /user/avatar`
- **功能**: 上传并更新用户头像
- **限制**: 文件大小不超过2MB
- **存储**: 阿里云OSS

### 2. 商家模块 (MerchantController)

#### 2.1 商家CRUD
- 创建商家信息
- 查询商家列表
- 更新商家信息
- 删除商家(逻辑删除)

#### 2.2 商家信息字段
- 名称、Logo、描述
- 配送费、起送价、评分
- 商家地址、联系电话
- 营业状态

### 3. 商品模块 (DishController/CategoryController)

#### 3.1 商品分类
- 分类CRUD操作
- 支持多级分类

#### 3.2 商品管理
- 商品SPU/SKU管理
- 商品价格、图片、描述
- 商品上下架状态

#### 3.3 商品字段
```java
- id: 商品ID
- name: 商品名称
- price: 商品价格
- image: 商品图片
- description: 商品描述
- status: 商品状态
- createTime/updateTime: 时间戳
```

### 4. 购物车模块 (CartController)

#### 4.1 购物车操作
- 添加商品到购物车
- 更新商品数量
- 删除购物车商品
- 清空购物车

#### 4.2 购物车字段
```java
- id: 购物车ID
- quantity: 商品数量
- userId: 用户ID
- skuId: SKU ID
- spuId: SPU ID
- status: 状态
- isDeleted: 是否删除
```

### 5. 订单模块 (OrderController)

#### 5.1 订单创建
- 生成唯一订单号
- 计算订单总价
- 关联地址、优惠券

#### 5.2 订单状态
- 待支付
- 已支付
- 配送中
- 已完成
- 已取消

#### 5.3 订单字段
```java
- id: 订单ID
- orderNumber: 订单号
- userId: 用户ID
- merchantId: 商家ID
- address_id: 地址ID
- totalPrice: 总价
- deliveryFee: 配送费
- discountPrice: 优惠金额
- status: 订单状态
- payTime: 支付时间
- cancelTime: 取消时间
- remark: 备注
```

### 6. 优惠券模块 (VoucherController/VoucherOrderController)

#### 6.1 优惠券类型
- 普通优惠券
- 秒杀优惠券

#### 6.2 优惠券操作
- 领取优惠券
- 查询用户优惠券
- 使用优惠券
- 过期优惠券处理

#### 6.3 优惠券字段
```java
- id: 优惠券ID
- merchantId: 商家ID
- title: 标题
- subTitle: 副标题
- rules: 使用规则
- payValue: 支付金额
- actualValue: 实际金额
- type: 类型(0:普通, 1:秒杀)
- status: 状态(1:上架, 2:下架, 3:过期)
```

### 7. 地址模块 (AddressController)

#### 7.1 地址管理
- 添加收货地址
- 查询地址列表
- 更新地址信息
- 删除地址

### 8. 广告横幅模块 (AdverBannerController)

#### 8.1 横幅管理
- 上传横幅图片
- 查询横幅列表
- 更新横幅信息

### 9. AI智能客服 (SmartChatController/ChatController)

#### 9.1 智能对话
- **接口**: `GET /v2/chat/scene`
- **功能**: 基于场景的智能对话
- **支持模型**: 
  - OpenAI (qwen-max-latest)
  - Ollama (deepseek-r1:1.5b)
- **场景类型**:
  - campus: 校园助手
  - food: 订单查询助手
  - 通用助手

#### 9.2 向量搜索
- 支持文档向量化存储
- 语义搜索
- PostgreSQL pgvector支持

### 10. 支付模块 (AliPayController)

#### 10.1 支付宝集成
- **接口**: 支付相关API
- **环境**: 沙箱环境
- **功能**:
  - 发起支付
  - 支付回调
  - 支付结果查询

#### 10.2 支付配置
```yaml
alipay:
  app-id: 9021000156657096
  gateway: https://openapi-sandbox.dl.alipaydev.com/gateway.do
  notify-url: http://pf835785.natappfree.cc/api/alipay/toSuccess
  return-url: http://localhost:8080/api/alipay/toSuccess
```

---

## 数据库设计

### 用户表 (user)
```sql
字段名              类型          说明
id                BIGINT       用户ID (主键)
username          VARCHAR      账号
nickname          VARCHAR      昵称
email             VARCHAR      邮箱
info              VARCHAR      用户信息
password          VARCHAR      密码(BCrypt加密)
avatar            VARCHAR      头像地址
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
status            INT          状态(0:健康, 1:删除, 2:异常)
role              INT          角色(0:普通用户, 1:商家, 2:管理员)
```

### 商家表 (merchant)
```sql
字段名              类型          说明
id                BIGINT       商家ID
name              VARCHAR      商家名称
avatar            VARCHAR      Logo
description       VARCHAR      商家描述
deliveryFee       DECIMAL      配送费
minOrderAmount    DECIMAL      起送价
score             DOUBLE       评分
address           VARCHAR      商家地址
email             VARCHAR      联系方式
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
status            INT          状态
userId            BIGINT       用户ID
```

### 商品表 (dish)
```sql
字段名              类型          说明
id                BIGINT       商品ID
name              VARCHAR      商品名称
price             DECIMAL      价格
image             VARCHAR      商品图片
description       VARCHAR      描述
status            INT          状态
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
creatId           BIGINT       创建人ID
updateId          BIGINT       更新人ID
```

### 商品SKU表 (dish_sku)
```sql
字段名              类型          说明
id                BIGINT       SKU ID
spuId             BIGINT       关联SPU ID
skuName           VARCHAR      SKU名称
price             DECIMAL      价格
stock             INT          库存
spec              VARCHAR      规格
```

### 商品SPU表 (dish_spu)
```sql
字段名              类型          说明
id                BIGINT       SPU ID
name              VARCHAR      SPU名称
categoryId        BIGINT       分类ID
price             DECIMAL      价格
image             VARCHAR      图片
description       VARCHAR      描述
status            INT          状态
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
```

### 购物车表 (cart)
```sql
字段名              类型          说明
id                BIGINT       购物车ID
quantity          INT          商品数量
userId            BIGINT       用户ID
skuId             BIGINT       SKU ID
spuId             BIGINT       SPU ID
status            INT          状态
isDeleted         INT          是否删除
```

### 订单表 (order_list)
```sql
字段名              类型          说明
id                BIGINT       订单ID
orderNumber       VARCHAR      订单号
userId            BIGINT       用户ID
merchantId        BIGINT       商家ID
address_id        BIGINT       地址ID
totalPrice        DECIMAL      总价
deliveryFee       DECIMAL      配送费
discountPrice     DECIMAL      优惠金额
status            INT          订单状态
isDeleted         INT          是否删除
payTime           DATETIME     支付时间
cancelTime        DATETIME     取消时间
remark            VARCHAR      备注
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
```

### 订单项表 (order_item)
```sql
字段名              类型          说明
id                BIGINT       订单项ID
orderId           BIGINT       订单ID
dishId            BIGINT       商品ID
quantity          INT          数量
price             DECIMAL      价格
```

### 优惠券表 (tb_voucher)
```sql
字段名              类型          说明
id                BIGINT       优惠券ID
merchantId        BIGINT       商家ID
title             VARCHAR      标题
subTitle          VARCHAR      副标题
rules             VARCHAR      使用规则
payValue          INT          支付金额(分)
actualValue       INT          实际金额(分)
type              INT          类型(0:普通, 1:秒杀)
status            INT          状态(1:上架, 2:下架, 3:过期)
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
```

### 优惠券订单表 (tb_voucher_order)
```sql
字段名              类型          说明
id                BIGINT       订单ID
userId            BIGINT       用户ID
voucherId         BIGINT       优惠券ID
payType           INT          支付方式
status            INT          状态
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
payTime           DATETIME     支付时间
```

### 地址表 (address)
```sql
字段名              类型          说明
id                BIGINT       地址ID
userId            BIGINT       用户ID
consignee         VARCHAR      收货人
phone             VARCHAR      电话
province          VARCHAR      省份
city              VARCHAR      城市
district          VARCHAR      区县
detail            VARCHAR      详细地址
isDefault         INT          是否默认
```

### 分类表 (category)
```sql
字段名              类型          说明
id                BIGINT       分类ID
name              VARCHAR      分类名称
parentId          BIGINT       父级分类ID
level             INT          分类层级
status            INT          状态
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
```

### 广告横幅表 (adver_banner)
```sql
字段名              类型          说明
id                BIGINT       横幅ID
imageUrl          VARCHAR      图片URL
linkUrl           VARCHAR      跳转链接
title             VARCHAR      标题
status            INT          状态
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
```

### 社交用户表 (sys_social_user)
```sql
字段名              类型          说明
id                BIGINT       ID
userId            BIGINT       用户ID
provider          VARCHAR      提供商(github/gitee)
providerId        VARCHAR      提供商ID
unionId           VARCHAR      联合ID
openId            VARCHAR      开放ID
createTime        DATETIME     创建时间
updateTime        DATETIME     更新时间
```

---

## API接口文档

### 通用响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 认证方式
- **方式**: Bearer Token (JWT)
- **Header**: `Authorization: Bearer {AccessToken}`
- **刷新**: 使用RefreshToken调用 `/user/refreshToken`

---

### 用户相关接口

#### 1. 用户注册
```http
POST /user/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "123456",
  "passwordConfirm": "123456",
  "code": "123456"
}
```

#### 2. 用户登录
```http
POST /user/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "id": "1",
    "username": "testuser",
    "nickname": "测试用户",
    "avatar": "http://...",
    "role": 0,
    "email": "test@example.com",
    "status": 0
  }
}
```

#### 3. 发送验证码
```http
POST /user/send-code
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com"
}
```

#### 4. 忘记密码
```http
POST /user/forget
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "newpassword",
  "passwordConfirm": "newpassword",
  "code": "123456"
}
```

#### 5. 修改密码
```http
POST /user/update-password
Content-Type: application/json
Authorization: Bearer {accessToken}

{
  "password": "oldpassword",
  "newPassword": "newpassword",
  "passwordConfirm": "newpassword"
}
```

#### 6. 刷新Token
```http
POST /user/refreshToken
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### 7. 获取用户信息
```http
GET /user/userinfo
Authorization: Bearer {accessToken}
```

#### 8. 更换头像
```http
POST /user/avatar
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data

avatar: [文件]
```

---

### 商家相关接口

#### 1. 获取商家列表
```http
GET /merchant/list
```

#### 2. 获取商家详情
```http
GET /merchant/{id}
```

#### 3. 创建商家
```http
POST /merchant/create
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "name": "测试商家",
  "description": "商家描述",
  "deliveryFee": 5.00,
  "minOrderAmount": 20.00,
  "address": "商家地址"
}
```

---

### 商品相关接口

#### 1. 获取商品列表
```http
GET /dish/list?categoryId=1&status=1
```

#### 2. 获取商品详情
```http
GET /dish/{id}
```

#### 3. 创建商品
```http
POST /dish/create
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "name": "测试商品",
  "price": 25.00,
  "description": "商品描述",
  "categoryId": 1
}
```

---

### 订单相关接口

#### 1. 创建订单
```http
POST /order/create
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "merchantId": 1,
  "addressId": 1,
  "cartItems": [
    {"skuId": 1, "quantity": 2}
  ],
  "voucherId": null,
  "remark": "备注"
}
```

#### 2. 查询订单列表
```http
GET /order/list
Authorization: Bearer {accessToken}
```

#### 3. 查询订单详情
```http
GET /order/{id}
Authorization: Bearer {accessToken}
```

#### 4. 取消订单
```http
POST /order/{id}/cancel
Authorization: Bearer {accessToken}
```

---

### 优惠券相关接口

#### 1. 获取优惠券列表
```http
GET /voucher/list?merchantId=1
```

#### 2. 领取优惠券
```http
POST /voucher/{id}/claim
Authorization: Bearer {accessToken}
```

#### 3. 查询我的优惠券
```http
GET /voucher/my
Authorization: Bearer {accessToken}
```

---

### AI智能客服接口

#### 1. 场景对话
```http
GET /v2/chat/scene?prompt=你好&chatId=123&scene=food
```

**参数说明**:
- `prompt`: 用户问题
- `chatId`: 会话ID(用于保持上下文)
- `scene`: 场景类型(campus/food/default)

**响应**: Server-Sent Events (SSE)流式响应

---

## 安全机制

### 1. JWT认证机制

#### 1.1 Token结构
```java
// AccessToken (短期)
有效期: 30分钟
用途: API访问认证

// RefreshToken (长期)
有效期: 7天
用途: 刷新AccessToken
```

#### 1.2 Token生成
```java
// 生成AccessToken
JwtUtil.createToken(userId)

// 生成RefreshToken
JwtUtil.createRefreshToken(userId)
```

#### 1.3 Token验证
```java
// 解析Token
Claims claims = JwtUtil.parseToken(token);

// 验证过期
JwtUtil.isTokenExpired(token);
```

### 2. 密码加密

#### 2.1 BCrypt加密
```java
// 加密
String encodedPassword = PwdUtil.encode(rawPassword);

// 验证
boolean isMatch = PwdUtil.match(rawPassword, encodedPassword);
```

#### 2.2 加密特性
- 自动加盐
- 每次加密结果不同
- 抗彩虹表攻击

### 3. 邮箱验证码防刷

#### 3.1 防刷策略
```java
// 防刷Key
String sentKey = RedisConstant.EMAIL_SENT + ":" + username + ":" + toEmail;

// 验证码Key
String codeKey = RedisConstant.EMAIL_CODE + ":" + username + ":" + toEmail;

// 限制
- 60秒内同一邮箱不能重复发送
- 验证码5分钟有效
- 验证后立即删除(防止重复使用)
```

### 4. 登录拦截器

#### 4.1 拦截器配置
```java
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        // 1. 获取Token
        String accessToken = request.getHeader("Authorization");
        
        // 2. 移除Bearer前缀
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        
        // 3. 验证Token
        if (JwtUtil.isTokenExpired(accessToken)) {
            response.setStatus(401);
            return false;
        }
        
        // 4. 解析用户ID并存入ThreadLocal
        Claims claims = JwtUtil.parseToken(accessToken);
        String userId = claims.getSubject();
        ThreadLocalUtil.set(userId);
        
        return true;
    }
    
    @Override
    public void afterCompletion(...) {
        ThreadLocalUtil.remove(); // 清理ThreadLocal
    }
}
```

#### 4.2 拦截规则
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/user/login",
                    "/user/register",
                    "/user/send-code",
                    "/user/forget",
                    "/merchant/list",
                    "/dish/list",
                    "/v2/chat/**"
                );
    }
}
```

### 5. 数据校验

#### 5.1 DTO校验
```java
public class LoginFormDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6-20位")
    private String password;
    
    @NotBlank(message = "验证码不能为空")
    private String code;
}
```

#### 5.2 Controller使用
```java
@PostMapping("/register")
public Result register(@RequestBody @Valid LoginFormDTO user) {
    userService.register(user);
    return Result.ok("注册成功");
}
```

### 6. 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        return Result.fail(e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail(message);
    }
}
```

---

## 缓存策略

### 1. Redis缓存工具 (CacheClient)

#### 1.1 缓存穿透解决方案
```java
// 逻辑: 查询Redis -> 不存在查询DB -> DB不存在缓存空值
public <R, ID> R queryWithPassThrough(
    String keyPrefix, 
    ID id, 
    Class<R> type, 
    Function<ID, R> dbFallback,
    Long expireTime, 
    TimeUnit timeUnit
) {
    String key = keyPrefix + id;
    String json = stringRedisTemplate.opsForValue().get(key);
    
    // 命中缓存
    if (StrUtil.isNotBlank(json)) {
        return JSONUtil.toBean(json, type);
    }
    
    // 判断是否为空值
    if (json != null) {
        return null;
    }
    
    // 查询数据库
    R r = dbFallback.apply(id);
    if (r == null) {
        // 缓存空值
        stringRedisTemplate.opsForValue().set(key, "", 
            CACHE_NULL_TTL, TimeUnit.MINUTES);
        return null;
    }
    
    // 写入Redis
    stringRedisTemplate.opsForValue().set(key, 
        JSONUtil.toJsonStr(r), expireTime, timeUnit);
    
    return r;
}
```

#### 1.2 缓存击穿解决方案(互斥锁)
```java
// 逻辑: 获取互斥锁 -> 查询DB -> 更新缓存 -> 释放锁
public <R, ID> R queryWithMutex(
    String keyPrefix, 
    ID id, 
    Class<R> type, 
    Function<ID, R> dbFallback,
    Long expireTime, 
    TimeUnit timeUnit
) {
    String key = keyPrefix + id;
    String json = stringRedisTemplate.opsForValue().get(key);
    
    if (StrUtil.isNotBlank(json)) {
        return JSONUtil.toBean(json, type);
    }
    
    String lockKey = "lock:merchant:" + id;
    R r = null;
    
    try {
        // 获取互斥锁
        boolean tryLock = tryLock(lockKey);
        if (!tryLock) {
            // 获取锁失败,休眠重试
            Thread.sleep(50);
            return queryWithMutex(keyPrefix, id, type, 
                dbFallback, expireTime, timeUnit);
        }
        
        // 获取锁成功,查询数据库
        r = dbFallback.apply(id);
        if (r == null) {
            stringRedisTemplate.opsForValue().set(key, "", 
                CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        
        // 写入Redis
        this.set(key, r, expireTime, timeUnit);
    } finally {
        unLock(lockKey);
    }
    
    return r;
}
```

#### 1.3 缓存击穿解决方案(逻辑过期)
```java
// 逻辑: 判断过期 -> 获取锁 -> 异步重建 -> 返回旧数据
public <R, ID> R queryWithLogicalExpire(
    String keyPrefix, 
    ID id, 
    Class<R> type, 
    Function<ID, R> dbFallback,
    Long expireTime, 
    TimeUnit timeUnit
) {
    String key = keyPrefix + id;
    String json = stringRedisTemplate.opsForValue().get(key);
    
    if (StrUtil.isBlank(json)) {
        return null;
    }
    
    // 命中缓存
    RedisData bean = JSONUtil.toBean(json, RedisData.class);
    R r = JSONUtil.toBean((JSONObject) bean.getData(), type);
    LocalDateTime deadLine = bean.getExpireTime();
    
    // 未过期,直接返回
    if (deadLine.isAfter(LocalDateTime.now())) {
        return r;
    }
    
    // 已过期,尝试获取锁
    String lockKey = LOCAL_MERCHANT_KEY + id;
    boolean lock = tryLock(lockKey);
    
    if (lock) {
        // 获取锁成功,异步重建
        CACHE_REBUILD_EXECUTOR.submit(() -> {
            try {
                R apply = dbFallback.apply(id);
                this.setWithLogicalExpire(key, apply, expireTime, timeUnit);
            } finally {
                unLock(lockKey);
            }
        });
    }
    
    // 返回过期数据
    return r;
}
```

### 2. Redis Key设计规范

```java
// 缓存Key格式: 项目名:模块:业务:标识

// 商家缓存
CACHE_MERCHANT_KEY = "cache:merchant:";

// 商家锁
LOCK_MERCHANT_KEY = "lock:merchant:";

// 邮箱验证码
EMAIL_CODE = "email:code:";

// 防刷标记
EMAIL_SENT = "email:sent:";
```

### 3. 缓存TTL设置

```java
// 商家缓存
CACHE_MERCHANT_TTL = 30; // 30分钟

// 空值缓存
CACHE_NULL_TTL = 2;      // 2分钟

// 验证码
EXPIRE_TIME = 300;       // 5分钟

// 防刷时间
SENT_TIME = 60;          // 60秒
```

### 4. 分布式ID生成

```java
@Component
public class RedisIdWorker {
    
    private static final long BEGIN_TIMESTAMP = 1704067200L;
    private static final long COUNT_BITS = 32L;
    
    public long nextId(String keyPrefix) {
        // 1. 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        
        // 2. 生成序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringRedisTemplate.opsForValue()
            .increment("icr:" + keyPrefix + ":" + date);
        
        // 3. 拼接并返回
        return timestamp << COUNT_BITS | count;
    }
}
```

---

## 部署说明

### 1. 环境要求

#### 1.1 基础环境
- **JDK**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 6.0+

#### 1.2 可选环境
- **RabbitMQ**: 消息队列
- **PostgreSQL**: 向量数据库
- **Ollama**: 本地AI模型

### 2. 配置文件

#### 2.1 application.yml配置

```yaml
# 服务器配置
server:
  port: 8080

# Spring配置
spring:
  # 数据源配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/takeaway
    username: root
    password: your_password
  
  # 邮件配置
  mail:
    host: smtp.qq.com
    port: 587
    username: ${MAIL_USERNAME_QQ}
    password: ${MAIL_PASSWORD_QQ}
    protocol: smtp
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  
  # Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
      connect-timeout: 2000
  
  # 文件上传配置
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 100MB
  
  # AI配置
  ai:
    openai:
      base-url: https://dashscope.aliyuncs.com/compatible-mode
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: qwen-max-latest
          temperature: 0.7
      embedding:
        options:
          model: text-embedding-v4
          dimensions: 1024

# MyBatis Plus配置
mybatis-plus:
  global-config:
    banner: false
    db-config:
      id-type: auto
  type-aliases-package: com.hope.domain.po
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    mapper-locations: classpath:com/hope/mapper/*.xml

# 阿里云OSS配置
aliyun:
  oss:
    endpoint: ${ALY_OSS_ACCESS_KEY_ENDPOINT}
    access-key-id: ${ALY_OSS_ACCESS_KEY_ID}
    access-key-secret: ${ALY_OSS_ACCESS_KEY_SECRET}
    bucket-name: ${ALY_OSS_ACCESS_KEY_BUCKET}

# 支付宝配置
alipay:
  app-id: your_app_id
  private-key: your_private_key
  public-key: alipay_public_key
  gateway: https://openapi-sandbox.dl.alipaydev.com/gateway.do
  notify-url: http://your-domain/api/alipay/toSuccess
  return-url: http://localhost:8080/api/alipay/toSuccess
```

### 3. 环境变量配置

```bash
# 邮件配置
export MAIL_USERNAME_QQ=your_email@qq.com
export MAIL_PASSWORD_QQ=your_email_authorization_code

# 阿里云OSS配置
export ALY_OSS_ACCESS_KEY_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
export ALY_OSS_ACCESS_KEY_ID=your_access_key_id
export ALY_OSS_ACCESS_KEY_SECRET=your_access_key_secret
export ALY_OSS_ACCESS_KEY_BUCKET=your_bucket_name

# OpenAI配置
export OPENAI_API_KEY=your_openai_api_key
```

### 4. 构建部署

#### 4.1 打包
```bash
mvn clean package -DskipTests
```

#### 4.2 运行
```bash
java -jar target/takeaway-0.0.1-SNAPSHOT.jar
```

#### 4.3 Docker部署
```dockerfile
FROM openjdk:17-slim
COPY target/takeaway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
# 构建镜像
docker build -t takeaway:latest .

# 运行容器
docker run -d -p 8080:8080 \
  -e MAIL_USERNAME_QQ=xxx \
  -e MAIL_PASSWORD_QQ=xxx \
  -e ALY_OSS_ACCESS_KEY_ID=xxx \
  -e ALY_OSS_ACCESS_KEY_SECRET=xxx \
  takeaway:latest
```

### 5. 数据库初始化

#### 5.1 创建数据库
```sql
CREATE DATABASE takeaway DEFAULT CHARSET utf8mb4;
```

#### 5.2 执行建表脚本
根据上述数据库设计章节创建所有表

#### 5.3 初始化数据
```sql
-- 插入测试管理员
INSERT INTO user (username, password, email, nickname, role) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 
        'admin@hope.com', '管理员', 2);
```

---

## 扩展功能

### 1. AI智能助手

#### 1.1 场景化对话
系统支持多种场景的智能对话:
- **校园助手**: 回答校园相关问题
- **订单助手**: 查询订单状态、处理售后
- **通用助手**: 通用问答服务

#### 1.2 向量检索
```java
// 文档向量化存储
Spring AI提供PostgreSQL pgvector集成
支持语义搜索和文档检索
```

#### 1.3 流式响应
```java
// 使用Server-Sent Events实现流式响应
@GetMapping(value = "/scene", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> chatWithScene(...) {
    return chatClient
        .prompt()
        .system(s -> s.text(systemPrompt))
        .user(prompt)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
        .stream()
        .content();
}
```

### 2. 日志监控

#### 2.1 AOP日志切面
```java
@Aspect
@Component
public class LoggingAspect {
    
    @Around("@annotation(com.hope.anno.MyLog)")
    public Object logAround(ProceedingJoinPoint joinPoint) {
        // 记录方法执行时间
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            
            log.info("方法执行耗时: {}ms", endTime - startTime);
            return result;
        } catch (Throwable e) {
            log.error("方法执行异常", e);
            throw e;
        }
    }
}
```

#### 2.2 日志配置
```xml
<!-- logback.xml -->
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/app.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <maxHistory>30</maxHistory>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### 3. 文件上传

#### 3.1 阿里云OSS集成
```java
@Component
public class AliOSSUtils {
    
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    
    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;
    
    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;
    
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    
    public String upload(MultipartFile file) throws IOException {
        OSS ossClient = new OSSClientBuilder()
            .build(endpoint, accessKeyId, accessKeySecret);
        
        String fileName = UUID.randomUUID() + 
                          file.getOriginalFilename();
        
        ossClient.putObject(bucketName, fileName, 
                          file.getInputStream());
        
        ossClient.shutdown();
        
        return "https://" + bucketName + "." + 
               endpoint + "/" + fileName;
    }
}
```

### 4. 支付集成

#### 4.1 支付宝支付流程
```
1. 用户下单
2. 调用支付宝API生成支付链接
3. 用户跳转支付宝沙箱完成支付
4. 支付宝回调通知
5. 更新订单状态
```

#### 4.2 支付配置
```java
@Configuration
public class AliPayConfig {
    
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
            alipay.getGateway(),
            alipay.getAppId(),
            alipay.getPrivateKey(),
            "json",
            "UTF-8",
            alipay.getPublicKey(),
            "RSA2"
        );
    }
}
```

### 5. 优惠券秒杀

#### 5.1 秒杀流程
```
1. 用户点击抢券
2. 检查库存(Redis)
3. 扣减库存
4. 生成优惠券订单
5. 异步写入数据库
```

#### 5.2 库存扣减
```java
// Redis原子操作
Long stock = stringRedisTemplate.opsForValue()
    .decrement("seckill:voucher:" + voucherId);

if (stock == null || stock < 0) {
    // 库存不足,回滚
    stringRedisTemplate.opsForValue()
        .increment("seckill:voucher:" + voucherId);
    throw new BusinessException("优惠券已抢完");
}
```

---

## 附录

### A. 错误码定义

```java
// 通用错误码
200: 成功
400: 请求参数错误
401: 未授权/Token过期
403: 无权限
404: 资源不存在
500: 服务器内部错误

// 业务错误码
1001: 用户名不存在
1002: 密码错误
1003: 邮箱已存在
1004: 用户名已被占用
1005: 验证码错误
1006: 验证码已过期
1007: 60秒内已发送验证码
1008: 两次密码不一致
```

### B. 常见问题

#### Q1: Token过期后如何处理?
A: 使用RefreshToken调用 `/user/refreshToken` 接口获取新的Token对

#### Q2: 验证码收不到怎么办?
A: 检查:
1. 邮箱配置是否正确
2. 邮箱授权码是否有效
3. 60秒内是否重复发送

#### Q3: 图片上传失败?
A: 检查:
1. 阿里云OSS配置是否正确
2. 文件大小是否超过限制
3. 文件格式是否支持

#### Q4: Redis连接失败?
A: 检查:
1. Redis服务是否启动
2. 密码是否正确
3. 防火墙是否开放端口

### C. 联系方式

- 项目名称: Hope外卖系统
- 技术支持: hope@example.com
- 文档版本: v1.0
- 更新日期: 2026-03-15

---

**文档结束**
