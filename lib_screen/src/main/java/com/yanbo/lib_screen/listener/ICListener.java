package com.yanbo.lib_screen.listener;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public interface ICListener {

    void onItemAction(int action, Object object);

    void onItemLongAction(int action, Object object);
}
