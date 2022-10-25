package wr40.orm;

import java.util.List;

public class H2QueryBuilder implements QueryBuilder {

  @Override
  public CreateTable createTable(String tableName) {
    return new CreateTableBuilder(tableName);
  }

  static class CreateTableBuilder implements CreateTable, NamedColumn, TypedColumn, ForeignKey, ReferredForeignKey {

    private Table table;
    private Column currentColumn;
    public CreateTableBuilder(String tableName) {
      this.table = new Table();
      this.table.tableName = tableName;
    }

    @Override
    public WithColumns ifNotExists() {
      this.table.ifNotExists = true;
      return this;
    }

    @Override
    public NamedColumn withColumn(String columnName) {
      this.currentColumn = new Column();
      currentColumn.name = columnName;
      this.table.columns.add(currentColumn);
      return this;
    }

    @Override
    public TypedColumn ofType(String columnType) {
      currentColumn.type = columnType;
      return this;
    }

    @Override
    public TypedColumn withAutoIncrement() {
      currentColumn.autoIncrement = true;
      return this;
    }

    @Override
    public TypedColumn nullable() {
      currentColumn.nullable = true;
      return this;
    }

    @Override
    public TypedColumn notNull() {
      currentColumn.nullable = false;
      return this;
    }

    @Override
    public TypedColumn primaryKey(String constraintName) {
      currentColumn.primaryKey = true;
      currentColumn.primaryKeyName = constraintName;
      return this;
    }

    @Override
    public ForeignKey foreignKey(String constraintName) {
      currentColumn.foreignKey = true;
      currentColumn.foreignKeyName = constraintName;
      return this;
    }

    @Override
    public ConfiguredTable and() {
      return this;
    }

    @Override
    public ReferredForeignKey refersTo(String tableName, String columnName) {
      currentColumn.foreignKeyRefTable = tableName;
      currentColumn.foreignKeyRefColumn = columnName;
      return this;
    }

    @Override
    public TypedColumn unique() {
      currentColumn.foreignKeyUnique = true;
      return this;
    }

    @Override
    public TypedColumn nonUnique() {
      currentColumn.foreignKeyUnique = false;
      return this;
    }

    @Override
    public String build() {
      StringBuilder sb = new StringBuilder();
      sb.append("CREATE TABLE ");

      sb.append(table.tableName).append(" ");

      if (table.ifNotExists) {
        sb.append("IF NOT EXISTS ");
      }

      sb.append("(\n");

      for (Column column : table.columns) {
        sb.append(column.name).append(" ").append(column.type).append(" ");

        if (column.nullable) {
          sb.append("NULL ");
        } else {
          sb.append("NOT NULL ");
        }

        if (column.autoIncrement) {
          sb.append("AUTO_INCREMENT ");
        }

        if (column.primaryKey) {
          sb.append("PRIMARY KEY ");
        }
        sb.deleteCharAt(sb.length() - 1).append(",\n");
      }
      sb.delete(sb.length() - 2, sb.length() - 1).append(");");
      return sb.toString();
    }
  }
}
