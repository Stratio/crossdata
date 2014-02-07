<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
// $ANTLR 3.5.1 Meta.g 2014-02-07 10:17:30
=======
// $ANTLR 3.5.1 Meta.g 2014-02-07 23:33:25
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

    package com.stratio.sdh.meta.generated;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaLexer extends Lexer {
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	public static final int T_COLON=31;
	public static final int T_COMMA=32;
	public static final int T_CONSISTENCY=33;
	public static final int T_CONSTANT=34;
	public static final int T_CREATE=35;
	public static final int T_DROP=36;
	public static final int T_EACH_QUORUM=37;
	public static final int T_END_SBRACKET=38;
	public static final int T_EQUAL=39;
	public static final int T_EXISTS=40;
	public static final int T_EXPLAIN=41;
	public static final int T_FALSE=42;
	public static final int T_FOR=43;
	public static final int T_IDENT=44;
	public static final int T_IF=45;
	public static final int T_KEYSPACE=46;
	public static final int T_LOCAL_ONE=47;
	public static final int T_LOCAL_QUORUM=48;
	public static final int T_NOT=49;
	public static final int T_ON=50;
	public static final int T_ONE=51;
	public static final int T_OPTIONS=52;
	public static final int T_PLAN=53;
	public static final int T_POINT=54;
	public static final int T_PROCESS=55;
	public static final int T_QUORUM=56;
	public static final int T_SEMICOLON=57;
	public static final int T_SET=58;
	public static final int T_START_SBRACKET=59;
	public static final int T_STOP=60;
	public static final int T_TABLE=61;
	public static final int T_TERM=62;
	public static final int T_THREE=63;
	public static final int T_TRIGGER=64;
	public static final int T_TRUE=65;
	public static final int T_TRUNCATE=66;
	public static final int T_TWO=67;
	public static final int T_USE=68;
	public static final int T_USING=69;
	public static final int T_WITH=70;
	public static final int U=71;
	public static final int V=72;
	public static final int W=73;
	public static final int WS=74;
	public static final int X=75;
	public static final int Y=76;
	public static final int Z=77;
=======
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
	public static final int T_ONE=55;
	public static final int T_OPTIONS=56;
	public static final int T_ORDER=57;
	public static final int T_PLAN=58;
	public static final int T_POINT=59;
	public static final int T_QUORUM=60;
	public static final int T_SELECT=61;
	public static final int T_SEMICOLON=62;
	public static final int T_SET=63;
	public static final int T_START_BRACKET=64;
	public static final int T_START_SBRACKET=65;
	public static final int T_STORAGE=66;
	public static final int T_TABLE=67;
	public static final int T_TERM=68;
	public static final int T_THREE=69;
	public static final int T_TRUE=70;
	public static final int T_TRUNCATE=71;
	public static final int T_TWO=72;
	public static final int T_USE=73;
	public static final int T_USING=74;
	public static final int T_VALUES=75;
	public static final int T_WITH=76;
	public static final int U=77;
	public static final int V=78;
	public static final int W=79;
	public static final int WS=80;
	public static final int X=81;
	public static final int Y=82;
	public static final int Z=83;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

	    


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

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:68:11: ( ( 'a' | 'A' ) )
=======
			// Meta.g:71:11: ( ( 'a' | 'A' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:69:11: ( ( 'b' | 'B' ) )
=======
			// Meta.g:72:11: ( ( 'b' | 'B' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:70:11: ( ( 'c' | 'C' ) )
=======
			// Meta.g:73:11: ( ( 'c' | 'C' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:71:11: ( ( 'd' | 'D' ) )
=======
			// Meta.g:74:11: ( ( 'd' | 'D' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:72:11: ( ( 'e' | 'E' ) )
=======
			// Meta.g:75:11: ( ( 'e' | 'E' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:73:11: ( ( 'f' | 'F' ) )
=======
			// Meta.g:76:11: ( ( 'f' | 'F' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:74:11: ( ( 'g' | 'G' ) )
=======
			// Meta.g:77:11: ( ( 'g' | 'G' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:75:11: ( ( 'h' | 'H' ) )
=======
			// Meta.g:78:11: ( ( 'h' | 'H' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:76:11: ( ( 'i' | 'I' ) )
=======
			// Meta.g:79:11: ( ( 'i' | 'I' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:77:11: ( ( 'j' | 'J' ) )
=======
			// Meta.g:80:11: ( ( 'j' | 'J' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:78:11: ( ( 'k' | 'K' ) )
=======
			// Meta.g:81:11: ( ( 'k' | 'K' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:79:11: ( ( 'l' | 'L' ) )
=======
			// Meta.g:82:11: ( ( 'l' | 'L' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:80:11: ( ( 'm' | 'M' ) )
=======
			// Meta.g:83:11: ( ( 'm' | 'M' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:81:11: ( ( 'n' | 'N' ) )
=======
			// Meta.g:84:11: ( ( 'n' | 'N' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:82:11: ( ( 'o' | 'O' ) )
=======
			// Meta.g:85:11: ( ( 'o' | 'O' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:83:11: ( ( 'p' | 'P' ) )
=======
			// Meta.g:86:11: ( ( 'p' | 'P' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:84:11: ( ( 'q' | 'Q' ) )
=======
			// Meta.g:87:11: ( ( 'q' | 'Q' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:85:11: ( ( 'r' | 'R' ) )
=======
			// Meta.g:88:11: ( ( 'r' | 'R' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:86:11: ( ( 's' | 'S' ) )
=======
			// Meta.g:89:11: ( ( 's' | 'S' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:87:11: ( ( 't' | 'T' ) )
=======
			// Meta.g:90:11: ( ( 't' | 'T' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:88:11: ( ( 'u' | 'U' ) )
=======
			// Meta.g:91:11: ( ( 'u' | 'U' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:89:11: ( ( 'v' | 'V' ) )
=======
			// Meta.g:92:11: ( ( 'v' | 'V' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:90:11: ( ( 'w' | 'W' ) )
=======
			// Meta.g:93:11: ( ( 'w' | 'W' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:91:11: ( ( 'x' | 'X' ) )
=======
			// Meta.g:94:11: ( ( 'x' | 'X' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:92:11: ( ( 'y' | 'Y' ) )
=======
			// Meta.g:95:11: ( ( 'y' | 'Y' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:93:11: ( ( 'z' | 'Z' ) )
=======
			// Meta.g:96:11: ( ( 'z' | 'Z' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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

	// $ANTLR start "T_TRUNCATE"
	public final void mT_TRUNCATE() throws RecognitionException {
		try {
			int _type = T_TRUNCATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:96:11: ( T R U N C A T E )
			// Meta.g:96:13: T R U N C A T E
=======
			// Meta.g:99:11: ( T R U N C A T E )
			// Meta.g:99:13: T R U N C A T E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:97:9: ( C R E A T E )
			// Meta.g:97:11: C R E A T E
=======
			// Meta.g:100:9: ( C R E A T E )
			// Meta.g:100:11: C R E A T E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:98:8: ( A L T E R )
			// Meta.g:98:10: A L T E R
=======
			// Meta.g:101:8: ( A L T E R )
			// Meta.g:101:10: A L T E R
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:99:11: ( K E Y S P A C E )
			// Meta.g:99:13: K E Y S P A C E
=======
			// Meta.g:102:11: ( K E Y S P A C E )
			// Meta.g:102:13: K E Y S P A C E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:100:6: ( N O T )
			// Meta.g:100:8: N O T
=======
			// Meta.g:103:6: ( N O T )
			// Meta.g:103:8: N O T
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:101:7: ( W I T H )
			// Meta.g:101:9: W I T H
=======
			// Meta.g:104:7: ( W I T H )
			// Meta.g:104:9: W I T H
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:102:7: ( D R O P )
			// Meta.g:102:9: D R O P
=======
			// Meta.g:105:7: ( D R O P )
			// Meta.g:105:9: D R O P
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:103:8: ( T A B L E )
			// Meta.g:103:10: T A B L E
=======
			// Meta.g:106:8: ( T A B L E )
			// Meta.g:106:10: T A B L E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:104:5: ( I F )
			// Meta.g:104:7: I F
=======
			// Meta.g:107:5: ( I F )
			// Meta.g:107:7: I F
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:105:9: ( E X I S T S )
			// Meta.g:105:11: E X I S T S
=======
			// Meta.g:108:9: ( E X I S T S )
			// Meta.g:108:11: E X I S T S
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:106:6: ( A N D )
			// Meta.g:106:8: A N D
=======
			// Meta.g:109:6: ( A N D )
			// Meta.g:109:8: A N D
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:107:6: ( U S E )
			// Meta.g:107:8: U S E
=======
			// Meta.g:110:6: ( U S E )
			// Meta.g:110:8: U S E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:108:6: ( S E T )
			// Meta.g:108:8: S E T
=======
			// Meta.g:111:6: ( S E T )
			// Meta.g:111:8: S E T
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:109:10: ( O P T I O N S )
			// Meta.g:109:12: O P T I O N S
=======
			// Meta.g:112:10: ( O P T I O N S )
			// Meta.g:112:12: O P T I O N S
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:110:12: ( A N A L Y T I C S )
			// Meta.g:110:14: A N A L Y T I C S
=======
			// Meta.g:113:12: ( A N A L Y T I C S )
			// Meta.g:113:14: A N A L Y T I C S
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:111:7: ( T R U E )
			// Meta.g:111:9: T R U E
=======
			// Meta.g:114:7: ( T R U E )
			// Meta.g:114:9: T R U E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:112:8: ( F A L S E )
			// Meta.g:112:10: F A L S E
=======
			// Meta.g:115:8: ( F A L S E )
			// Meta.g:115:10: F A L S E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:113:14: ( C O N S I S T E N C Y )
			// Meta.g:113:16: C O N S I S T E N C Y
=======
			// Meta.g:116:14: ( C O N S I S T E N C Y )
			// Meta.g:116:16: C O N S I S T E N C Y
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:114:6: ( A L L )
			// Meta.g:114:8: A L L
=======
			// Meta.g:117:6: ( A L L )
			// Meta.g:117:8: A L L
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:115:6: ( A N Y )
			// Meta.g:115:8: A N Y
=======
			// Meta.g:118:6: ( A N Y )
			// Meta.g:118:8: A N Y
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:116:9: ( Q U O R U M )
			// Meta.g:116:11: Q U O R U M
=======
			// Meta.g:119:9: ( Q U O R U M )
			// Meta.g:119:11: Q U O R U M
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:117:6: ( O N E )
			// Meta.g:117:8: O N E
=======
			// Meta.g:120:6: ( O N E )
			// Meta.g:120:8: O N E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:118:6: ( T W O )
			// Meta.g:118:8: T W O
=======
			// Meta.g:121:6: ( T W O )
			// Meta.g:121:8: T W O
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:119:8: ( T H R E E )
			// Meta.g:119:10: T H R E E
=======
			// Meta.g:122:8: ( T H R E E )
			// Meta.g:122:10: T H R E E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:120:14: ( E A C H '_' Q U O R U M )
			// Meta.g:120:16: E A C H '_' Q U O R U M
=======
			// Meta.g:123:14: ( E A C H '_' Q U O R U M )
			// Meta.g:123:16: E A C H '_' Q U O R U M
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:121:12: ( L O C A L '_' O N E )
			// Meta.g:121:14: L O C A L '_' O N E
=======
			// Meta.g:124:12: ( L O C A L '_' O N E )
			// Meta.g:124:14: L O C A L '_' O N E
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:122:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:122:17: L O C A L '_' Q U O R U M
=======
			// Meta.g:125:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:125:17: L O C A L '_' Q U O R U M
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:123:10: ( E X P L A I N )
			// Meta.g:123:12: E X P L A I N
=======
			// Meta.g:126:10: ( E X P L A I N )
			// Meta.g:126:12: E X P L A I N
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:124:7: ( P L A N )
			// Meta.g:124:9: P L A N
=======
			// Meta.g:127:7: ( P L A N )
			// Meta.g:127:9: P L A N
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:125:6: ( F O R )
			// Meta.g:125:8: F O R
=======
			// Meta.g:128:6: ( F O R )
			// Meta.g:128:8: F O R
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// $ANTLR start "T_STOP"
	public final void mT_STOP() throws RecognitionException {
		try {
			int _type = T_STOP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:126:7: ( S T O P )
			// Meta.g:126:9: S T O P
			{
			mS(); 

			mT(); 

			mO(); 

			mP(); 

=======
	// $ANTLR start "T_INSERT"
	public final void mT_INSERT() throws RecognitionException {
		try {
			int _type = T_INSERT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:129:9: ( I N S E R T )
			// Meta.g:129:11: I N S E R T
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
			// Meta.g:130:7: ( I N T O )
			// Meta.g:130:9: I N T O
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
			// Meta.g:131:10: ( C O M P A C T )
			// Meta.g:131:12: C O M P A C T
			{
			mC(); 

			mO(); 

			mM(); 

			mP(); 

			mA(); 

			mC(); 

			mT(); 

>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// $ANTLR end "T_STOP"

	// $ANTLR start "T_PROCESS"
	public final void mT_PROCESS() throws RecognitionException {
		try {
			int _type = T_PROCESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:127:10: ( P R O C E S S )
			// Meta.g:127:12: P R O C E S S
			{
			mP(); 

			mR(); 

			mO(); 

			mC(); 

			mE(); 

			mS(); 

			mS(); 
=======
	// $ANTLR end "T_COMPACT"

	// $ANTLR start "T_STORAGE"
	public final void mT_STORAGE() throws RecognitionException {
		try {
			int _type = T_STORAGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:132:10: ( S T O R A G E )
			// Meta.g:132:12: S T O R A G E
			{
			mS(); 

			mT(); 

			mO(); 

			mR(); 

			mA(); 

			mG(); 

			mE(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// $ANTLR end "T_PROCESS"

	// $ANTLR start "T_TRIGGER"
	public final void mT_TRIGGER() throws RecognitionException {
		try {
			int _type = T_TRIGGER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:129:10: ( T R I G G E R )
			// Meta.g:129:12: T R I G G E R
			{
			mT(); 

=======
	// $ANTLR end "T_STORAGE"

	// $ANTLR start "T_CLUSTERING"
	public final void mT_CLUSTERING() throws RecognitionException {
		try {
			int _type = T_CLUSTERING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:133:13: ( C L U S T E R I N G )
			// Meta.g:133:15: C L U S T E R I N G
			{
			mC(); 

			mL(); 

			mU(); 

			mS(); 

			mT(); 

			mE(); 

>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			mR(); 

			mI(); 

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			mG(); 

			mG(); 

=======
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
			// Meta.g:134:8: ( O R D E R )
			// Meta.g:134:10: O R D E R
			{
			mO(); 

			mR(); 

			mD(); 

>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// $ANTLR end "T_TRIGGER"

	// $ANTLR start "T_ON"
	public final void mT_ON() throws RecognitionException {
		try {
			int _type = T_ON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:130:5: ( O N )
			// Meta.g:130:7: O N
			{
			mO(); 

			mN(); 
=======
	// $ANTLR end "T_ORDER"

	// $ANTLR start "T_SELECT"
	public final void mT_SELECT() throws RecognitionException {
		try {
			int _type = T_SELECT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:135:9: ( S E L E C T )
			// Meta.g:135:11: S E L E C T
			{
			mS(); 

			mE(); 

			mL(); 

			mE(); 

			mC(); 

			mT(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// $ANTLR end "T_ON"
=======
	// $ANTLR end "T_SELECT"
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

	// $ANTLR start "T_USING"
	public final void mT_USING() throws RecognitionException {
		try {
			int _type = T_USING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:132:8: ( U S I N G )
			// Meta.g:132:10: U S I N G
=======
			// Meta.g:136:8: ( U S I N G )
			// Meta.g:136:10: U S I N G
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
=======
	// $ANTLR start "T_VALUES"
	public final void mT_VALUES() throws RecognitionException {
		try {
			int _type = T_VALUES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:137:9: ( V A L U E S )
			// Meta.g:137:11: V A L U E S
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

>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:134:12: ( ';' )
			// Meta.g:134:14: ';'
=======
			// Meta.g:139:12: ( ';' )
			// Meta.g:139:14: ';'
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:135:8: ( '=' )
			// Meta.g:135:10: '='
=======
			// Meta.g:140:8: ( '=' )
			// Meta.g:140:10: '='
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:136:8: ( '.' )
			// Meta.g:136:10: '.'
=======
			// Meta.g:141:8: ( '.' )
			// Meta.g:141:10: '.'
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:137:17: ( '{' )
			// Meta.g:137:19: '{'
=======
			// Meta.g:142:17: ( '{' )
			// Meta.g:142:19: '{'
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:138:15: ( '}' )
			// Meta.g:138:17: '}'
=======
			// Meta.g:143:15: ( '}' )
			// Meta.g:143:17: '}'
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:139:8: ( ':' )
			// Meta.g:139:10: ':'
=======
			// Meta.g:144:8: ( ':' )
			// Meta.g:144:10: ':'
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:140:8: ( ',' )
			// Meta.g:140:10: ','
=======
			// Meta.g:145:8: ( ',' )
			// Meta.g:145:10: ','
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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

	// $ANTLR start "T_START_BRACKET"
	public final void mT_START_BRACKET() throws RecognitionException {
		try {
			int _type = T_START_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:146:16: ( '(' )
			// Meta.g:146:18: '('
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
	// $ANTLR end "T_START_BRACKET"

	// $ANTLR start "T_END_BRACKET"
	public final void mT_END_BRACKET() throws RecognitionException {
		try {
			int _type = T_END_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:147:14: ( ')' )
			// Meta.g:147:16: ')'
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
	// $ANTLR end "T_END_BRACKET"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:142:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
=======
			// Meta.g:149:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:143:15: ( '0' .. '9' )
=======
			// Meta.g:150:15: ( '0' .. '9' )
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:145:11: ( ( DIGIT )+ )
			// Meta.g:145:13: ( DIGIT )+
			{
			// Meta.g:145:13: ( DIGIT )+
=======
			// Meta.g:152:11: ( ( DIGIT )+ )
			// Meta.g:152:13: ( DIGIT )+
			{
			// Meta.g:152:13: ( DIGIT )+
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')) ) {
					alt1=1;
				}

				switch (alt1) {
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
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:147:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:147:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:147:17: ( LETTER | DIGIT | '_' )*
=======
			// Meta.g:154:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:154:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:154:17: ( LETTER | DIGIT | '_' )*
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					alt2=1;
				}

				switch (alt2) {
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
					break loop2;
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:149:7: ( ( LETTER | DIGIT | '_' | '.' )+ )
			// Meta.g:149:9: ( LETTER | DIGIT | '_' | '.' )+
			{
			// Meta.g:149:9: ( LETTER | DIGIT | '_' | '.' )+
=======
			// Meta.g:156:7: ( ( LETTER | DIGIT | '_' | '.' )+ )
			// Meta.g:156:9: ( LETTER | DIGIT | '_' | '.' )+
			{
			// Meta.g:156:9: ( LETTER | DIGIT | '_' | '.' )+
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0=='.'||(LA3_0 >= '0' && LA3_0 <= '9')||(LA3_0 >= 'A' && LA3_0 <= 'Z')||LA3_0=='_'||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
					alt3=1;
				}

				switch (alt3) {
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
	// $ANTLR end "T_TERM"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:330:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:330:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:330:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
=======
			// Meta.g:394:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:394:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:394:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '\t' && LA4_0 <= '\n')||LA4_0=='\r'||LA4_0==' ') ) {
					alt4=1;
				}

				switch (alt4) {
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
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
		// Meta.g:1:8: ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_STOP | T_PROCESS | T_TRIGGER | T_ON | T_USING | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_CONSTANT | T_IDENT | T_TERM | WS )
		int alt5=46;
=======
		// Meta.g:1:8: ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_USING | T_VALUES | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_BRACKET | T_END_BRACKET | T_CONSTANT | T_IDENT | T_TERM | WS )
		int alt5=52;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
		alt5 = dfa5.predict(input);
		switch (alt5) {
			case 1 :
				// Meta.g:1:10: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 2 :
				// Meta.g:1:21: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 3 :
				// Meta.g:1:30: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 4 :
				// Meta.g:1:38: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 5 :
				// Meta.g:1:49: T_NOT
				{
				mT_NOT(); 

				}
				break;
			case 6 :
				// Meta.g:1:55: T_WITH
				{
				mT_WITH(); 

				}
				break;
			case 7 :
				// Meta.g:1:62: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 8 :
				// Meta.g:1:69: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 9 :
				// Meta.g:1:77: T_IF
				{
				mT_IF(); 

				}
				break;
			case 10 :
				// Meta.g:1:82: T_EXISTS
				{
				mT_EXISTS(); 

				}
				break;
			case 11 :
				// Meta.g:1:91: T_AND
				{
				mT_AND(); 

				}
				break;
			case 12 :
				// Meta.g:1:97: T_USE
				{
				mT_USE(); 

				}
				break;
			case 13 :
				// Meta.g:1:103: T_SET
				{
				mT_SET(); 

				}
				break;
			case 14 :
				// Meta.g:1:109: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 15 :
				// Meta.g:1:119: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 16 :
				// Meta.g:1:131: T_TRUE
				{
				mT_TRUE(); 

				}
				break;
			case 17 :
				// Meta.g:1:138: T_FALSE
				{
				mT_FALSE(); 

				}
				break;
			case 18 :
				// Meta.g:1:146: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 19 :
				// Meta.g:1:160: T_ALL
				{
				mT_ALL(); 

				}
				break;
			case 20 :
				// Meta.g:1:166: T_ANY
				{
				mT_ANY(); 

				}
				break;
			case 21 :
				// Meta.g:1:172: T_QUORUM
				{
				mT_QUORUM(); 

				}
				break;
			case 22 :
				// Meta.g:1:181: T_ONE
				{
				mT_ONE(); 

				}
				break;
			case 23 :
				// Meta.g:1:187: T_TWO
				{
				mT_TWO(); 

				}
				break;
			case 24 :
				// Meta.g:1:193: T_THREE
				{
				mT_THREE(); 

				}
				break;
			case 25 :
				// Meta.g:1:201: T_EACH_QUORUM
				{
				mT_EACH_QUORUM(); 

				}
				break;
			case 26 :
				// Meta.g:1:215: T_LOCAL_ONE
				{
				mT_LOCAL_ONE(); 

				}
				break;
			case 27 :
				// Meta.g:1:227: T_LOCAL_QUORUM
				{
				mT_LOCAL_QUORUM(); 

				}
				break;
			case 28 :
				// Meta.g:1:242: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 29 :
				// Meta.g:1:252: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 30 :
				// Meta.g:1:259: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 31 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:265: T_STOP
				{
				mT_STOP(); 
=======
				// Meta.g:1:265: T_INSERT
				{
				mT_INSERT(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 32 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:272: T_PROCESS
				{
				mT_PROCESS(); 
=======
				// Meta.g:1:274: T_INTO
				{
				mT_INTO(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 33 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:282: T_TRIGGER
				{
				mT_TRIGGER(); 
=======
				// Meta.g:1:281: T_COMPACT
				{
				mT_COMPACT(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 34 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:292: T_ON
				{
				mT_ON(); 
=======
				// Meta.g:1:291: T_STORAGE
				{
				mT_STORAGE(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 35 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:297: T_USING
				{
				mT_USING(); 
=======
				// Meta.g:1:301: T_CLUSTERING
				{
				mT_CLUSTERING(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 36 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:305: T_SEMICOLON
				{
				mT_SEMICOLON(); 
=======
				// Meta.g:1:314: T_ORDER
				{
				mT_ORDER(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 37 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:317: T_EQUAL
				{
				mT_EQUAL(); 
=======
				// Meta.g:1:322: T_SELECT
				{
				mT_SELECT(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 38 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:325: T_POINT
				{
				mT_POINT(); 
=======
				// Meta.g:1:331: T_USING
				{
				mT_USING(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 39 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:333: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 
=======
				// Meta.g:1:339: T_VALUES
				{
				mT_VALUES(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 40 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:350: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 
=======
				// Meta.g:1:348: T_SEMICOLON
				{
				mT_SEMICOLON(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 41 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:365: T_COLON
				{
				mT_COLON(); 
=======
				// Meta.g:1:360: T_EQUAL
				{
				mT_EQUAL(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 42 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:373: T_COMMA
				{
				mT_COMMA(); 
=======
				// Meta.g:1:368: T_POINT
				{
				mT_POINT(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 43 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:381: T_CONSTANT
				{
				mT_CONSTANT(); 
=======
				// Meta.g:1:376: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 44 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:392: T_IDENT
				{
				mT_IDENT(); 
=======
				// Meta.g:1:393: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 45 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:400: T_TERM
				{
				mT_TERM(); 
=======
				// Meta.g:1:408: T_COLON
				{
				mT_COLON(); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

				}
				break;
			case 46 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				// Meta.g:1:407: WS
=======
				// Meta.g:1:416: T_COMMA
				{
				mT_COMMA(); 

				}
				break;
			case 47 :
				// Meta.g:1:424: T_START_BRACKET
				{
				mT_START_BRACKET(); 

				}
				break;
			case 48 :
				// Meta.g:1:440: T_END_BRACKET
				{
				mT_END_BRACKET(); 

				}
				break;
			case 49 :
				// Meta.g:1:454: T_CONSTANT
				{
				mT_CONSTANT(); 

				}
				break;
			case 50 :
				// Meta.g:1:465: T_IDENT
				{
				mT_IDENT(); 

				}
				break;
			case 51 :
				// Meta.g:1:473: T_TERM
				{
				mT_TERM(); 

				}
				break;
			case 52 :
				// Meta.g:1:480: WS
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA5 dfa5 = new DFA5(this);
	static final String DFA5_eotS =
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
		"\1\uffff\20\34\2\uffff\1\70\4\uffff\1\71\1\34\3\uffff\15\34\1\112\6\34"+
		"\1\123\6\34\2\uffff\3\34\1\137\4\34\1\144\1\145\1\34\1\147\1\34\1\151"+
		"\2\34\1\uffff\3\34\1\157\1\34\1\161\2\34\1\uffff\1\164\1\34\1\166\5\34"+
		"\1\174\2\34\1\uffff\4\34\2\uffff\1\34\1\uffff\1\34\1\uffff\1\u0085\1\u0086"+
		"\3\34\1\uffff\1\34\1\uffff\1\u008b\1\34\1\uffff\1\34\1\uffff\2\34\1\u0090"+
		"\2\34\1\uffff\1\34\1\u0094\1\u0095\2\34\1\u0098\2\34\2\uffff\3\34\1\u009e"+
		"\1\uffff\1\34\1\u00a0\2\34\1\uffff\3\34\2\uffff\1\u00a6\1\34\1\uffff\2"+
		"\34\1\u00aa\2\34\1\uffff\1\34\1\uffff\1\u00ae\3\34\1\u00b3\1\uffff\3\34"+
		"\1\uffff\1\u00b7\1\34\1\u00b9\1\uffff\2\34\1\u00bc\1\u00bd\1\uffff\2\34"+
		"\1\u00c0\1\uffff\1\34\1\uffff\2\34\2\uffff\1\34\1\u00c5\1\uffff\1\34\1"+
		"\u00c7\2\34\1\uffff\1\34\1\uffff\1\34\1\u00cc\1\u00cd\1\34\2\uffff\1\u00cf"+
		"\1\uffff";
	static final String DFA5_eofS =
		"\u00d0\uffff";
	static final String DFA5_minS =
		"\1\11\20\56\2\uffff\1\56\4\uffff\2\56\3\uffff\33\56\2\uffff\20\56\1\uffff"+
		"\10\56\1\uffff\13\56\1\uffff\4\56\2\uffff\1\56\1\uffff\1\56\1\uffff\5"+
		"\56\1\uffff\1\56\1\uffff\2\56\1\uffff\1\56\1\uffff\5\56\1\uffff\10\56"+
		"\2\uffff\4\56\1\uffff\4\56\1\uffff\3\56\2\uffff\2\56\1\uffff\5\56\1\uffff"+
		"\1\56\1\uffff\5\56\1\uffff\3\56\1\uffff\3\56\1\uffff\4\56\1\uffff\3\56"+
		"\1\uffff\1\56\1\uffff\2\56\2\uffff\2\56\1\uffff\4\56\1\uffff\1\56\1\uffff"+
		"\4\56\2\uffff\1\56\1\uffff";
	static final String DFA5_maxS =
		"\1\175\20\172\2\uffff\1\172\4\uffff\2\172\3\uffff\33\172\2\uffff\20\172"+
		"\1\uffff\10\172\1\uffff\13\172\1\uffff\4\172\2\uffff\1\172\1\uffff\1\172"+
		"\1\uffff\5\172\1\uffff\1\172\1\uffff\2\172\1\uffff\1\172\1\uffff\5\172"+
		"\1\uffff\10\172\2\uffff\4\172\1\uffff\4\172\1\uffff\3\172\2\uffff\2\172"+
		"\1\uffff\5\172\1\uffff\1\172\1\uffff\5\172\1\uffff\3\172\1\uffff\3\172"+
		"\1\uffff\4\172\1\uffff\3\172\1\uffff\1\172\1\uffff\2\172\2\uffff\2\172"+
		"\1\uffff\4\172\1\uffff\1\172\1\uffff\4\172\2\uffff\1\172\1\uffff";
	static final String DFA5_acceptS =
		"\21\uffff\1\44\1\45\1\uffff\1\47\1\50\1\51\1\52\2\uffff\1\55\1\56\1\54"+
		"\33\uffff\1\46\1\53\20\uffff\1\11\10\uffff\1\42\13\uffff\1\27\4\uffff"+
		"\1\23\1\13\1\uffff\1\24\1\uffff\1\5\5\uffff\1\14\1\uffff\1\15\2\uffff"+
		"\1\26\1\uffff\1\36\5\uffff\1\20\10\uffff\1\6\1\7\4\uffff\1\37\4\uffff"+
		"\1\35\3\uffff\1\10\1\30\2\uffff\1\3\5\uffff\1\43\1\uffff\1\21\5\uffff"+
		"\1\2\3\uffff\1\12\3\uffff\1\25\4\uffff\1\41\3\uffff\1\34\1\uffff\1\16"+
		"\2\uffff\1\40\1\1\2\uffff\1\4\4\uffff\1\17\1\uffff\1\32\4\uffff\1\22\1"+
		"\31\1\uffff\1\33";
	static final String DFA5_specialS =
		"\u00d0\uffff}>";
	static final String[] DFA5_transitionS = {
			"\2\33\2\uffff\1\33\22\uffff\1\33\13\uffff\1\27\1\uffff\1\23\1\uffff\12"+
			"\30\1\26\1\21\1\uffff\1\22\3\uffff\1\3\1\31\1\2\1\7\1\11\1\15\2\31\1"+
			"\10\1\31\1\4\1\17\1\31\1\5\1\14\1\20\1\16\1\31\1\13\1\1\1\12\1\31\1\6"+
			"\3\31\4\uffff\1\32\1\uffff\1\3\1\31\1\2\1\7\1\11\1\15\2\31\1\10\1\31"+
			"\1\4\1\17\1\31\1\5\1\14\1\20\1\16\1\31\1\13\1\1\1\12\1\31\1\6\3\31\1"+
			"\24\1\uffff\1\25",
			"\1\32\1\uffff\12\41\7\uffff\1\36\6\41\1\40\11\41\1\35\4\41\1\37\3\41"+
			"\4\uffff\1\41\1\uffff\1\36\6\41\1\40\11\41\1\35\4\41\1\37\3\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\43\2\41\1\42\10\41\4\uffff\1\41"+
			"\1\uffff\16\41\1\43\2\41\1\42\10\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\44\1\41\1\45\14\41\4\uffff\1\41"+
			"\1\uffff\13\41\1\44\1\41\1\45\14\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\46\25\41\4\uffff\1\41\1\uffff\4\41"+
			"\1\46\25\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\47\13\41\4\uffff\1\41\1\uffff\16"+
			"\41\1\47\13\41",
			"\1\32\1\uffff\12\41\7\uffff\10\41\1\50\21\41\4\uffff\1\41\1\uffff\10"+
			"\41\1\50\21\41",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\51\10\41\4\uffff\1\41\1\uffff\21"+
			"\41\1\51\10\41",
			"\1\32\1\uffff\12\41\7\uffff\5\41\1\52\24\41\4\uffff\1\41\1\uffff\5\41"+
			"\1\52\24\41",
			"\1\32\1\uffff\12\41\7\uffff\1\54\26\41\1\53\2\41\4\uffff\1\41\1\uffff"+
			"\1\54\26\41\1\53\2\41",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\55\7\41\4\uffff\1\41\1\uffff\22"+
			"\41\1\55\7\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\56\16\41\1\57\6\41\4\uffff\1\41\1"+
			"\uffff\4\41\1\56\16\41\1\57\6\41",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\61\1\41\1\60\12\41\4\uffff\1\41"+
			"\1\uffff\15\41\1\61\1\41\1\60\12\41",
			"\1\32\1\uffff\12\41\7\uffff\1\62\15\41\1\63\13\41\4\uffff\1\41\1\uffff"+
			"\1\62\15\41\1\63\13\41",
			"\1\32\1\uffff\12\41\7\uffff\24\41\1\64\5\41\4\uffff\1\41\1\uffff\24"+
			"\41\1\64\5\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\65\13\41\4\uffff\1\41\1\uffff\16"+
			"\41\1\65\13\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\66\5\41\1\67\10\41\4\uffff\1\41"+
			"\1\uffff\13\41\1\66\5\41\1\67\10\41",
=======
		"\1\uffff\21\37\2\uffff\1\76\6\uffff\1\77\1\37\3\uffff\16\37\1\121\17\37"+
		"\2\uffff\2\37\1\150\6\37\1\157\1\160\1\37\1\162\1\37\1\164\2\37\1\uffff"+
		"\5\37\1\174\1\37\1\176\3\37\1\u0082\2\37\1\u0085\5\37\1\u008b\1\37\1\uffff"+
		"\6\37\2\uffff\1\37\1\uffff\1\37\1\uffff\1\u0095\1\u0096\1\37\1\u0098\3"+
		"\37\1\uffff\1\37\1\uffff\3\37\1\uffff\2\37\1\uffff\2\37\1\u00a4\2\37\1"+
		"\uffff\1\u00a7\1\u00a8\4\37\1\u00ad\2\37\2\uffff\1\37\1\uffff\3\37\1\u00b4"+
		"\3\37\1\u00b8\1\u00b9\2\37\1\uffff\2\37\2\uffff\1\u00be\3\37\1\uffff\2"+
		"\37\1\u00c4\1\u00c5\2\37\1\uffff\1\u00c8\2\37\2\uffff\1\u00cb\1\37\1\u00ce"+
		"\1\37\1\uffff\1\37\1\u00d1\3\37\2\uffff\1\u00d5\1\37\1\uffff\1\u00d7\1"+
		"\u00d8\1\uffff\2\37\1\uffff\1\u00db\1\37\1\uffff\2\37\1\u00df\1\uffff"+
		"\1\37\2\uffff\2\37\1\uffff\2\37\1\u00e5\1\uffff\1\37\1\u00e7\2\37\1\u00ea"+
		"\1\uffff\1\37\1\uffff\1\37\1\u00ed\1\uffff\1\u00ee\1\37\2\uffff\1\u00f0"+
		"\1\uffff";
	static final String DFA5_eofS =
		"\u00f1\uffff";
	static final String DFA5_minS =
		"\1\11\21\56\2\uffff\1\56\6\uffff\2\56\3\uffff\36\56\2\uffff\21\56\1\uffff"+
		"\26\56\1\uffff\6\56\2\uffff\1\56\1\uffff\1\56\1\uffff\7\56\1\uffff\1\56"+
		"\1\uffff\3\56\1\uffff\2\56\1\uffff\5\56\1\uffff\11\56\2\uffff\1\56\1\uffff"+
		"\13\56\1\uffff\2\56\2\uffff\4\56\1\uffff\6\56\1\uffff\3\56\2\uffff\4\56"+
		"\1\uffff\5\56\2\uffff\2\56\1\uffff\2\56\1\uffff\2\56\1\uffff\2\56\1\uffff"+
		"\3\56\1\uffff\1\56\2\uffff\2\56\1\uffff\3\56\1\uffff\5\56\1\uffff\1\56"+
		"\1\uffff\2\56\1\uffff\2\56\2\uffff\1\56\1\uffff";
	static final String DFA5_maxS =
		"\1\175\21\172\2\uffff\1\172\6\uffff\2\172\3\uffff\36\172\2\uffff\21\172"+
		"\1\uffff\26\172\1\uffff\6\172\2\uffff\1\172\1\uffff\1\172\1\uffff\7\172"+
		"\1\uffff\1\172\1\uffff\3\172\1\uffff\2\172\1\uffff\5\172\1\uffff\11\172"+
		"\2\uffff\1\172\1\uffff\13\172\1\uffff\2\172\2\uffff\4\172\1\uffff\6\172"+
		"\1\uffff\3\172\2\uffff\4\172\1\uffff\5\172\2\uffff\2\172\1\uffff\2\172"+
		"\1\uffff\2\172\1\uffff\2\172\1\uffff\3\172\1\uffff\1\172\2\uffff\2\172"+
		"\1\uffff\3\172\1\uffff\5\172\1\uffff\1\172\1\uffff\2\172\1\uffff\2\172"+
		"\2\uffff\1\172\1\uffff";
	static final String DFA5_acceptS =
		"\22\uffff\1\50\1\51\1\uffff\1\53\1\54\1\55\1\56\1\57\1\60\2\uffff\1\63"+
		"\1\64\1\62\36\uffff\1\52\1\61\21\uffff\1\11\26\uffff\1\27\6\uffff\1\23"+
		"\1\13\1\uffff\1\24\1\uffff\1\5\7\uffff\1\14\1\uffff\1\15\3\uffff\1\26"+
		"\2\uffff\1\36\5\uffff\1\20\11\uffff\1\6\1\7\1\uffff\1\40\13\uffff\1\35"+
		"\2\uffff\1\10\1\30\4\uffff\1\3\6\uffff\1\46\3\uffff\1\44\1\21\4\uffff"+
		"\1\2\5\uffff\1\37\1\12\2\uffff\1\45\2\uffff\1\25\2\uffff\1\47\2\uffff"+
		"\1\41\3\uffff\1\34\1\uffff\1\42\1\16\2\uffff\1\1\3\uffff\1\4\5\uffff\1"+
		"\17\1\uffff\1\32\2\uffff\1\43\2\uffff\1\22\1\31\1\uffff\1\33";
	static final String DFA5_specialS =
		"\u00f1\uffff}>";
	static final String[] DFA5_transitionS = {
			"\2\36\2\uffff\1\36\22\uffff\1\36\7\uffff\1\31\1\32\2\uffff\1\30\1\uffff"+
			"\1\24\1\uffff\12\33\1\27\1\22\1\uffff\1\23\3\uffff\1\3\1\34\1\2\1\7\1"+
			"\11\1\15\2\34\1\10\1\34\1\4\1\17\1\34\1\5\1\14\1\20\1\16\1\34\1\13\1"+
			"\1\1\12\1\21\1\6\3\34\4\uffff\1\35\1\uffff\1\3\1\34\1\2\1\7\1\11\1\15"+
			"\2\34\1\10\1\34\1\4\1\17\1\34\1\5\1\14\1\20\1\16\1\34\1\13\1\1\1\12\1"+
			"\21\1\6\3\34\1\25\1\uffff\1\26",
			"\1\35\1\uffff\12\44\7\uffff\1\41\6\44\1\43\11\44\1\40\4\44\1\42\3\44"+
			"\4\uffff\1\44\1\uffff\1\41\6\44\1\43\11\44\1\40\4\44\1\42\3\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\47\2\44\1\46\2\44\1\45\10\44\4\uffff"+
			"\1\44\1\uffff\13\44\1\47\2\44\1\46\2\44\1\45\10\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\50\1\44\1\51\14\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\50\1\44\1\51\14\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\52\25\44\4\uffff\1\44\1\uffff\4\44"+
			"\1\52\25\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\53\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\53\13\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\54\21\44\4\uffff\1\44\1\uffff\10"+
			"\44\1\54\21\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\55\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\55\10\44",
			"\1\35\1\uffff\12\44\7\uffff\5\44\1\56\7\44\1\57\14\44\4\uffff\1\44\1"+
			"\uffff\5\44\1\56\7\44\1\57\14\44",
			"\1\35\1\uffff\12\44\7\uffff\1\61\26\44\1\60\2\44\4\uffff\1\44\1\uffff"+
			"\1\61\26\44\1\60\2\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\62\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\62\7\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\63\16\44\1\64\6\44\4\uffff\1\44\1"+
			"\uffff\4\44\1\63\16\44\1\64\6\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\66\1\44\1\65\1\44\1\67\10\44\4\uffff"+
			"\1\44\1\uffff\15\44\1\66\1\44\1\65\1\44\1\67\10\44",
			"\1\35\1\uffff\12\44\7\uffff\1\70\15\44\1\71\13\44\4\uffff\1\44\1\uffff"+
			"\1\70\15\44\1\71\13\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\72\5\44\4\uffff\1\44\1\uffff\24"+
			"\44\1\72\5\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\73\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\73\13\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\74\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\74\16\44",
			"\1\35\1\uffff\12\44\7\uffff\1\75\31\44\4\uffff\1\44\1\uffff\1\75\31"+
			"\44",
			"",
			"",
			"\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\35\1\uffff\12\33\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\100\5\44\4\uffff\1\44\1\uffff\24"+
			"\44\1\100\5\44",
			"\1\35\1\uffff\12\44\7\uffff\1\44\1\101\30\44\4\uffff\1\44\1\uffff\1"+
			"\44\1\101\30\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\102\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\102\13\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\103\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\103\10\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\104\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\104\25\44",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\106\1\105\14\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\106\1\105\14\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\107\5\44\4\uffff\1\44\1\uffff\24"+
			"\44\1\107\5\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\111\7\44\1\110\6\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\111\7\44\1\110\6\44",
			"\1\35\1\uffff\12\44\7\uffff\1\113\2\44\1\112\24\44\1\114\1\44\4\uffff"+
			"\1\44\1\uffff\1\113\2\44\1\112\24\44\1\114\1\44",
			"\1\35\1\uffff\12\44\7\uffff\30\44\1\115\1\44\4\uffff\1\44\1\uffff\30"+
			"\44\1\115\1\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\116\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\116\6\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\117\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\117\6\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\120\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\120\13\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\122\1\123\6\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\122\1\123\6\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\124\6\44\1\125\12\44\4\uffff\1\44"+
			"\1\uffff\10\44\1\124\6\44\1\125\12\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\126\27\44\4\uffff\1\44\1\uffff\2"+
			"\44\1\126\27\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\127\3\44\1\130\21\44\4\uffff\1\44"+
			"\1\uffff\4\44\1\127\3\44\1\130\21\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\132\7\44\1\131\6\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\132\7\44\1\131\6\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\133\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\133\13\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\134\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\134\6\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\135\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\135\25\44",
			"\1\35\1\uffff\12\44\7\uffff\3\44\1\136\26\44\4\uffff\1\44\1\uffff\3"+
			"\44\1\136\26\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\137\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\137\16\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\140\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\140\10\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\141\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\141\13\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\142\27\44\4\uffff\1\44\1\uffff\2"+
			"\44\1\142\27\44",
			"\1\35\1\uffff\12\44\7\uffff\1\143\31\44\4\uffff\1\44\1\uffff\1\143\31"+
			"\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\144\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\144\16\44",
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\146\10\44\1\145\14\44\4\uffff\1\44"+
			"\1\uffff\4\44\1\146\10\44\1\145\14\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\147\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\147\16\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\151\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\151\25\44",
			"\1\35\1\uffff\12\44\7\uffff\1\152\31\44\4\uffff\1\44\1\uffff\1\152\31"+
			"\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\153\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\153\7\44",
			"\1\35\1\uffff\12\44\7\uffff\17\44\1\154\12\44\4\uffff\1\44\1\uffff\17"+
			"\44\1\154\12\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\155\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\155\7\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\156\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\156\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\161\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\161\16\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\163\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\163\7\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\7\44\1\165\22\44\4\uffff\1\44\1\uffff\7"+
			"\44\1\165\22\44",
			"\1\35\1\uffff\12\44\7\uffff\17\44\1\166\12\44\4\uffff\1\44\1\uffff\17"+
			"\44\1\166\12\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\167\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\167\25\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\170\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\170\13\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\171\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\171\7\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\172\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\172\16\44",
			"\1\35\1\uffff\12\44\7\uffff\7\44\1\173\22\44\4\uffff\1\44\1\uffff\7"+
			"\44\1\173\22\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\175\14\44\4\uffff\1\44\1\uffff\15"+
			"\44\1\175\14\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\177\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\177\25\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u0080\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u0080\10\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u0081\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u0081\21\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u0083\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u0083\25\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u0084\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u0084\7\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u0086\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u0086\10\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u0087\31\44\4\uffff\1\44\1\uffff\1\u0087"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u0088\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u0088\14\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u0089\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u0089\5\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u008a\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u008a\27\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u008c\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u008c\25\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u008d\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u008d\25\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u008e\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u008e\6\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u008f\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u008f\21\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u0090\31\44\4\uffff\1\44\1\uffff\1\u0090"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u0091\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u0091\6\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u0092\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u0092\10\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\30\44\1\u0093\1\44\4\uffff\1\44\1\uffff"+
			"\30\44\1\u0093\1\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\17\44\1\u0094\12\44\4\uffff\1\44\1\uffff"+
			"\17\44\1\u0094\12\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u0097\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u0097\10\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u0099\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u0099\6\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u009a\31\44\4\uffff\1\44\1\uffff\1\u009a"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\u009b\1\uffff\32\44",
			"",
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			"\1\32\1\uffff\12\41\7\uffff\10\41\1\73\13\41\1\72\5\41\4\uffff\1\41"+
			"\1\uffff\10\41\1\73\13\41\1\72\5\41",
			"\1\32\1\uffff\12\41\7\uffff\1\41\1\74\30\41\4\uffff\1\41\1\uffff\1\41"+
			"\1\74\30\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\75\13\41\4\uffff\1\41\1\uffff\16"+
			"\41\1\75\13\41",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\76\10\41\4\uffff\1\41\1\uffff\21"+
			"\41\1\76\10\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\77\25\41\4\uffff\1\41\1\uffff\4\41"+
			"\1\77\25\41",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\100\14\41\4\uffff\1\41\1\uffff\15"+
			"\41\1\100\14\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\102\7\41\1\101\6\41\4\uffff\1\41"+
			"\1\uffff\13\41\1\102\7\41\1\101\6\41",
			"\1\32\1\uffff\12\41\7\uffff\1\104\2\41\1\103\24\41\1\105\1\41\4\uffff"+
			"\1\41\1\uffff\1\104\2\41\1\103\24\41\1\105\1\41",
			"\1\32\1\uffff\12\41\7\uffff\30\41\1\106\1\41\4\uffff\1\41\1\uffff\30"+
			"\41\1\106\1\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\107\6\41\4\uffff\1\41\1\uffff\23"+
			"\41\1\107\6\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\110\6\41\4\uffff\1\41\1\uffff\23"+
			"\41\1\110\6\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\111\13\41\4\uffff\1\41\1\uffff\16"+
			"\41\1\111\13\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\10\41\1\113\6\41\1\114\12\41\4\uffff\1\41"+
			"\1\uffff\10\41\1\113\6\41\1\114\12\41",
			"\1\32\1\uffff\12\41\7\uffff\2\41\1\115\27\41\4\uffff\1\41\1\uffff\2"+
			"\41\1\115\27\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\116\3\41\1\117\21\41\4\uffff\1\41"+
			"\1\uffff\4\41\1\116\3\41\1\117\21\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\120\6\41\4\uffff\1\41\1\uffff\23"+
			"\41\1\120\6\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\121\13\41\4\uffff\1\41\1\uffff\16"+
			"\41\1\121\13\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\122\6\41\4\uffff\1\41\1\uffff\23"+
			"\41\1\122\6\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\124\25\41\4\uffff\1\41\1\uffff\4"+
			"\41\1\124\25\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\125\16\41\4\uffff\1\41\1\uffff\13"+
			"\41\1\125\16\41",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\126\10\41\4\uffff\1\41\1\uffff\21"+
			"\41\1\126\10\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\127\13\41\4\uffff\1\41\1\uffff\16"+
			"\41\1\127\13\41",
			"\1\32\1\uffff\12\41\7\uffff\2\41\1\130\27\41\4\uffff\1\41\1\uffff\2"+
			"\41\1\130\27\41",
			"\1\32\1\uffff\12\41\7\uffff\1\131\31\41\4\uffff\1\41\1\uffff\1\131\31"+
			"\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\132\13\41\4\uffff\1\41\1\uffff\16"+
			"\41\1\132\13\41",
=======
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\u009c\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u009c\23\44",
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			"",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u009d\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u009d\27\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u009e\31\44\4\uffff\1\44\1\uffff\1\u009e"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u009f\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u009f\13\44",
			"",
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\134\10\41\1\133\14\41\4\uffff\1\41"+
			"\1\uffff\4\41\1\134\10\41\1\133\14\41",
			"\1\32\1\uffff\12\41\7\uffff\6\41\1\135\23\41\4\uffff\1\41\1\uffff\6"+
			"\41\1\135\23\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\136\16\41\4\uffff\1\41\1\uffff\13"+
			"\41\1\136\16\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\140\25\41\4\uffff\1\41\1\uffff\4"+
			"\41\1\140\25\41",
			"\1\32\1\uffff\12\41\7\uffff\1\141\31\41\4\uffff\1\41\1\uffff\1\141\31"+
			"\41",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\142\7\41\4\uffff\1\41\1\uffff\22"+
			"\41\1\142\7\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\143\25\41\4\uffff\1\41\1\uffff\4"+
			"\41\1\143\25\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\146\16\41\4\uffff\1\41\1\uffff\13"+
			"\41\1\146\16\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\150\7\41\4\uffff\1\41\1\uffff\22"+
			"\41\1\150\7\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\7\41\1\152\22\41\4\uffff\1\41\1\uffff\7"+
			"\41\1\152\22\41",
			"\1\32\1\uffff\12\41\7\uffff\17\41\1\153\12\41\4\uffff\1\41\1\uffff\17"+
			"\41\1\153\12\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\154\7\41\4\uffff\1\41\1\uffff\22"+
			"\41\1\154\7\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\155\16\41\4\uffff\1\41\1\uffff\13"+
			"\41\1\155\16\41",
			"\1\32\1\uffff\12\41\7\uffff\7\41\1\156\22\41\4\uffff\1\41\1\uffff\7"+
			"\41\1\156\22\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\160\14\41\4\uffff\1\41\1\uffff\15"+
			"\41\1\160\14\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\17\41\1\162\12\41\4\uffff\1\41\1\uffff\17"+
			"\41\1\162\12\41",
			"\1\32\1\uffff\12\41\7\uffff\10\41\1\163\21\41\4\uffff\1\41\1\uffff\10"+
			"\41\1\163\21\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\165\7\41\4\uffff\1\41\1\uffff\22"+
			"\41\1\165\7\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\167\10\41\4\uffff\1\41\1\uffff\21"+
			"\41\1\167\10\41",
			"\1\32\1\uffff\12\41\7\uffff\1\170\31\41\4\uffff\1\41\1\uffff\1\170\31"+
			"\41",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\171\14\41\4\uffff\1\41\1\uffff\15"+
			"\41\1\171\14\41",
			"\1\32\1\uffff\12\41\7\uffff\2\41\1\172\27\41\4\uffff\1\41\1\uffff\2"+
			"\41\1\172\27\41",
			"\1\32\1\uffff\12\41\7\uffff\2\41\1\173\27\41\4\uffff\1\41\1\uffff\2"+
			"\41\1\173\27\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\6\41\1\175\23\41\4\uffff\1\41\1\uffff\6"+
			"\41\1\175\23\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\176\25\41\4\uffff\1\41\1\uffff\4"+
			"\41\1\176\25\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\177\25\41\4\uffff\1\41\1\uffff\4"+
			"\41\1\177\25\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\u0080\6\41\4\uffff\1\41\1\uffff"+
			"\23\41\1\u0080\6\41",
			"\1\32\1\uffff\12\41\7\uffff\10\41\1\u0081\21\41\4\uffff\1\41\1\uffff"+
			"\10\41\1\u0081\21\41",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\u0082\10\41\4\uffff\1\41\1\uffff"+
			"\21\41\1\u0082\10\41",
			"",
			"",
			"\1\32\1\uffff\12\41\7\uffff\30\41\1\u0083\1\41\4\uffff\1\41\1\uffff"+
			"\30\41\1\u0083\1\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\17\41\1\u0084\12\41\4\uffff\1\41\1\uffff"+
			"\17\41\1\u0084\12\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\u0087\6\41\4\uffff\1\41\1\uffff"+
			"\23\41\1\u0087\6\41",
			"\1\32\1\uffff\12\41\7\uffff\1\u0088\31\41\4\uffff\1\41\1\uffff\1\u0088"+
			"\31\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\u0089\1\uffff\32\41",
=======
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00a0\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00a0\10\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00a1\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00a1\25\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00a2\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00a2\5\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\u00a3\16\44\4\uffff\1\44\1\uffff"+
			"\13\44\1\u00a3\16\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00a5\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00a5\25\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u00a6\31\44\4\uffff\1\44\1\uffff\1\u00a6"+
			"\31\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00a9\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00a9\25\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00aa\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00aa\7\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00ab\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00ab\27\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00ac\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00ac\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00ae\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00ae\6\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u00af\31\44\4\uffff\1\44\1\uffff\1\u00af"+
			"\31\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00b0\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00b0\6\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00b1\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00b1\7\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u00b2\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00b2\21\44",
			"\1\35\1\uffff\12\44\7\uffff\20\44\1\u00b3\11\44\4\uffff\1\44\1\uffff"+
			"\20\44\1\u00b3\11\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00b5\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00b5\6\44",
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\u00b6\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u00b6\23\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00b7\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00b7\14\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\u00ba\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00ba\15\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\u00bb\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00bc\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00bc\7\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00bd\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00bd\6\44",
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			"",
			"\1\32\1\uffff\12\41\7\uffff\6\41\1\u008a\23\41\4\uffff\1\41\1\uffff"+
			"\6\41\1\u008a\23\41",
			"",
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\u008c\13\41\4\uffff\1\41\1\uffff"+
			"\16\41\1\u008c\13\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u008d\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u008d\25\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\24\41\1\u008e\5\41\4\uffff\1\41\1\uffff"+
			"\24\41\1\u008e\5\41",
			"\1\32\1\uffff\12\41\7\uffff\13\41\1\u008f\16\41\4\uffff\1\41\1\uffff"+
			"\13\41\1\u008f\16\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u0091\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u0091\25\41",
			"\1\32\1\uffff\12\41\7\uffff\1\u0092\31\41\4\uffff\1\41\1\uffff\1\u0092"+
			"\31\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u0093\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u0093\25\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u0096\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u0096\25\41",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\u0097\7\41\4\uffff\1\41\1\uffff"+
			"\22\41\1\u0097\7\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\u0099\6\41\4\uffff\1\41\1\uffff"+
			"\23\41\1\u0099\6\41",
			"\1\32\1\uffff\12\41\7\uffff\1\u009a\31\41\4\uffff\1\41\1\uffff\1\u009a"+
			"\31\41",
=======
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00bf\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00bf\6\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00c0\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00c0\6\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00c1\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00c1\10\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u00c2\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00c2\21\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00c3\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00c3\27\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00c6\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00c6\14\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00c7\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00c7\5\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00c9\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00c9\25\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00ca\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00ca\7\44",
			"",
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u00cc\1\44\1\u00cd\11\44\4\uffff"+
			"\1\44\1\uffff\16\44\1\u00cc\1\44\1\u00cd\11\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00cf\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00cf\25\44",
			"",
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\u009b\7\41\4\uffff\1\41\1\uffff"+
			"\22\41\1\u009b\7\41",
			"\1\32\1\uffff\12\41\7\uffff\10\41\1\u009c\21\41\4\uffff\1\41\1\uffff"+
			"\10\41\1\u009c\21\41",
			"\1\32\1\uffff\12\41\7\uffff\20\41\1\u009d\11\41\4\uffff\1\41\1\uffff"+
			"\20\41\1\u009d\11\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\u009f\14\41\4\uffff\1\41\1\uffff"+
			"\15\41\1\u009f\14\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\14\41\1\u00a1\15\41\4\uffff\1\41\1\uffff"+
			"\14\41\1\u00a1\15\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\u00a2\1\uffff\32\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\u00a3\7\41\4\uffff\1\41\1\uffff"+
			"\22\41\1\u00a3\7\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\u00a4\6\41\4\uffff\1\41\1\uffff"+
			"\23\41\1\u00a4\6\41",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\u00a5\10\41\4\uffff\1\41\1\uffff"+
			"\21\41\1\u00a5\10\41",
=======
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00d0\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00d0\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u00d2\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00d2\21\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00d3\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00d3\27\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00d4\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00d4\25\44",
			"",
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u00d6\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00d6\13\44",
			"",
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\23\41\1\u00a7\6\41\4\uffff\1\41\1\uffff"+
			"\23\41\1\u00a7\6\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\10\41\1\u00a8\21\41\4\uffff\1\41\1\uffff"+
			"\10\41\1\u00a8\21\41",
			"\1\32\1\uffff\12\41\7\uffff\2\41\1\u00a9\27\41\4\uffff\1\41\1\uffff"+
			"\2\41\1\u00a9\27\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\u00ab\14\41\4\uffff\1\41\1\uffff"+
			"\15\41\1\u00ab\14\41",
			"\1\32\1\uffff\12\41\7\uffff\24\41\1\u00ac\5\41\4\uffff\1\41\1\uffff"+
			"\24\41\1\u00ac\5\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\u00ad\7\41\4\uffff\1\41\1\uffff"+
			"\22\41\1\u00ad\7\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\u00af\1\41\1\u00b0\11\41\4\uffff"+
			"\1\41\1\uffff\16\41\1\u00af\1\41\1\u00b0\11\41",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\u00b1\7\41\4\uffff\1\41\1\uffff"+
			"\22\41\1\u00b1\7\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u00b2\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u00b2\25\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u00b4\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u00b4\25\41",
			"\1\32\1\uffff\12\41\7\uffff\2\41\1\u00b5\27\41\4\uffff\1\41\1\uffff"+
			"\2\41\1\u00b5\27\41",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u00b6\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u00b6\25\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\u00b8\13\41\4\uffff\1\41\1\uffff"+
			"\16\41\1\u00b8\13\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\u00ba\14\41\4\uffff\1\41\1\uffff"+
			"\15\41\1\u00ba\14\41",
			"\1\32\1\uffff\12\41\7\uffff\24\41\1\u00bb\5\41\4\uffff\1\41\1\uffff"+
			"\24\41\1\u00bb\5\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\15\41\1\u00be\14\41\4\uffff\1\41\1\uffff"+
			"\15\41\1\u00be\14\41",
			"\1\32\1\uffff\12\41\7\uffff\22\41\1\u00bf\7\41\4\uffff\1\41\1\uffff"+
			"\22\41\1\u00bf\7\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\u00c1\10\41\4\uffff\1\41\1\uffff"+
			"\21\41\1\u00c1\10\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\4\41\1\u00c2\25\41\4\uffff\1\41\1\uffff"+
			"\4\41\1\u00c2\25\41",
			"\1\32\1\uffff\12\41\7\uffff\16\41\1\u00c3\13\41\4\uffff\1\41\1\uffff"+
			"\16\41\1\u00c3\13\41",
			"",
			"",
			"\1\32\1\uffff\12\41\7\uffff\2\41\1\u00c4\27\41\4\uffff\1\41\1\uffff"+
			"\2\41\1\u00c4\27\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\24\41\1\u00c6\5\41\4\uffff\1\41\1\uffff"+
			"\24\41\1\u00c6\5\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\21\41\1\u00c8\10\41\4\uffff\1\41\1\uffff"+
			"\21\41\1\u00c8\10\41",
			"\1\32\1\uffff\12\41\7\uffff\30\41\1\u00c9\1\41\4\uffff\1\41\1\uffff"+
			"\30\41\1\u00c9\1\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\14\41\1\u00ca\15\41\4\uffff\1\41\1\uffff"+
			"\14\41\1\u00ca\15\41",
			"",
			"\1\32\1\uffff\12\41\7\uffff\24\41\1\u00cb\5\41\4\uffff\1\41\1\uffff"+
			"\24\41\1\u00cb\5\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\32\41\4\uffff\1\41\1\uffff\32\41",
			"\1\32\1\uffff\12\41\7\uffff\14\41\1\u00ce\15\41\4\uffff\1\41\1\uffff"+
			"\14\41\1\u00ce\15\41",
=======
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00d9\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00d9\14\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00da\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00da\5\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00dc\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00dc\14\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00dd\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00dd\14\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00de\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00de\7\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00e0\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00e0\10\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00e1\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00e1\25\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u00e2\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00e2\13\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00e3\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00e3\27\44",
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\u00e4\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u00e4\23\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00e6\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00e6\5\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00e8\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00e8\10\44",
			"\1\35\1\uffff\12\44\7\uffff\30\44\1\u00e9\1\44\4\uffff\1\44\1\uffff"+
			"\30\44\1\u00e9\1\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\u00eb\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00eb\15\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00ec\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00ec\5\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\u00ef\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00ef\15\44",
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			""
	};

	static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
	static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
	static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
	static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
	static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
	static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
	static final short[][] DFA5_transition;

	static {
		int numStates = DFA5_transitionS.length;
		DFA5_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
		}
	}

	protected class DFA5 extends DFA {

		public DFA5(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 5;
			this.eot = DFA5_eot;
			this.eof = DFA5_eof;
			this.min = DFA5_min;
			this.max = DFA5_max;
			this.accept = DFA5_accept;
			this.special = DFA5_special;
			this.transition = DFA5_transition;
		}
		@Override
		public String getDescription() {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			return "1:1: Tokens : ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_STOP | T_PROCESS | T_TRIGGER | T_ON | T_USING | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_CONSTANT | T_IDENT | T_TERM | WS );";
=======
			return "1:1: Tokens : ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_USING | T_VALUES | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_BRACKET | T_END_BRACKET | T_CONSTANT | T_IDENT | T_TERM | WS );";
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
		}
	}

}
