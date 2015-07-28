package leo.nero.com.leo;

import android.app.Application;

import leo.nero.com.leo.sp.Nero;

/**
 * Created by zhouyou on 2015/7/17.
 */
public class App extends Application {

    private static App instance = new App();

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Nero.init(this);
    }
}
