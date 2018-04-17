# QuickDevelopFrame
快速开发android app的一套框架，
实现了fragment /activity 的变形mvp扩展抽取
一套刷新逻辑 已经重写了各种 刷新都是基于一套逻辑
还有很多的base适配器
实现一个适配器不需要创建viewholder 
已默认开启 databind ,mvvm

内嵌多套工具类。 
```groovy

allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

```groovy

dependencies {
	        compile 'com.github.qssq:QuickDevelopFrame:v1.0'
	}
```

```
    public class MyApp extends SuperAppContext {
}

```

```
    SuperAppContext.showToast("xxxx");
 ```
    
