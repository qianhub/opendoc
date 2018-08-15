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
    - X-QianHub-Sign 对请求内容的签名， 使用私钥对整个 HTTP 的请求内容 做签名，并将签名结果转成 Hex 格式，保存到 X-QianHub-Sign 中
* OpenAPI 接收到请求时，会使用第三方的公钥去校验请求内容是否合法，只有合法后才会继续后续操作。

我们提供了简单测试样例[Java](./TestClient.java)，可供参考
## 接口说明

测试环境：(目前仅提供了测试环境)

URL: https://open.sanyitest.com/gateway

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

## 查询门店

查询当前 AppID 所能查看到的门店列表

请求:
```javascript
{
    "version": "1.0",
    "action": "qianhub.agent.list-shop"    
}
```

返回值:
```javascript
{
    "shops": [
        {
          "brand_id": 1, // 品牌 ID
          "brand_name": "哇哇叫",// 屏品牌名称
          "shop_id": 10, // 门店 ID 
          "sn": "20001", // 门店编码
          "shop_name": "【树莓派】哇哇叫欢乐海岸店", // 门店名称
          "shop_address": "深圳市南山区1-12铺", // 地址 可能为 null
          "shop_contact": "0755-26960000" // 联系方式 可能为 null
        }
    ]
}
```

## 查询门店数据

此接口是用于查询门店各种数据，接口的返回值根据请求的不同而不同，需要区别对待。

请求:
```javascript
{
    "version": "1.0",
    "action": "qianhub.agent.query",
    "body": {
      "shop_id": 10, // 门店 ID
      "boh_type": "cashier.finish" // 查询类型，值会有差别
      "begin": '2018-07-28 12:00:00', // 开始时间
      "end": '2018-07-28 15:00:00' // 截止时间
    }
}
```

此接口返回的是一个 JSON 数组，数组中的对象中除了 "json" 这个字段的内容会根据 boh_type 不同而不同, 其他字段都是固定的。
```javascript
[ // 返回的是数组
  {
    "shop_10": 10, // 门店的标识, Long
    "staff_id":1436, // 服务员 ID Long
    "staff_name": "王店", // 服务员项目 Long
    "boh_type": "cashier.finish", // 数据类型
    "json": {} // 数据结构，与 boh_type 有关
  }
]
```

## 批量查询门店数据

此接口是用于批量查询门店数据，接口的返回值根据请求的不同而不同，需要区别对待。

请求:
```javascript
{
    "version": "1.0",
    "action": "qianhub.agent.batch-query",
    "body": {
      "shop_id": 10, // 门店 ID
      "boh_types": ["cashier.finish", "handover.stat"], // 需要查的数据类型
      "begin": '2018-07-28 12:00:00', // 开始时间
      "end": '2018-07-28 15:00:00' // 截止时间
    }
}
```

该接口和查询指定数据类型的请求类似，只是 action 变成了 qianhub.agent.batch-query, 而请求参数中的 boh_types 变成了数组 (注意参数名为 boh_types)

而返回值的结构也是一样的，只是内容中的 boh_type 会根据内容而不同。

返回的结果是按照时间正序排序。

### 查询已结账的账单

boh_type = cashier.finish

返回值:
```javascript
[
  {  
    "shop_10": 10,
    "staff_id":1436,
    "staff_name": "王店",
    "boh_type": "cashier.finish",
    "json": {
      "brand_id": 1,  // 品牌 ID Long
      "brand_name": "哇哇叫", // 品牌名称 
      "shop_id": 10, // 门店 ID Long 
      "shop_name": "【树莓派】哇哇叫欢乐海岸店", // 门店名称
      "sn": "201807280001", // 账单编码
      "bill_id": 5539576104912, // 账单标识，全局唯一 Long
      "amount": 109, // 账单金额
      "real_amount": 109, // 应收金额
      "goods_amount": 109, // 金额
      "order_amount": 109, // 订单金额
      "paid": 109, // 已支付金额
      "surcharge": 0, // 服务费
      "mincharge": 0, // 低消差
      "discount": 0, // (旧)折扣(负数)
      "promotion": 0, // 促销(负数)
      "rounding": 0, // 抹零(是一种支付方式, 但要算到应收中，并从支付中去掉)
      "cashier_staff_id": 1436, // 收银员 ID Long
      "cashier_staff_name": "王店", // 收银员名称
      "undo_index": 0, // 反结账序号，如果大于0，说明是反结账后结账的账单
      "orders": [ // 账单包含的订单
        {
          "order_id": 5539576103912, // 订单 ID 全局唯一
          "order_name": "A09", // 订单名称，一般为餐桌名字
          "order_sn": "201807280001", // 订单的 SN
          "person_count": 6, // 就餐人数 Int
          "table_id": 392, // 餐桌 ID Long
          "order_amount": 109, // 订单金额
          "goods_amount": 109, // 菜品金额
          "surcharge": 0, // 服务费
          "mincharge": 0, // 低消差
          "discount": 0,// 折扣(负数)
          "promotion": 0, // 活动减免(负数)
          "voucher": 0, // 使用代金券
          "details": [
            {
              "detail_id": 5539576102912, // 点菜的 ID Long
              "goods": { // 菜品信息 
                "goods_id": 670, // 菜品 ID Long
                "goods_name": "蝴蝶虾", // 菜品名称
                "goods_sn": "9001293", // 菜品编码
                "product_type_id": 1, // 产品类型 ID
                "product_type_name": "普通", // 菜品类型名称
                "goods_group_id": 28, // 菜品大类 ID
                "goods_group_name": "蝴蝶虾", // 大类名称 
                "goods_group_sn": "181593785323", // 大类的 SN
                "goods_subgroup_id": 58, // 菜品小类 ID 
                "goods_subgroup_name": "海鲜类", // 菜品小类名称
                "goods_subgroup_sn": "196666227206", // 小类的 SN
                "unit_type_id": 36, // 规格 ID 
                "unit_type_name": "份" // 规格名称
              },
              "tags": [], // 菜品的标签
              "origin_price": 52, // 原价
              "current_price": 52, // 现价
              "real_amount": 52, // 实际的总价
              "quantity": 1, // 份数
              "void_quantity": 0, // 退量 
              "weight": 1, // 重量，称重时有值
              "staff_id": 1436, // 点餐员 ID
              "staff_name": "王店", // 点餐员名称
              "remark": "不要辣", // 备注信息，可以为 null
              "children": [
                {
                  "detail_id": 5539576102913,
                  "goods": {
                    "goods_id": 2668,
                    "goods_name": "鲜咸",
                    "goods_sn": "9001736",
                    "product_type_id": 7,
                    "product_type_name": "做法",
                    "goods_group_id": 178,
                    "goods_group_name": "鲜咸",
                    "goods_group_sn": "18159005323", // 大类的 SN
                    "goods_subgroup_id": 307,
                    "goods_subgroup_name": "做法",
                    "goods_subgroup_sn": "198888", // 小类的 SN
                    "unit_type_id": 36,
                    "unit_type_name": "份"
                  },
                  "tags": [
                    "cooking"
                  ],
                  "origin_price": 55,
                  "current_price": 55,
                  "quantity": 1,
                  "void_quantity": 0,
                  "weight": 1,
                  "staff_id": 1436,
                  "staff_name": "王店",
                  "children": []
                }
              ]
            },
            {
              "detail_id": 5539576102914,
              "goods": {
                "goods_id": 429,
                "goods_name": " 回鱼",
                "goods_sn": "9000974",
                "product_type_id": 3,
                "product_type_name": "时价",
                "goods_group_id": 28,
                "goods_group_name": " 回鱼",
                "goods_subgroup_id": 57,
                "goods_subgroup_name": "鱼类",
                "unit_type_id": 36,
                "unit_type_name": "份"
              },
              "tags": [],
              "origin_price": 2,
              "current_price": 2,
              "quantity": 1,
              "void_quantity": 0,
              "weight": 1,
              "staff_id": 1436,
              "staff_name": "王店",
              "children": [
                {
                  "detail_id": 5539576102915,
                  "goods": {
                    "goods_id": 2674,
                    "goods_name": "全银",
                    "goods_sn": "9000108",
                    "product_type_id": 7,
                    "product_type_name": "做法",
                    "goods_group_id": 178,
                    "goods_group_name": "全银",
                    "goods_group_sn": "18159005323", // 大类的 SN
                    "goods_subgroup_id": 307,
                    "goods_subgroup_name": "做法",
                    "goods_subgroup_sn": "198888", // 小类的 SN
                    "unit_type_id": 36,
                    "unit_type_name": "份"
                  },
                  "tags": [
                    "cooking"
                  ],
                  "origin_price": 0,
                  "current_price": 0,
                  "quantity": 1,
                  "void_quantity": 0,
                  "weight": 1,
                  "staff_id": 1436,
                  "staff_name": "王店",
                  "children": []
                }
              ]
            }
          ]
        }
      ],
      "payments": [ // 支付方式
        {
          "payment_type_id": 2, // 支付方式 ID Int
          "payment_type_name": "银行卡", // 支付方式名称
          "payment_type_sn": "2402", // 支付的 SN
          "value": 109, // 支付金额
          "change": 0, // 找零
          "unreal": 0 // 虚收
        }
      ],
      "promotions": [
        {
          "promotion_id": 892, // 活动标识
          "promotion_name": "会员折扣", // 活动名称
          "promotion_type_id": 11, // 活动类别 ID
          "promotion_type_name": "会员折扣", // 活动类型名称
          "value": -10 // 减免金额
        }
      ], // 活动减免明细
      "discounts": [
        {
          "discount_id": 34,
          "discount_name": "85折",
          "value": -22
        }
      ],// 折扣明细
      "create_on": "2018-07-28 15:29:38", // 创建时间
      "cashier_on": "2018-07-28 15:30:43" // 收银时间
    }
  }
]
```

几点说明:
* bill_id 是全局唯一的，但可能会返回重复数据，当逆结时，再重新结账后，bill_id 是不变的，第三方需要做更新账单的处理
* 未做特殊说明时，金额单位为 元，类型为 Double 

菜品标签说明:
* gift 赠送
* changed 改价
* special 特价
* return 退菜
* discount 折扣
* weight 称重
* ingredient 加料
* cooking 做法

promotion_type_id 促销活动类型说明:
* 1 特价
* 10 会员价
* 11 会员特价
* 24 秒付买单优惠(第三方支付系统的优惠活动)
* 25 第二份优惠

product_type_id 产品类型说明:
* 1 普通
* 2 称重
* 3 时价
* 4 套餐
* 5 特价
* 6 加料
* 7 做法

payment\_type\_id 和 payment_type\_sn 说明:

|payment_type\_id  | payment_type\_sn      | 支付名称 |
|:--            |:--      	|:--  |
|1           | 2401    		| 现金 |
|2        	| 2402    	| 银行卡 |
|3        	| 2403    	| 储值卡 |
|4        	| 2404    	| 支付宝|
|5        	| 2405    	| 微信 |
|6        	| 2406    	| 代金券 |
|8        	| 2408    	| 减免 |
|9        	| 2409    	| 赊账 |
|10        	| 2410    	| 抹零 |
|11        	| 2411    	| 低消差 |
|13        	| 2413    	| 服务费|
|14        	| 2400(12数字)| 自定义付款|
|16        	| 2416    	| 积分抵现 |

说明：
* 以上是 使用的默认编码，如果用户设置了自己的支付编码，就以用户的为准，没有设置，才会使用默认值。
 
### 查询交接信息

boh_type = handover.stat

返回值:
```javascript
[
  {
    "shop_id": 10,
    "staff_id": 1436,
    "staff_name": "刘涛",
    "boh_type": "handover.stat",
    "json": {
      "summary": [
        {
          "key": "order",
          "item": "开台数",
          "value": "3"
        },
        {
          "key": "person",
          "item": "就餐人数",
          "value": "14"
        },
        {
          "key": "avgOfPerson",
          "item": "人均消费",
          "value": "34.86"
        },
        {
          "key": "avgOfOrder",
          "item": "桌均消费",
          "value": "162.67"
        },
        {
          "key": "turnRate",
          "item": "翻台率",
          "value": "4.35%"
        },
        {
          "key": "personRate",
          "item": "上座率",
          "value": "5.13%"
        },
        {
          "key": "bill",
          "item": "收银单数",
          "value": "3"
        }
      ],
      "detail": [
        {
          "key": "cash",
          "item": "现金",
          "count": 3,
          "amount": 159,
          "subdetail": []
        },
        {
          "key": "card",
          "item": "银行卡",
          "count": 1,
          "amount": 59,
          "subdetail": []
        },
        {
          "key": "tenpay",
          "item": "微信",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "alipay",
          "item": "支付宝",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "voucher",
          "item": "代金券实收",
          "count": 2,
          "amount": 200,
          "subdetail": [
            {
              "key": "欢乐海岸75元抵100元代金券",
              "item": "欢乐海岸75元抵100元代金券",
              "count": 2,
              "amount": 200,
              "subdetail": []
            }
          ]
        },
        {
          "key": "debit",
          "item": "挂单",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "custom",
          "item": "自定义实收",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "cashierSum",
          "item": "合计",
          "count": 6,
          "amount": 418,
          "subdetail": []
        }
      ],
      "waive": [
        {
          "key": "discount",
          "item": "折扣",
          "count": 1,
          "amount": 69.8,
          "subdetail": [
            {
              "key": "欢乐海岸全单8折",
              "item": "欢乐海岸全单8折",
              "count": 1,
              "amount": 69.8,
              "subdetail": []
            }
          ]
        },
        {
          "key": "promotion",
          "item": "促销优惠",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "waive",
          "item": "免单",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "point",
          "item": "积分抵现",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "free",
          "item": "赠送",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "change",
          "item": "改价",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "rounding",
          "item": "抹零",
          "count": 1,
          "amount": 0.2,
          "subdetail": []
        },
        {
          "key": "unrealVoucher",
          "item": "代金券虚收",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "unrealCustom",
          "item": "自定义虚收",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "waiveSum",
          "item": "合计",
          "count": 2,
          "amount": 70,
          "subdetail": []
        }
      ],
      "sale": [
        {
          "key": "goodsSubGroup",
          "item": "菜品销售",
          "count": 26,
          "amount": 488,
          "subdetail": []
        },
        {
          "key": "surcharge",
          "item": "服务费",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "mincharge",
          "item": "低消差",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "tax",
          "item": "消费税",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "extra",
          "item": "不找赎",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "saleSum",
          "item": "合计",
          "count": 26,
          "amount": 488,
          "subdetail": []
        }
      ],
      "member": [
        {
          "key": "charge",
          "item": "充值实收",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "gift",
          "item": "充值赠送",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "realstorevalue",
          "item": "抵扣实收",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "giftstorevalue",
          "item": "抵扣赠送",
          "count": 0,
          "amount": 0,
          "subdetail": []
        },
        {
          "key": "point",
          "item": "积分抵现",
          "count": 0,
          "amount": 0,
          "subdetail": []
        }
      ],
      "goods": [
        {
          "key": "组合锅",
          "item": "组合锅",
          "count": 10,
          "amount": 348,
          "subdetail": [
            {
              "key": "海鲜类",
              "item": "海鲜类",
              "count": 3,
              "amount": 146,
              "subdetail": []
            },
            {
              "key": "鱼类",
              "item": "鱼类",
              "count": 4,
              "amount": 63,
              "subdetail": []
            },
            {
              "key": "肉类",
              "item": "肉类",
              "count": 3,
              "amount": 139,
              "subdetail": []
            }
          ]
        },
        {
          "key": "餐具",
          "item": "餐具",
          "count": 14,
          "amount": 140,
          "subdetail": [
            {
              "key": "餐具&茶位",
              "item": "餐具&茶位",
              "count": 14,
              "amount": 140,
              "subdetail": []
            }
          ]
        },
        {
          "key": "做法",
          "item": "做法",
          "count": 2,
          "amount": 0,
          "subdetail": [
            {
              "key": "做法",
              "item": "做法",
              "count": 2,
              "amount": 0,
              "subdetail": []
            }
          ]
        },
        {
          "key": "expectValue",
          "item": "合计",
          "count": 26,
          "amount": 488,
          "subdetail": []
        }
      ],
      "waimai": [],
      "waimaiGoods": []
    },
    "create_on": "2018-07-29 17:06:02"
  }
]
```
### 查询日结信息

boh_type = dayend.stat

日结统计与交接统计的格式一致, 只有以下少许差别: 
* 增加了营业日 businessDay

```javascript
{
  "json": {
    "businessDay": '2018-07-29', // 营业日结日
      "summary": [
      ]
    }
}
```

### 外卖账单信息

boh_type = waimai.order

**TODO 补充外卖订单格式**


