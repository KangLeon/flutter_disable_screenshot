package com.kangleon.flutter_disable_screenshot;

import android.app.Activity;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class FlutterDisableScreenshotPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {

    private MethodChannel methodChannel;

    private Activity activity;

    private boolean isDisabled;

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        setDisabled(this.isDisabled, null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {

    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        methodChannel = new MethodChannel(binding.getBinaryMessenger(), "flutter_disable_screen_shot");
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        methodChannel.setMethodCallHandler(null);
    }

    private void setDisabled(boolean isDisabled, MethodChannel.Result result) {
        if (activity != null) {
            if (isDisabled)
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            else
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
            if (result != null)
                result.success(true);
        } else {
            if (result != null)
                result.notImplemented();
        }
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {
            case "setDisabled":
                boolean value = call.argument("disabled");
                this.isDisabled = value;
                setDisabled(value, result);
                break;
        }
    }
}
