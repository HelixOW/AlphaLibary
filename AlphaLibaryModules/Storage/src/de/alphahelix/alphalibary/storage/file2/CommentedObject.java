package de.alphahelix.alphalibary.storage.file2;

import java.util.Arrays;
import java.util.Objects;

public class CommentedObject<T> {
	
	private final T object;
	private final String[] comment;
	
	public CommentedObject(T object, String... comment) {
		this.object = object;
		this.comment = comment;
	}
	
	@Override
	public int hashCode() {
		int result = Objects.hash(getObject());
		result = 31 * result + Arrays.hashCode(getComment());
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		CommentedObject that = (CommentedObject) o;
		return Objects.equals(getObject(), that.getObject()) &&
				Arrays.equals(getComment(), that.getComment());
	}
	
	@Override
	public String toString() {
		return "CommentedObject{" +
				"object=" + object +
				", comment=" + Arrays.toString(comment) +
				'}';
	}
	
	public T getObject() {
		return object;
	}
	
	public String[] getComment() {
		return comment;
	}
}
