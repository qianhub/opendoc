# 彩虹云宝开放接口

开放接口对外提供了访问食通宝数据的能力。目前仅只提供了访问收银机数据的能力。

约束说明:
* 所有接口使用 HTTP POST 方式
* Content-Type 为 application/json 
* 未做特殊说明情况下，字符集使用 UTF-8
* 所有请求都要做签名校验

## 鉴权

为了验证调用者身份(以下称为第三方)，每个接口都会做权限校验。校验方式采用了 RSA 非对称校验。

校验过程如下:
* 第三方提供一个 RAS 证书的公钥文件给食通宝，私钥文件自行保留，不能外泄。推荐使用 1024 位证书。
* 在请求的 HTTP Header 中必须包含2个字段:
    - X-QianHub-App  食通宝分配的 AppID
    - X-QianHub-Sign 对请求内容的签名， 使用私钥对整个 HTTP 的 Body 做签名，并将签名结果转成 Hex 格式，保存到 X-QianHub-Sign 中
* OpenAPI 接收到请求时，会使用第三方的公钥去校验请求内容是否合法，只有合法后才会继续后续操作。

## 接口说明

测试环境：(目前仅提供了测试环境)

URL: https://open.sanyitest.com/openapi

请求内容:
```javascript
{
    "version": "1.0", // 版本号，目前固定为 1.0, 必选字段
    "action": "sample.init", // 请求的方法，根据功能而定 必选字段
    "body": { // 请求的内容，根据功能而定 可选字段

    }
}
```

说明:
* 每个接口的 action 都会不同
* body 可以为 null

返回值:
* 当 HTTP Code = 200 时，返回是成功结果，内容就是返回的 Json 结构
* 当 HTTP Code = 400 时，返回业务领域的失败

400 时的异常信息:
```javascript
{
    "error_code": 1000, // 错误码，Int 类型，必选字段
    "message": "系统错误", // 错误描述，String 类型， 必选字段
    "extra_message": "Odd number of characters." // 附加信息，可选字段
}
```

## 测试账号

AppID: 507435914698

公钥:
```
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgIpJjMaj0YbGZLkbagg+qGfY3zuVKjj8muSfC
L7HugMANtKDj/ziExh8NSroA2bI6i0DR1n6slrio6bBWqnKFDZwjldDobDOrNXWNxbb3cIncDke9
CPbDdyWqaimchKMitVgVC8DWy38EDE2u5xQfaz8Tcedkllk7UJkWbciHMQIDAQAB
```

私钥:
```
MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOAikmMxqPRhsZkuRtqCD6oZ9jfO
5UqOPya5J8Ivse6AwA20oOP/OITGHw1KugDZsjqLQNHWfqyWuKjpsFaqcoUNnCOV0OhsM6s1dY3F
tvdwidwOR70I9sN3JapqKZyEoyK1WBULwNbLfwQMTa7nFB9rPxNx52SWWTtQmRZtyIcxAgMBAAEC
gYEAimOuQUL7UBE2CB+zrd0acOvgw+qiVptn0LAIJXUvhtTGQHAj20LNkeWGbL2UBUxlKJKsriOj
SUsyr1DDCW/qjqa0D2v0pr2/ZAVFVq9gYdYKBhabNDbwMettt2R3jXAHKs2oy4I2cd9wYW6Ue30V
PBqXy/Q80RXA3wcLSfs+E3ECQQD3z8R0h+lMhMTtaUdV9VA6a9WHXQpLx6kBoPkobd+OJ6ASbwyY
Arq4a9IV/ADyEhKNYeAQwjFKfYQuiXuNAU+NAkEA54qGas3uEkoNDVYPlOesf2XCpx934hIqw8Qn
emMZSZU8F1as/ZIw3S2lmoxMzfAEyFRWSEVpow/5KA5ydHgLNQJAO1+GPD1MAk9VN2Sf+NJbtIOd
l18NVnax4XgF+k/I3jBUQ9ZjeBA/WGxM24OTXmxCEam/m4RLdwN3pga+mVwVCQJBAJyUCghqEGgf
2am+HDVnYjiY+USJPEoOXQscOFJEd9JR8FwcCkpENXUtLENSQ5I0kRdkKSEgh6p039pdwrrOf00C
QGVoickJeJzMyJ+QJQyWH/PO0OpP57Ye4TUiloESf+pYSbZAHUgJ7pRFk/++5HkD9oDTGjkUsaK9
gXCfk2tfIok=
```

## 测试接口

为了方便验证接口的签名算法，特提供了最简单的测试接口，action = qianhub.test

请求头:

```text
X-QianHub-App: 507435914698
X-QianHub-Sign: 1dc43b8c1d86ea048ee63a13becf93986a5464f261fe871f4550cdc68a9bdddbea66e0b17b2b659830762699b73a7bfc676ce288233cc44b99afdb70898d2052d31d604664759cfa9ff25a85d3f35a8cf538cca9d491678c1e94b647efe3a265289224416321d4315a94a8e5b57fe4313336b692d57c19360a7ec1c865e9f808
```

请求内容(若改动，则签名也要相应变动):
```javascript
{"version":"1.0","action":"qianhub.test","body":{"name":"Jack"}}
```

返回值:
```javascript
{
    "name": "Jack"
}
```

