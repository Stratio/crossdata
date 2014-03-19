// $ANTLR 3.5.1 Meta.g 2014-03-19 12:02:51

    package com.stratio.meta.core.grammar.generated;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public MetaLexer() {} 
	public MetaLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public MetaLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "Meta.g"; }

	// $ANTLR start "T__149"
	public final void mT__149() throws RecognitionException {
		try {
			int _type = T__149;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:14:8: ( '1' )
			// Meta.g:14:10: '1'
			{
			match('1'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__149"

	// $ANTLR start "T__150"
	public final void mT__150() throws RecognitionException {
		try {
			int _type = T__150;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:15:8: ( 'D' )
			// Meta.g:15:10: 'D'
			{
			match('D'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__150"

	// $ANTLR start "T__151"
	public final void mT__151() throws RecognitionException {
		try {
			int _type = T__151;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:16:8: ( 'H' )
			// Meta.g:16:10: 'H'
			{
			match('H'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__151"

	// $ANTLR start "T__152"
	public final void mT__152() throws RecognitionException {
		try {
			int _type = T__152;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:17:8: ( 'M' )
			// Meta.g:17:10: 'M'
			{
			match('M'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__152"

	// $ANTLR start "T__153"
	public final void mT__153() throws RecognitionException {
		try {
			int _type = T__153;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:18:8: ( 'S' )
			// Meta.g:18:10: 'S'
			{
			match('S'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__153"

	// $ANTLR start "T__154"
	public final void mT__154() throws RecognitionException {
		try {
			int _type = T__154;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:19:8: ( 'd' )
			// Meta.g:19:10: 'd'
			{
			match('d'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__154"

	// $ANTLR start "T__155"
	public final void mT__155() throws RecognitionException {
		try {
			int _type = T__155;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:20:8: ( 'h' )
			// Meta.g:20:10: 'h'
			{
			match('h'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__155"

	// $ANTLR start "T__156"
	public final void mT__156() throws RecognitionException {
		try {
			int _type = T__156;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:21:8: ( 'm' )
			// Meta.g:21:10: 'm'
			{
			match('m'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__156"

	// $ANTLR start "T__157"
	public final void mT__157() throws RecognitionException {
		try {
			int _type = T__157;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:22:8: ( 's' )
			// Meta.g:22:10: 's'
			{
			match('s'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__157"

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// Meta.g:67:11: ( ( 'a' | 'A' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "A"

	// $ANTLR start "B"
	public final void mB() throws RecognitionException {
		try {
			// Meta.g:68:11: ( ( 'b' | 'B' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "B"

	// $ANTLR start "C"
	public final void mC() throws RecognitionException {
		try {
			// Meta.g:69:11: ( ( 'c' | 'C' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "C"

	// $ANTLR start "D"
	public final void mD() throws RecognitionException {
		try {
			// Meta.g:70:11: ( ( 'd' | 'D' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "D"

	// $ANTLR start "E"
	public final void mE() throws RecognitionException {
		try {
			// Meta.g:71:11: ( ( 'e' | 'E' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "E"

	// $ANTLR start "F"
	public final void mF() throws RecognitionException {
		try {
			// Meta.g:72:11: ( ( 'f' | 'F' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "F"

	// $ANTLR start "G"
	public final void mG() throws RecognitionException {
		try {
			// Meta.g:73:11: ( ( 'g' | 'G' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "G"

	// $ANTLR start "H"
	public final void mH() throws RecognitionException {
		try {
			// Meta.g:74:11: ( ( 'h' | 'H' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "H"

	// $ANTLR start "I"
	public final void mI() throws RecognitionException {
		try {
			// Meta.g:75:11: ( ( 'i' | 'I' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "I"

	// $ANTLR start "J"
	public final void mJ() throws RecognitionException {
		try {
			// Meta.g:76:11: ( ( 'j' | 'J' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "J"

	// $ANTLR start "K"
	public final void mK() throws RecognitionException {
		try {
			// Meta.g:77:11: ( ( 'k' | 'K' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K"

	// $ANTLR start "L"
	public final void mL() throws RecognitionException {
		try {
			// Meta.g:78:11: ( ( 'l' | 'L' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "L"

	// $ANTLR start "M"
	public final void mM() throws RecognitionException {
		try {
			// Meta.g:79:11: ( ( 'm' | 'M' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "M"

	// $ANTLR start "N"
	public final void mN() throws RecognitionException {
		try {
			// Meta.g:80:11: ( ( 'n' | 'N' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "N"

	// $ANTLR start "O"
	public final void mO() throws RecognitionException {
		try {
			// Meta.g:81:11: ( ( 'o' | 'O' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "O"

	// $ANTLR start "P"
	public final void mP() throws RecognitionException {
		try {
			// Meta.g:82:11: ( ( 'p' | 'P' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "P"

	// $ANTLR start "Q"
	public final void mQ() throws RecognitionException {
		try {
			// Meta.g:83:11: ( ( 'q' | 'Q' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Q"

	// $ANTLR start "R"
	public final void mR() throws RecognitionException {
		try {
			// Meta.g:84:11: ( ( 'r' | 'R' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "R"

	// $ANTLR start "S"
	public final void mS() throws RecognitionException {
		try {
			// Meta.g:85:11: ( ( 's' | 'S' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "S"

	// $ANTLR start "T"
	public final void mT() throws RecognitionException {
		try {
			// Meta.g:86:11: ( ( 't' | 'T' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T"

	// $ANTLR start "U"
	public final void mU() throws RecognitionException {
		try {
			// Meta.g:87:11: ( ( 'u' | 'U' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "U"

	// $ANTLR start "V"
	public final void mV() throws RecognitionException {
		try {
			// Meta.g:88:11: ( ( 'v' | 'V' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "V"

	// $ANTLR start "W"
	public final void mW() throws RecognitionException {
		try {
			// Meta.g:89:11: ( ( 'w' | 'W' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "W"

	// $ANTLR start "X"
	public final void mX() throws RecognitionException {
		try {
			// Meta.g:90:11: ( ( 'x' | 'X' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "X"

	// $ANTLR start "Y"
	public final void mY() throws RecognitionException {
		try {
			// Meta.g:91:11: ( ( 'y' | 'Y' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Y"

	// $ANTLR start "Z"
	public final void mZ() throws RecognitionException {
		try {
			// Meta.g:92:11: ( ( 'z' | 'Z' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Z"

	// $ANTLR start "EXPONENT"
	public final void mEXPONENT() throws RecognitionException {
		try {
			// Meta.g:93:19: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
			// Meta.g:93:21: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Meta.g:93:31: ( '+' | '-' )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0=='+'||LA1_0=='-') ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// Meta.g:
					{
					if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			// Meta.g:93:42: ( '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EXPONENT"

	// $ANTLR start "POINT"
	public final void mPOINT() throws RecognitionException {
		try {
			// Meta.g:94:15: ( '.' )
			// Meta.g:94:17: '.'
			{
			match('.'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "POINT"

	// $ANTLR start "T_TRUNCATE"
	public final void mT_TRUNCATE() throws RecognitionException {
		try {
			int _type = T_TRUNCATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:97:11: ( T R U N C A T E )
			// Meta.g:97:13: T R U N C A T E
			{
			mT(); 

			mR(); 

			mU(); 

			mN(); 

			mC(); 

			mA(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TRUNCATE"

	// $ANTLR start "T_CREATE"
	public final void mT_CREATE() throws RecognitionException {
		try {
			int _type = T_CREATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:98:9: ( C R E A T E )
			// Meta.g:98:11: C R E A T E
			{
			mC(); 

			mR(); 

			mE(); 

			mA(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CREATE"

	// $ANTLR start "T_ALTER"
	public final void mT_ALTER() throws RecognitionException {
		try {
			int _type = T_ALTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:99:8: ( A L T E R )
			// Meta.g:99:10: A L T E R
			{
			mA(); 

			mL(); 

			mT(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ALTER"

	// $ANTLR start "T_KEYSPACE"
	public final void mT_KEYSPACE() throws RecognitionException {
		try {
			int _type = T_KEYSPACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:100:11: ( K E Y S P A C E )
			// Meta.g:100:13: K E Y S P A C E
			{
			mK(); 

			mE(); 

			mY(); 

			mS(); 

			mP(); 

			mA(); 

			mC(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_KEYSPACE"

	// $ANTLR start "T_NOT"
	public final void mT_NOT() throws RecognitionException {
		try {
			int _type = T_NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:101:6: ( N O T )
			// Meta.g:101:8: N O T
			{
			mN(); 

			mO(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_NOT"

	// $ANTLR start "T_WITH"
	public final void mT_WITH() throws RecognitionException {
		try {
			int _type = T_WITH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:102:7: ( W I T H )
			// Meta.g:102:9: W I T H
			{
			mW(); 

			mI(); 

			mT(); 

			mH(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_WITH"

	// $ANTLR start "T_DROP"
	public final void mT_DROP() throws RecognitionException {
		try {
			int _type = T_DROP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:103:7: ( D R O P )
			// Meta.g:103:9: D R O P
			{
			mD(); 

			mR(); 

			mO(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DROP"

	// $ANTLR start "T_TABLE"
	public final void mT_TABLE() throws RecognitionException {
		try {
			int _type = T_TABLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:104:8: ( T A B L E )
			// Meta.g:104:10: T A B L E
			{
			mT(); 

			mA(); 

			mB(); 

			mL(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TABLE"

	// $ANTLR start "T_IF"
	public final void mT_IF() throws RecognitionException {
		try {
			int _type = T_IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:105:5: ( I F )
			// Meta.g:105:7: I F
			{
			mI(); 

			mF(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_IF"

	// $ANTLR start "T_EXISTS"
	public final void mT_EXISTS() throws RecognitionException {
		try {
			int _type = T_EXISTS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:106:9: ( E X I S T S )
			// Meta.g:106:11: E X I S T S
			{
			mE(); 

			mX(); 

			mI(); 

			mS(); 

			mT(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EXISTS"

	// $ANTLR start "T_AND"
	public final void mT_AND() throws RecognitionException {
		try {
			int _type = T_AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:107:6: ( A N D )
			// Meta.g:107:8: A N D
			{
			mA(); 

			mN(); 

			mD(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AND"

	// $ANTLR start "T_USE"
	public final void mT_USE() throws RecognitionException {
		try {
			int _type = T_USE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:108:6: ( U S E )
			// Meta.g:108:8: U S E
			{
			mU(); 

			mS(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_USE"

	// $ANTLR start "T_SET"
	public final void mT_SET() throws RecognitionException {
		try {
			int _type = T_SET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:109:6: ( S E T )
			// Meta.g:109:8: S E T
			{
			mS(); 

			mE(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SET"

	// $ANTLR start "T_OPTIONS"
	public final void mT_OPTIONS() throws RecognitionException {
		try {
			int _type = T_OPTIONS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:110:10: ( O P T I O N S )
			// Meta.g:110:12: O P T I O N S
			{
			mO(); 

			mP(); 

			mT(); 

			mI(); 

			mO(); 

			mN(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_OPTIONS"

	// $ANTLR start "T_ANALYTICS"
	public final void mT_ANALYTICS() throws RecognitionException {
		try {
			int _type = T_ANALYTICS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:111:12: ( A N A L Y T I C S )
			// Meta.g:111:14: A N A L Y T I C S
			{
			mA(); 

			mN(); 

			mA(); 

			mL(); 

			mY(); 

			mT(); 

			mI(); 

			mC(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ANALYTICS"

	// $ANTLR start "T_TRUE"
	public final void mT_TRUE() throws RecognitionException {
		try {
			int _type = T_TRUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:112:7: ( T R U E )
			// Meta.g:112:9: T R U E
			{
			mT(); 

			mR(); 

			mU(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TRUE"

	// $ANTLR start "T_FALSE"
	public final void mT_FALSE() throws RecognitionException {
		try {
			int _type = T_FALSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:113:8: ( F A L S E )
			// Meta.g:113:10: F A L S E
			{
			mF(); 

			mA(); 

			mL(); 

			mS(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FALSE"

	// $ANTLR start "T_CONSISTENCY"
	public final void mT_CONSISTENCY() throws RecognitionException {
		try {
			int _type = T_CONSISTENCY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:114:14: ( C O N S I S T E N C Y )
			// Meta.g:114:16: C O N S I S T E N C Y
			{
			mC(); 

			mO(); 

			mN(); 

			mS(); 

			mI(); 

			mS(); 

			mT(); 

			mE(); 

			mN(); 

			mC(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CONSISTENCY"

	// $ANTLR start "T_ALL"
	public final void mT_ALL() throws RecognitionException {
		try {
			int _type = T_ALL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:115:6: ( A L L )
			// Meta.g:115:8: A L L
			{
			mA(); 

			mL(); 

			mL(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ALL"

	// $ANTLR start "T_ANY"
	public final void mT_ANY() throws RecognitionException {
		try {
			int _type = T_ANY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:116:6: ( A N Y )
			// Meta.g:116:8: A N Y
			{
			mA(); 

			mN(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ANY"

	// $ANTLR start "T_QUORUM"
	public final void mT_QUORUM() throws RecognitionException {
		try {
			int _type = T_QUORUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:117:9: ( Q U O R U M )
			// Meta.g:117:11: Q U O R U M
			{
			mQ(); 

			mU(); 

			mO(); 

			mR(); 

			mU(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_QUORUM"

	// $ANTLR start "T_ONE"
	public final void mT_ONE() throws RecognitionException {
		try {
			int _type = T_ONE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:118:6: ( O N E )
			// Meta.g:118:8: O N E
			{
			mO(); 

			mN(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ONE"

	// $ANTLR start "T_TWO"
	public final void mT_TWO() throws RecognitionException {
		try {
			int _type = T_TWO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:119:6: ( T W O )
			// Meta.g:119:8: T W O
			{
			mT(); 

			mW(); 

			mO(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TWO"

	// $ANTLR start "T_THREE"
	public final void mT_THREE() throws RecognitionException {
		try {
			int _type = T_THREE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:120:8: ( T H R E E )
			// Meta.g:120:10: T H R E E
			{
			mT(); 

			mH(); 

			mR(); 

			mE(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_THREE"

	// $ANTLR start "T_EACH_QUORUM"
	public final void mT_EACH_QUORUM() throws RecognitionException {
		try {
			int _type = T_EACH_QUORUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:121:14: ( E A C H '_' Q U O R U M )
			// Meta.g:121:16: E A C H '_' Q U O R U M
			{
			mE(); 

			mA(); 

			mC(); 

			mH(); 

			match('_'); 
			mQ(); 

			mU(); 

			mO(); 

			mR(); 

			mU(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EACH_QUORUM"

	// $ANTLR start "T_LOCAL_ONE"
	public final void mT_LOCAL_ONE() throws RecognitionException {
		try {
			int _type = T_LOCAL_ONE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:122:12: ( L O C A L '_' O N E )
			// Meta.g:122:14: L O C A L '_' O N E
			{
			mL(); 

			mO(); 

			mC(); 

			mA(); 

			mL(); 

			match('_'); 
			mO(); 

			mN(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LOCAL_ONE"

	// $ANTLR start "T_LOCAL_QUORUM"
	public final void mT_LOCAL_QUORUM() throws RecognitionException {
		try {
			int _type = T_LOCAL_QUORUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:123:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:123:17: L O C A L '_' Q U O R U M
			{
			mL(); 

			mO(); 

			mC(); 

			mA(); 

			mL(); 

			match('_'); 
			mQ(); 

			mU(); 

			mO(); 

			mR(); 

			mU(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LOCAL_QUORUM"

	// $ANTLR start "T_EXPLAIN"
	public final void mT_EXPLAIN() throws RecognitionException {
		try {
			int _type = T_EXPLAIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:124:10: ( E X P L A I N )
			// Meta.g:124:12: E X P L A I N
			{
			mE(); 

			mX(); 

			mP(); 

			mL(); 

			mA(); 

			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EXPLAIN"

	// $ANTLR start "T_PLAN"
	public final void mT_PLAN() throws RecognitionException {
		try {
			int _type = T_PLAN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:125:7: ( P L A N )
			// Meta.g:125:9: P L A N
			{
			mP(); 

			mL(); 

			mA(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PLAN"

	// $ANTLR start "T_FOR"
	public final void mT_FOR() throws RecognitionException {
		try {
			int _type = T_FOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:126:6: ( F O R )
			// Meta.g:126:8: F O R
			{
			mF(); 

			mO(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FOR"

	// $ANTLR start "T_INDEX"
	public final void mT_INDEX() throws RecognitionException {
		try {
			int _type = T_INDEX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:127:8: ( I N D E X )
			// Meta.g:127:10: I N D E X
			{
			mI(); 

			mN(); 

			mD(); 

			mE(); 

			mX(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INDEX"

	// $ANTLR start "T_LIST"
	public final void mT_LIST() throws RecognitionException {
		try {
			int _type = T_LIST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:128:7: ( L I S T )
			// Meta.g:128:9: L I S T
			{
			mL(); 

			mI(); 

			mS(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LIST"

	// $ANTLR start "T_REMOVE"
	public final void mT_REMOVE() throws RecognitionException {
		try {
			int _type = T_REMOVE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:129:9: ( R E M O V E )
			// Meta.g:129:11: R E M O V E
			{
			mR(); 

			mE(); 

			mM(); 

			mO(); 

			mV(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_REMOVE"

	// $ANTLR start "T_UDF"
	public final void mT_UDF() throws RecognitionException {
		try {
			int _type = T_UDF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:130:6: ( U D F )
			// Meta.g:130:8: U D F
			{
			mU(); 

			mD(); 

			mF(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_UDF"

	// $ANTLR start "T_PROCESS"
	public final void mT_PROCESS() throws RecognitionException {
		try {
			int _type = T_PROCESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:131:10: ( P R O C E S S )
			// Meta.g:131:12: P R O C E S S
			{
			mP(); 

			mR(); 

			mO(); 

			mC(); 

			mE(); 

			mS(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PROCESS"

	// $ANTLR start "T_TRIGGER"
	public final void mT_TRIGGER() throws RecognitionException {
		try {
			int _type = T_TRIGGER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:132:10: ( T R I G G E R )
			// Meta.g:132:12: T R I G G E R
			{
			mT(); 

			mR(); 

			mI(); 

			mG(); 

			mG(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TRIGGER"

	// $ANTLR start "T_STOP"
	public final void mT_STOP() throws RecognitionException {
		try {
			int _type = T_STOP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:133:7: ( S T O P )
			// Meta.g:133:9: S T O P
			{
			mS(); 

			mT(); 

			mO(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_STOP"

	// $ANTLR start "T_ON"
	public final void mT_ON() throws RecognitionException {
		try {
			int _type = T_ON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:134:5: ( O N )
			// Meta.g:134:7: O N
			{
			mO(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ON"

	// $ANTLR start "T_USING"
	public final void mT_USING() throws RecognitionException {
		try {
			int _type = T_USING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:135:8: ( U S I N G )
			// Meta.g:135:10: U S I N G
			{
			mU(); 

			mS(); 

			mI(); 

			mN(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_USING"

	// $ANTLR start "T_TYPE"
	public final void mT_TYPE() throws RecognitionException {
		try {
			int _type = T_TYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:136:7: ( T Y P E )
			// Meta.g:136:9: T Y P E
			{
			mT(); 

			mY(); 

			mP(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TYPE"

	// $ANTLR start "T_ADD"
	public final void mT_ADD() throws RecognitionException {
		try {
			int _type = T_ADD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:137:6: ( A D D )
			// Meta.g:137:8: A D D
			{
			mA(); 

			mD(); 

			mD(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ADD"

	// $ANTLR start "T_PRIMARY"
	public final void mT_PRIMARY() throws RecognitionException {
		try {
			int _type = T_PRIMARY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:138:10: ( P R I M A R Y )
			// Meta.g:138:12: P R I M A R Y
			{
			mP(); 

			mR(); 

			mI(); 

			mM(); 

			mA(); 

			mR(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PRIMARY"

	// $ANTLR start "T_KEY"
	public final void mT_KEY() throws RecognitionException {
		try {
			int _type = T_KEY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:139:6: ( K E Y )
			// Meta.g:139:8: K E Y
			{
			mK(); 

			mE(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_KEY"

	// $ANTLR start "T_INSERT"
	public final void mT_INSERT() throws RecognitionException {
		try {
			int _type = T_INSERT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:140:9: ( I N S E R T )
			// Meta.g:140:11: I N S E R T
			{
			mI(); 

			mN(); 

			mS(); 

			mE(); 

			mR(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INSERT"

	// $ANTLR start "T_INTO"
	public final void mT_INTO() throws RecognitionException {
		try {
			int _type = T_INTO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:141:7: ( I N T O )
			// Meta.g:141:9: I N T O
			{
			mI(); 

			mN(); 

			mT(); 

			mO(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INTO"

	// $ANTLR start "T_COMPACT"
	public final void mT_COMPACT() throws RecognitionException {
		try {
			int _type = T_COMPACT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:142:10: ( C O M P A C T )
			// Meta.g:142:12: C O M P A C T
			{
			mC(); 

			mO(); 

			mM(); 

			mP(); 

			mA(); 

			mC(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COMPACT"

	// $ANTLR start "T_STORAGE"
	public final void mT_STORAGE() throws RecognitionException {
		try {
			int _type = T_STORAGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:143:10: ( S T O R A G E )
			// Meta.g:143:12: S T O R A G E
			{
			mS(); 

			mT(); 

			mO(); 

			mR(); 

			mA(); 

			mG(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_STORAGE"

	// $ANTLR start "T_CLUSTERING"
	public final void mT_CLUSTERING() throws RecognitionException {
		try {
			int _type = T_CLUSTERING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:144:13: ( C L U S T E R I N G )
			// Meta.g:144:15: C L U S T E R I N G
			{
			mC(); 

			mL(); 

			mU(); 

			mS(); 

			mT(); 

			mE(); 

			mR(); 

			mI(); 

			mN(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CLUSTERING"

	// $ANTLR start "T_ORDER"
	public final void mT_ORDER() throws RecognitionException {
		try {
			int _type = T_ORDER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:145:8: ( O R D E R )
			// Meta.g:145:10: O R D E R
			{
			mO(); 

			mR(); 

			mD(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ORDER"

	// $ANTLR start "T_SELECT"
	public final void mT_SELECT() throws RecognitionException {
		try {
			int _type = T_SELECT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:146:9: ( S E L E C T )
			// Meta.g:146:11: S E L E C T
			{
			mS(); 

			mE(); 

			mL(); 

			mE(); 

			mC(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SELECT"

	// $ANTLR start "T_VALUES"
	public final void mT_VALUES() throws RecognitionException {
		try {
			int _type = T_VALUES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:147:9: ( V A L U E S )
			// Meta.g:147:11: V A L U E S
			{
			mV(); 

			mA(); 

			mL(); 

			mU(); 

			mE(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_VALUES"

	// $ANTLR start "T_UPDATE"
	public final void mT_UPDATE() throws RecognitionException {
		try {
			int _type = T_UPDATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:148:9: ( U P D A T E )
			// Meta.g:148:11: U P D A T E
			{
			mU(); 

			mP(); 

			mD(); 

			mA(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_UPDATE"

	// $ANTLR start "T_WHERE"
	public final void mT_WHERE() throws RecognitionException {
		try {
			int _type = T_WHERE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:149:8: ( W H E R E )
			// Meta.g:149:10: W H E R E
			{
			mW(); 

			mH(); 

			mE(); 

			mR(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_WHERE"

	// $ANTLR start "T_IN"
	public final void mT_IN() throws RecognitionException {
		try {
			int _type = T_IN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:150:5: ( I N )
			// Meta.g:150:7: I N
			{
			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_IN"

	// $ANTLR start "T_FROM"
	public final void mT_FROM() throws RecognitionException {
		try {
			int _type = T_FROM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:151:7: ( F R O M )
			// Meta.g:151:9: F R O M
			{
			mF(); 

			mR(); 

			mO(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FROM"

	// $ANTLR start "T_DELETE"
	public final void mT_DELETE() throws RecognitionException {
		try {
			int _type = T_DELETE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:152:9: ( D E L E T E )
			// Meta.g:152:11: D E L E T E
			{
			mD(); 

			mE(); 

			mL(); 

			mE(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DELETE"

	// $ANTLR start "T_WINDOW"
	public final void mT_WINDOW() throws RecognitionException {
		try {
			int _type = T_WINDOW;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:153:9: ( W I N D O W )
			// Meta.g:153:11: W I N D O W
			{
			mW(); 

			mI(); 

			mN(); 

			mD(); 

			mO(); 

			mW(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_WINDOW"

	// $ANTLR start "T_LAST"
	public final void mT_LAST() throws RecognitionException {
		try {
			int _type = T_LAST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:154:7: ( L A S T )
			// Meta.g:154:9: L A S T
			{
			mL(); 

			mA(); 

			mS(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LAST"

	// $ANTLR start "T_ROWS"
	public final void mT_ROWS() throws RecognitionException {
		try {
			int _type = T_ROWS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:155:7: ( R O W S )
			// Meta.g:155:9: R O W S
			{
			mR(); 

			mO(); 

			mW(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ROWS"

	// $ANTLR start "T_INNER"
	public final void mT_INNER() throws RecognitionException {
		try {
			int _type = T_INNER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:156:8: ( I N N E R )
			// Meta.g:156:10: I N N E R
			{
			mI(); 

			mN(); 

			mN(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INNER"

	// $ANTLR start "T_JOIN"
	public final void mT_JOIN() throws RecognitionException {
		try {
			int _type = T_JOIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:157:7: ( J O I N )
			// Meta.g:157:9: J O I N
			{
			mJ(); 

			mO(); 

			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_JOIN"

	// $ANTLR start "T_BY"
	public final void mT_BY() throws RecognitionException {
		try {
			int _type = T_BY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:158:5: ( B Y )
			// Meta.g:158:7: B Y
			{
			mB(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_BY"

	// $ANTLR start "T_LIMIT"
	public final void mT_LIMIT() throws RecognitionException {
		try {
			int _type = T_LIMIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:159:8: ( L I M I T )
			// Meta.g:159:10: L I M I T
			{
			mL(); 

			mI(); 

			mM(); 

			mI(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LIMIT"

	// $ANTLR start "T_DISABLE"
	public final void mT_DISABLE() throws RecognitionException {
		try {
			int _type = T_DISABLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:160:10: ( D I S A B L E )
			// Meta.g:160:12: D I S A B L E
			{
			mD(); 

			mI(); 

			mS(); 

			mA(); 

			mB(); 

			mL(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DISABLE"

	// $ANTLR start "T_DISTINCT"
	public final void mT_DISTINCT() throws RecognitionException {
		try {
			int _type = T_DISTINCT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:161:11: ( D I S T I N C T )
			// Meta.g:161:13: D I S T I N C T
			{
			mD(); 

			mI(); 

			mS(); 

			mT(); 

			mI(); 

			mN(); 

			mC(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DISTINCT"

	// $ANTLR start "T_COUNT"
	public final void mT_COUNT() throws RecognitionException {
		try {
			int _type = T_COUNT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:162:8: ( C O U N T )
			// Meta.g:162:10: C O U N T
			{
			mC(); 

			mO(); 

			mU(); 

			mN(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COUNT"

	// $ANTLR start "T_AS"
	public final void mT_AS() throws RecognitionException {
		try {
			int _type = T_AS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:163:5: ( A S )
			// Meta.g:163:7: A S
			{
			mA(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AS"

	// $ANTLR start "T_BETWEEN"
	public final void mT_BETWEEN() throws RecognitionException {
		try {
			int _type = T_BETWEEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:164:10: ( B E T W E E N )
			// Meta.g:164:12: B E T W E E N
			{
			mB(); 

			mE(); 

			mT(); 

			mW(); 

			mE(); 

			mE(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_BETWEEN"

	// $ANTLR start "T_ASC"
	public final void mT_ASC() throws RecognitionException {
		try {
			int _type = T_ASC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:165:6: ( A S C )
			// Meta.g:165:8: A S C
			{
			mA(); 

			mS(); 

			mC(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ASC"

	// $ANTLR start "T_DESC"
	public final void mT_DESC() throws RecognitionException {
		try {
			int _type = T_DESC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:166:7: ( D E S C )
			// Meta.g:166:9: D E S C
			{
			mD(); 

			mE(); 

			mS(); 

			mC(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DESC"

	// $ANTLR start "T_LIKE"
	public final void mT_LIKE() throws RecognitionException {
		try {
			int _type = T_LIKE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:167:7: ( L I K E )
			// Meta.g:167:9: L I K E
			{
			mL(); 

			mI(); 

			mK(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LIKE"

	// $ANTLR start "T_EPHEMERAL"
	public final void mT_EPHEMERAL() throws RecognitionException {
		try {
			int _type = T_EPHEMERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:168:12: ( E P H E M E R A L )
			// Meta.g:168:14: E P H E M E R A L
			{
			mE(); 

			mP(); 

			mH(); 

			mE(); 

			mM(); 

			mE(); 

			mR(); 

			mA(); 

			mL(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EPHEMERAL"

	// $ANTLR start "T_AT"
	public final void mT_AT() throws RecognitionException {
		try {
			int _type = T_AT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:169:5: ( '@' )
			// Meta.g:169:7: '@'
			{
			match('@'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AT"

	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:171:12: ( ';' )
			// Meta.g:171:14: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SEMICOLON"

	// $ANTLR start "T_EQUAL"
	public final void mT_EQUAL() throws RecognitionException {
		try {
			int _type = T_EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:172:8: ( '=' )
			// Meta.g:172:10: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EQUAL"

	// $ANTLR start "T_START_SBRACKET"
	public final void mT_START_SBRACKET() throws RecognitionException {
		try {
			int _type = T_START_SBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:173:17: ( '{' )
			// Meta.g:173:19: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_START_SBRACKET"

	// $ANTLR start "T_END_SBRACKET"
	public final void mT_END_SBRACKET() throws RecognitionException {
		try {
			int _type = T_END_SBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:174:15: ( '}' )
			// Meta.g:174:17: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_END_SBRACKET"

	// $ANTLR start "T_COLON"
	public final void mT_COLON() throws RecognitionException {
		try {
			int _type = T_COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:175:8: ( ':' )
			// Meta.g:175:10: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COLON"

	// $ANTLR start "T_COMMA"
	public final void mT_COMMA() throws RecognitionException {
		try {
			int _type = T_COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:176:8: ( ',' )
			// Meta.g:176:10: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COMMA"

	// $ANTLR start "T_START_PARENTHESIS"
	public final void mT_START_PARENTHESIS() throws RecognitionException {
		try {
			int _type = T_START_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:177:20: ( '(' )
			// Meta.g:177:22: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_START_PARENTHESIS"

	// $ANTLR start "T_END_PARENTHESIS"
	public final void mT_END_PARENTHESIS() throws RecognitionException {
		try {
			int _type = T_END_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:178:18: ( ')' )
			// Meta.g:178:20: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_END_PARENTHESIS"

	// $ANTLR start "T_QUOTE"
	public final void mT_QUOTE() throws RecognitionException {
		try {
			int _type = T_QUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:179:8: ( '\"' )
			// Meta.g:179:10: '\"'
			{
			match('\"'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_QUOTE"

	// $ANTLR start "T_SINGLE_QUOTE"
	public final void mT_SINGLE_QUOTE() throws RecognitionException {
		try {
			int _type = T_SINGLE_QUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:180:15: ( '\\'' )
			// Meta.g:180:17: '\\''
			{
			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SINGLE_QUOTE"

	// $ANTLR start "T_DEFAULT"
	public final void mT_DEFAULT() throws RecognitionException {
		try {
			int _type = T_DEFAULT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:181:10: ( D E F A U L T )
			// Meta.g:181:12: D E F A U L T
			{
			mD(); 

			mE(); 

			mF(); 

			mA(); 

			mU(); 

			mL(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DEFAULT"

	// $ANTLR start "T_LUCENE"
	public final void mT_LUCENE() throws RecognitionException {
		try {
			int _type = T_LUCENE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:182:9: ( L U C E N E )
			// Meta.g:182:11: L U C E N E
			{
			mL(); 

			mU(); 

			mC(); 

			mE(); 

			mN(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LUCENE"

	// $ANTLR start "T_CUSTOM"
	public final void mT_CUSTOM() throws RecognitionException {
		try {
			int _type = T_CUSTOM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:183:9: ( C U S T O M )
			// Meta.g:183:11: C U S T O M
			{
			mC(); 

			mU(); 

			mS(); 

			mT(); 

			mO(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CUSTOM"

	// $ANTLR start "T_START_BRACKET"
	public final void mT_START_BRACKET() throws RecognitionException {
		try {
			int _type = T_START_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:184:16: ( '[' )
			// Meta.g:184:18: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_START_BRACKET"

	// $ANTLR start "T_END_BRACKET"
	public final void mT_END_BRACKET() throws RecognitionException {
		try {
			int _type = T_END_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:185:14: ( ']' )
			// Meta.g:185:16: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_END_BRACKET"

	// $ANTLR start "T_PLUS"
	public final void mT_PLUS() throws RecognitionException {
		try {
			int _type = T_PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:186:7: ( '+' )
			// Meta.g:186:9: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PLUS"

	// $ANTLR start "T_SUBTRACT"
	public final void mT_SUBTRACT() throws RecognitionException {
		try {
			int _type = T_SUBTRACT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:187:11: ( '-' )
			// Meta.g:187:13: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SUBTRACT"

	// $ANTLR start "T_INTERROGATION"
	public final void mT_INTERROGATION() throws RecognitionException {
		try {
			int _type = T_INTERROGATION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:188:16: ( '?' )
			// Meta.g:188:18: '?'
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INTERROGATION"

	// $ANTLR start "T_ASTERISK"
	public final void mT_ASTERISK() throws RecognitionException {
		try {
			int _type = T_ASTERISK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:189:11: ( '*' )
			// Meta.g:189:13: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ASTERISK"

	// $ANTLR start "T_GROUP"
	public final void mT_GROUP() throws RecognitionException {
		try {
			int _type = T_GROUP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:190:8: ( G R O U P )
			// Meta.g:190:10: G R O U P
			{
			mG(); 

			mR(); 

			mO(); 

			mU(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_GROUP"

	// $ANTLR start "T_AGGREGATION"
	public final void mT_AGGREGATION() throws RecognitionException {
		try {
			int _type = T_AGGREGATION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:191:14: ( A G G R E G A T I O N )
			// Meta.g:191:16: A G G R E G A T I O N
			{
			mA(); 

			mG(); 

			mG(); 

			mR(); 

			mE(); 

			mG(); 

			mA(); 

			mT(); 

			mI(); 

			mO(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AGGREGATION"

	// $ANTLR start "T_MAX"
	public final void mT_MAX() throws RecognitionException {
		try {
			int _type = T_MAX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:192:6: ( M A X )
			// Meta.g:192:8: M A X
			{
			mM(); 

			mA(); 

			mX(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MAX"

	// $ANTLR start "T_MIN"
	public final void mT_MIN() throws RecognitionException {
		try {
			int _type = T_MIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:193:6: ( M I N )
			// Meta.g:193:8: M I N
			{
			mM(); 

			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MIN"

	// $ANTLR start "T_AVG"
	public final void mT_AVG() throws RecognitionException {
		try {
			int _type = T_AVG;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:194:6: ( A V G )
			// Meta.g:194:8: A V G
			{
			mA(); 

			mV(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AVG"

	// $ANTLR start "T_GT"
	public final void mT_GT() throws RecognitionException {
		try {
			int _type = T_GT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:195:5: ( '>' )
			// Meta.g:195:7: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_GT"

	// $ANTLR start "T_LT"
	public final void mT_LT() throws RecognitionException {
		try {
			int _type = T_LT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:196:5: ( '<' )
			// Meta.g:196:7: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LT"

	// $ANTLR start "T_GTE"
	public final void mT_GTE() throws RecognitionException {
		try {
			int _type = T_GTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:197:6: ( '>' '=' )
			// Meta.g:197:8: '>' '='
			{
			match('>'); 
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_GTE"

	// $ANTLR start "T_LTE"
	public final void mT_LTE() throws RecognitionException {
		try {
			int _type = T_LTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:198:6: ( '<' '=' )
			// Meta.g:198:8: '<' '='
			{
			match('<'); 
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LTE"

	// $ANTLR start "T_NOT_EQUAL"
	public final void mT_NOT_EQUAL() throws RecognitionException {
		try {
			int _type = T_NOT_EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:199:12: ( '<' '>' )
			// Meta.g:199:14: '<' '>'
			{
			match('<'); 
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_NOT_EQUAL"

	// $ANTLR start "T_TOKEN"
	public final void mT_TOKEN() throws RecognitionException {
		try {
			int _type = T_TOKEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:200:8: ( T O K E N )
			// Meta.g:200:10: T O K E N
			{
			mT(); 

			mO(); 

			mK(); 

			mE(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TOKEN"

	// $ANTLR start "T_SECONDS"
	public final void mT_SECONDS() throws RecognitionException {
		try {
			int _type = T_SECONDS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:201:10: ( S E C O N D S )
			// Meta.g:201:12: S E C O N D S
			{
			mS(); 

			mE(); 

			mC(); 

			mO(); 

			mN(); 

			mD(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SECONDS"

	// $ANTLR start "T_MINUTES"
	public final void mT_MINUTES() throws RecognitionException {
		try {
			int _type = T_MINUTES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:202:10: ( M I N U T E S )
			// Meta.g:202:12: M I N U T E S
			{
			mM(); 

			mI(); 

			mN(); 

			mU(); 

			mT(); 

			mE(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MINUTES"

	// $ANTLR start "T_HOURS"
	public final void mT_HOURS() throws RecognitionException {
		try {
			int _type = T_HOURS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:203:8: ( H O U R S )
			// Meta.g:203:10: H O U R S
			{
			mH(); 

			mO(); 

			mU(); 

			mR(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_HOURS"

	// $ANTLR start "T_DAYS"
	public final void mT_DAYS() throws RecognitionException {
		try {
			int _type = T_DAYS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:204:7: ( D A Y S )
			// Meta.g:204:9: D A Y S
			{
			mD(); 

			mA(); 

			mY(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DAYS"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// Meta.g:206:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
			// Meta.g:
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LETTER"

	// $ANTLR start "DIGIT"
	public final void mDIGIT() throws RecognitionException {
		try {
			// Meta.g:207:15: ( '0' .. '9' )
			// Meta.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIGIT"

	// $ANTLR start "QUOTED_LITERAL"
	public final void mQUOTED_LITERAL() throws RecognitionException {
		try {
			int _type = QUOTED_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			int c;

			 StringBuilder sb = new StringBuilder(); 
			// Meta.g:211:38: ( '\\'' (c=~ ( '\\'' ) | '\\'' '\\'' )* '\\'' )
			// Meta.g:212:9: '\\'' (c=~ ( '\\'' ) | '\\'' '\\'' )* '\\''
			{
			match('\''); 
			// Meta.g:212:14: (c=~ ( '\\'' ) | '\\'' '\\'' )*
			loop3:
			while (true) {
				int alt3=3;
				int LA3_0 = input.LA(1);
				if ( (LA3_0=='\'') ) {
					int LA3_1 = input.LA(2);
					if ( (LA3_1=='\'') ) {
						alt3=2;
					}

				}
				else if ( ((LA3_0 >= '\u0000' && LA3_0 <= '&')||(LA3_0 >= '(' && LA3_0 <= '\uFFFF')) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// Meta.g:212:15: c=~ ( '\\'' )
					{
					c= input.LA(1);
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					 sb.appendCodePoint(c);
					}
					break;
				case 2 :
					// Meta.g:212:53: '\\'' '\\''
					{
					match('\''); 
					match('\''); 
					 sb.appendCodePoint('\''); 
					}
					break;

				default :
					break loop3;
				}
			}

			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
			 setText(sb.toString()); 
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QUOTED_LITERAL"

	// $ANTLR start "T_CONSTANT"
	public final void mT_CONSTANT() throws RecognitionException {
		try {
			int _type = T_CONSTANT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:215:11: ( ( DIGIT )+ )
			// Meta.g:215:13: ( DIGIT )+
			{
			// Meta.g:215:13: ( DIGIT )+
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CONSTANT"

	// $ANTLR start "T_IDENT"
	public final void mT_IDENT() throws RecognitionException {
		try {
			int _type = T_IDENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:217:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:217:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:217:17: ( LETTER | DIGIT | '_' )*
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( ((LA5_0 >= '0' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
					alt5=1;
				}

				switch (alt5) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop5;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_IDENT"

	// $ANTLR start "T_KS_AND_TN"
	public final void mT_KS_AND_TN() throws RecognitionException {
		try {
			int _type = T_KS_AND_TN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:219:12: ( LETTER ( LETTER | DIGIT | '_' )* ( POINT LETTER ( LETTER | DIGIT | '_' )* )? )
			// Meta.g:219:14: LETTER ( LETTER | DIGIT | '_' )* ( POINT LETTER ( LETTER | DIGIT | '_' )* )?
			{
			mLETTER(); 

			// Meta.g:219:21: ( LETTER | DIGIT | '_' )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '0' && LA6_0 <= '9')||(LA6_0 >= 'A' && LA6_0 <= 'Z')||LA6_0=='_'||(LA6_0 >= 'a' && LA6_0 <= 'z')) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop6;
				}
			}

			// Meta.g:219:45: ( POINT LETTER ( LETTER | DIGIT | '_' )* )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0=='.') ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// Meta.g:219:46: POINT LETTER ( LETTER | DIGIT | '_' )*
					{
					mPOINT(); 

					mLETTER(); 

					// Meta.g:219:59: ( LETTER | DIGIT | '_' )*
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( ((LA7_0 >= '0' && LA7_0 <= '9')||(LA7_0 >= 'A' && LA7_0 <= 'Z')||LA7_0=='_'||(LA7_0 >= 'a' && LA7_0 <= 'z')) ) {
							alt7=1;
						}

						switch (alt7) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop7;
						}
					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_KS_AND_TN"

	// $ANTLR start "T_TERM"
	public final void mT_TERM() throws RecognitionException {
		try {
			int _type = T_TERM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:221:7: ( ( LETTER | DIGIT | '_' | '.' )+ )
			// Meta.g:221:9: ( LETTER | DIGIT | '_' | '.' )+
			{
			// Meta.g:221:9: ( LETTER | DIGIT | '_' | '.' )+
			int cnt9=0;
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0=='.'||(LA9_0 >= '0' && LA9_0 <= '9')||(LA9_0 >= 'A' && LA9_0 <= 'Z')||LA9_0=='_'||(LA9_0 >= 'a' && LA9_0 <= 'z')) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// Meta.g:
					{
					if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt9 >= 1 ) break loop9;
					EarlyExitException eee = new EarlyExitException(9, input);
					throw eee;
				}
				cnt9++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TERM"

	// $ANTLR start "T_FLOAT"
	public final void mT_FLOAT() throws RecognitionException {
		try {
			int _type = T_FLOAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:223:8: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
			int alt16=3;
			alt16 = dfa16.predict(input);
			switch (alt16) {
				case 1 :
					// Meta.g:223:12: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
					{
					// Meta.g:223:12: ( '0' .. '9' )+
					int cnt10=0;
					loop10:
					while (true) {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt10 >= 1 ) break loop10;
							EarlyExitException eee = new EarlyExitException(10, input);
							throw eee;
						}
						cnt10++;
					}

					match('.'); 
					// Meta.g:223:28: ( '0' .. '9' )*
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop11;
						}
					}

					// Meta.g:223:40: ( EXPONENT )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0=='E'||LA12_0=='e') ) {
						alt12=1;
					}
					switch (alt12) {
						case 1 :
							// Meta.g:223:40: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:224:10: '.' ( '0' .. '9' )+ ( EXPONENT )?
					{
					match('.'); 
					// Meta.g:224:14: ( '0' .. '9' )+
					int cnt13=0;
					loop13:
					while (true) {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( ((LA13_0 >= '0' && LA13_0 <= '9')) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt13 >= 1 ) break loop13;
							EarlyExitException eee = new EarlyExitException(13, input);
							throw eee;
						}
						cnt13++;
					}

					// Meta.g:224:26: ( EXPONENT )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0=='E'||LA14_0=='e') ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// Meta.g:224:26: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 3 :
					// Meta.g:225:10: ( '0' .. '9' )+ EXPONENT
					{
					// Meta.g:225:10: ( '0' .. '9' )+
					int cnt15=0;
					loop15:
					while (true) {
						int alt15=2;
						int LA15_0 = input.LA(1);
						if ( ((LA15_0 >= '0' && LA15_0 <= '9')) ) {
							alt15=1;
						}

						switch (alt15) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt15 >= 1 ) break loop15;
							EarlyExitException eee = new EarlyExitException(15, input);
							throw eee;
						}
						cnt15++;
					}

					mEXPONENT(); 

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FLOAT"

	// $ANTLR start "T_PATH"
	public final void mT_PATH() throws RecognitionException {
		try {
			int _type = T_PATH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:228:7: ( ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+ )
			// Meta.g:228:9: ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+
			{
			// Meta.g:228:9: ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+
			int cnt17=0;
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( ((LA17_0 >= '-' && LA17_0 <= '9')||(LA17_0 >= 'A' && LA17_0 <= 'Z')||LA17_0=='_'||(LA17_0 >= 'a' && LA17_0 <= 'z')) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '-' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt17 >= 1 ) break loop17;
					EarlyExitException eee = new EarlyExitException(17, input);
					throw eee;
				}
				cnt17++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PATH"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:963:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:963:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:963:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			int cnt18=0;
			loop18:
			while (true) {
				int alt18=2;
				int LA18_0 = input.LA(1);
				if ( ((LA18_0 >= '\t' && LA18_0 <= '\n')||LA18_0=='\r'||LA18_0==' ') ) {
					alt18=1;
				}

				switch (alt18) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt18 >= 1 ) break loop18;
					EarlyExitException eee = new EarlyExitException(18, input);
					throw eee;
				}
				cnt18++;
			}

			 
			        _channel = HIDDEN; 
			    
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// Meta.g:1:8: ( T__149 | T__150 | T__151 | T__152 | T__153 | T__154 | T__155 | T__156 | T__157 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_WHERE | T_IN | T_FROM | T_DELETE | T_WINDOW | T_LAST | T_ROWS | T_INNER | T_JOIN | T_BY | T_LIMIT | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_EPHEMERAL | T_AT | T_SEMICOLON | T_EQUAL | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_PARENTHESIS | T_END_PARENTHESIS | T_QUOTE | T_SINGLE_QUOTE | T_DEFAULT | T_LUCENE | T_CUSTOM | T_START_BRACKET | T_END_BRACKET | T_PLUS | T_SUBTRACT | T_INTERROGATION | T_ASTERISK | T_GROUP | T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_TOKEN | T_SECONDS | T_MINUTES | T_HOURS | T_DAYS | QUOTED_LITERAL | T_CONSTANT | T_IDENT | T_KS_AND_TN | T_TERM | T_FLOAT | T_PATH | WS )
		int alt19=124;
		alt19 = dfa19.predict(input);
		switch (alt19) {
			case 1 :
				// Meta.g:1:10: T__149
				{
				mT__149(); 

				}
				break;
			case 2 :
				// Meta.g:1:17: T__150
				{
				mT__150(); 

				}
				break;
			case 3 :
				// Meta.g:1:24: T__151
				{
				mT__151(); 

				}
				break;
			case 4 :
				// Meta.g:1:31: T__152
				{
				mT__152(); 

				}
				break;
			case 5 :
				// Meta.g:1:38: T__153
				{
				mT__153(); 

				}
				break;
			case 6 :
				// Meta.g:1:45: T__154
				{
				mT__154(); 

				}
				break;
			case 7 :
				// Meta.g:1:52: T__155
				{
				mT__155(); 

				}
				break;
			case 8 :
				// Meta.g:1:59: T__156
				{
				mT__156(); 

				}
				break;
			case 9 :
				// Meta.g:1:66: T__157
				{
				mT__157(); 

				}
				break;
			case 10 :
				// Meta.g:1:73: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 11 :
				// Meta.g:1:84: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 12 :
				// Meta.g:1:93: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 13 :
				// Meta.g:1:101: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 14 :
				// Meta.g:1:112: T_NOT
				{
				mT_NOT(); 

				}
				break;
			case 15 :
				// Meta.g:1:118: T_WITH
				{
				mT_WITH(); 

				}
				break;
			case 16 :
				// Meta.g:1:125: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 17 :
				// Meta.g:1:132: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 18 :
				// Meta.g:1:140: T_IF
				{
				mT_IF(); 

				}
				break;
			case 19 :
				// Meta.g:1:145: T_EXISTS
				{
				mT_EXISTS(); 

				}
				break;
			case 20 :
				// Meta.g:1:154: T_AND
				{
				mT_AND(); 

				}
				break;
			case 21 :
				// Meta.g:1:160: T_USE
				{
				mT_USE(); 

				}
				break;
			case 22 :
				// Meta.g:1:166: T_SET
				{
				mT_SET(); 

				}
				break;
			case 23 :
				// Meta.g:1:172: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 24 :
				// Meta.g:1:182: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 25 :
				// Meta.g:1:194: T_TRUE
				{
				mT_TRUE(); 

				}
				break;
			case 26 :
				// Meta.g:1:201: T_FALSE
				{
				mT_FALSE(); 

				}
				break;
			case 27 :
				// Meta.g:1:209: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 28 :
				// Meta.g:1:223: T_ALL
				{
				mT_ALL(); 

				}
				break;
			case 29 :
				// Meta.g:1:229: T_ANY
				{
				mT_ANY(); 

				}
				break;
			case 30 :
				// Meta.g:1:235: T_QUORUM
				{
				mT_QUORUM(); 

				}
				break;
			case 31 :
				// Meta.g:1:244: T_ONE
				{
				mT_ONE(); 

				}
				break;
			case 32 :
				// Meta.g:1:250: T_TWO
				{
				mT_TWO(); 

				}
				break;
			case 33 :
				// Meta.g:1:256: T_THREE
				{
				mT_THREE(); 

				}
				break;
			case 34 :
				// Meta.g:1:264: T_EACH_QUORUM
				{
				mT_EACH_QUORUM(); 

				}
				break;
			case 35 :
				// Meta.g:1:278: T_LOCAL_ONE
				{
				mT_LOCAL_ONE(); 

				}
				break;
			case 36 :
				// Meta.g:1:290: T_LOCAL_QUORUM
				{
				mT_LOCAL_QUORUM(); 

				}
				break;
			case 37 :
				// Meta.g:1:305: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 38 :
				// Meta.g:1:315: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 39 :
				// Meta.g:1:322: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 40 :
				// Meta.g:1:328: T_INDEX
				{
				mT_INDEX(); 

				}
				break;
			case 41 :
				// Meta.g:1:336: T_LIST
				{
				mT_LIST(); 

				}
				break;
			case 42 :
				// Meta.g:1:343: T_REMOVE
				{
				mT_REMOVE(); 

				}
				break;
			case 43 :
				// Meta.g:1:352: T_UDF
				{
				mT_UDF(); 

				}
				break;
			case 44 :
				// Meta.g:1:358: T_PROCESS
				{
				mT_PROCESS(); 

				}
				break;
			case 45 :
				// Meta.g:1:368: T_TRIGGER
				{
				mT_TRIGGER(); 

				}
				break;
			case 46 :
				// Meta.g:1:378: T_STOP
				{
				mT_STOP(); 

				}
				break;
			case 47 :
				// Meta.g:1:385: T_ON
				{
				mT_ON(); 

				}
				break;
			case 48 :
				// Meta.g:1:390: T_USING
				{
				mT_USING(); 

				}
				break;
			case 49 :
				// Meta.g:1:398: T_TYPE
				{
				mT_TYPE(); 

				}
				break;
			case 50 :
				// Meta.g:1:405: T_ADD
				{
				mT_ADD(); 

				}
				break;
			case 51 :
				// Meta.g:1:411: T_PRIMARY
				{
				mT_PRIMARY(); 

				}
				break;
			case 52 :
				// Meta.g:1:421: T_KEY
				{
				mT_KEY(); 

				}
				break;
			case 53 :
				// Meta.g:1:427: T_INSERT
				{
				mT_INSERT(); 

				}
				break;
			case 54 :
				// Meta.g:1:436: T_INTO
				{
				mT_INTO(); 

				}
				break;
			case 55 :
				// Meta.g:1:443: T_COMPACT
				{
				mT_COMPACT(); 

				}
				break;
			case 56 :
				// Meta.g:1:453: T_STORAGE
				{
				mT_STORAGE(); 

				}
				break;
			case 57 :
				// Meta.g:1:463: T_CLUSTERING
				{
				mT_CLUSTERING(); 

				}
				break;
			case 58 :
				// Meta.g:1:476: T_ORDER
				{
				mT_ORDER(); 

				}
				break;
			case 59 :
				// Meta.g:1:484: T_SELECT
				{
				mT_SELECT(); 

				}
				break;
			case 60 :
				// Meta.g:1:493: T_VALUES
				{
				mT_VALUES(); 

				}
				break;
			case 61 :
				// Meta.g:1:502: T_UPDATE
				{
				mT_UPDATE(); 

				}
				break;
			case 62 :
				// Meta.g:1:511: T_WHERE
				{
				mT_WHERE(); 

				}
				break;
			case 63 :
				// Meta.g:1:519: T_IN
				{
				mT_IN(); 

				}
				break;
			case 64 :
				// Meta.g:1:524: T_FROM
				{
				mT_FROM(); 

				}
				break;
			case 65 :
				// Meta.g:1:531: T_DELETE
				{
				mT_DELETE(); 

				}
				break;
			case 66 :
				// Meta.g:1:540: T_WINDOW
				{
				mT_WINDOW(); 

				}
				break;
			case 67 :
				// Meta.g:1:549: T_LAST
				{
				mT_LAST(); 

				}
				break;
			case 68 :
				// Meta.g:1:556: T_ROWS
				{
				mT_ROWS(); 

				}
				break;
			case 69 :
				// Meta.g:1:563: T_INNER
				{
				mT_INNER(); 

				}
				break;
			case 70 :
				// Meta.g:1:571: T_JOIN
				{
				mT_JOIN(); 

				}
				break;
			case 71 :
				// Meta.g:1:578: T_BY
				{
				mT_BY(); 

				}
				break;
			case 72 :
				// Meta.g:1:583: T_LIMIT
				{
				mT_LIMIT(); 

				}
				break;
			case 73 :
				// Meta.g:1:591: T_DISABLE
				{
				mT_DISABLE(); 

				}
				break;
			case 74 :
				// Meta.g:1:601: T_DISTINCT
				{
				mT_DISTINCT(); 

				}
				break;
			case 75 :
				// Meta.g:1:612: T_COUNT
				{
				mT_COUNT(); 

				}
				break;
			case 76 :
				// Meta.g:1:620: T_AS
				{
				mT_AS(); 

				}
				break;
			case 77 :
				// Meta.g:1:625: T_BETWEEN
				{
				mT_BETWEEN(); 

				}
				break;
			case 78 :
				// Meta.g:1:635: T_ASC
				{
				mT_ASC(); 

				}
				break;
			case 79 :
				// Meta.g:1:641: T_DESC
				{
				mT_DESC(); 

				}
				break;
			case 80 :
				// Meta.g:1:648: T_LIKE
				{
				mT_LIKE(); 

				}
				break;
			case 81 :
				// Meta.g:1:655: T_EPHEMERAL
				{
				mT_EPHEMERAL(); 

				}
				break;
			case 82 :
				// Meta.g:1:667: T_AT
				{
				mT_AT(); 

				}
				break;
			case 83 :
				// Meta.g:1:672: T_SEMICOLON
				{
				mT_SEMICOLON(); 

				}
				break;
			case 84 :
				// Meta.g:1:684: T_EQUAL
				{
				mT_EQUAL(); 

				}
				break;
			case 85 :
				// Meta.g:1:692: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 

				}
				break;
			case 86 :
				// Meta.g:1:709: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 

				}
				break;
			case 87 :
				// Meta.g:1:724: T_COLON
				{
				mT_COLON(); 

				}
				break;
			case 88 :
				// Meta.g:1:732: T_COMMA
				{
				mT_COMMA(); 

				}
				break;
			case 89 :
				// Meta.g:1:740: T_START_PARENTHESIS
				{
				mT_START_PARENTHESIS(); 

				}
				break;
			case 90 :
				// Meta.g:1:760: T_END_PARENTHESIS
				{
				mT_END_PARENTHESIS(); 

				}
				break;
			case 91 :
				// Meta.g:1:778: T_QUOTE
				{
				mT_QUOTE(); 

				}
				break;
			case 92 :
				// Meta.g:1:786: T_SINGLE_QUOTE
				{
				mT_SINGLE_QUOTE(); 

				}
				break;
			case 93 :
				// Meta.g:1:801: T_DEFAULT
				{
				mT_DEFAULT(); 

				}
				break;
			case 94 :
				// Meta.g:1:811: T_LUCENE
				{
				mT_LUCENE(); 

				}
				break;
			case 95 :
				// Meta.g:1:820: T_CUSTOM
				{
				mT_CUSTOM(); 

				}
				break;
			case 96 :
				// Meta.g:1:829: T_START_BRACKET
				{
				mT_START_BRACKET(); 

				}
				break;
			case 97 :
				// Meta.g:1:845: T_END_BRACKET
				{
				mT_END_BRACKET(); 

				}
				break;
			case 98 :
				// Meta.g:1:859: T_PLUS
				{
				mT_PLUS(); 

				}
				break;
			case 99 :
				// Meta.g:1:866: T_SUBTRACT
				{
				mT_SUBTRACT(); 

				}
				break;
			case 100 :
				// Meta.g:1:877: T_INTERROGATION
				{
				mT_INTERROGATION(); 

				}
				break;
			case 101 :
				// Meta.g:1:893: T_ASTERISK
				{
				mT_ASTERISK(); 

				}
				break;
			case 102 :
				// Meta.g:1:904: T_GROUP
				{
				mT_GROUP(); 

				}
				break;
			case 103 :
				// Meta.g:1:912: T_AGGREGATION
				{
				mT_AGGREGATION(); 

				}
				break;
			case 104 :
				// Meta.g:1:926: T_MAX
				{
				mT_MAX(); 

				}
				break;
			case 105 :
				// Meta.g:1:932: T_MIN
				{
				mT_MIN(); 

				}
				break;
			case 106 :
				// Meta.g:1:938: T_AVG
				{
				mT_AVG(); 

				}
				break;
			case 107 :
				// Meta.g:1:944: T_GT
				{
				mT_GT(); 

				}
				break;
			case 108 :
				// Meta.g:1:949: T_LT
				{
				mT_LT(); 

				}
				break;
			case 109 :
				// Meta.g:1:954: T_GTE
				{
				mT_GTE(); 

				}
				break;
			case 110 :
				// Meta.g:1:960: T_LTE
				{
				mT_LTE(); 

				}
				break;
			case 111 :
				// Meta.g:1:966: T_NOT_EQUAL
				{
				mT_NOT_EQUAL(); 

				}
				break;
			case 112 :
				// Meta.g:1:978: T_TOKEN
				{
				mT_TOKEN(); 

				}
				break;
			case 113 :
				// Meta.g:1:986: T_SECONDS
				{
				mT_SECONDS(); 

				}
				break;
			case 114 :
				// Meta.g:1:996: T_MINUTES
				{
				mT_MINUTES(); 

				}
				break;
			case 115 :
				// Meta.g:1:1006: T_HOURS
				{
				mT_HOURS(); 

				}
				break;
			case 116 :
				// Meta.g:1:1014: T_DAYS
				{
				mT_DAYS(); 

				}
				break;
			case 117 :
				// Meta.g:1:1021: QUOTED_LITERAL
				{
				mQUOTED_LITERAL(); 

				}
				break;
			case 118 :
				// Meta.g:1:1036: T_CONSTANT
				{
				mT_CONSTANT(); 

				}
				break;
			case 119 :
				// Meta.g:1:1047: T_IDENT
				{
				mT_IDENT(); 

				}
				break;
			case 120 :
				// Meta.g:1:1055: T_KS_AND_TN
				{
				mT_KS_AND_TN(); 

				}
				break;
			case 121 :
				// Meta.g:1:1067: T_TERM
				{
				mT_TERM(); 

				}
				break;
			case 122 :
				// Meta.g:1:1074: T_FLOAT
				{
				mT_FLOAT(); 

				}
				break;
			case 123 :
				// Meta.g:1:1082: T_PATH
				{
				mT_PATH(); 

				}
				break;
			case 124 :
				// Meta.g:1:1089: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA16 dfa16 = new DFA16(this);
	protected DFA19 dfa19 = new DFA19(this);
	static final String DFA16_eotS =
		"\5\uffff";
	static final String DFA16_eofS =
		"\5\uffff";
	static final String DFA16_minS =
		"\2\56\3\uffff";
	static final String DFA16_maxS =
		"\1\71\1\145\3\uffff";
	static final String DFA16_acceptS =
		"\2\uffff\1\2\1\1\1\3";
	static final String DFA16_specialS =
		"\5\uffff}>";
	static final String[] DFA16_transitionS = {
			"\1\2\1\uffff\12\1",
			"\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
			"",
			"",
			""
	};

	static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
	static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
	static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
	static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
	static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
	static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
	static final short[][] DFA16_transition;

	static {
		int numStates = DFA16_transitionS.length;
		DFA16_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
		}
	}

	protected class DFA16 extends DFA {

		public DFA16(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 16;
			this.eot = DFA16_eot;
			this.eof = DFA16_eof;
			this.min = DFA16_min;
			this.max = DFA16_max;
			this.accept = DFA16_accept;
			this.special = DFA16_special;
			this.transition = DFA16_transition;
		}
		@Override
		public String getDescription() {
			return "223:1: T_FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
		}
	}

	static final String DFA19_eotS =
		"\1\uffff\1\66\1\71\1\100\1\102\1\105\1\110\1\111\1\112\1\113\22\114\12"+
		"\uffff\1\174\3\uffff\1\176\2\uffff\1\114\1\u0081\1\u0084\1\u0085\1\114"+
		"\2\u0086\3\uffff\2\u0086\1\uffff\5\114\1\u0086\1\uffff\1\114\1\uffff\2"+
		"\114\1\uffff\2\114\5\uffff\15\114\1\u00ae\6\114\1\u00b7\1\u00b8\7\114"+
		"\1\u00c6\17\114\1\u00da\1\114\3\uffff\1\114\7\uffff\4\u0086\1\64\1\uffff"+
		"\6\114\1\u00e8\1\114\1\u00eb\1\u00ec\1\u00ee\6\114\1\u00f7\12\114\1\u0102"+
		"\1\u0103\1\114\1\u0105\1\u0106\1\uffff\1\u0107\1\114\1\u0109\1\u010a\1"+
		"\u010c\3\114\2\uffff\10\114\1\u0118\1\114\1\u011a\2\114\1\uffff\1\u011d"+
		"\2\114\1\u0120\17\114\1\uffff\2\114\2\u0086\1\64\1\u008c\1\u0135\1\114"+
		"\1\u0137\3\114\1\u013b\1\uffff\1\u00e8\1\114\2\uffff\1\114\1\uffff\2\114"+
		"\1\u0140\2\114\1\u0143\2\114\1\uffff\1\114\1\u0147\10\114\2\uffff\1\114"+
		"\3\uffff\1\114\2\uffff\1\114\1\uffff\1\u0153\4\114\1\u0158\5\114\1\uffff"+
		"\1\114\1\uffff\2\114\1\uffff\2\114\1\uffff\1\u0163\2\114\1\u0166\1\114"+
		"\1\u0168\1\u0169\1\114\1\u016b\3\114\1\u016f\1\114\1\u0171\2\114\1\u0086"+
		"\1\64\1\u008c\1\uffff\1\114\1\uffff\3\114\1\uffff\1\u0179\3\114\1\uffff"+
		"\2\114\1\uffff\1\114\1\u0180\1\u0181\1\uffff\1\u0182\3\114\1\u0186\2\114"+
		"\1\u0189\3\114\1\uffff\1\114\1\u018e\1\u018f\1\114\1\uffff\1\u0191\4\114"+
		"\1\u0196\2\114\1\u0199\1\u019a\1\uffff\2\114\1\uffff\1\u019d\2\uffff\1"+
		"\114\1\uffff\3\114\1\uffff\1\114\1\uffff\1\114\1\u01a4\1\u008c\1\u01a5"+
		"\3\114\1\uffff\1\114\1\u01aa\4\114\3\uffff\1\u01af\2\114\1\uffff\1\114"+
		"\1\u01b3\1\uffff\3\114\1\u01b7\2\uffff\1\u01b8\1\uffff\1\u01b9\3\114\1"+
		"\uffff\1\u01bd\1\114\2\uffff\1\u01bf\1\114\1\uffff\1\u01c2\2\114\1\u01c5"+
		"\1\u01c6\1\114\2\uffff\1\u01c8\1\u01c9\1\114\1\u01cb\1\uffff\1\u01cc\1"+
		"\u01cd\1\114\1\u01cf\1\uffff\1\114\1\u01d1\1\114\1\uffff\3\114\3\uffff"+
		"\1\u01d6\2\114\1\uffff\1\u01d9\1\uffff\2\114\1\uffff\1\u01dc\1\u01dd\2"+
		"\uffff\1\u01de\2\uffff\1\u01df\3\uffff\1\u01e0\1\uffff\1\114\1\uffff\3"+
		"\114\1\u01e5\1\uffff\2\114\1\uffff\2\114\5\uffff\2\114\1\u01ec\1\114\1"+
		"\uffff\1\114\1\u01ef\1\u01f0\2\114\1\u01f3\1\uffff\2\114\2\uffff\1\114"+
		"\1\u01f7\1\uffff\1\u01f8\1\u01f9\1\114\3\uffff\1\u01fb\1\uffff";
	static final String DFA19_eofS =
		"\u01fc\uffff";
	static final String DFA19_minS =
		"\1\11\33\55\12\uffff\1\0\3\uffff\1\55\2\uffff\1\55\2\75\4\55\3\uffff\1"+
		"\55\1\53\1\uffff\6\55\1\uffff\1\55\1\uffff\2\55\1\uffff\2\55\5\uffff\57"+
		"\55\3\uffff\1\55\7\uffff\2\55\1\53\1\55\1\60\1\uffff\41\55\1\uffff\10"+
		"\55\2\uffff\15\55\1\uffff\23\55\1\uffff\2\55\1\53\1\55\1\60\10\55\1\uffff"+
		"\2\55\2\uffff\1\55\1\uffff\10\55\1\uffff\12\55\2\uffff\1\55\3\uffff\1"+
		"\55\2\uffff\1\55\1\uffff\13\55\1\uffff\1\55\1\uffff\2\55\1\uffff\2\55"+
		"\1\uffff\22\55\1\60\1\55\1\uffff\1\55\1\uffff\3\55\1\uffff\4\55\1\uffff"+
		"\2\55\1\uffff\3\55\1\uffff\13\55\1\uffff\4\55\1\uffff\12\55\1\uffff\2"+
		"\55\1\uffff\1\55\2\uffff\1\55\1\uffff\3\55\1\uffff\1\55\1\uffff\7\55\1"+
		"\uffff\6\55\3\uffff\3\55\1\uffff\2\55\1\uffff\4\55\2\uffff\1\55\1\uffff"+
		"\4\55\1\uffff\2\55\2\uffff\2\55\1\uffff\6\55\2\uffff\4\55\1\uffff\4\55"+
		"\1\uffff\3\55\1\uffff\3\55\3\uffff\3\55\1\uffff\1\55\1\uffff\2\55\1\uffff"+
		"\2\55\2\uffff\1\55\2\uffff\1\55\3\uffff\1\55\1\uffff\1\55\1\uffff\4\55"+
		"\1\uffff\2\55\1\uffff\2\55\5\uffff\4\55\1\uffff\6\55\1\uffff\2\55\2\uffff"+
		"\2\55\1\uffff\3\55\3\uffff\1\55\1\uffff";
	static final String DFA19_maxS =
		"\1\175\33\172\12\uffff\1\uffff\3\uffff\1\172\2\uffff\1\172\1\75\1\76\4"+
		"\172\3\uffff\2\172\1\uffff\6\172\1\uffff\1\172\1\uffff\2\172\1\uffff\2"+
		"\172\5\uffff\57\172\3\uffff\1\172\7\uffff\4\172\1\71\1\uffff\41\172\1"+
		"\uffff\10\172\2\uffff\15\172\1\uffff\23\172\1\uffff\4\172\1\71\10\172"+
		"\1\uffff\2\172\2\uffff\1\172\1\uffff\10\172\1\uffff\12\172\2\uffff\1\172"+
		"\3\uffff\1\172\2\uffff\1\172\1\uffff\13\172\1\uffff\1\172\1\uffff\2\172"+
		"\1\uffff\2\172\1\uffff\22\172\1\71\1\172\1\uffff\1\172\1\uffff\3\172\1"+
		"\uffff\4\172\1\uffff\2\172\1\uffff\3\172\1\uffff\13\172\1\uffff\4\172"+
		"\1\uffff\12\172\1\uffff\2\172\1\uffff\1\172\2\uffff\1\172\1\uffff\3\172"+
		"\1\uffff\1\172\1\uffff\7\172\1\uffff\6\172\3\uffff\3\172\1\uffff\2\172"+
		"\1\uffff\4\172\2\uffff\1\172\1\uffff\4\172\1\uffff\2\172\2\uffff\2\172"+
		"\1\uffff\6\172\2\uffff\4\172\1\uffff\4\172\1\uffff\3\172\1\uffff\3\172"+
		"\3\uffff\3\172\1\uffff\1\172\1\uffff\2\172\1\uffff\2\172\2\uffff\1\172"+
		"\2\uffff\1\172\3\uffff\1\172\1\uffff\1\172\1\uffff\4\172\1\uffff\2\172"+
		"\1\uffff\2\172\5\uffff\4\172\1\uffff\6\172\1\uffff\2\172\2\uffff\2\172"+
		"\1\uffff\3\172\3\uffff\1\172\1\uffff";
	static final String DFA19_acceptS =
		"\34\uffff\1\122\1\123\1\124\1\125\1\126\1\127\1\130\1\131\1\132\1\133"+
		"\1\uffff\1\140\1\141\1\142\1\uffff\1\144\1\145\7\uffff\1\173\1\174\1\1"+
		"\2\uffff\1\2\6\uffff\1\3\1\uffff\1\4\2\uffff\1\5\2\uffff\1\6\1\7\1\10"+
		"\1\11\1\167\57\uffff\1\134\1\165\1\143\1\uffff\1\155\1\153\1\156\1\157"+
		"\1\154\1\166\1\171\5\uffff\1\172\41\uffff\1\114\10\uffff\1\22\1\77\15"+
		"\uffff\1\57\23\uffff\1\107\15\uffff\1\170\2\uffff\1\150\1\151\1\uffff"+
		"\1\26\10\uffff\1\40\12\uffff\1\34\1\24\1\uffff\1\35\1\62\1\116\1\uffff"+
		"\1\152\1\64\1\uffff\1\16\13\uffff\1\25\1\uffff\1\53\2\uffff\1\37\2\uffff"+
		"\1\47\24\uffff\1\20\1\uffff\1\117\3\uffff\1\164\4\uffff\1\56\2\uffff\1"+
		"\31\3\uffff\1\61\13\uffff\1\17\4\uffff\1\66\12\uffff\1\100\2\uffff\1\51"+
		"\1\uffff\1\120\1\103\1\uffff\1\46\3\uffff\1\104\1\uffff\1\106\7\uffff"+
		"\1\163\6\uffff\1\21\1\41\1\160\3\uffff\1\113\2\uffff\1\14\4\uffff\1\76"+
		"\1\50\1\uffff\1\105\4\uffff\1\60\2\uffff\1\72\1\32\2\uffff\1\110\6\uffff"+
		"\1\146\1\101\4\uffff\1\73\4\uffff\1\13\3\uffff\1\137\3\uffff\1\102\1\65"+
		"\1\23\3\uffff\1\75\1\uffff\1\36\2\uffff\1\136\2\uffff\1\52\1\74\1\uffff"+
		"\1\135\1\111\1\uffff\1\162\1\161\1\70\1\uffff\1\55\1\uffff\1\67\4\uffff"+
		"\1\45\2\uffff\1\27\2\uffff\1\54\1\63\1\115\1\112\1\12\4\uffff\1\15\6\uffff"+
		"\1\30\2\uffff\1\121\1\43\2\uffff\1\71\3\uffff\1\33\1\147\1\42\1\uffff"+
		"\1\44";
	static final String DFA19_specialS =
		"\46\uffff\1\0\u01d5\uffff}>";
	static final String[] DFA19_transitionS = {
			"\2\65\2\uffff\1\65\22\uffff\1\65\1\uffff\1\45\4\uffff\1\46\1\43\1\44"+
			"\1\54\1\51\1\42\1\52\1\62\1\64\1\60\1\1\10\60\1\41\1\35\1\57\1\36\1\56"+
			"\1\53\1\34\1\14\1\33\1\13\1\2\1\21\1\24\1\55\1\3\1\20\1\32\1\15\1\26"+
			"\1\4\1\16\1\23\1\27\1\25\1\30\1\5\1\12\1\22\1\31\1\17\3\61\1\47\1\uffff"+
			"\1\50\1\uffff\1\63\1\uffff\1\14\1\33\1\13\1\6\1\21\1\24\1\55\1\7\1\20"+
			"\1\32\1\15\1\26\1\10\1\16\1\23\1\27\1\25\1\30\1\11\1\12\1\22\1\31\1\17"+
			"\3\61\1\37\1\uffff\1\40",
			"\1\64\1\67\1\64\12\60\7\uffff\4\63\1\70\25\63\4\uffff\1\63\1\uffff\4"+
			"\63\1\70\25\63",
			"\1\64\1\77\1\64\12\76\7\uffff\1\75\3\76\1\73\3\76\1\74\10\76\1\72\10"+
			"\76\4\uffff\1\76\1\uffff\1\75\3\76\1\73\3\76\1\74\10\76\1\72\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\101\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\101\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\103\7\76\1\104\21\76\4\uffff\1\76\1"+
			"\uffff\1\103\7\76\1\104\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\106\16\76\1\107\6\76\4\uffff\1"+
			"\76\1\uffff\4\76\1\106\16\76\1\107\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\75\3\76\1\73\3\76\1\74\10\76\1\72\10"+
			"\76\4\uffff\1\76\1\uffff\1\75\3\76\1\73\3\76\1\74\10\76\1\72\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\101\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\101\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\103\7\76\1\104\21\76\4\uffff\1\76\1"+
			"\uffff\1\103\7\76\1\104\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\106\16\76\1\107\6\76\4\uffff\1"+
			"\76\1\uffff\4\76\1\106\16\76\1\107\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\116\6\76\1\120\6\76\1\122\2\76\1\115"+
			"\4\76\1\117\1\76\1\121\1\76\4\uffff\1\76\1\uffff\1\116\6\76\1\120\6\76"+
			"\1\122\2\76\1\115\4\76\1\117\1\76\1\121\1\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\125\2\76\1\124\2\76\1\123\2\76"+
			"\1\126\5\76\4\uffff\1\76\1\uffff\13\76\1\125\2\76\1\124\2\76\1\123\2"+
			"\76\1\126\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\131\2\76\1\133\4\76\1\127\1\76"+
			"\1\130\4\76\1\132\2\76\1\134\4\76\4\uffff\1\76\1\uffff\3\76\1\131\2\76"+
			"\1\133\4\76\1\127\1\76\1\130\4\76\1\132\2\76\1\134\4\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\135\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\135\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\136\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\136\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\7\76\1\140\1\137\21\76\4\uffff\1\76\1"+
			"\uffff\7\76\1\140\1\137\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\5\76\1\141\7\76\1\142\14\76\4\uffff\1"+
			"\76\1\uffff\5\76\1\141\7\76\1\142\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\144\16\76\1\145\7\76\1\143\2\76\4\uffff"+
			"\1\76\1\uffff\1\144\16\76\1\145\7\76\1\143\2\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\147\13\76\1\150\2\76\1\146\7\76"+
			"\4\uffff\1\76\1\uffff\3\76\1\147\13\76\1\150\2\76\1\146\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\152\1\76\1\151\1\76\1\153\10\76"+
			"\4\uffff\1\76\1\uffff\15\76\1\152\1\76\1\151\1\76\1\153\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\154\15\76\1\155\2\76\1\156\10\76\4\uffff"+
			"\1\76\1\uffff\1\154\15\76\1\155\2\76\1\156\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\157\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\157\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\162\7\76\1\161\5\76\1\160\5\76\1\163"+
			"\5\76\4\uffff\1\76\1\uffff\1\162\7\76\1\161\5\76\1\160\5\76\1\163\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\164\5\76\1\165\10\76\4\uffff\1"+
			"\76\1\uffff\13\76\1\164\5\76\1\165\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\166\11\76\1\167\13\76\4\uffff\1"+
			"\76\1\uffff\4\76\1\166\11\76\1\167\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\170\31\76\4\uffff\1\76\1\uffff\1\170"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\171\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\171\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\173\23\76\1\172\1\76\4\uffff\1"+
			"\76\1\uffff\4\76\1\173\23\76\1\172\1\76",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\0\175",
			"",
			"",
			"",
			"\15\64\7\uffff\32\64\4\uffff\1\64\1\uffff\32\64",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\177\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\177\10\76",
			"\1\u0080",
			"\1\u0082\1\u0083",
			"\1\64\1\67\1\64\12\60\7\uffff\4\63\1\70\25\63\4\uffff\1\63\1\uffff\4"+
			"\63\1\70\25\63",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\63\1\64\12\u0087\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\1\64\1\63\1\64\12\63\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"",
			"",
			"",
			"\1\64\1\63\1\64\12\u0088\7\uffff\4\63\1\u0089\25\63\4\uffff\1\63\1\uffff"+
			"\4\63\1\u0089\25\63",
			"\1\u008c\1\uffff\1\u008b\1\63\1\64\12\u008a\7\uffff\32\63\4\uffff\1"+
			"\63\1\uffff\32\63",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u008d\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u008d\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\5\76\1\u0090\5\76\1\u008e\6\76\1\u008f"+
			"\7\76\4\uffff\1\76\1\uffff\5\76\1\u0090\5\76\1\u008e\6\76\1\u008f\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u0091\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u0091\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\30\76\1\u0092\1\76\4\uffff\1\76\1\uffff"+
			"\30\76\1\u0092\1\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\63\1\64\12\63\7\uffff\32\u0093\4\uffff\1\63\1\uffff\32\u0093",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u0094\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u0094\5\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\27\76\1\u0095\2\76\4\uffff\1\76\1\uffff"+
			"\27\76\1\u0095\2\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u0096\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u0096\14\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u0099\10\76\1\u0098\7\76\1\u0097"+
			"\6\76\4\uffff\1\76\1\uffff\2\76\1\u0099\10\76\1\u0098\7\76\1\u0097\6"+
			"\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u009a\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u009a\13\76",
			"",
			"",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u009c\13\76\1\u009b\5\76\4\uffff"+
			"\1\76\1\uffff\10\76\1\u009c\13\76\1\u009b\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\76\1\u009d\30\76\4\uffff\1\76\1\uffff"+
			"\1\76\1\u009d\30\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u009e\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u009e\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u009f\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u009f\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\17\76\1\u00a0\12\76\4\uffff\1\76\1\uffff"+
			"\17\76\1\u00a0\12\76",
			"\1\64\1\77\1\64\12\76\7\uffff\12\76\1\u00a1\17\76\4\uffff\1\76\1\uffff"+
			"\12\76\1\u00a1\17\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00a2\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00a2\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u00a4\1\u00a3\6\76\1\u00a5\5\76"+
			"\4\uffff\1\76\1\uffff\14\76\1\u00a4\1\u00a3\6\76\1\u00a5\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u00a6\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u00a6\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u00a7\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u00a7\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u00a9\7\76\1\u00a8\6\76\4\uffff"+
			"\1\76\1\uffff\13\76\1\u00a9\7\76\1\u00a8\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u00ab\2\76\1\u00aa\24\76\1\u00ac\1\76"+
			"\4\uffff\1\76\1\uffff\1\u00ab\2\76\1\u00aa\24\76\1\u00ac\1\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\u00ad\26\76\4\uffff\1\76\1\uffff"+
			"\3\76\1\u00ad\26\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u00af\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u00af\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u00b0\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u00b0\23\76",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u00b1\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u00b1\23\76",
			"\1\64\1\77\1\64\12\76\7\uffff\30\76\1\u00b2\1\76\4\uffff\1\76\1\uffff"+
			"\30\76\1\u00b2\1\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u00b3\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u00b3\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u00b5\5\76\1\u00b4\6\76\4\uffff"+
			"\1\76\1\uffff\15\76\1\u00b5\5\76\1\u00b4\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00b6\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00b6\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\u00b9\11\76\1\u00bc\4\76\1\u00ba"+
			"\1\u00bb\6\76\4\uffff\1\76\1\uffff\3\76\1\u00b9\11\76\1\u00bc\4\76\1"+
			"\u00ba\1\u00bb\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u00bd\6\76\1\u00be\12\76\4\uffff"+
			"\1\76\1\uffff\10\76\1\u00bd\6\76\1\u00be\12\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u00bf\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u00bf\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\7\76\1\u00c0\22\76\4\uffff\1\76\1\uffff"+
			"\7\76\1\u00c0\22\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00c1\3\76\1\u00c2\21\76\4\uffff"+
			"\1\76\1\uffff\4\76\1\u00c1\3\76\1\u00c2\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\5\76\1\u00c3\24\76\4\uffff\1\76\1\uffff"+
			"\5\76\1\u00c3\24\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\u00c4\26\76\4\uffff\1\76\1\uffff"+
			"\3\76\1\u00c4\26\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u00c5\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u00c5\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00c7\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00c7\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\u00c8\26\76\4\uffff\1\76\1\uffff"+
			"\3\76\1\u00c8\26\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u00c9\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u00c9\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u00ca\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u00ca\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u00cb\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u00cb\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u00cc\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u00cc\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u00cd\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u00cd\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\12\76\1\u00d0\1\76\1\u00cf\5\76\1\u00ce"+
			"\7\76\4\uffff\1\76\1\uffff\12\76\1\u00d0\1\76\1\u00cf\5\76\1\u00ce\7"+
			"\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u00d1\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u00d1\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u00d2\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u00d2\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u00d3\31\76\4\uffff\1\76\1\uffff\1\u00d3"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u00d5\5\76\1\u00d4\13\76\4\uffff"+
			"\1\76\1\uffff\10\76\1\u00d5\5\76\1\u00d4\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u00d6\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u00d6\15\76",
			"\1\64\1\77\1\64\12\76\7\uffff\26\76\1\u00d7\3\76\4\uffff\1\76\1\uffff"+
			"\26\76\1\u00d7\3\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u00d8\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u00d8\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u00d9\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u00d9\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u00db\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u00db\6\76",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u00dc\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u00dc\13\76",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\64\1\63\1\64\12\u0087\7\uffff\4\63\1\u00dd\25\63\4\uffff\1\63\1\uffff"+
			"\4\63\1\u00dd\25\63",
			"\1\64\1\63\1\64\12\u0088\7\uffff\4\63\1\u0089\25\63\4\uffff\1\63\1\uffff"+
			"\4\63\1\u0089\25\63",
			"\1\u008c\1\uffff\1\u00df\1\63\1\64\12\u00de\7\uffff\32\63\4\uffff\1"+
			"\63\1\uffff\32\63",
			"\1\64\1\63\1\64\12\u008a\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\u00e0",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\17\76\1\u00e1\12\76\4\uffff\1\76\1\uffff"+
			"\17\76\1\u00e1\12\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00e2\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00e2\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u00e3\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u00e3\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u00e4\31\76\4\uffff\1\76\1\uffff\1\u00e4"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u00e5\22\76\1\u00e6\6\76\4\uffff\1\76"+
			"\1\uffff\1\u00e5\22\76\1\u00e6\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u00e7\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u00e7\7\76",
			"\1\64\1\63\1\64\12\u00e9\7\uffff\32\u00e9\4\uffff\1\u00e9\1\uffff\32"+
			"\u00e9",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u00ea\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u00ea\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u00ed\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u00ed\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00ef\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00ef\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u00f0\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u00f0\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\17\76\1\u00f1\1\76\1\u00f2\10\76\4\uffff"+
			"\1\76\1\uffff\17\76\1\u00f1\1\76\1\u00f2\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00f4\10\76\1\u00f3\14\76\4\uffff"+
			"\1\76\1\uffff\4\76\1\u00f4\10\76\1\u00f3\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u00f5\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u00f5\23\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u00f6\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u00f6\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00f8\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00f8\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00f9\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00f9\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u00fa\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u00fa\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u00fb\31\76\4\uffff\1\76\1\uffff\1\u00fb"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u00fc\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u00fc\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\17\76\1\u00fd\12\76\4\uffff\1\76\1\uffff"+
			"\17\76\1\u00fd\12\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u00fe\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u00fe\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u00ff\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u00ff\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u0100\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u0100\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0101\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0101\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u0104\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u0104\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u0108\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u0108\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u010b\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u010b\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\7\76\1\u010d\22\76\4\uffff\1\76\1\uffff"+
			"\7\76\1\u010d\22\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\u010e\26\76\4\uffff\1\76\1\uffff"+
			"\3\76\1\u010e\26\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u010f\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u010f\10\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0110\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0110\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0111\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0111\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u0112\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u0112\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0113\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0113\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u0114\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u0114\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u0115\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u0115\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\7\76\1\u0116\22\76\4\uffff\1\76\1\uffff"+
			"\7\76\1\u0116\22\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0117\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0117\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u0119\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u0119\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u011b\31\76\4\uffff\1\76\1\uffff\1\u011b"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u011c\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u011c\21\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u011e\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u011e\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u011f\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u011f\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u0121\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u0121\15\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u0122\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u0122\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u0123\31\76\4\uffff\1\76\1\uffff\1\u0123"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u0124\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u0124\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u0125\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u0125\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0126\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0126\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u0127\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u0127\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0128\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0128\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u0129\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u0129\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u012a\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u012a\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u012b\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u012b\15\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u012c\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u012c\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u012d\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u012d\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u012e\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u012e\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u012f\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u012f\14\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\26\76\1\u0130\3\76\4\uffff\1\76\1\uffff"+
			"\26\76\1\u0130\3\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u0131\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u0131\5\76",
			"\1\u008c\1\uffff\1\u0133\1\63\1\64\12\u0132\7\uffff\32\63\4\uffff\1"+
			"\63\1\uffff\32\63",
			"\1\64\1\63\1\64\12\u00de\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\u0134",
			"\3\64\12\u00e0\7\uffff\32\64\4\uffff\1\64\1\uffff\32\64",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u0136\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u0136\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u0138\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u0138\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\76\1\u0139\30\76\4\uffff\1\76\1\uffff"+
			"\1\76\1\u0139\30\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u013a\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u013a\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\63\1\64\12\u00e9\7\uffff\32\u00e9\4\uffff\1\u00e9\1\uffff\32"+
			"\u00e9",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u013c\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u013c\7\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u013d\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u013d\6\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u013e\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u013e\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u013f\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u013f\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u0141\31\76\4\uffff\1\76\1\uffff\1\u0141"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u0142\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u0142\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u0144\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u0144\23\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0145\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0145\25\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0146\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0146\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u0148\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u0148\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u0149\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u0149\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u014a\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u014a\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u014b\31\76\4\uffff\1\76\1\uffff\1\u014b"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u014c\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u014c\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u014d\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u014d\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u014e\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u014e\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u014f\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u014f\10\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\30\76\1\u0150\1\76\4\uffff\1\76\1\uffff"+
			"\30\76\1\u0150\1\76",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0151\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0151\25\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\17\76\1\u0152\12\76\4\uffff\1\76\1\uffff"+
			"\17\76\1\u0152\12\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u0154\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u0154\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0155\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0155\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\27\76\1\u0156\2\76\4\uffff\1\76\1\uffff"+
			"\27\76\1\u0156\2\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u0157\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u0157\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u0159\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u0159\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u015a\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u015a\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u015b\31\76\4\uffff\1\76\1\uffff\1\u015b"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\u015c\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u015d\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u015d\15\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u015e\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u015e\23\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u015f\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u015f\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u0160\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u0160\13\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u0161\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u0161\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0162\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0162\25\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u0164\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u0164\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u0165\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u0165\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u0167\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u0167\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u016a\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u016a\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u016c\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u016c\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u016d\31\76\4\uffff\1\76\1\uffff\1\u016d"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\25\76\1\u016e\4\76\4\uffff\1\76\1\uffff"+
			"\25\76\1\u016e\4\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0170\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0170\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0172\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0172\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\17\76\1\u0173\12\76\4\uffff\1\76\1\uffff"+
			"\17\76\1\u0173\12\76",
			"\1\64\1\63\1\64\12\u0132\7\uffff\32\63\4\uffff\1\63\1\uffff\32\63",
			"\12\u0174",
			"\3\64\12\u0134\7\uffff\32\64\4\uffff\1\64\1\uffff\32\64",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0175\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0175\25\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u0176\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u0176\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u0177\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u0177\16\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u0178\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u0178\14\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u017a\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u017a\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u017b\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u017b\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\3\76\1\u017c\26\76\4\uffff\1\76\1\uffff"+
			"\3\76\1\u017c\26\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u017d\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u017d\23\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u017e\31\76\4\uffff\1\76\1\uffff\1\u017e"+
			"\31\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u017f\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u017f\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0183\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0183\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u0184\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u0184\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u0185\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u0185\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0187\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0187\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u0188\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u0188\15\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u018a\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u018a\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u018b\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u018b\23\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u018c\31\76\4\uffff\1\76\1\uffff\1\u018c"+
			"\31\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\26\76\1\u018d\3\76\4\uffff\1\76\1\uffff"+
			"\26\76\1\u018d\3\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u0190\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u0190\6\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u0192\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u0192\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u0193\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u0193\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\20\76\1\u0194\11\76\4\uffff\1\76\1\uffff"+
			"\20\76\1\u0194\11\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0195\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0195\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u0197\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u0197\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u0198\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u0198\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u019b\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u019b\15\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\u019c\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u019e\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u019e\25\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u019f\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u019f\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u01a0\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u01a0\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01a1\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01a1\25\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u01a2\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u01a2\7\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01a3\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01a3\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\3\64\12\u0174\7\uffff\32\64\4\uffff\1\64\1\uffff\32\64",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u01a6\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u01a6\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01a7\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01a7\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u01a8\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u01a8\27\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u01a9\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u01a9\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u01ab\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u01ab\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01ac\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01ac\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u01ad\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u01ad\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u01ae\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u01ae\10\76",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u01b0\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u01b0\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u01b1\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u01b1\6\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u01b2\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u01b2\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u01b4\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u01b4\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u01b5\31\76\4\uffff\1\76\1\uffff\1\u01b5"+
			"\31\76",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u01b6\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u01b6\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u01ba\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u01ba\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u01bb\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u01bb\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u01bc\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u01bc\10\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u01be\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u01be\7\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u01c0\1\76\1\u01c1\11\76\4\uffff"+
			"\1\76\1\uffff\16\76\1\u01c0\1\76\1\u01c1\11\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u01c3\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u01c3\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\30\76\1\u01c4\1\76\4\uffff\1\76\1\uffff"+
			"\30\76\1\u01c4\1\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u01c7\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u01c7\14\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u01ca\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u01ca\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01ce\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01ce\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01d0\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01d0\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u01d2\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u01d2\21\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u01d3\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u01d3\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\23\76\1\u01d4\6\76\4\uffff\1\76\1\uffff"+
			"\23\76\1\u01d4\6\76",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01d5\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01d5\25\76",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u01d7\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u01d7\13\76",
			"\1\64\1\77\1\64\12\76\7\uffff\1\u01d8\31\76\4\uffff\1\76\1\uffff\1\u01d8"+
			"\31\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u01da\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u01da\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u01db\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u01db\5\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u01e1\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u01e1\14\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u01e2\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u01e2\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\22\76\1\u01e3\7\76\4\uffff\1\76\1\uffff"+
			"\22\76\1\u01e3\7\76",
			"\1\64\1\77\1\64\12\76\7\uffff\10\76\1\u01e4\21\76\4\uffff\1\76\1\uffff"+
			"\10\76\1\u01e4\21\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u01e6\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u01e6\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\13\76\1\u01e7\16\76\4\uffff\1\76\1\uffff"+
			"\13\76\1\u01e7\16\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\4\76\1\u01e8\25\76\4\uffff\1\76\1\uffff"+
			"\4\76\1\u01e8\25\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u01e9\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u01e9\13\76",
			"",
			"",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\2\76\1\u01ea\27\76\4\uffff\1\76\1\uffff"+
			"\2\76\1\u01ea\27\76",
			"\1\64\1\77\1\64\12\76\7\uffff\6\76\1\u01eb\23\76\4\uffff\1\76\1\uffff"+
			"\6\76\1\u01eb\23\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\16\76\1\u01ed\13\76\4\uffff\1\76\1\uffff"+
			"\16\76\1\u01ed\13\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u01ee\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u01ee\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\21\76\1\u01f1\10\76\4\uffff\1\76\1\uffff"+
			"\21\76\1\u01f1\10\76",
			"\1\64\1\77\1\64\12\76\7\uffff\30\76\1\u01f2\1\76\4\uffff\1\76\1\uffff"+
			"\30\76\1\u01f2\1\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\15\76\1\u01f4\14\76\4\uffff\1\76\1\uffff"+
			"\15\76\1\u01f4\14\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u01f5\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u01f5\15\76",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\24\76\1\u01f6\5\76\4\uffff\1\76\1\uffff"+
			"\24\76\1\u01f6\5\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			"\1\64\1\77\1\64\12\76\7\uffff\14\76\1\u01fa\15\76\4\uffff\1\76\1\uffff"+
			"\14\76\1\u01fa\15\76",
			"",
			"",
			"",
			"\1\64\1\77\1\64\12\76\7\uffff\32\76\4\uffff\1\76\1\uffff\32\76",
			""
	};

	static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
	static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
	static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
	static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
	static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
	static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
	static final short[][] DFA19_transition;

	static {
		int numStates = DFA19_transitionS.length;
		DFA19_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
		}
	}

	protected class DFA19 extends DFA {

		public DFA19(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 19;
			this.eot = DFA19_eot;
			this.eof = DFA19_eof;
			this.min = DFA19_min;
			this.max = DFA19_max;
			this.accept = DFA19_accept;
			this.special = DFA19_special;
			this.transition = DFA19_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__149 | T__150 | T__151 | T__152 | T__153 | T__154 | T__155 | T__156 | T__157 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_WHERE | T_IN | T_FROM | T_DELETE | T_WINDOW | T_LAST | T_ROWS | T_INNER | T_JOIN | T_BY | T_LIMIT | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_EPHEMERAL | T_AT | T_SEMICOLON | T_EQUAL | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_PARENTHESIS | T_END_PARENTHESIS | T_QUOTE | T_SINGLE_QUOTE | T_DEFAULT | T_LUCENE | T_CUSTOM | T_START_BRACKET | T_END_BRACKET | T_PLUS | T_SUBTRACT | T_INTERROGATION | T_ASTERISK | T_GROUP | T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_TOKEN | T_SECONDS | T_MINUTES | T_HOURS | T_DAYS | QUOTED_LITERAL | T_CONSTANT | T_IDENT | T_KS_AND_TN | T_TERM | T_FLOAT | T_PATH | WS );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA19_38 = input.LA(1);
						s = -1;
						if ( ((LA19_38 >= '\u0000' && LA19_38 <= '\uFFFF')) ) {s = 125;}
						else s = 124;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 19, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
