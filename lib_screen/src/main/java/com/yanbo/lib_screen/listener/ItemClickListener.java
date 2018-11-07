package com.yanbo.lib_screen.listener;

/**
 * Created by lzan13 on 2018/3/10.
 */
public abstract class ItemClickListener implements ICListener {

    @Override
    public abstract void onItemAction(int action, Object object);

    @Override
    public void onItemLongAction(int action, Object object) {
        
    }
}
