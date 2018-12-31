# JAVA开发各种工具类集合
注：各功能可能需要引入相关的JAR包或需要安装相关的软件才能正常使用。

## com.zcj.ext : 第三方相关的工具类
| 包名          | 第三方名称    |  简介  |
| --------      | :-----        | :---- |
| amap          | 高德地图      | 根据基站和WIFI数据查询经纬度|
| elasticsearch | ElasticSearch | 简单封装|
| fastdfs       | FastDFS       | 简单封装|
| jpush         | JPush         | 简单封装|
| mongodb       | MongoDB       | -|
| oss.aliyun    | 阿里云存储    | 简单封装|
| pingpp        | Ping++        | 简单封装[已废弃]|

## com.zcj.util : 各种工具类
| 包名          | 用途    |  简介  |
| --------      | :-----        | :---- |
| code          | 代码生成工具      | 可通过在Entity上注解来生成SQL、Java、Html代码文件|
| convertor     | 文件格式转换工具  | SWF/PDF/OFFICE/JPG 相关格式文件的相互转换 |
| email         | 发送邮件工具      | 可发送文本邮件和HTML邮件 |
| filenameutils | 文件名处理工具    | 拷贝自apache的包 |
| freemarker    | Freemarker工具    | 可用于实现关键字过滤、HTML处理 |
| jdbc          | 数据库相关工具    | 可用于获取数据相关的信息 |
| json.gson     | GSON相关工具      | 包含GSON转换时字段过滤器和字段名称修改器 |
| net           | 网络相关工具      | 多线程下载工具 |
| poi.excel     | EXCEL相关工具     | 可实现Excel的导入和导出功能 |
| vod           | 视频相关工具      | 视频转码、视频截图 |
| zxing         | 二维码工具        | 可生成二维码和识别二维码 |

| 类名          | 用途    |  简介  |
| --------      | :-----        | :---- |
| FtpUtil       | FTP相关         | 可连接FTP、上传文件、下载文件 |
| UtilClass     | Class操作相关   | 读取package下的class、读取class里的field |
| UtilCollection| 集合相关操作    | 初始化集合 |
| UtilConvert   | 格式相关转换    | 集合、Map、String、小数等格式的转换 |
| UtilCookie    | Cookie相关      | 设置Cookie |
| UtilCrypto    | 加解密          | 可加密和解密 |
| UtilDate      | 日期            | 日期转换、日期初始化、日期计算等 |
| UtilEncryption| MD5加密工具     | 可用于MD5加密 |
| UtilFile      | 文件操作        | 图片截图、压缩图片、下载文件、ZIP打包、删除、获取MIME类型、文件名操作 |
| UtilFileMd5   | 文件MD5获取     | 可获取文件的MD5值 |
| UtilHtml      | HTML内容工具    | 可提取图片内容、可过滤和处理HTML标签 |
| UtilImage     | 图片处理        | 可压缩图片、加水印 |
| UtilJdbc      | 数据库相关操作  | 可读取数据库和表的信息、查询数据并转化 |
| UtilMath      | 数学公式        | 计算百分比、平均值、极大值、极小值、中值、方差、标准差、区分度、弧长、两坐标距离等 |
| UtilMedia     | 音视频          | 可判断文件格式类型 |
| UtilMyBatis   | SQL文件的执行   | 借助MyBatis，实现SQL文件的执行 |
| UtilPc        | 电脑相关工具    | 可获取MAC地址 |
| UtilPinyin    | 拼音工具        | 借助pinyin4j获取中文拼音的全拼和首拼 |
| UtilProperties| Properties文件  | 可增删改查Properties文件内的配置属性 |
| UtilRandom    | 随机数据生成器  | 可随机生成时间、身份证、Email、手机号、中文姓名、地址 |
| UtilSecurity  | 加解密          | 可加密和解密 |
| UtilString    | 字符串相关      | 可通过字符串验证IP地址、邮箱、MAC地址、手机号、车牌、中文、身份证<br>可通过字符串获取生日、年龄、数字<br>可生成各种系统唯一码<br>可移除字符串中元素、截取字符串 |
| UtilUri       | Uri相关         | URL地址相关的处理和判断 |

## com.zcj.web : WEB开发相关基础类
| 包名          | 用途    |  简介  |
| --------      | :-----        | :---- |
| context       | 内容存储和获取   | session 和 ThreadLocal的内容获取 |
| dto           | 数据传输对象     | 各类操作时的数据传输对象：文件上传下载时、API调用时、分页查询时 |
| listener      | -                | ServletContextListener 的基础实现类，包含了数据库的获取和操作方法 |
| mybatis       | WEB开发的基础类  | 包含了 Entity、ORM、Service 的基础类 |
| spring        | Spring相关工具   | Bean 的获取 |
| springmvc     | WEB开发的基础类  | 包含了控制层的基础类、各种拦截器、文件上传下载构建工具 |
| struts        | 基于Struts框架的基础类    | 已废弃 |