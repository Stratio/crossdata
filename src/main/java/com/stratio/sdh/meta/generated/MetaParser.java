// $ANTLR 3.5.1 Meta.g 2014-02-08 00:44:01

    package com.stratio.sdh.meta.generated;    
    import com.stratio.sdh.meta.statements.Statement;
    import com.stratio.sdh.meta.statements.AlterKeyspaceStatement;
    import com.stratio.sdh.meta.statements.CreateKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropTableStatement;
    import com.stratio.sdh.meta.statements.ExplainPlanStatement;
    import com.stratio.sdh.meta.statements.InsertIntoStatement;
    import com.stratio.sdh.meta.statements.SelectStatement;
    import com.stratio.sdh.meta.statements.SetOptionsStatement;
    import com.stratio.sdh.meta.statements.StopProcessStatement;
    import com.stratio.sdh.meta.statements.DropTriggerStatement;
    import com.stratio.sdh.meta.statements.CreateTriggerStatement;
    import com.stratio.sdh.meta.statements.TruncateStatement;
    import com.stratio.sdh.meta.statements.UseStatement;
    import com.stratio.sdh.meta.structures.CollectionLiteral;
    import com.stratio.sdh.meta.structures.Consistency;
    import com.stratio.sdh.meta.structures.ConstantProperty;
    import com.stratio.sdh.meta.structures.IdentifierProperty;
    import com.stratio.sdh.meta.structures.MapLiteralProperty;
    import com.stratio.sdh.meta.structures.Option;
    import com.stratio.sdh.meta.structures.Term;
    import com.stratio.sdh.meta.structures.ValueCell;
    import com.stratio.sdh.meta.structures.ValueProperty;
    import java.util.HashMap;
    import java.util.Map;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "DIGIT", "E", 
		"F", "G", "H", "I", "J", "K", "L", "LETTER", "M", "N", "O", "P", "Q", 
		"R", "S", "T", "T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", "T_ANY", "T_CLUSTERING", 
		"T_COLON", "T_COMMA", "T_COMPACT", "T_CONSISTENCY", "T_CONSTANT", "T_CREATE", 
		"T_DROP", "T_EACH_QUORUM", "T_END_BRACKET", "T_END_SBRACKET", "T_EQUAL", 
		"T_EXISTS", "T_EXPLAIN", "T_FALSE", "T_FOR", "T_IDENT", "T_IF", "T_INSERT", 
		"T_INTO", "T_KEYSPACE", "T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_NOT", "T_ON", 
		"T_ONE", "T_OPTIONS", "T_ORDER", "T_PLAN", "T_POINT", "T_PROCESS", "T_QUORUM", 
		"T_SELECT", "T_SEMICOLON", "T_SET", "T_START_BRACKET", "T_START_SBRACKET", 
		"T_STOP", "T_STORAGE", "T_TABLE", "T_TERM", "T_THREE", "T_TRIGGER", "T_TRUE", 
		"T_TRUNCATE", "T_TWO", "T_USE", "T_USING", "T_VALUES", "T_WITH", "U", 
		"V", "W", "WS", "X", "Y", "Z"
	};
	public static final int EOF=-1;
	public static final int A=4;
	public static final int B=5;
	public static final int C=6;
	public static final int D=7;
	public static final int DIGIT=8;
	public static final int E=9;
	public static final int F=10;
	public static final int G=11;
	public static final int H=12;
	public static final int I=13;
	public static final int J=14;
	public static final int K=15;
	public static final int L=16;
	public static final int LETTER=17;
	public static final int M=18;
	public static final int N=19;
	public static final int O=20;
	public static final int P=21;
	public static final int Q=22;
	public static final int R=23;
	public static final int S=24;
	public static final int T=25;
	public static final int T_ALL=26;
	public static final int T_ALTER=27;
	public static final int T_ANALYTICS=28;
	public static final int T_AND=29;
	public static final int T_ANY=30;
	public static final int T_CLUSTERING=31;
	public static final int T_COLON=32;
	public static final int T_COMMA=33;
	public static final int T_COMPACT=34;
	public static final int T_CONSISTENCY=35;
	public static final int T_CONSTANT=36;
	public static final int T_CREATE=37;
	public static final int T_DROP=38;
	public static final int T_EACH_QUORUM=39;
	public static final int T_END_BRACKET=40;
	public static final int T_END_SBRACKET=41;
	public static final int T_EQUAL=42;
	public static final int T_EXISTS=43;
	public static final int T_EXPLAIN=44;
	public static final int T_FALSE=45;
	public static final int T_FOR=46;
	public static final int T_IDENT=47;
	public static final int T_IF=48;
	public static final int T_INSERT=49;
	public static final int T_INTO=50;
	public static final int T_KEYSPACE=51;
	public static final int T_LOCAL_ONE=52;
	public static final int T_LOCAL_QUORUM=53;
	public static final int T_NOT=54;
	public static final int T_ON=55;
	public static final int T_ONE=56;
	public static final int T_OPTIONS=57;
	public static final int T_ORDER=58;
	public static final int T_PLAN=59;
	public static final int T_POINT=60;
	public static final int T_PROCESS=61;
	public static final int T_QUORUM=62;
	public static final int T_SELECT=63;
	public static final int T_SEMICOLON=64;
	public static final int T_SET=65;
	public static final int T_START_BRACKET=66;
	public static final int T_START_SBRACKET=67;
	public static final int T_STOP=68;
	public static final int T_STORAGE=69;
	public static final int T_TABLE=70;
	public static final int T_TERM=71;
	public static final int T_THREE=72;
	public static final int T_TRIGGER=73;
	public static final int T_TRUE=74;
	public static final int T_TRUNCATE=75;
	public static final int T_TWO=76;
	public static final int T_USE=77;
	public static final int T_USING=78;
	public static final int T_VALUES=79;
	public static final int T_WITH=80;
	public static final int U=81;
	public static final int V=82;
	public static final int W=83;
	public static final int WS=84;
	public static final int X=85;
	public static final int Y=86;
	public static final int Z=87;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public MetaParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public MetaParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return MetaParser.tokenNames; }
	@Override public String getGrammarFileName() { return "Meta.g"; }


	    public void displayRecognitionError(String[] tokenNames, RecognitionException e){
	        System.err.print("Error recognized: ");
	        String hdr = getErrorHeader(e);
	        String msg = getErrorMessage(e, tokenNames);
	        System.err.print(hdr+": ");
	        System.err.println(msg);
	    }



	// $ANTLR start "stopProcessStatement"
	// Meta.g:174:1: stopProcessStatement returns [StopProcessStatement stprst] : T_STOP T_PROCESS ident= T_IDENT ;
	public final StopProcessStatement stopProcessStatement() throws RecognitionException {
		StopProcessStatement stprst = null;


		Token ident=null;

		try {
			// Meta.g:174:59: ( T_STOP T_PROCESS ident= T_IDENT )
			// Meta.g:175:5: T_STOP T_PROCESS ident= T_IDENT
			{
			match(input,T_STOP,FOLLOW_T_STOP_in_stopProcessStatement1182); 
			match(input,T_PROCESS,FOLLOW_T_PROCESS_in_stopProcessStatement1184); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_stopProcessStatement1188); 
			 stprst = new StopProcessStatement((ident!=null?ident.getText():null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return stprst;
	}
	// $ANTLR end "stopProcessStatement"



	// $ANTLR start "dropTriggerStatement"
	// Meta.g:179:1: dropTriggerStatement returns [DropTriggerStatement drtrst] : T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT ;
	public final DropTriggerStatement dropTriggerStatement() throws RecognitionException {
		DropTriggerStatement drtrst = null;


		Token ident=null;
		Token ident2=null;

		try {
			// Meta.g:179:59: ( T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT )
			// Meta.g:180:5: T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTriggerStatement1211); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropTriggerStatement1218); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement1222); 
			match(input,T_ON,FOLLOW_T_ON_in_dropTriggerStatement1229); 
			ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement1238); 
			drtrst = new DropTriggerStatement((ident!=null?ident.getText():null),(ident2!=null?ident2.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return drtrst;
	}
	// $ANTLR end "dropTriggerStatement"



	// $ANTLR start "createTriggerStatement"
	// Meta.g:187:1: createTriggerStatement returns [CreateTriggerStatement crtrst] : T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT ;
	public final CreateTriggerStatement createTriggerStatement() throws RecognitionException {
		CreateTriggerStatement crtrst = null;


		Token trigger_name=null;
		Token table_name=null;
		Token class_name=null;

		try {
			// Meta.g:187:63: ( T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT )
			// Meta.g:188:5: T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTriggerStatement1266); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_createTriggerStatement1273); 
			trigger_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement1277); 
			match(input,T_ON,FOLLOW_T_ON_in_createTriggerStatement1284); 
			table_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement1293); 
			match(input,T_USING,FOLLOW_T_USING_in_createTriggerStatement1299); 
			class_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement1303); 
			crtrst = new CreateTriggerStatement((trigger_name!=null?trigger_name.getText():null),(table_name!=null?table_name.getText():null),(class_name!=null?class_name.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return crtrst;
	}
	// $ANTLR end "createTriggerStatement"



	// $ANTLR start "selectStatement"
	// Meta.g:196:1: selectStatement returns [SelectStatement slctst] : T_SELECT ;
	public final SelectStatement selectStatement() throws RecognitionException {
		SelectStatement slctst = null;


		try {
			// Meta.g:196:49: ( T_SELECT )
			// Meta.g:197:5: T_SELECT
			{
			match(input,T_SELECT,FOLLOW_T_SELECT_in_selectStatement1333); 
			slctst = new SelectStatement();
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return slctst;
	}
	// $ANTLR end "selectStatement"



	// $ANTLR start "insertIntoStatement"
	// Meta.g:200:1: insertIntoStatement returns [InsertIntoStatement nsntst] : T_INSERT T_INTO tableName= getTableID T_START_BRACKET ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_BRACKET (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? ;
	public final InsertIntoStatement insertIntoStatement() throws RecognitionException {
		InsertIntoStatement nsntst = null;


		Token ident1=null;
		Token identN=null;
		String tableName =null;
		SelectStatement selectStmnt =null;
		ValueCell term1 =null;
		ValueCell termN =null;
		Option opt1 =null;
		Option optN =null;


		        List<String> ids = new ArrayList<>();
		        boolean ifNotExists = false;
		        int typeValues = InsertIntoStatement.TYPE_VALUES_CLAUSE;
		        List<ValueCell> cellValues = new ArrayList<>();
		        boolean optsInc = false;
		        List<Option> options = new ArrayList<>();
		    
		try {
			// Meta.g:208:6: ( T_INSERT T_INTO tableName= getTableID T_START_BRACKET ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_BRACKET (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? )
			// Meta.g:209:5: T_INSERT T_INTO tableName= getTableID T_START_BRACKET ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_BRACKET (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			{
			match(input,T_INSERT,FOLLOW_T_INSERT_in_insertIntoStatement1363); 
			match(input,T_INTO,FOLLOW_T_INTO_in_insertIntoStatement1370); 
			pushFollow(FOLLOW_getTableID_in_insertIntoStatement1379);
			tableName=getTableID();
			state._fsp--;

			match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_insertIntoStatement1385); 
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement1394); 
			ids.add((ident1!=null?ident1.getText():null));
			// Meta.g:214:5: ( T_COMMA identN= T_IDENT )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==T_COMMA) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// Meta.g:214:6: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement1404); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement1408); 
					ids.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop1;
				}
			}

			match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_insertIntoStatement1419); 
			// Meta.g:216:5: (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET )
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==T_SELECT) ) {
				alt3=1;
			}
			else if ( (LA3_0==T_VALUES) ) {
				alt3=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}

			switch (alt3) {
				case 1 :
					// Meta.g:217:9: selectStmnt= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_insertIntoStatement1438);
					selectStmnt=selectStatement();
					state._fsp--;

					typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;
					}
					break;
				case 2 :
					// Meta.g:219:9: T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET
					{
					match(input,T_VALUES,FOLLOW_T_VALUES_in_insertIntoStatement1461); 
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_insertIntoStatement1471); 
					pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement1488);
					term1=getTermOrLiteral();
					state._fsp--;

					cellValues.add(term1);
					// Meta.g:222:13: ( T_COMMA termN= getTermOrLiteral )*
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( (LA2_0==T_COMMA) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// Meta.g:222:14: T_COMMA termN= getTermOrLiteral
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement1505); 
							pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement1509);
							termN=getTermOrLiteral();
							state._fsp--;

							cellValues.add(termN);
							}
							break;

						default :
							break loop2;
						}
					}

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_insertIntoStatement1523); 
					}
					break;

			}

			// Meta.g:225:5: ( T_IF T_NOT T_EXISTS )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==T_IF) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// Meta.g:225:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_insertIntoStatement1536); 
					match(input,T_NOT,FOLLOW_T_NOT_in_insertIntoStatement1538); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_insertIntoStatement1540); 
					ifNotExists=true;
					}
					break;

			}

			// Meta.g:226:5: ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==T_USING) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Meta.g:227:9: T_USING opt1= getOption ( T_AND optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_insertIntoStatement1561); 
					optsInc=true;
					pushFollow(FOLLOW_getOption_in_insertIntoStatement1576);
					opt1=getOption();
					state._fsp--;


					            options.add(opt1);
					        
					// Meta.g:231:9: ( T_AND optN= getOption )*
					loop5:
					while (true) {
						int alt5=2;
						int LA5_0 = input.LA(1);
						if ( (LA5_0==T_AND) ) {
							alt5=1;
						}

						switch (alt5) {
						case 1 :
							// Meta.g:231:10: T_AND optN= getOption
							{
							match(input,T_AND,FOLLOW_T_AND_in_insertIntoStatement1589); 
							pushFollow(FOLLOW_getOption_in_insertIntoStatement1593);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop5;
						}
					}

					}
					break;

			}


			        if(typeValues==InsertIntoStatement.TYPE_SELECT_CLAUSE)
			            if(optsInc)
			                nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists, options);
			            else
			                nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists);
			        else
			            if(optsInc)
			                nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists, options);
			            else
			                nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists);
			                
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return nsntst;
	}
	// $ANTLR end "insertIntoStatement"



	// $ANTLR start "explainPlanStatement"
	// Meta.g:249:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		Statement parsedStmnt =null;

		try {
			// Meta.g:249:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:250:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement1631); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement1633); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement1635); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement1639);
			parsedStmnt=metaStatement();
			state._fsp--;

			xpplst = new ExplainPlanStatement(parsedStmnt);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return xpplst;
	}
	// $ANTLR end "explainPlanStatement"



	// $ANTLR start "setOptionsStatement"
	// Meta.g:254:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:261:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:262:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement1673); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement1675); 
			// Meta.g:262:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==T_ANALYTICS) ) {
				alt13=1;
			}
			else if ( (LA13_0==T_CONSISTENCY) ) {
				alt13=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
					// Meta.g:263:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1687); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1689); 
					// Meta.g:263:29: ( T_TRUE | T_FALSE )
					int alt7=2;
					int LA7_0 = input.LA(1);
					if ( (LA7_0==T_TRUE) ) {
						alt7=1;
					}
					else if ( (LA7_0==T_FALSE) ) {
						alt7=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 7, 0, input);
						throw nvae;
					}

					switch (alt7) {
						case 1 :
							// Meta.g:263:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1692); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:263:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1695); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:264:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( (LA9_0==T_AND) ) {
						alt9=1;
					}
					switch (alt9) {
						case 1 :
							// Meta.g:264:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1710); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1712); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1714); 
							// Meta.g:265:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt8=9;
							switch ( input.LA(1) ) {
							case T_ALL:
								{
								alt8=1;
								}
								break;
							case T_ANY:
								{
								alt8=2;
								}
								break;
							case T_QUORUM:
								{
								alt8=3;
								}
								break;
							case T_ONE:
								{
								alt8=4;
								}
								break;
							case T_TWO:
								{
								alt8=5;
								}
								break;
							case T_THREE:
								{
								alt8=6;
								}
								break;
							case T_EACH_QUORUM:
								{
								alt8=7;
								}
								break;
							case T_LOCAL_ONE:
								{
								alt8=8;
								}
								break;
							case T_LOCAL_QUORUM:
								{
								alt8=9;
								}
								break;
							default:
								NoViableAltException nvae =
									new NoViableAltException("", 8, 0, input);
								throw nvae;
							}
							switch (alt8) {
								case 1 :
									// Meta.g:265:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1729); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:266:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1748); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:267:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1766); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:268:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1784); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:269:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1802); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:270:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1820); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:271:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1838); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:272:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1856); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:273:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1874); 
									cnstc=Consistency.LOCAL_QUORUM;
									}
									break;

							}

							checks.set(1, true);
							}
							break;

					}

					 stptst = new SetOptionsStatement(analytics, cnstc, checks);
					}
					break;
				case 2 :
					// Meta.g:277:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1924); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1926); 
					// Meta.g:278:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt10=9;
					switch ( input.LA(1) ) {
					case T_ALL:
						{
						alt10=1;
						}
						break;
					case T_ANY:
						{
						alt10=2;
						}
						break;
					case T_QUORUM:
						{
						alt10=3;
						}
						break;
					case T_ONE:
						{
						alt10=4;
						}
						break;
					case T_TWO:
						{
						alt10=5;
						}
						break;
					case T_THREE:
						{
						alt10=6;
						}
						break;
					case T_EACH_QUORUM:
						{
						alt10=7;
						}
						break;
					case T_LOCAL_ONE:
						{
						alt10=8;
						}
						break;
					case T_LOCAL_QUORUM:
						{
						alt10=9;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 10, 0, input);
						throw nvae;
					}
					switch (alt10) {
						case 1 :
							// Meta.g:278:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1942); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:279:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1961); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:280:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1979); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:281:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1997); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:282:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement2015); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:283:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement2033); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:284:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement2051); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:285:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement2069); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:286:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement2087); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:288:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==T_AND) ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// Meta.g:288:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement2115); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement2117); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement2119); 
							// Meta.g:288:36: ( T_TRUE | T_FALSE )
							int alt11=2;
							int LA11_0 = input.LA(1);
							if ( (LA11_0==T_TRUE) ) {
								alt11=1;
							}
							else if ( (LA11_0==T_FALSE) ) {
								alt11=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 11, 0, input);
								throw nvae;
							}

							switch (alt11) {
								case 1 :
									// Meta.g:288:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement2122); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:288:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement2125); 
									analytics=false;
									}
									break;

							}

							checks.set(0, true);
							}
							break;

					}

					 stptst = new SetOptionsStatement(analytics, cnstc, checks);
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return stptst;
	}
	// $ANTLR end "setOptionsStatement"



	// $ANTLR start "useStatement"
	// Meta.g:294:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:294:41: ( T_USE iden= T_IDENT )
			// Meta.g:295:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement2180); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement2188); 
			usst = new UseStatement((iden!=null?iden.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return usst;
	}
	// $ANTLR end "useStatement"



	// $ANTLR start "dropKeyspaceStatement"
	// Meta.g:299:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:302:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:303:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement2218); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement2224); 
			// Meta.g:305:5: ( T_IF T_EXISTS )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==T_IF) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// Meta.g:305:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement2231); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement2233); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement2245); 
			 drksst = new DropKeyspaceStatement((iden!=null?iden.getText():null), ifExists);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return drksst;
	}
	// $ANTLR end "dropKeyspaceStatement"



	// $ANTLR start "alterKeyspaceStatement"
	// Meta.g:310:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:313:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:314:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement2279); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement2285); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2293); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement2299); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2307); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement2309); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement2313);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:319:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( (LA15_0==T_AND) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// Meta.g:319:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement2322); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2326); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement2328); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement2332);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop15;
				}
			}

			 alksst = new AlterKeyspaceStatement((ident!=null?ident.getText():null), properties); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return alksst;
	}
	// $ANTLR end "alterKeyspaceStatement"



	// $ANTLR start "createKeyspaceStatement"
	// Meta.g:323:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final CreateKeyspaceStatement createKeyspaceStatement() throws RecognitionException {
		CreateKeyspaceStatement crksst = null;


		Token identKS=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        boolean ifNotExists = false;
		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:327:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:328:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement2371); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement2377); 
			// Meta.g:330:5: ( T_IF T_NOT T_EXISTS )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IF) ) {
				alt16=1;
			}
			switch (alt16) {
				case 1 :
					// Meta.g:330:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement2384); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement2386); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement2388); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2400); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement2406); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2418); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement2420); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement2424);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:334:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( (LA17_0==T_AND) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// Meta.g:334:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement2433); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2437); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement2439); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement2443);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop17;
				}
			}

			 crksst = new CreateKeyspaceStatement((identKS!=null?identKS.getText():null), ifNotExists, properties); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return crksst;
	}
	// $ANTLR end "createKeyspaceStatement"



	// $ANTLR start "dropTableStatement"
	// Meta.g:338:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String ident =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:341:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID )
			// Meta.g:342:5: T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement2482); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement2488); 
			// Meta.g:344:5: ( T_IF T_EXISTS )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_IF) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// Meta.g:344:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement2495); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement2497); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement2509);
			ident=getTableID();
			state._fsp--;


			        drtbst = new DropTableStatement(ident, ifExists);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return drtbst;
	}
	// $ANTLR end "dropTableStatement"



	// $ANTLR start "truncateStatement"
	// Meta.g:350:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:350:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:351:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement2529); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement2542);
			ident=getTableID();
			state._fsp--;


			            trst = new TruncateStatement(ident);
				
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return trst;
	}
	// $ANTLR end "truncateStatement"



	// $ANTLR start "metaStatement"
	// Meta.g:357:1: metaStatement returns [Statement st] : (st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement );
	public final Statement metaStatement() throws RecognitionException {
		Statement st = null;


		CreateTriggerStatement st_crtr =null;
		DropTriggerStatement st_drtr =null;
		StopProcessStatement st_stpr =null;
		SelectStatement st_slct =null;
		InsertIntoStatement st_nsnt =null;
		ExplainPlanStatement st_xppl =null;
		SetOptionsStatement st_stpt =null;
		UseStatement st_usks =null;
		DropKeyspaceStatement st_drks =null;
		CreateKeyspaceStatement st_crks =null;
		AlterKeyspaceStatement st_alks =null;
		DropTableStatement st_tbdr =null;
		TruncateStatement st_trst =null;

		try {
			// Meta.g:357:37: (st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement )
			int alt19=12;
			switch ( input.LA(1) ) {
			case T_CREATE:
				{
				int LA19_1 = input.LA(2);
				if ( (LA19_1==T_TRIGGER) ) {
					alt19=1;
				}
				else if ( (LA19_1==T_KEYSPACE) ) {
					alt19=9;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 19, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case T_DROP:
				{
				switch ( input.LA(2) ) {
				case T_TRIGGER:
					{
					alt19=2;
					}
					break;
				case T_KEYSPACE:
					{
					alt19=8;
					}
					break;
				case T_TABLE:
					{
					alt19=11;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 19, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_STOP:
				{
				alt19=3;
				}
				break;
			case T_INSERT:
				{
				alt19=4;
				}
				break;
			case T_EXPLAIN:
				{
				alt19=5;
				}
				break;
			case T_SET:
				{
				alt19=6;
				}
				break;
			case T_USE:
				{
				alt19=7;
				}
				break;
			case T_ALTER:
				{
				alt19=10;
				}
				break;
			case T_TRUNCATE:
				{
				alt19=12;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 19, 0, input);
				throw nvae;
			}
			switch (alt19) {
				case 1 :
					// Meta.g:360:5: st_crtr= createTriggerStatement
					{
					pushFollow(FOLLOW_createTriggerStatement_in_metaStatement2566);
					st_crtr=createTriggerStatement();
					state._fsp--;

					 st = st_crtr; 
					}
					break;
				case 2 :
					// Meta.g:361:6: st_drtr= dropTriggerStatement
					{
					pushFollow(FOLLOW_dropTriggerStatement_in_metaStatement2578);
					st_drtr=dropTriggerStatement();
					state._fsp--;

					 st = st_drtr; 
					}
					break;
				case 3 :
					// Meta.g:362:6: st_stpr= stopProcessStatement st_slct= selectStatement
					{
					pushFollow(FOLLOW_stopProcessStatement_in_metaStatement2591);
					st_stpr=stopProcessStatement();
					state._fsp--;

					 st = st_stpr; 
					pushFollow(FOLLOW_selectStatement_in_metaStatement2604);
					st_slct=selectStatement();
					state._fsp--;

					 st = st_slct;
					}
					break;
				case 4 :
					// Meta.g:365:7: st_nsnt= insertIntoStatement
					{
					pushFollow(FOLLOW_insertIntoStatement_in_metaStatement2618);
					st_nsnt=insertIntoStatement();
					state._fsp--;

					 st = st_nsnt;
					}
					break;
				case 5 :
					// Meta.g:367:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement2633);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 6 :
					// Meta.g:368:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement2647);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 7 :
					// Meta.g:369:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement2661);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 8 :
					// Meta.g:370:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement2675);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 9 :
					// Meta.g:371:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement2689);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 10 :
					// Meta.g:372:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement2703);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 11 :
					// Meta.g:373:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement2717);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 12 :
					// Meta.g:374:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement2731);
					st_trst=truncateStatement();
					state._fsp--;

					 st = st_trst; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return st;
	}
	// $ANTLR end "metaStatement"



	// $ANTLR start "query"
	// Meta.g:376:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final Statement query() throws RecognitionException {
		Statement st = null;


		Statement mtst =null;

		try {
			// Meta.g:376:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:377:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query2752);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:377:21: ( T_SEMICOLON )+
			int cnt20=0;
			loop20:
			while (true) {
				int alt20=2;
				int LA20_0 = input.LA(1);
				if ( (LA20_0==T_SEMICOLON) ) {
					alt20=1;
				}

				switch (alt20) {
				case 1 :
					// Meta.g:377:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query2755); 
					}
					break;

				default :
					if ( cnt20 >= 1 ) break loop20;
					EarlyExitException eee = new EarlyExitException(20, input);
					throw eee;
				}
				cnt20++;
			}

			match(input,EOF,FOLLOW_EOF_in_query2759); 

					st = mtst;
				
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return st;
	}
	// $ANTLR end "query"



	// $ANTLR start "getOption"
	// Meta.g:384:1: getOption returns [Option opt] : ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty );
	public final Option getOption() throws RecognitionException {
		Option opt = null;


		Token identProp=null;
		ValueProperty valueProp =null;

		try {
			// Meta.g:384:31: ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty )
			int alt21=3;
			switch ( input.LA(1) ) {
			case T_COMPACT:
				{
				alt21=1;
				}
				break;
			case T_CLUSTERING:
				{
				alt21=2;
				}
				break;
			case T_IDENT:
				{
				alt21=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 21, 0, input);
				throw nvae;
			}
			switch (alt21) {
				case 1 :
					// Meta.g:385:5: T_COMPACT T_STORAGE
					{
					match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getOption2779); 
					match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getOption2781); 
					opt =new Option(Option.OPTION_COMPACT);
					}
					break;
				case 2 :
					// Meta.g:386:7: T_CLUSTERING T_ORDER
					{
					match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getOption2791); 
					match(input,T_ORDER,FOLLOW_T_ORDER_in_getOption2793); 
					opt =new Option(Option.OPTION_CLUSTERING);
					}
					break;
				case 3 :
					// Meta.g:387:7: identProp= T_IDENT T_EQUAL valueProp= getValueProperty
					{
					identProp=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOption2805); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getOption2807); 
					pushFollow(FOLLOW_getValueProperty_in_getOption2811);
					valueProp=getValueProperty();
					state._fsp--;

					opt =new Option((identProp!=null?identProp.getText():null), valueProp);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return opt;
	}
	// $ANTLR end "getOption"



	// $ANTLR start "getList"
	// Meta.g:390:1: getList returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getList() throws RecognitionException {
		List list = null;


		ParserRuleReturnScope term1 =null;
		ParserRuleReturnScope termN =null;


		        list = new ArrayList<String>();
		    
		try {
			// Meta.g:393:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:394:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getList2839);
			term1=getTerm();
			state._fsp--;

			list.add((term1!=null?input.toString(term1.start,term1.stop):null));
			// Meta.g:395:5: ( T_COMMA termN= getTerm )*
			loop22:
			while (true) {
				int alt22=2;
				int LA22_0 = input.LA(1);
				if ( (LA22_0==T_COMMA) ) {
					alt22=1;
				}

				switch (alt22) {
				case 1 :
					// Meta.g:395:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getList2848); 
					pushFollow(FOLLOW_getTerm_in_getList2852);
					termN=getTerm();
					state._fsp--;

					list.add((termN!=null?input.toString(termN.start,termN.stop):null));
					}
					break;

				default :
					break loop22;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return list;
	}
	// $ANTLR end "getList"



	// $ANTLR start "getTermOrLiteral"
	// Meta.g:398:1: getTermOrLiteral returns [ValueCell vc] : (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET );
	public final ValueCell getTermOrLiteral() throws RecognitionException {
		ValueCell vc = null;


		ParserRuleReturnScope term =null;
		ParserRuleReturnScope term1 =null;
		ParserRuleReturnScope termN =null;


		        CollectionLiteral cl = new CollectionLiteral();
		    
		try {
			// Meta.g:401:6: (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==T_IDENT||LA25_0==T_TERM) ) {
				alt25=1;
			}
			else if ( (LA25_0==T_START_SBRACKET) ) {
				alt25=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 25, 0, input);
				throw nvae;
			}

			switch (alt25) {
				case 1 :
					// Meta.g:402:5: term= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getTermOrLiteral2886);
					term=getTerm();
					state._fsp--;

					vc =new Term((term!=null?input.toString(term.start,term.stop):null));
					}
					break;
				case 2 :
					// Meta.g:404:5: T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getTermOrLiteral2900); 
					// Meta.g:405:5: (term1= getTerm ( T_COMMA termN= getTerm )* )?
					int alt24=2;
					int LA24_0 = input.LA(1);
					if ( (LA24_0==T_IDENT||LA24_0==T_TERM) ) {
						alt24=1;
					}
					switch (alt24) {
						case 1 :
							// Meta.g:406:9: term1= getTerm ( T_COMMA termN= getTerm )*
							{
							pushFollow(FOLLOW_getTerm_in_getTermOrLiteral2918);
							term1=getTerm();
							state._fsp--;

							cl.addLiteral(new Term((term1!=null?input.toString(term1.start,term1.stop):null)));
							// Meta.g:407:9: ( T_COMMA termN= getTerm )*
							loop23:
							while (true) {
								int alt23=2;
								int LA23_0 = input.LA(1);
								if ( (LA23_0==T_COMMA) ) {
									alt23=1;
								}

								switch (alt23) {
								case 1 :
									// Meta.g:407:10: T_COMMA termN= getTerm
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getTermOrLiteral2931); 
									pushFollow(FOLLOW_getTerm_in_getTermOrLiteral2935);
									termN=getTerm();
									state._fsp--;

									cl.addLiteral(new Term((termN!=null?input.toString(termN.start,termN.stop):null)));
									}
									break;

								default :
									break loop23;
								}
							}

							}
							break;

					}

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getTermOrLiteral2952); 
					vc =cl;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return vc;
	}
	// $ANTLR end "getTermOrLiteral"



	// $ANTLR start "getTableID"
	// Meta.g:412:1: getTableID returns [String tableID] : (ks= T_IDENT '.' )? ident= T_IDENT ;
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ks=null;
		Token ident=null;

		try {
			// Meta.g:412:36: ( (ks= T_IDENT '.' )? ident= T_IDENT )
			// Meta.g:413:5: (ks= T_IDENT '.' )? ident= T_IDENT
			{
			// Meta.g:413:5: (ks= T_IDENT '.' )?
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0==T_IDENT) ) {
				int LA26_1 = input.LA(2);
				if ( (LA26_1==T_POINT) ) {
					alt26=1;
				}
			}
			switch (alt26) {
				case 1 :
					// Meta.g:413:6: ks= T_IDENT '.'
					{
					ks=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2974); 
					match(input,T_POINT,FOLLOW_T_POINT_in_getTableID2976); 
					}
					break;

			}

			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2987); 
			tableID = new String((ks!=null?ks.getText():null)==null?(ident!=null?ident.getText():null):(ks!=null?ks.getText():null)+'.'+(ident!=null?ident.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return tableID;
	}
	// $ANTLR end "getTableID"


	public static class getTerm_return extends ParserRuleReturnScope {
		public String term;
	};


	// $ANTLR start "getTerm"
	// Meta.g:416:1: getTerm returns [String term] : (ident= T_IDENT |noIdent= T_TERM );
	public final MetaParser.getTerm_return getTerm() throws RecognitionException {
		MetaParser.getTerm_return retval = new MetaParser.getTerm_return();
		retval.start = input.LT(1);

		Token ident=null;
		Token noIdent=null;

		try {
			// Meta.g:416:30: (ident= T_IDENT |noIdent= T_TERM )
			int alt27=2;
			int LA27_0 = input.LA(1);
			if ( (LA27_0==T_IDENT) ) {
				alt27=1;
			}
			else if ( (LA27_0==T_TERM) ) {
				alt27=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 27, 0, input);
				throw nvae;
			}

			switch (alt27) {
				case 1 :
					// Meta.g:417:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm3007); 
					retval.term = (ident!=null?ident.getText():null);
					}
					break;
				case 2 :
					// Meta.g:418:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm3019); 
					retval.term = (noIdent!=null?noIdent.getText():null);
					}
					break;

			}
			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "getTerm"



	// $ANTLR start "getMapLiteral"
	// Meta.g:421:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		ParserRuleReturnScope leftTerm1 =null;
		ParserRuleReturnScope rightTerm1 =null;
		ParserRuleReturnScope leftTermN =null;
		ParserRuleReturnScope rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:424:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:425:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral3049); 
			// Meta.g:426:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0==T_IDENT||LA29_0==T_TERM) ) {
				alt29=1;
			}
			switch (alt29) {
				case 1 :
					// Meta.g:426:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral3059);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral3061); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral3065);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put((leftTerm1!=null?input.toString(leftTerm1.start,leftTerm1.stop):null), (rightTerm1!=null?input.toString(rightTerm1.start,rightTerm1.stop):null));
					// Meta.g:427:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop28:
					while (true) {
						int alt28=2;
						int LA28_0 = input.LA(1);
						if ( (LA28_0==T_COMMA) ) {
							alt28=1;
						}

						switch (alt28) {
						case 1 :
							// Meta.g:427:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral3074); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral3078);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral3080); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral3084);
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put((leftTermN!=null?input.toString(leftTermN.start,leftTermN.stop):null), (rightTermN!=null?input.toString(rightTermN.start,rightTermN.stop):null));
							}
							break;

						default :
							break loop28;
						}
					}

					}
					break;

			}

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral3096); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return mapTerms;
	}
	// $ANTLR end "getMapLiteral"



	// $ANTLR start "getValueProperty"
	// Meta.g:431:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Map<String, String> mapliteral =null;

		try {
			// Meta.g:431:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral )
			int alt30=3;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt30=1;
				}
				break;
			case T_CONSTANT:
				{
				alt30=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt30=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 30, 0, input);
				throw nvae;
			}
			switch (alt30) {
				case 1 :
					// Meta.g:432:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty3118); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:433:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty3130); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:434:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty3142);
					mapliteral=getMapLiteral();
					state._fsp--;

					value = new MapLiteralProperty(mapliteral);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "getValueProperty"

	// Delegated rules



	public static final BitSet FOLLOW_T_STOP_in_stopProcessStatement1182 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_PROCESS_in_stopProcessStatement1184 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_stopProcessStatement1188 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTriggerStatement1211 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropTriggerStatement1218 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1222 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_ON_in_dropTriggerStatement1229 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1238 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTriggerStatement1266 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_TRIGGER_in_createTriggerStatement1273 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1277 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_ON_in_createTriggerStatement1284 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1293 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_USING_in_createTriggerStatement1299 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SELECT_in_selectStatement1333 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INSERT_in_insertIntoStatement1363 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_INTO_in_insertIntoStatement1370 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_insertIntoStatement1379 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_insertIntoStatement1385 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement1394 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement1404 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement1408 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_insertIntoStatement1419 = new BitSet(new long[]{0x8000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_selectStatement_in_insertIntoStatement1438 = new BitSet(new long[]{0x0001000000000002L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_VALUES_in_insertIntoStatement1461 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_insertIntoStatement1471 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000088L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement1488 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement1505 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000088L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement1509 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_insertIntoStatement1523 = new BitSet(new long[]{0x0001000000000002L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_IF_in_insertIntoStatement1536 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_insertIntoStatement1538 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_insertIntoStatement1540 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_USING_in_insertIntoStatement1561 = new BitSet(new long[]{0x0000800480000000L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement1576 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_insertIntoStatement1589 = new BitSet(new long[]{0x0000800480000000L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement1593 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement1631 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement1633 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement1635 = new BitSet(new long[]{0x0002106008000000L,0x0000000000002812L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement1639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement1673 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement1675 = new BitSet(new long[]{0x0000000810000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1687 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1689 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1692 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1695 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1710 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1712 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1714 = new BitSet(new long[]{0x4130008044000000L,0x0000000000001100L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1729 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1748 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1766 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1784 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1802 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1820 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1838 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1856 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1874 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1924 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1926 = new BitSet(new long[]{0x4130008044000000L,0x0000000000001100L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1942 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1961 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1979 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1997 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement2015 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement2033 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement2051 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement2069 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement2087 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement2115 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement2117 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement2119 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement2122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement2125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement2180 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement2188 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement2218 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement2224 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement2231 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement2233 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement2245 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement2279 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement2285 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2293 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement2299 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2307 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement2309 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement2313 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement2322 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2326 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement2328 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement2332 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement2371 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement2377 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement2384 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement2386 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement2388 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2400 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement2406 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2418 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement2420 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement2424 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement2433 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2437 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement2439 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement2443 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement2482 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement2488 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement2495 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement2497 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement2509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement2529 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement2542 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTriggerStatement_in_metaStatement2566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTriggerStatement_in_metaStatement2578 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stopProcessStatement_in_metaStatement2591 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_selectStatement_in_metaStatement2604 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_insertIntoStatement_in_metaStatement2618 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement2633 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement2647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement2661 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement2675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement2689 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement2703 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement2717 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement2731 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query2752 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query2755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_EOF_in_query2759 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COMPACT_in_getOption2779 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_STORAGE_in_getOption2781 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getOption2791 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getOption2793 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOption2805 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getOption2807 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_getOption2811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getList2839 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getList2848 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getTerm_in_getList2852 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral2886 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getTermOrLiteral2900 = new BitSet(new long[]{0x0000820000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral2918 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getTermOrLiteral2931 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral2935 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getTermOrLiteral2952 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2974 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_T_POINT_in_getTableID2976 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2987 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm3007 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm3019 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral3049 = new BitSet(new long[]{0x0000820000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3059 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral3061 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3065 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral3074 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3078 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral3080 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3084 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral3096 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty3118 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty3130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty3142 = new BitSet(new long[]{0x0000000000000002L});
}
