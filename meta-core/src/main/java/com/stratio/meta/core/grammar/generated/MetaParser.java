// $ANTLR 3.5.1 Meta.g 2014-03-19 12:02:50

    package com.stratio.meta.core.grammar.generated;    
    import com.stratio.meta.core.statements.*;
    import com.stratio.meta.core.structures.*;
    import com.stratio.meta.core.utils.*;
    import java.util.LinkedHashMap;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Set;
    import java.util.HashSet;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "DIGIT", "E", 
		"EXPONENT", "F", "G", "H", "I", "J", "K", "L", "LETTER", "M", "N", "O", 
		"P", "POINT", "Q", "QUOTED_LITERAL", "R", "S", "T", "T_ADD", "T_AGGREGATION", 
		"T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", "T_ANY", "T_AS", "T_ASC", 
		"T_ASTERISK", "T_AT", "T_AVG", "T_BETWEEN", "T_BY", "T_CLUSTERING", "T_COLON", 
		"T_COMMA", "T_COMPACT", "T_CONSISTENCY", "T_CONSTANT", "T_COUNT", "T_CREATE", 
		"T_CUSTOM", "T_DAYS", "T_DEFAULT", "T_DELETE", "T_DESC", "T_DISABLE", 
		"T_DISTINCT", "T_DROP", "T_EACH_QUORUM", "T_END_BRACKET", "T_END_PARENTHESIS", 
		"T_END_SBRACKET", "T_EPHEMERAL", "T_EQUAL", "T_EXISTS", "T_EXPLAIN", "T_FALSE", 
		"T_FLOAT", "T_FOR", "T_FROM", "T_GROUP", "T_GT", "T_GTE", "T_HOURS", "T_IDENT", 
		"T_IF", "T_IN", "T_INDEX", "T_INNER", "T_INSERT", "T_INTERROGATION", "T_INTO", 
		"T_JOIN", "T_KEY", "T_KEYSPACE", "T_KS_AND_TN", "T_LAST", "T_LIKE", "T_LIMIT", 
		"T_LIST", "T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_LT", "T_LTE", "T_LUCENE", 
		"T_MAX", "T_MIN", "T_MINUTES", "T_NOT", "T_NOT_EQUAL", "T_ON", "T_ONE", 
		"T_OPTIONS", "T_ORDER", "T_PATH", "T_PLAN", "T_PLUS", "T_PRIMARY", "T_PROCESS", 
		"T_QUORUM", "T_QUOTE", "T_REMOVE", "T_ROWS", "T_SECONDS", "T_SELECT", 
		"T_SEMICOLON", "T_SET", "T_SINGLE_QUOTE", "T_START_BRACKET", "T_START_PARENTHESIS", 
		"T_START_SBRACKET", "T_STOP", "T_STORAGE", "T_SUBTRACT", "T_TABLE", "T_TERM", 
		"T_THREE", "T_TOKEN", "T_TRIGGER", "T_TRUE", "T_TRUNCATE", "T_TWO", "T_TYPE", 
		"T_UDF", "T_UPDATE", "T_USE", "T_USING", "T_VALUES", "T_WHERE", "T_WINDOW", 
		"T_WITH", "U", "V", "W", "WS", "X", "Y", "Z", "'1'", "'D'", "'H'", "'M'", 
		"'S'", "'d'", "'h'", "'m'", "'s'"
	};
	public static final int EOF=-1;
	public static final int T__149=149;
	public static final int T__150=150;
	public static final int T__151=151;
	public static final int T__152=152;
	public static final int T__153=153;
	public static final int T__154=154;
	public static final int T__155=155;
	public static final int T__156=156;
	public static final int T__157=157;
	public static final int A=4;
	public static final int B=5;
	public static final int C=6;
	public static final int D=7;
	public static final int DIGIT=8;
	public static final int E=9;
	public static final int EXPONENT=10;
	public static final int F=11;
	public static final int G=12;
	public static final int H=13;
	public static final int I=14;
	public static final int J=15;
	public static final int K=16;
	public static final int L=17;
	public static final int LETTER=18;
	public static final int M=19;
	public static final int N=20;
	public static final int O=21;
	public static final int P=22;
	public static final int POINT=23;
	public static final int Q=24;
	public static final int QUOTED_LITERAL=25;
	public static final int R=26;
	public static final int S=27;
	public static final int T=28;
	public static final int T_ADD=29;
	public static final int T_AGGREGATION=30;
	public static final int T_ALL=31;
	public static final int T_ALTER=32;
	public static final int T_ANALYTICS=33;
	public static final int T_AND=34;
	public static final int T_ANY=35;
	public static final int T_AS=36;
	public static final int T_ASC=37;
	public static final int T_ASTERISK=38;
	public static final int T_AT=39;
	public static final int T_AVG=40;
	public static final int T_BETWEEN=41;
	public static final int T_BY=42;
	public static final int T_CLUSTERING=43;
	public static final int T_COLON=44;
	public static final int T_COMMA=45;
	public static final int T_COMPACT=46;
	public static final int T_CONSISTENCY=47;
	public static final int T_CONSTANT=48;
	public static final int T_COUNT=49;
	public static final int T_CREATE=50;
	public static final int T_CUSTOM=51;
	public static final int T_DAYS=52;
	public static final int T_DEFAULT=53;
	public static final int T_DELETE=54;
	public static final int T_DESC=55;
	public static final int T_DISABLE=56;
	public static final int T_DISTINCT=57;
	public static final int T_DROP=58;
	public static final int T_EACH_QUORUM=59;
	public static final int T_END_BRACKET=60;
	public static final int T_END_PARENTHESIS=61;
	public static final int T_END_SBRACKET=62;
	public static final int T_EPHEMERAL=63;
	public static final int T_EQUAL=64;
	public static final int T_EXISTS=65;
	public static final int T_EXPLAIN=66;
	public static final int T_FALSE=67;
	public static final int T_FLOAT=68;
	public static final int T_FOR=69;
	public static final int T_FROM=70;
	public static final int T_GROUP=71;
	public static final int T_GT=72;
	public static final int T_GTE=73;
	public static final int T_HOURS=74;
	public static final int T_IDENT=75;
	public static final int T_IF=76;
	public static final int T_IN=77;
	public static final int T_INDEX=78;
	public static final int T_INNER=79;
	public static final int T_INSERT=80;
	public static final int T_INTERROGATION=81;
	public static final int T_INTO=82;
	public static final int T_JOIN=83;
	public static final int T_KEY=84;
	public static final int T_KEYSPACE=85;
	public static final int T_KS_AND_TN=86;
	public static final int T_LAST=87;
	public static final int T_LIKE=88;
	public static final int T_LIMIT=89;
	public static final int T_LIST=90;
	public static final int T_LOCAL_ONE=91;
	public static final int T_LOCAL_QUORUM=92;
	public static final int T_LT=93;
	public static final int T_LTE=94;
	public static final int T_LUCENE=95;
	public static final int T_MAX=96;
	public static final int T_MIN=97;
	public static final int T_MINUTES=98;
	public static final int T_NOT=99;
	public static final int T_NOT_EQUAL=100;
	public static final int T_ON=101;
	public static final int T_ONE=102;
	public static final int T_OPTIONS=103;
	public static final int T_ORDER=104;
	public static final int T_PATH=105;
	public static final int T_PLAN=106;
	public static final int T_PLUS=107;
	public static final int T_PRIMARY=108;
	public static final int T_PROCESS=109;
	public static final int T_QUORUM=110;
	public static final int T_QUOTE=111;
	public static final int T_REMOVE=112;
	public static final int T_ROWS=113;
	public static final int T_SECONDS=114;
	public static final int T_SELECT=115;
	public static final int T_SEMICOLON=116;
	public static final int T_SET=117;
	public static final int T_SINGLE_QUOTE=118;
	public static final int T_START_BRACKET=119;
	public static final int T_START_PARENTHESIS=120;
	public static final int T_START_SBRACKET=121;
	public static final int T_STOP=122;
	public static final int T_STORAGE=123;
	public static final int T_SUBTRACT=124;
	public static final int T_TABLE=125;
	public static final int T_TERM=126;
	public static final int T_THREE=127;
	public static final int T_TOKEN=128;
	public static final int T_TRIGGER=129;
	public static final int T_TRUE=130;
	public static final int T_TRUNCATE=131;
	public static final int T_TWO=132;
	public static final int T_TYPE=133;
	public static final int T_UDF=134;
	public static final int T_UPDATE=135;
	public static final int T_USE=136;
	public static final int T_USING=137;
	public static final int T_VALUES=138;
	public static final int T_WHERE=139;
	public static final int T_WINDOW=140;
	public static final int T_WITH=141;
	public static final int U=142;
	public static final int V=143;
	public static final int W=144;
	public static final int WS=145;
	public static final int X=146;
	public static final int Y=147;
	public static final int Z=148;

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


	    private ErrorsHelper foundErrors = new ErrorsHelper();

	    public ErrorsHelper getFoundErrors(){
	        return foundErrors;
	    }

	    @Override
	    public void displayRecognitionError(String[] tokenNames, RecognitionException e){        
	        String hdr = getErrorHeader(e);
	        String msg = getErrorMessage(e, tokenNames);
	        /*System.err.println("Antlr exception: ");
	        System.err.print("\tError recognized: ");
	        System.err.print(hdr+": ");
	        System.err.println(msg);*/
	        AntlrError antlrError = new AntlrError(hdr, msg);
	        foundErrors.addError(antlrError);
	    }



	// $ANTLR start "deleteStatement"
	// Meta.g:233:1: deleteStatement returns [DeleteStatement ds] : T_DELETE (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )? T_FROM tablename= getTableID T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ;
	public final DeleteStatement deleteStatement() throws RecognitionException {
		DeleteStatement ds = null;


		Token firstField=null;
		Token field=null;
		String tablename =null;
		MetaRelation rel1 =null;
		MetaRelation relN =null;


				ds = new DeleteStatement();
			
		try {
			// Meta.g:236:3: ( T_DELETE (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )? T_FROM tablename= getTableID T_WHERE rel1= getRelation ( T_AND relN= getRelation )* )
			// Meta.g:237:2: T_DELETE (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )? T_FROM tablename= getTableID T_WHERE rel1= getRelation ( T_AND relN= getRelation )*
			{
			match(input,T_DELETE,FOLLOW_T_DELETE_in_deleteStatement2087); 
			// Meta.g:238:2: (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==T_IDENT) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// Meta.g:238:3: firstField= T_IDENT ( T_COMMA field= T_IDENT )*
					{
					firstField=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_deleteStatement2094); 
					ds.addColumn((firstField!=null?firstField.getText():null));
					// Meta.g:239:3: ( T_COMMA field= T_IDENT )*
					loop1:
					while (true) {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( (LA1_0==T_COMMA) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// Meta.g:239:4: T_COMMA field= T_IDENT
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_deleteStatement2101); 
							field=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_deleteStatement2105); 
							ds.addColumn((field!=null?field.getText():null));
							}
							break;

						default :
							break loop1;
						}
					}

					}
					break;

			}

			match(input,T_FROM,FOLLOW_T_FROM_in_deleteStatement2124); 
			pushFollow(FOLLOW_getTableID_in_deleteStatement2129);
			tablename=getTableID();
			state._fsp--;

			ds.setTablename(tablename);
			match(input,T_WHERE,FOLLOW_T_WHERE_in_deleteStatement2134); 
			pushFollow(FOLLOW_getRelation_in_deleteStatement2139);
			rel1=getRelation();
			state._fsp--;

			ds.addRelation(rel1);
			// Meta.g:244:44: ( T_AND relN= getRelation )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==T_AND) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// Meta.g:244:45: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_deleteStatement2144); 
					pushFollow(FOLLOW_getRelation_in_deleteStatement2148);
					relN=getRelation();
					state._fsp--;

					ds.addRelation(relN);
					}
					break;

				default :
					break loop3;
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
		return ds;
	}
	// $ANTLR end "deleteStatement"



	// $ANTLR start "addStatement"
	// Meta.g:248:1: addStatement returns [AddStatement as] : T_ADD ( T_QUOTE | T_SINGLE_QUOTE ) name= T_PATH ( T_QUOTE | T_SINGLE_QUOTE ) ;
	public final AddStatement addStatement() throws RecognitionException {
		AddStatement as = null;


		Token name=null;

		try {
			// Meta.g:248:39: ( T_ADD ( T_QUOTE | T_SINGLE_QUOTE ) name= T_PATH ( T_QUOTE | T_SINGLE_QUOTE ) )
			// Meta.g:249:2: T_ADD ( T_QUOTE | T_SINGLE_QUOTE ) name= T_PATH ( T_QUOTE | T_SINGLE_QUOTE )
			{
			match(input,T_ADD,FOLLOW_T_ADD_in_addStatement2167); 
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			name=(Token)match(input,T_PATH,FOLLOW_T_PATH_in_addStatement2179); 
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
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
	// Meta.g:253:1: listStatement returns [ListStatement ls] : T_LIST (type= getListTypes ) ;
	public final ListStatement listStatement() throws RecognitionException {
		ListStatement ls = null;


		ParserRuleReturnScope type =null;

		try {
			// Meta.g:253:41: ( T_LIST (type= getListTypes ) )
			// Meta.g:254:2: T_LIST (type= getListTypes )
			{
			match(input,T_LIST,FOLLOW_T_LIST_in_listStatement2204); 
			// Meta.g:254:9: (type= getListTypes )
			// Meta.g:254:10: type= getListTypes
			{
			pushFollow(FOLLOW_getListTypes_in_listStatement2209);
			type=getListTypes();
			state._fsp--;

			}


					if((type!=null?input.toString(type.start,type.stop):null) != null){
						ls = new ListStatement((type!=null?input.toString(type.start,type.stop):null));
					}else{
						throw new RecognitionException();
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
		return ls;
	}
	// $ANTLR end "listStatement"



	// $ANTLR start "removeUDFStatement"
	// Meta.g:265:1: removeUDFStatement returns [RemoveUDFStatement rus] : T_REMOVE T_UDF ( T_QUOTE | T_SINGLE_QUOTE ) jar= getTerm ( T_QUOTE | T_SINGLE_QUOTE ) ;
	public final RemoveUDFStatement removeUDFStatement() throws RecognitionException {
		RemoveUDFStatement rus = null;


		Term jar =null;

		try {
			// Meta.g:265:52: ( T_REMOVE T_UDF ( T_QUOTE | T_SINGLE_QUOTE ) jar= getTerm ( T_QUOTE | T_SINGLE_QUOTE ) )
			// Meta.g:267:2: T_REMOVE T_UDF ( T_QUOTE | T_SINGLE_QUOTE ) jar= getTerm ( T_QUOTE | T_SINGLE_QUOTE )
			{
			match(input,T_REMOVE,FOLLOW_T_REMOVE_in_removeUDFStatement2230); 
			match(input,T_UDF,FOLLOW_T_UDF_in_removeUDFStatement2232); 
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_getTerm_in_removeUDFStatement2244);
			jar=getTerm();
			state._fsp--;

			rus = new RemoveUDFStatement(jar.getTerm());
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
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
		return rus;
	}
	// $ANTLR end "removeUDFStatement"



	// $ANTLR start "dropIndexStatement"
	// Meta.g:271:1: dropIndexStatement returns [DropIndexStatement dis] : T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT ;
	public final DropIndexStatement dropIndexStatement() throws RecognitionException {
		DropIndexStatement dis = null;


		Token name=null;


				dis = new DropIndexStatement();
			
		try {
			// Meta.g:274:3: ( T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT )
			// Meta.g:275:2: T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropIndexStatement2274); 
			match(input,T_INDEX,FOLLOW_T_INDEX_in_dropIndexStatement2276); 
			// Meta.g:276:2: ( T_IF T_EXISTS )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==T_IF) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// Meta.g:276:3: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropIndexStatement2280); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropIndexStatement2282); 
					dis.setDropIfExists();
					}
					break;

			}

			name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropIndexStatement2291); 
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
	// Meta.g:284:1: createIndexStatement returns [CreateIndexStatement cis] : T_CREATE indexType= getIndexType T_INDEX ( T_IF T_NOT T_EXISTS )? (name= T_IDENT )? T_ON tablename= getTableID T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? ;
	public final CreateIndexStatement createIndexStatement() throws RecognitionException {
		CreateIndexStatement cis = null;


		Token name=null;
		Token firstField=null;
		Token field=null;
		Token key=null;
		String indexType =null;
		String tablename =null;
		Term usingClass =null;
		ValueProperty value =null;


				cis = new CreateIndexStatement();
			
		try {
			// Meta.g:287:3: ( T_CREATE indexType= getIndexType T_INDEX ( T_IF T_NOT T_EXISTS )? (name= T_IDENT )? T_ON tablename= getTableID T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? )
			// Meta.g:288:2: T_CREATE indexType= getIndexType T_INDEX ( T_IF T_NOT T_EXISTS )? (name= T_IDENT )? T_ON tablename= getTableID T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createIndexStatement2316); 
			pushFollow(FOLLOW_getIndexType_in_createIndexStatement2320);
			indexType=getIndexType();
			state._fsp--;

			cis.setIndexType(indexType);
			match(input,T_INDEX,FOLLOW_T_INDEX_in_createIndexStatement2324); 
			// Meta.g:289:2: ( T_IF T_NOT T_EXISTS )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==T_IF) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// Meta.g:289:3: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createIndexStatement2328); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createIndexStatement2330); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createIndexStatement2332); 
					cis.setCreateIfNotExists();
					}
					break;

			}

			// Meta.g:290:2: (name= T_IDENT )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==T_IDENT) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Meta.g:290:3: name= T_IDENT
					{
					name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2342); 
					cis.setName((name!=null?name.getText():null));
					}
					break;

			}

			match(input,T_ON,FOLLOW_T_ON_in_createIndexStatement2350); 
			pushFollow(FOLLOW_getTableID_in_createIndexStatement2354);
			tablename=getTableID();
			state._fsp--;

			cis.setTablename(tablename);
			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createIndexStatement2359); 
			firstField=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2364); 
			cis.addColumn((firstField!=null?firstField.getText():null));
			// Meta.g:294:2: ( T_COMMA field= T_IDENT )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==T_COMMA) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// Meta.g:294:3: T_COMMA field= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createIndexStatement2370); 
					field=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2376); 
					cis.addColumn((field!=null?field.getText():null));
					}
					break;

				default :
					break loop7;
				}
			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createIndexStatement2385); 
			// Meta.g:298:2: ( T_USING usingClass= getTerm )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==T_USING) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// Meta.g:298:3: T_USING usingClass= getTerm
					{
					match(input,T_USING,FOLLOW_T_USING_in_createIndexStatement2389); 
					pushFollow(FOLLOW_getTerm_in_createIndexStatement2393);
					usingClass=getTerm();
					state._fsp--;

					cis.setUsingClass(usingClass.getTerm());
					}
					break;

			}

			// Meta.g:299:2: ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==T_WITH) ) {
				alt10=1;
			}
			switch (alt10) {
				case 1 :
					// Meta.g:299:3: T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createIndexStatement2401); 
					match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_createIndexStatement2403); 
					key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2407); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement2409); 
					pushFollow(FOLLOW_getValueProperty_in_createIndexStatement2413);
					value=getValueProperty();
					state._fsp--;

					cis.addOption((key!=null?key.getText():null), value);
					// Meta.g:300:3: ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					loop9:
					while (true) {
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( (LA9_0==T_AND) ) {
							alt9=1;
						}

						switch (alt9) {
						case 1 :
							// Meta.g:300:4: T_AND key= T_IDENT T_EQUAL value= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_createIndexStatement2420); 
							key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2424); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement2426); 
							pushFollow(FOLLOW_getValueProperty_in_createIndexStatement2430);
							value=getValueProperty();
							state._fsp--;

							cis.addOption((key!=null?key.getText():null), value);
							}
							break;

						default :
							break loop9;
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



	// $ANTLR start "updateTableStatement"
	// Meta.g:309:1: updateTableStatement returns [UpdateTableStatement pdtbst] : T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )? ;
	public final UpdateTableStatement updateTableStatement() throws RecognitionException {
		UpdateTableStatement pdtbst = null;


		Token id1=null;
		Token idN=null;
		String tablename =null;
		Option opt1 =null;
		Option optN =null;
		Assignment assig1 =null;
		Assignment assigN =null;
		MetaRelation rel1 =null;
		MetaRelation relN =null;
		Term term1 =null;
		Term termN =null;


		        boolean optsInc = false;
		        boolean condsInc = false;
		        List<Option> options = new ArrayList<>();
		        List<Assignment> assignments = new ArrayList<>();
		        List<MetaRelation> whereclauses = new ArrayList<>();
		        Map<String, Term> conditions = new HashMap<>();
		    
		try {
			// Meta.g:317:6: ( T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )? )
			// Meta.g:318:5: T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )?
			{
			match(input,T_UPDATE,FOLLOW_T_UPDATE_in_updateTableStatement2471); 
			pushFollow(FOLLOW_getTableID_in_updateTableStatement2475);
			tablename=getTableID();
			state._fsp--;

			// Meta.g:319:5: ( T_USING opt1= getOption (optN= getOption )* )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==T_USING) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// Meta.g:319:6: T_USING opt1= getOption (optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_updateTableStatement2482); 
					pushFollow(FOLLOW_getOption_in_updateTableStatement2486);
					opt1=getOption();
					state._fsp--;

					optsInc = true; options.add(opt1);
					// Meta.g:319:66: (optN= getOption )*
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( (LA11_0==T_CLUSTERING||LA11_0==T_COMPACT||LA11_0==T_IDENT) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// Meta.g:319:67: optN= getOption
							{
							pushFollow(FOLLOW_getOption_in_updateTableStatement2493);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop11;
						}
					}

					}
					break;

			}

			match(input,T_SET,FOLLOW_T_SET_in_updateTableStatement2505); 
			pushFollow(FOLLOW_getAssignment_in_updateTableStatement2509);
			assig1=getAssignment();
			state._fsp--;

			assignments.add(assig1);
			// Meta.g:320:59: ( T_COMMA assigN= getAssignment )*
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( (LA13_0==T_COMMA) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// Meta.g:320:60: T_COMMA assigN= getAssignment
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_updateTableStatement2514); 
					pushFollow(FOLLOW_getAssignment_in_updateTableStatement2518);
					assigN=getAssignment();
					state._fsp--;

					assignments.add(assigN);
					}
					break;

				default :
					break loop13;
				}
			}

			match(input,T_WHERE,FOLLOW_T_WHERE_in_updateTableStatement2528); 
			pushFollow(FOLLOW_getRelation_in_updateTableStatement2532);
			rel1=getRelation();
			state._fsp--;

			whereclauses.add(rel1);
			// Meta.g:321:56: ( T_AND relN= getRelation )*
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==T_AND) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// Meta.g:321:57: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_updateTableStatement2537); 
					pushFollow(FOLLOW_getRelation_in_updateTableStatement2541);
					relN=getRelation();
					state._fsp--;

					whereclauses.add(relN);
					}
					break;

				default :
					break loop14;
				}
			}

			// Meta.g:322:5: ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IF) ) {
				alt16=1;
			}
			switch (alt16) {
				case 1 :
					// Meta.g:322:6: T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )*
					{
					match(input,T_IF,FOLLOW_T_IF_in_updateTableStatement2552); 
					id1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_updateTableStatement2556); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_updateTableStatement2558); 
					pushFollow(FOLLOW_getTerm_in_updateTableStatement2562);
					term1=getTerm();
					state._fsp--;

					condsInc = true; conditions.put((id1!=null?id1.getText():null), new Term(term1.getTerm()));
					// Meta.g:323:21: ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )*
					loop15:
					while (true) {
						int alt15=2;
						int LA15_0 = input.LA(1);
						if ( (LA15_0==T_AND) ) {
							alt15=1;
						}

						switch (alt15) {
						case 1 :
							// Meta.g:323:22: T_AND idN= T_IDENT T_EQUAL termN= getTerm
							{
							match(input,T_AND,FOLLOW_T_AND_in_updateTableStatement2588); 
							idN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_updateTableStatement2592); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_updateTableStatement2594); 
							pushFollow(FOLLOW_getTerm_in_updateTableStatement2598);
							termN=getTerm();
							state._fsp--;

							conditions.put((idN!=null?idN.getText():null), new Term(termN.getTerm()));
							}
							break;

						default :
							break loop15;
						}
					}

					}
					break;

			}

			 
			        if(optsInc)
			            if(condsInc)
			                pdtbst = new UpdateTableStatement(tablename, options, assignments, whereclauses, conditions);
			            else
			                pdtbst = new UpdateTableStatement(tablename, options, assignments, whereclauses);
			        else
			            if(condsInc)
			                pdtbst = new UpdateTableStatement(tablename, assignments, whereclauses, conditions);
			            else
			                pdtbst = new UpdateTableStatement(tablename, assignments, whereclauses);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return pdtbst;
	}
	// $ANTLR end "updateTableStatement"



	// $ANTLR start "stopProcessStatement"
	// Meta.g:338:1: stopProcessStatement returns [StopProcessStatement stprst] : T_STOP T_PROCESS ident= T_IDENT ;
	public final StopProcessStatement stopProcessStatement() throws RecognitionException {
		StopProcessStatement stprst = null;


		Token ident=null;

		try {
			// Meta.g:338:59: ( T_STOP T_PROCESS ident= T_IDENT )
			// Meta.g:339:5: T_STOP T_PROCESS ident= T_IDENT
			{
			match(input,T_STOP,FOLLOW_T_STOP_in_stopProcessStatement2630); 
			match(input,T_PROCESS,FOLLOW_T_PROCESS_in_stopProcessStatement2632); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_stopProcessStatement2636); 
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
	// Meta.g:342:1: dropTriggerStatement returns [DropTriggerStatement drtrst] : T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT ;
	public final DropTriggerStatement dropTriggerStatement() throws RecognitionException {
		DropTriggerStatement drtrst = null;


		Token ident=null;
		Token ident2=null;

		try {
			// Meta.g:342:59: ( T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT )
			// Meta.g:343:5: T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTriggerStatement2658); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropTriggerStatement2665); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement2669); 
			match(input,T_ON,FOLLOW_T_ON_in_dropTriggerStatement2676); 
			ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement2685); 
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
	// Meta.g:350:1: createTriggerStatement returns [CreateTriggerStatement crtrst] : T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT ;
	public final CreateTriggerStatement createTriggerStatement() throws RecognitionException {
		CreateTriggerStatement crtrst = null;


		Token trigger_name=null;
		Token table_name=null;
		Token class_name=null;

		try {
			// Meta.g:350:63: ( T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT )
			// Meta.g:351:5: T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTriggerStatement2713); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_createTriggerStatement2720); 
			trigger_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2724); 
			match(input,T_ON,FOLLOW_T_ON_in_createTriggerStatement2731); 
			table_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2740); 
			match(input,T_USING,FOLLOW_T_USING_in_createTriggerStatement2746); 
			class_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2750); 
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



	// $ANTLR start "createTableStatement"
	// Meta.g:360:1: createTableStatement returns [CreateTableStatement crtast] : T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= getTableID T_START_PARENTHESIS (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS ) )* ) T_END_PARENTHESIS ( T_WITH properties= getMetaProperties )? ;
	public final CreateTableStatement createTableStatement() throws RecognitionException {
		CreateTableStatement crtast = null;


		Token ident_column1=null;
		Token ident_columN=null;
		Token primaryK=null;
		Token partitionKN=null;
		Token partitionK=null;
		Token clusterKN=null;
		String name_table =null;
		String type1 =null;
		String typeN =null;
		List<MetaProperty> properties =null;


		    LinkedHashMap<String, String> columns = new LinkedHashMap<>();
		    List<String>   primaryKey = new ArrayList<String>();
		    List<String> clusterKey = new ArrayList<String>();
		    LinkedHashMap<String, ValueProperty> propierties = new LinkedHashMap<>();
		    int Type_Primary_Key= 0;
		    int columnNumberPK= 0;
		    int columnNumberPK_inter= 0;
		    boolean ifNotExists_2 = false;
		    boolean withClusterKey = false;
		    boolean withPropierties = false;
		    
		try {
			// Meta.g:372:6: ( T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= getTableID T_START_PARENTHESIS (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS ) )* ) T_END_PARENTHESIS ( T_WITH properties= getMetaProperties )? )
			// Meta.g:373:5: T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= getTableID T_START_PARENTHESIS (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS ) )* ) T_END_PARENTHESIS ( T_WITH properties= getMetaProperties )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTableStatement2789); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_createTableStatement2795); 
			// Meta.g:375:5: ( T_IF T_NOT T_EXISTS )?
			int alt17=2;
			int LA17_0 = input.LA(1);
			if ( (LA17_0==T_IF) ) {
				alt17=1;
			}
			switch (alt17) {
				case 1 :
					// Meta.g:375:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createTableStatement2802); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createTableStatement2804); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createTableStatement2806); 
					ifNotExists_2 = true;
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_createTableStatement2819);
			name_table=getTableID();
			state._fsp--;

			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement2825); 
			// Meta.g:377:25: (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS ) )* )
			// Meta.g:378:17: ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS ) )*
			{
			ident_column1=input.LT(1);
			if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_getDataType_in_createTableStatement2869);
			type1=getDataType();
			state._fsp--;

			// Meta.g:378:67: ( T_PRIMARY T_KEY )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_PRIMARY) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// Meta.g:378:68: T_PRIMARY T_KEY
					{
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2872); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2874); 
					}
					break;

			}

			columns.put((ident_column1!=null?ident_column1.getText():null),type1); Type_Primary_Key=1;
			// Meta.g:379:17: ( ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS ) )*
			loop24:
			while (true) {
				int alt24=3;
				int LA24_0 = input.LA(1);
				if ( (LA24_0==T_COMMA) ) {
					int LA24_2 = input.LA(2);
					if ( (LA24_2==T_IDENT||LA24_2==T_KEY) ) {
						alt24=1;
					}
					else if ( (LA24_2==T_PRIMARY) ) {
						alt24=2;
					}

				}

				switch (alt24) {
				case 1 :
					// Meta.g:380:21: ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? )
					{
					// Meta.g:380:21: ( T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? )
					// Meta.g:380:23: T_COMMA ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )?
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement2923); 
					ident_columN=input.LT(1);
					if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
						input.consume();
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_getDataType_in_createTableStatement2937);
					typeN=getDataType();
					state._fsp--;

					// Meta.g:380:80: ( T_PRIMARY T_KEY )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==T_PRIMARY) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// Meta.g:380:81: T_PRIMARY T_KEY
							{
							match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2940); 
							match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2942); 
							Type_Primary_Key=2;columnNumberPK=columnNumberPK_inter +1;
							}
							break;

					}

					columns.put((ident_columN!=null?ident_columN.getText():null),typeN);columnNumberPK_inter+=1;
					}

					}
					break;
				case 2 :
					// Meta.g:381:22: ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS )
					{
					// Meta.g:381:22: ( T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS )
					// Meta.g:382:25: T_COMMA T_PRIMARY T_KEY T_START_PARENTHESIS ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) ) T_END_PARENTHESIS
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3000); 
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement3002); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement3004); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement3006); 
					// Meta.g:383:25: ( (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* ) | ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* ) )
					int alt23=2;
					int LA23_0 = input.LA(1);
					if ( (LA23_0==T_IDENT||LA23_0==T_KEY) ) {
						alt23=1;
					}
					else if ( (LA23_0==T_START_PARENTHESIS) ) {
						alt23=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 23, 0, input);
						throw nvae;
					}

					switch (alt23) {
						case 1 :
							// Meta.g:384:29: (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* )
							{
							// Meta.g:384:29: (primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* )
							// Meta.g:384:33: primaryK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )*
							{
							primaryK=input.LT(1);
							if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
								input.consume();
								state.errorRecovery=false;
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							primaryKey.add((primaryK!=null?primaryK.getText():null));Type_Primary_Key=3;
							// Meta.g:386:33: ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )*
							loop20:
							while (true) {
								int alt20=2;
								int LA20_0 = input.LA(1);
								if ( (LA20_0==T_COMMA) ) {
									alt20=1;
								}

								switch (alt20) {
								case 1 :
									// Meta.g:386:34: T_COMMA partitionKN= ( T_IDENT | T_KEY )
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3139); 
									partitionKN=input.LT(1);
									if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
										input.consume();
										state.errorRecovery=false;
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										throw mse;
									}
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop20;
								}
							}

							}

							}
							break;
						case 2 :
							// Meta.g:388:30: ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* )
							{
							// Meta.g:388:30: ( T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )* )
							// Meta.g:389:33: T_START_PARENTHESIS partitionK= ( T_IDENT | T_KEY ) ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )* T_END_PARENTHESIS ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )*
							{
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement3248); 
							partitionK=input.LT(1);
							if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
								input.consume();
								state.errorRecovery=false;
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							primaryKey.add((partitionK!=null?partitionK.getText():null));Type_Primary_Key=4;
							// Meta.g:390:37: ( T_COMMA partitionKN= ( T_IDENT | T_KEY ) )*
							loop21:
							while (true) {
								int alt21=2;
								int LA21_0 = input.LA(1);
								if ( (LA21_0==T_COMMA) ) {
									alt21=1;
								}

								switch (alt21) {
								case 1 :
									// Meta.g:390:38: T_COMMA partitionKN= ( T_IDENT | T_KEY )
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3299); 
									partitionKN=input.LT(1);
									if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
										input.consume();
										state.errorRecovery=false;
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										throw mse;
									}
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop21;
								}
							}

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3347); 
							// Meta.g:392:33: ( T_COMMA clusterKN= ( T_IDENT | T_KEY ) )*
							loop22:
							while (true) {
								int alt22=2;
								int LA22_0 = input.LA(1);
								if ( (LA22_0==T_COMMA) ) {
									alt22=1;
								}

								switch (alt22) {
								case 1 :
									// Meta.g:392:34: T_COMMA clusterKN= ( T_IDENT | T_KEY )
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3383); 
									clusterKN=input.LT(1);
									if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
										input.consume();
										state.errorRecovery=false;
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										throw mse;
									}
									clusterKey.add((clusterKN!=null?clusterKN.getText():null));withClusterKey=true;
									}
									break;

								default :
									break loop22;
								}
							}

							}

							}
							break;

					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3479); 
					}

					}
					break;

				default :
					break loop24;
				}
			}

			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3551); 
			// Meta.g:400:23: ( T_WITH properties= getMetaProperties )?
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==T_WITH) ) {
				alt25=1;
			}
			switch (alt25) {
				case 1 :
					// Meta.g:400:24: T_WITH properties= getMetaProperties
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createTableStatement3554); 
					withPropierties=true;
					pushFollow(FOLLOW_getMetaProperties_in_createTableStatement3560);
					properties=getMetaProperties();
					state._fsp--;

					}
					break;

			}

			crtast = new CreateTableStatement(name_table,columns,primaryKey,clusterKey,properties,Type_Primary_Key,ifNotExists_2,withClusterKey,columnNumberPK,withPropierties);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return crtast;
	}
	// $ANTLR end "createTableStatement"



	// $ANTLR start "alterTableStatement"
	// Meta.g:406:1: alterTableStatement returns [AlterTableStatement altast] : T_ALTER T_TABLE name_table= getTableID ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) ;
	public final AlterTableStatement alterTableStatement() throws RecognitionException {
		AlterTableStatement altast = null;


		Token column=null;
		Token type=null;
		Token identProp1=null;
		Token identPropN=null;
		String name_table =null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        LinkedHashMap<String, ValueProperty> option = new LinkedHashMap<>();
		        int prop= 0;
		    
		try {
			// Meta.g:410:6: ( T_ALTER T_TABLE name_table= getTableID ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) )
			// Meta.g:411:5: T_ALTER T_TABLE name_table= getTableID ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement3623); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_alterTableStatement3629); 
			pushFollow(FOLLOW_getTableID_in_alterTableStatement3637);
			name_table=getTableID();
			state._fsp--;

			// Meta.g:414:5: ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			int alt27=4;
			switch ( input.LA(1) ) {
			case T_ALTER:
				{
				alt27=1;
				}
				break;
			case T_ADD:
				{
				alt27=2;
				}
				break;
			case T_DROP:
				{
				alt27=3;
				}
				break;
			case T_WITH:
				{
				alt27=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 27, 0, input);
				throw nvae;
			}
			switch (alt27) {
				case 1 :
					// Meta.g:414:6: T_ALTER column= T_IDENT T_TYPE type= T_IDENT
					{
					match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement3644); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3648); 
					match(input,T_TYPE,FOLLOW_T_TYPE_in_alterTableStatement3650); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3654); 
					prop=1;
					}
					break;
				case 2 :
					// Meta.g:415:10: T_ADD column= T_IDENT type= T_IDENT
					{
					match(input,T_ADD,FOLLOW_T_ADD_in_alterTableStatement3667); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3671); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3675); 
					prop=2;
					}
					break;
				case 3 :
					// Meta.g:416:10: T_DROP column= T_IDENT
					{
					match(input,T_DROP,FOLLOW_T_DROP_in_alterTableStatement3688); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3692); 
					prop=3;
					}
					break;
				case 4 :
					// Meta.g:417:10: T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_alterTableStatement3705); 
					identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3722); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement3724); 
					pushFollow(FOLLOW_getValueProperty_in_alterTableStatement3728);
					valueProp1=getValueProperty();
					state._fsp--;

					option.put((identProp1!=null?identProp1.getText():null), valueProp1);
					// Meta.g:419:13: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					loop26:
					while (true) {
						int alt26=2;
						int LA26_0 = input.LA(1);
						if ( (LA26_0==T_AND) ) {
							alt26=1;
						}

						switch (alt26) {
						case 1 :
							// Meta.g:419:14: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_alterTableStatement3745); 
							identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3749); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement3751); 
							pushFollow(FOLLOW_getValueProperty_in_alterTableStatement3755);
							valuePropN=getValueProperty();
							state._fsp--;

							option.put((identPropN!=null?identPropN.getText():null), valuePropN);
							}
							break;

						default :
							break loop26;
						}
					}

					prop=4;
					}
					break;

			}

			altast = new AlterTableStatement(name_table, (column!=null?column.getText():null), (type!=null?type.getText():null), option, prop);  
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return altast;
	}
	// $ANTLR end "alterTableStatement"



	// $ANTLR start "selectStatement"
	// Meta.g:425:1: selectStatement returns [SelectStatement slctst] : T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )? ;
	public final SelectStatement selectStatement() throws RecognitionException {
		SelectStatement slctst = null;


		Token constant=null;
		SelectionClause selClause =null;
		String tablename =null;
		WindowSelect window =null;
		String identJoin =null;
		Map<String, String> fields =null;
		List<MetaRelation> whereClauses =null;
		List<MetaOrdering> ordering =null;
		List groupby =null;


		        boolean windowInc = false;
		        boolean joinInc = false;
		        boolean whereInc = false;
		        boolean orderInc = false;
		        boolean groupInc = false;
		        boolean limitInc = false;
		        boolean disable = false;
		    
		try {
			// Meta.g:434:6: ( T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )? )
			// Meta.g:435:5: T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )?
			{
			match(input,T_SELECT,FOLLOW_T_SELECT_in_selectStatement3810); 
			pushFollow(FOLLOW_getSelectClause_in_selectStatement3814);
			selClause=getSelectClause();
			state._fsp--;

			match(input,T_FROM,FOLLOW_T_FROM_in_selectStatement3816); 
			pushFollow(FOLLOW_getTableID_in_selectStatement3820);
			tablename=getTableID();
			state._fsp--;

			// Meta.g:436:5: ( T_WITH T_WINDOW window= getWindow )?
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==T_WITH) ) {
				alt28=1;
			}
			switch (alt28) {
				case 1 :
					// Meta.g:436:6: T_WITH T_WINDOW window= getWindow
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_selectStatement3828); 
					match(input,T_WINDOW,FOLLOW_T_WINDOW_in_selectStatement3830); 
					windowInc = true;
					pushFollow(FOLLOW_getWindow_in_selectStatement3836);
					window=getWindow();
					state._fsp--;

					}
					break;

			}

			// Meta.g:437:5: ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )?
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0==T_INNER) ) {
				alt29=1;
			}
			switch (alt29) {
				case 1 :
					// Meta.g:437:6: T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields
					{
					match(input,T_INNER,FOLLOW_T_INNER_in_selectStatement3849); 
					match(input,T_JOIN,FOLLOW_T_JOIN_in_selectStatement3851); 
					 joinInc = true;
					pushFollow(FOLLOW_getTableID_in_selectStatement3857);
					identJoin=getTableID();
					state._fsp--;

					match(input,T_ON,FOLLOW_T_ON_in_selectStatement3859); 
					pushFollow(FOLLOW_getFields_in_selectStatement3863);
					fields=getFields();
					state._fsp--;

					}
					break;

			}

			// Meta.g:438:5: ( T_WHERE whereClauses= getWhereClauses )?
			int alt30=2;
			int LA30_0 = input.LA(1);
			if ( (LA30_0==T_WHERE) ) {
				alt30=1;
			}
			switch (alt30) {
				case 1 :
					// Meta.g:438:6: T_WHERE whereClauses= getWhereClauses
					{
					match(input,T_WHERE,FOLLOW_T_WHERE_in_selectStatement3872); 
					whereInc = true;
					pushFollow(FOLLOW_getWhereClauses_in_selectStatement3878);
					whereClauses=getWhereClauses();
					state._fsp--;

					}
					break;

			}

			// Meta.g:439:5: ( T_ORDER T_BY ordering= getOrdering )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==T_ORDER) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// Meta.g:439:6: T_ORDER T_BY ordering= getOrdering
					{
					match(input,T_ORDER,FOLLOW_T_ORDER_in_selectStatement3887); 
					match(input,T_BY,FOLLOW_T_BY_in_selectStatement3889); 
					orderInc = true;
					pushFollow(FOLLOW_getOrdering_in_selectStatement3895);
					ordering=getOrdering();
					state._fsp--;

					}
					break;

			}

			// Meta.g:440:5: ( T_GROUP T_BY groupby= getList )?
			int alt32=2;
			int LA32_0 = input.LA(1);
			if ( (LA32_0==T_GROUP) ) {
				alt32=1;
			}
			switch (alt32) {
				case 1 :
					// Meta.g:440:6: T_GROUP T_BY groupby= getList
					{
					match(input,T_GROUP,FOLLOW_T_GROUP_in_selectStatement3904); 
					match(input,T_BY,FOLLOW_T_BY_in_selectStatement3906); 
					groupInc = true;
					pushFollow(FOLLOW_getList_in_selectStatement3912);
					groupby=getList();
					state._fsp--;

					}
					break;

			}

			// Meta.g:441:5: ( T_LIMIT constant= T_CONSTANT )?
			int alt33=2;
			int LA33_0 = input.LA(1);
			if ( (LA33_0==T_LIMIT) ) {
				alt33=1;
			}
			switch (alt33) {
				case 1 :
					// Meta.g:441:6: T_LIMIT constant= T_CONSTANT
					{
					match(input,T_LIMIT,FOLLOW_T_LIMIT_in_selectStatement3921); 
					limitInc = true;
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_selectStatement3927); 
					}
					break;

			}

			// Meta.g:442:5: ( T_DISABLE T_ANALYTICS )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==T_DISABLE) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// Meta.g:442:6: T_DISABLE T_ANALYTICS
					{
					match(input,T_DISABLE,FOLLOW_T_DISABLE_in_selectStatement3936); 
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_selectStatement3938); 
					disable = true;
					}
					break;

			}


			        slctst = new SelectStatement(selClause, tablename);        
			        if(windowInc)
			            slctst.setWindow(window);
			        if(joinInc)
			            slctst.setJoin(new InnerJoin(identJoin, fields)); 
			        if(whereInc)
			             slctst.setWhere(whereClauses); 
			        if(orderInc)
			             slctst.setOrder(ordering);
			        if(groupInc)
			            slctst.setGroup(new GroupBy(groupby)); 
			        if(limitInc)
			            slctst.setLimit(Integer.parseInt((constant!=null?constant.getText():null)));
			        if(disable)
			            slctst.setDisableAnalytics(true);
			    
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
	// Meta.g:461:1: insertIntoStatement returns [InsertIntoStatement nsntst] : T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? ;
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
			// Meta.g:469:6: ( T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? )
			// Meta.g:470:5: T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			{
			match(input,T_INSERT,FOLLOW_T_INSERT_in_insertIntoStatement3971); 
			match(input,T_INTO,FOLLOW_T_INTO_in_insertIntoStatement3978); 
			pushFollow(FOLLOW_getTableID_in_insertIntoStatement3987);
			tableName=getTableID();
			state._fsp--;

			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3993); 
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement4002); 
			ids.add((ident1!=null?ident1.getText():null));
			// Meta.g:475:5: ( T_COMMA identN= T_IDENT )*
			loop35:
			while (true) {
				int alt35=2;
				int LA35_0 = input.LA(1);
				if ( (LA35_0==T_COMMA) ) {
					alt35=1;
				}

				switch (alt35) {
				case 1 :
					// Meta.g:475:6: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement4012); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement4016); 
					ids.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop35;
				}
			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement4027); 
			// Meta.g:477:5: (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS )
			int alt37=2;
			int LA37_0 = input.LA(1);
			if ( (LA37_0==T_SELECT) ) {
				alt37=1;
			}
			else if ( (LA37_0==T_VALUES) ) {
				alt37=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 37, 0, input);
				throw nvae;
			}

			switch (alt37) {
				case 1 :
					// Meta.g:478:9: selectStmnt= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_insertIntoStatement4046);
					selectStmnt=selectStatement();
					state._fsp--;

					typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;
					}
					break;
				case 2 :
					// Meta.g:480:9: T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS
					{
					match(input,T_VALUES,FOLLOW_T_VALUES_in_insertIntoStatement4069); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement4079); 
					pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement4096);
					term1=getTermOrLiteral();
					state._fsp--;

					cellValues.add(term1);
					// Meta.g:483:13: ( T_COMMA termN= getTermOrLiteral )*
					loop36:
					while (true) {
						int alt36=2;
						int LA36_0 = input.LA(1);
						if ( (LA36_0==T_COMMA) ) {
							alt36=1;
						}

						switch (alt36) {
						case 1 :
							// Meta.g:483:14: T_COMMA termN= getTermOrLiteral
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement4113); 
							pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement4117);
							termN=getTermOrLiteral();
							state._fsp--;

							cellValues.add(termN);
							}
							break;

						default :
							break loop36;
						}
					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement4131); 
					}
					break;

			}

			// Meta.g:486:5: ( T_IF T_NOT T_EXISTS )?
			int alt38=2;
			int LA38_0 = input.LA(1);
			if ( (LA38_0==T_IF) ) {
				alt38=1;
			}
			switch (alt38) {
				case 1 :
					// Meta.g:486:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_insertIntoStatement4144); 
					match(input,T_NOT,FOLLOW_T_NOT_in_insertIntoStatement4146); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_insertIntoStatement4148); 
					ifNotExists=true;
					}
					break;

			}

			// Meta.g:487:5: ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( (LA40_0==T_USING) ) {
				alt40=1;
			}
			switch (alt40) {
				case 1 :
					// Meta.g:488:9: T_USING opt1= getOption ( T_AND optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_insertIntoStatement4169); 
					optsInc=true;
					pushFollow(FOLLOW_getOption_in_insertIntoStatement4184);
					opt1=getOption();
					state._fsp--;


					            options.add(opt1);
					        
					// Meta.g:492:9: ( T_AND optN= getOption )*
					loop39:
					while (true) {
						int alt39=2;
						int LA39_0 = input.LA(1);
						if ( (LA39_0==T_AND) ) {
							alt39=1;
						}

						switch (alt39) {
						case 1 :
							// Meta.g:492:10: T_AND optN= getOption
							{
							match(input,T_AND,FOLLOW_T_AND_in_insertIntoStatement4197); 
							pushFollow(FOLLOW_getOption_in_insertIntoStatement4201);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop39;
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
	// Meta.g:509:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		MetaStatement parsedStmnt =null;

		try {
			// Meta.g:509:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:510:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement4238); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement4240); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement4242); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement4246);
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
	// Meta.g:514:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:521:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:522:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement4280); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement4282); 
			// Meta.g:522:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==T_ANALYTICS) ) {
				alt47=1;
			}
			else if ( (LA47_0==T_CONSISTENCY) ) {
				alt47=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 47, 0, input);
				throw nvae;
			}

			switch (alt47) {
				case 1 :
					// Meta.g:523:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement4294); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4296); 
					// Meta.g:523:29: ( T_TRUE | T_FALSE )
					int alt41=2;
					int LA41_0 = input.LA(1);
					if ( (LA41_0==T_TRUE) ) {
						alt41=1;
					}
					else if ( (LA41_0==T_FALSE) ) {
						alt41=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 41, 0, input);
						throw nvae;
					}

					switch (alt41) {
						case 1 :
							// Meta.g:523:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement4299); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:523:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement4302); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:524:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt43=2;
					int LA43_0 = input.LA(1);
					if ( (LA43_0==T_AND) ) {
						alt43=1;
					}
					switch (alt43) {
						case 1 :
							// Meta.g:524:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement4317); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement4319); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4321); 
							// Meta.g:525:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt42=9;
							switch ( input.LA(1) ) {
							case T_ALL:
								{
								alt42=1;
								}
								break;
							case T_ANY:
								{
								alt42=2;
								}
								break;
							case T_QUORUM:
								{
								alt42=3;
								}
								break;
							case T_ONE:
								{
								alt42=4;
								}
								break;
							case T_TWO:
								{
								alt42=5;
								}
								break;
							case T_THREE:
								{
								alt42=6;
								}
								break;
							case T_EACH_QUORUM:
								{
								alt42=7;
								}
								break;
							case T_LOCAL_ONE:
								{
								alt42=8;
								}
								break;
							case T_LOCAL_QUORUM:
								{
								alt42=9;
								}
								break;
							default:
								NoViableAltException nvae =
									new NoViableAltException("", 42, 0, input);
								throw nvae;
							}
							switch (alt42) {
								case 1 :
									// Meta.g:525:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement4336); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:526:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement4355); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:527:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement4373); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:528:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement4391); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:529:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement4409); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:530:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement4427); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:531:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4445); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:532:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4463); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:533:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4481); 
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
					// Meta.g:537:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement4531); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4533); 
					// Meta.g:538:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt44=9;
					switch ( input.LA(1) ) {
					case T_ALL:
						{
						alt44=1;
						}
						break;
					case T_ANY:
						{
						alt44=2;
						}
						break;
					case T_QUORUM:
						{
						alt44=3;
						}
						break;
					case T_ONE:
						{
						alt44=4;
						}
						break;
					case T_TWO:
						{
						alt44=5;
						}
						break;
					case T_THREE:
						{
						alt44=6;
						}
						break;
					case T_EACH_QUORUM:
						{
						alt44=7;
						}
						break;
					case T_LOCAL_ONE:
						{
						alt44=8;
						}
						break;
					case T_LOCAL_QUORUM:
						{
						alt44=9;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 44, 0, input);
						throw nvae;
					}
					switch (alt44) {
						case 1 :
							// Meta.g:538:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement4549); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:539:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement4568); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:540:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement4586); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:541:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement4604); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:542:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement4622); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:543:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement4640); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:544:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4658); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:545:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4676); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:546:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4694); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:548:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt46=2;
					int LA46_0 = input.LA(1);
					if ( (LA46_0==T_AND) ) {
						alt46=1;
					}
					switch (alt46) {
						case 1 :
							// Meta.g:548:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement4722); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement4724); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4726); 
							// Meta.g:548:36: ( T_TRUE | T_FALSE )
							int alt45=2;
							int LA45_0 = input.LA(1);
							if ( (LA45_0==T_TRUE) ) {
								alt45=1;
							}
							else if ( (LA45_0==T_FALSE) ) {
								alt45=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 45, 0, input);
								throw nvae;
							}

							switch (alt45) {
								case 1 :
									// Meta.g:548:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement4729); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:548:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement4732); 
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
	// Meta.g:553:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:553:41: ( T_USE iden= T_IDENT )
			// Meta.g:554:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement4782); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement4790); 
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
	// Meta.g:557:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:560:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:561:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement4815); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement4821); 
			// Meta.g:563:5: ( T_IF T_EXISTS )?
			int alt48=2;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==T_IF) ) {
				alt48=1;
			}
			switch (alt48) {
				case 1 :
					// Meta.g:563:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement4828); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement4830); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement4842); 
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
	// Meta.g:567:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:570:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:571:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement4871); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement4877); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4885); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement4891); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4899); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement4901); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement4905);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:576:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop49:
			while (true) {
				int alt49=2;
				int LA49_0 = input.LA(1);
				if ( (LA49_0==T_AND) ) {
					alt49=1;
				}

				switch (alt49) {
				case 1 :
					// Meta.g:576:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement4914); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4918); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement4920); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement4924);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop49;
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
	// Meta.g:579:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
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
			// Meta.g:583:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:584:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement4958); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement4960); 
			// Meta.g:585:5: ( T_IF T_NOT T_EXISTS )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==T_IF) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// Meta.g:585:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement4967); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement4969); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement4971); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement4983); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement4989); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement5001); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement5003); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement5007);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:589:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop51:
			while (true) {
				int alt51=2;
				int LA51_0 = input.LA(1);
				if ( (LA51_0==T_AND) ) {
					alt51=1;
				}

				switch (alt51) {
				case 1 :
					// Meta.g:589:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement5016); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement5020); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement5022); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement5026);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop51;
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
	// Meta.g:592:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String identID =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:595:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID )
			// Meta.g:596:5: T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement5059); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement5065); 
			// Meta.g:598:5: ( T_IF T_EXISTS )?
			int alt52=2;
			int LA52_0 = input.LA(1);
			if ( (LA52_0==T_IF) ) {
				alt52=1;
			}
			switch (alt52) {
				case 1 :
					// Meta.g:598:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement5072); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement5074); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement5086);
			identID=getTableID();
			state._fsp--;


			        drtbst = new DropTableStatement(identID, ifExists);
			    
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
	// Meta.g:603:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:603:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:604:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement5101); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement5114);
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
	// Meta.g:609:1: metaStatement returns [MetaStatement st] : (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_pdtb= updateTableStatement |st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement |ds= deleteStatement );
	public final MetaStatement metaStatement() throws RecognitionException {
		MetaStatement st = null;


		CreateTableStatement st_crta =null;
		AlterTableStatement st_alta =null;
		CreateTriggerStatement st_crtr =null;
		DropTriggerStatement st_drtr =null;
		StopProcessStatement st_stpr =null;
		UpdateTableStatement st_pdtb =null;
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
		CreateIndexStatement cis =null;
		DropIndexStatement dis =null;
		ListStatement ls =null;
		AddStatement add =null;
		RemoveUDFStatement rs =null;
		DeleteStatement ds =null;

		try {
			// Meta.g:609:41: (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_pdtb= updateTableStatement |st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement |ds= deleteStatement )
			int alt53=22;
			switch ( input.LA(1) ) {
			case T_CREATE:
				{
				switch ( input.LA(2) ) {
				case T_TABLE:
					{
					alt53=1;
					}
					break;
				case T_TRIGGER:
					{
					alt53=3;
					}
					break;
				case T_KEYSPACE:
					{
					alt53=13;
					}
					break;
				case T_CUSTOM:
				case T_DEFAULT:
				case T_LUCENE:
					{
					alt53=17;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_ALTER:
				{
				int LA53_2 = input.LA(2);
				if ( (LA53_2==T_TABLE) ) {
					alt53=2;
				}
				else if ( (LA53_2==T_KEYSPACE) ) {
					alt53=14;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 2, input);
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
					alt53=4;
					}
					break;
				case T_KEYSPACE:
					{
					alt53=12;
					}
					break;
				case T_TABLE:
					{
					alt53=15;
					}
					break;
				case T_INDEX:
					{
					alt53=18;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_STOP:
				{
				alt53=5;
				}
				break;
			case T_UPDATE:
				{
				alt53=6;
				}
				break;
			case T_SELECT:
				{
				alt53=7;
				}
				break;
			case T_INSERT:
				{
				alt53=8;
				}
				break;
			case T_EXPLAIN:
				{
				alt53=9;
				}
				break;
			case T_SET:
				{
				alt53=10;
				}
				break;
			case T_USE:
				{
				alt53=11;
				}
				break;
			case T_TRUNCATE:
				{
				alt53=16;
				}
				break;
			case T_LIST:
				{
				alt53=19;
				}
				break;
			case T_ADD:
				{
				alt53=20;
				}
				break;
			case T_REMOVE:
				{
				alt53=21;
				}
				break;
			case T_DELETE:
				{
				alt53=22;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 53, 0, input);
				throw nvae;
			}
			switch (alt53) {
				case 1 :
					// Meta.g:610:5: st_crta= createTableStatement
					{
					pushFollow(FOLLOW_createTableStatement_in_metaStatement5134);
					st_crta=createTableStatement();
					state._fsp--;

					 st = st_crta;
					}
					break;
				case 2 :
					// Meta.g:611:7: st_alta= alterTableStatement
					{
					pushFollow(FOLLOW_alterTableStatement_in_metaStatement5147);
					st_alta=alterTableStatement();
					state._fsp--;

					 st = st_alta;
					}
					break;
				case 3 :
					// Meta.g:612:7: st_crtr= createTriggerStatement
					{
					pushFollow(FOLLOW_createTriggerStatement_in_metaStatement5160);
					st_crtr=createTriggerStatement();
					state._fsp--;

					 st = st_crtr; 
					}
					break;
				case 4 :
					// Meta.g:613:7: st_drtr= dropTriggerStatement
					{
					pushFollow(FOLLOW_dropTriggerStatement_in_metaStatement5173);
					st_drtr=dropTriggerStatement();
					state._fsp--;

					 st = st_drtr; 
					}
					break;
				case 5 :
					// Meta.g:614:7: st_stpr= stopProcessStatement
					{
					pushFollow(FOLLOW_stopProcessStatement_in_metaStatement5187);
					st_stpr=stopProcessStatement();
					state._fsp--;

					 st = st_stpr; 
					}
					break;
				case 6 :
					// Meta.g:615:7: st_pdtb= updateTableStatement
					{
					pushFollow(FOLLOW_updateTableStatement_in_metaStatement5201);
					st_pdtb=updateTableStatement();
					state._fsp--;

					 st = st_pdtb; 
					}
					break;
				case 7 :
					// Meta.g:616:7: st_slct= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_metaStatement5215);
					st_slct=selectStatement();
					state._fsp--;

					 st = st_slct;
					}
					break;
				case 8 :
					// Meta.g:617:7: st_nsnt= insertIntoStatement
					{
					pushFollow(FOLLOW_insertIntoStatement_in_metaStatement5229);
					st_nsnt=insertIntoStatement();
					state._fsp--;

					 st = st_nsnt;
					}
					break;
				case 9 :
					// Meta.g:618:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement5243);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 10 :
					// Meta.g:619:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement5257);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 11 :
					// Meta.g:620:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement5271);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 12 :
					// Meta.g:621:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement5285);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 13 :
					// Meta.g:622:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement5299);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 14 :
					// Meta.g:623:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement5313);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 15 :
					// Meta.g:624:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement5327);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 16 :
					// Meta.g:625:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement5341);
					st_trst=truncateStatement();
					state._fsp--;

					 st = st_trst; 
					}
					break;
				case 17 :
					// Meta.g:626:7: cis= createIndexStatement
					{
					pushFollow(FOLLOW_createIndexStatement_in_metaStatement5355);
					cis=createIndexStatement();
					state._fsp--;

					 st = cis; 
					}
					break;
				case 18 :
					// Meta.g:627:7: dis= dropIndexStatement
					{
					pushFollow(FOLLOW_dropIndexStatement_in_metaStatement5370);
					dis=dropIndexStatement();
					state._fsp--;

					 st = dis; 
					}
					break;
				case 19 :
					// Meta.g:628:7: ls= listStatement
					{
					pushFollow(FOLLOW_listStatement_in_metaStatement5385);
					ls=listStatement();
					state._fsp--;

					 st = ls; 
					}
					break;
				case 20 :
					// Meta.g:629:7: add= addStatement
					{
					pushFollow(FOLLOW_addStatement_in_metaStatement5400);
					add=addStatement();
					state._fsp--;

					 st = add; 
					}
					break;
				case 21 :
					// Meta.g:630:7: rs= removeUDFStatement
					{
					pushFollow(FOLLOW_removeUDFStatement_in_metaStatement5415);
					rs=removeUDFStatement();
					state._fsp--;

					 st = rs; 
					}
					break;
				case 22 :
					// Meta.g:631:7: ds= deleteStatement
					{
					pushFollow(FOLLOW_deleteStatement_in_metaStatement5430);
					ds=deleteStatement();
					state._fsp--;

					 st = ds; 
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
	// Meta.g:634:1: query returns [MetaStatement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final MetaStatement query() throws RecognitionException {
		MetaStatement st = null;


		MetaStatement mtst =null;

		try {
			// Meta.g:634:33: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:635:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query5453);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:635:21: ( T_SEMICOLON )+
			int cnt54=0;
			loop54:
			while (true) {
				int alt54=2;
				int LA54_0 = input.LA(1);
				if ( (LA54_0==T_SEMICOLON) ) {
					alt54=1;
				}

				switch (alt54) {
				case 1 :
					// Meta.g:635:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query5456); 
					}
					break;

				default :
					if ( cnt54 >= 1 ) break loop54;
					EarlyExitException eee = new EarlyExitException(54, input);
					throw eee;
				}
				cnt54++;
			}

			match(input,EOF,FOLLOW_EOF_in_query5460); 

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



	// $ANTLR start "getIndexType"
	// Meta.g:641:1: getIndexType returns [String indexType] : (idxType= T_DEFAULT |idxType= T_LUCENE |idxType= T_CUSTOM ) ;
	public final String getIndexType() throws RecognitionException {
		String indexType = null;


		Token idxType=null;

		try {
			// Meta.g:641:40: ( (idxType= T_DEFAULT |idxType= T_LUCENE |idxType= T_CUSTOM ) )
			// Meta.g:642:5: (idxType= T_DEFAULT |idxType= T_LUCENE |idxType= T_CUSTOM )
			{
			// Meta.g:642:5: (idxType= T_DEFAULT |idxType= T_LUCENE |idxType= T_CUSTOM )
			int alt55=3;
			switch ( input.LA(1) ) {
			case T_DEFAULT:
				{
				alt55=1;
				}
				break;
			case T_LUCENE:
				{
				alt55=2;
				}
				break;
			case T_CUSTOM:
				{
				alt55=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 55, 0, input);
				throw nvae;
			}
			switch (alt55) {
				case 1 :
					// Meta.g:642:7: idxType= T_DEFAULT
					{
					idxType=(Token)match(input,T_DEFAULT,FOLLOW_T_DEFAULT_in_getIndexType5483); 
					}
					break;
				case 2 :
					// Meta.g:643:7: idxType= T_LUCENE
					{
					idxType=(Token)match(input,T_LUCENE,FOLLOW_T_LUCENE_in_getIndexType5493); 
					}
					break;
				case 3 :
					// Meta.g:644:7: idxType= T_CUSTOM
					{
					idxType=(Token)match(input,T_CUSTOM,FOLLOW_T_CUSTOM_in_getIndexType5503); 
					}
					break;

			}

			indexType =(idxType!=null?idxType.getText():null);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return indexType;
	}
	// $ANTLR end "getIndexType"



	// $ANTLR start "getMetaProperties"
	// Meta.g:648:1: getMetaProperties returns [List<MetaProperty> props] : firstProp= getMetaProperty ( T_AND newProp= getMetaProperty )* ;
	public final List<MetaProperty> getMetaProperties() throws RecognitionException {
		List<MetaProperty> props = null;


		MetaProperty firstProp =null;
		MetaProperty newProp =null;


		        props = new ArrayList<>();
		    
		try {
			// Meta.g:651:6: (firstProp= getMetaProperty ( T_AND newProp= getMetaProperty )* )
			// Meta.g:652:5: firstProp= getMetaProperty ( T_AND newProp= getMetaProperty )*
			{
			pushFollow(FOLLOW_getMetaProperty_in_getMetaProperties5536);
			firstProp=getMetaProperty();
			state._fsp--;

			props.add(firstProp);
			// Meta.g:653:5: ( T_AND newProp= getMetaProperty )*
			loop56:
			while (true) {
				int alt56=2;
				int LA56_0 = input.LA(1);
				if ( (LA56_0==T_AND) ) {
					alt56=1;
				}

				switch (alt56) {
				case 1 :
					// Meta.g:653:6: T_AND newProp= getMetaProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_getMetaProperties5545); 
					pushFollow(FOLLOW_getMetaProperty_in_getMetaProperties5549);
					newProp=getMetaProperty();
					state._fsp--;

					props.add(newProp);
					}
					break;

				default :
					break loop56;
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
		return props;
	}
	// $ANTLR end "getMetaProperties"



	// $ANTLR start "getMetaProperty"
	// Meta.g:656:1: getMetaProperty returns [MetaProperty mp] : ( (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS ) | T_EPHEMERAL (| T_EQUAL ( T_FALSE | T_TRUE ) ) );
	public final MetaProperty getMetaProperty() throws RecognitionException {
		MetaProperty mp = null;


		Token identProp=null;
		ValueProperty valueProp =null;
		List<MetaOrdering> ordering =null;


		        BooleanProperty boolProp = new BooleanProperty(true);
		    
		try {
			// Meta.g:659:6: ( (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS ) | T_EPHEMERAL (| T_EQUAL ( T_FALSE | T_TRUE ) ) )
			int alt60=2;
			int LA60_0 = input.LA(1);
			if ( (LA60_0==T_CLUSTERING||LA60_0==T_COMPACT||LA60_0==T_IDENT) ) {
				alt60=1;
			}
			else if ( (LA60_0==T_EPHEMERAL) ) {
				alt60=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 60, 0, input);
				throw nvae;
			}

			switch (alt60) {
				case 1 :
					// Meta.g:660:5: (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS )
					{
					// Meta.g:660:5: (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS )
					int alt57=3;
					switch ( input.LA(1) ) {
					case T_IDENT:
						{
						alt57=1;
						}
						break;
					case T_COMPACT:
						{
						alt57=2;
						}
						break;
					case T_CLUSTERING:
						{
						alt57=3;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 57, 0, input);
						throw nvae;
					}
					switch (alt57) {
						case 1 :
							// Meta.g:660:6: identProp= T_IDENT T_EQUAL valueProp= getValueProperty
							{
							identProp=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getMetaProperty5580); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getMetaProperty5582); 
							pushFollow(FOLLOW_getValueProperty_in_getMetaProperty5586);
							valueProp=getValueProperty();
							state._fsp--;

							mp = new PropertyNameValue((identProp!=null?identProp.getText():null), valueProp);
							}
							break;
						case 2 :
							// Meta.g:661:7: T_COMPACT T_STORAGE
							{
							match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getMetaProperty5597); 
							match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getMetaProperty5599); 
							mp = new PropertyCompactStorage();
							}
							break;
						case 3 :
							// Meta.g:662:7: T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS
							{
							match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getMetaProperty5609); 
							match(input,T_ORDER,FOLLOW_T_ORDER_in_getMetaProperty5611); 
							match(input,T_BY,FOLLOW_T_BY_in_getMetaProperty5613); 
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getMetaProperty5615); 
							pushFollow(FOLLOW_getOrdering_in_getMetaProperty5619);
							ordering=getOrdering();
							state._fsp--;

							mp = new PropertyClusteringOrder(ordering);
							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getMetaProperty5623); 
							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:663:7: T_EPHEMERAL (| T_EQUAL ( T_FALSE | T_TRUE ) )
					{
					match(input,T_EPHEMERAL,FOLLOW_T_EPHEMERAL_in_getMetaProperty5632); 
					// Meta.g:663:19: (| T_EQUAL ( T_FALSE | T_TRUE ) )
					int alt59=2;
					int LA59_0 = input.LA(1);
					if ( (LA59_0==T_AND||LA59_0==T_SEMICOLON) ) {
						alt59=1;
					}
					else if ( (LA59_0==T_EQUAL) ) {
						alt59=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 59, 0, input);
						throw nvae;
					}

					switch (alt59) {
						case 1 :
							// Meta.g:663:21: 
							{
							}
							break;
						case 2 :
							// Meta.g:663:23: T_EQUAL ( T_FALSE | T_TRUE )
							{
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getMetaProperty5638); 
							// Meta.g:663:31: ( T_FALSE | T_TRUE )
							int alt58=2;
							int LA58_0 = input.LA(1);
							if ( (LA58_0==T_FALSE) ) {
								alt58=1;
							}
							else if ( (LA58_0==T_TRUE) ) {
								alt58=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 58, 0, input);
								throw nvae;
							}

							switch (alt58) {
								case 1 :
									// Meta.g:663:32: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_getMetaProperty5641); 
									new BooleanProperty(false);
									}
									break;
								case 2 :
									// Meta.g:663:72: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_getMetaProperty5647); 
									}
									break;

							}

							}
							break;

					}

					mp = new PropertyNameValue("ephemeral", boolProp);
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
		return mp;
	}
	// $ANTLR end "getMetaProperty"



	// $ANTLR start "getDataType"
	// Meta.g:666:1: getDataType returns [String dataType] : (ident1= T_IDENT ( T_LT ident2= T_IDENT ( T_COMMA ident3= T_IDENT )? T_GT )? ) ;
	public final String getDataType() throws RecognitionException {
		String dataType = null;


		Token ident1=null;
		Token ident2=null;
		Token ident3=null;

		try {
			// Meta.g:666:38: ( (ident1= T_IDENT ( T_LT ident2= T_IDENT ( T_COMMA ident3= T_IDENT )? T_GT )? ) )
			// Meta.g:667:5: (ident1= T_IDENT ( T_LT ident2= T_IDENT ( T_COMMA ident3= T_IDENT )? T_GT )? )
			{
			// Meta.g:667:5: (ident1= T_IDENT ( T_LT ident2= T_IDENT ( T_COMMA ident3= T_IDENT )? T_GT )? )
			// Meta.g:668:9: ident1= T_IDENT ( T_LT ident2= T_IDENT ( T_COMMA ident3= T_IDENT )? T_GT )?
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getDataType5680); 
			// Meta.g:668:24: ( T_LT ident2= T_IDENT ( T_COMMA ident3= T_IDENT )? T_GT )?
			int alt62=2;
			int LA62_0 = input.LA(1);
			if ( (LA62_0==T_LT) ) {
				alt62=1;
			}
			switch (alt62) {
				case 1 :
					// Meta.g:668:25: T_LT ident2= T_IDENT ( T_COMMA ident3= T_IDENT )? T_GT
					{
					match(input,T_LT,FOLLOW_T_LT_in_getDataType5683); 
					ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getDataType5687); 
					// Meta.g:668:45: ( T_COMMA ident3= T_IDENT )?
					int alt61=2;
					int LA61_0 = input.LA(1);
					if ( (LA61_0==T_COMMA) ) {
						alt61=1;
					}
					switch (alt61) {
						case 1 :
							// Meta.g:668:46: T_COMMA ident3= T_IDENT
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getDataType5690); 
							ident3=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getDataType5694); 
							}
							break;

					}

					match(input,T_GT,FOLLOW_T_GT_in_getDataType5698); 
					}
					break;

			}

			}

			dataType = (ident1!=null?ident1.getText():null).concat(ident2==null?"":"<"+(ident2!=null?ident2.getText():null)).concat(ident3==null?"":","+(ident3!=null?ident3.getText():null)).concat(ident2==null?"":">");
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return dataType;
	}
	// $ANTLR end "getDataType"



	// $ANTLR start "getOrdering"
	// Meta.g:673:1: getOrdering returns [List<MetaOrdering> order] : ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )* ;
	public final List<MetaOrdering> getOrdering() throws RecognitionException {
		List<MetaOrdering> order = null;


		Token ident1=null;
		Token identN=null;


		        order = new ArrayList<>();
		        MetaOrdering ordering;
		    
		try {
			// Meta.g:677:6: (ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )* )
			// Meta.g:678:5: ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )*
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOrdering5738); 
			ordering = new MetaOrdering((ident1!=null?ident1.getText():null));
			// Meta.g:678:65: ( T_ASC | T_DESC )?
			int alt63=3;
			int LA63_0 = input.LA(1);
			if ( (LA63_0==T_ASC) ) {
				alt63=1;
			}
			else if ( (LA63_0==T_DESC) ) {
				alt63=2;
			}
			switch (alt63) {
				case 1 :
					// Meta.g:678:66: T_ASC
					{
					match(input,T_ASC,FOLLOW_T_ASC_in_getOrdering5743); 
					ordering.setOrderDir(OrderDirection.ASC);
					}
					break;
				case 2 :
					// Meta.g:678:118: T_DESC
					{
					match(input,T_DESC,FOLLOW_T_DESC_in_getOrdering5749); 
					ordering.setOrderDir(OrderDirection.DESC);
					}
					break;

			}

			order.add(ordering);
			// Meta.g:679:5: ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )*
			loop65:
			while (true) {
				int alt65=2;
				int LA65_0 = input.LA(1);
				if ( (LA65_0==T_COMMA) ) {
					alt65=1;
				}

				switch (alt65) {
				case 1 :
					// Meta.g:679:6: T_COMMA identN= T_IDENT ( T_ASC | T_DESC )?
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getOrdering5762); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOrdering5766); 
					ordering = new MetaOrdering((identN!=null?identN.getText():null));
					// Meta.g:679:74: ( T_ASC | T_DESC )?
					int alt64=3;
					int LA64_0 = input.LA(1);
					if ( (LA64_0==T_ASC) ) {
						alt64=1;
					}
					else if ( (LA64_0==T_DESC) ) {
						alt64=2;
					}
					switch (alt64) {
						case 1 :
							// Meta.g:679:75: T_ASC
							{
							match(input,T_ASC,FOLLOW_T_ASC_in_getOrdering5771); 
							ordering.setOrderDir(OrderDirection.ASC);
							}
							break;
						case 2 :
							// Meta.g:679:127: T_DESC
							{
							match(input,T_DESC,FOLLOW_T_DESC_in_getOrdering5777); 
							ordering.setOrderDir(OrderDirection.DESC);
							}
							break;

					}

					order.add(ordering);
					}
					break;

				default :
					break loop65;
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
		return order;
	}
	// $ANTLR end "getOrdering"



	// $ANTLR start "getWhereClauses"
	// Meta.g:682:1: getWhereClauses returns [List<MetaRelation> clauses] : rel1= getRelation ( T_AND relN= getRelation )* ;
	public final List<MetaRelation> getWhereClauses() throws RecognitionException {
		List<MetaRelation> clauses = null;


		MetaRelation rel1 =null;
		MetaRelation relN =null;


		        clauses = new ArrayList<>();
		    
		try {
			// Meta.g:685:6: (rel1= getRelation ( T_AND relN= getRelation )* )
			// Meta.g:686:5: rel1= getRelation ( T_AND relN= getRelation )*
			{
			pushFollow(FOLLOW_getRelation_in_getWhereClauses5811);
			rel1=getRelation();
			state._fsp--;

			clauses.add(rel1);
			// Meta.g:686:43: ( T_AND relN= getRelation )*
			loop66:
			while (true) {
				int alt66=2;
				int LA66_0 = input.LA(1);
				if ( (LA66_0==T_AND) ) {
					alt66=1;
				}

				switch (alt66) {
				case 1 :
					// Meta.g:686:44: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_getWhereClauses5816); 
					pushFollow(FOLLOW_getRelation_in_getWhereClauses5820);
					relN=getRelation();
					state._fsp--;

					clauses.add(relN);
					}
					break;

				default :
					break loop66;
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
		return clauses;
	}
	// $ANTLR end "getWhereClauses"



	// $ANTLR start "getFields"
	// Meta.g:689:1: getFields returns [Map<String, String> fields] : ident1L= getTableID T_EQUAL ident1R= getTableID (identNL= getTableID T_EQUAL identNR= getTableID )* ;
	public final Map<String, String> getFields() throws RecognitionException {
		Map<String, String> fields = null;


		String ident1L =null;
		String ident1R =null;
		String identNL =null;
		String identNR =null;


		        fields = new HashMap<>();
		    
		try {
			// Meta.g:692:6: (ident1L= getTableID T_EQUAL ident1R= getTableID (identNL= getTableID T_EQUAL identNR= getTableID )* )
			// Meta.g:693:5: ident1L= getTableID T_EQUAL ident1R= getTableID (identNL= getTableID T_EQUAL identNR= getTableID )*
			{
			pushFollow(FOLLOW_getTableID_in_getFields5850);
			ident1L=getTableID();
			state._fsp--;

			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getFields5852); 
			pushFollow(FOLLOW_getTableID_in_getFields5856);
			ident1R=getTableID();
			state._fsp--;

			 fields.put(ident1L, ident1R);
			// Meta.g:694:5: (identNL= getTableID T_EQUAL identNR= getTableID )*
			loop67:
			while (true) {
				int alt67=2;
				int LA67_0 = input.LA(1);
				if ( (LA67_0==T_IDENT||LA67_0==T_KS_AND_TN) ) {
					alt67=1;
				}

				switch (alt67) {
				case 1 :
					// Meta.g:694:6: identNL= getTableID T_EQUAL identNR= getTableID
					{
					pushFollow(FOLLOW_getTableID_in_getFields5867);
					identNL=getTableID();
					state._fsp--;

					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getFields5869); 
					pushFollow(FOLLOW_getTableID_in_getFields5873);
					identNR=getTableID();
					state._fsp--;

					 fields.put(identNL, identNR);
					}
					break;

				default :
					break loop67;
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
		return fields;
	}
	// $ANTLR end "getFields"



	// $ANTLR start "getWindow"
	// Meta.g:697:1: getWindow returns [WindowSelect ws] : ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) ) ;
	public final WindowSelect getWindow() throws RecognitionException {
		WindowSelect ws = null;


		Token cnstnt=null;
		TimeUnit unit =null;

		try {
			// Meta.g:697:36: ( ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) ) )
			// Meta.g:698:5: ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) )
			{
			// Meta.g:698:5: ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) )
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==T_LAST) ) {
				alt69=1;
			}
			else if ( (LA69_0==T_CONSTANT) ) {
				alt69=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 69, 0, input);
				throw nvae;
			}

			switch (alt69) {
				case 1 :
					// Meta.g:698:6: T_LAST
					{
					match(input,T_LAST,FOLLOW_T_LAST_in_getWindow5895); 
					ws = new WindowLast();
					}
					break;
				case 2 :
					// Meta.g:699:7: cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit )
					{
					cnstnt=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getWindow5908); 
					// Meta.g:699:25: ( T_ROWS |unit= getTimeUnit )
					int alt68=2;
					int LA68_0 = input.LA(1);
					if ( (LA68_0==T_ROWS) ) {
						alt68=1;
					}
					else if ( (LA68_0==T_DAYS||LA68_0==T_HOURS||LA68_0==T_MINUTES||LA68_0==T_SECONDS||(LA68_0 >= 150 && LA68_0 <= 157)) ) {
						alt68=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 68, 0, input);
						throw nvae;
					}

					switch (alt68) {
						case 1 :
							// Meta.g:699:26: T_ROWS
							{
							match(input,T_ROWS,FOLLOW_T_ROWS_in_getWindow5911); 
							ws = new WindowRows(Integer.parseInt((cnstnt!=null?cnstnt.getText():null)));
							}
							break;
						case 2 :
							// Meta.g:700:26: unit= getTimeUnit
							{
							pushFollow(FOLLOW_getTimeUnit_in_getWindow5943);
							unit=getTimeUnit();
							state._fsp--;

							ws = new WindowTime(Integer.parseInt((cnstnt!=null?cnstnt.getText():null)), unit);
							}
							break;

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
		return ws;
	}
	// $ANTLR end "getWindow"



	// $ANTLR start "getTimeUnit"
	// Meta.g:704:1: getTimeUnit returns [TimeUnit unit] : ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | T_SECONDS | T_MINUTES | T_HOURS | T_DAYS ) ;
	public final TimeUnit getTimeUnit() throws RecognitionException {
		TimeUnit unit = null;


		try {
			// Meta.g:704:36: ( ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | T_SECONDS | T_MINUTES | T_HOURS | T_DAYS ) )
			// Meta.g:705:5: ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | T_SECONDS | T_MINUTES | T_HOURS | T_DAYS )
			{
			// Meta.g:705:5: ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | T_SECONDS | T_MINUTES | T_HOURS | T_DAYS )
			int alt70=12;
			switch ( input.LA(1) ) {
			case 153:
				{
				alt70=1;
				}
				break;
			case 152:
				{
				alt70=2;
				}
				break;
			case 151:
				{
				alt70=3;
				}
				break;
			case 150:
				{
				alt70=4;
				}
				break;
			case 157:
				{
				alt70=5;
				}
				break;
			case 156:
				{
				alt70=6;
				}
				break;
			case 155:
				{
				alt70=7;
				}
				break;
			case 154:
				{
				alt70=8;
				}
				break;
			case T_SECONDS:
				{
				alt70=9;
				}
				break;
			case T_MINUTES:
				{
				alt70=10;
				}
				break;
			case T_HOURS:
				{
				alt70=11;
				}
				break;
			case T_DAYS:
				{
				alt70=12;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 70, 0, input);
				throw nvae;
			}
			switch (alt70) {
				case 1 :
					// Meta.g:705:7: 'S'
					{
					match(input,153,FOLLOW_153_in_getTimeUnit5993); 
					unit =TimeUnit.SECONDS;
					}
					break;
				case 2 :
					// Meta.g:706:7: 'M'
					{
					match(input,152,FOLLOW_152_in_getTimeUnit6003); 
					unit =TimeUnit.MINUTES;
					}
					break;
				case 3 :
					// Meta.g:707:7: 'H'
					{
					match(input,151,FOLLOW_151_in_getTimeUnit6013); 
					unit =TimeUnit.HOURS;
					}
					break;
				case 4 :
					// Meta.g:708:7: 'D'
					{
					match(input,150,FOLLOW_150_in_getTimeUnit6023); 
					unit =TimeUnit.DAYS;
					}
					break;
				case 5 :
					// Meta.g:709:7: 's'
					{
					match(input,157,FOLLOW_157_in_getTimeUnit6033); 
					unit =TimeUnit.SECONDS;
					}
					break;
				case 6 :
					// Meta.g:710:7: 'm'
					{
					match(input,156,FOLLOW_156_in_getTimeUnit6043); 
					unit =TimeUnit.MINUTES;
					}
					break;
				case 7 :
					// Meta.g:711:7: 'h'
					{
					match(input,155,FOLLOW_155_in_getTimeUnit6053); 
					unit =TimeUnit.HOURS;
					}
					break;
				case 8 :
					// Meta.g:712:7: 'd'
					{
					match(input,154,FOLLOW_154_in_getTimeUnit6063); 
					unit =TimeUnit.DAYS;
					}
					break;
				case 9 :
					// Meta.g:713:7: T_SECONDS
					{
					match(input,T_SECONDS,FOLLOW_T_SECONDS_in_getTimeUnit6073); 
					unit =TimeUnit.SECONDS;
					}
					break;
				case 10 :
					// Meta.g:714:7: T_MINUTES
					{
					match(input,T_MINUTES,FOLLOW_T_MINUTES_in_getTimeUnit6083); 
					unit =TimeUnit.MINUTES;
					}
					break;
				case 11 :
					// Meta.g:715:7: T_HOURS
					{
					match(input,T_HOURS,FOLLOW_T_HOURS_in_getTimeUnit6093); 
					unit =TimeUnit.HOURS;
					}
					break;
				case 12 :
					// Meta.g:716:7: T_DAYS
					{
					match(input,T_DAYS,FOLLOW_T_DAYS_in_getTimeUnit6103); 
					unit =TimeUnit.DAYS;
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
		return unit;
	}
	// $ANTLR end "getTimeUnit"



	// $ANTLR start "getSelectClause"
	// Meta.g:720:1: getSelectClause returns [SelectionClause sc] : (scc= getSelectionCount |scl= getSelectionList );
	public final SelectionClause getSelectClause() throws RecognitionException {
		SelectionClause sc = null;


		SelectionCount scc =null;
		SelectionList scl =null;

		try {
			// Meta.g:720:45: (scc= getSelectionCount |scl= getSelectionList )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==T_COUNT) ) {
				int LA71_1 = input.LA(2);
				if ( (LA71_1==T_START_PARENTHESIS) ) {
					int LA71_3 = input.LA(3);
					if ( (LA71_3==T_ASTERISK||LA71_3==149) ) {
						alt71=1;
					}
					else if ( (LA71_3==T_AGGREGATION||LA71_3==T_AVG||LA71_3==T_COUNT||LA71_3==T_END_PARENTHESIS||LA71_3==T_IDENT||LA71_3==T_KS_AND_TN||(LA71_3 >= T_MAX && LA71_3 <= T_MIN)) ) {
						alt71=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 71, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 71, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA71_0==T_AGGREGATION||LA71_0==T_ASTERISK||LA71_0==T_AVG||LA71_0==T_DISTINCT||LA71_0==T_IDENT||LA71_0==T_KS_AND_TN||(LA71_0 >= T_MAX && LA71_0 <= T_MIN)) ) {
				alt71=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 71, 0, input);
				throw nvae;
			}

			switch (alt71) {
				case 1 :
					// Meta.g:721:5: scc= getSelectionCount
					{
					pushFollow(FOLLOW_getSelectionCount_in_getSelectClause6129);
					scc=getSelectionCount();
					state._fsp--;

					sc = scc;
					}
					break;
				case 2 :
					// Meta.g:722:7: scl= getSelectionList
					{
					pushFollow(FOLLOW_getSelectionList_in_getSelectClause6141);
					scl=getSelectionList();
					state._fsp--;

					sc = scl;
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
		return sc;
	}
	// $ANTLR end "getSelectClause"



	// $ANTLR start "getSelectionCount"
	// Meta.g:725:1: getSelectionCount returns [SelectionCount scc] : T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )? ;
	public final SelectionCount getSelectionCount() throws RecognitionException {
		SelectionCount scc = null;


		Token ident=null;


		        boolean identInc = false;
		        char symbol = '*';
		    
		try {
			// Meta.g:729:6: ( T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )? )
			// Meta.g:730:5: T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )?
			{
			match(input,T_COUNT,FOLLOW_T_COUNT_in_getSelectionCount6167); 
			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelectionCount6169); 
			// Meta.g:730:33: ( T_ASTERISK | '1' )
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==T_ASTERISK) ) {
				alt72=1;
			}
			else if ( (LA72_0==149) ) {
				alt72=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 72, 0, input);
				throw nvae;
			}

			switch (alt72) {
				case 1 :
					// Meta.g:730:35: T_ASTERISK
					{
					match(input,T_ASTERISK,FOLLOW_T_ASTERISK_in_getSelectionCount6173); 
					}
					break;
				case 2 :
					// Meta.g:730:48: '1'
					{
					match(input,149,FOLLOW_149_in_getSelectionCount6177); 
					symbol = '1';
					}
					break;

			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelectionCount6183); 
			// Meta.g:731:5: ( T_AS ident= T_IDENT )?
			int alt73=2;
			int LA73_0 = input.LA(1);
			if ( (LA73_0==T_AS) ) {
				alt73=1;
			}
			switch (alt73) {
				case 1 :
					// Meta.g:731:6: T_AS ident= T_IDENT
					{
					match(input,T_AS,FOLLOW_T_AS_in_getSelectionCount6190); 
					identInc = true;
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelectionCount6196); 
					}
					break;

			}


			        if(identInc)
			            scc = new SelectionCount(symbol, identInc, (ident!=null?ident.getText():null));
			        else
			            scc = new SelectionCount(symbol);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return scc;
	}
	// $ANTLR end "getSelectionCount"



	// $ANTLR start "getSelectionList"
	// Meta.g:740:1: getSelectionList returns [SelectionList scl] : ( T_DISTINCT )? selections= getSelection ;
	public final SelectionList getSelectionList() throws RecognitionException {
		SelectionList scl = null;


		Selection selections =null;


		        boolean distinct = false;
		    
		try {
			// Meta.g:743:6: ( ( T_DISTINCT )? selections= getSelection )
			// Meta.g:744:5: ( T_DISTINCT )? selections= getSelection
			{
			// Meta.g:744:5: ( T_DISTINCT )?
			int alt74=2;
			int LA74_0 = input.LA(1);
			if ( (LA74_0==T_DISTINCT) ) {
				alt74=1;
			}
			switch (alt74) {
				case 1 :
					// Meta.g:744:6: T_DISTINCT
					{
					match(input,T_DISTINCT,FOLLOW_T_DISTINCT_in_getSelectionList6231); 
					distinct = true;
					}
					break;

			}

			pushFollow(FOLLOW_getSelection_in_getSelectionList6239);
			selections=getSelection();
			state._fsp--;

			 scl = new SelectionList(distinct, selections);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return scl;
	}
	// $ANTLR end "getSelectionList"



	// $ANTLR start "getSelection"
	// Meta.g:748:1: getSelection returns [Selection slct] : ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* ) ;
	public final Selection getSelection() throws RecognitionException {
		Selection slct = null;


		Token ident1=null;
		Token identN=null;
		SelectorMeta selector1 =null;
		SelectorMeta selectorN =null;


		        SelectionSelector slsl;
		        List<SelectionSelector> selections = new ArrayList<>();
		    
		try {
			// Meta.g:752:6: ( ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* ) )
			// Meta.g:753:5: ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* )
			{
			// Meta.g:753:5: ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* )
			int alt78=2;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==T_ASTERISK) ) {
				alt78=1;
			}
			else if ( (LA78_0==T_AGGREGATION||LA78_0==T_AVG||LA78_0==T_COUNT||LA78_0==T_IDENT||LA78_0==T_KS_AND_TN||(LA78_0 >= T_MAX && LA78_0 <= T_MIN)) ) {
				alt78=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 78, 0, input);
				throw nvae;
			}

			switch (alt78) {
				case 1 :
					// Meta.g:754:9: T_ASTERISK
					{
					match(input,T_ASTERISK,FOLLOW_T_ASTERISK_in_getSelection6279); 
					 slct = new SelectionAsterisk();
					}
					break;
				case 2 :
					// Meta.g:755:11: selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )*
					{
					pushFollow(FOLLOW_getSelector_in_getSelection6302);
					selector1=getSelector();
					state._fsp--;

					 slsl = new SelectionSelector(selector1);
					// Meta.g:755:77: ( T_AS ident1= T_IDENT )?
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==T_AS) ) {
						alt75=1;
					}
					switch (alt75) {
						case 1 :
							// Meta.g:755:78: T_AS ident1= T_IDENT
							{
							match(input,T_AS,FOLLOW_T_AS_in_getSelection6307); 
							ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelection6311); 
							slsl.setAlias((ident1!=null?ident1.getText():null));
							}
							break;

					}

					selections.add(slsl);
					// Meta.g:756:13: ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )*
					loop77:
					while (true) {
						int alt77=2;
						int LA77_0 = input.LA(1);
						if ( (LA77_0==T_COMMA) ) {
							alt77=1;
						}

						switch (alt77) {
						case 1 :
							// Meta.g:756:14: T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )?
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelection6332); 
							pushFollow(FOLLOW_getSelector_in_getSelection6336);
							selectorN=getSelector();
							state._fsp--;

							slsl = new SelectionSelector(selectorN);
							// Meta.g:756:87: ( T_AS identN= T_IDENT )?
							int alt76=2;
							int LA76_0 = input.LA(1);
							if ( (LA76_0==T_AS) ) {
								alt76=1;
							}
							switch (alt76) {
								case 1 :
									// Meta.g:756:88: T_AS identN= T_IDENT
									{
									match(input,T_AS,FOLLOW_T_AS_in_getSelection6341); 
									identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelection6345); 
									slsl.setAlias((identN!=null?identN.getText():null));
									}
									break;

							}

							selections.add(slsl);
							}
							break;

						default :
							break loop77;
						}
					}

					 slct = new SelectionSelectors(selections);
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
		return slct;
	}
	// $ANTLR end "getSelection"



	// $ANTLR start "getSelector"
	// Meta.g:761:1: getSelector returns [SelectorMeta slmt] : ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) ) ;
	public final SelectorMeta getSelector() throws RecognitionException {
		SelectorMeta slmt = null;


		SelectorMeta select1 =null;
		SelectorMeta selectN =null;
		String identID =null;


		        List<SelectorMeta> params = new ArrayList<>();
		        GroupByFunction gbFunc = null;
		    
		try {
			// Meta.g:765:6: ( ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) ) )
			// Meta.g:766:5: ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) )
			{
			// Meta.g:766:5: ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) )
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==T_AGGREGATION||LA85_0==T_AVG||LA85_0==T_COUNT||(LA85_0 >= T_MAX && LA85_0 <= T_MIN)) ) {
				alt85=1;
			}
			else if ( (LA85_0==T_IDENT||LA85_0==T_KS_AND_TN) ) {
				alt85=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 85, 0, input);
				throw nvae;
			}

			switch (alt85) {
				case 1 :
					// Meta.g:766:7: ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS
					{
					// Meta.g:766:7: ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT )
					int alt79=5;
					switch ( input.LA(1) ) {
					case T_AGGREGATION:
						{
						alt79=1;
						}
						break;
					case T_MAX:
						{
						alt79=2;
						}
						break;
					case T_MIN:
						{
						alt79=3;
						}
						break;
					case T_AVG:
						{
						alt79=4;
						}
						break;
					case T_COUNT:
						{
						alt79=5;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 79, 0, input);
						throw nvae;
					}
					switch (alt79) {
						case 1 :
							// Meta.g:766:8: T_AGGREGATION
							{
							match(input,T_AGGREGATION,FOLLOW_T_AGGREGATION_in_getSelector6400); 
							gbFunc = GroupByFunction.aggregation;
							}
							break;
						case 2 :
							// Meta.g:767:10: T_MAX
							{
							match(input,T_MAX,FOLLOW_T_MAX_in_getSelector6414); 
							gbFunc = GroupByFunction.max;
							}
							break;
						case 3 :
							// Meta.g:768:10: T_MIN
							{
							match(input,T_MIN,FOLLOW_T_MIN_in_getSelector6428); 
							gbFunc = GroupByFunction.min;
							}
							break;
						case 4 :
							// Meta.g:769:10: T_AVG
							{
							match(input,T_AVG,FOLLOW_T_AVG_in_getSelector6442); 
							gbFunc = GroupByFunction.avg;
							}
							break;
						case 5 :
							// Meta.g:770:10: T_COUNT
							{
							match(input,T_COUNT,FOLLOW_T_COUNT_in_getSelector6456); 
							gbFunc = GroupByFunction.count;
							}
							break;

					}

					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelector6482); 
					// Meta.g:773:17: (select1= getSelector ( T_COMMA selectN= getSelector )* )?
					int alt81=2;
					int LA81_0 = input.LA(1);
					if ( (LA81_0==T_AGGREGATION||LA81_0==T_AVG||LA81_0==T_COUNT||LA81_0==T_IDENT||LA81_0==T_KS_AND_TN||(LA81_0 >= T_MAX && LA81_0 <= T_MIN)) ) {
						alt81=1;
					}
					switch (alt81) {
						case 1 :
							// Meta.g:773:18: select1= getSelector ( T_COMMA selectN= getSelector )*
							{
							pushFollow(FOLLOW_getSelector_in_getSelector6504);
							select1=getSelector();
							state._fsp--;

							params.add(select1);
							// Meta.g:773:61: ( T_COMMA selectN= getSelector )*
							loop80:
							while (true) {
								int alt80=2;
								int LA80_0 = input.LA(1);
								if ( (LA80_0==T_COMMA) ) {
									alt80=1;
								}

								switch (alt80) {
								case 1 :
									// Meta.g:773:62: T_COMMA selectN= getSelector
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelector6509); 
									pushFollow(FOLLOW_getSelector_in_getSelector6513);
									selectN=getSelector();
									state._fsp--;

									params.add(selectN);
									}
									break;

								default :
									break loop80;
								}
							}

							}
							break;

					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelector6534); 
					slmt = new SelectorGroupBy(gbFunc, params);
					}
					break;
				case 2 :
					// Meta.g:775:11: identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS )
					{
					pushFollow(FOLLOW_getTableID_in_getSelector6550);
					identID=getTableID();
					state._fsp--;

					// Meta.g:775:30: (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS )
					int alt84=2;
					int LA84_0 = input.LA(1);
					if ( (LA84_0==T_AS||LA84_0==T_COMMA||LA84_0==T_END_PARENTHESIS||LA84_0==T_FROM) ) {
						alt84=1;
					}
					else if ( (LA84_0==T_START_PARENTHESIS) ) {
						alt84=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 84, 0, input);
						throw nvae;
					}

					switch (alt84) {
						case 1 :
							// Meta.g:776:13: 
							{
							slmt = new SelectorIdentifier(identID);
							}
							break;
						case 2 :
							// Meta.g:777:15: T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS
							{
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelector6582); 
							// Meta.g:777:35: (select1= getSelector ( T_COMMA selectN= getSelector )* )?
							int alt83=2;
							int LA83_0 = input.LA(1);
							if ( (LA83_0==T_AGGREGATION||LA83_0==T_AVG||LA83_0==T_COUNT||LA83_0==T_IDENT||LA83_0==T_KS_AND_TN||(LA83_0 >= T_MAX && LA83_0 <= T_MIN)) ) {
								alt83=1;
							}
							switch (alt83) {
								case 1 :
									// Meta.g:777:36: select1= getSelector ( T_COMMA selectN= getSelector )*
									{
									pushFollow(FOLLOW_getSelector_in_getSelector6587);
									select1=getSelector();
									state._fsp--;

									params.add(select1);
									// Meta.g:777:79: ( T_COMMA selectN= getSelector )*
									loop82:
									while (true) {
										int alt82=2;
										int LA82_0 = input.LA(1);
										if ( (LA82_0==T_COMMA) ) {
											alt82=1;
										}

										switch (alt82) {
										case 1 :
											// Meta.g:777:80: T_COMMA selectN= getSelector
											{
											match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelector6592); 
											pushFollow(FOLLOW_getSelector_in_getSelector6596);
											selectN=getSelector();
											state._fsp--;

											params.add(selectN);
											}
											break;

										default :
											break loop82;
										}
									}

									}
									break;

							}

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelector6621); 
							slmt = new SelectorFunction(identID, params);
							}
							break;

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
		return slmt;
	}
	// $ANTLR end "getSelector"


	public static class getListTypes_return extends ParserRuleReturnScope {
		public String listType;
	};


	// $ANTLR start "getListTypes"
	// Meta.g:783:1: getListTypes returns [String listType] : ident= ( T_PROCESS | T_UDF | T_TRIGGER ) ;
	public final MetaParser.getListTypes_return getListTypes() throws RecognitionException {
		MetaParser.getListTypes_return retval = new MetaParser.getListTypes_return();
		retval.start = input.LT(1);

		Token ident=null;

		try {
			// Meta.g:783:39: (ident= ( T_PROCESS | T_UDF | T_TRIGGER ) )
			// Meta.g:787:2: ident= ( T_PROCESS | T_UDF | T_TRIGGER )
			{
			ident=input.LT(1);
			if ( input.LA(1)==T_PROCESS||input.LA(1)==T_TRIGGER||input.LA(1)==T_UDF ) {
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



	// $ANTLR start "getAssignment"
	// Meta.g:790:1: getAssignment returns [Assignment assign] : ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm ) ;
	public final Assignment getAssignment() throws RecognitionException {
		Assignment assign = null;


		Token ident=null;
		ValueAssignment value =null;
		Term termL =null;
		Term termR =null;

		try {
			// Meta.g:790:42: (ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm ) )
			// Meta.g:791:5: ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm )
			{
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getAssignment6691); 
			// Meta.g:791:19: ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm )
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==T_EQUAL) ) {
				alt86=1;
			}
			else if ( (LA86_0==T_START_BRACKET) ) {
				alt86=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 86, 0, input);
				throw nvae;
			}

			switch (alt86) {
				case 1 :
					// Meta.g:792:9: T_EQUAL value= getValueAssign
					{
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getAssignment6703); 
					pushFollow(FOLLOW_getValueAssign_in_getAssignment6707);
					value=getValueAssign();
					state._fsp--;

					assign = new Assignment(new IdentifierAssignment((ident!=null?ident.getText():null)), value);
					}
					break;
				case 2 :
					// Meta.g:794:9: T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm
					{
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_getAssignment6726); 
					pushFollow(FOLLOW_getTerm_in_getAssignment6730);
					termL=getTerm();
					state._fsp--;

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_getAssignment6732); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getAssignment6734); 
					pushFollow(FOLLOW_getTerm_in_getAssignment6738);
					termR=getTerm();
					state._fsp--;

					 
					            assign = new Assignment (new IdentifierAssignment((ident!=null?ident.getText():null), termL), new ValueAssignment(termR));
					        
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
		return assign;
	}
	// $ANTLR end "getAssignment"



	// $ANTLR start "getValueAssign"
	// Meta.g:800:1: getValueAssign returns [ValueAssignment valueAssign] : (term1= getTerm |ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList ) );
	public final ValueAssignment getValueAssign() throws RecognitionException {
		ValueAssignment valueAssign = null;


		Token ident=null;
		Term term1 =null;
		Map<String, String> mapLiteral =null;
		IdentIntOrLiteral value1 =null;
		IdentIntOrLiteral value2 =null;

		try {
			// Meta.g:800:53: (term1= getTerm |ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList ) )
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==T_IDENT) ) {
				int LA89_1 = input.LA(2);
				if ( (LA89_1==T_AT||LA89_1==T_COMMA||LA89_1==T_WHERE) ) {
					alt89=1;
				}
				else if ( (LA89_1==T_PLUS||LA89_1==T_SUBTRACT) ) {
					alt89=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 89, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA89_0==QUOTED_LITERAL||LA89_0==T_CONSTANT||LA89_0==T_FALSE||LA89_0==T_KS_AND_TN||LA89_0==T_PATH||LA89_0==T_TERM||LA89_0==T_TRUE||LA89_0==149) ) {
				alt89=1;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 89, 0, input);
				throw nvae;
			}

			switch (alt89) {
				case 1 :
					// Meta.g:801:5: term1= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getValueAssign6765);
					term1=getTerm();
					state._fsp--;

					 valueAssign = new ValueAssignment(term1);
					}
					break;
				case 2 :
					// Meta.g:802:7: ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList )
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueAssign6777); 
					// Meta.g:802:21: ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList )
					int alt88=2;
					int LA88_0 = input.LA(1);
					if ( (LA88_0==T_PLUS) ) {
						alt88=1;
					}
					else if ( (LA88_0==T_SUBTRACT) ) {
						alt88=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 88, 0, input);
						throw nvae;
					}

					switch (alt88) {
						case 1 :
							// Meta.g:802:22: T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList )
							{
							match(input,T_PLUS,FOLLOW_T_PLUS_in_getValueAssign6780); 
							// Meta.g:802:29: ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList )
							int alt87=2;
							int LA87_0 = input.LA(1);
							if ( (LA87_0==T_START_SBRACKET) ) {
								int LA87_1 = input.LA(2);
								if ( (LA87_1==T_START_SBRACKET) ) {
									alt87=1;
								}
								else if ( (LA87_1==QUOTED_LITERAL||LA87_1==T_CONSTANT||LA87_1==T_FALSE||LA87_1==T_IDENT||LA87_1==T_KS_AND_TN||LA87_1==T_PATH||LA87_1==T_TERM||LA87_1==T_TRUE||LA87_1==149) ) {
									alt87=2;
								}

								else {
									int nvaeMark = input.mark();
									try {
										input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 87, 1, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

							}
							else if ( (LA87_0==T_CONSTANT||LA87_0==T_START_BRACKET) ) {
								alt87=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 87, 0, input);
								throw nvae;
							}

							switch (alt87) {
								case 1 :
									// Meta.g:802:30: T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET
									{
									match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getValueAssign6783); 
									pushFollow(FOLLOW_getMapLiteral_in_getValueAssign6787);
									mapLiteral=getMapLiteral();
									state._fsp--;

									match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getValueAssign6789); 
									 valueAssign = new ValueAssignment(new IdentMap((ident!=null?ident.getText():null), new MapLiteralProperty(mapLiteral)));
									}
									break;
								case 2 :
									// Meta.g:803:35: value1= getIntSetOrList
									{
									pushFollow(FOLLOW_getIntSetOrList_in_getValueAssign6829);
									value1=getIntSetOrList();
									state._fsp--;

									 
									                                                            if(value1 instanceof IntTerm)
									                                                                valueAssign = new ValueAssignment(new IntTerm((ident!=null?ident.getText():null), '+', ((IntTerm) value1).getTerm()));
									                                                            else if(value1 instanceof ListLiteral)
									                                                                valueAssign = new ValueAssignment(new ListLiteral((ident!=null?ident.getText():null), '+', ((ListLiteral) value1).getLiterals()));
									                                                            else
									                                                                valueAssign = new ValueAssignment(new SetLiteral((ident!=null?ident.getText():null), '+', ((SetLiteral) value1).getLiterals()));
									                                                         
									}
									break;

							}

							}
							break;
						case 2 :
							// Meta.g:812:11: T_SUBTRACT value2= getIntSetOrList
							{
							match(input,T_SUBTRACT,FOLLOW_T_SUBTRACT_in_getValueAssign6873); 
							pushFollow(FOLLOW_getIntSetOrList_in_getValueAssign6877);
							value2=getIntSetOrList();
							state._fsp--;

							 
							                                                if(value2 instanceof IntTerm)
							                                                    valueAssign = new ValueAssignment(new IntTerm((ident!=null?ident.getText():null), '-', ((IntTerm) value2).getTerm()));
							                                                else if(value2 instanceof ListLiteral)
							                                                    valueAssign = new ValueAssignment(new ListLiteral((ident!=null?ident.getText():null), '-', ((ListLiteral) value2).getLiterals()));
							                                                else
							                                                    valueAssign = new ValueAssignment(new SetLiteral((ident!=null?ident.getText():null), '-', ((SetLiteral) value2).getLiterals()));
							                                            
							}
							break;

					}

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
		return valueAssign;
	}
	// $ANTLR end "getValueAssign"



	// $ANTLR start "getIntSetOrList"
	// Meta.g:823:1: getIntSetOrList returns [IdentIntOrLiteral iiol] : (constant= T_CONSTANT | T_START_BRACKET list= getList T_END_BRACKET | T_START_SBRACKET set= getSet T_END_SBRACKET );
	public final IdentIntOrLiteral getIntSetOrList() throws RecognitionException {
		IdentIntOrLiteral iiol = null;


		Token constant=null;
		List list =null;
		Set set =null;

		try {
			// Meta.g:823:49: (constant= T_CONSTANT | T_START_BRACKET list= getList T_END_BRACKET | T_START_SBRACKET set= getSet T_END_SBRACKET )
			int alt90=3;
			switch ( input.LA(1) ) {
			case T_CONSTANT:
				{
				alt90=1;
				}
				break;
			case T_START_BRACKET:
				{
				alt90=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt90=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 90, 0, input);
				throw nvae;
			}
			switch (alt90) {
				case 1 :
					// Meta.g:824:5: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getIntSetOrList6903); 
					 iiol = new IntTerm(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 2 :
					// Meta.g:825:7: T_START_BRACKET list= getList T_END_BRACKET
					{
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_getIntSetOrList6913); 
					pushFollow(FOLLOW_getList_in_getIntSetOrList6917);
					list=getList();
					state._fsp--;

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_getIntSetOrList6919); 
					 iiol = new ListLiteral(list);
					}
					break;
				case 3 :
					// Meta.g:826:7: T_START_SBRACKET set= getSet T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getIntSetOrList6929); 
					pushFollow(FOLLOW_getSet_in_getIntSetOrList6933);
					set=getSet();
					state._fsp--;

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getIntSetOrList6935); 
					 iiol = new SetLiteral(set);
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
		return iiol;
	}
	// $ANTLR end "getIntSetOrList"



	// $ANTLR start "getRelation"
	// Meta.g:829:1: getRelation returns [MetaRelation mrel] : ( T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS ) |ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm ) );
	public final MetaRelation getRelation() throws RecognitionException {
		MetaRelation mrel = null;


		Token ident=null;
		List<String> listIds =null;
		String operator =null;
		Term term =null;
		List terms =null;
		String compSymbol =null;
		Term termR =null;
		Term term1 =null;
		Term term2 =null;

		try {
			// Meta.g:829:40: ( T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS ) |ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm ) )
			int alt93=2;
			int LA93_0 = input.LA(1);
			if ( (LA93_0==T_TOKEN) ) {
				alt93=1;
			}
			else if ( (LA93_0==T_IDENT) ) {
				alt93=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 93, 0, input);
				throw nvae;
			}

			switch (alt93) {
				case 1 :
					// Meta.g:830:5: T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS )
					{
					match(input,T_TOKEN,FOLLOW_T_TOKEN_in_getRelation6953); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation6955); 
					pushFollow(FOLLOW_getIds_in_getRelation6959);
					listIds=getIds();
					state._fsp--;

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation6961); 
					pushFollow(FOLLOW_getComparator_in_getRelation6965);
					operator=getComparator();
					state._fsp--;

					// Meta.g:830:89: (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS )
					int alt91=2;
					int LA91_0 = input.LA(1);
					if ( (LA91_0==QUOTED_LITERAL||LA91_0==T_CONSTANT||LA91_0==T_FALSE||LA91_0==T_IDENT||LA91_0==T_KS_AND_TN||LA91_0==T_PATH||LA91_0==T_TERM||LA91_0==T_TRUE||LA91_0==149) ) {
						alt91=1;
					}
					else if ( (LA91_0==T_TOKEN) ) {
						alt91=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 91, 0, input);
						throw nvae;
					}

					switch (alt91) {
						case 1 :
							// Meta.g:830:90: term= getTerm
							{
							pushFollow(FOLLOW_getTerm_in_getRelation6970);
							term=getTerm();
							state._fsp--;

							mrel = new RelationToken(listIds, operator, term);
							}
							break;
						case 2 :
							// Meta.g:831:31: T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS
							{
							match(input,T_TOKEN,FOLLOW_T_TOKEN_in_getRelation7004); 
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation7006); 
							pushFollow(FOLLOW_getTerms_in_getRelation7010);
							terms=getTerms();
							state._fsp--;

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation7012); 
							mrel = new RelationToken(listIds, operator, terms);
							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:832:7: ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm )
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getRelation7025); 
					// Meta.g:832:21: (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm )
					int alt92=3;
					switch ( input.LA(1) ) {
					case T_EQUAL:
					case T_GT:
					case T_GTE:
					case T_LIKE:
					case T_LT:
					case T_LTE:
					case T_NOT_EQUAL:
						{
						alt92=1;
						}
						break;
					case T_IN:
						{
						alt92=2;
						}
						break;
					case T_BETWEEN:
						{
						alt92=3;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 92, 0, input);
						throw nvae;
					}
					switch (alt92) {
						case 1 :
							// Meta.g:832:23: compSymbol= getComparator termR= getTerm
							{
							pushFollow(FOLLOW_getComparator_in_getRelation7031);
							compSymbol=getComparator();
							state._fsp--;

							pushFollow(FOLLOW_getTerm_in_getRelation7035);
							termR=getTerm();
							state._fsp--;

							mrel = new RelationCompare((ident!=null?ident.getText():null), compSymbol, termR);
							}
							break;
						case 2 :
							// Meta.g:833:23: T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS
							{
							match(input,T_IN,FOLLOW_T_IN_in_getRelation7061); 
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation7063); 
							pushFollow(FOLLOW_getTerms_in_getRelation7067);
							terms=getTerms();
							state._fsp--;

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation7069); 
							mrel = new RelationIn((ident!=null?ident.getText():null), terms);
							}
							break;
						case 3 :
							// Meta.g:834:23: T_BETWEEN term1= getTerm T_AND term2= getTerm
							{
							match(input,T_BETWEEN,FOLLOW_T_BETWEEN_in_getRelation7095); 
							pushFollow(FOLLOW_getTerm_in_getRelation7099);
							term1=getTerm();
							state._fsp--;

							match(input,T_AND,FOLLOW_T_AND_in_getRelation7101); 
							pushFollow(FOLLOW_getTerm_in_getRelation7105);
							term2=getTerm();
							state._fsp--;

							mrel = new RelationBetween((ident!=null?ident.getText():null), term1, term2);
							}
							break;

					}

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
		return mrel;
	}
	// $ANTLR end "getRelation"



	// $ANTLR start "getComparator"
	// Meta.g:838:1: getComparator returns [String comparator] : ( T_EQUAL | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_LIKE );
	public final String getComparator() throws RecognitionException {
		String comparator = null;


		try {
			// Meta.g:838:42: ( T_EQUAL | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_LIKE )
			int alt94=7;
			switch ( input.LA(1) ) {
			case T_EQUAL:
				{
				alt94=1;
				}
				break;
			case T_GT:
				{
				alt94=2;
				}
				break;
			case T_LT:
				{
				alt94=3;
				}
				break;
			case T_GTE:
				{
				alt94=4;
				}
				break;
			case T_LTE:
				{
				alt94=5;
				}
				break;
			case T_NOT_EQUAL:
				{
				alt94=6;
				}
				break;
			case T_LIKE:
				{
				alt94=7;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 94, 0, input);
				throw nvae;
			}
			switch (alt94) {
				case 1 :
					// Meta.g:839:5: T_EQUAL
					{
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getComparator7145); 
					comparator ="=";
					}
					break;
				case 2 :
					// Meta.g:840:7: T_GT
					{
					match(input,T_GT,FOLLOW_T_GT_in_getComparator7155); 
					comparator =">";
					}
					break;
				case 3 :
					// Meta.g:841:7: T_LT
					{
					match(input,T_LT,FOLLOW_T_LT_in_getComparator7165); 
					comparator ="<";
					}
					break;
				case 4 :
					// Meta.g:842:7: T_GTE
					{
					match(input,T_GTE,FOLLOW_T_GTE_in_getComparator7175); 
					comparator =">=";
					}
					break;
				case 5 :
					// Meta.g:843:7: T_LTE
					{
					match(input,T_LTE,FOLLOW_T_LTE_in_getComparator7186); 
					comparator ="<=";
					}
					break;
				case 6 :
					// Meta.g:844:7: T_NOT_EQUAL
					{
					match(input,T_NOT_EQUAL,FOLLOW_T_NOT_EQUAL_in_getComparator7196); 
					comparator ="<>";
					}
					break;
				case 7 :
					// Meta.g:845:7: T_LIKE
					{
					match(input,T_LIKE,FOLLOW_T_LIKE_in_getComparator7207); 
					comparator ="LIKE";
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
		return comparator;
	}
	// $ANTLR end "getComparator"



	// $ANTLR start "getIds"
	// Meta.g:848:1: getIds returns [List<String> listStrs] : ident1= T_IDENT ( T_COMMA identN= T_IDENT )* ;
	public final List<String> getIds() throws RecognitionException {
		List<String> listStrs = null;


		Token ident1=null;
		Token identN=null;


		        listStrs = new ArrayList<>();
		    
		try {
			// Meta.g:851:6: (ident1= T_IDENT ( T_COMMA identN= T_IDENT )* )
			// Meta.g:852:5: ident1= T_IDENT ( T_COMMA identN= T_IDENT )*
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getIds7235); 
			listStrs.add((ident1!=null?ident1.getText():null));
			// Meta.g:852:50: ( T_COMMA identN= T_IDENT )*
			loop95:
			while (true) {
				int alt95=2;
				int LA95_0 = input.LA(1);
				if ( (LA95_0==T_COMMA) ) {
					alt95=1;
				}

				switch (alt95) {
				case 1 :
					// Meta.g:852:51: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getIds7240); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getIds7244); 
					listStrs.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop95;
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
		return listStrs;
	}
	// $ANTLR end "getIds"



	// $ANTLR start "getOptions"
	// Meta.g:855:1: getOptions returns [List<Option> opts] : opt1= getOption (optN= getOption )* ;
	public final List<Option> getOptions() throws RecognitionException {
		List<Option> opts = null;


		Option opt1 =null;
		Option optN =null;


		        opts = new ArrayList<>();
		    
		try {
			// Meta.g:857:6: (opt1= getOption (optN= getOption )* )
			// Meta.g:858:5: opt1= getOption (optN= getOption )*
			{
			pushFollow(FOLLOW_getOption_in_getOptions7269);
			opt1=getOption();
			state._fsp--;

			opts.add(opt1);
			// Meta.g:858:38: (optN= getOption )*
			loop96:
			while (true) {
				int alt96=2;
				int LA96_0 = input.LA(1);
				if ( (LA96_0==T_CLUSTERING||LA96_0==T_COMPACT||LA96_0==T_IDENT) ) {
					alt96=1;
				}

				switch (alt96) {
				case 1 :
					// Meta.g:858:39: optN= getOption
					{
					pushFollow(FOLLOW_getOption_in_getOptions7276);
					optN=getOption();
					state._fsp--;

					opts.add(optN);
					}
					break;

				default :
					break loop96;
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
		return opts;
	}
	// $ANTLR end "getOptions"



	// $ANTLR start "getOption"
	// Meta.g:861:1: getOption returns [Option opt] : ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty );
	public final Option getOption() throws RecognitionException {
		Option opt = null;


		Token identProp=null;
		ValueProperty valueProp =null;

		try {
			// Meta.g:861:31: ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty )
			int alt97=3;
			switch ( input.LA(1) ) {
			case T_COMPACT:
				{
				alt97=1;
				}
				break;
			case T_CLUSTERING:
				{
				alt97=2;
				}
				break;
			case T_IDENT:
				{
				alt97=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 97, 0, input);
				throw nvae;
			}
			switch (alt97) {
				case 1 :
					// Meta.g:862:5: T_COMPACT T_STORAGE
					{
					match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getOption7296); 
					match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getOption7298); 
					opt =new Option(Option.OPTION_COMPACT);
					}
					break;
				case 2 :
					// Meta.g:863:7: T_CLUSTERING T_ORDER
					{
					match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getOption7308); 
					match(input,T_ORDER,FOLLOW_T_ORDER_in_getOption7310); 
					opt =new Option(Option.OPTION_CLUSTERING);
					}
					break;
				case 3 :
					// Meta.g:864:7: identProp= T_IDENT T_EQUAL valueProp= getValueProperty
					{
					identProp=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOption7322); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getOption7324); 
					pushFollow(FOLLOW_getValueProperty_in_getOption7328);
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
	// Meta.g:867:1: getList returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getList() throws RecognitionException {
		List list = null;


		Term term1 =null;
		Term termN =null;


		        list = new ArrayList<String>();
		    
		try {
			// Meta.g:870:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:871:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getList7356);
			term1=getTerm();
			state._fsp--;

			list.add(term1.getTerm());
			// Meta.g:872:5: ( T_COMMA termN= getTerm )*
			loop98:
			while (true) {
				int alt98=2;
				int LA98_0 = input.LA(1);
				if ( (LA98_0==T_COMMA) ) {
					alt98=1;
				}

				switch (alt98) {
				case 1 :
					// Meta.g:872:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getList7365); 
					pushFollow(FOLLOW_getTerm_in_getList7369);
					termN=getTerm();
					state._fsp--;

					list.add(termN.getTerm());
					}
					break;

				default :
					break loop98;
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



	// $ANTLR start "getTerms"
	// Meta.g:875:1: getTerms returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getTerms() throws RecognitionException {
		List list = null;


		Term term1 =null;
		Term termN =null;


		        list = new ArrayList<Term>();
		    
		try {
			// Meta.g:878:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:879:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getTerms7403);
			term1=getTerm();
			state._fsp--;

			list.add(term1);
			// Meta.g:880:5: ( T_COMMA termN= getTerm )*
			loop99:
			while (true) {
				int alt99=2;
				int LA99_0 = input.LA(1);
				if ( (LA99_0==T_COMMA) ) {
					alt99=1;
				}

				switch (alt99) {
				case 1 :
					// Meta.g:880:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getTerms7412); 
					pushFollow(FOLLOW_getTerm_in_getTerms7416);
					termN=getTerm();
					state._fsp--;

					list.add(termN);
					}
					break;

				default :
					break loop99;
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
	// $ANTLR end "getTerms"



	// $ANTLR start "getSet"
	// Meta.g:883:1: getSet returns [Set set] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final Set getSet() throws RecognitionException {
		Set set = null;


		Term term1 =null;
		Term termN =null;


		        set = new HashSet<String>();
		    
		try {
			// Meta.g:886:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:887:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getSet7450);
			term1=getTerm();
			state._fsp--;

			set.add(term1.getTerm());
			// Meta.g:888:5: ( T_COMMA termN= getTerm )*
			loop100:
			while (true) {
				int alt100=2;
				int LA100_0 = input.LA(1);
				if ( (LA100_0==T_COMMA) ) {
					alt100=1;
				}

				switch (alt100) {
				case 1 :
					// Meta.g:888:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getSet7459); 
					pushFollow(FOLLOW_getTerm_in_getSet7463);
					termN=getTerm();
					state._fsp--;

					set.add(termN.getTerm());
					}
					break;

				default :
					break loop100;
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
		return set;
	}
	// $ANTLR end "getSet"



	// $ANTLR start "getTermOrLiteral"
	// Meta.g:891:1: getTermOrLiteral returns [ValueCell vc] : (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET );
	public final ValueCell getTermOrLiteral() throws RecognitionException {
		ValueCell vc = null;


		Term term =null;
		Term term1 =null;
		Term termN =null;


		        CollectionLiteral cl = new CollectionLiteral();
		    
		try {
			// Meta.g:894:6: (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET )
			int alt103=2;
			int LA103_0 = input.LA(1);
			if ( (LA103_0==QUOTED_LITERAL||LA103_0==T_CONSTANT||LA103_0==T_FALSE||LA103_0==T_IDENT||LA103_0==T_KS_AND_TN||LA103_0==T_PATH||LA103_0==T_TERM||LA103_0==T_TRUE||LA103_0==149) ) {
				alt103=1;
			}
			else if ( (LA103_0==T_START_SBRACKET) ) {
				alt103=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 103, 0, input);
				throw nvae;
			}

			switch (alt103) {
				case 1 :
					// Meta.g:895:5: term= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7497);
					term=getTerm();
					state._fsp--;

					vc =term;
					}
					break;
				case 2 :
					// Meta.g:897:5: T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getTermOrLiteral7511); 
					// Meta.g:898:5: (term1= getTerm ( T_COMMA termN= getTerm )* )?
					int alt102=2;
					int LA102_0 = input.LA(1);
					if ( (LA102_0==QUOTED_LITERAL||LA102_0==T_CONSTANT||LA102_0==T_FALSE||LA102_0==T_IDENT||LA102_0==T_KS_AND_TN||LA102_0==T_PATH||LA102_0==T_TERM||LA102_0==T_TRUE||LA102_0==149) ) {
						alt102=1;
					}
					switch (alt102) {
						case 1 :
							// Meta.g:899:9: term1= getTerm ( T_COMMA termN= getTerm )*
							{
							pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7529);
							term1=getTerm();
							state._fsp--;

							cl.addLiteral(term1);
							// Meta.g:900:9: ( T_COMMA termN= getTerm )*
							loop101:
							while (true) {
								int alt101=2;
								int LA101_0 = input.LA(1);
								if ( (LA101_0==T_COMMA) ) {
									alt101=1;
								}

								switch (alt101) {
								case 1 :
									// Meta.g:900:10: T_COMMA termN= getTerm
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getTermOrLiteral7542); 
									pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7546);
									termN=getTerm();
									state._fsp--;

									cl.addLiteral(termN);
									}
									break;

								default :
									break loop101;
								}
							}

							}
							break;

					}

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getTermOrLiteral7563); 
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
	// Meta.g:905:1: getTableID returns [String tableID] : (ident1= T_IDENT |ident2= T_KS_AND_TN ) ;
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ident1=null;
		Token ident2=null;


		        tableID ="";
		    
		try {
			// Meta.g:908:6: ( (ident1= T_IDENT |ident2= T_KS_AND_TN ) )
			// Meta.g:909:5: (ident1= T_IDENT |ident2= T_KS_AND_TN )
			{
			// Meta.g:909:5: (ident1= T_IDENT |ident2= T_KS_AND_TN )
			int alt104=2;
			int LA104_0 = input.LA(1);
			if ( (LA104_0==T_IDENT) ) {
				alt104=1;
			}
			else if ( (LA104_0==T_KS_AND_TN) ) {
				alt104=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 104, 0, input);
				throw nvae;
			}

			switch (alt104) {
				case 1 :
					// Meta.g:909:6: ident1= T_IDENT
					{
					ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID7593); 
					tableID = new String((ident1!=null?ident1.getText():null));
					}
					break;
				case 2 :
					// Meta.g:910:7: ident2= T_KS_AND_TN
					{
					ident2=(Token)match(input,T_KS_AND_TN,FOLLOW_T_KS_AND_TN_in_getTableID7609); 
					tableID = new String((ident2!=null?ident2.getText():null));
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
		return tableID;
	}
	// $ANTLR end "getTableID"



	// $ANTLR start "getTerm"
	// Meta.g:913:1: getTerm returns [Term term] : term1= getPartialTerm (| T_AT term2= getPartialTerm ) ;
	public final Term getTerm() throws RecognitionException {
		Term term = null;


		Term term1 =null;
		Term term2 =null;

		try {
			// Meta.g:913:28: (term1= getPartialTerm (| T_AT term2= getPartialTerm ) )
			// Meta.g:914:5: term1= getPartialTerm (| T_AT term2= getPartialTerm )
			{
			pushFollow(FOLLOW_getPartialTerm_in_getTerm7634);
			term1=getPartialTerm();
			state._fsp--;

			// Meta.g:914:26: (| T_AT term2= getPartialTerm )
			int alt105=2;
			int LA105_0 = input.LA(1);
			if ( (LA105_0==T_AND||(LA105_0 >= T_COLON && LA105_0 <= T_COMMA)||LA105_0==T_DISABLE||(LA105_0 >= T_END_BRACKET && LA105_0 <= T_END_SBRACKET)||LA105_0==T_GROUP||LA105_0==T_IF||LA105_0==T_LIMIT||LA105_0==T_ORDER||LA105_0==T_QUOTE||LA105_0==T_SEMICOLON||LA105_0==T_SINGLE_QUOTE||LA105_0==T_USING||LA105_0==T_WHERE||LA105_0==T_WITH) ) {
				alt105=1;
			}
			else if ( (LA105_0==T_AT) ) {
				alt105=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 105, 0, input);
				throw nvae;
			}

			switch (alt105) {
				case 1 :
					// Meta.g:914:28: 
					{
					term = term1;
					}
					break;
				case 2 :
					// Meta.g:915:5: T_AT term2= getPartialTerm
					{
					match(input,T_AT,FOLLOW_T_AT_in_getTerm7647); 
					pushFollow(FOLLOW_getPartialTerm_in_getTerm7651);
					term2=getPartialTerm();
					state._fsp--;

					term = new Term(term1.getTerm()+"@"+term2.getTerm());
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
		return term;
	}
	// $ANTLR end "getTerm"



	// $ANTLR start "getPartialTerm"
	// Meta.g:918:1: getPartialTerm returns [Term term] : (ident= T_IDENT |constant= T_CONSTANT | '1' | T_FALSE | T_TRUE |ksAndTn= T_KS_AND_TN |noIdent= T_TERM |path= T_PATH |qLiteral= QUOTED_LITERAL );
	public final Term getPartialTerm() throws RecognitionException {
		Term term = null;


		Token ident=null;
		Token constant=null;
		Token ksAndTn=null;
		Token noIdent=null;
		Token path=null;
		Token qLiteral=null;

		try {
			// Meta.g:918:35: (ident= T_IDENT |constant= T_CONSTANT | '1' | T_FALSE | T_TRUE |ksAndTn= T_KS_AND_TN |noIdent= T_TERM |path= T_PATH |qLiteral= QUOTED_LITERAL )
			int alt106=9;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt106=1;
				}
				break;
			case T_CONSTANT:
				{
				alt106=2;
				}
				break;
			case 149:
				{
				alt106=3;
				}
				break;
			case T_FALSE:
				{
				alt106=4;
				}
				break;
			case T_TRUE:
				{
				alt106=5;
				}
				break;
			case T_KS_AND_TN:
				{
				alt106=6;
				}
				break;
			case T_TERM:
				{
				alt106=7;
				}
				break;
			case T_PATH:
				{
				alt106=8;
				}
				break;
			case QUOTED_LITERAL:
				{
				alt106=9;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 106, 0, input);
				throw nvae;
			}
			switch (alt106) {
				case 1 :
					// Meta.g:919:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getPartialTerm7673); 
					term = new Term((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:920:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getPartialTerm7685); 
					term = new Term((constant!=null?constant.getText():null));
					}
					break;
				case 3 :
					// Meta.g:921:7: '1'
					{
					match(input,149,FOLLOW_149_in_getPartialTerm7695); 
					term = new Term("1");
					}
					break;
				case 4 :
					// Meta.g:922:7: T_FALSE
					{
					match(input,T_FALSE,FOLLOW_T_FALSE_in_getPartialTerm7705); 
					term = new Term("false");
					}
					break;
				case 5 :
					// Meta.g:923:7: T_TRUE
					{
					match(input,T_TRUE,FOLLOW_T_TRUE_in_getPartialTerm7715); 
					term = new Term("true");
					}
					break;
				case 6 :
					// Meta.g:924:7: ksAndTn= T_KS_AND_TN
					{
					ksAndTn=(Token)match(input,T_KS_AND_TN,FOLLOW_T_KS_AND_TN_in_getPartialTerm7727); 
					term = new Term((ksAndTn!=null?ksAndTn.getText():null));
					}
					break;
				case 7 :
					// Meta.g:925:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getPartialTerm7739); 
					term = new Term((noIdent!=null?noIdent.getText():null));
					}
					break;
				case 8 :
					// Meta.g:926:7: path= T_PATH
					{
					path=(Token)match(input,T_PATH,FOLLOW_T_PATH_in_getPartialTerm7752); 
					term = new Term((path!=null?path.getText():null));
					}
					break;
				case 9 :
					// Meta.g:927:7: qLiteral= QUOTED_LITERAL
					{
					qLiteral=(Token)match(input,QUOTED_LITERAL,FOLLOW_QUOTED_LITERAL_in_getPartialTerm7764); 
					term = new Term((qLiteral!=null?qLiteral.getText():null), true);
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
		return term;
	}
	// $ANTLR end "getPartialTerm"



	// $ANTLR start "getMapLiteral"
	// Meta.g:930:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		Term leftTerm1 =null;
		Term rightTerm1 =null;
		Term leftTermN =null;
		Term rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:933:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:934:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral7790); 
			// Meta.g:935:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt108=2;
			int LA108_0 = input.LA(1);
			if ( (LA108_0==QUOTED_LITERAL||LA108_0==T_CONSTANT||LA108_0==T_FALSE||LA108_0==T_IDENT||LA108_0==T_KS_AND_TN||LA108_0==T_PATH||LA108_0==T_TERM||LA108_0==T_TRUE||LA108_0==149) ) {
				alt108=1;
			}
			switch (alt108) {
				case 1 :
					// Meta.g:935:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral7800);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral7802); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral7806);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put(leftTerm1.getTerm(), rightTerm1.getTerm());
					// Meta.g:936:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop107:
					while (true) {
						int alt107=2;
						int LA107_0 = input.LA(1);
						if ( (LA107_0==T_COMMA) ) {
							alt107=1;
						}

						switch (alt107) {
						case 1 :
							// Meta.g:936:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral7815); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral7819);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral7821); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral7825);
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put(leftTermN.getTerm(), rightTermN.getTerm());
							}
							break;

						default :
							break loop107;
						}
					}

					}
					break;

			}

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral7837); 
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
	// Meta.g:940:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat | T_FALSE | T_TRUE | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |quotedLiteral= QUOTED_LITERAL );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Token quotedLiteral=null;
		Map<String, String> mapliteral =null;
		String number =null;


		        StringBuilder sb = new StringBuilder();
		    
		try {
			// Meta.g:943:6: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat | T_FALSE | T_TRUE | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |quotedLiteral= QUOTED_LITERAL )
			int alt109=9;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt109=1;
				}
				break;
			case T_CONSTANT:
				{
				alt109=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt109=3;
				}
				break;
			case T_FLOAT:
			case T_TERM:
				{
				alt109=4;
				}
				break;
			case T_FALSE:
				{
				alt109=5;
				}
				break;
			case T_TRUE:
				{
				alt109=6;
				}
				break;
			case T_COMPACT:
				{
				alt109=7;
				}
				break;
			case T_CLUSTERING:
				{
				alt109=8;
				}
				break;
			case QUOTED_LITERAL:
				{
				alt109=9;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 109, 0, input);
				throw nvae;
			}
			switch (alt109) {
				case 1 :
					// Meta.g:944:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty7867); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:945:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty7879); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:946:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty7891);
					mapliteral=getMapLiteral();
					state._fsp--;

					value = new MapLiteralProperty(mapliteral);
					}
					break;
				case 4 :
					// Meta.g:947:7: number= getFloat
					{
					pushFollow(FOLLOW_getFloat_in_getValueProperty7903);
					number=getFloat();
					state._fsp--;

					value = new FloatProperty(Float.parseFloat(number));
					}
					break;
				case 5 :
					// Meta.g:948:7: T_FALSE
					{
					match(input,T_FALSE,FOLLOW_T_FALSE_in_getValueProperty7913); 
					value = new BooleanProperty(false);
					}
					break;
				case 6 :
					// Meta.g:949:7: T_TRUE
					{
					match(input,T_TRUE,FOLLOW_T_TRUE_in_getValueProperty7923); 
					value = new BooleanProperty(true);
					}
					break;
				case 7 :
					// Meta.g:950:7: T_COMPACT T_STORAGE
					{
					match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getValueProperty7933); 
					match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getValueProperty7935); 
					value = new IdentifierProperty("COMPACT STORAGE");
					}
					break;
				case 8 :
					// Meta.g:951:7: T_CLUSTERING T_ORDER
					{
					match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getValueProperty7945); 
					match(input,T_ORDER,FOLLOW_T_ORDER_in_getValueProperty7947); 
					value = new IdentifierProperty("CLUSTERING ORDER");
					}
					break;
				case 9 :
					// Meta.g:952:7: quotedLiteral= QUOTED_LITERAL
					{
					quotedLiteral=(Token)match(input,QUOTED_LITERAL,FOLLOW_QUOTED_LITERAL_in_getValueProperty7959); 
					value = new QuotedLiteral((quotedLiteral!=null?quotedLiteral.getText():null));
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



	// $ANTLR start "getFloat"
	// Meta.g:956:1: getFloat returns [String floating] : (termToken= T_TERM |floatToken= T_FLOAT );
	public final String getFloat() throws RecognitionException {
		String floating = null;


		Token termToken=null;
		Token floatToken=null;

		try {
			// Meta.g:956:35: (termToken= T_TERM |floatToken= T_FLOAT )
			int alt110=2;
			int LA110_0 = input.LA(1);
			if ( (LA110_0==T_TERM) ) {
				alt110=1;
			}
			else if ( (LA110_0==T_FLOAT) ) {
				alt110=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 110, 0, input);
				throw nvae;
			}

			switch (alt110) {
				case 1 :
					// Meta.g:957:5: termToken= T_TERM
					{
					termToken=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getFloat7984); 
					floating =(termToken!=null?termToken.getText():null);
					}
					break;
				case 2 :
					// Meta.g:959:5: floatToken= T_FLOAT
					{
					floatToken=(Token)match(input,T_FLOAT,FOLLOW_T_FLOAT_in_getFloat8002); 
					floating =(floatToken!=null?floatToken.getText():null);
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
		return floating;
	}
	// $ANTLR end "getFloat"

	// Delegated rules



	public static final BitSet FOLLOW_T_DELETE_in_deleteStatement2087 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000840L});
	public static final BitSet FOLLOW_T_IDENT_in_deleteStatement2094 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_COMMA_in_deleteStatement2101 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_deleteStatement2105 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_FROM_in_deleteStatement2124 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_deleteStatement2129 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_WHERE_in_deleteStatement2134 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L,0x0000000000000001L});
	public static final BitSet FOLLOW_getRelation_in_deleteStatement2139 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_deleteStatement2144 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L,0x0000000000000001L});
	public static final BitSet FOLLOW_getRelation_in_deleteStatement2148 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_ADD_in_addStatement2167 = new BitSet(new long[]{0x0000000000000000L,0x0040800000000000L});
	public static final BitSet FOLLOW_set_in_addStatement2169 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L});
	public static final BitSet FOLLOW_T_PATH_in_addStatement2179 = new BitSet(new long[]{0x0000000000000000L,0x0040800000000000L});
	public static final BitSet FOLLOW_set_in_addStatement2181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIST_in_listStatement2204 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L,0x0000000000000042L});
	public static final BitSet FOLLOW_getListTypes_in_listStatement2209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_REMOVE_in_removeUDFStatement2230 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_UDF_in_removeUDFStatement2232 = new BitSet(new long[]{0x0000000000000000L,0x0040800000000000L});
	public static final BitSet FOLLOW_set_in_removeUDFStatement2234 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_removeUDFStatement2244 = new BitSet(new long[]{0x0000000000000000L,0x0040800000000000L});
	public static final BitSet FOLLOW_set_in_removeUDFStatement2248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropIndexStatement2274 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_INDEX_in_dropIndexStatement2276 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
	public static final BitSet FOLLOW_T_IF_in_dropIndexStatement2280 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropIndexStatement2282 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_dropIndexStatement2291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createIndexStatement2316 = new BitSet(new long[]{0x0028000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_getIndexType_in_createIndexStatement2320 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_INDEX_in_createIndexStatement2324 = new BitSet(new long[]{0x0000000000000000L,0x0000002000001800L});
	public static final BitSet FOLLOW_T_IF_in_createIndexStatement2328 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_T_NOT_in_createIndexStatement2330 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXISTS_in_createIndexStatement2332 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2342 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_T_ON_in_createIndexStatement2350 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_createIndexStatement2354 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createIndexStatement2359 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2364 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createIndexStatement2370 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2376 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createIndexStatement2385 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000002200L});
	public static final BitSet FOLLOW_T_USING_in_createIndexStatement2389 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_createIndexStatement2393 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_WITH_in_createIndexStatement2401 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_createIndexStatement2403 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2407 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement2409 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement2413 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_createIndexStatement2420 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2424 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement2426 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement2430 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_UPDATE_in_updateTableStatement2471 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_updateTableStatement2475 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_USING_in_updateTableStatement2482 = new BitSet(new long[]{0x0000480000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getOption_in_updateTableStatement2486 = new BitSet(new long[]{0x0000480000000000L,0x0020000000000800L});
	public static final BitSet FOLLOW_getOption_in_updateTableStatement2493 = new BitSet(new long[]{0x0000480000000000L,0x0020000000000800L});
	public static final BitSet FOLLOW_T_SET_in_updateTableStatement2505 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getAssignment_in_updateTableStatement2509 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_COMMA_in_updateTableStatement2514 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getAssignment_in_updateTableStatement2518 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_WHERE_in_updateTableStatement2528 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L,0x0000000000000001L});
	public static final BitSet FOLLOW_getRelation_in_updateTableStatement2532 = new BitSet(new long[]{0x0000000400000002L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_AND_in_updateTableStatement2537 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L,0x0000000000000001L});
	public static final BitSet FOLLOW_getRelation_in_updateTableStatement2541 = new BitSet(new long[]{0x0000000400000002L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_IF_in_updateTableStatement2552 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_updateTableStatement2556 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_updateTableStatement2558 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_updateTableStatement2562 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_updateTableStatement2588 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_updateTableStatement2592 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_updateTableStatement2594 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_updateTableStatement2598 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_STOP_in_stopProcessStatement2630 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_T_PROCESS_in_stopProcessStatement2632 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_stopProcessStatement2636 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTriggerStatement2658 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropTriggerStatement2665 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement2669 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_T_ON_in_dropTriggerStatement2676 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement2685 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTriggerStatement2713 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRIGGER_in_createTriggerStatement2720 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2724 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_T_ON_in_createTriggerStatement2731 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2740 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_USING_in_createTriggerStatement2746 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2750 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTableStatement2789 = new BitSet(new long[]{0x0000000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_createTableStatement2795 = new BitSet(new long[]{0x0000000000000000L,0x0000000000401800L});
	public static final BitSet FOLLOW_T_IF_in_createTableStatement2802 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_T_NOT_in_createTableStatement2804 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXISTS_in_createTableStatement2806 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_createTableStatement2819 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement2825 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100800L});
	public static final BitSet FOLLOW_set_in_createTableStatement2859 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getDataType_in_createTableStatement2869 = new BitSet(new long[]{0x2000200000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2872 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2874 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement2923 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100800L});
	public static final BitSet FOLLOW_set_in_createTableStatement2927 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getDataType_in_createTableStatement2937 = new BitSet(new long[]{0x2000200000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2940 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2942 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3000 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement3002 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement3004 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement3006 = new BitSet(new long[]{0x0000000000000000L,0x0100000000100800L});
	public static final BitSet FOLLOW_set_in_createTableStatement3068 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3139 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100800L});
	public static final BitSet FOLLOW_set_in_createTableStatement3143 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement3248 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100800L});
	public static final BitSet FOLLOW_set_in_createTableStatement3252 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3299 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100800L});
	public static final BitSet FOLLOW_set_in_createTableStatement3303 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3347 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3383 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100800L});
	public static final BitSet FOLLOW_set_in_createTableStatement3387 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3479 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3551 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_WITH_in_createTableStatement3554 = new BitSet(new long[]{0x8000480000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getMetaProperties_in_createTableStatement3560 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement3623 = new BitSet(new long[]{0x0000000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_alterTableStatement3629 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_alterTableStatement3637 = new BitSet(new long[]{0x0400000120000000L,0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement3644 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3648 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_TYPE_in_alterTableStatement3650 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ADD_in_alterTableStatement3667 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3671 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_alterTableStatement3688 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_WITH_in_alterTableStatement3705 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3722 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement3724 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement3728 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_alterTableStatement3745 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3749 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement3751 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement3755 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_SELECT_in_selectStatement3810 = new BitSet(new long[]{0x0202014040000000L,0x0000000300400800L});
	public static final BitSet FOLLOW_getSelectClause_in_selectStatement3814 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_FROM_in_selectStatement3816 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_selectStatement3820 = new BitSet(new long[]{0x0100000000000002L,0x0000010002008080L,0x0000000000002800L});
	public static final BitSet FOLLOW_T_WITH_in_selectStatement3828 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_WINDOW_in_selectStatement3830 = new BitSet(new long[]{0x0001000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_getWindow_in_selectStatement3836 = new BitSet(new long[]{0x0100000000000002L,0x0000010002008080L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_INNER_in_selectStatement3849 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_T_JOIN_in_selectStatement3851 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_selectStatement3857 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_T_ON_in_selectStatement3859 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getFields_in_selectStatement3863 = new BitSet(new long[]{0x0100000000000002L,0x0000010002000080L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_WHERE_in_selectStatement3872 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L,0x0000000000000001L});
	public static final BitSet FOLLOW_getWhereClauses_in_selectStatement3878 = new BitSet(new long[]{0x0100000000000002L,0x0000010002000080L});
	public static final BitSet FOLLOW_T_ORDER_in_selectStatement3887 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_BY_in_selectStatement3889 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getOrdering_in_selectStatement3895 = new BitSet(new long[]{0x0100000000000002L,0x0000000002000080L});
	public static final BitSet FOLLOW_T_GROUP_in_selectStatement3904 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_BY_in_selectStatement3906 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getList_in_selectStatement3912 = new BitSet(new long[]{0x0100000000000002L,0x0000000002000000L});
	public static final BitSet FOLLOW_T_LIMIT_in_selectStatement3921 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_T_CONSTANT_in_selectStatement3927 = new BitSet(new long[]{0x0100000000000002L});
	public static final BitSet FOLLOW_T_DISABLE_in_selectStatement3936 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_selectStatement3938 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INSERT_in_insertIntoStatement3971 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_T_INTO_in_insertIntoStatement3978 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_insertIntoStatement3987 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3993 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement4002 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement4012 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement4016 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement4027 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_selectStatement_in_insertIntoStatement4046 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_VALUES_in_insertIntoStatement4069 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement4079 = new BitSet(new long[]{0x0001000002000000L,0x4200020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement4096 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement4113 = new BitSet(new long[]{0x0001000002000000L,0x4200020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement4117 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement4131 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_IF_in_insertIntoStatement4144 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_T_NOT_in_insertIntoStatement4146 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXISTS_in_insertIntoStatement4148 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_USING_in_insertIntoStatement4169 = new BitSet(new long[]{0x0000480000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement4184 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_insertIntoStatement4197 = new BitSet(new long[]{0x0000480000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement4201 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement4238 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement4240 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement4242 = new BitSet(new long[]{0x0444000120000000L,0x0429000004010004L,0x0000000000000188L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement4246 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement4280 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement4282 = new BitSet(new long[]{0x0000800200000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement4294 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4296 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement4299 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement4302 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement4317 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement4319 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4321 = new BitSet(new long[]{0x0800000880000000L,0x8000404018000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement4336 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement4355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement4373 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement4391 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement4409 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement4427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4463 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4481 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement4531 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4533 = new BitSet(new long[]{0x0800000880000000L,0x8000404018000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement4549 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement4568 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement4586 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement4604 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement4622 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement4640 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4658 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4676 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4694 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement4722 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement4724 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4726 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement4729 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement4732 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement4782 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement4790 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement4815 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement4821 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement4828 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement4830 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement4842 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement4871 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement4877 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4885 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement4891 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4899 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement4901 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement4905 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement4914 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4918 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement4920 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement4924 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement4958 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement4960 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement4967 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement4969 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement4971 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement4983 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement4989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement5001 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement5003 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement5007 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement5016 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement5020 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement5022 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement5026 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement5059 = new BitSet(new long[]{0x0000000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement5065 = new BitSet(new long[]{0x0000000000000000L,0x0000000000401800L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement5072 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement5074 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement5086 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement5101 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement5114 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTableStatement_in_metaStatement5134 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterTableStatement_in_metaStatement5147 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTriggerStatement_in_metaStatement5160 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTriggerStatement_in_metaStatement5173 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stopProcessStatement_in_metaStatement5187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_updateTableStatement_in_metaStatement5201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_selectStatement_in_metaStatement5215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_insertIntoStatement_in_metaStatement5229 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement5243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement5257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement5271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement5285 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement5299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement5313 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement5327 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement5341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createIndexStatement_in_metaStatement5355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropIndexStatement_in_metaStatement5370 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_listStatement_in_metaStatement5385 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_addStatement_in_metaStatement5400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_removeUDFStatement_in_metaStatement5415 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_deleteStatement_in_metaStatement5430 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query5453 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query5456 = new BitSet(new long[]{0x0000000000000000L,0x0010000000000000L});
	public static final BitSet FOLLOW_EOF_in_query5460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DEFAULT_in_getIndexType5483 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LUCENE_in_getIndexType5493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CUSTOM_in_getIndexType5503 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMetaProperty_in_getMetaProperties5536 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_getMetaProperties5545 = new BitSet(new long[]{0x8000480000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getMetaProperty_in_getMetaProperties5549 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getMetaProperty5580 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_getMetaProperty5582 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_getMetaProperty5586 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COMPACT_in_getMetaProperty5597 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_T_STORAGE_in_getMetaProperty5599 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getMetaProperty5609 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getMetaProperty5611 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_BY_in_getMetaProperty5613 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getMetaProperty5615 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getOrdering_in_getMetaProperty5619 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getMetaProperty5623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EPHEMERAL_in_getMetaProperty5632 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_getMetaProperty5638 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_FALSE_in_getMetaProperty5641 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_getMetaProperty5647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getDataType5680 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
	public static final BitSet FOLLOW_T_LT_in_getDataType5683 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_getDataType5687 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_T_COMMA_in_getDataType5690 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_getDataType5694 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_T_GT_in_getDataType5698 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOrdering5738 = new BitSet(new long[]{0x0080202000000002L});
	public static final BitSet FOLLOW_T_ASC_in_getOrdering5743 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_DESC_in_getOrdering5749 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getOrdering5762 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_getOrdering5766 = new BitSet(new long[]{0x0080202000000002L});
	public static final BitSet FOLLOW_T_ASC_in_getOrdering5771 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_DESC_in_getOrdering5777 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getRelation_in_getWhereClauses5811 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_getWhereClauses5816 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L,0x0000000000000001L});
	public static final BitSet FOLLOW_getRelation_in_getWhereClauses5820 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_getTableID_in_getFields5850 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_getFields5852 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_getFields5856 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_getFields5867 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_getFields5869 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400800L});
	public static final BitSet FOLLOW_getTableID_in_getFields5873 = new BitSet(new long[]{0x0000000000000002L,0x0000000000400800L});
	public static final BitSet FOLLOW_T_LAST_in_getWindow5895 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getWindow5908 = new BitSet(new long[]{0x0010000000000000L,0x0006000400000400L,0x000000003FC00000L});
	public static final BitSet FOLLOW_T_ROWS_in_getWindow5911 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTimeUnit_in_getWindow5943 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_153_in_getTimeUnit5993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_152_in_getTimeUnit6003 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_151_in_getTimeUnit6013 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_150_in_getTimeUnit6023 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_157_in_getTimeUnit6033 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_156_in_getTimeUnit6043 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_155_in_getTimeUnit6053 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_154_in_getTimeUnit6063 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SECONDS_in_getTimeUnit6073 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_MINUTES_in_getTimeUnit6083 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_HOURS_in_getTimeUnit6093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DAYS_in_getTimeUnit6103 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelectionCount_in_getSelectClause6129 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelectionList_in_getSelectClause6141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COUNT_in_getSelectionCount6167 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelectionCount6169 = new BitSet(new long[]{0x0000004000000000L,0x0000000000000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_ASTERISK_in_getSelectionCount6173 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_149_in_getSelectionCount6177 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelectionCount6183 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelectionCount6190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelectionCount6196 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DISTINCT_in_getSelectionList6231 = new BitSet(new long[]{0x0002014040000000L,0x0000000300400800L});
	public static final BitSet FOLLOW_getSelection_in_getSelectionList6239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ASTERISK_in_getSelection6279 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelector_in_getSelection6302 = new BitSet(new long[]{0x0000201000000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelection6307 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelection6311 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelection6332 = new BitSet(new long[]{0x0002010040000000L,0x0000000300400800L});
	public static final BitSet FOLLOW_getSelector_in_getSelection6336 = new BitSet(new long[]{0x0000201000000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelection6341 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelection6345 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_AGGREGATION_in_getSelector6400 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_MAX_in_getSelector6414 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_MIN_in_getSelector6428 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_AVG_in_getSelector6442 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_COUNT_in_getSelector6456 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelector6482 = new BitSet(new long[]{0x2002010040000000L,0x0000000300400800L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6504 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelector6509 = new BitSet(new long[]{0x0002010040000000L,0x0000000300400800L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6513 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelector6534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTableID_in_getSelector6550 = new BitSet(new long[]{0x0000000000000002L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelector6582 = new BitSet(new long[]{0x2002010040000000L,0x0000000300400800L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6587 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelector6592 = new BitSet(new long[]{0x0002010040000000L,0x0000000300400800L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6596 = new BitSet(new long[]{0x2000200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelector6621 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_getListTypes6660 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getAssignment6691 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_getAssignment6703 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getValueAssign_in_getAssignment6707 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_getAssignment6726 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getAssignment6730 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_getAssignment6732 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_getAssignment6734 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getAssignment6738 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getValueAssign6765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueAssign6777 = new BitSet(new long[]{0x0000000000000000L,0x1000080000000000L});
	public static final BitSet FOLLOW_T_PLUS_in_getValueAssign6780 = new BitSet(new long[]{0x0001000000000000L,0x0280000000000000L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getValueAssign6783 = new BitSet(new long[]{0x0000000000000000L,0x0200000000000000L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueAssign6787 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getValueAssign6789 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getIntSetOrList_in_getValueAssign6829 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SUBTRACT_in_getValueAssign6873 = new BitSet(new long[]{0x0001000000000000L,0x0280000000000000L});
	public static final BitSet FOLLOW_getIntSetOrList_in_getValueAssign6877 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getIntSetOrList6903 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_getIntSetOrList6913 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getList_in_getIntSetOrList6917 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_getIntSetOrList6919 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getIntSetOrList6929 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getSet_in_getIntSetOrList6933 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getIntSetOrList6935 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TOKEN_in_getRelation6953 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation6955 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_getIds_in_getRelation6959 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation6961 = new BitSet(new long[]{0x0000000000000000L,0x0000001061000301L});
	public static final BitSet FOLLOW_getComparator_in_getRelation6965 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200005L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TOKEN_in_getRelation7004 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation7006 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerms_in_getRelation7010 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation7012 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getRelation7025 = new BitSet(new long[]{0x0000020000000000L,0x0000001061002301L});
	public static final BitSet FOLLOW_getComparator_in_getRelation7031 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getRelation7035 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IN_in_getRelation7061 = new BitSet(new long[]{0x0000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation7063 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerms_in_getRelation7067 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation7069 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_BETWEEN_in_getRelation7095 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getRelation7099 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_T_AND_in_getRelation7101 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getRelation7105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EQUAL_in_getComparator7145 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_GT_in_getComparator7155 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LT_in_getComparator7165 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_GTE_in_getComparator7175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LTE_in_getComparator7186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_NOT_EQUAL_in_getComparator7196 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIKE_in_getComparator7207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getIds7235 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getIds7240 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_IDENT_in_getIds7244 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getOption_in_getOptions7269 = new BitSet(new long[]{0x0000480000000002L,0x0000000000000800L});
	public static final BitSet FOLLOW_getOption_in_getOptions7276 = new BitSet(new long[]{0x0000480000000002L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_COMPACT_in_getOption7296 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_T_STORAGE_in_getOption7298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getOption7308 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getOption7310 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOption7322 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_EQUAL_in_getOption7324 = new BitSet(new long[]{0x0001480002000000L,0x4200000000000818L,0x0000000000000004L});
	public static final BitSet FOLLOW_getValueProperty_in_getOption7328 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getList7356 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getList7365 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getList7369 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getTerm_in_getTerms7403 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getTerms7412 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getTerms7416 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getTerm_in_getSet7450 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getSet7459 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getSet7463 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7497 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getTermOrLiteral7511 = new BitSet(new long[]{0x4001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7529 = new BitSet(new long[]{0x4000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getTermOrLiteral7542 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7546 = new BitSet(new long[]{0x4000200000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getTermOrLiteral7563 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID7593 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_KS_AND_TN_in_getTableID7609 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getPartialTerm_in_getTerm7634 = new BitSet(new long[]{0x0000008000000002L});
	public static final BitSet FOLLOW_T_AT_in_getTerm7647 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getPartialTerm_in_getTerm7651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getPartialTerm7673 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getPartialTerm7685 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_149_in_getPartialTerm7695 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_getPartialTerm7705 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_getPartialTerm7715 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_KS_AND_TN_in_getPartialTerm7727 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getPartialTerm7739 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_PATH_in_getPartialTerm7752 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUOTED_LITERAL_in_getPartialTerm7764 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral7790 = new BitSet(new long[]{0x4001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7800 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral7802 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7806 = new BitSet(new long[]{0x4000200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral7815 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7819 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral7821 = new BitSet(new long[]{0x0001000002000000L,0x4000020000400808L,0x0000000000200004L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7825 = new BitSet(new long[]{0x4000200000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral7837 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty7867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty7879 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty7891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getFloat_in_getValueProperty7903 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_getValueProperty7913 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_getValueProperty7923 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COMPACT_in_getValueProperty7933 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_T_STORAGE_in_getValueProperty7935 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getValueProperty7945 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getValueProperty7947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUOTED_LITERAL_in_getValueProperty7959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getFloat7984 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FLOAT_in_getFloat8002 = new BitSet(new long[]{0x0000000000000002L});
}
