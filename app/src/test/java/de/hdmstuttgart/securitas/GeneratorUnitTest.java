package de.hdmstuttgart.securitas;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hdmstuttgart.securitas.util.KeyCode;
import de.hdmstuttgart.securitas.util.Generator;

public class GeneratorUnitTest {
    Generator generator = new Generator();

    @Test
    public void password_quality_isCorrect() {
        //properties (lower/upper/number/symbol/length>=8)
        //only one property
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("test")); //lower
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("TEST")); //upper
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("1234")); //number
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("!§$%")); //symbol
        // 2 properties
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("testtest")); //lower  + length
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("TESTTEST")); //upper  + length
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("12345678")); //number + length
        assertEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("!§$%&*-€")); //symbol + length
        //3 properties
        assertEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("Testtest")); //upper  + lower  + length
        assertEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("tEST1"));    //lower  + upper  + number
        assertEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("1234$%&€")); //number + symbol + length
        //4 properties
        assertEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("T!esttest"));    //upper  + symbol + lower + length
        assertEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("1234$%&€TEST")); //number + symbol + upper + length
        //5 properties
        assertEquals(KeyCode.PW_QUALITY_STRONG, generator.checkPasswordQuality("Test1234!?"));//upper + lower + number+ symbol + length

    }

    @Test
    public void password_quality_isNotCorrect() {
        assertNotEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("Testtest"));    //3 properties -> ok
        assertNotEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("Testtest!"));   //4 properties -> ok
        assertNotEquals(KeyCode.PW_QUALITY_BAD, generator.checkPasswordQuality("Testtest!5"));  //5 properties -> strong

        assertNotEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("test"));   //1 properties -> bad
        assertNotEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("Test"));   //2 properties -> bad
        assertNotEquals(KeyCode.PW_QUALITY_OK, generator.checkPasswordQuality("Test+1234!"));   //5 properties -> strong

        assertNotEquals(KeyCode.PW_QUALITY_STRONG, generator.checkPasswordQuality("test"));   //1 property ->bad
        assertNotEquals(KeyCode.PW_QUALITY_STRONG, generator.checkPasswordQuality("Test"));   //2 property ->bad
        assertNotEquals(KeyCode.PW_QUALITY_STRONG, generator.checkPasswordQuality("Test!"));   //3 property ->ok
        assertNotEquals(KeyCode.PW_QUALITY_STRONG, generator.checkPasswordQuality("Test12?"));   //4 property ->ok
    }
}

