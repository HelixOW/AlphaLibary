package de.alphahelix.alphalibary.nms.wrappers;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class DataWatcherWrapper {

    private static final ReflectionUtil.SaveMethod S =
            ReflectionUtil.getDeclaredMethod("set", "DataWatcher",
                    ReflectionUtil.getNmsClass("DataWatcherObject"), Object.class);

    private Object dataWatcher;
    private boolean stackTrace = true;

    public DataWatcherWrapper(Object dataWatcher) {
        this.dataWatcher = dataWatcher;
    }

    public DataWatcherWrapper(Object dataWatcher, boolean stackTrace) {
        this.dataWatcher = dataWatcher;
        this.stackTrace = stackTrace;
    }

    public Object getDataWatcher() {
        return dataWatcher;
    }

    public boolean isStackTrace() {
        return stackTrace;
    }

    public DataWatcherWrapper setStackTrace(boolean stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    public void set(Object dataWatcherObject, Object value) {
        S.invoke(dataWatcher, stackTrace, dataWatcherObject, value);
    }
}
