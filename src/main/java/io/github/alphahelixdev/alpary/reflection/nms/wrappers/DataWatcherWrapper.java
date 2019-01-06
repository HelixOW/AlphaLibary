package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveMethod;

public class DataWatcherWrapper {

    private static final SaveMethod DW_SET = NMSUtil.getReflections().getDeclaredMethod("set",
            Utils.nms().getNMSClass("DataWatcher"), Utils.nms().getNMSClass("DataWatcherObject"),
            Object.class);

    private final Object dataWatcher;
    private boolean stackTrace = true;

    public DataWatcherWrapper(Object dataWatcher) {
        this.dataWatcher = dataWatcher;
    }

    public DataWatcherWrapper(Object dataWatcher, boolean stackTrace) {
        this.dataWatcher = dataWatcher;
        this.stackTrace = stackTrace;
    }

    public static SaveMethod datawatcherSet() {
        return DataWatcherWrapper.DW_SET;
    }

    public void set(Object dataWatcherObject, Object value) {
        DataWatcherWrapper.datawatcherSet().invoke(this.getDataWatcher(), this.isStackTrace(), dataWatcherObject,
                value);
    }

    public Object getDataWatcher() {
        return this.dataWatcher;
    }

    public boolean isStackTrace() {
        return this.stackTrace;
    }

    public DataWatcherWrapper setStackTrace(boolean stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

}
