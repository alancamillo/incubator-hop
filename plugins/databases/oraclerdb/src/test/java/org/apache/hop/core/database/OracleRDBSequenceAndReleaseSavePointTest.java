package org.apache.hop.core.database;

import org.apache.hop.core.util.Utils;
import org.apache.hop.junit.rules.RestorePDIEnvironment;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class OracleRDBSequenceAndReleaseSavePointTest {
    @ClassRule
    public static RestorePDIEnvironment env = new RestorePDIEnvironment();

    final String sequenceName = "sequence_name";

    //Set these parameters for the test
    DatabaseInterface db = new OracleRDBDatabaseMeta();
    Boolean sequenceSupport = true;
    Boolean savepointSupport = true;


    @Test
    public void testSequenceSupport(){
        assertSupports( db, sequenceSupport );
        assertEquals( "SELECT sequence_name.nextval FROM dual", db.getSQLNextSequenceValue( sequenceName ) );
        assertEquals( "SELECT sequence_name.currval FROM DUAL", db.getSQLCurrentSequenceValue( sequenceName ) );
    }

    @Test
    public void testSavepointSuport(){
        if(savepointSupport){
            assertTrue( db.releaseSavepoint() );
        }else{
            assertFalse( db.releaseSavepoint() );
        }
    }


    public static void assertSupports( DatabaseInterface db, boolean expected ) {
        String dbType = db.getClass().getSimpleName();
        if ( expected ) {
            assertTrue( dbType, db.supportsSequences() );
            assertFalse( dbType + ": List of Sequences", Utils.isEmpty( db.getSQLListOfSequences() ) );
            assertFalse( dbType + ": Sequence Exists", Utils.isEmpty( db.getSQLSequenceExists( "testSeq" ) ) );
            assertFalse( dbType + ": Current Value", Utils.isEmpty( db.getSQLCurrentSequenceValue( "testSeq" ) ) );
            assertFalse( dbType + ": Next Value", Utils.isEmpty( db.getSQLNextSequenceValue( "testSeq" ) ) );
        } else {
            assertFalse( db.getClass().getSimpleName(), db.supportsSequences() );
            assertTrue( dbType + ": List of Sequences", Utils.isEmpty( db.getSQLListOfSequences() ) );
            assertTrue( dbType + ": Sequence Exists", Utils.isEmpty( db.getSQLSequenceExists( "testSeq" ) ) );
            assertTrue( dbType + ": Current Value", Utils.isEmpty( db.getSQLCurrentSequenceValue( "testSeq" ) ) );
            assertTrue( dbType + ": Next Value", Utils.isEmpty( db.getSQLNextSequenceValue( "testSeq" ) ) );
        }
    }
}
