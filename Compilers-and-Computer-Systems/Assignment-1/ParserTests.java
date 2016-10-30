import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * A load of tests for the Compilers Assignment 1 Task 2
 * 
 * README
 *
 * This is the first draft
 *
 * This has to be put in the same package as the Parser.java class
 *
 * Feel free to share this with whoever you want to.
 *
 * It is strongly recommended that you write some of your own tests too.
 * WARNING:
 * These tests are made from my interpretation of the assignment
 * and i do not take any responsibility if they are wrong
 * use as you wish.
 *
 * @author Rikkey Paal
 */
public class ParserTests {

	/**
	 * correct bracket depth
	 */
	
	Parser parser;
	ArrayList<Class<?extends Token>> invalidTerms;
	
	/**
	 * initiates the parser that will be tested
	 */
	@Before
	public void init() {
		parser = Task2.create();
		invalidTerms = new ArrayList<>();
		invalidTerms.add( T_Def.class);
		invalidTerms.add( T_If.class);
		invalidTerms.add( T_Then.class);
		invalidTerms.add( T_Else.class);
		invalidTerms.add( T_While.class);
		invalidTerms.add( T_Do.class);
		invalidTerms.add( T_Repeat.class);
		invalidTerms.add( T_Until.class);
		invalidTerms.add( T_Break.class);
		invalidTerms.add( T_Continue.class);
		invalidTerms.add(T_LeftBracket.class);
		invalidTerms.add(T_RightBracket.class);
		invalidTerms.add(T_EqualDefines.class);
		invalidTerms.add(T_Equal.class);
		invalidTerms.add(T_LessThan.class);
		invalidTerms.add(T_GreaterThan.class);
		invalidTerms.add(T_LessEq.class);
		invalidTerms.add(T_GreaterEq.class);
		invalidTerms.add(T_Comma.class);
		invalidTerms.add(T_Assign.class);
		invalidTerms.add(T_Plus.class);
		invalidTerms.add(T_Times.class);
		invalidTerms.add(T_Minus.class);
		invalidTerms.add(T_Div.class);
		invalidTerms.add(T_Identifier.class);
	}
	
	/* PRIVATE METHODS */

		private String assertEquals(Block expecting, Block actual){
			String returnString = "";
			if(expecting.exps.size() == actual.exps.size()){
				List<Exp> ee = expecting.exps;
				List<Exp> ea = actual.exps;
				for(int i = 0; i<ee.size(); i++){
					if(ee.get(i).getClass().equals(ea.get(i).getClass())){
						if(ee.get(i).getClass().equals(BlockExp.class)){
							assertEquals(((BlockExp)ee.get(i)).b, ((BlockExp)ea.get(i)).b);
						}else if(ee.get(i).getClass().equals(IntLiteral.class)){
							if(((IntLiteral)ee.get(i)).n != ((IntLiteral)ea.get(i)).n){
								returnString += "Integer at "+i+" of block "+blockToString(expecting)+" and "+ blockToString(actual)+" have differnt values";
							}
						}
					}else{
						returnString+= "index "+i+" of block "+blockToString(expecting)+" and "+ blockToString(actual)+" are of different types";
					}
				}
			}else{
				returnString += blockToString(expecting)+" is not the same size as "+blockToString(actual);
			}
			return returnString;
		}
	
		private void testValidInput(List<Token> input, Block expecting){
			try {
				String str = assertEquals(expecting, parser.parse(input));
				if(str.length()>0){
					fail(str);
				}
			} catch (SyntaxException e) {
				fail("threw syntax exception for valid input \""+input+"\"");
			} catch (Task2Exception e) {
				fail("threw Task2 exception for valid input \""+input+"\"");
			}
		}
		
		private void testValidInput(Token t, Exp e){
			testValidInput(new Block(new ArrayList<>(Arrays.asList(new Exp[]{e}))), new Token[]{t});
		}
		
		private void testValidInput(Block b, Token ...t){
			testValidInput(new ArrayList<Token>(Arrays.asList(t)), b);
		}
		
		private void testSyntaxInValidInputL(List<Token> input){
			try {
				parser.parse(input);
				fail("dident throw syntax exception for invalid input \""+input+"\"");
			} catch (SyntaxException e) {
			} catch (Task2Exception e) {
				fail("threw Task2 exception where syntax exception should have been thrown for \""+input+"\"");
			}
		}
		
		private void testSyntaxInValidInput(Token input){
			List<Token> tokens = new ArrayList<>();
			tokens.add(input);
			testSyntaxInValidInputL(tokens);
		}
		
		private void testSyntaxInValidInput(Token ...input){
			testSyntaxInValidInputL(Arrays.asList(input));
		}
		
		private String blockToString(Block b){
			String currString = "";
			for (Exp e: b.exps){
				if(e.getClass().equals(IntLiteral.class)){
					currString+=((IntLiteral)e).n+";";
				}else if(e.getClass().equals(Skip.class)){
					currString+="skip;";
				}else if(e.getClass().equals(BlockExp.class)){
					currString = currString.length()>0?currString.substring(0,currString.length()-1):currString;
					currString+="("+blockToString(((BlockExp)e).b)+");";
				}else{
					return null;
				}
			}
			return currString.length()>0?currString.substring(0,currString.length()-1):currString;
		}
		
		private String listToString(List<Token> tokens){
			String tokenString = "[";
			for(Token t :tokens){
				if (t.getClass().equals(T_Identifier.class)){
					tokenString += "Identifier("+((T_Identifier)t).s+"), ";
				}else if(t.getClass().equals(T_Integer.class)){
					tokenString += "Integer("+((T_Integer)t).n+"), ";
				}else {
					tokenString += (t.getClass()+"").substring(31)+", ";
				}
			}
			tokenString = tokenString.substring(0,tokenString.length()-2)+"]";
			return tokenString;
		}
		
		private Block makeBlock(Exp ...exps){
			return new Block(new ArrayList<>(Arrays.asList(exps)));
		}
		
	/**
	 * tests that the {@link Task2#create()}
	 */
	@Test
	public void testParserExists(){
		assertNotNull(parser);
	}
	
	/* INVALID INPUT*/
		
		/**
		 * tests that a {@link SyntaxException} is thrown when a null input is provided
		 */
		@Test
		public void testNullInput(){
			testSyntaxInValidInputL(null);
		}
		
		/**
		 * tests that a {@link SyntaxException} is thrown when a empty input is provided
		 */
		@Test
		public void testEmptyInput(){
			testSyntaxInValidInputL(new ArrayList<>());
		}
	
		/**
		 * tests that a list of input holding a single token will always return a syntax exception
		 */
		@Test
		public void testCantBeOneLong(){
			ArrayList<Class<?extends Token>> allTokens = new ArrayList<>();
			allTokens.addAll(invalidTerms);
			allTokens.add(T_Skip.class);
			allTokens.add(T_Semicolon.class);
			allTokens.add(T_Integer.class);
			allTokens.add(T_LeftCurlyBracket.class);
			allTokens.add(T_RightCurlyBracket.class);
			
			for(int i = 0; i< allTokens.size(); i++){
				Token a = null;
				try {
					if(allTokens.get(i).equals(T_Identifier.class)){
						a = new T_Identifier("hi");
					}else if(allTokens.get(i).equals(T_Integer.class)){
						a = new T_Integer(5);
					}else{
						a = allTokens.get(i).newInstance();
					}
					
					testSyntaxInValidInput(a);
				} catch (InstantiationException
						| IllegalAccessException e) {
					fail("Failed trying to intantiate " + allTokens.get(i).getClass());
				}
			}
		}
		
		/**
		 * tests that a list of input holding two tokens will always return a syntax exception
		 */
		@Test
		public void testCantBeTwoLong(){
			ArrayList<Class<?extends Token>> allTokens = new ArrayList<>();
			allTokens.addAll(invalidTerms);
			allTokens.add(T_Skip.class);
			allTokens.add(T_Semicolon.class);
			allTokens.add(T_Integer.class);
			allTokens.add(T_LeftCurlyBracket.class);
			allTokens.add(T_RightCurlyBracket.class);
			
			for(int i = 0; i< allTokens.size(); i++){
				for(int x = 0; x<allTokens.size(); x++){
					Token a = null,b = null;
					try {
						if(allTokens.get(i).equals(T_Identifier.class)){
							a = new T_Identifier("hi");
						}else if(allTokens.get(i).equals(T_Integer.class)){
							a = new T_Integer(5);
						}else{
							a = allTokens.get(i).newInstance();
						}
						
						if(allTokens.get(x).equals(T_Identifier.class)){
							b = new T_Identifier("hi");
						}else if(allTokens.get(x).equals(T_Integer.class)){
							b = new T_Integer(5);
						}else{
							b = allTokens.get(x).newInstance();
						}
						testSyntaxInValidInput(a, b);
					} catch (InstantiationException
							| IllegalAccessException e) {
						fail("Failed trying to intantiate " + (a==null?allTokens.get(i).getClass():allTokens.get(x).getClass()));
					}
				}
			}
		}
		
		/**
		 * tests that a list of input holding two tokens will always return a syntax exception if it starts with a {@link T_LeftCurlyBracket} but doesn't end in a {@link T_RightCurlyBracket}
		 */
		@Test
		public void testCantBeThreeLongWithoutEndingInRightCurlyBracket(){
			ArrayList<Class<?extends Token>> allTokens = new ArrayList<>();
			allTokens.addAll(invalidTerms);
			allTokens.add(T_Skip.class);
			allTokens.add(T_Semicolon.class);
			allTokens.add(T_Integer.class);
			allTokens.add(T_LeftCurlyBracket.class);
			for(int i = 0; i< allTokens.size(); i++){
				for(int x = 0; x<allTokens.size(); x++){
					Token a = null,b = null;
					try {
						if(allTokens.get(i).equals(T_Identifier.class)){
							a = new T_Identifier("hi");
						}else if(allTokens.get(i).equals(T_Integer.class)){
							a = new T_Integer(5);
						}else{
							a = allTokens.get(i).newInstance();
						}
						
						if(allTokens.get(x).equals(T_Identifier.class)){
							b = new T_Identifier("hi");
						}else if(allTokens.get(x).equals(T_Integer.class)){
							b = new T_Integer(5);
						}else{
							b = allTokens.get(x).newInstance();
						}
						testSyntaxInValidInput(new T_LeftCurlyBracket(), a, b);
					} catch (InstantiationException
							| IllegalAccessException e) {
						fail("Failed trying to intantiate " + (a==null?allTokens.get(i).getClass():allTokens.get(x).getClass()));
					}
				}
			}
		}
		
		/**
		 * tests that a list of input holding two tokens will always return a syntax exception if it doesnt start with a {@link T_LeftCurlyBracket} even if it does end in a {@link T_RightCurlyBracket}
		 */
		@Test
		public void testCantBeThreeLongWithoutStartingWithLeftCurlyBracket(){
			ArrayList<Class<?extends Token>> allTokens = new ArrayList<>();
			allTokens.addAll(invalidTerms);
			allTokens.add(T_Skip.class);
			allTokens.add(T_Semicolon.class);
			allTokens.add(T_Integer.class);
			allTokens.add(T_RightCurlyBracket.class);
			for(int i = 0; i< allTokens.size(); i++){
				for(int x = 0; x<allTokens.size(); x++){
					Token a = null,b = null;
					try {
						if(allTokens.get(i).equals(T_Identifier.class)){
							a = new T_Identifier("hi");
						}else if(allTokens.get(i).equals(T_Integer.class)){
							a = new T_Integer(5);
						}else{
							a = allTokens.get(i).newInstance();
						}
						
						if(allTokens.get(x).equals(T_Identifier.class)){
							b = new T_Identifier("hi");
						}else if(allTokens.get(x).equals(T_Integer.class)){
							b = new T_Integer(5);
						}else{
							b = allTokens.get(x).newInstance();
						}
						testSyntaxInValidInput(a, b, new T_RightCurlyBracket());
					} catch (InstantiationException
							| IllegalAccessException e) {
						fail("Failed trying to intantiate " + (a==null?allTokens.get(i).getClass():allTokens.get(x).getClass()));
					}
				}
			}
		}
		
		/**
		 * tests that a list of input holding two tokens will always return a syntax exception if it doesnt start with a {@link T_LeftCurlyBracket} even if it does end in a {@link T_RightCurlyBracket}
		 */
		@Test
		public void testCantBeThreeLongWithoutStartingWithLeftCurlyBracketOrEndingWithRightCurlyBracket(){
			ArrayList<Class<?extends Token>> allTokens = new ArrayList<>();
			allTokens.addAll(invalidTerms);
			allTokens.add(T_Skip.class);
			allTokens.add(T_Semicolon.class);
			allTokens.add(T_Integer.class);
			allTokens.add(T_RightCurlyBracket.class);
			allTokens.add(T_LeftCurlyBracket.class);
			for(int i = 0; i< allTokens.size(); i++){
				for(int x = 0; x<allTokens.size(); x++){
					for(int y = 0; y<allTokens.size(); y++){
						Token a = null,b = null, c = null;
						if(allTokens.get(i).equals(T_LeftCurlyBracket.class)&&allTokens.get(y).equals(T_RightCurlyBracket.class)){
							continue;
						}
						try {
							if(allTokens.get(i).equals(T_Identifier.class)){
								a = new T_Identifier("hi");
							}else if(allTokens.get(i).equals(T_Integer.class)){
								a = new T_Integer(5);
							}else{
								a = allTokens.get(i).newInstance();
							}
							
							if(allTokens.get(x).equals(T_Identifier.class)){
								b = new T_Identifier("hi");
							}else if(allTokens.get(x).equals(T_Integer.class)){
								b = new T_Integer(5);
							}else{
								b = allTokens.get(x).newInstance();
							}
							
							if(allTokens.get(y).equals(T_Identifier.class)){
								c = new T_Identifier("hi");
							}else if(allTokens.get(y).equals(T_Integer.class)){
								c = new T_Integer(5);
							}else{
								c = allTokens.get(y).newInstance();
							}
							testSyntaxInValidInput(a, b,c);
						} catch (InstantiationException
								| IllegalAccessException e) {
							fail("Failed trying to intantiate " + (a==null?allTokens.get(i).getClass():allTokens.get(y).getClass()));
						}
					}
				}
			}
		}
		
		/**
		 * tests that if the input starts with a {@link T_LeftCurlyBracket} and ends with a {@link T_RightCurlyBracket}, it will still throw a {@link SyntaxException} if the content is a invalid Token
		 */
		@Test
		public void testThreeLongContainingInvalidToken(){
			ArrayList<Class<?extends Token>> allTokens = new ArrayList<>();
			allTokens.addAll(invalidTerms);
			allTokens.add(T_Semicolon.class);
			allTokens.add(T_LeftCurlyBracket.class);
			allTokens.add(T_RightCurlyBracket.class);
			
			for(int i = 0; i< allTokens.size(); i++){
				Token a = null;
				try {
					if(allTokens.get(i).equals(T_Identifier.class)){
						a = new T_Identifier("hi");
					}else if(allTokens.get(i).equals(T_Integer.class)){
						a = new T_Integer(5);
					}else{
						a = allTokens.get(i).newInstance();
					}
					
					testSyntaxInValidInput(new T_LeftCurlyBracket(), a, new T_RightCurlyBracket());
				} catch (InstantiationException
						| IllegalAccessException e) {
					fail("Failed trying to intantiate " + allTokens.get(i).getClass());
				}
			}
		}
		
		/**
		 * tests that if the input starts with a {@link T_LeftCurlyBracket} and ends with a {@link T_RightCurlyBracket}, it will not throw a {@link SyntaxException} if the content is a valid Token (integer or skip
		 */
		@Test
		public void testThreeLongContainingValidToken(){
			testValidInput(makeBlock(new IntLiteral(5)), new T_LeftCurlyBracket(), new T_Integer(5), new T_RightCurlyBracket());
			testValidInput(makeBlock(new Skip()), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket());
		}
		
	/*	BRACKETING */
		
		/**
		 * tests that a {@link SyntaxException} will be thrown if there is an unclosed bracket nested inside another
		 */
		@Test
		public void testNestedUnclosedBrackets(){
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(5), new T_RightCurlyBracket());
		}
		
		/**
		 * tests that a {@link SyntaxException} will not be thrown if there is nested brackets that are all closed
		 */
		@Test
		public void testNestedClosedBrackets(){
			testValidInput(makeBlock(new BlockExp(makeBlock(new Skip()))), new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new BlockExp(makeBlock(new IntLiteral(5)))), new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(5), new T_RightCurlyBracket(), new T_RightCurlyBracket());
		}
		
		/**
		 * tests that a {@link SyntaxException} will be thrown if there is an unclosed bracket one after another
		 */
		@Test
		public void testLinearUnClosedBrackets(){
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(13), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(76), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket());
		}
		
		/**
		 * tests that a {@link SyntaxException} will be thrown if there is too many right curly brackets nested inside another
		 */
		@Test
		public void testNestedUnOpenedBrackets(){
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_Integer(5), new T_RightCurlyBracket(), new T_RightCurlyBracket());
		}
		
		/**
		 * tests that a {@link SyntaxException} will be thrown if there is an unclosed bracket one after another
		 */
		@Test
		public void testLinearUnOpenedBrackets(){
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(13), new T_RightCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(76), new T_RightCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
		}
		
	/*SEMICOLON SPLiT*/

		/**
		 * tests that a {@link SyntaxException} will be thrown if there is two bracket one after another that are not sepperated by a semicolon
		 */
		@Test
		public void testLinearUnSepperatedBrackets(){
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(13), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Skip(), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(76), new T_RightCurlyBracket(),new T_LeftCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
		}
		
		/**
		 * tests that a {@link SyntaxException} will not be thrown if there are brackets one after another seepperated by a semicolon
		 */
		@Test
		public void testLinearSepperatedBrackets(){
			testValidInput(makeBlock(new BlockExp(makeBlock(new Skip())), new BlockExp(makeBlock(new Skip()))), 				new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), 	   new T_RightCurlyBracket(), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Skip(), 	 new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new BlockExp(makeBlock(new Skip())), new BlockExp(makeBlock(new IntLiteral(12)))), 		new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), 	   new T_RightCurlyBracket(), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new BlockExp(makeBlock(new IntLiteral(13))), new BlockExp(makeBlock(new Skip()))), 		new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(13), new T_RightCurlyBracket(), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Skip(), 	 new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new BlockExp(makeBlock(new IntLiteral(76))), new BlockExp(makeBlock(new IntLiteral(12)))), new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Integer(76), new T_RightCurlyBracket(), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
		}


		/**
		 * tests that a {@link SyntaxException} will be thrown if there is two parts one after another that are not sepperated by a semicolon
		 */
		@Test
		public void testLinearUnSepperated(){
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_Skip(), new T_Skip(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_Skip(), new T_Integer(934), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_Integer(12), new T_Skip(), new T_RightCurlyBracket());
			testSyntaxInValidInput(new T_LeftCurlyBracket(), new T_Integer(12), new T_Integer(934), new T_RightCurlyBracket());
			
		}
		
		/**
		 * tests that a {@link SyntaxException} will not be thrown if there is two parts one after another that are sepperated by a semicolon
		 */
		@Test
		public void testLinearSepperatedSkip(){
			testValidInput(makeBlock(new Skip(), new Skip()), new T_LeftCurlyBracket(), new T_Skip(), new T_Semicolon(), new T_Skip(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new Skip(), new IntLiteral(934)), new T_LeftCurlyBracket(), new T_Skip(), new T_Semicolon(),new T_Integer(934), new T_RightCurlyBracket());
			testValidInput(makeBlock(new IntLiteral(12), new Skip()), new T_LeftCurlyBracket(), new T_Integer(12), new T_Semicolon(),new T_Skip(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new IntLiteral(12), new IntLiteral(934)), new T_LeftCurlyBracket(), new T_Integer(12), new T_Semicolon(),new T_Integer(934), new T_RightCurlyBracket());
		}
		
	/*DUMMY CODE*/
		
		/**
		 * tests against a group of valid input to ensure they all pass
		 */
		@Test
		public void testMultipleValidParseing(){
			testValidInput(makeBlock(new Skip(), new IntLiteral(12)),
					new T_LeftCurlyBracket(), new T_Skip(), new T_Semicolon(), new T_Integer(12), new T_RightCurlyBracket());
			testValidInput(makeBlock(new Skip(), new BlockExp(makeBlock(new IntLiteral(12)))),
					new T_LeftCurlyBracket(), new T_Skip(), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new BlockExp(makeBlock(new Skip(), new IntLiteral(12)))), 
					new T_LeftCurlyBracket(), new T_LeftCurlyBracket(), new T_Skip(), new T_Semicolon(), new T_Integer(12), new T_RightCurlyBracket(), new T_RightCurlyBracket());
			testValidInput(makeBlock(new Skip(), new BlockExp(makeBlock(new Skip(), new IntLiteral(12), new BlockExp(makeBlock(new IntLiteral(56), new Skip())), new BlockExp(makeBlock(new IntLiteral(42))), new IntLiteral(7)))),
					new T_LeftCurlyBracket(), new T_Skip(), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Skip(), new T_Semicolon(), new T_Integer(12), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Integer(56), new T_Semicolon(), new T_Skip(), new T_RightCurlyBracket(), new T_Semicolon(), new T_LeftCurlyBracket(), new T_Integer(42), new T_RightCurlyBracket(),new T_Semicolon(), new T_Integer(7), new T_RightCurlyBracket(), new T_RightCurlyBracket());
		}
}
