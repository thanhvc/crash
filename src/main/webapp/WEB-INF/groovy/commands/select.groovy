import org.crsh.console.ConsoleBuilder;
import org.kohsuke.args4j.Argument;
import javax.jcr.query.Query;
import org.crsh.console.ConsoleBuilder;
import org.crsh.display.DisplayBuilder;
import org.kohsuke.args4j.Option;

public class select extends org.crsh.shell.AnyArgumentClassCommand {

  @Option(name="-o",aliases=["--offset"],usage="The result offset")
  def Integer offset;

  @Option(name="-l",aliases=["--limit"],usage="The result limit")
  def Integer limit;

  public Object execute() throws ScriptException {
    assertConnected();

    //
    def queryMgr = session.workspace.queryManager;

    //
    def statement = "select";
    arguments.each { statement += " " + it };

    //
    def query = queryMgr.createQuery(statement, Query.SQL);

    //
    def result = query.execute();

    // Column we will display
    def columnNames = [];

    //
    def nodes = result.nodes;
    if (offset != null) {
      nodes.skip(offset);
    }

    //
    def builder = new DisplayBuilder();

    //
    builder.node {
      def index = 0;
      while (nodes.hasNext()) {
        def n = nodes.next();
        if (limit != null && index >= limit)
          break;
        node(n.path) {
          n.properties.each() { property ->
            label(property.name + ": " + formatPropertyValue(property));
          }
        }
        index++;
      }
    }

/*
    //
    def builder = new ConsoleBuilder();
    builder.table {
      ['foo'].each { a ->
        row {
          result.columnNames.each { columnName ->
            cell(columnName)
          }
        }
      }
    };


    builder.table {
      def index = 0;
      while (rows.hasNext()) {
        def r = rows.next();
        if (limit != null && index >= limit)
          break;
        row {
          result.columnNames.each { columnName ->
            def value = r.getValue(columnName);
            cell(value!=null?formatValue(value):'');
          }
        }
        index++;
      }
    }
*/
    //
    return builder;
  }
}