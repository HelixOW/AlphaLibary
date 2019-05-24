package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class DataWatcherWrapper {
	
	private static final SaveMethod DW_SET = NMSUtil.getReflections().getDeclaredMethod("set",
			Utils.nms().getNMSClass("DataWatcher"), Utils.nms().getNMSClass("DataWatcherObject"),
			Object.class);
	
	private final Object dataWatcher;
	private boolean stackTrace = true;
	
	public static SaveMethod datawatcherSet() {
		return DataWatcherWrapper.DW_SET;
	}
	
	public void set(Object dataWatcherObject, Object value) {
		DataWatcherWrapper.datawatcherSet().invoke(this.getDataWatcher(), this.isStackTrace(), dataWatcherObject,
				value);
	}
	
}
