# **视频投屏，支持网络投屏和本地投屏**
**android Dlna开发 **

[GitHub主页](https://github.com/yanbo469/VideoDlnaScreen)


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
	//必须添加，不加会报错
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
```

Step 4.使用方法

```java
 //得到当前搜索到的所有设备
 private List<ClingDevice> clingDevices;

 /**
  * 基于EventBus，回调会回来的值来显示当前找到的设备
  */
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEventBus(DeviceEvent event) {
     clingDevices = DeviceManager.getInstance().getClingDeviceList();
   }
   
  @Override
  public void onStart() {
     super.onStart();
     EventBus.getDefault().register(this);
   }

  @Override
  public void onStop() {
     super.onStop();
     EventBus.getDefault().unregister(this);
  }
  
     
  //选择你要投屏的设备；
  DeviceManager.getInstance().setCurrClingDevice(ClingDevice);
  
  //设置网络投屏的信息
  RemoteItem itemurl1 = new RemoteItem("一路之下", "425703", "张杰",107362668, "00:04:33", "1280x720", url1);
  
  //添加网络投屏的信息
  ClingManager.getInstance().setRemoteItem(itemurl1);
  
  //设置本地投屏的信息
  private List<DIDLObject> objectList;  
  
  final DIDLObject object = objectList.get(position);
  
	if (object instanceof Container) {
		//得到本地文件夹
		Container container = (Container) object;
		//点进文件夹刷新数据List<DIDLObject> objectList
		ClingManager.getInstance().searchLocalContent(containerId);
	} else if (object instanceof Item) {
		//得到本地文件
		Item item = (Item) object;
		// 设置本地投屏的信息
		ClingManager.getInstance().setLocalItem(item);     
	}


   public Item localItem;
   public RemoteItem remoteItem;
   localItem = ClingManager.getInstance().getLocalItem();
   remoteItem = ClingManager.getInstance().getRemoteItem();	
  /**
   * 播放开关
   */
  private void play() {
  
      if (ControlManager.getInstance().getState() == ControlManager.CastState.STOPED) {
		if (localItem != null) {
		
		  	 newPlayCastLocalContent();
		 } else {
		 
			 newPlayCastRemoteContent();
		 }
       } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PAUSED) {
       
        	 playCast();
       } else if (ControlManager.getInstance().getState() == ControlManager.CastState.PLAYING) {
       
           	 pauseCast();
       } else {
       
           	 Toast.makeText(getBaseContext(), "正在连接设备，稍后操作", Toast.LENGTH_SHORT).show();
        }
    }

  /**
   * 本地投屏
   */
  private void newPlayCastLocalContent() {
  
	ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
	
	ControlManager.getInstance().newPlayCast(localItem, new ControlCallback() {
	
	@Override
	public void onSuccess() {
	
	  ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
	  
	  ControlManager.getInstance().initScreenCastCallback();
	  
	     runOnUiThread(new Runnable() {
	     	  @Override
	          public void run() {
		  
		      playView.setImageResource(R.mipmap.ic_launcher_round);
		     }
	      }
	      
	     @Override
	     public void onError(int code, String msg) {
	     
		  ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
		  
		  showToast(String.format("New play cast local content failed %s", msg));
	});
   }


  /**
   * 网络投屏
   */
  private void newPlayCastRemoteContent() {
  
	 ControlManager.getInstance().setState(ControlManager.CastState.TRANSITIONING);
	 
	 ControlManager.getInstance().newPlayCast(remoteItem, new ControlCallback() {

		@Override
		public void onSuccess() {
		
			  ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);
			  
			  ControlManager.getInstance().initScreenCastCallback();
			  
			runOnUiThread(new Runnable() {
			
			    @Override
			    public void run() {
			    
				playView.setImageResource(R.mipmap.ic_launcher_round);
			    }
			});
            	}

		@Override
		public void onError(int code, String msg) {
		
			ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
			
			showToast(String.format("New play cast remote content failed %s", msg));
		    }
		});
  }
    
  /**
   * 播放
   */
  private void playCast() {
  
        ControlManager.getInstance().playCast(new ControlCallback() {
	
            @Override
            public void onSuccess() {
	    
                ControlManager.getInstance().setState(ControlManager.CastState.PLAYING);

			runOnUiThread(new Runnable() {
			    @Override
			    public void run() {
				playView.setImageResource(R.mipmap.ic_launcher_round);
			    }
			});
            }

            @Override
            public void onError(int code, String msg) {
	    
                showToast(String.format("Play cast failed %s", msg));
            }
        });
    }
    

  /**
   * 暂停
   */
  private void pauseCast() {
  
        ControlManager.getInstance().pauseCast(new ControlCallback() {
	
            @Override
            public void onSuccess() {
	    
                ControlManager.getInstance().setState(ControlManager.CastState.PAUSED);
		
                runOnUiThread(new Runnable() {
		
                    @Override
                    public void run() {
		    
                        playView.setImageResource(R.mipmap.ic_launcher_round);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
	    
                showToast(String.format("Pause cast failed %s", msg));
            }
        });
    }
    
/**
  * 退出投屏
  */
private void stopCast() {

        ControlManager.getInstance().stopCast(new ControlCallback() {
	
            @Override
            public void onSuccess() {
	    
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
		
                runOnUiThread(new Runnable() {
		
                    @Override
                    public void run() {
		    
                        playView.setImageResource(R.mipmap.ic_launcher_round);
                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
	    
                showToast(String.format("Stop cast failed %s", msg));
            }
        });
    }
    
  /**
   * 改变投屏进度
   */
  private void seekCast(int progress) {
  
         String target = VMDate.toTimeString(progress);
	 
         ControlManager.getInstance().seekCast(target, new ControlCallback() {
	 
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
	    
                showToast(String.format("Seek cast failed %s", msg));
            }
        });
    }
   
  /**
   * 设置音量大小
   */
  private void setVolume(int volume) {
  
         currVolume = volume;
         ControlManager.getInstance().setVolumeCast(volume, new ControlCallback() {
	 
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String msg) {
	    
                showToast(String.format("Set cast volume failed %s", msg));
            }
        });
    }
}
