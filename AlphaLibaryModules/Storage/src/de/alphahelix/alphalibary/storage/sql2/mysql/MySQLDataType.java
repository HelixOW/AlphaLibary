package de.alphahelix.alphalibary.storage.sql2.mysql;

import de.alphahelix.alphalibary.storage.sql2.SQLDataType;

public enum MySQLDataType implements SQLDataType {
    INT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    TINYINT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    SMALLINT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    MEDIUMINT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    BIGINT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    FLOAT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    DOUBLE {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    DECIMAL {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    DATE {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    DATETIME {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    TIMESTAMP {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    TIME {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    YEAR {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    CHAR {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    VARCHAR {
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
    },
    TEXT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    TINYBLOB {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    TINYTEXT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    MEDIUMBLOB {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    MEDIUMTEXT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    LONGBLOB {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    LONGTEXT {
        @Override
        public String sqlName() {
            return this.name();
        }
    },
    ENUM {
        @Override
        public String sqlName() {
            return this.name();
        }
    }
}
