package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.server.path.PathConstants.FIELD_SEPARATOR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

import junit.framework.TestCase;
import edu.wustl.cab2b.server.path.PathConstants;
import edu.wustl.common.util.logger.Logger;

public class MySqlLoaderTest extends TestCase {
    private static Connection con = TestUtil.getConnection();

    private File file = null;

    @Override
    protected void setUp() throws Exception {
        Logger.configure();
        String createTableSQL = "create table TEST_TABLE1 (ID BIGINT(38) NOT NULL, NAME VARCHAR(10) NULL,PRIMARY KEY (ID))";
        SQLQueryUtil.executeUpdate(createTableSQL, con);
    }

    public void testMySqlLoader() {
        String str = "SomeName";
        String home = System.getProperty("user.home");
        file = new File(home, "GeneratedFromMySqlLoaderTest.txt");
        String fileName = file.getAbsolutePath();
        file.delete();
        try {
            file.createNewFile();
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            for (int i = 1; i < 101; i++) {
                fileWriter.write(Long.toString(i));
                fileWriter.write(FIELD_SEPARATOR);
                fileWriter.write(str + i);
                fileWriter.write("\n");
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Unable to write to file " + fileName, false);
        }
        String columns = "(ID,NAME)";
        String tableName = "TEST_TABLE1";
        new MySqlLoader().loadDataFromFile(con, fileName, columns, tableName, null,PathConstants.FIELD_SEPARATOR);

        String selectSQL = "SELECT ID,NAME FROM TEST_TABLE1";
        int recordCount = 0;
        String[][] rs = null;

        rs = SQLQueryUtil.executeQuery(selectSQL, con);

        for (int i = 0; i < rs.length; i++) {
            recordCount++;
            Long id = Long.parseLong(rs[i][0]);
            String name = (String) rs[i][1];
            assertTrue(id > 0 && id < 101);
            assertTrue(name.startsWith(str));
        }
        assertEquals(100, recordCount);
        file.delete();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        if (file != null) {
            file.delete();
        }
        SQLQueryUtil.executeUpdate("DROP table TEST_TABLE1", con);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        TestUtil.close(con);
    }
}
