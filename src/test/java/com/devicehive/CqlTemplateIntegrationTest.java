package com.devicehive;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlOperations;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertThat;

/**
 * Created by tmatvienko on 2/2/15.
 */
public class CqlTemplateIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CqlOperations cqlTemplate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm");

    @Test
    public void supportsPojoToCqlMappings() {
        insertEventUsingCqlString();
        insertEventUsingStatementBuildWithQueryBuilder();
        insertEventUsingPreparedStatement();

        ResultSet resultSet = cqlTemplate.query("select * from device_notification where device_guid='" + deviceGuid + "'");

        assertThat(resultSet.all().size(), Is.is(1));

        Select select1 = QueryBuilder.select().from("device_notification").where(QueryBuilder.eq("device_guid", deviceGuid2))
                .limit(10);
        ResultSet resultSet1 = cqlTemplate.query(select1);

        assertThat(resultSet1.all().size(), Is.is(1));

        Select select2 = QueryBuilder.select().from("device_notification").where(QueryBuilder.in("device_guid", deviceGuid, deviceGuid2)).limit(10);
        ResultSet resultSet2 = cqlTemplate.query(select2);

        assertThat(resultSet2.all().size(), Is.is(2));
    }

    private void insertEventUsingCqlString() {
        cqlTemplate.execute("insert into device_notification (id, notification, device_guid, timestamp) values ('" +
                String.valueOf(UUIDs.timeBased().timestamp()) + "', 'notification', '" + deviceGuid + "', '" + date.getTime() + "')");
    }

    private void insertEventUsingStatementBuildWithQueryBuilder() {
        Insert insertStatement = QueryBuilder.insertInto("device_notification").value("id", String.valueOf(UUIDs.timeBased().timestamp())).value("notification", "notification")
                .value("device_guid", deviceGuid2).value("timestamp", date);
        cqlTemplate.execute(insertStatement);
    }

    private void insertEventUsingPreparedStatement() {
        PreparedStatement preparedStatement = cqlTemplate.getSession().prepare("insert into device_notification (id, notification, device_guid, timestamp) values (?, ?, ?, ?)");
        Statement insertStatement = preparedStatement.bind(String.valueOf(UUIDs.timeBased().timestamp()), "notification", deviceGuid3, date);
        cqlTemplate.execute(insertStatement);
    }
}
