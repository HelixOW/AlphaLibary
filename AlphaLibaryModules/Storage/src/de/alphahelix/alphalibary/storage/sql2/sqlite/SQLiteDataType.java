package de.alphahelix.alphalibary.storage.sql2.sqlite;

import de.alphahelix.alphalibary.storage.sql2.SQLDataType;

public enum SQLiteDataType implements SQLDataType {
	NULL {
		@Override
		public String sqlName() {
			return this.name();
		}
	},
	INTEGER {
		@Override
		public String sqlName() {
			return this.name();
		}
	},
	REAL {
		@Override
		public String sqlName() {
			return this.name();
		}
	},
	TEXT {
		@Override
		public String sqlName() {
			return this.name();
		}
	},
	BLOB {
		@Override
		public String sqlName() {
			return this.name();
		}
	}
}
