# 圆形进度条
![error](https://github.com/MissGongYi/CircleProgressBar/blob/master/preview/show.gif)
## in xml
```
<com.missgy.progressbar.CircleProgressView
        android:id="@+id/circleProgress"
        android:layout_width="250dp"
        android:layout_height="250dp"
        app:barWidth="23dp"
        app:endColor="#ff0000"
        app:startColor="#00ff08" />
```
## in activity
```
 circleProgress.setProgressNum(0.9,3000);
 ```
 First parameter is progress ,second parameter is execution animation
 ## custom parameters
 |name|Meaning|
|:---|:---|
|barWidth|线宽|
|startColor|进度条开始的颜色|
|endColor|进度条结束的颜色|
# 线型进度条
![error](https://github.com/MissGongYi/CircleProgressBar/blob/master/preview/show2.gif)
逻辑都差不多，就没写自定义属性，数据都写死了,主要设计角度和弧度之间的转换
