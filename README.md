1、doc/first_method下资源来自：http://openjdk.java.net/projects/code-tools/jol
2、doc/second_method下资源来自：http://www.javaspecialists.co.za/archive/Issue078.html
3、doc/third_method下资源来自：https://github.com/twitter/commons/blob/master/src/java/com/twitter/common/objectsize/ObjectSizeCalculator.java
4、doc/fourth_method下资源来自：http://jroller.com/maxim/entry/again_about_determining_size_of
5、doc/fifth_method下资源来自：http://www.javamex.com/classmexer/
6、doc下“java-oject-size.jar”包在本项目下执行“mvn package”命令得到
7、由测试结果可知：“MemoryCounter.java”实现有瑕疵，使用“jol-core-0.3.2.jar”不稳定
8、要使用基于“java.lang.instrument.Instrumentation”的方式，必须指定“-javaagent”命令行参数
