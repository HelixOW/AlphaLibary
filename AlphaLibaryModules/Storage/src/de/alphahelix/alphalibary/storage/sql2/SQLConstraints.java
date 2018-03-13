package de.alphahelix.alphalibary.storage.sql2;

public enum SQLConstraints {
	
	/**
	 * Ensures that a column cannot have a NULL value
	 */
	NOT_NULL,
	/**
	 * Ensures that all values in a column are different
	 */
	UNIQUE,
	/**
	 * A combination of a NOT NULL and UNIQUE. Uniquely identifies each row in a table
	 */
	PRIMARY_KEY,
	/**
	 * Uniquely identifies a row/record in another table
	 */
	FOREIGN_KEY,
	/**
	 * Ensures that all values in a column satisfies a specific condition
	 */
	CHECK,
	/**
	 * Sets a default value for a column when no value is specified
	 */
	DEFAULT,
	/**
	 * Used to create and retrieve data from the database very quickly
	 */
	INDEX
	
}
