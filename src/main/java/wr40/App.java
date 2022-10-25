package wr40;

import wr40.orm.H2QueryBuilder;
import wr40.orm.QueryBuilder;

public class App {

  public static void main(String[] args) {
    QueryBuilder qb = new H2QueryBuilder();

    String createTable = qb.createTable("book")
        .ifNotExists()
        .withColumn("id").ofType("long").notNull().withAutoIncrement().primaryKey().and()
        .withColumn("title").ofType("varchar(255)").notNull().and()
        .withColumn("author").ofType("varchar(255)").notNull().and()
        .withColumn("published").ofType("date").nullable().and()
        .withColumn("ISBN").ofType("varchar(128)").nullable().and()
        .build();

    System.out.println(createTable);
  }
}
