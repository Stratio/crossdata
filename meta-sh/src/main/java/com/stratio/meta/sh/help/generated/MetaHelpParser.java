// $ANTLR 3.5.1 MetaHelp.g 2014-08-18 21:17:46

package com.stratio.meta.sh.help.generated;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;

import com.stratio.meta.sh.help.HelpStatement;
import com.stratio.meta.sh.help.HelpType;

@SuppressWarnings("all")
public class MetaHelpParser extends Parser {
    public static final String[] tokenNames = new String[] {
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "E", "EXPONENT",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "POINT", "Q", "R",
            "S", "T", "T_ADD", "T_ALTER", "T_ANALYTICS", "T_AS", "T_ASC", "T_BETWEEN",
            "T_CLUSTERING", "T_COMPACT", "T_CONSISTENCY", "T_COUNT", "T_CREATE", "T_DATATYPES",
            "T_DEFAULT", "T_DELETE", "T_DESC", "T_DESCRIBE", "T_DISABLE", "T_DISTINCT",
            "T_DROP", "T_EXIT", "T_EXPLAIN", "T_FOR", "T_FROM", "T_HASH", "T_HELP",
            "T_INDEX", "T_INSERT", "T_INTO", "T_KEY", "T_KEYSPACE", "T_KEYSPACES",
            "T_LIKE", "T_LIST", "T_LUCENE", "T_OPTIONS", "T_ORDER", "T_PLAN", "T_PRIMARY",
            "T_PROCESS", "T_QUIT", "T_REMOVE", "T_SELECT", "T_SEMICOLON", "T_SET",
            "T_STOP", "T_STORAGE", "T_TABLE", "T_TABLES", "T_TRIGGER", "T_TRUNCATE",
            "T_TYPE", "T_UDF", "T_UPDATE", "T_USE", "T_USING", "T_VALUES", "U", "V",
            "W", "WS", "X", "Y", "Z"
    };
    public static final int EOF = -1;
    public static final int A = 4;
    public static final int B = 5;
    public static final int C = 6;
    public static final int D = 7;
    public static final int E = 8;
    public static final int EXPONENT = 9;
    public static final int F = 10;
    public static final int G = 11;
    public static final int H = 12;
    public static final int I = 13;
    public static final int J = 14;
    public static final int K = 15;
    public static final int L = 16;
    public static final int M = 17;
    public static final int N = 18;
    public static final int O = 19;
    public static final int P = 20;
    public static final int POINT = 21;
    public static final int Q = 22;
    public static final int R = 23;
    public static final int S = 24;
    public static final int T = 25;
    public static final int T_ADD = 26;
    public static final int T_ALTER = 27;
    public static final int T_ANALYTICS = 28;
    public static final int T_AS = 29;
    public static final int T_ASC = 30;
    public static final int T_BETWEEN = 31;
    public static final int T_CLUSTERING = 32;
    public static final int T_COMPACT = 33;
    public static final int T_CONSISTENCY = 34;
    public static final int T_COUNT = 35;
    public static final int T_CREATE = 36;
    public static final int T_DATATYPES = 37;
    public static final int T_DEFAULT = 38;
    public static final int T_DELETE = 39;
    public static final int T_DESC = 40;
    public static final int T_DESCRIBE = 41;
    public static final int T_DISABLE = 42;
    public static final int T_DISTINCT = 43;
    public static final int T_DROP = 44;
    public static final int T_EXIT = 45;
    public static final int T_EXPLAIN = 46;
    public static final int T_FOR = 47;
    public static final int T_FROM = 48;
    public static final int T_HASH = 49;
    public static final int T_HELP = 50;
    public static final int T_INDEX = 51;
    public static final int T_INSERT = 52;
    public static final int T_INTO = 53;
    public static final int T_KEY = 54;
    public static final int T_KEYSPACE = 55;
    public static final int T_KEYSPACES = 56;
    public static final int T_LIKE = 57;
    public static final int T_LIST = 58;
    public static final int T_LUCENE = 59;
    public static final int T_OPTIONS = 60;
    public static final int T_ORDER = 61;
    public static final int T_PLAN = 62;
    public static final int T_PRIMARY = 63;
    public static final int T_PROCESS = 64;
    public static final int T_QUIT = 65;
    public static final int T_REMOVE = 66;
    public static final int T_SELECT = 67;
    public static final int T_SEMICOLON = 68;
    public static final int T_SET = 69;
    public static final int T_STOP = 70;
    public static final int T_STORAGE = 71;
    public static final int T_TABLE = 72;
    public static final int T_TABLES = 73;
    public static final int T_TRIGGER = 74;
    public static final int T_TRUNCATE = 75;
    public static final int T_TYPE = 76;
    public static final int T_UDF = 77;
    public static final int T_UPDATE = 78;
    public static final int T_USE = 79;
    public static final int T_USING = 80;
    public static final int T_VALUES = 81;
    public static final int U = 82;
    public static final int V = 83;
    public static final int W = 84;
    public static final int WS = 85;
    public static final int X = 86;
    public static final int Y = 87;
    public static final int Z = 88;
    public static final BitSet FOLLOW_T_DESCRIBE_in_describeHelpStatement1257 = new BitSet(
            new long[] { 0x0180000000000002L, 0x0000000000000300L });

    // delegators
    public static final BitSet FOLLOW_T_KEYSPACES_in_describeHelpStatement1266 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_KEYSPACE_in_describeHelpStatement1274 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_TABLES_in_describeHelpStatement1282 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_TABLE_in_describeHelpStatement1290 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_ALTER_in_alterHelpStatement1310 = new BitSet(
            new long[] { 0x0080000000000002L, 0x0000000000000100L });
    public static final BitSet FOLLOW_T_KEYSPACE_in_alterHelpStatement1319 = new BitSet(
            new long[] { 0x0000000000000002L });
    // $ANTLR end "describeHelpStatement"
    public static final BitSet FOLLOW_T_TABLE_in_alterHelpStatement1327 = new BitSet(
            new long[] { 0x0000000000000002L });
    // $ANTLR end "alterHelpStatement"
    public static final BitSet FOLLOW_T_LIST_in_listHelpStatement1347 = new BitSet(
            new long[] { 0x0000000000000002L, 0x0000000000002401L });
    // $ANTLR end "listHelpStatement"
    public static final BitSet FOLLOW_T_PROCESS_in_listHelpStatement1356 = new BitSet(
            new long[] { 0x0000000000000002L });
    // $ANTLR end "dropHelpStatement"
    public static final BitSet FOLLOW_T_UDF_in_listHelpStatement1364 = new BitSet(new long[] { 0x0000000000000002L });
    // $ANTLR end "insertHelpStatement"
    public static final BitSet FOLLOW_T_TRIGGER_in_listHelpStatement1372 = new BitSet(
            new long[] { 0x0000000000000002L });
    // $ANTLR end "createHelpStatement"
    public static final BitSet FOLLOW_T_DROP_in_dropHelpStatement1392 = new BitSet(
            new long[] { 0x0088000000000002L, 0x0000000000000500L });
    // $ANTLR end "helpStatement"
    public static final BitSet FOLLOW_T_KEYSPACE_in_dropHelpStatement1401 = new BitSet(
            new long[] { 0x0000000000000002L });
    // $ANTLR end "query"

    // Delegated rules
    public static final BitSet FOLLOW_T_TABLE_in_dropHelpStatement1409 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_INDEX_in_dropHelpStatement1417 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_TRIGGER_in_dropHelpStatement1425 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_INSERT_in_insertHelpStatement1445 = new BitSet(
            new long[] { 0x0020000000000002L });
    public static final BitSet FOLLOW_T_INTO_in_insertHelpStatement1454 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_CREATE_in_createHelpStatement1474 = new BitSet(
            new long[] { 0x0888004000000002L, 0x0000000000000100L });
    public static final BitSet FOLLOW_T_KEYSPACE_in_createHelpStatement1483 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_TABLE_in_createHelpStatement1491 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1499 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_DEFAULT_in_createHelpStatement1508 = new BitSet(
            new long[] { 0x0008000000000000L });
    public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1510 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_LUCENE_in_createHelpStatement1520 = new BitSet(
            new long[] { 0x0008000000000000L });
    public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1522 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_in_helpStatement1548 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_DATATYPES_in_helpStatement1562 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_createHelpStatement_in_helpStatement1573 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_UPDATE_in_helpStatement1582 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_insertHelpStatement_in_helpStatement1593 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_TRUNCATE_in_helpStatement1602 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_dropHelpStatement_in_helpStatement1613 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_SELECT_in_helpStatement1622 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_ADD_in_helpStatement1631 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_listHelpStatement_in_helpStatement1642 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_REMOVE_in_helpStatement1651 = new BitSet(
            new long[] { 0x0000000000000000L, 0x0000000000002000L });
    public static final BitSet FOLLOW_T_UDF_in_helpStatement1653 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_DELETE_in_helpStatement1662 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_SET_in_helpStatement1671 = new BitSet(new long[] { 0x1000000000000000L });
    public static final BitSet FOLLOW_T_OPTIONS_in_helpStatement1673 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_EXPLAIN_in_helpStatement1682 = new BitSet(new long[] { 0x4000000000000000L });
    public static final BitSet FOLLOW_T_PLAN_in_helpStatement1684 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_alterHelpStatement_in_helpStatement1695 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_STOP_in_helpStatement1704 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_describeHelpStatement_in_helpStatement1718 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_T_HELP_in_query1747 = new BitSet(
            new long[] { 0x041072B00C000000L, 0x000000000000487EL });
    public static final BitSet FOLLOW_helpStatement_in_query1752 = new BitSet(
            new long[] { 0x0000000000000000L, 0x0000000000000010L });
    public static final BitSet FOLLOW_T_SEMICOLON_in_query1759 = new BitSet(
            new long[] { 0x0000000000000000L, 0x0000000000000010L });
    public static final BitSet FOLLOW_EOF_in_query1763 = new BitSet(new long[] { 0x0000000000000002L });
    public MetaHelpParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public MetaHelpParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] { };
    }

    @Override public String[] getTokenNames() {
        return MetaHelpParser.tokenNames;
    }

    @Override public String getGrammarFileName() {
        return "MetaHelp.g";
    }

    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        System.err.print("Error recognized: ");
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        System.err.print(hdr + ": ");
        System.err.println(msg);
    }

    // $ANTLR start "describeHelpStatement"
    // MetaHelp.g:139:1: describeHelpStatement returns [HelpType type] : T_DESCRIBE ( T_KEYSPACES | T_KEYSPACE | T_TABLES | T_TABLE )? ;
    public final HelpType describeHelpStatement() throws RecognitionException {
        HelpType type = null;

        try {
            // MetaHelp.g:139:46: ( T_DESCRIBE ( T_KEYSPACES | T_KEYSPACE | T_TABLES | T_TABLE )? )
            // MetaHelp.g:140:2: T_DESCRIBE ( T_KEYSPACES | T_KEYSPACE | T_TABLES | T_TABLE )?
            {
                match(input, T_DESCRIBE, FOLLOW_T_DESCRIBE_in_describeHelpStatement1257);
                type = HelpType.DESCRIBE;
                // MetaHelp.g:141:2: ( T_KEYSPACES | T_KEYSPACE | T_TABLES | T_TABLE )?
                int alt1 = 5;
                switch (input.LA(1)) {
                case T_KEYSPACES: {
                    alt1 = 1;
                }
                break;
                case T_KEYSPACE: {
                    alt1 = 2;
                }
                break;
                case T_TABLES: {
                    alt1 = 3;
                }
                break;
                case T_TABLE: {
                    alt1 = 4;
                }
                break;
                }
                switch (alt1) {
                case 1:
                    // MetaHelp.g:142:3: T_KEYSPACES
                {
                    match(input, T_KEYSPACES, FOLLOW_T_KEYSPACES_in_describeHelpStatement1266);
                    type = HelpType.DESCRIBE_KEYSPACES;
                }
                break;
                case 2:
                    // MetaHelp.g:143:5: T_KEYSPACE
                {
                    match(input, T_KEYSPACE, FOLLOW_T_KEYSPACE_in_describeHelpStatement1274);
                    type = HelpType.DESCRIBE_KEYSPACE;
                }
                break;
                case 3:
                    // MetaHelp.g:144:5: T_TABLES
                {
                    match(input, T_TABLES, FOLLOW_T_TABLES_in_describeHelpStatement1282);
                    type = HelpType.DESCRIBE_TABLES;
                }
                break;
                case 4:
                    // MetaHelp.g:145:5: T_TABLE
                {
                    match(input, T_TABLE, FOLLOW_T_TABLE_in_describeHelpStatement1290);
                    type = HelpType.DESCRIBE_TABLE;
                }
                break;

                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return type;
    }

    // $ANTLR start "alterHelpStatement"
    // MetaHelp.g:149:1: alterHelpStatement returns [HelpType type] : T_ALTER ( T_KEYSPACE | T_TABLE )? ;
    public final HelpType alterHelpStatement() throws RecognitionException {
        HelpType type = null;

        try {
            // MetaHelp.g:149:43: ( T_ALTER ( T_KEYSPACE | T_TABLE )? )
            // MetaHelp.g:150:2: T_ALTER ( T_KEYSPACE | T_TABLE )?
            {
                match(input, T_ALTER, FOLLOW_T_ALTER_in_alterHelpStatement1310);
                type = HelpType.ALTER;
                // MetaHelp.g:151:2: ( T_KEYSPACE | T_TABLE )?
                int alt2 = 3;
                int LA2_0 = input.LA(1);
                if ((LA2_0 == T_KEYSPACE)) {
                    alt2 = 1;
                } else if ((LA2_0 == T_TABLE)) {
                    alt2 = 2;
                }
                switch (alt2) {
                case 1:
                    // MetaHelp.g:152:3: T_KEYSPACE
                {
                    match(input, T_KEYSPACE, FOLLOW_T_KEYSPACE_in_alterHelpStatement1319);
                    type = HelpType.ALTER_KEYSPACE;
                }
                break;
                case 2:
                    // MetaHelp.g:153:5: T_TABLE
                {
                    match(input, T_TABLE, FOLLOW_T_TABLE_in_alterHelpStatement1327);
                    type = HelpType.ALTER_TABLE;
                }
                break;

                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return type;
    }

    // $ANTLR start "listHelpStatement"
    // MetaHelp.g:157:1: listHelpStatement returns [HelpType type] : T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )? ;
    public final HelpType listHelpStatement() throws RecognitionException {
        HelpType type = null;

        try {
            // MetaHelp.g:157:42: ( T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )? )
            // MetaHelp.g:158:2: T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )?
            {
                match(input, T_LIST, FOLLOW_T_LIST_in_listHelpStatement1347);
                type = HelpType.LIST;
                // MetaHelp.g:159:2: ( T_PROCESS | T_UDF | T_TRIGGER )?
                int alt3 = 4;
                switch (input.LA(1)) {
                case T_PROCESS: {
                    alt3 = 1;
                }
                break;
                case T_UDF: {
                    alt3 = 2;
                }
                break;
                case T_TRIGGER: {
                    alt3 = 3;
                }
                break;
                }
                switch (alt3) {
                case 1:
                    // MetaHelp.g:160:3: T_PROCESS
                {
                    match(input, T_PROCESS, FOLLOW_T_PROCESS_in_listHelpStatement1356);
                    type = HelpType.LIST_PROCESS;
                }
                break;
                case 2:
                    // MetaHelp.g:161:5: T_UDF
                {
                    match(input, T_UDF, FOLLOW_T_UDF_in_listHelpStatement1364);
                    type = HelpType.LIST_UDF;
                }
                break;
                case 3:
                    // MetaHelp.g:162:5: T_TRIGGER
                {
                    match(input, T_TRIGGER, FOLLOW_T_TRIGGER_in_listHelpStatement1372);
                    type = HelpType.LIST_TRIGGER;
                }
                break;

                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return type;
    }

    // $ANTLR start "dropHelpStatement"
    // MetaHelp.g:166:1: dropHelpStatement returns [HelpType type] : T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )? ;
    public final HelpType dropHelpStatement() throws RecognitionException {
        HelpType type = null;

        try {
            // MetaHelp.g:166:42: ( T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )? )
            // MetaHelp.g:167:2: T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )?
            {
                match(input, T_DROP, FOLLOW_T_DROP_in_dropHelpStatement1392);
                type = HelpType.DROP;
                // MetaHelp.g:168:2: ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )?
                int alt4 = 5;
                switch (input.LA(1)) {
                case T_KEYSPACE: {
                    alt4 = 1;
                }
                break;
                case T_TABLE: {
                    alt4 = 2;
                }
                break;
                case T_INDEX: {
                    alt4 = 3;
                }
                break;
                case T_TRIGGER: {
                    alt4 = 4;
                }
                break;
                }
                switch (alt4) {
                case 1:
                    // MetaHelp.g:169:3: T_KEYSPACE
                {
                    match(input, T_KEYSPACE, FOLLOW_T_KEYSPACE_in_dropHelpStatement1401);
                    type = HelpType.DROP_KEYSPACE;
                }
                break;
                case 2:
                    // MetaHelp.g:170:5: T_TABLE
                {
                    match(input, T_TABLE, FOLLOW_T_TABLE_in_dropHelpStatement1409);
                    type = HelpType.DROP_TABLE;
                }
                break;
                case 3:
                    // MetaHelp.g:171:5: T_INDEX
                {
                    match(input, T_INDEX, FOLLOW_T_INDEX_in_dropHelpStatement1417);
                    type = HelpType.DROP_INDEX;
                }
                break;
                case 4:
                    // MetaHelp.g:172:5: T_TRIGGER
                {
                    match(input, T_TRIGGER, FOLLOW_T_TRIGGER_in_dropHelpStatement1425);
                    type = HelpType.DROP_TRIGGER;
                }
                break;

                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return type;
    }

    // $ANTLR start "insertHelpStatement"
    // MetaHelp.g:176:1: insertHelpStatement returns [HelpType type] : T_INSERT ( T_INTO )? ;
    public final HelpType insertHelpStatement() throws RecognitionException {
        HelpType type = null;

        try {
            // MetaHelp.g:176:44: ( T_INSERT ( T_INTO )? )
            // MetaHelp.g:177:2: T_INSERT ( T_INTO )?
            {
                match(input, T_INSERT, FOLLOW_T_INSERT_in_insertHelpStatement1445);
                type = HelpType.INSERT_INTO;
                // MetaHelp.g:178:2: ( T_INTO )?
                int alt5 = 2;
                int LA5_0 = input.LA(1);
                if ((LA5_0 == T_INTO)) {
                    alt5 = 1;
                }
                switch (alt5) {
                case 1:
                    // MetaHelp.g:179:3: T_INTO
                {
                    match(input, T_INTO, FOLLOW_T_INTO_in_insertHelpStatement1454);
                    type = HelpType.INSERT_INTO;
                }
                break;

                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return type;
    }

    // $ANTLR start "createHelpStatement"
    // MetaHelp.g:183:1: createHelpStatement returns [HelpType type] : T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )? ;
    public final HelpType createHelpStatement() throws RecognitionException {
        HelpType type = null;

        try {
            // MetaHelp.g:183:44: ( T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )? )
            // MetaHelp.g:184:2: T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )?
            {
                match(input, T_CREATE, FOLLOW_T_CREATE_in_createHelpStatement1474);
                type = HelpType.CREATE;
                // MetaHelp.g:185:2: ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )?
                int alt6 = 6;
                switch (input.LA(1)) {
                case T_KEYSPACE: {
                    alt6 = 1;
                }
                break;
                case T_TABLE: {
                    alt6 = 2;
                }
                break;
                case T_INDEX: {
                    alt6 = 3;
                }
                break;
                case T_DEFAULT: {
                    alt6 = 4;
                }
                break;
                case T_LUCENE: {
                    alt6 = 5;
                }
                break;
                }
                switch (alt6) {
                case 1:
                    // MetaHelp.g:186:3: T_KEYSPACE
                {
                    match(input, T_KEYSPACE, FOLLOW_T_KEYSPACE_in_createHelpStatement1483);
                    type = HelpType.CREATE_KEYSPACE;
                }
                break;
                case 2:
                    // MetaHelp.g:187:5: T_TABLE
                {
                    match(input, T_TABLE, FOLLOW_T_TABLE_in_createHelpStatement1491);
                    type = HelpType.CREATE_TABLE;
                }
                break;
                case 3:
                    // MetaHelp.g:188:5: T_INDEX
                {
                    match(input, T_INDEX, FOLLOW_T_INDEX_in_createHelpStatement1499);
                    type = HelpType.CREATE_INDEX;
                }
                break;
                case 4:
                    // MetaHelp.g:189:5: ( T_DEFAULT T_INDEX )
                {
                    // MetaHelp.g:189:5: ( T_DEFAULT T_INDEX )
                    // MetaHelp.g:189:6: T_DEFAULT T_INDEX
                    {
                        match(input, T_DEFAULT, FOLLOW_T_DEFAULT_in_createHelpStatement1508);
                        match(input, T_INDEX, FOLLOW_T_INDEX_in_createHelpStatement1510);
                    }

                    type = HelpType.CREATE_INDEX;
                }
                break;
                case 5:
                    // MetaHelp.g:190:5: ( T_LUCENE T_INDEX )
                {
                    // MetaHelp.g:190:5: ( T_LUCENE T_INDEX )
                    // MetaHelp.g:190:6: T_LUCENE T_INDEX
                    {
                        match(input, T_LUCENE, FOLLOW_T_LUCENE_in_createHelpStatement1520);
                        match(input, T_INDEX, FOLLOW_T_INDEX_in_createHelpStatement1522);
                    }

                    type = HelpType.CREATE_LUCENE_INDEX;
                }
                break;

                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving
        }
        return type;
    }

    // $ANTLR start "helpStatement"
    // MetaHelp.g:194:1: helpStatement returns [HelpType type] : ( ( T_QUIT | T_EXIT ) | ( T_DATATYPES ) | (createType= createHelpStatement ) | ( T_UPDATE ) | (insertType= insertHelpStatement ) | ( T_TRUNCATE ) | (dropType= dropHelpStatement ) | ( T_SELECT ) | ( T_ADD ) | (listType= listHelpStatement ) | ( T_REMOVE T_UDF ) | ( T_DELETE ) | ( T_SET T_OPTIONS ) | ( T_EXPLAIN T_PLAN ) | (alterType= alterHelpStatement ) | ( T_STOP ) | (describeType= describeHelpStatement ) );
    public final HelpType helpStatement() throws RecognitionException {
        HelpType type = null;

        HelpType createType = null;
        HelpType insertType = null;
        HelpType dropType = null;
        HelpType listType = null;
        HelpType alterType = null;
        HelpType describeType = null;

        HelpType t = HelpType.CONSOLE_HELP;

        try {
            // MetaHelp.g:197:3: ( ( T_QUIT | T_EXIT ) | ( T_DATATYPES ) | (createType= createHelpStatement ) | ( T_UPDATE ) | (insertType= insertHelpStatement ) | ( T_TRUNCATE ) | (dropType= dropHelpStatement ) | ( T_SELECT ) | ( T_ADD ) | (listType= listHelpStatement ) | ( T_REMOVE T_UDF ) | ( T_DELETE ) | ( T_SET T_OPTIONS ) | ( T_EXPLAIN T_PLAN ) | (alterType= alterHelpStatement ) | ( T_STOP ) | (describeType= describeHelpStatement ) )
            int alt7 = 17;
            switch (input.LA(1)) {
            case T_EXIT:
            case T_QUIT: {
                alt7 = 1;
            }
            break;
            case T_DATATYPES: {
                alt7 = 2;
            }
            break;
            case T_CREATE: {
                alt7 = 3;
            }
            break;
            case T_UPDATE: {
                alt7 = 4;
            }
            break;
            case T_INSERT: {
                alt7 = 5;
            }
            break;
            case T_TRUNCATE: {
                alt7 = 6;
            }
            break;
            case T_DROP: {
                alt7 = 7;
            }
            break;
            case T_SELECT: {
                alt7 = 8;
            }
            break;
            case T_ADD: {
                alt7 = 9;
            }
            break;
            case T_LIST: {
                alt7 = 10;
            }
            break;
            case T_REMOVE: {
                alt7 = 11;
            }
            break;
            case T_DELETE: {
                alt7 = 12;
            }
            break;
            case T_SET: {
                alt7 = 13;
            }
            break;
            case T_EXPLAIN: {
                alt7 = 14;
            }
            break;
            case T_ALTER: {
                alt7 = 15;
            }
            break;
            case T_STOP: {
                alt7 = 16;
            }
            break;
            case T_DESCRIBE: {
                alt7 = 17;
            }
            break;
            default:
                NoViableAltException nvae =
                        new NoViableAltException("", 7, 0, input);
                throw nvae;
            }
            switch (alt7) {
            case 1:
                // MetaHelp.g:198:2: ( T_QUIT | T_EXIT )
            {
                if (input.LA(1) == T_EXIT || input.LA(1) == T_QUIT) {
                    input.consume();
                    state.errorRecovery = false;
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }
                t = HelpType.EXIT;
            }
            break;
            case 2:
                // MetaHelp.g:199:4: ( T_DATATYPES )
            {
                // MetaHelp.g:199:4: ( T_DATATYPES )
                // MetaHelp.g:199:5: T_DATATYPES
                {
                    match(input, T_DATATYPES, FOLLOW_T_DATATYPES_in_helpStatement1562);
                }

                t = HelpType.DATATYPES;
            }
            break;
            case 3:
                // MetaHelp.g:200:4: (createType= createHelpStatement )
            {
                // MetaHelp.g:200:4: (createType= createHelpStatement )
                // MetaHelp.g:200:5: createType= createHelpStatement
                {
                    pushFollow(FOLLOW_createHelpStatement_in_helpStatement1573);
                    createType = createHelpStatement();
                    state._fsp--;

                }

                t = createType;
            }
            break;
            case 4:
                // MetaHelp.g:201:4: ( T_UPDATE )
            {
                // MetaHelp.g:201:4: ( T_UPDATE )
                // MetaHelp.g:201:5: T_UPDATE
                {
                    match(input, T_UPDATE, FOLLOW_T_UPDATE_in_helpStatement1582);
                }

                t = HelpType.UPDATE;
            }
            break;
            case 5:
                // MetaHelp.g:202:4: (insertType= insertHelpStatement )
            {
                // MetaHelp.g:202:4: (insertType= insertHelpStatement )
                // MetaHelp.g:202:5: insertType= insertHelpStatement
                {
                    pushFollow(FOLLOW_insertHelpStatement_in_helpStatement1593);
                    insertType = insertHelpStatement();
                    state._fsp--;

                }

                t = insertType;
            }
            break;
            case 6:
                // MetaHelp.g:203:4: ( T_TRUNCATE )
            {
                // MetaHelp.g:203:4: ( T_TRUNCATE )
                // MetaHelp.g:203:5: T_TRUNCATE
                {
                    match(input, T_TRUNCATE, FOLLOW_T_TRUNCATE_in_helpStatement1602);
                }

                t = HelpType.TRUNCATE;
            }
            break;
            case 7:
                // MetaHelp.g:204:4: (dropType= dropHelpStatement )
            {
                // MetaHelp.g:204:4: (dropType= dropHelpStatement )
                // MetaHelp.g:204:5: dropType= dropHelpStatement
                {
                    pushFollow(FOLLOW_dropHelpStatement_in_helpStatement1613);
                    dropType = dropHelpStatement();
                    state._fsp--;

                }

                t = dropType;
            }
            break;
            case 8:
                // MetaHelp.g:205:4: ( T_SELECT )
            {
                // MetaHelp.g:205:4: ( T_SELECT )
                // MetaHelp.g:205:5: T_SELECT
                {
                    match(input, T_SELECT, FOLLOW_T_SELECT_in_helpStatement1622);
                }

                t = HelpType.SELECT;
            }
            break;
            case 9:
                // MetaHelp.g:206:4: ( T_ADD )
            {
                // MetaHelp.g:206:4: ( T_ADD )
                // MetaHelp.g:206:5: T_ADD
                {
                    match(input, T_ADD, FOLLOW_T_ADD_in_helpStatement1631);
                }

                t = HelpType.ADD;
            }
            break;
            case 10:
                // MetaHelp.g:207:4: (listType= listHelpStatement )
            {
                // MetaHelp.g:207:4: (listType= listHelpStatement )
                // MetaHelp.g:207:5: listType= listHelpStatement
                {
                    pushFollow(FOLLOW_listHelpStatement_in_helpStatement1642);
                    listType = listHelpStatement();
                    state._fsp--;

                }

                t = listType;
            }
            break;
            case 11:
                // MetaHelp.g:208:4: ( T_REMOVE T_UDF )
            {
                // MetaHelp.g:208:4: ( T_REMOVE T_UDF )
                // MetaHelp.g:208:5: T_REMOVE T_UDF
                {
                    match(input, T_REMOVE, FOLLOW_T_REMOVE_in_helpStatement1651);
                    match(input, T_UDF, FOLLOW_T_UDF_in_helpStatement1653);
                }

                t = HelpType.REMOVE_UDF;
            }
            break;
            case 12:
                // MetaHelp.g:209:4: ( T_DELETE )
            {
                // MetaHelp.g:209:4: ( T_DELETE )
                // MetaHelp.g:209:5: T_DELETE
                {
                    match(input, T_DELETE, FOLLOW_T_DELETE_in_helpStatement1662);
                }

                t = HelpType.DELETE;
            }
            break;
            case 13:
                // MetaHelp.g:210:4: ( T_SET T_OPTIONS )
            {
                // MetaHelp.g:210:4: ( T_SET T_OPTIONS )
                // MetaHelp.g:210:5: T_SET T_OPTIONS
                {
                    match(input, T_SET, FOLLOW_T_SET_in_helpStatement1671);
                    match(input, T_OPTIONS, FOLLOW_T_OPTIONS_in_helpStatement1673);
                }

                t = HelpType.SET_OPTIONS;
            }
            break;
            case 14:
                // MetaHelp.g:211:4: ( T_EXPLAIN T_PLAN )
            {
                // MetaHelp.g:211:4: ( T_EXPLAIN T_PLAN )
                // MetaHelp.g:211:5: T_EXPLAIN T_PLAN
                {
                    match(input, T_EXPLAIN, FOLLOW_T_EXPLAIN_in_helpStatement1682);
                    match(input, T_PLAN, FOLLOW_T_PLAN_in_helpStatement1684);
                }

                t = HelpType.EXPLAIN_PLAN;
            }
            break;
            case 15:
                // MetaHelp.g:212:4: (alterType= alterHelpStatement )
            {
                // MetaHelp.g:212:4: (alterType= alterHelpStatement )
                // MetaHelp.g:212:5: alterType= alterHelpStatement
                {
                    pushFollow(FOLLOW_alterHelpStatement_in_helpStatement1695);
                    alterType = alterHelpStatement();
                    state._fsp--;

                }

                t = alterType;
            }
            break;
            case 16:
                // MetaHelp.g:213:4: ( T_STOP )
            {
                // MetaHelp.g:213:4: ( T_STOP )
                // MetaHelp.g:213:5: T_STOP
                {
                    match(input, T_STOP, FOLLOW_T_STOP_in_helpStatement1704);
                }

                t = HelpType.STOP;
            }
            break;
            case 17:
                // MetaHelp.g:214:7: (describeType= describeHelpStatement )
            {
                // MetaHelp.g:214:7: (describeType= describeHelpStatement )
                // MetaHelp.g:214:8: describeType= describeHelpStatement
                {
                    pushFollow(FOLLOW_describeHelpStatement_in_helpStatement1718);
                    describeType = describeHelpStatement();
                    state._fsp--;

                }

                t = describeType;
            }
            break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving

            type = t;

        }
        return type;
    }

    // $ANTLR start "query"
    // MetaHelp.g:220:1: query returns [HelpStatement st] : T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF ;
    public final HelpStatement query() throws RecognitionException {
        HelpStatement st = null;

        HelpType type = null;

        HelpType t = HelpType.CONSOLE_HELP;

        try {
            // MetaHelp.g:223:3: ( T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF )
            // MetaHelp.g:224:2: T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF
            {
                match(input, T_HELP, FOLLOW_T_HELP_in_query1747);
                // MetaHelp.g:224:9: (type= helpStatement )?
                int alt8 = 2;
                int LA8_0 = input.LA(1);
                if (((LA8_0 >= T_ADD && LA8_0 <= T_ALTER) || (LA8_0 >= T_CREATE && LA8_0 <= T_DATATYPES)
                        || LA8_0 == T_DELETE || LA8_0 == T_DESCRIBE || (LA8_0 >= T_DROP && LA8_0 <= T_EXPLAIN)
                        || LA8_0 == T_INSERT || LA8_0 == T_LIST || (LA8_0 >= T_QUIT && LA8_0 <= T_SELECT) || (
                        LA8_0 >= T_SET && LA8_0 <= T_STOP) || LA8_0 == T_TRUNCATE || LA8_0 == T_UPDATE)) {
                    alt8 = 1;
                }
                switch (alt8) {
                case 1:
                    // MetaHelp.g:224:10: type= helpStatement
                {
                    pushFollow(FOLLOW_helpStatement_in_query1752);
                    type = helpStatement();
                    state._fsp--;

                    t = type;
                }
                break;

                }

                // MetaHelp.g:224:41: ( T_SEMICOLON )*
                loop9:
                while (true) {
                    int alt9 = 2;
                    int LA9_0 = input.LA(1);
                    if ((LA9_0 == T_SEMICOLON)) {
                        alt9 = 1;
                    }

                    switch (alt9) {
                    case 1:
                        // MetaHelp.g:224:42: T_SEMICOLON
                    {
                        match(input, T_SEMICOLON, FOLLOW_T_SEMICOLON_in_query1759);
                    }
                    break;

                    default:
                        break loop9;
                    }
                }

                match(input, EOF, FOLLOW_EOF_in_query1763);
            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            // do for sure before leaving

            st = new HelpStatement(t);

        }
        return st;
    }
}
