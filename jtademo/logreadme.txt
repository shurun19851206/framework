log4j输出多个自定义日志文件，动态配置路径 (2010-10-19 10:05:24)转载▼
标签： it	
1.    log4j输出多个自定义日志文件
 log4j的强大功能无可置疑，但实际应用中免不了遇到某个功能需要输出独立的日志文件的情况，怎样才能把所需的内容从原有日志中分离，形成单独的日志文件呢？其实只要在现有的log4j基础上稍加配置即可轻松实现这一功能。
　　先看一个常见的log4j.properties文件，它是在控制台和myweb.log文件中记录日志：
 
log4j.rootLogger=DEBUG, stdout, logfile
 
log4j.category.org.springframework=ERROR
log4j.category.org.apache=INFO
 
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m%n
 
log4j.appender.logfile=org.apache.log4j.RollingFileAppender
log4j.appender.logfile.File=${myweb.root}/WEB-INF/log/myweb.log
log4j.appender.logfile.MaxFileSize=512KB
log4j.appender.logfile.MaxBackupIndex=5
log4j.appender.logfile.layout=org.apache.log4j.PatternLayout
log4j.appender.logfile.layout.ConversionPattern=%d %p [%c] - %m%n
　　
如果想对不同的类输出不同的文件(以cn.com.Test为例)，先要在Test.java中定义:
private static Log logger = LogFactory.getLog(Test.class);
　　然后在log4j.properties中加入:
log4j.logger.cn.com.Test= DEBUG, test
log4j.appender.test=org.apache.log4j.FileAppender
log4j.appender.test.File=${myweb.root}/WEB-INF/log/test.log
log4j.appender.test.layout=org.apache.log4j.PatternLayout
log4j.appender.test.layout.ConversionPattern=%d %p [%c] - %m%n
　　也就是让cn.com.Test中的logger使用log4j.appender.test所做的配置。
 
　　但是，如果在同一类中需要输出多个日志文件呢？其实道理是一样的，先在Test.java中定义:
private static Log logger1 = LogFactory.getLog("myTest1");
private static Log logger2 = LogFactory.getLog("myTest2");
　　然后在log4j.properties中加入:
log4j.logger.myTest1= DEBUG, test1
log4j.appender.test1=org.apache.log4j.FileAppender
log4j.appender.test1.File=${myweb.root}/WEB-INF/log/test1.log
log4j.appender.test1.layout=org.apache.log4j.PatternLayout
log4j.appender.test1.layout.ConversionPattern=%d %p [%c] - %m%n
　　
log4j.logger.myTest2= DEBUG, test2
log4j.appender.test2=org.apache.log4j.FileAppender
log4j.appender.test2.File=${myweb.root}/WEB-INF/log/test2.log
log4j.appender.test2.layout=org.apache.log4j.PatternLayout
log4j.appender.test2.layout.ConversionPattern=%d %p [%c] - %m%n
　　也就是在用logger时给它一个自定义的名字(如这里的"myTest1")，然后在log4j.properties中做出相应配置即可。别忘了不同日志要使用不同的logger(如输出到test1.log的要用logger1.info("abc"))。
 
　　还有一个问题，就是这些自定义的日志默认是同时输出到log4j.rootLogger所配置的日志中的，如何能只让它们输出到自己指定的日志中呢？别急，这里有个开关：
log4j.additivity.myTest1 = false
　　它用来设置是否同时输出到log4j.rootLogger所配置的日志中，设为false就不会输出到其它地方啦！注意这里的"myTest1"是你在程序中给logger起的那个自定义的名字！
如果你说，我只是不想同时输出这个日志到log4j.rootLogger所配置的logfile中，stdout里我还想同时输出呢！那也好办，把你的log4j.logger.myTest1 = DEBUG, test1改为下式就OK啦！
log4j.logger.myTest1=DEBUG, test1, stdout
 
2.动态配置路径
若程序需要的日志路径需要不断的变化，而又不可能每次都去改配置文件，那就要采取两种方法。
第一种
        log4j的配置文件支持windows的环境变量，格式类似velocity:${env}，那我们就用环境变量表示可能会变化的路径。上文已使用“log4j.appender.test1.File=${myweb.root}/WEB-INF/log/test1.log”。
第二种
       这种方法是不用配置文件，而是在程序里用代码配置，代码是活的，所以路径肯定可以写活。示例如下：
 
view plaincopy to clipboardprint?
Logger myTest = Logger.getLogger("myTest");  
  
Layout layout = new PatternLayout("%d %p [%c] - %m%n");  
  
Appender appender = new FileAppender(layout, logFilePath);  
  
myTest.addAppender(appender);  
Logger myTest = Logger.getLogger("myTest"); Layout layout = new PatternLayout("%d %p [%c] - %m%n"); Appender appender = new FileAppender(layout, logFilePath); myTest.addAppender(appender);
 
 附：CoonversionPattern参数的格式含义
 
%c 输出日志信息所属的类的全名
%d 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy-MM-dd HH:mm:ss }，输出类似：2002-10-18- 22：10：28
%f 输出日志信息所属的类的类名
%l 输出日志事件的发生位置，即输出日志信息的语句处于它所在的类的第几行
%m 输出代码中指定的信息，如log(message)中的message
%n 输出一个回车换行符，Windows平台为“\r\n”，Unix平台为“\n”
%p 输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL。如果是调用debug()输出的，则为DEBUG，依此类推
%r 输出自应用启动到输出该日志信息所耗费的毫秒数
%t 输出产生该日志事件的线程名



##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
##############################################################################################################################################
其中log4j.properties简要配置如下:

复制代码
log4j.debug=true   
log4j.rootLogger=DEBUG,D,E   
log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
log4j.appender.E.File = logs/logs.log
log4j.appender.E.Append = true
log4j.appender.E.Threshold = DEBUG
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
#log4j.appender.E.layout.

log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = logs/error.log
log4j.appender.D.Append = true
log4j.appender.D.Threshold = ERROR
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
复制代码
下面讲一下log4j.properties的配置参数

1 基本格式如下：

复制代码
#配置根Logger
log4j.rootLogger  =   [ level ]   ,  appenderName1 ,  appenderName2 ,  …

#配置日志信息输出目的地Appender
log4j.appender.appenderName  =  fully.qualified.name.of.appender.class 
　　log4j.appender.appenderName.option1  =  value1 
　　… 
　　log4j.appender.appenderName.optionN  =  valueN 

#配置日志信息的格式（布局）
log4j.appender.appenderName.layout  =  fully.qualified.name.of.layout.class 
　　log4j.appender.appenderName.layout.option1  =  value1 
　　… 
　　log4j.appender.appenderName.layout.optionN  =  valueN 
复制代码
其中 [ level ]日志输出级别共有五级

FATAL      0  
ERROR      3  
WARN       4  
INFO       6  
DEBUG      7 
Appender 为日志输出目的地，Log4j提供的appender有以下几种：

org.apache.log4j.ConsoleAppender（控制台），
org.apache.log4j.FileAppender（文件），
org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件），
org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件），
org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
Layout：日志输出格式，Log4j提供的layout有以下几种：

org.apache.log4j.HTMLLayout（以HTML表格形式布局），
org.apache.log4j.PatternLayout（可以灵活地指定布局模式），
org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
打印参数: Log4J采用类似C语言中的printf函数的打印格式格式化日志信息，如下:

    %m   输出代码中指定的消息%p   输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL
    %r   输出自应用启动到输出该log信息耗费的毫秒数
    %c   输出所属的类目，通常就是所在类的全名 
    %t   输出产生该日志事件的线程名 
    %n   输出一个回车换行符，Windows平台为“\r\n”，Unix平台为“\n” 
    %d   输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss , SSS}，输出类似：2002年10月18日  22 ： 10 ： 28 ， 921 
    %l   输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。举例：test.main(test.java: 10 ) 
参数意义说明

输出级别的种类
ERROR 为严重错误 主要是程序的错误
WARN 为一般警告，比如session丢失
INFO 为一般要显示的信息，比如登录登出
DEBUG 为程序的调试信息
配置日志信息输出目的地
log4j.appender.appenderName=??
1.org.apache.log4j.ConsoleAppender（控制台）
2.org.apache.log4j.FileAppender（文件）
3.org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件）
4.org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件）
5.org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
配置日志信息的格式
log4j.appender.appenderName.layout = ??
1.org.apache.log4j.HTMLLayout（以HTML表格形式布局），
2.org.apache.log4j.PatternLayout（可以灵活地指定布局模式），
3.org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
4.org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
ConsoleAppender选项
Threshold=DEBUG:指定日志消息的输出最低层次。
ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
Target=System.err：默认情况下是：System.out,指定输出控制台
FileAppender 选项
Threshold=DEBUF:指定日志消息的输出最低层次。
ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
File=mylog.txt:指定消息输出到mylog.txt文件。
Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
RollingFileAppender 选项
Threshold=DEBUG:指定日志消息的输出最低层次。
ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
File=mylog.txt:指定消息输出到mylog.txt文件。
Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
MaxFileSize=100KB: 后缀可以是KB, MB 或者是 GB. 在日志文件到达该大小时，将会自动滚动，即将原来的内容移到mylog.log.1文件。
MaxBackupIndex=2:指定可以产生的滚动文件的最大数。
日志信息格式中几个符号所代表的含义：
 -X号: X信息输出时左对齐；
 %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
 %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
 %r: 输出自应用启动到输出该log信息耗费的毫秒数
 %c: 输出日志信息所属的类目，通常就是所在类的全名
 %t: 输出产生该日志事件的线程名
 %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main (TestLog4.java:10)
 %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
 %%: 输出一个"%"字符
 %F: 输出日志消息产生时所在的文件名称
 %L: 输出代码中的行号
 %m: 输出代码中指定的消息,产生的日志具体信息
 %n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行
示例的配置文件 log4j.properties

log4j.debug=true   
log4j.rootLogger=DEBUG,D,E

log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
log4j.appender.E.File = logs/logs.log
log4j.appender.E.Append = true
log4j.appender.E.Threshold = DEBUG
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
#log4j.appender.E.layout.

log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = logs/error.log
log4j.appender.D.Append = true
log4j.appender.D.Threshold = ERROR
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
加载配置文件时 控制台会显示log4j解析配置文件的过程 有错误会报错

log4j: Parsing for [root] with value=[DEBUG,D,E].
log4j: Level token is [DEBUG].
log4j: Category root set to DEBUG
log4j: Parsing appender named "D".
log4j: Parsing layout options for "D".
log4j: Setting property [conversionPattern] to [%-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n].
log4j: End of parsing for "D".
log4j: Setting property [threshold] to [ERROR].
log4j: Setting property [append] to [true].
log4j: Setting property [file] to [logs/error.log].
log4j: setFile called: logs/error.log, true
log4j: setFile ended
log4j: Appender [D] to be rolled at midnight.
log4j: Parsed "D" options.
log4j: Parsing appender named "E".
log4j: Parsing layout options for "E".
log4j: Setting property [conversionPattern] to [%-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n].
log4j: End of parsing for "E".
log4j: Setting property [file] to [logs/logs.log].
log4j: Setting property [threshold] to [DEBUG].
log4j: Setting property [append] to [true].
log4j: setFile called: logs/logs.log, true
log4j: setFile ended
log4j: Appender [E] to be rolled at midnight.
log4j: Parsed "E" options.
log4j: Finished configuring.
