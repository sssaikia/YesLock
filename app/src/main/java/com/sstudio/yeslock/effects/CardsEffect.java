package com.sstudio.yeslock.effects;

/**
 * Created by Alan on 7/8/2017.
 */

import android.view.View;
import android.view.ViewPropertyAnimator;

import com.sstudio.yeslock.JazzyEffect;

public class CardsEffect implements JazzyEffect {

    private static final int INITIAL_ROTATION_ANGLE = 90;

    @Override
    public void initView(View item, int position, int scrollDirection) {
        item.setPivotX(item.getWidth() / 2);
        item.setPivotY(item.getHeight() / 2);
        item.setRotationX(INITIAL_ROTATION_ANGLE * scrollDirection);
        item.setTranslationY(item.getHeight() * scrollDirection);
    }

    @Override
    public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimator animator) {
        animator.rotationXBy(-INITIAL_ROTATION_ANGLE * scrollDirection)
                .translationYBy(-item.getHeight() * scrollDirection);
    }
}