1、doc/first_method下资源来自：http://openjdk.java.net/projects/code-tools/jol<br/>
2、doc/second_method下资源来自：http://www.javaspecialists.co.za/archive/Issue078.html<br/>
3、doc/third_method下资源来自：https://github.com/twitter/commons/blob/master/src/java/com/twitter/common/objectsize/ObjectSizeCalculator.java<br/>
4、doc/fourth_method下资源来自：http://jroller.com/maxim/entry/again_about_determining_size_of<br/>
5、doc/fifth_method下资源来自：http://www.javamex.com/classmexer/<br/>
6、“java-object-size.jar”包在本项目下执行“mvn package”命令得到<br/>
7、由测试结果可知：“MemoryCounter.java”实现有瑕疵，使用“jol-core-0.3.2.jar”不稳定，“ObjectSizeCalculator”实现也有瑕疵<br/>
8、要使用基于“java.lang.instrument.Instrumentation”的方式，必须指定“-javaagent”命令行参数<br/>
9、如何使用？根据是否基于“java.lang.instrument.Instrumentation”，采用如下两种方式的其中一种：
```
java -javaagent:java-object-size.jar -classpath ".:java-object-size.jar" YourMainClass
java -classpath ".:java-object-size.jar" YourMainClass
```
