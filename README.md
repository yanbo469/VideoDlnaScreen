# **视频投屏，支持网络投屏和本地投屏**
**android Dlna开发 **

[GitHub主页](https://github.com/yanbo469/Utils）


## 集成方式

### 添加依赖

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```java
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
      maven {url 'http://4thline.org/m2'}
	}
 
}
```

Step 2. Add the dependency

```java
android  {

 packagingOptions {
        exclude 'META-INF/beans.xml'
    }

  }
dependencies {
       compileOnly 'javax.enterprise:cdi-api:2.0'
       implementation 'com.github.yanbo469:VideoDlnaScreen:v1.0'
}

```

Step 3. Add the Initialization

```java
public class Application {

    @Override
    public void onCreate() {
        super.onCreate();
	    //初始化
      VApplication.init(this);
    }
}
