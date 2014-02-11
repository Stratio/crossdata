// $ANTLR 3.5.1 Meta.g 2014-02-11 16:01:15

    package com.stratio.sdh.meta.generated;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaLexer extends Lexer {
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

	// $ANTLR start "T__84"
	public final void mT__84() throws RecognitionException {
		try {
			int _type = T__84;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:14:7: ( '(' )
			// Meta.g:14:9: '('
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
	// $ANTLR end "T__84"

	// $ANTLR start "T__85"
	public final void mT__85() throws RecognitionException {
		try {
			int _type = T__85;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:15:7: ( ')' )
			// Meta.g:15:9: ')'
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
	// $ANTLR end "T__85"

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// Meta.g:72:11: ( ( 'a' | 'A' ) )
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
			// Meta.g:73:11: ( ( 'b' | 'B' ) )
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
			// Meta.g:74:11: ( ( 'c' | 'C' ) )
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
			// Meta.g:75:11: ( ( 'd' | 'D' ) )
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
			// Meta.g:76:11: ( ( 'e' | 'E' ) )
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
			// Meta.g:77:11: ( ( 'f' | 'F' ) )
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
			// Meta.g:78:11: ( ( 'g' | 'G' ) )
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
			// Meta.g:79:11: ( ( 'h' | 'H' ) )
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
			// Meta.g:80:11: ( ( 'i' | 'I' ) )
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
			// Meta.g:81:11: ( ( 'j' | 'J' ) )
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
			// Meta.g:82:11: ( ( 'k' | 'K' ) )
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
			// Meta.g:83:11: ( ( 'l' | 'L' ) )
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
			// Meta.g:84:11: ( ( 'm' | 'M' ) )
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
			// Meta.g:85:11: ( ( 'n' | 'N' ) )
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
			// Meta.g:86:11: ( ( 'o' | 'O' ) )
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
			// Meta.g:87:11: ( ( 'p' | 'P' ) )
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
			// Meta.g:88:11: ( ( 'q' | 'Q' ) )
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
			// Meta.g:89:11: ( ( 'r' | 'R' ) )
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
			// Meta.g:90:11: ( ( 's' | 'S' ) )
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
			// Meta.g:91:11: ( ( 't' | 'T' ) )
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
			// Meta.g:92:11: ( ( 'u' | 'U' ) )
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
			// Meta.g:93:11: ( ( 'v' | 'V' ) )
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
			// Meta.g:94:11: ( ( 'w' | 'W' ) )
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
			// Meta.g:95:11: ( ( 'x' | 'X' ) )
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
			// Meta.g:96:11: ( ( 'y' | 'Y' ) )
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
			// Meta.g:97:11: ( ( 'z' | 'Z' ) )
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
			// Meta.g:98:19: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
			// Meta.g:98:21: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Meta.g:98:31: ( '+' | '-' )?
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

			// Meta.g:98:42: ( '0' .. '9' )+
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

	// $ANTLR start "T_TRUNCATE"
	public final void mT_TRUNCATE() throws RecognitionException {
		try {
			int _type = T_TRUNCATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:101:11: ( T R U N C A T E )
			// Meta.g:101:13: T R U N C A T E
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
			// Meta.g:102:9: ( C R E A T E )
			// Meta.g:102:11: C R E A T E
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
			// Meta.g:103:8: ( A L T E R )
			// Meta.g:103:10: A L T E R
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
			// Meta.g:104:11: ( K E Y S P A C E )
			// Meta.g:104:13: K E Y S P A C E
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
			// Meta.g:105:6: ( N O T )
			// Meta.g:105:8: N O T
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
			// Meta.g:106:7: ( W I T H )
			// Meta.g:106:9: W I T H
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
			// Meta.g:107:7: ( D R O P )
			// Meta.g:107:9: D R O P
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
			// Meta.g:108:8: ( T A B L E )
			// Meta.g:108:10: T A B L E
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
			// Meta.g:109:5: ( I F )
			// Meta.g:109:7: I F
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
			// Meta.g:110:9: ( E X I S T S )
			// Meta.g:110:11: E X I S T S
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
			// Meta.g:111:6: ( A N D )
			// Meta.g:111:8: A N D
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
			// Meta.g:112:6: ( U S E )
			// Meta.g:112:8: U S E
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
			// Meta.g:113:6: ( S E T )
			// Meta.g:113:8: S E T
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
			// Meta.g:114:10: ( O P T I O N S )
			// Meta.g:114:12: O P T I O N S
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
			// Meta.g:115:12: ( A N A L Y T I C S )
			// Meta.g:115:14: A N A L Y T I C S
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
			// Meta.g:116:7: ( T R U E )
			// Meta.g:116:9: T R U E
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
			// Meta.g:117:8: ( F A L S E )
			// Meta.g:117:10: F A L S E
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
			// Meta.g:118:14: ( C O N S I S T E N C Y )
			// Meta.g:118:16: C O N S I S T E N C Y
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
			// Meta.g:119:6: ( A L L )
			// Meta.g:119:8: A L L
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
			// Meta.g:120:6: ( A N Y )
			// Meta.g:120:8: A N Y
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
			// Meta.g:121:9: ( Q U O R U M )
			// Meta.g:121:11: Q U O R U M
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
			// Meta.g:122:6: ( O N E )
			// Meta.g:122:8: O N E
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
			// Meta.g:123:6: ( T W O )
			// Meta.g:123:8: T W O
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
			// Meta.g:124:8: ( T H R E E )
			// Meta.g:124:10: T H R E E
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
			// Meta.g:125:14: ( E A C H '_' Q U O R U M )
			// Meta.g:125:16: E A C H '_' Q U O R U M
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
			// Meta.g:126:12: ( L O C A L '_' O N E )
			// Meta.g:126:14: L O C A L '_' O N E
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
			// Meta.g:127:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:127:17: L O C A L '_' Q U O R U M
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
			// Meta.g:128:10: ( E X P L A I N )
			// Meta.g:128:12: E X P L A I N
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
			// Meta.g:129:7: ( P L A N )
			// Meta.g:129:9: P L A N
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
			// Meta.g:130:6: ( F O R )
			// Meta.g:130:8: F O R
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

	// $ANTLR start "T_STOP"
	public final void mT_STOP() throws RecognitionException {
		try {
			int _type = T_STOP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:131:7: ( S T O P )
			// Meta.g:131:9: S T O P
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

	// $ANTLR start "T_PROCESS"
	public final void mT_PROCESS() throws RecognitionException {
		try {
			int _type = T_PROCESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:132:10: ( P R O C E S S )
			// Meta.g:132:12: P R O C E S S
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
			// Meta.g:134:10: ( T R I G G E R )
			// Meta.g:134:12: T R I G G E R
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

	// $ANTLR start "T_ON"
	public final void mT_ON() throws RecognitionException {
		try {
			int _type = T_ON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:135:5: ( O N )
			// Meta.g:135:7: O N
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
			// Meta.g:137:8: ( U S I N G )
			// Meta.g:137:10: U S I N G
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
			// Meta.g:138:7: ( T Y P E )
			// Meta.g:138:9: T Y P E
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
			// Meta.g:139:6: ( A D D )
			// Meta.g:139:8: A D D
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
			// Meta.g:141:10: ( P R I M A R Y )
			// Meta.g:141:12: P R I M A R Y
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
			// Meta.g:142:6: ( K E Y )
			// Meta.g:142:8: K E Y
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

	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:145:12: ( ';' )
			// Meta.g:145:14: ';'
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
			// Meta.g:146:8: ( '=' )
			// Meta.g:146:10: '='
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

	// $ANTLR start "T_POINT"
	public final void mT_POINT() throws RecognitionException {
		try {
			int _type = T_POINT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:147:8: ( '.' )
			// Meta.g:147:10: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_POINT"

	// $ANTLR start "T_START_SBRACKET"
	public final void mT_START_SBRACKET() throws RecognitionException {
		try {
			int _type = T_START_SBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:148:17: ( '{' )
			// Meta.g:148:19: '{'
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
			// Meta.g:149:15: ( '}' )
			// Meta.g:149:17: '}'
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
			// Meta.g:150:8: ( ':' )
			// Meta.g:150:10: ':'
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
			// Meta.g:151:8: ( ',' )
			// Meta.g:151:10: ','
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

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// Meta.g:153:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
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
			// Meta.g:154:15: ( '0' .. '9' )
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

	// $ANTLR start "T_CONSTANT"
	public final void mT_CONSTANT() throws RecognitionException {
		try {
			int _type = T_CONSTANT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:156:11: ( ( DIGIT )+ )
			// Meta.g:156:13: ( DIGIT )+
			{
			// Meta.g:156:13: ( DIGIT )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
					alt3=1;
				}

				switch (alt3) {
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
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
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
			// Meta.g:158:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:158:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:158:17: ( LETTER | DIGIT | '_' )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '0' && LA4_0 <= '9')||(LA4_0 >= 'A' && LA4_0 <= 'Z')||LA4_0=='_'||(LA4_0 >= 'a' && LA4_0 <= 'z')) ) {
					alt4=1;
				}

				switch (alt4) {
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
					break loop4;
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

	// $ANTLR start "T_TERM"
	public final void mT_TERM() throws RecognitionException {
		try {
			int _type = T_TERM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:160:7: ( ( LETTER | DIGIT | '_' | '.' )+ )
			// Meta.g:160:9: ( LETTER | DIGIT | '_' | '.' )+
			{
			// Meta.g:160:9: ( LETTER | DIGIT | '_' | '.' )+
			int cnt5=0;
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( (LA5_0=='.'||(LA5_0 >= '0' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
					alt5=1;
				}

				switch (alt5) {
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
					if ( cnt5 >= 1 ) break loop5;
					EarlyExitException eee = new EarlyExitException(5, input);
					throw eee;
				}
				cnt5++;
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
			// Meta.g:161:8: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
			int alt12=3;
			alt12 = dfa12.predict(input);
			switch (alt12) {
				case 1 :
					// Meta.g:161:12: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
					{
					// Meta.g:161:12: ( '0' .. '9' )+
					int cnt6=0;
					loop6:
					while (true) {
						int alt6=2;
						int LA6_0 = input.LA(1);
						if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
							alt6=1;
						}

						switch (alt6) {
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
							if ( cnt6 >= 1 ) break loop6;
							EarlyExitException eee = new EarlyExitException(6, input);
							throw eee;
						}
						cnt6++;
					}

					match('.'); 
					// Meta.g:161:28: ( '0' .. '9' )*
					loop7:
					while (true) {
						int alt7=2;
						int LA7_0 = input.LA(1);
						if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
							alt7=1;
						}

						switch (alt7) {
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
							break loop7;
						}
					}

					// Meta.g:161:40: ( EXPONENT )?
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0=='E'||LA8_0=='e') ) {
						alt8=1;
					}
					switch (alt8) {
						case 1 :
							// Meta.g:161:40: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:162:10: '.' ( '0' .. '9' )+ ( EXPONENT )?
					{
					match('.'); 
					// Meta.g:162:14: ( '0' .. '9' )+
					int cnt9=0;
					loop9:
					while (true) {
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( ((LA9_0 >= '0' && LA9_0 <= '9')) ) {
							alt9=1;
						}

						switch (alt9) {
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
							if ( cnt9 >= 1 ) break loop9;
							EarlyExitException eee = new EarlyExitException(9, input);
							throw eee;
						}
						cnt9++;
					}

					// Meta.g:162:26: ( EXPONENT )?
					int alt10=2;
					int LA10_0 = input.LA(1);
					if ( (LA10_0=='E'||LA10_0=='e') ) {
						alt10=1;
					}
					switch (alt10) {
						case 1 :
							// Meta.g:162:26: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 3 :
					// Meta.g:163:10: ( '0' .. '9' )+ EXPONENT
					{
					// Meta.g:163:10: ( '0' .. '9' )+
					int cnt11=0;
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
							if ( cnt11 >= 1 ) break loop11;
							EarlyExitException eee = new EarlyExitException(11, input);
							throw eee;
						}
						cnt11++;
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

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:429:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:429:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:429:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			int cnt13=0;
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( ((LA13_0 >= '\t' && LA13_0 <= '\n')||LA13_0=='\r'||LA13_0==' ') ) {
					alt13=1;
				}

				switch (alt13) {
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
					if ( cnt13 >= 1 ) break loop13;
					EarlyExitException eee = new EarlyExitException(13, input);
					throw eee;
				}
				cnt13++;
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
		// Meta.g:1:8: ( T__84 | T__85 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_STOP | T_PROCESS | T_TRIGGER | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_CONSTANT | T_IDENT | T_TERM | T_FLOAT | WS )
		int alt14=53;
		alt14 = dfa14.predict(input);
		switch (alt14) {
			case 1 :
				// Meta.g:1:10: T__84
				{
				mT__84(); 

				}
				break;
			case 2 :
				// Meta.g:1:16: T__85
				{
				mT__85(); 

				}
				break;
			case 3 :
				// Meta.g:1:22: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 4 :
				// Meta.g:1:33: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 5 :
				// Meta.g:1:42: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 6 :
				// Meta.g:1:50: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 7 :
				// Meta.g:1:61: T_NOT
				{
				mT_NOT(); 

				}
				break;
			case 8 :
				// Meta.g:1:67: T_WITH
				{
				mT_WITH(); 

				}
				break;
			case 9 :
				// Meta.g:1:74: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 10 :
				// Meta.g:1:81: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 11 :
				// Meta.g:1:89: T_IF
				{
				mT_IF(); 

				}
				break;
			case 12 :
				// Meta.g:1:94: T_EXISTS
				{
				mT_EXISTS(); 

				}
				break;
			case 13 :
				// Meta.g:1:103: T_AND
				{
				mT_AND(); 

				}
				break;
			case 14 :
				// Meta.g:1:109: T_USE
				{
				mT_USE(); 

				}
				break;
			case 15 :
				// Meta.g:1:115: T_SET
				{
				mT_SET(); 

				}
				break;
			case 16 :
				// Meta.g:1:121: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 17 :
				// Meta.g:1:131: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 18 :
				// Meta.g:1:143: T_TRUE
				{
				mT_TRUE(); 

				}
				break;
			case 19 :
				// Meta.g:1:150: T_FALSE
				{
				mT_FALSE(); 

				}
				break;
			case 20 :
				// Meta.g:1:158: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 21 :
				// Meta.g:1:172: T_ALL
				{
				mT_ALL(); 

				}
				break;
			case 22 :
				// Meta.g:1:178: T_ANY
				{
				mT_ANY(); 

				}
				break;
			case 23 :
				// Meta.g:1:184: T_QUORUM
				{
				mT_QUORUM(); 

				}
				break;
			case 24 :
				// Meta.g:1:193: T_ONE
				{
				mT_ONE(); 

				}
				break;
			case 25 :
				// Meta.g:1:199: T_TWO
				{
				mT_TWO(); 

				}
				break;
			case 26 :
				// Meta.g:1:205: T_THREE
				{
				mT_THREE(); 

				}
				break;
			case 27 :
				// Meta.g:1:213: T_EACH_QUORUM
				{
				mT_EACH_QUORUM(); 

				}
				break;
			case 28 :
				// Meta.g:1:227: T_LOCAL_ONE
				{
				mT_LOCAL_ONE(); 

				}
				break;
			case 29 :
				// Meta.g:1:239: T_LOCAL_QUORUM
				{
				mT_LOCAL_QUORUM(); 

				}
				break;
			case 30 :
				// Meta.g:1:254: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 31 :
				// Meta.g:1:264: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 32 :
				// Meta.g:1:271: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 33 :
				// Meta.g:1:277: T_STOP
				{
				mT_STOP(); 

				}
				break;
			case 34 :
				// Meta.g:1:284: T_PROCESS
				{
				mT_PROCESS(); 

				}
				break;
			case 35 :
				// Meta.g:1:294: T_TRIGGER
				{
				mT_TRIGGER(); 

				}
				break;
			case 36 :
				// Meta.g:1:304: T_ON
				{
				mT_ON(); 

				}
				break;
			case 37 :
				// Meta.g:1:309: T_USING
				{
				mT_USING(); 

				}
				break;
			case 38 :
				// Meta.g:1:317: T_TYPE
				{
				mT_TYPE(); 

				}
				break;
			case 39 :
				// Meta.g:1:324: T_ADD
				{
				mT_ADD(); 

				}
				break;
			case 40 :
				// Meta.g:1:330: T_PRIMARY
				{
				mT_PRIMARY(); 

				}
				break;
			case 41 :
				// Meta.g:1:340: T_KEY
				{
				mT_KEY(); 

				}
				break;
			case 42 :
				// Meta.g:1:346: T_SEMICOLON
				{
				mT_SEMICOLON(); 

				}
				break;
			case 43 :
				// Meta.g:1:358: T_EQUAL
				{
				mT_EQUAL(); 

				}
				break;
			case 44 :
				// Meta.g:1:366: T_POINT
				{
				mT_POINT(); 

				}
				break;
			case 45 :
				// Meta.g:1:374: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 

				}
				break;
			case 46 :
				// Meta.g:1:391: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 

				}
				break;
			case 47 :
				// Meta.g:1:406: T_COLON
				{
				mT_COLON(); 

				}
				break;
			case 48 :
				// Meta.g:1:414: T_COMMA
				{
				mT_COMMA(); 

				}
				break;
			case 49 :
				// Meta.g:1:422: T_CONSTANT
				{
				mT_CONSTANT(); 

				}
				break;
			case 50 :
				// Meta.g:1:433: T_IDENT
				{
				mT_IDENT(); 

				}
				break;
			case 51 :
				// Meta.g:1:441: T_TERM
				{
				mT_TERM(); 

				}
				break;
			case 52 :
				// Meta.g:1:448: T_FLOAT
				{
				mT_FLOAT(); 

				}
				break;
			case 53 :
				// Meta.g:1:456: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA12 dfa12 = new DFA12(this);
	protected DFA14 dfa14 = new DFA14(this);
	static final String DFA12_eotS =
		"\5\uffff";
	static final String DFA12_eofS =
		"\5\uffff";
	static final String DFA12_minS =
		"\2\56\3\uffff";
	static final String DFA12_maxS =
		"\1\71\1\145\3\uffff";
	static final String DFA12_acceptS =
		"\2\uffff\1\2\1\1\1\3";
	static final String DFA12_specialS =
		"\5\uffff}>";
	static final String[] DFA12_transitionS = {
			"\1\2\1\uffff\12\1",
			"\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
			"",
			"",
			""
	};

	static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
	static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
	static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
	static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
	static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
	static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
	static final short[][] DFA12_transition;

	static {
		int numStates = DFA12_transitionS.length;
		DFA12_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
		}
	}

	protected class DFA12 extends DFA {

		public DFA12(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 12;
			this.eot = DFA12_eot;
			this.eof = DFA12_eof;
			this.min = DFA12_min;
			this.max = DFA12_max;
			this.accept = DFA12_accept;
			this.special = DFA12_special;
			this.transition = DFA12_transition;
		}
		@Override
		public String getDescription() {
			return "161:1: T_FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
		}
	}

	static final String DFA14_eotS =
		"\3\uffff\20\36\2\uffff\1\74\4\uffff\1\76\1\36\3\uffff\17\36\1\123\6\36"+
		"\1\134\6\36\1\uffff\1\34\1\uffff\2\34\3\36\1\156\5\36\1\164\1\165\1\36"+
		"\1\167\1\170\1\171\1\173\2\36\1\uffff\3\36\1\u0081\1\36\1\u0083\2\36\1"+
		"\uffff\1\u0086\1\36\1\u0088\5\36\4\34\1\uffff\1\36\1\u0091\2\36\1\uffff"+
		"\1\36\1\u0095\3\36\2\uffff\1\36\3\uffff\1\36\1\uffff\1\u009b\1\u009c\3"+
		"\36\1\uffff\1\36\1\uffff\1\u00a1\1\36\1\uffff\1\36\1\uffff\2\36\1\u00a6"+
		"\2\36\2\34\1\36\1\uffff\1\36\1\u00ab\1\u00ac\1\uffff\2\36\1\u00af\2\36"+
		"\2\uffff\3\36\1\u00b5\1\uffff\1\36\1\u00b7\2\36\1\uffff\4\36\2\uffff\1"+
		"\u00be\1\36\1\uffff\2\36\1\u00c2\2\36\1\uffff\1\36\1\uffff\1\u00c6\4\36"+
		"\1\u00cc\1\uffff\3\36\1\uffff\1\u00d0\1\36\1\u00d2\1\uffff\2\36\1\u00d5"+
		"\1\u00d6\1\u00d7\1\uffff\2\36\1\u00da\1\uffff\1\36\1\uffff\2\36\3\uffff"+
		"\1\36\1\u00df\1\uffff\1\36\1\u00e1\2\36\1\uffff\1\36\1\uffff\1\36\1\u00e6"+
		"\1\u00e7\1\36\2\uffff\1\u00e9\1\uffff";
	static final String DFA14_eofS =
		"\u00ea\uffff";
	static final String DFA14_minS =
		"\1\11\2\uffff\20\56\2\uffff\1\56\4\uffff\2\56\3\uffff\35\56\1\uffff\1"+
		"\60\1\uffff\1\60\1\53\22\56\1\uffff\10\56\1\uffff\10\56\1\53\1\60\1\53"+
		"\1\60\1\uffff\4\56\1\uffff\5\56\2\uffff\1\56\3\uffff\1\56\1\uffff\5\56"+
		"\1\uffff\1\56\1\uffff\2\56\1\uffff\1\56\1\uffff\5\56\2\60\1\56\1\uffff"+
		"\3\56\1\uffff\5\56\2\uffff\4\56\1\uffff\4\56\1\uffff\4\56\2\uffff\2\56"+
		"\1\uffff\5\56\1\uffff\1\56\1\uffff\6\56\1\uffff\3\56\1\uffff\3\56\1\uffff"+
		"\5\56\1\uffff\3\56\1\uffff\1\56\1\uffff\2\56\3\uffff\2\56\1\uffff\4\56"+
		"\1\uffff\1\56\1\uffff\4\56\2\uffff\1\56\1\uffff";
	static final String DFA14_maxS =
		"\1\175\2\uffff\20\172\2\uffff\1\172\4\uffff\2\172\3\uffff\35\172\1\uffff"+
		"\1\145\1\uffff\1\145\1\71\22\172\1\uffff\10\172\1\uffff\10\172\1\71\1"+
		"\145\2\71\1\uffff\4\172\1\uffff\5\172\2\uffff\1\172\3\uffff\1\172\1\uffff"+
		"\5\172\1\uffff\1\172\1\uffff\2\172\1\uffff\1\172\1\uffff\5\172\2\71\1"+
		"\172\1\uffff\3\172\1\uffff\5\172\2\uffff\4\172\1\uffff\4\172\1\uffff\4"+
		"\172\2\uffff\2\172\1\uffff\5\172\1\uffff\1\172\1\uffff\6\172\1\uffff\3"+
		"\172\1\uffff\3\172\1\uffff\5\172\1\uffff\3\172\1\uffff\1\172\1\uffff\2"+
		"\172\3\uffff\2\172\1\uffff\4\172\1\uffff\1\172\1\uffff\4\172\2\uffff\1"+
		"\172\1\uffff";
	static final String DFA14_acceptS =
		"\1\uffff\1\1\1\2\20\uffff\1\52\1\53\1\uffff\1\55\1\56\1\57\1\60\2\uffff"+
		"\1\63\1\65\1\62\35\uffff\1\54\1\uffff\1\61\24\uffff\1\13\10\uffff\1\44"+
		"\14\uffff\1\64\4\uffff\1\31\5\uffff\1\25\1\15\1\uffff\1\26\1\47\1\51\1"+
		"\uffff\1\7\5\uffff\1\16\1\uffff\1\17\2\uffff\1\30\1\uffff\1\40\10\uffff"+
		"\1\22\3\uffff\1\46\5\uffff\1\10\1\11\4\uffff\1\41\4\uffff\1\37\4\uffff"+
		"\1\12\1\32\2\uffff\1\5\5\uffff\1\45\1\uffff\1\23\6\uffff\1\4\3\uffff\1"+
		"\14\3\uffff\1\27\5\uffff\1\43\3\uffff\1\36\1\uffff\1\20\2\uffff\1\42\1"+
		"\50\1\3\2\uffff\1\6\4\uffff\1\21\1\uffff\1\34\4\uffff\1\24\1\33\1\uffff"+
		"\1\35";
	static final String DFA14_specialS =
		"\u00ea\uffff}>";
	static final String[] DFA14_transitionS = {
			"\2\35\2\uffff\1\35\22\uffff\1\35\7\uffff\1\1\1\2\2\uffff\1\31\1\uffff"+
			"\1\25\1\uffff\12\32\1\30\1\23\1\uffff\1\24\3\uffff\1\5\1\33\1\4\1\11"+
			"\1\13\1\17\2\33\1\12\1\33\1\6\1\21\1\33\1\7\1\16\1\22\1\20\1\33\1\15"+
			"\1\3\1\14\1\33\1\10\3\33\4\uffff\1\34\1\uffff\1\5\1\33\1\4\1\11\1\13"+
			"\1\17\2\33\1\12\1\33\1\6\1\21\1\33\1\7\1\16\1\22\1\20\1\33\1\15\1\3\1"+
			"\14\1\33\1\10\3\33\1\26\1\uffff\1\27",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\1\40\6\44\1\42\11\44\1\37\4\44\1\41\1\44"+
			"\1\43\1\44\4\uffff\1\44\1\uffff\1\40\6\44\1\42\11\44\1\37\4\44\1\41\1"+
			"\44\1\43\1\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\46\2\44\1\45\10\44\4\uffff\1\44"+
			"\1\uffff\16\44\1\46\2\44\1\45\10\44",
			"\1\34\1\uffff\12\44\7\uffff\3\44\1\51\7\44\1\47\1\44\1\50\14\44\4\uffff"+
			"\1\44\1\uffff\3\44\1\51\7\44\1\47\1\44\1\50\14\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\52\25\44\4\uffff\1\44\1\uffff\4\44"+
			"\1\52\25\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\53\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\53\13\44",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\54\21\44\4\uffff\1\44\1\uffff\10"+
			"\44\1\54\21\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\55\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\55\10\44",
			"\1\34\1\uffff\12\44\7\uffff\5\44\1\56\24\44\4\uffff\1\44\1\uffff\5\44"+
			"\1\56\24\44",
			"\1\34\1\uffff\12\44\7\uffff\1\60\26\44\1\57\2\44\4\uffff\1\44\1\uffff"+
			"\1\60\26\44\1\57\2\44",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\61\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\61\7\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\62\16\44\1\63\6\44\4\uffff\1\44\1"+
			"\uffff\4\44\1\62\16\44\1\63\6\44",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\65\1\44\1\64\12\44\4\uffff\1\44"+
			"\1\uffff\15\44\1\65\1\44\1\64\12\44",
			"\1\34\1\uffff\12\44\7\uffff\1\66\15\44\1\67\13\44\4\uffff\1\44\1\uffff"+
			"\1\66\15\44\1\67\13\44",
			"\1\34\1\uffff\12\44\7\uffff\24\44\1\70\5\44\4\uffff\1\44\1\uffff\24"+
			"\44\1\70\5\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\71\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\71\13\44",
			"\1\34\1\uffff\12\44\7\uffff\13\44\1\72\5\44\1\73\10\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\72\5\44\1\73\10\44",
			"",
			"",
			"\1\34\1\uffff\12\75\7\uffff\32\34\4\uffff\1\34\1\uffff\32\34",
			"",
			"",
			"",
			"",
			"\1\77\1\uffff\12\32\7\uffff\4\34\1\100\25\34\4\uffff\1\34\1\uffff\4"+
			"\34\1\100\25\34",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\102\13\44\1\101\5\44\4\uffff\1\44"+
			"\1\uffff\10\44\1\102\13\44\1\101\5\44",
			"\1\34\1\uffff\12\44\7\uffff\1\44\1\103\30\44\4\uffff\1\44\1\uffff\1"+
			"\44\1\103\30\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\104\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\104\13\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\105\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\105\10\44",
			"\1\34\1\uffff\12\44\7\uffff\17\44\1\106\12\44\4\uffff\1\44\1\uffff\17"+
			"\44\1\106\12\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\107\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\107\25\44",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\110\14\44\4\uffff\1\44\1\uffff\15"+
			"\44\1\110\14\44",
			"\1\34\1\uffff\12\44\7\uffff\13\44\1\112\7\44\1\111\6\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\112\7\44\1\111\6\44",
			"\1\34\1\uffff\12\44\7\uffff\1\114\2\44\1\113\24\44\1\115\1\44\4\uffff"+
			"\1\44\1\uffff\1\114\2\44\1\113\24\44\1\115\1\44",
			"\1\34\1\uffff\12\44\7\uffff\3\44\1\116\26\44\4\uffff\1\44\1\uffff\3"+
			"\44\1\116\26\44",
			"\1\34\1\uffff\12\44\7\uffff\30\44\1\117\1\44\4\uffff\1\44\1\uffff\30"+
			"\44\1\117\1\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\120\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\120\6\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\121\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\121\6\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\122\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\122\13\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\124\6\44\1\125\12\44\4\uffff\1\44"+
			"\1\uffff\10\44\1\124\6\44\1\125\12\44",
			"\1\34\1\uffff\12\44\7\uffff\2\44\1\126\27\44\4\uffff\1\44\1\uffff\2"+
			"\44\1\126\27\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\127\3\44\1\130\21\44\4\uffff\1\44"+
			"\1\uffff\4\44\1\127\3\44\1\130\21\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\131\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\131\6\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\132\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\132\13\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\133\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\133\6\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\135\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\135\25\44",
			"\1\34\1\uffff\12\44\7\uffff\13\44\1\136\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\136\16\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\137\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\137\10\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\140\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\140\13\44",
			"\1\34\1\uffff\12\44\7\uffff\2\44\1\141\27\44\4\uffff\1\44\1\uffff\2"+
			"\44\1\141\27\44",
			"\1\34\1\uffff\12\44\7\uffff\1\142\31\44\4\uffff\1\44\1\uffff\1\142\31"+
			"\44",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\144\5\44\1\143\13\44\4\uffff\1\44"+
			"\1\uffff\10\44\1\144\5\44\1\143\13\44",
			"",
			"\12\75\13\uffff\1\145\37\uffff\1\145",
			"",
			"\12\146\13\uffff\1\147\37\uffff\1\147",
			"\1\151\1\uffff\1\151\2\uffff\12\150",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\153\10\44\1\152\14\44\4\uffff\1\44"+
			"\1\uffff\4\44\1\153\10\44\1\152\14\44",
			"\1\34\1\uffff\12\44\7\uffff\6\44\1\154\23\44\4\uffff\1\44\1\uffff\6"+
			"\44\1\154\23\44",
			"\1\34\1\uffff\12\44\7\uffff\13\44\1\155\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\155\16\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\157\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\157\25\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\160\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\160\25\44",
			"\1\34\1\uffff\12\44\7\uffff\1\161\31\44\4\uffff\1\44\1\uffff\1\161\31"+
			"\44",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\162\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\162\7\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\163\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\163\25\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\13\44\1\166\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\166\16\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\172\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\172\7\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\7\44\1\174\22\44\4\uffff\1\44\1\uffff\7"+
			"\44\1\174\22\44",
			"\1\34\1\uffff\12\44\7\uffff\17\44\1\175\12\44\4\uffff\1\44\1\uffff\17"+
			"\44\1\175\12\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\176\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\176\7\44",
			"\1\34\1\uffff\12\44\7\uffff\13\44\1\177\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\177\16\44",
			"\1\34\1\uffff\12\44\7\uffff\7\44\1\u0080\22\44\4\uffff\1\44\1\uffff"+
			"\7\44\1\u0080\22\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\u0082\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u0082\14\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\17\44\1\u0084\12\44\4\uffff\1\44\1\uffff"+
			"\17\44\1\u0084\12\44",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\u0085\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u0085\21\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\u0087\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u0087\7\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\u0089\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u0089\10\44",
			"\1\34\1\uffff\12\44\7\uffff\1\u008a\31\44\4\uffff\1\44\1\uffff\1\u008a"+
			"\31\44",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\u008b\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u008b\14\44",
			"\1\34\1\uffff\12\44\7\uffff\2\44\1\u008c\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u008c\27\44",
			"\1\34\1\uffff\12\44\7\uffff\14\44\1\u008d\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u008d\15\44",
			"\1\151\1\uffff\1\151\2\uffff\12\u008e",
			"\12\146\13\uffff\1\147\37\uffff\1\147",
			"\1\151\1\uffff\1\151\2\uffff\12\u008f",
			"\12\150",
			"",
			"\1\34\1\uffff\12\44\7\uffff\2\44\1\u0090\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u0090\27\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\6\44\1\u0092\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u0092\23\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u0093\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u0093\25\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u0094\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u0094\25\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\u0096\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u0096\6\44",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\u0097\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u0097\21\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\u0098\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u0098\10\44",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\30\44\1\u0099\1\44\4\uffff\1\44\1\uffff"+
			"\30\44\1\u0099\1\44",
			"",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\17\44\1\u009a\12\44\4\uffff\1\44\1\uffff"+
			"\17\44\1\u009a\12\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\u009d\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u009d\6\44",
			"\1\34\1\uffff\12\44\7\uffff\1\u009e\31\44\4\uffff\1\44\1\uffff\1\u009e"+
			"\31\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\u009f\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\6\44\1\u00a0\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u00a0\23\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\u00a2\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00a2\13\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00a3\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00a3\25\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\24\44\1\u00a4\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00a4\5\44",
			"\1\34\1\uffff\12\44\7\uffff\13\44\1\u00a5\16\44\4\uffff\1\44\1\uffff"+
			"\13\44\1\u00a5\16\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00a7\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00a7\25\44",
			"\1\34\1\uffff\12\44\7\uffff\1\u00a8\31\44\4\uffff\1\44\1\uffff\1\u00a8"+
			"\31\44",
			"\12\u008e",
			"\12\u008f",
			"\1\34\1\uffff\12\44\7\uffff\1\u00a9\31\44\4\uffff\1\44\1\uffff\1\u00a9"+
			"\31\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00aa\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00aa\25\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00ad\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00ad\25\44",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\u00ae\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00ae\7\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\u00b0\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00b0\6\44",
			"\1\34\1\uffff\12\44\7\uffff\1\u00b1\31\44\4\uffff\1\44\1\uffff\1\u00b1"+
			"\31\44",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\u00b2\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00b2\7\44",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\u00b3\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00b3\21\44",
			"\1\34\1\uffff\12\44\7\uffff\20\44\1\u00b4\11\44\4\uffff\1\44\1\uffff"+
			"\20\44\1\u00b4\11\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\u00b6\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00b6\14\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\14\44\1\u00b8\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00b8\15\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\u00b9\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\u00ba\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00ba\7\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\u00bb\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00bb\10\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\u00bc\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00bc\6\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\u00bd\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00bd\10\44",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\23\44\1\u00bf\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00bf\6\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\10\44\1\u00c0\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00c0\21\44",
			"\1\34\1\uffff\12\44\7\uffff\2\44\1\u00c1\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00c1\27\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\u00c3\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00c3\14\44",
			"\1\34\1\uffff\12\44\7\uffff\24\44\1\u00c4\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00c4\5\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\u00c5\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00c5\7\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\u00c7\1\44\1\u00c8\11\44\4\uffff"+
			"\1\44\1\uffff\16\44\1\u00c7\1\44\1\u00c8\11\44",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\u00c9\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00c9\7\44",
			"\1\34\1\uffff\12\44\7\uffff\30\44\1\u00ca\1\44\4\uffff\1\44\1\uffff"+
			"\30\44\1\u00ca\1\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00cb\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00cb\25\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00cd\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00cd\25\44",
			"\1\34\1\uffff\12\44\7\uffff\2\44\1\u00ce\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00ce\27\44",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00cf\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00cf\25\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\u00d1\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00d1\13\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\u00d3\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00d3\14\44",
			"\1\34\1\uffff\12\44\7\uffff\24\44\1\u00d4\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00d4\5\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\15\44\1\u00d8\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00d8\14\44",
			"\1\34\1\uffff\12\44\7\uffff\22\44\1\u00d9\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00d9\7\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\u00db\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00db\10\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\4\44\1\u00dc\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00dc\25\44",
			"\1\34\1\uffff\12\44\7\uffff\16\44\1\u00dd\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00dd\13\44",
			"",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\2\44\1\u00de\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00de\27\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\24\44\1\u00e0\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00e0\5\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\21\44\1\u00e2\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00e2\10\44",
			"\1\34\1\uffff\12\44\7\uffff\30\44\1\u00e3\1\44\4\uffff\1\44\1\uffff"+
			"\30\44\1\u00e3\1\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\14\44\1\u00e4\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00e4\15\44",
			"",
			"\1\34\1\uffff\12\44\7\uffff\24\44\1\u00e5\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00e5\5\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\34\1\uffff\12\44\7\uffff\14\44\1\u00e8\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00e8\15\44",
			"",
			"",
			"\1\34\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			""
	};

	static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
	static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
	static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
	static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
	static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
	static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
	static final short[][] DFA14_transition;

	static {
		int numStates = DFA14_transitionS.length;
		DFA14_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
		}
	}

	protected class DFA14 extends DFA {

		public DFA14(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 14;
			this.eot = DFA14_eot;
			this.eof = DFA14_eof;
			this.min = DFA14_min;
			this.max = DFA14_max;
			this.accept = DFA14_accept;
			this.special = DFA14_special;
			this.transition = DFA14_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__84 | T__85 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_STOP | T_PROCESS | T_TRIGGER | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_CONSTANT | T_IDENT | T_TERM | T_FLOAT | WS );";
		}
	}

}
