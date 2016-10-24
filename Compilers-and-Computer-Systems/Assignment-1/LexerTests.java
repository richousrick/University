import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * A load of tests for the Compilers Assignment 1 Task 1
 * 
 * It is strongly recommended that you write some of your own tests too.
 * WARNING:
 * These tests are made from my interpretation of the assignment
 * and i do not take any responsibility if they are wrong
 * use as you wish.
 *
 * @author Rikkey Paal
 */
public class LexerTests {

	
	/*
	 * parts separated by whitespace
	 */
	
	/* questions
	 * does null / uninitialised / whitespace = error
	 * 
	 */
	
	Lexer lexer; 
	Random random;
	ArrayList<String> terms;
	ArrayList<Class<? extends Token>> tokenClasses;
	ArrayList<String> mathTerms;
	ArrayList<Class<? extends Token>> mathTokenClasses;
	
	@Before
	public void init() {
		lexer = Task1.create();
		random = new Random();
		
		terms = new ArrayList<>();
		tokenClasses = new ArrayList<>();
		terms.add("def");
		tokenClasses.add( T_Def.class);
		terms.add("skip");
		tokenClasses.add( T_Skip.class);
		terms.add("if");
		tokenClasses.add( T_If.class);
		terms.add("then");
		tokenClasses.add( T_Then.class);
		terms.add("else");
		tokenClasses.add( T_Else.class);
		terms.add("while");
		tokenClasses.add( T_While.class);
		terms.add("do");
		tokenClasses.add( T_Do.class);
		terms.add("repeat");
		tokenClasses.add( T_Repeat.class);
		terms.add("until");
		tokenClasses.add( T_Until.class);
		terms.add("break");
		tokenClasses.add( T_Break.class);
		terms.add("continue");
		tokenClasses.add( T_Continue.class);
		
		mathTerms = new ArrayList<>();
		mathTokenClasses = new ArrayList<>();
		
		mathTerms.add(";");
		mathTokenClasses.add(T_Semicolon.class);
		mathTerms.add("(");
		mathTokenClasses.add(T_LeftBracket.class);
		mathTerms.add(")");
		mathTokenClasses.add(T_RightBracket.class);
		mathTerms.add("=");
		mathTokenClasses.add(T_EqualDefines.class);
		mathTerms.add("==");
		mathTokenClasses.add(T_Equal.class);
		mathTerms.add("<");
		mathTokenClasses.add(T_LessThan.class);
		mathTerms.add(">");
		mathTokenClasses.add(T_GreaterThan.class);
		mathTerms.add("<=");
		mathTokenClasses.add(T_LessEq.class);
		mathTerms.add(">=");
		mathTokenClasses.add(T_GreaterEq.class);
		mathTerms.add(",");
		mathTokenClasses.add(T_Comma.class);
		mathTerms.add("{");
		mathTokenClasses.add(T_LeftCurlyBracket.class);
		mathTerms.add("}");
		mathTokenClasses.add(T_RightCurlyBracket.class);
		mathTerms.add(":=");
		mathTokenClasses.add(T_Assign.class);
		mathTerms.add("+");
		mathTokenClasses.add(T_Plus.class);
		mathTerms.add("*");
		mathTokenClasses.add(T_Times.class);
		mathTerms.add("-");
		mathTokenClasses.add(T_Minus.class);
		mathTerms.add("/");
		mathTokenClasses.add(T_Div.class);
		
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	class StringBool{
		String s = "";
		boolean b = false;
	}
	
	/* PRIVATE METHODS*/
		private void lexicalFail(String message){
			fail("should throw a lexical exception for \""+message+"\"");
		}
		
		private void falseTaskFail(String message){
			fail("should throw a lexical exception for \""+message+"\"; threw a Task1Exception");
		}
		
		private void taskFail(String message){
			fail("should throw a task exception for \""+message+"\"");
		}
		
		private void falseLexicalFail(String message){
			fail("should throw a task exception for \""+message+"\"; threw a LexicalException");
		}
		
		private boolean assertEqualTokens(Token expected, Token actual){
			if(expected.getClass().equals(actual.getClass())){
				if(expected.getClass().equals(T_Identifier.class)){
					return ((T_Identifier)expected).s.equals(((T_Identifier)actual).s);
				}else if(expected.getClass().equals(T_Integer.class)){
					return ((T_Integer)expected).n == ((T_Integer)actual).n;
				}else{
					return true;
				}
			}
			return false;
		}
		
		private StringBool assertEqualsTokensList(List<Token> expected, List<Token> actual){
			StringBool strb = new StringBool();
			if(expected.size() == actual.size()){
				for(int i = 0; i< expected.size(); i++){
					if(!assertEqualTokens(expected.get(i), actual.get(i))){
							strb.s+="mismatch between expected and actual at index "+i;
					}
				}
			}else{
					strb.s+=("expected list is of different size to list given \nexpected:"+expected.size()+"\nactual:"+actual.size());
			}
			strb.b= strb.s.equals("");
			return strb;
		}
		
		private void testLexicalInvalidInput(String input){
			try {
				lexer.lex(input);
				lexicalFail(input);
			} catch (LexicalException e) {
			} catch (Task1Exception e) {
				 falseTaskFail(input);
			}
		}
		
		private void testTaskInvalidInput(String input){
			try {
				lexer.lex(input);
				taskFail(input);
			} catch (LexicalException e) {
				 falseLexicalFail(input);
			} catch (Task1Exception e) {
			}
		}
		
		private void testValidInput(String input, List<Token> expecting){
			try {
				StringBool strb = assertEqualsTokensList(expecting, lexer.lex(input));
				if(!strb.b){
					fail(strb.s);
				}
			} catch (LexicalException e) {
				fail("threw lexical exception for valid input \""+input+"\"");
			} catch (Task1Exception e) {
				fail("threw Task1 exception for valid input \""+input+"\"");
			}
		}
		
		private void testValidInput(String input, Token expecting){
			List<Token> list = new ArrayList<>();
			list.add(expecting);
			testValidInput(input, list);
		}
		
		public void testRepeatedStringInput(String s){
			String str = "";
			for(int i = 0; i<5; i++){
				str+=s;
				testLexicalInvalidInput(str);
			}
		}
		
		private void testValidIdentifier(String identifier){
			testValidInput(identifier, new T_Identifier(identifier));
		}
		
	/* CHECK EMPTY INPUT FORMAT */
	
		/**
		 * Tests that a {@link LexicalException} is thrown for a null input
		 */
		@Test
		public void testNullInput(){
			testValidInput(null, new ArrayList<Token>());
		}
		
		/**
		 * Tests that a {@link LexicalException} is thrown for a empty string input
		 */
		@Test
		public void testEmptyInput(){
			testValidInput("",new ArrayList<Token>());
		}
	
		
		/**
		 * Tests that a {@link LexicalException} is thrown for a input of only spaces
		 */
		@Test
		public void testSpaceStringInput(){
			testValidInput(" ",new ArrayList<Token>());
		}
		
		/**
		 * Tests that a {@link LexicalException} is thrown for a input of only newline
		 */
		@Test
		public void testNewlineStringInput(){
			testValidInput("\n",new ArrayList<Token>());
		}
		
		/**
		 * Tests that a {@link LexicalException} is thrown for a input of only form feed
		 */
		@Test
		public void testFormFeedStringInput(){
			testValidInput("\f",new ArrayList<Token>());
		}
		
		/**
		 * Tests that a {@link LexicalException} is thrown for a input of only carriage return
		 */
		@Test
		public void testCarriageReturnStringInput(){
			testValidInput("\r",new ArrayList<Token>());
		}
		
		/**
		 * Tests that a {@link LexicalException} is thrown for a input of only tab
		 */
		@Test
		public void testTabStringInput(){
			testValidInput("\t",new ArrayList<Token>());
		}
		
		/**
		 * Tests that a {@link LexicalException} is thrown for a input of only vertical tab
		 */
		@Test
		public void testVerticalTabStringInput(){
			testValidInput("\13",new ArrayList<Token>());
		} 
		
	/* TEST INTEGER */
		
		/* NORMAL */
		
			/**
	 		 * test that all of the integers form 0 to 9 are valid 
			 */
			@Test
			public void testSingleIntegers(){
				for (int i = 0; i<10; i++){
					ArrayList<Token> t = new ArrayList<>();
					t.add(new T_Integer(i));
					testValidInput(i+"",t);
				}
			}
			
			/**
			 * test 10 random 2 digit integers 
			 */
			@Test
			public void testDoubleIntegers(){
				for (int i = 0; i<10; i++){
					int n = random.nextInt(90) + 10;
					testValidInput(n+"",new T_Integer(n));
				}
			}
			
			/**
			 * test 10 random 3 digit integers 
			 */
			@Test
			public void testTripleIntegers(){
				for (int i = 0; i<10; i++){
					int n = random.nextInt(900) + 100;
					testValidInput(n+"",new T_Integer(n));
				}
			}
		
			/**
			 * test 100 random integers 
			 */
			@Test
			public void testLargeIntegers(){
				for (int i = 0; i<100; i++){
					int n = random.nextInt(Integer.MAX_VALUE) + 100;
					testValidInput(n+"",new T_Integer(n));
				}
			}
			
		/* INTEGER SIZE */
			
			/**
			 * test compiling with value Integer.MAX_VALUE +1
			 */
			@Test
			public void testTooLargeInteger(){
				testTaskInvalidInput("2147483648");
			}
			
			/**
			 * test compiling with value Integer.MAX_VALUE
			 */
			@Test
			public void testMaxInteger(){
				testValidInput(Integer.MAX_VALUE+"", new T_Integer(Integer.MAX_VALUE));
			}
		
		/* INTEGER FORMAT */
			
			/**
			 * test compiling with value starting with numerous 0
			 */
			@Test
			public void testIntegersStartingWithZero(){
				String zeroes = "";
				for (int i = 0; i<5; i++){
					zeroes += "0";
					for (int x = 0; x<10; x++){
						int n = random.nextInt(900) + 100;
						testValidInput(zeroes+n,new T_Integer(n));
					}
				}
			}
			
			/**
			 * test compiling with value with numerous 0
			 */
			@Test
			public void testMultipleZero(){
				String zeroes = "";
				for (int i = 0; i<5; i++){
					zeroes += "0";
					testValidInput(zeroes,new T_Integer(0));
				}
			}
			
			/**
			 * test compiling with integer containing dot
			 */
			@Test
			public void testIntegersWithDot(){
				String dec = "";
				for (int x = 0; x<10; x++){
					dec = (random.nextInt(900) + 100)+"."+(random.nextInt(900) + 100);
					testLexicalInvalidInput(dec);
				}
			}
			
			/**
			 * test compiling with integer starting with minus
			 */
			@Test
			public void testIntegersWithMinus(){
				int dec;
				for (int x = 0; x<10; x++){
					dec = (random.nextInt(900) + 100);
					List<Token> list = new ArrayList<>();
					list.add(new T_Minus());
					list.add(new T_Integer(dec));
					testValidInput("-"+dec, list);
				}
			}
			
			/**
			 * test compiling with integer starting with plus
			 */
			@Test
			public void testIntegersWithPlus(){
				int dec;
				for (int x = 0; x<10; x++){
					dec = (random.nextInt(900) + 100);
					List<Token> list = new ArrayList<>();
					list.add(new T_Plus());
					list.add(new T_Integer(dec));
					testValidInput("+"+dec, list);
				}
			}
			
			/**
			 * test compiling with integer followed by f e.g. 12f, to see that it is not converted into a float
			 */
			@Test
			public void testFloatString(){
				int dec;
				for (int x = 0; x<10; x++){
					dec = (random.nextInt(900) + 100);
					List<Token> list = new ArrayList<>();
					list.add(new T_Integer(dec));
					list.add(new T_Identifier("f"));
					testValidInput(dec+"f", list);
				}
			}
			
			/**
			 * test compiling with integer followed by d e.g. 12d, to see that it is not converted into a double
			 */
			@Test
			public void testDoubleString(){
				int dec;
				for (int x = 0; x<10; x++){
					dec = (random.nextInt(900) + 100);
					List<Token> list = new ArrayList<>();
					list.add(new T_Integer(dec));
					list.add(new T_Identifier("d"));
					testValidInput(dec+"d", list);
				}
			}
			
	/* SYNTAX FORMAT */
			
		/* MATH SYNTAX SPELLING */
			
			/**
			 * test that the correct syntax is tokenised
			 */
			@Test
			public void testCorrectMathSyntaxSpelling(){
				testValidInput(";", new T_Semicolon());
				testValidInput("(", new T_LeftBracket());
				testValidInput(")", new T_RightBracket());
				testValidInput("=", new T_EqualDefines());
				testValidInput("==", new T_Equal());
				testValidInput("<", new T_LessThan());
				testValidInput(">", new T_GreaterThan());
				testValidInput("<=", new T_LessEq());
				testValidInput(">=", new T_GreaterEq());
				testValidInput(",", new T_Comma());
				testValidInput("{", new T_LeftCurlyBracket());
				testValidInput("}", new T_RightCurlyBracket());
				testValidInput(":=", new T_Assign());
				testValidInput("+", new T_Plus());
				testValidInput("*", new T_Times());
				testValidInput("-", new T_Minus());
				testValidInput("/", new T_Div());
			}
			
			/**
			 * test that the math syntax is tokenised in the correct order
			 */
			@Test
			public void testMathSyntaxOrder(){
				List<Token> expected = new ArrayList<>();
				expected.add(new T_Equal());
				expected.add(new T_EqualDefines());
				testValidInput("===", expected);
				expected.set(0, new T_LessEq());
				testValidInput("<==", expected);
				expected.set(0, new T_GreaterEq());
				testValidInput(">==", expected);
			}
			
		/* TERM SYNTAX SPELLING */
			
			/**
			 * test that the correct syntax is tokenised
			 */
			@Test
			public void testCorrectTermSpelling(){
				testValidInput("def", new T_Def());
				testValidInput("skip", new T_Skip());
				testValidInput("if", new T_If());
				testValidInput("then", new T_Then());
				testValidInput("else", new T_Else());
				testValidInput("while", new T_While());
				testValidInput("do", new T_Do());
				testValidInput("repeat", new T_Repeat());
				testValidInput("until", new T_Until());
				testValidInput("break", new T_Break());
				testValidInput("continue", new T_Continue());
			}
			
			/**
			 * test that the capitalised syntax creates a locaical exception
			 * this is because the identifiers must begin with a lowercase letter, and the terms must be all lowercase
			 */
			@Test
			public void testCapitalisedTermSpelling(){
				testLexicalInvalidInput("Def");
				testLexicalInvalidInput("Skip");
				testLexicalInvalidInput("If");
				testLexicalInvalidInput("Then");
				testLexicalInvalidInput("Else");
				testLexicalInvalidInput("While");
				testLexicalInvalidInput("Do");
				testLexicalInvalidInput("Repeat");
				testLexicalInvalidInput("Until");
				testLexicalInvalidInput("Break");
				testLexicalInvalidInput("Continue");
			}
			
			/**
			 * test that the terms with a repeated first letter create identifiers not identifiers and terms
			 */
			@Test
			public void testIncorrectTermSpelling(){
				testValidIdentifier("ddef");
				testValidIdentifier("sskip");
				testValidIdentifier("iif");
				testValidIdentifier("tthen");
				testValidIdentifier("eelse");
				testValidIdentifier("wwhile");
				testValidIdentifier("ddo");
				testValidIdentifier("rrepeat");
				testValidIdentifier("uuntil");
				testValidIdentifier("bbreak");
				testValidIdentifier("ccontinue");
				
				testValidIdentifier("deff");
				testValidIdentifier("skipp");
				testValidIdentifier("iff");
				testValidIdentifier("thenn");
				testValidIdentifier("elsee");
				testValidIdentifier("whilee");
				testValidIdentifier("doo");
				testValidIdentifier("repeatt");
				testValidIdentifier("untill");
				testValidIdentifier("breakk");
				testValidIdentifier("continuee");
			}
			
			/**
			 * test that the terms spelt in camelcase returns new identifiers
			 */
			@Test
			public void testCamelcaseSpelling(){
				testValidIdentifier("deF");
				testValidIdentifier("skIp");
				testValidIdentifier("iF");
				testValidIdentifier("thEn");
				testValidIdentifier("elSe");
				testValidIdentifier("whiLe");
				testValidIdentifier("dO");
				testValidIdentifier("repeAt");
				testValidIdentifier("untIl");
				testValidIdentifier("breAk");
				testValidIdentifier("continUe");
			}
			
			/**
			 * test that concatenated terms yeild a identifier not a list containing those two terms
			 */
			@Test
			public void testConcatTerm(){
				for(int i = 0; i< terms.size(); i++){
					for (int x = i+1;x<terms.size(); x++){
						testValidIdentifier(terms.get(i)+terms.get(x));
					}
				}
			}
			
	/* TOKENIZATION */
			
		/* SPACE SPLIT */
			
			/**
			 * tests that tokens are split by spaces
			 */
			@Test
			public void testTermsSplitWithSpace(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					for (int x = i+1;x<terms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(tokenClasses.get(i).newInstance());
							expected.add(tokenClasses.get(x).newInstance());
							testValidInput(terms.get(i)+" "+terms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that tokens and identifiers are split by spaces
			 */
			@Test
			public void testTermsAndIdentifierSplitWithSpace(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(terms.get(i)+" identifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(terms.get(i)+" anotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("identifier "+terms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier "+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that tokens and integers are split by spaces
			 */
			@Test
			public void testTermsAndIntegerSplitWithSpace(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Integer(12));
						testValidInput(terms.get(i)+" 12", expected);
						
						expected.set(1, new T_Integer(0));
						testValidInput(terms.get(i)+" 0", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Integer(12));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("12 "+terms.get(i), expected);
						
						expected.set(0, new T_Integer(0));
						testValidInput("0 "+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that identifiers are split by spaces
			 */
			@Test
			public void testIdentifiersSplitWithSpace(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("test and another test", expected);
			}
			
			/**
			 * tests that integers are split by spaces
			 */
			@Test
			public void testIntegersSplitWithSpace(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Integer(3));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				testValidInput("12 3 1 102923", expected);
			}
			
			/**
			 * tests that integers and identifiers are split by spaces
			 */
			@Test
			public void testIntegersAndIdentifiersSplitWithSpace(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("12 and 1 102923 another test", expected);
			}
			
			/**
			 * tests that math tokens are split by spaces
			 */
			@Test
			public void testMathTermsSplitWithSpace(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					for (int x = i+1;x<mathTerms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(mathTokenClasses.get(i).newInstance());
							expected.add(mathTokenClasses.get(x).newInstance());
							testValidInput(mathTerms.get(i)+" "+mathTerms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that math tokens and identifiers are split by spaces
			 */
			@Test
			public void testMathTermsAndIdentifierSplitWithSpace(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(mathTokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(mathTerms.get(i)+" identifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(mathTerms.get(i)+" anotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(mathTokenClasses.get(i).newInstance());
						testValidInput("identifier "+mathTerms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier "+mathTerms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		/* NEW LINE SPLIT */
			
			/**
			 * tests that tokens are split by new lines
			 */
			@Test
			public void testTermsSplitWithNewLine(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					for (int x = i+1;x<terms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(tokenClasses.get(i).newInstance());
							expected.add(tokenClasses.get(x).newInstance());
							testValidInput(terms.get(i)+"\n"+terms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that tokens and identifiers are split by new lines
			 */
			@Test
			public void testTermsAndIdentifierSplitWithNewLine(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(terms.get(i)+"\nidentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(terms.get(i)+"\nanotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("identifier\n"+terms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\n"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that tokens and integers are split by new lines
			 */
			@Test
			public void testTermsAndIntegerSplitWithNewLine(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Integer(12));
						testValidInput(terms.get(i)+"\n12", expected);
						
						expected.set(1, new T_Integer(0));
						testValidInput(terms.get(i)+"\n0", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Integer(12));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("12\n"+terms.get(i), expected);
						
						expected.set(0, new T_Integer(0));
						testValidInput("0\n"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that identifiers are split by new lines
			 */
			@Test
			public void testIdentifiersSplitWithNewLine(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("test\nand\nanother\ntest", expected);
			}
			
			/**
			 * tests that integers are split by new lines
			 */
			@Test
			public void testIntegersSplitWithNewLine(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Integer(3));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				testValidInput("12\n3\n1\n102923", expected);
			}
			
			/**
			 * tests that integers and identifiers are split by new lines
			 */
			@Test
			public void testIntegersAndIdentifiersSplitWithNewLine(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("12\nand\n1\n102923\nanother\ntest", expected);
			}
			
			/**
			 * tests that math tokens are split by new lines
			 */
			@Test
			public void testMathTermsSplitWithNewLine(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					for (int x = i+1;x<mathTerms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(mathTokenClasses.get(i).newInstance());
							expected.add(mathTokenClasses.get(x).newInstance());
							testValidInput(mathTerms.get(i)+"\n"+mathTerms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that math tokens and identifiers are split by new lines
			 */
			@Test
			public void testMathTermsAndIdentifierSplitWithNewLine(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(mathTokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(mathTerms.get(i)+"\nidentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(mathTerms.get(i)+"\nanotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(mathTokenClasses.get(i).newInstance());
						testValidInput("identifier\n"+mathTerms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\n"+mathTerms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		/* FORM FEED SPLIT */
			
			/**
			 * tests that tokens are split by form feeds
			 */
			@Test
			public void testTermsSplitWithFormFeed(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					for (int x = i+1;x<terms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(tokenClasses.get(i).newInstance());
							expected.add(tokenClasses.get(x).newInstance());
							testValidInput(terms.get(i)+"\f"+terms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that tokens and identifiers are split by form feeds
			 */
			@Test
			public void testTermsAndIdentifierSplitWithFormFeed(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(terms.get(i)+"\fidentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(terms.get(i)+"\fanotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("identifier\f"+terms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\f"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that tokens and integers are split by form feeds
			 */
			@Test
			public void testTermsAndIntegerSplitWithFormFeed(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Integer(12));
						testValidInput(terms.get(i)+"\f12", expected);
						
						expected.set(1, new T_Integer(0));
						testValidInput(terms.get(i)+"\f0", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Integer(12));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("12\f"+terms.get(i), expected);
						
						expected.set(0, new T_Integer(0));
						testValidInput("0\f"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that identifiers are split by form feeds
			 */
			@Test
			public void testIdentifiersSplitWithFormFeed(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("test\fand\fanother\ftest", expected);
			}
			
			/**
			 * tests that integers are split by form feeds
			 */
			@Test
			public void testIntegersSplitWithFormFeed(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Integer(3));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				testValidInput("12\f3\f1\f102923", expected);
			}
			
			/**
			 * tests that integers and identifiers are split by form feeds
			 */
			@Test
			public void testIntegersAndIdentifiersSplitWithFormFeed(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("12\fand\f1\f102923\fanother\ftest", expected);
			}
			
			/**
			 * tests that math tokens are split by form feeds
			 */
			@Test
			public void testMathTermsSplitWithFormFeed(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					for (int x = i+1;x<mathTerms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(mathTokenClasses.get(i).newInstance());
							expected.add(mathTokenClasses.get(x).newInstance());
							testValidInput(mathTerms.get(i)+"\f"+mathTerms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that math tokens and identifiers are split by form feeds
			 */
			@Test
			public void testMathTermsAndIdentifierSplitWithFormFeed(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(mathTokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(mathTerms.get(i)+"\fidentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(mathTerms.get(i)+"\fanotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(mathTokenClasses.get(i).newInstance());
						testValidInput("identifier\f"+mathTerms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\f"+mathTerms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		/* CARRIAGE RETURN SPLIT */
			
			/**
			 * tests that tokens are split by carriage returns
			 */
			@Test
			public void testTermsSplitWithCarriageReturn(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					for (int x = i+1;x<terms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(tokenClasses.get(i).newInstance());
							expected.add(tokenClasses.get(x).newInstance());
							testValidInput(terms.get(i)+"\r"+terms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that tokens and identifiers are split by carriage returns
			 */
			@Test
			public void testTermsAndIdentifierSplitWithCarriageReturn(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(terms.get(i)+"\ridentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(terms.get(i)+"\ranotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("identifier\r"+terms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\r"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that tokens and integers are split by carriage returns
			 */
			@Test
			public void testTermsAndIntegerSplitWithCarriageReturn(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Integer(12));
						testValidInput(terms.get(i)+"\r12", expected);
						
						expected.set(1, new T_Integer(0));
						testValidInput(terms.get(i)+"\r0", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Integer(12));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("12\r"+terms.get(i), expected);
						
						expected.set(0, new T_Integer(0));
						testValidInput("0\r"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that identifiers are split by carriage returns
			 */
			@Test
			public void testIdentifiersSplitWithCarriageReturn(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("test\rand\ranother\rtest", expected);
			}
			
			/**
			 * tests that integers are split by carriage returns
			 */
			@Test
			public void testIntegersSplitWithCarriageReturn(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Integer(3));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				testValidInput("12\r3\r1\r102923", expected);
			}
			
			/**
			 * tests that integers and identifiers are split by carriage returns
			 */
			@Test
			public void testIntegersAndIdentifiersSplitWithCarriageReturn(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("12\rand\r1\r102923\ranother\rtest", expected);
			}
			
			/**
			 * tests that math tokens are split by carriage returns
			 */
			@Test
			public void testMathTermsSplitWithCarriageReturn(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					for (int x = i+1;x<mathTerms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(mathTokenClasses.get(i).newInstance());
							expected.add(mathTokenClasses.get(x).newInstance());
							testValidInput(mathTerms.get(i)+"\r"+mathTerms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that math tokens and identifiers are split by carriage returns
			 */
			@Test
			public void testMathTermsAndIdentifierSplitWithCarriageReturn(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(mathTokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(mathTerms.get(i)+"\ridentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(mathTerms.get(i)+"\ranotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(mathTokenClasses.get(i).newInstance());
						testValidInput("identifier\r"+mathTerms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\r"+mathTerms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		/* TAB SPLIT */
			
			/**
			 * tests that tokens are split by tabs
			 */
			@Test
			public void testTermsSplitWithTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					for (int x = i+1;x<terms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(tokenClasses.get(i).newInstance());
							expected.add(tokenClasses.get(x).newInstance());
							testValidInput(terms.get(i)+"\t"+terms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that tokens and identifiers are split by tabs
			 */
			@Test
			public void testTermsAndIdentifierSplitWithTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(terms.get(i)+"\tidentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(terms.get(i)+"\tanotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("identifier\t"+terms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\t"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that tokens and integers are split by tabs
			 */
			@Test
			public void testTermsAndIntegerSplitWithTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Integer(12));
						testValidInput(terms.get(i)+"\t12", expected);
						
						expected.set(1, new T_Integer(0));
						testValidInput(terms.get(i)+"\t0", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Integer(12));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("12\t"+terms.get(i), expected);
						
						expected.set(0, new T_Integer(0));
						testValidInput("0\t"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that identifiers are split by tabs
			 */
			@Test
			public void testIdentifiersSplitWithTab(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("test\tand\tanother\ttest", expected);
			}
			
			/**
			 * tests that integers are split by tabs
			 */
			@Test
			public void testIntegersSplitWithTab(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Integer(3));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				testValidInput("12\t3\t1\t102923", expected);
			}
			
			/**
			 * tests that integers and identifiers are split by tabs
			 */
			@Test
			public void testIntegersAndIdentifiersSplitWithTab(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("12\tand\t1\t102923\tanother\ttest", expected);
			}
			
			/**
			 * tests that math tokens are split by tabs
			 */
			@Test
			public void testMathTermsSplitWithTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					for (int x = i+1;x<mathTerms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(mathTokenClasses.get(i).newInstance());
							expected.add(mathTokenClasses.get(x).newInstance());
							testValidInput(mathTerms.get(i)+"\t"+mathTerms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that math tokens and identifiers are split by tabs
			 */
			@Test
			public void testMathTermsAndIdentifierSplitWithTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(mathTokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(mathTerms.get(i)+"\tidentifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(mathTerms.get(i)+"\tanotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(mathTokenClasses.get(i).newInstance());
						testValidInput("identifier\t"+mathTerms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\t"+mathTerms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		/* VERTICAL TAB SPLIT */
			
			/**
			 * tests that tokens are split by vertical tabs
			 */
			@Test
			public void testTermsSplitWithVerticalTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					for (int x = i+1;x<terms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(tokenClasses.get(i).newInstance());
							expected.add(tokenClasses.get(x).newInstance());
							testValidInput(terms.get(i)+"\013"+terms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that tokens and identifiers are split by vertical tabs
			 */
			@Test
			public void testTermsAndIdentifierSplitWithVerticalTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(terms.get(i)+"\013identifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(terms.get(i)+"\013anotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("identifier\013"+terms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\013"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that tokens and integers are split by vertical tabs
			 */
			@Test
			public void testTermsAndIntegerSplitWithVerticalTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< terms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(tokenClasses.get(i).newInstance());
						expected.add(new T_Integer(12));
						testValidInput(terms.get(i)+"\01312", expected);
						
						expected.set(1, new T_Integer(0));
						testValidInput(terms.get(i)+"\0130", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Integer(12));
						expected.add(tokenClasses.get(i).newInstance());
						testValidInput("12\013"+terms.get(i), expected);
						
						expected.set(0, new T_Integer(0));
						testValidInput("0\013"+terms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			/**
			 * tests that identifiers are split by vertical tabs
			 */
			@Test
			public void testIdentifiersSplitWithVerticalTab(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("test\013and\013another\013test", expected);
			}
			
			/**
			 * tests that integers are split by vertical tabs
			 */
			@Test
			public void testIntegersSplitWithVerticalTab(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Integer(3));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				testValidInput("12\0133\0131\013102923", expected);
			}
			
			/**
			 * tests that integers and identifiers are split by vertical tabs
			 */
			@Test
			public void testIntegersAndIdentifiersSplitWithVerticalTab(){
				ArrayList<Token> expected = new ArrayList<>();
				expected.add(new T_Integer(12));
				expected.add(new T_Identifier("and"));
				expected.add(new T_Integer(1));
				expected.add(new T_Integer(102923));
				expected.add(new T_Identifier("another"));
				expected.add(new T_Identifier("test"));
				testValidInput("12\013and\0131\013102923\013another\013test", expected);
			}
			
			/**
			 * tests that math tokens are split by vertical tabs
			 */
			@Test
			public void testMathTermsSplitWithVerticalTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					for (int x = i+1;x<mathTerms.size(); x++){
						try {
							expected = new ArrayList<>();
							expected.add(mathTokenClasses.get(i).newInstance());
							expected.add(mathTokenClasses.get(x).newInstance());
							testValidInput(mathTerms.get(i)+"\013"+mathTerms.get(x), expected);
						} catch (InstantiationException
								| IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * tests that math tokens and identifiers are split by vertical tabs
			 */
			@Test
			public void testMathTermsAndIdentifierSplitWithVerticalTab(){
				ArrayList<Token> expected;
				for(int i = 0; i< mathTerms.size(); i++){
					try {
						expected = new ArrayList<>();
						expected.add(mathTokenClasses.get(i).newInstance());
						expected.add(new T_Identifier("identifier"));
						testValidInput(mathTerms.get(i)+"\013identifier", expected);
						
						expected.set(1, new T_Identifier("anotherIdentifier"));
						testValidInput(mathTerms.get(i)+"\013anotherIdentifier", expected);
						
						expected = new ArrayList<>();
						expected.add(new T_Identifier("identifier"));
						expected.add(mathTokenClasses.get(i).newInstance());
						testValidInput("identifier\013"+mathTerms.get(i), expected);
						
						expected.set(0, new T_Identifier("anotherIdentifier"));
						testValidInput("anotherIdentifier\013"+mathTerms.get(i), expected);
						
					} catch (InstantiationException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		/* ALL ESCAPE CHARACTERS */
			
			/**
			 * tests all syntax terms and math terms with integers and identifiers separated with all escape characters in random orders
			 */
			@Test
			public void testAllTermsIndentifiersIntegersWithAllEscapeCharactersRandomOrder(){
				try{
					for(int i = 0 ; i<terms.size(); i++){
						for(int y = 0 ; y<mathTerms.size(); y++){
							ArrayList<Token> expected = new ArrayList<>();
							String code = "";
							ArrayList<Token> possibleTokens = new ArrayList<>();
							ArrayList<String> tokenString = new ArrayList<>();
							
							possibleTokens.add(tokenClasses.get(i).newInstance());
							tokenString.add(terms.get(i));
							
							possibleTokens.add(mathTokenClasses.get(y).newInstance());
							tokenString.add(mathTerms.get(y));
							
							possibleTokens.add(new T_Identifier("anotherIdentifier"));
							tokenString.add("anotherIdentifier");
							
							possibleTokens.add(new T_Integer(123457905));
							tokenString.add(123457905+"");
							
							ArrayList<String> escapeChars = new ArrayList<>(Arrays.asList(new String[]{" ","\n","\t","\f","\r", "\013"}));
							
							for(int t = 0; t<4; t++){
								int next = random.nextInt(4-t);
								expected.add(possibleTokens.get(next));
								code += tokenString.get(next);
								
								possibleTokens.remove(next);
								tokenString.remove(next);
								
								next = random.nextInt(5-t);
								code+=escapeChars.get(next);
								escapeChars.remove(next);
								
							}
							testValidInput(code, expected);
						}
					}
				}catch (InstantiationException
						| IllegalAccessException e){
					e.printStackTrace();
				}
			}

		/* INVALID CHARACTERS */
			
			/**
			 * tests to make sure invalid ASCII characters throw errors
			 */
			@Test
			public void testInvalidASCIICharacters(){
				ArrayList<Character> chars = new ArrayList<>();
				for(int i = 1; i < 8; i++){
					chars.add((char)i);
				}
				for(int i = 14; i < 31; i++){
					chars.add((char)i);
				}
				for(int i = 33; i < 40; i++){
					chars.add((char)i);
				}
				chars.add((char)46);
				chars.add((char)63);
				for(int i = 91; i < 96; i++){
					chars.add((char)i);
				}
				chars.add((char)124);
				chars.add((char)126);
				chars.add((char)127);
				for(char c: chars){
					testLexicalInvalidInput(c+"");
				}
			}

		/* EMPTY SPLIT */
			
			/**
			 * tests that math terms can split normal terms
			 */
			@Test
			public void testSplitTermWithMathTerms() {
				List<Token> expected = new ArrayList<>();
				expected.add(new T_Def());
				expected.add(new T_Equal());
				expected.add(new T_Skip());
				expected.add(new T_LessThan());
				testValidInput("def==skip<", expected);
			}
			
			/**
			 * tests that math terms can split identifiers
			 */
			@Test
			public void testSplitIdentifierWithMathTerms() {
				List<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_LessEq());
				expected.add(new T_Identifier("anotherTest"));
				expected.add(new T_EqualDefines());
				testValidInput("test<=anotherTest=", expected);
			}
			
			/**
			 * tests that integers can split normal terms
			 */
			@Test
			public void testSplitTermWithIntegers() {
				List<Token> expected = new ArrayList<>();
				expected.add(new T_Def());
				expected.add(new T_Integer(12));
				expected.add(new T_Skip());
				expected.add(new T_Integer(5));
				testValidInput("def12skip5", expected);
			}
			
			/**
			 * tests that integers can split identifiers
			 */
			@Test
			public void testSplitIdentifierWithIntegerss() {
				List<Token> expected = new ArrayList<>();
				expected.add(new T_Identifier("test"));
				expected.add(new T_Integer(6));
				expected.add(new T_Identifier("anotherTest"));
				expected.add(new T_Integer(124));
				testValidInput("test6anotherTest124", expected);
			}

}
