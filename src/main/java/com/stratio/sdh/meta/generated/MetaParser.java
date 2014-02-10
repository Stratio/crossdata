// $ANTLR 3.5.1 Meta.g 2014-02-10 09:11:48

    package com.stratio.sdh.meta.generated;    
    import com.stratio.sdh.meta.statements.Statement;
    import com.stratio.sdh.meta.statements.AlterKeyspaceStatement;
    import com.stratio.sdh.meta.statements.CreateKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropKeyspaceStatement;
    import com.stratio.sdh.meta.statements.CreateIndexStatement;
    import com.stratio.sdh.meta.statements.DropIndexStatement;
    import com.stratio.sdh.meta.statements.DropTableStatement;
    import com.stratio.sdh.meta.statements.ExplainPlanStatement;
    import com.stratio.sdh.meta.statements.SetOptionsStatement;
    import com.stratio.sdh.meta.statements.TruncateStatement;
    import com.stratio.sdh.meta.statements.UseStatement;
    import com.stratio.sdh.meta.statements.AddStatement;
    import com.stratio.sdh.meta.statements.ListStatement;
    import com.stratio.sdh.meta.statements.RemoveUDFStatement;
    import com.stratio.sdh.meta.structures.Consistency;
    import com.stratio.sdh.meta.structures.ConstantProperty;
    import com.stratio.sdh.meta.structures.IdentifierProperty;
    import com.stratio.sdh.meta.structures.MapLiteralProperty;
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
		"R", "S", "T", "T_ADD", "T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", "T_ANY", 
		"T_COLON", "T_COMMA", "T_CONSISTENCY", "T_CONSTANT", "T_CREATE", "T_DROP", 
		"T_EACH_QUORUM", "T_END_SBRACKET", "T_EQUAL", "T_EXISTS", "T_EXPLAIN", 
		"T_FALSE", "T_FOR", "T_IDENT", "T_IF", "T_INDEX", "T_INDEX_TYPE", "T_KEYSPACE", 
		"T_LEFT_PARENTHESIS", "T_LIST", "T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_NOT", 
		"T_ON", "T_ONE", "T_OPTIONS", "T_PATH", "T_PLAN", "T_POINT", "T_QUORUM", 
		"T_QUOTE", "T_REMOVE", "T_RIGHT_PARENTHESIS", "T_SEMICOLON", "T_SET", 
		"T_START_SBRACKET", "T_TABLE", "T_TERM", "T_THREE", "T_TRUE", "T_TRUNCATE", 
		"T_TWO", "T_UDF", "T_USE", "T_USING", "T_WITH", "U", "V", "W", "WS", "X", 
		"Y", "Z", "'PROCESS'", "'TRIGGER'", "'UDF'"
	};
	public static final int EOF=-1;
	public static final int T__85=85;
	public static final int T__86=86;
	public static final int T__87=87;
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
	public static final int T_ADD=26;
	public static final int T_ALL=27;
	public static final int T_ALTER=28;
	public static final int T_ANALYTICS=29;
	public static final int T_AND=30;
	public static final int T_ANY=31;
	public static final int T_COLON=32;
	public static final int T_COMMA=33;
	public static final int T_CONSISTENCY=34;
	public static final int T_CONSTANT=35;
	public static final int T_CREATE=36;
	public static final int T_DROP=37;
	public static final int T_EACH_QUORUM=38;
	public static final int T_END_SBRACKET=39;
	public static final int T_EQUAL=40;
	public static final int T_EXISTS=41;
	public static final int T_EXPLAIN=42;
	public static final int T_FALSE=43;
	public static final int T_FOR=44;
	public static final int T_IDENT=45;
	public static final int T_IF=46;
	public static final int T_INDEX=47;
	public static final int T_INDEX_TYPE=48;
	public static final int T_KEYSPACE=49;
	public static final int T_LEFT_PARENTHESIS=50;
	public static final int T_LIST=51;
	public static final int T_LOCAL_ONE=52;
	public static final int T_LOCAL_QUORUM=53;
	public static final int T_NOT=54;
	public static final int T_ON=55;
	public static final int T_ONE=56;
	public static final int T_OPTIONS=57;
	public static final int T_PATH=58;
	public static final int T_PLAN=59;
	public static final int T_POINT=60;
	public static final int T_QUORUM=61;
	public static final int T_QUOTE=62;
	public static final int T_REMOVE=63;
	public static final int T_RIGHT_PARENTHESIS=64;
	public static final int T_SEMICOLON=65;
	public static final int T_SET=66;
	public static final int T_START_SBRACKET=67;
	public static final int T_TABLE=68;
	public static final int T_TERM=69;
	public static final int T_THREE=70;
	public static final int T_TRUE=71;
	public static final int T_TRUNCATE=72;
	public static final int T_TWO=73;
	public static final int T_UDF=74;
	public static final int T_USE=75;
	public static final int T_USING=76;
	public static final int T_WITH=77;
	public static final int U=78;
	public static final int V=79;
	public static final int W=80;
	public static final int WS=81;
	public static final int X=82;
	public static final int Y=83;
	public static final int Z=84;

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



	// $ANTLR start "addStatement"
	// Meta.g:163:1: addStatement returns [AddStatement as] : T_ADD T_QUOTE name= T_PATH T_QUOTE ;
	public final AddStatement addStatement() throws RecognitionException {
		AddStatement as = null;


		Token name=null;

		try {
			// Meta.g:163:39: ( T_ADD T_QUOTE name= T_PATH T_QUOTE )
			// Meta.g:164:2: T_ADD T_QUOTE name= T_PATH T_QUOTE
			{
			match(input,T_ADD,FOLLOW_T_ADD_in_addStatement1110); 
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_addStatement1112); 
			name=(Token)match(input,T_PATH,FOLLOW_T_PATH_in_addStatement1116); 
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_addStatement1118); 
			as = new AddStatement((name!=null?name.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return as;
	}
	// $ANTLR end "addStatement"



	// $ANTLR start "listStatement"
	// Meta.g:168:1: listStatement returns [ListStatement ls] : T_LIST (type= getListTypes ) ;
	public final ListStatement listStatement() throws RecognitionException {
		ListStatement ls = null;


		ParserRuleReturnScope type =null;

		try {
			// Meta.g:168:41: ( T_LIST (type= getListTypes ) )
			// Meta.g:169:2: T_LIST (type= getListTypes )
			{
			match(input,T_LIST,FOLLOW_T_LIST_in_listStatement1135); 
			// Meta.g:169:9: (type= getListTypes )
			// Meta.g:169:10: type= getListTypes
			{
			pushFollow(FOLLOW_getListTypes_in_listStatement1140);
			type=getListTypes();
			state._fsp--;

			}

			ls = new ListStatement((type!=null?input.toString(type.start,type.stop):null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return ls;
	}
	// $ANTLR end "listStatement"



	// $ANTLR start "removeUDFStatement"
	// Meta.g:173:1: removeUDFStatement returns [RemoveUDFStatement rus] : T_REMOVE 'UDF' T_QUOTE jar= T_TERM T_QUOTE ;
	public final RemoveUDFStatement removeUDFStatement() throws RecognitionException {
		RemoveUDFStatement rus = null;


		Token jar=null;

		try {
			// Meta.g:173:52: ( T_REMOVE 'UDF' T_QUOTE jar= T_TERM T_QUOTE )
			// Meta.g:174:2: T_REMOVE 'UDF' T_QUOTE jar= T_TERM T_QUOTE
			{
			match(input,T_REMOVE,FOLLOW_T_REMOVE_in_removeUDFStatement1158); 
			match(input,87,FOLLOW_87_in_removeUDFStatement1160); 
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_removeUDFStatement1162); 
			jar=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_removeUDFStatement1166); 
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_removeUDFStatement1168); 
			rus = new RemoveUDFStatement((jar!=null?jar.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return rus;
	}
	// $ANTLR end "removeUDFStatement"



	// $ANTLR start "dropIndexStatement"
	// Meta.g:178:1: dropIndexStatement returns [DropIndexStatement dis] : T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT ;
	public final DropIndexStatement dropIndexStatement() throws RecognitionException {
		DropIndexStatement dis = null;


		Token name=null;


				dis = new DropIndexStatement();
			
		try {
			// Meta.g:181:3: ( T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT )
			// Meta.g:182:2: T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropIndexStatement1190); 
			match(input,T_INDEX,FOLLOW_T_INDEX_in_dropIndexStatement1192); 
			// Meta.g:183:2: ( T_IF T_EXISTS )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==T_IF) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// Meta.g:183:3: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropIndexStatement1196); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropIndexStatement1198); 
					dis.setDropIfExists();
					}
					break;

			}

			name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropIndexStatement1207); 
			dis.setName((name!=null?name.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return dis;
	}
	// $ANTLR end "dropIndexStatement"



	// $ANTLR start "createIndexStatement"
	// Meta.g:191:1: createIndexStatement returns [CreateIndexStatement cis] : T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? name= T_IDENT T_ON tablename= T_IDENT T_LEFT_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_RIGHT_PARENTHESIS ( T_USING usingClass= T_TERM )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? ;
	public final CreateIndexStatement createIndexStatement() throws RecognitionException {
		CreateIndexStatement cis = null;


		Token indexType=null;
		Token name=null;
		Token tablename=null;
		Token firstField=null;
		Token field=null;
		Token usingClass=null;
		Token key=null;
		ValueProperty value =null;


				cis = new CreateIndexStatement();
			
		try {
			// Meta.g:194:3: ( T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? name= T_IDENT T_ON tablename= T_IDENT T_LEFT_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_RIGHT_PARENTHESIS ( T_USING usingClass= T_TERM )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? )
			// Meta.g:195:2: T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? name= T_IDENT T_ON tablename= T_IDENT T_LEFT_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_RIGHT_PARENTHESIS ( T_USING usingClass= T_TERM )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createIndexStatement1232); 
			indexType=(Token)match(input,T_INDEX_TYPE,FOLLOW_T_INDEX_TYPE_in_createIndexStatement1236); 
			cis.setIndexType((indexType!=null?indexType.getText():null));
			match(input,T_INDEX,FOLLOW_T_INDEX_in_createIndexStatement1240); 
			// Meta.g:196:2: ( T_IF T_NOT T_EXISTS )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==T_IF) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// Meta.g:196:3: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createIndexStatement1244); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createIndexStatement1246); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createIndexStatement1248); 
					cis.setCreateIfNotExists();
					}
					break;

			}

			name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement1257); 
			cis.setName((name!=null?name.getText():null));
			match(input,T_ON,FOLLOW_T_ON_in_createIndexStatement1262); 
			tablename=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement1266); 
			cis.setTablename((tablename!=null?tablename.getText():null));
			match(input,T_LEFT_PARENTHESIS,FOLLOW_T_LEFT_PARENTHESIS_in_createIndexStatement1271); 
			firstField=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement1276); 
			cis.addColumn((firstField!=null?firstField.getText():null));
			// Meta.g:201:2: ( T_COMMA field= T_IDENT )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==T_COMMA) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// Meta.g:201:3: T_COMMA field= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createIndexStatement1282); 
					field=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement1288); 
					cis.addColumn((field!=null?field.getText():null));
					}
					break;

				default :
					break loop3;
				}
			}

			match(input,T_RIGHT_PARENTHESIS,FOLLOW_T_RIGHT_PARENTHESIS_in_createIndexStatement1297); 
			// Meta.g:205:2: ( T_USING usingClass= T_TERM )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==T_USING) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// Meta.g:205:3: T_USING usingClass= T_TERM
					{
					match(input,T_USING,FOLLOW_T_USING_in_createIndexStatement1301); 
					usingClass=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_createIndexStatement1305); 
					cis.setUsingClass((usingClass!=null?usingClass.getText():null));
					}
					break;

			}

			// Meta.g:206:2: ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==T_WITH) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Meta.g:206:3: T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createIndexStatement1313); 
					match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_createIndexStatement1315); 
					key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement1319); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement1321); 
					pushFollow(FOLLOW_getValueProperty_in_createIndexStatement1325);
					value=getValueProperty();
					state._fsp--;

					cis.addOption((key!=null?key.getText():null), value);
					// Meta.g:207:3: ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					loop5:
					while (true) {
						int alt5=2;
						int LA5_0 = input.LA(1);
						if ( (LA5_0==T_AND) ) {
							alt5=1;
						}

						switch (alt5) {
						case 1 :
							// Meta.g:207:4: T_AND key= T_IDENT T_EQUAL value= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_createIndexStatement1332); 
							key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement1336); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement1338); 
							pushFollow(FOLLOW_getValueProperty_in_createIndexStatement1342);
							value=getValueProperty();
							state._fsp--;

							cis.addOption((key!=null?key.getText():null), value);
							}
							break;

						default :
							break loop5;
						}
					}

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
		return cis;
	}
	// $ANTLR end "createIndexStatement"



	// $ANTLR start "explainPlanStatement"
	// Meta.g:212:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		Statement parsedStmnt =null;

		try {
			// Meta.g:212:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:213:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement1373); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement1375); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement1377); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement1381);
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
	// Meta.g:217:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:224:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:225:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement1415); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement1417); 
			// Meta.g:225:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
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
					// Meta.g:226:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1429); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1431); 
					// Meta.g:226:29: ( T_TRUE | T_FALSE )
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
							// Meta.g:226:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1434); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:226:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1437); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:227:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( (LA9_0==T_AND) ) {
						alt9=1;
					}
					switch (alt9) {
						case 1 :
							// Meta.g:227:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1452); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1454); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1456); 
							// Meta.g:228:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
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
									// Meta.g:228:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1471); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:229:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1490); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:230:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1508); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:231:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1526); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:232:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1544); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:233:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1562); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:234:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1580); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:235:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1598); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:236:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1616); 
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
					// Meta.g:240:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1666); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1668); 
					// Meta.g:241:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
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
							// Meta.g:241:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1684); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:242:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1703); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:243:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1721); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:244:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1739); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:245:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1757); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:246:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1775); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:247:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1793); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:248:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1811); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:249:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1829); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:251:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==T_AND) ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// Meta.g:251:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1857); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1859); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1861); 
							// Meta.g:251:36: ( T_TRUE | T_FALSE )
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
									// Meta.g:251:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1864); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:251:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1867); 
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
	// Meta.g:257:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:257:41: ( T_USE iden= T_IDENT )
			// Meta.g:258:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement1922); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement1930); 
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
	// Meta.g:262:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:265:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:266:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement1960); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1966); 
			// Meta.g:268:5: ( T_IF T_EXISTS )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==T_IF) ) {
				alt14=1;
			}
			switch (alt14) {
				case 1 :
					// Meta.g:268:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement1973); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement1975); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement1987); 
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
	// Meta.g:273:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:276:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:277:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement2021); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement2027); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2035); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement2041); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2049); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement2051); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement2055);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:282:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( (LA15_0==T_AND) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// Meta.g:282:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement2064); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2068); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement2070); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement2074);
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
	// Meta.g:286:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
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
			// Meta.g:290:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:291:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement2113); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement2119); 
			// Meta.g:293:5: ( T_IF T_NOT T_EXISTS )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IF) ) {
				alt16=1;
			}
			switch (alt16) {
				case 1 :
					// Meta.g:293:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement2126); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement2128); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement2130); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2142); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement2148); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2160); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement2162); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement2166);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:297:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( (LA17_0==T_AND) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// Meta.g:297:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement2175); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2179); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement2181); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement2185);
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
	// Meta.g:301:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String ident =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:304:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID )
			// Meta.g:305:5: T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement2224); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement2230); 
			// Meta.g:307:5: ( T_IF T_EXISTS )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_IF) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// Meta.g:307:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement2237); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement2239); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement2251);
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
	// Meta.g:313:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:313:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:314:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement2271); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement2284);
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
	// Meta.g:320:1: metaStatement returns [Statement st] : (st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement );
	public final Statement metaStatement() throws RecognitionException {
		Statement st = null;


		ExplainPlanStatement st_xppl =null;
		SetOptionsStatement st_stpt =null;
		UseStatement st_usks =null;
		DropKeyspaceStatement st_drks =null;
		CreateKeyspaceStatement st_crks =null;
		AlterKeyspaceStatement st_alks =null;
		DropTableStatement st_tbdr =null;
		TruncateStatement st_trst =null;
		CreateIndexStatement cis =null;
		DropIndexStatement dis =null;
		ListStatement ls =null;
		AddStatement add =null;
		RemoveUDFStatement rs =null;

		try {
			// Meta.g:320:37: (st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement )
			int alt19=13;
			switch ( input.LA(1) ) {
			case T_EXPLAIN:
				{
				alt19=1;
				}
				break;
			case T_SET:
				{
				alt19=2;
				}
				break;
			case T_USE:
				{
				alt19=3;
				}
				break;
			case T_DROP:
				{
				switch ( input.LA(2) ) {
				case T_KEYSPACE:
					{
					alt19=4;
					}
					break;
				case T_TABLE:
					{
					alt19=7;
					}
					break;
				case T_INDEX:
					{
					alt19=10;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 19, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_CREATE:
				{
				int LA19_5 = input.LA(2);
				if ( (LA19_5==T_KEYSPACE) ) {
					alt19=5;
				}
				else if ( (LA19_5==T_INDEX_TYPE) ) {
					alt19=9;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 19, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case T_ALTER:
				{
				alt19=6;
				}
				break;
			case T_TRUNCATE:
				{
				alt19=8;
				}
				break;
			case T_LIST:
				{
				alt19=11;
				}
				break;
			case T_ADD:
				{
				alt19=12;
				}
				break;
			case T_REMOVE:
				{
				alt19=13;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 19, 0, input);
				throw nvae;
			}
			switch (alt19) {
				case 1 :
					// Meta.g:321:5: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement2307);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 2 :
					// Meta.g:322:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement2321);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 3 :
					// Meta.g:323:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement2335);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 4 :
					// Meta.g:324:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement2349);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 5 :
					// Meta.g:325:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement2363);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 6 :
					// Meta.g:326:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement2377);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 7 :
					// Meta.g:327:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement2391);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 8 :
					// Meta.g:328:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement2405);
					st_trst=truncateStatement();
					state._fsp--;

					 st = st_trst; 
					}
					break;
				case 9 :
					// Meta.g:329:7: cis= createIndexStatement
					{
					pushFollow(FOLLOW_createIndexStatement_in_metaStatement2419);
					cis=createIndexStatement();
					state._fsp--;

					 st = cis; 
					}
					break;
				case 10 :
					// Meta.g:330:7: dis= dropIndexStatement
					{
					pushFollow(FOLLOW_dropIndexStatement_in_metaStatement2434);
					dis=dropIndexStatement();
					state._fsp--;

					 st = dis; 
					}
					break;
				case 11 :
					// Meta.g:331:7: ls= listStatement
					{
					pushFollow(FOLLOW_listStatement_in_metaStatement2449);
					ls=listStatement();
					state._fsp--;

					 st = ls; 
					}
					break;
				case 12 :
					// Meta.g:332:7: add= addStatement
					{
					pushFollow(FOLLOW_addStatement_in_metaStatement2464);
					add=addStatement();
					state._fsp--;

					 st = add; 
					}
					break;
				case 13 :
					// Meta.g:333:7: rs= removeUDFStatement
					{
					pushFollow(FOLLOW_removeUDFStatement_in_metaStatement2479);
					rs=removeUDFStatement();
					state._fsp--;

					 st = rs; 
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
	// Meta.g:336:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final Statement query() throws RecognitionException {
		Statement st = null;


		Statement mtst =null;

		try {
			// Meta.g:336:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:337:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query2502);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:337:21: ( T_SEMICOLON )+
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
					// Meta.g:337:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query2505); 
					}
					break;

				default :
					if ( cnt20 >= 1 ) break loop20;
					EarlyExitException eee = new EarlyExitException(20, input);
					throw eee;
				}
				cnt20++;
			}

			match(input,EOF,FOLLOW_EOF_in_query2509); 

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


	public static class getListTypes_return extends ParserRuleReturnScope {
		public String listType;
	};


	// $ANTLR start "getListTypes"
	// Meta.g:344:1: getListTypes returns [String listType] : ident= ( 'PROCESS' | 'UDF' | 'TRIGGER' ) ;
	public final MetaParser.getListTypes_return getListTypes() throws RecognitionException {
		MetaParser.getListTypes_return retval = new MetaParser.getListTypes_return();
		retval.start = input.LT(1);

		Token ident=null;

		try {
			// Meta.g:344:39: (ident= ( 'PROCESS' | 'UDF' | 'TRIGGER' ) )
			// Meta.g:345:2: ident= ( 'PROCESS' | 'UDF' | 'TRIGGER' )
			{
			ident=input.LT(1);
			if ( (input.LA(1) >= 85 && input.LA(1) <= 87) ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			retval.listType = new String((ident!=null?ident.getText():null));
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
	// $ANTLR end "getListTypes"



	// $ANTLR start "getTableID"
	// Meta.g:348:1: getTableID returns [String tableID] : (ks= T_IDENT '.' )? ident= T_IDENT ;
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ks=null;
		Token ident=null;

		try {
			// Meta.g:348:36: ( (ks= T_IDENT '.' )? ident= T_IDENT )
			// Meta.g:349:5: (ks= T_IDENT '.' )? ident= T_IDENT
			{
			// Meta.g:349:5: (ks= T_IDENT '.' )?
			int alt21=2;
			int LA21_0 = input.LA(1);
			if ( (LA21_0==T_IDENT) ) {
				int LA21_1 = input.LA(2);
				if ( (LA21_1==T_POINT) ) {
					alt21=1;
				}
			}
			switch (alt21) {
				case 1 :
					// Meta.g:349:6: ks= T_IDENT '.'
					{
					ks=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2561); 
					match(input,T_POINT,FOLLOW_T_POINT_in_getTableID2563); 
					}
					break;

			}

			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2574); 
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
	// Meta.g:352:1: getTerm returns [String term] : (ident= T_IDENT |noIdent= T_TERM );
	public final MetaParser.getTerm_return getTerm() throws RecognitionException {
		MetaParser.getTerm_return retval = new MetaParser.getTerm_return();
		retval.start = input.LT(1);

		Token ident=null;
		Token noIdent=null;

		try {
			// Meta.g:352:30: (ident= T_IDENT |noIdent= T_TERM )
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==T_IDENT) ) {
				alt22=1;
			}
			else if ( (LA22_0==T_TERM) ) {
				alt22=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 22, 0, input);
				throw nvae;
			}

			switch (alt22) {
				case 1 :
					// Meta.g:353:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm2594); 
					retval.term = (ident!=null?ident.getText():null);
					}
					break;
				case 2 :
					// Meta.g:354:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm2606); 
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
	// Meta.g:357:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		ParserRuleReturnScope leftTerm1 =null;
		ParserRuleReturnScope rightTerm1 =null;
		ParserRuleReturnScope leftTermN =null;
		ParserRuleReturnScope rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:360:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:361:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral2636); 
			// Meta.g:362:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==T_IDENT||LA24_0==T_TERM) ) {
				alt24=1;
			}
			switch (alt24) {
				case 1 :
					// Meta.g:362:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2646);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2648); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2652);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put((leftTerm1!=null?input.toString(leftTerm1.start,leftTerm1.stop):null), (rightTerm1!=null?input.toString(rightTerm1.start,rightTerm1.stop):null));
					// Meta.g:363:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop23:
					while (true) {
						int alt23=2;
						int LA23_0 = input.LA(1);
						if ( (LA23_0==T_COMMA) ) {
							alt23=1;
						}

						switch (alt23) {
						case 1 :
							// Meta.g:363:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral2661); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2665);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2667); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2671);
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put((leftTermN!=null?input.toString(leftTermN.start,leftTermN.stop):null), (rightTermN!=null?input.toString(rightTermN.start,rightTermN.stop):null));
							}
							break;

						default :
							break loop23;
						}
					}

					}
					break;

			}

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral2683); 
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
	// Meta.g:367:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Map<String, String> mapliteral =null;

		try {
			// Meta.g:367:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral )
			int alt25=3;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt25=1;
				}
				break;
			case T_CONSTANT:
				{
				alt25=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt25=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 25, 0, input);
				throw nvae;
			}
			switch (alt25) {
				case 1 :
					// Meta.g:368:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty2705); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:369:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty2717); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:370:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty2729);
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



	public static final BitSet FOLLOW_T_ADD_in_addStatement1110 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_addStatement1112 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_PATH_in_addStatement1116 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_addStatement1118 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIST_in_listStatement1135 = new BitSet(new long[]{0x0000000000000000L,0x0000000000E00000L});
	public static final BitSet FOLLOW_getListTypes_in_listStatement1140 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_REMOVE_in_removeUDFStatement1158 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_removeUDFStatement1160 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_removeUDFStatement1162 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_TERM_in_removeUDFStatement1166 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_removeUDFStatement1168 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropIndexStatement1190 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_INDEX_in_dropIndexStatement1192 = new BitSet(new long[]{0x0000600000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropIndexStatement1196 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropIndexStatement1198 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropIndexStatement1207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createIndexStatement1232 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_T_INDEX_TYPE_in_createIndexStatement1236 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_INDEX_in_createIndexStatement1240 = new BitSet(new long[]{0x0000600000000000L});
	public static final BitSet FOLLOW_T_IF_in_createIndexStatement1244 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createIndexStatement1246 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createIndexStatement1248 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement1257 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_ON_in_createIndexStatement1262 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement1266 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_LEFT_PARENTHESIS_in_createIndexStatement1271 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement1276 = new BitSet(new long[]{0x0000000200000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_COMMA_in_createIndexStatement1282 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement1288 = new BitSet(new long[]{0x0000000200000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_RIGHT_PARENTHESIS_in_createIndexStatement1297 = new BitSet(new long[]{0x0000000000000002L,0x0000000000003000L});
	public static final BitSet FOLLOW_T_USING_in_createIndexStatement1301 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_TERM_in_createIndexStatement1305 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_WITH_in_createIndexStatement1313 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_createIndexStatement1315 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement1319 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement1321 = new BitSet(new long[]{0x0000200800000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement1325 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_AND_in_createIndexStatement1332 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement1336 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement1338 = new BitSet(new long[]{0x0000200800000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement1342 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement1373 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement1375 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement1377 = new BitSet(new long[]{0x8008043014000000L,0x0000000000000904L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement1381 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement1415 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement1417 = new BitSet(new long[]{0x0000000420000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1429 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1431 = new BitSet(new long[]{0x0000080000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1434 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1437 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1452 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1454 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1456 = new BitSet(new long[]{0x2130004088000000L,0x0000000000000240L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1471 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1490 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1508 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1526 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1544 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1562 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1580 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1598 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1616 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1666 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1668 = new BitSet(new long[]{0x2130004088000000L,0x0000000000000240L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1684 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1703 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1721 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1739 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1757 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1775 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1793 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1811 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1829 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1857 = new BitSet(new long[]{0x0000000020000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1859 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1861 = new BitSet(new long[]{0x0000080000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement1922 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement1930 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement1960 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1966 = new BitSet(new long[]{0x0000600000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement1973 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement1975 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement1987 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement2021 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement2027 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2035 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement2041 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2049 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement2051 = new BitSet(new long[]{0x0000200800000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement2055 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement2064 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2068 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement2070 = new BitSet(new long[]{0x0000200800000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement2074 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement2113 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement2119 = new BitSet(new long[]{0x0000600000000000L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement2126 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement2128 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement2130 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2142 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement2148 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2160 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement2162 = new BitSet(new long[]{0x0000200800000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement2166 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement2175 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2179 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement2181 = new BitSet(new long[]{0x0000200800000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement2185 = new BitSet(new long[]{0x0000000040000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement2224 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement2230 = new BitSet(new long[]{0x0000600000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement2237 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement2239 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement2251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement2271 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement2284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement2307 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement2321 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement2335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement2349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement2363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement2377 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement2391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement2405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createIndexStatement_in_metaStatement2419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropIndexStatement_in_metaStatement2434 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_listStatement_in_metaStatement2449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_addStatement_in_metaStatement2464 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_removeUDFStatement_in_metaStatement2479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query2502 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query2505 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_EOF_in_query2509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_getListTypes2528 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2561 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_T_POINT_in_getTableID2563 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2574 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm2594 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm2606 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral2636 = new BitSet(new long[]{0x0000208000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2646 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2648 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2652 = new BitSet(new long[]{0x0000008200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral2661 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2665 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2667 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2671 = new BitSet(new long[]{0x0000008200000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral2683 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty2705 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty2717 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty2729 = new BitSet(new long[]{0x0000000000000002L});
}
