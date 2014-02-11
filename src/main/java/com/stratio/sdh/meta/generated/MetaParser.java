// $ANTLR 3.5.1 Meta.g 2014-02-11 16:01:14

    package com.stratio.sdh.meta.generated;    
    import com.stratio.sdh.meta.statements.Statement;
    import com.stratio.sdh.meta.statements.AlterKeyspaceStatement;
    import com.stratio.sdh.meta.statements.CreateKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropTableStatement;
    import com.stratio.sdh.meta.statements.ExplainPlanStatement;
    import com.stratio.sdh.meta.statements.SetOptionsStatement;
    import com.stratio.sdh.meta.statements.StopProcessStatement;
    import com.stratio.sdh.meta.statements.DropTriggerStatement;
    import com.stratio.sdh.meta.statements.CreateTriggerStatement;
    import com.stratio.sdh.meta.statements.TruncateStatement;
    import com.stratio.sdh.meta.statements.UseStatement;
    import com.stratio.sdh.meta.statements.AlterTableStatement;
    import com.stratio.sdh.meta.statements.CreateTableStatement;
    import com.stratio.sdh.meta.structures.Consistency;
    import com.stratio.sdh.meta.structures.ConstantProperty;
    import com.stratio.sdh.meta.structures.IdentifierProperty;
    import com.stratio.sdh.meta.structures.MapLiteralProperty;
    import com.stratio.sdh.meta.structures.ValueProperty;
    import com.stratio.sdh.meta.structures.FloatProperty;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.LinkedHashMap;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "DIGIT", "E", 
		"EXPONENT", "F", "G", "H", "I", "J", "K", "L", "LETTER", "M", "N", "O", 
		"P", "Q", "R", "S", "T", "T_ADD", "T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", 
		"T_ANY", "T_COLON", "T_COMMA", "T_CONSISTENCY", "T_CONSTANT", "T_CREATE", 
		"T_DROP", "T_EACH_QUORUM", "T_END_SBRACKET", "T_EQUAL", "T_EXISTS", "T_EXPLAIN", 
		"T_FALSE", "T_FLOAT", "T_FOR", "T_IDENT", "T_IF", "T_KEY", "T_KEYSPACE", 
		"T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_NOT", "T_ON", "T_ONE", "T_OPTIONS", 
		"T_PLAN", "T_POINT", "T_PRIMARY", "T_PROCESS", "T_QUORUM", "T_SEMICOLON", 
		"T_SET", "T_START_SBRACKET", "T_STOP", "T_TABLE", "T_TERM", "T_THREE", 
		"T_TRIGGER", "T_TRUE", "T_TRUNCATE", "T_TWO", "T_TYPE", "T_USE", "T_USING", 
		"T_WITH", "U", "V", "W", "WS", "X", "Y", "Z", "'('", "')'"
	};
	public static final int EOF=-1;
	public static final int T__84=84;
	public static final int T__85=85;
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
	public static final int Q=23;
	public static final int R=24;
	public static final int S=25;
	public static final int T=26;
	public static final int T_ADD=27;
	public static final int T_ALL=28;
	public static final int T_ALTER=29;
	public static final int T_ANALYTICS=30;
	public static final int T_AND=31;
	public static final int T_ANY=32;
	public static final int T_COLON=33;
	public static final int T_COMMA=34;
	public static final int T_CONSISTENCY=35;
	public static final int T_CONSTANT=36;
	public static final int T_CREATE=37;
	public static final int T_DROP=38;
	public static final int T_EACH_QUORUM=39;
	public static final int T_END_SBRACKET=40;
	public static final int T_EQUAL=41;
	public static final int T_EXISTS=42;
	public static final int T_EXPLAIN=43;
	public static final int T_FALSE=44;
	public static final int T_FLOAT=45;
	public static final int T_FOR=46;
	public static final int T_IDENT=47;
	public static final int T_IF=48;
	public static final int T_KEY=49;
	public static final int T_KEYSPACE=50;
	public static final int T_LOCAL_ONE=51;
	public static final int T_LOCAL_QUORUM=52;
	public static final int T_NOT=53;
	public static final int T_ON=54;
	public static final int T_ONE=55;
	public static final int T_OPTIONS=56;
	public static final int T_PLAN=57;
	public static final int T_POINT=58;
	public static final int T_PRIMARY=59;
	public static final int T_PROCESS=60;
	public static final int T_QUORUM=61;
	public static final int T_SEMICOLON=62;
	public static final int T_SET=63;
	public static final int T_START_SBRACKET=64;
	public static final int T_STOP=65;
	public static final int T_TABLE=66;
	public static final int T_TERM=67;
	public static final int T_THREE=68;
	public static final int T_TRIGGER=69;
	public static final int T_TRUE=70;
	public static final int T_TRUNCATE=71;
	public static final int T_TWO=72;
	public static final int T_TYPE=73;
	public static final int T_USE=74;
	public static final int T_USING=75;
	public static final int T_WITH=76;
	public static final int U=77;
	public static final int V=78;
	public static final int W=79;
	public static final int WS=80;
	public static final int X=81;
	public static final int Y=82;
	public static final int Z=83;

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
	// Meta.g:169:1: stopProcessStatement returns [StopProcessStatement stprst] : T_STOP T_PROCESS ident= T_IDENT ;
	public final StopProcessStatement stopProcessStatement() throws RecognitionException {
		StopProcessStatement stprst = null;


		Token ident=null;

		try {
			// Meta.g:169:59: ( T_STOP T_PROCESS ident= T_IDENT )
			// Meta.g:170:5: T_STOP T_PROCESS ident= T_IDENT
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
	// Meta.g:174:1: dropTriggerStatement returns [DropTriggerStatement drtrst] : T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT ;
	public final DropTriggerStatement dropTriggerStatement() throws RecognitionException {
		DropTriggerStatement drtrst = null;


		Token ident=null;
		Token ident2=null;

		try {
			// Meta.g:174:59: ( T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT )
			// Meta.g:175:5: T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT
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
	// Meta.g:182:1: createTriggerStatement returns [CreateTriggerStatement crtrst] : T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT ;
	public final CreateTriggerStatement createTriggerStatement() throws RecognitionException {
		CreateTriggerStatement crtrst = null;


		Token trigger_name=null;
		Token table_name=null;
		Token class_name=null;

		try {
			// Meta.g:182:63: ( T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT )
			// Meta.g:183:5: T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT
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



	// $ANTLR start "createTableStatement"
	// Meta.g:191:1: createTableStatement returns [CreateTableStatement crtast] : T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= T_IDENT '(' (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* ) ')' ( T_WITH )? (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )? ;
	public final CreateTableStatement createTableStatement() throws RecognitionException {
		CreateTableStatement crtast = null;


		Token name_table=null;
		Token ident_column1=null;
		Token type1=null;
		Token ident_columN=null;
		Token typeN=null;
		Token primaryK=null;
		Token partitionKN=null;
		Token partitionK=null;
		Token clusterKN=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


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
			// Meta.g:204:6: ( T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= T_IDENT '(' (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* ) ')' ( T_WITH )? (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )? )
			// Meta.g:207:5: T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= T_IDENT '(' (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* ) ')' ( T_WITH )? (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTableStatement1343); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_createTableStatement1349); 
			// Meta.g:209:5: ( T_IF T_NOT T_EXISTS )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==T_IF) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// Meta.g:209:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createTableStatement1356); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createTableStatement1358); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createTableStatement1360); 
					ifNotExists_2 = true;
					}
					break;

			}

			name_table=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1373); 
			match(input,84,FOLLOW_84_in_createTableStatement1379); 
			// Meta.g:211:9: (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* )
			// Meta.g:213:17: ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )*
			{
			ident_column1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1414); 
			type1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1418); 
			// Meta.g:213:53: ( T_PRIMARY T_KEY )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==T_PRIMARY) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// Meta.g:213:54: T_PRIMARY T_KEY
					{
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement1421); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement1423); 
					}
					break;

			}

			columns.put((ident_column1!=null?ident_column1.getText():null),(type1!=null?type1.getText():null)); Type_Primary_Key=1;
			// Meta.g:214:17: ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )*
			loop8:
			while (true) {
				int alt8=3;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==T_COMMA) ) {
					int LA8_2 = input.LA(2);
					if ( (LA8_2==T_IDENT) ) {
						alt8=1;
					}
					else if ( (LA8_2==T_PRIMARY) ) {
						alt8=2;
					}

				}

				switch (alt8) {
				case 1 :
					// Meta.g:215:21: ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? )
					{
					// Meta.g:215:21: ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? )
					// Meta.g:215:23: ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )?
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement1472); 
					ident_columN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1476); 
					typeN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1480); 
					// Meta.g:215:62: ( T_PRIMARY T_KEY )?
					int alt3=2;
					int LA3_0 = input.LA(1);
					if ( (LA3_0==T_PRIMARY) ) {
						alt3=1;
					}
					switch (alt3) {
						case 1 :
							// Meta.g:215:63: T_PRIMARY T_KEY
							{
							match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement1483); 
							match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement1485); 
							Type_Primary_Key=2;columnNumberPK=columnNumberPK_inter +1;
							}
							break;

					}

					columns.put((ident_columN!=null?ident_columN.getText():null),(typeN!=null?typeN.getText():null));columnNumberPK_inter+=1;
					}

					}
					break;
				case 2 :
					// Meta.g:216:22: ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' )
					{
					// Meta.g:216:22: ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' )
					// Meta.g:217:25: ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')'
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement1543); 
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement1545); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement1547); 
					match(input,84,FOLLOW_84_in_createTableStatement1549); 
					// Meta.g:218:25: ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) )
					int alt7=2;
					int LA7_0 = input.LA(1);
					if ( (LA7_0==T_IDENT) ) {
						alt7=1;
					}
					else if ( (LA7_0==84) ) {
						alt7=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 7, 0, input);
						throw nvae;
					}

					switch (alt7) {
						case 1 :
							// Meta.g:219:29: (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* )
							{
							// Meta.g:219:29: (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* )
							// Meta.g:219:33: primaryK= T_IDENT ( ',' partitionKN= T_IDENT )*
							{
							primaryK=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1611); 
							primaryKey.add((primaryK!=null?primaryK.getText():null));Type_Primary_Key=3;
							// Meta.g:221:33: ( ',' partitionKN= T_IDENT )*
							loop4:
							while (true) {
								int alt4=2;
								int LA4_0 = input.LA(1);
								if ( (LA4_0==T_COMMA) ) {
									alt4=1;
								}

								switch (alt4) {
								case 1 :
									// Meta.g:221:34: ',' partitionKN= T_IDENT
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement1676); 
									partitionKN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1679); 
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop4;
								}
							}

							}

							}
							break;
						case 2 :
							// Meta.g:223:30: ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* )
							{
							// Meta.g:223:30: ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* )
							// Meta.g:224:33: '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )*
							{
							match(input,84,FOLLOW_84_in_createTableStatement1778); 
							partitionK=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1782); 
							primaryKey.add((partitionK!=null?partitionK.getText():null));Type_Primary_Key=4;
							// Meta.g:225:37: ( ',' partitionKN= T_IDENT )*
							loop5:
							while (true) {
								int alt5=2;
								int LA5_0 = input.LA(1);
								if ( (LA5_0==T_COMMA) ) {
									alt5=1;
								}

								switch (alt5) {
								case 1 :
									// Meta.g:225:38: ',' partitionKN= T_IDENT
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement1823); 
									partitionKN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1826); 
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop5;
								}
							}

							match(input,85,FOLLOW_85_in_createTableStatement1864); 
							// Meta.g:227:33: ( ',' clusterKN= T_IDENT )*
							loop6:
							while (true) {
								int alt6=2;
								int LA6_0 = input.LA(1);
								if ( (LA6_0==T_COMMA) ) {
									alt6=1;
								}

								switch (alt6) {
								case 1 :
									// Meta.g:227:34: ',' clusterKN= T_IDENT
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement1900); 
									clusterKN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement1904); 
									clusterKey.add((clusterKN!=null?clusterKN.getText():null));withClusterKey=true;
									}
									break;

								default :
									break loop6;
								}
							}

							}

							}
							break;

					}

					match(input,85,FOLLOW_85_in_createTableStatement1990); 
					}

					}
					break;

				default :
					break loop8;
				}
			}

			}

			match(input,85,FOLLOW_85_in_createTableStatement2063); 
			// Meta.g:236:9: ( T_WITH )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==T_WITH) ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// Meta.g:236:9: T_WITH
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createTableStatement2065); 
					}
					break;

			}

			// Meta.g:237:5: (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==T_IDENT) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// Meta.g:237:7: identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					{
					identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2076); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createTableStatement2078); 
					pushFollow(FOLLOW_getValueProperty_in_createTableStatement2082);
					valueProp1=getValueProperty();
					state._fsp--;

					propierties.put((identProp1!=null?identProp1.getText():null), valueProp1);withPropierties=true;
					// Meta.g:238:13: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					loop10:
					while (true) {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( (LA10_0==T_AND) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// Meta.g:238:14: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_createTableStatement2099); 
							identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2103); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createTableStatement2105); 
							pushFollow(FOLLOW_getValueProperty_in_createTableStatement2109);
							valuePropN=getValueProperty();
							state._fsp--;

							propierties.put((identPropN!=null?identPropN.getText():null), valuePropN);withPropierties=true;
							}
							break;

						default :
							break loop10;
						}
					}

					}
					break;

			}

			crtast = new CreateTableStatement((name_table!=null?name_table.getText():null),columns,primaryKey,clusterKey,propierties,Type_Primary_Key,ifNotExists_2,withClusterKey,columnNumberPK,withPropierties);  
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
	// Meta.g:243:1: alterTableStatement returns [AlterTableStatement altast] : T_ALTER T_TABLE name_table= T_IDENT ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) ;
	public final AlterTableStatement alterTableStatement() throws RecognitionException {
		AlterTableStatement altast = null;


		Token name_table=null;
		Token column=null;
		Token type=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        LinkedHashMap<String, ValueProperty> option = new LinkedHashMap<>();
		        int prop= 0;
		    
		try {
			// Meta.g:247:6: ( T_ALTER T_TABLE name_table= T_IDENT ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) )
			// Meta.g:249:5: T_ALTER T_TABLE name_table= T_IDENT ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement2174); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_alterTableStatement2180); 
			name_table=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2188); 
			// Meta.g:252:5: ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			int alt13=4;
			switch ( input.LA(1) ) {
			case T_ALTER:
				{
				alt13=1;
				}
				break;
			case T_ADD:
				{
				alt13=2;
				}
				break;
			case T_DROP:
				{
				alt13=3;
				}
				break;
			case T_WITH:
				{
				alt13=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}
			switch (alt13) {
				case 1 :
					// Meta.g:252:6: T_ALTER column= T_IDENT T_TYPE type= T_IDENT
					{
					match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement2195); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2199); 
					match(input,T_TYPE,FOLLOW_T_TYPE_in_alterTableStatement2201); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2205); 
					prop=1;
					}
					break;
				case 2 :
					// Meta.g:253:10: T_ADD column= T_IDENT type= T_IDENT
					{
					match(input,T_ADD,FOLLOW_T_ADD_in_alterTableStatement2218); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2222); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2226); 
					prop=2;
					}
					break;
				case 3 :
					// Meta.g:254:10: T_DROP column= T_IDENT
					{
					match(input,T_DROP,FOLLOW_T_DROP_in_alterTableStatement2239); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2243); 
					prop=3;
					}
					break;
				case 4 :
					// Meta.g:255:10: T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_alterTableStatement2256); 
					identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2273); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement2275); 
					pushFollow(FOLLOW_getValueProperty_in_alterTableStatement2279);
					valueProp1=getValueProperty();
					state._fsp--;

					option.put((identProp1!=null?identProp1.getText():null), valueProp1);
					// Meta.g:257:13: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					loop12:
					while (true) {
						int alt12=2;
						int LA12_0 = input.LA(1);
						if ( (LA12_0==T_AND) ) {
							alt12=1;
						}

						switch (alt12) {
						case 1 :
							// Meta.g:257:14: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_alterTableStatement2296); 
							identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement2300); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement2302); 
							pushFollow(FOLLOW_getValueProperty_in_alterTableStatement2306);
							valuePropN=getValueProperty();
							state._fsp--;

							option.put((identPropN!=null?identPropN.getText():null), valuePropN);
							}
							break;

						default :
							break loop12;
						}
					}

					prop=4;
					}
					break;

			}

			altast = new AlterTableStatement((name_table!=null?name_table.getText():null),(column!=null?column.getText():null),(type!=null?type.getText():null),option,prop);  
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



	// $ANTLR start "explainPlanStatement"
	// Meta.g:263:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		Statement parsedStmnt =null;

		try {
			// Meta.g:263:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:264:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement2357); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement2359); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement2361); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement2365);
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
	// Meta.g:268:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:275:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:276:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement2399); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement2401); 
			// Meta.g:276:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt20=2;
			int LA20_0 = input.LA(1);
			if ( (LA20_0==T_ANALYTICS) ) {
				alt20=1;
			}
			else if ( (LA20_0==T_CONSISTENCY) ) {
				alt20=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 20, 0, input);
				throw nvae;
			}

			switch (alt20) {
				case 1 :
					// Meta.g:277:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement2413); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement2415); 
					// Meta.g:277:29: ( T_TRUE | T_FALSE )
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==T_TRUE) ) {
						alt14=1;
					}
					else if ( (LA14_0==T_FALSE) ) {
						alt14=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 14, 0, input);
						throw nvae;
					}

					switch (alt14) {
						case 1 :
							// Meta.g:277:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement2418); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:277:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement2421); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:278:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt16=2;
					int LA16_0 = input.LA(1);
					if ( (LA16_0==T_AND) ) {
						alt16=1;
					}
					switch (alt16) {
						case 1 :
							// Meta.g:278:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement2436); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement2438); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement2440); 
							// Meta.g:279:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt15=9;
							switch ( input.LA(1) ) {
							case T_ALL:
								{
								alt15=1;
								}
								break;
							case T_ANY:
								{
								alt15=2;
								}
								break;
							case T_QUORUM:
								{
								alt15=3;
								}
								break;
							case T_ONE:
								{
								alt15=4;
								}
								break;
							case T_TWO:
								{
								alt15=5;
								}
								break;
							case T_THREE:
								{
								alt15=6;
								}
								break;
							case T_EACH_QUORUM:
								{
								alt15=7;
								}
								break;
							case T_LOCAL_ONE:
								{
								alt15=8;
								}
								break;
							case T_LOCAL_QUORUM:
								{
								alt15=9;
								}
								break;
							default:
								NoViableAltException nvae =
									new NoViableAltException("", 15, 0, input);
								throw nvae;
							}
							switch (alt15) {
								case 1 :
									// Meta.g:279:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement2455); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:280:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement2474); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:281:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement2492); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:282:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement2510); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:283:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement2528); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:284:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement2546); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:285:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement2564); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:286:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement2582); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:287:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement2600); 
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
					// Meta.g:291:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement2650); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement2652); 
					// Meta.g:292:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt17=9;
					switch ( input.LA(1) ) {
					case T_ALL:
						{
						alt17=1;
						}
						break;
					case T_ANY:
						{
						alt17=2;
						}
						break;
					case T_QUORUM:
						{
						alt17=3;
						}
						break;
					case T_ONE:
						{
						alt17=4;
						}
						break;
					case T_TWO:
						{
						alt17=5;
						}
						break;
					case T_THREE:
						{
						alt17=6;
						}
						break;
					case T_EACH_QUORUM:
						{
						alt17=7;
						}
						break;
					case T_LOCAL_ONE:
						{
						alt17=8;
						}
						break;
					case T_LOCAL_QUORUM:
						{
						alt17=9;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 17, 0, input);
						throw nvae;
					}
					switch (alt17) {
						case 1 :
							// Meta.g:292:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement2668); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:293:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement2687); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:294:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement2705); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:295:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement2723); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:296:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement2741); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:297:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement2759); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:298:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement2777); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:299:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement2795); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:300:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement2813); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:302:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==T_AND) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// Meta.g:302:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement2841); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement2843); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement2845); 
							// Meta.g:302:36: ( T_TRUE | T_FALSE )
							int alt18=2;
							int LA18_0 = input.LA(1);
							if ( (LA18_0==T_TRUE) ) {
								alt18=1;
							}
							else if ( (LA18_0==T_FALSE) ) {
								alt18=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 18, 0, input);
								throw nvae;
							}

							switch (alt18) {
								case 1 :
									// Meta.g:302:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement2848); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:302:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement2851); 
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
	// Meta.g:308:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:308:41: ( T_USE iden= T_IDENT )
			// Meta.g:309:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement2906); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement2914); 
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
	// Meta.g:313:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:316:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:317:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement2944); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement2950); 
			// Meta.g:319:5: ( T_IF T_EXISTS )?
			int alt21=2;
			int LA21_0 = input.LA(1);
			if ( (LA21_0==T_IF) ) {
				alt21=1;
			}
			switch (alt21) {
				case 1 :
					// Meta.g:319:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement2957); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement2959); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement2971); 
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
	// Meta.g:324:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:327:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:328:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement3005); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement3011); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement3019); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement3025); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement3033); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement3035); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement3039);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:333:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop22:
			while (true) {
				int alt22=2;
				int LA22_0 = input.LA(1);
				if ( (LA22_0==T_AND) ) {
					alt22=1;
				}

				switch (alt22) {
				case 1 :
					// Meta.g:333:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement3048); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement3052); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement3054); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement3058);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop22;
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
	// Meta.g:337:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
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
			// Meta.g:341:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:342:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement3097); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement3103); 
			// Meta.g:344:5: ( T_IF T_NOT T_EXISTS )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==T_IF) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// Meta.g:344:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement3110); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement3112); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement3114); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement3126); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement3132); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement3144); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement3146); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement3150);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:348:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop24:
			while (true) {
				int alt24=2;
				int LA24_0 = input.LA(1);
				if ( (LA24_0==T_AND) ) {
					alt24=1;
				}

				switch (alt24) {
				case 1 :
					// Meta.g:348:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement3159); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement3163); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement3165); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement3169);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop24;
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
	// Meta.g:352:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String ident =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:355:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID )
			// Meta.g:356:5: T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement3208); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement3214); 
			// Meta.g:358:5: ( T_IF T_EXISTS )?
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==T_IF) ) {
				alt25=1;
			}
			switch (alt25) {
				case 1 :
					// Meta.g:358:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement3221); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement3223); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement3235);
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
	// Meta.g:364:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:364:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:365:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement3255); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement3268);
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
	// Meta.g:371:1: metaStatement returns [Statement st] : (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement );
	public final Statement metaStatement() throws RecognitionException {
		Statement st = null;


		CreateTableStatement st_crta =null;
		AlterTableStatement st_alta =null;
		CreateTriggerStatement st_crtr =null;
		DropTriggerStatement st_drtr =null;
		StopProcessStatement st_stpr =null;
		ExplainPlanStatement st_xppl =null;
		SetOptionsStatement st_stpt =null;
		UseStatement st_usks =null;
		DropKeyspaceStatement st_drks =null;
		CreateKeyspaceStatement st_crks =null;
		AlterKeyspaceStatement st_alks =null;
		DropTableStatement st_tbdr =null;
		TruncateStatement st_trst =null;

		try {
			// Meta.g:371:37: (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement )
			int alt26=13;
			switch ( input.LA(1) ) {
			case T_CREATE:
				{
				switch ( input.LA(2) ) {
				case T_TABLE:
					{
					alt26=1;
					}
					break;
				case T_TRIGGER:
					{
					alt26=3;
					}
					break;
				case T_KEYSPACE:
					{
					alt26=10;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 26, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_ALTER:
				{
				int LA26_2 = input.LA(2);
				if ( (LA26_2==T_TABLE) ) {
					alt26=2;
				}
				else if ( (LA26_2==T_KEYSPACE) ) {
					alt26=11;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 26, 2, input);
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
					alt26=4;
					}
					break;
				case T_KEYSPACE:
					{
					alt26=9;
					}
					break;
				case T_TABLE:
					{
					alt26=12;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 26, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_STOP:
				{
				alt26=5;
				}
				break;
			case T_EXPLAIN:
				{
				alt26=6;
				}
				break;
			case T_SET:
				{
				alt26=7;
				}
				break;
			case T_USE:
				{
				alt26=8;
				}
				break;
			case T_TRUNCATE:
				{
				alt26=13;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 26, 0, input);
				throw nvae;
			}
			switch (alt26) {
				case 1 :
					// Meta.g:373:5: st_crta= createTableStatement
					{
					pushFollow(FOLLOW_createTableStatement_in_metaStatement3291);
					st_crta=createTableStatement();
					state._fsp--;

					 st = st_crta;
					}
					break;
				case 2 :
					// Meta.g:374:7: st_alta= alterTableStatement
					{
					pushFollow(FOLLOW_alterTableStatement_in_metaStatement3304);
					st_alta=alterTableStatement();
					state._fsp--;

					 st = st_alta;
					}
					break;
				case 3 :
					// Meta.g:375:7: st_crtr= createTriggerStatement
					{
					pushFollow(FOLLOW_createTriggerStatement_in_metaStatement3317);
					st_crtr=createTriggerStatement();
					state._fsp--;

					 st = st_crtr; 
					}
					break;
				case 4 :
					// Meta.g:376:7: st_drtr= dropTriggerStatement
					{
					pushFollow(FOLLOW_dropTriggerStatement_in_metaStatement3330);
					st_drtr=dropTriggerStatement();
					state._fsp--;

					 st = st_drtr; 
					}
					break;
				case 5 :
					// Meta.g:377:7: st_stpr= stopProcessStatement
					{
					pushFollow(FOLLOW_stopProcessStatement_in_metaStatement3344);
					st_stpr=stopProcessStatement();
					state._fsp--;

					 st = st_stpr; 
					}
					break;
				case 6 :
					// Meta.g:378:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement3358);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 7 :
					// Meta.g:379:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement3372);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 8 :
					// Meta.g:380:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement3386);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 9 :
					// Meta.g:381:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement3400);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 10 :
					// Meta.g:382:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement3414);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 11 :
					// Meta.g:383:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement3428);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 12 :
					// Meta.g:384:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement3442);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 13 :
					// Meta.g:385:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement3456);
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
	// Meta.g:387:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final Statement query() throws RecognitionException {
		Statement st = null;


		Statement mtst =null;

		try {
			// Meta.g:387:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:388:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query3477);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:388:21: ( T_SEMICOLON )+
			int cnt27=0;
			loop27:
			while (true) {
				int alt27=2;
				int LA27_0 = input.LA(1);
				if ( (LA27_0==T_SEMICOLON) ) {
					alt27=1;
				}

				switch (alt27) {
				case 1 :
					// Meta.g:388:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query3480); 
					}
					break;

				default :
					if ( cnt27 >= 1 ) break loop27;
					EarlyExitException eee = new EarlyExitException(27, input);
					throw eee;
				}
				cnt27++;
			}

			match(input,EOF,FOLLOW_EOF_in_query3484); 

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



	// $ANTLR start "getTableID"
	// Meta.g:395:1: getTableID returns [String tableID] : (ks= T_IDENT '.' )? ident= T_IDENT ;
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ks=null;
		Token ident=null;

		try {
			// Meta.g:395:36: ( (ks= T_IDENT '.' )? ident= T_IDENT )
			// Meta.g:396:5: (ks= T_IDENT '.' )? ident= T_IDENT
			{
			// Meta.g:396:5: (ks= T_IDENT '.' )?
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==T_IDENT) ) {
				int LA28_1 = input.LA(2);
				if ( (LA28_1==T_POINT) ) {
					alt28=1;
				}
			}
			switch (alt28) {
				case 1 :
					// Meta.g:396:6: ks= T_IDENT '.'
					{
					ks=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID3508); 
					match(input,T_POINT,FOLLOW_T_POINT_in_getTableID3510); 
					}
					break;

			}

			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID3521); 
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
	// Meta.g:399:1: getTerm returns [String term] : (ident= T_IDENT |noIdent= T_TERM );
	public final MetaParser.getTerm_return getTerm() throws RecognitionException {
		MetaParser.getTerm_return retval = new MetaParser.getTerm_return();
		retval.start = input.LT(1);

		Token ident=null;
		Token noIdent=null;

		try {
			// Meta.g:399:30: (ident= T_IDENT |noIdent= T_TERM )
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0==T_IDENT) ) {
				alt29=1;
			}
			else if ( (LA29_0==T_TERM) ) {
				alt29=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 29, 0, input);
				throw nvae;
			}

			switch (alt29) {
				case 1 :
					// Meta.g:400:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm3541); 
					retval.term = (ident!=null?ident.getText():null);
					}
					break;
				case 2 :
					// Meta.g:401:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm3553); 
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
	// Meta.g:404:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		ParserRuleReturnScope leftTerm1 =null;
		ParserRuleReturnScope rightTerm1 =null;
		ParserRuleReturnScope leftTermN =null;
		ParserRuleReturnScope rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:407:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:408:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral3583); 
			// Meta.g:409:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==T_IDENT||LA31_0==T_TERM) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// Meta.g:409:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral3593);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral3595); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral3599);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put((leftTerm1!=null?input.toString(leftTerm1.start,leftTerm1.stop):null), (rightTerm1!=null?input.toString(rightTerm1.start,rightTerm1.stop):null));
					// Meta.g:410:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop30:
					while (true) {
						int alt30=2;
						int LA30_0 = input.LA(1);
						if ( (LA30_0==T_COMMA) ) {
							alt30=1;
						}

						switch (alt30) {
						case 1 :
							// Meta.g:410:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral3608); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral3612);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral3614); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral3618);
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put((leftTermN!=null?input.toString(leftTermN.start,leftTermN.stop):null), (rightTermN!=null?input.toString(rightTermN.start,rightTermN.stop):null));
							}
							break;

						default :
							break loop30;
						}
					}

					}
					break;

			}

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral3630); 
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
	// Meta.g:414:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Map<String, String> mapliteral =null;
		String number =null;

		try {
			// Meta.g:414:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat )
			int alt32=4;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt32=1;
				}
				break;
			case T_CONSTANT:
				{
				alt32=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt32=3;
				}
				break;
			case T_FLOAT:
			case T_TERM:
				{
				alt32=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 32, 0, input);
				throw nvae;
			}
			switch (alt32) {
				case 1 :
					// Meta.g:415:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty3652); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:416:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty3664); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:417:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty3676);
					mapliteral=getMapLiteral();
					state._fsp--;

					value = new MapLiteralProperty(mapliteral);
					}
					break;
				case 4 :
					// Meta.g:418:7: number= getFloat
					{
					pushFollow(FOLLOW_getFloat_in_getValueProperty3688);
					number=getFloat();
					state._fsp--;

					value = new FloatProperty(Float.parseFloat(number));
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
	// Meta.g:422:1: getFloat returns [String floating] : (termToken= T_TERM |floatToken= T_FLOAT );
	public final String getFloat() throws RecognitionException {
		String floating = null;


		Token termToken=null;
		Token floatToken=null;

		try {
			// Meta.g:422:35: (termToken= T_TERM |floatToken= T_FLOAT )
			int alt33=2;
			int LA33_0 = input.LA(1);
			if ( (LA33_0==T_TERM) ) {
				alt33=1;
			}
			else if ( (LA33_0==T_FLOAT) ) {
				alt33=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 33, 0, input);
				throw nvae;
			}

			switch (alt33) {
				case 1 :
					// Meta.g:423:5: termToken= T_TERM
					{
					termToken=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getFloat3713); 
					floating =(termToken!=null?termToken.getText():null);
					}
					break;
				case 2 :
					// Meta.g:425:5: floatToken= T_FLOAT
					{
					floatToken=(Token)match(input,T_FLOAT,FOLLOW_T_FLOAT_in_getFloat3731); 
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



	public static final BitSet FOLLOW_T_STOP_in_stopProcessStatement1182 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_T_PROCESS_in_stopProcessStatement1184 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_stopProcessStatement1188 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTriggerStatement1211 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropTriggerStatement1218 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1222 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_ON_in_dropTriggerStatement1229 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1238 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTriggerStatement1266 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_TRIGGER_in_createTriggerStatement1273 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1277 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_ON_in_createTriggerStatement1284 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1293 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_USING_in_createTriggerStatement1299 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1303 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTableStatement1343 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_TABLE_in_createTableStatement1349 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_createTableStatement1356 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createTableStatement1358 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createTableStatement1360 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1373 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_84_in_createTableStatement1379 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1414 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1418 = new BitSet(new long[]{0x0800000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement1421 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement1423 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement1472 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1476 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1480 = new BitSet(new long[]{0x0800000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement1483 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement1485 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement1543 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement1545 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement1547 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_84_in_createTableStatement1549 = new BitSet(new long[]{0x0000800000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1611 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement1676 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1679 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_84_in_createTableStatement1778 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1782 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement1823 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1826 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_85_in_createTableStatement1864 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement1900 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement1904 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_85_in_createTableStatement1990 = new BitSet(new long[]{0x0000000400000000L,0x0000000000200000L});
	public static final BitSet FOLLOW_85_in_createTableStatement2063 = new BitSet(new long[]{0x0000800000000002L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_WITH_in_createTableStatement2065 = new BitSet(new long[]{0x0000800000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2076 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createTableStatement2078 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_createTableStatement2082 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_AND_in_createTableStatement2099 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2103 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createTableStatement2105 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_createTableStatement2109 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement2174 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_TABLE_in_alterTableStatement2180 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2188 = new BitSet(new long[]{0x0000004028000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement2195 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
	public static final BitSet FOLLOW_T_TYPE_in_alterTableStatement2201 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2205 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ADD_in_alterTableStatement2218 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2222 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2226 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_alterTableStatement2239 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_WITH_in_alterTableStatement2256 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2273 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement2275 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement2279 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_AND_in_alterTableStatement2296 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement2300 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement2302 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement2306 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement2357 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement2359 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement2361 = new BitSet(new long[]{0x8000086020000000L,0x0000000000000482L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement2365 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement2399 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement2401 = new BitSet(new long[]{0x0000000840000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement2413 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement2415 = new BitSet(new long[]{0x0000100000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement2418 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement2421 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement2436 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement2438 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement2440 = new BitSet(new long[]{0x2098008110000000L,0x0000000000000110L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement2455 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement2474 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement2492 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement2510 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement2528 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement2546 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement2564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement2582 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement2600 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement2650 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement2652 = new BitSet(new long[]{0x2098008110000000L,0x0000000000000110L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement2668 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement2687 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement2705 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement2723 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement2741 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement2759 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement2777 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement2795 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement2813 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement2841 = new BitSet(new long[]{0x0000000040000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement2843 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement2845 = new BitSet(new long[]{0x0000100000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement2848 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement2851 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement2906 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement2914 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement2944 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement2950 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement2957 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement2959 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement2971 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement3005 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement3011 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement3019 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement3025 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement3033 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement3035 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement3039 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement3048 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement3052 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement3054 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement3058 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement3097 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement3103 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement3110 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement3112 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement3114 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement3126 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement3132 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement3144 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement3146 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement3150 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement3159 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement3163 = new BitSet(new long[]{0x0000020000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement3165 = new BitSet(new long[]{0x0000A01000000000L,0x0000000000000009L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement3169 = new BitSet(new long[]{0x0000000080000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement3208 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement3214 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement3221 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement3223 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement3235 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement3255 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement3268 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTableStatement_in_metaStatement3291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterTableStatement_in_metaStatement3304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTriggerStatement_in_metaStatement3317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTriggerStatement_in_metaStatement3330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stopProcessStatement_in_metaStatement3344 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement3358 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement3372 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement3386 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement3400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement3414 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement3428 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement3442 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement3456 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query3477 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query3480 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_EOF_in_query3484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID3508 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_POINT_in_getTableID3510 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID3521 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm3541 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm3553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral3583 = new BitSet(new long[]{0x0000810000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3593 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral3595 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3599 = new BitSet(new long[]{0x0000010400000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral3608 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3612 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral3614 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral3618 = new BitSet(new long[]{0x0000010400000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral3630 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty3652 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty3664 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty3676 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getFloat_in_getValueProperty3688 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getFloat3713 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FLOAT_in_getFloat3731 = new BitSet(new long[]{0x0000000000000002L});
}
