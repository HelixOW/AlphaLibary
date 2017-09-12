package de.alphahelix.alphalibary.nms.wrappers;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class DataWatcherWrapper {

    private static final ReflectionUtil.SaveMethod S =
            ReflectionUtil.getDeclaredMethod("set", "DataWatcher",
                    ReflectionUtil.getNmsClass("DataWatcherObject"), Object.class);

    private final Object dataWatcher;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataWatcherWrapper that = (DataWatcherWrapper) o;
        return isStackTrace() == that.isStackTrace() &&
                Objects.equal(getDataWatcher(), that.getDataWatcher());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDataWatcher(), isStackTrace());
    }

    @Override
    public String toString() {
        return "DataWatcherWrapper{" +
                "dataWatcher=" + dataWatcher +
                ", stackTrace=" + stackTrace +
                '}';
    }
}
