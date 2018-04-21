package de.alphahelix.alphalibary.forms.particledata;

import com.google.common.base.Objects;

public class EffectData<T> {
	
	private final T dataValue;
	
	public EffectData(T dataValue) {
		this.dataValue = dataValue;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getDataValue());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		EffectData<?> that = (EffectData<?>) o;
		return Objects.equal(getDataValue(), that.getDataValue());
	}
	
	@Override
	public String toString() {
		return "EffectData{" +
				"dataValue=" + dataValue +
				'}';
	}
	
	public T getDataValue() {
		return dataValue;
	}
}
