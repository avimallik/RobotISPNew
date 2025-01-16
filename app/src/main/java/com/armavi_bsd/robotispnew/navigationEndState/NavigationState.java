package com.armavi_bsd.robotispnew.navigationEndState;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class NavigationState {
    public void navigateToActivity(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
