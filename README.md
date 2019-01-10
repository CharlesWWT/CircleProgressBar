![error](https://github.com/MissGongYi/CircleProgressBar/blob/master/show.gif)
# how to use
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
