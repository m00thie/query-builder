package wr40.orm;

import java.util.ArrayList;
import java.util.List;

public interface QueryBuilder {

  CreateTable createTable(String tableName);

  interface CreateTable extends IfNotExists, ConfiguredTable {}

  interface ConfiguredTable extends WithColumns, BuildQuery {}

  interface BuildQuery {
    String build();
  }

  interface IfNotExists {
    WithColumns ifNotExists();
  }

  interface WithColumns {
    NamedColumn withColumn(String columnName);
  }

  interface NamedColumn {
    TypedColumn ofType(String columnType);
  }

  interface TypedColumn {
    TypedColumn withAutoIncrement();
    TypedColumn nullable();
    TypedColumn notNull();
    default TypedColumn primaryKey() {
      return primaryKey(null);
    }
    TypedColumn primaryKey(String constraintName);
    ForeignKey foreignKey(String constraintName);
    ConfiguredTable and();
  }

  interface ForeignKey {
    ReferredForeignKey refersTo(String tableName, String columnName);
  }

  interface ReferredForeignKey {
    TypedColumn unique();
    TypedColumn nonUnique();
  }

  class Table {
    String tableName;
    String action;
    boolean ifNotExists;
    boolean ifExists;
    List<Column> columns = new ArrayList<>();
  }

  class Column {
    String name;
    String type;
    String comment;
    boolean autoIncrement;
    boolean nullable;
    boolean primaryKey;
    String primaryKeyName;
    boolean foreignKey;
    String foreignKeyName;
    String foreignKeyRefTable;
    String foreignKeyRefColumn;
    boolean foreignKeyUnique;
  }
}
