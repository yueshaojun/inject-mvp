# inject-mvp
inject mvp
## 用来解决mvp模式中presenter无法复用的问题
### 使用示例：
1、添加依赖
app/build.gradle添加依赖：
```
 annotationProcessor project(':injector-compiler')
 compile project(':annotation')
```
2、使用presenter的类添加注解
```
 @Presenter(type = PresenterType.ACTIVITY)
 LoginPresenter loginPresenter;
```
使用@Presenter注解标记使用的presenter，type 指定为PresenterType.ACTIVITY（使用presnter的类是activity） 或者 PresenterType.FRAGMENT（使用presnter的类是fragment），目前只支持两种。
3、Application 添加成员
```
 AndroidBinder<Activity> activityAndroidBinder = ActivityBinder.create();
 AndroidBinder<Fragment> fragmentAndroidBinder = FragmentBinder.create();  
```
并实现接口ActivitySupporter,FragmentSupporter，在对应的实现方法中将其返回：
```
@Override
    public AndroidBinder<Activity> getActivitySupport() {
        return activityAndroidBinder;
    }

@Override
public AndroidBinder<Fragment> getFragmentSupport() {
    return fragmentAndroidBinder;
}
```
4、以下二选一
i、在Activity或者Fragment的基类中：对应的生命周期添加
((ActivitySupporter)getApplication()).getActivitySupport().bind(this);
或
((ActivitySupporter)getApplication()).getActivitySupport().unbind(this);
ii、在Application中注册生命周期：
```
registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                // 用法2
                activityAndroidBinder.bind(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // 用法2
                activityAndroidBinder.unbind(activity);
            }
        });
```
第二种方法，可以不需要基类处理，但是仅限于Activity类型。
### 注意
并不提供IOC的功能，***在bind方法调用前***需要自己实例或者通过Dagger实例对象。
