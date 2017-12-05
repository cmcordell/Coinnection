package personal.calebcordell.coinnection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

import java.util.Locale;

import personal.calebcordell.coinnection.presentation.Utils;

public class UtilsUnitTest {

    @Test public void utilFormatNumberAustraliaTest() throws Exception {
        Locale.setDefault(new Locale("en", "au"));
        double testNumber = 7560123.25978;

        String expected = "7,560,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Australia is wrong", expected, result);
    }
    @Test public void utilFormatNumberBrazilTest() throws Exception {
        Locale.setDefault(new Locale("br"));
        double testNumber = 7560123.25978;

        String expected = "7 560 123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Brazil is wrong", expected, result);
    }
    @Test public void utilFormatNumberCanadaEnglishTest() throws Exception {
        Locale.setDefault(Locale.CANADA);
        double testNumber = 7560123.25978;

        String expected = "7,560,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Canada (English) is wrong", expected, result);
    }
    @Test public void utilFormatNumberCanadaFrenchTest() throws Exception {
        Locale.setDefault(Locale.CANADA_FRENCH);
        double testNumber = 7560123.25978;

        String expected = "7 560 123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Canada (French) is wrong", expected, result);
    }
    @Test public void utilFormatNumberChinaTest() throws Exception {
        Locale.setDefault(Locale.CHINA);
        double testNumber = 7560123.25978;

        String expected = "756,0123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in China is wrong", expected, result);
    }
    @Test public void utilFormatNumberFranceTest() throws Exception {
        Locale.setDefault(Locale.FRANCE);
        double testNumber = 7560123.25978;

        String expected = "7 560 123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in France is wrong", expected, result);
    }
    @Test public void utilFormatNumberGermanyTest() throws Exception {
        Locale.setDefault(Locale.GERMANY);
        double testNumber = 7560123.25978;

        String expected = "7 560 123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Germany is wrong", expected, result);
    }
    @Test public void utilFormatNumberIndiaTest() throws Exception {
        Locale.setDefault(new Locale("hi", "in"));
        double testNumber = 7560123.25978;

        String expected = "75,60,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in India is wrong", expected, result);
    }
    @Test public void utilFormatNumberIndonesiaTest() throws Exception {
        Locale.setDefault(new Locale("id"));
        double testNumber = 7560123.25978;

        String expected = "7 560 123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Indonesia is wrong", expected, result);
    }
    @Test public void utilFormatNumberItalyTest() throws Exception {
        Locale.setDefault(Locale.ITALY);
        double testNumber = 7560123.25978;

        String expected = "7.560.123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Italy is wrong", expected, result);
    }
    @Test public void utilFormatNumberJapanTest() throws Exception {
        Locale.setDefault(Locale.JAPAN);
        double testNumber = 7560123.25978;

        String expected = "7,560,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Japan is wrong", expected, result);
    }
    @Test public void utilFormatNumberKoreaTest() throws Exception {
        Locale.setDefault(Locale.KOREA);
        double testNumber = 7560123.25978;

        String expected = "7,560,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Korea is wrong", expected, result);
    }
    @Test public void utilFormatNumberMexicoTest() throws Exception {
        Locale.setDefault(new Locale("mx"));
        double testNumber = 7560123.25978;

        String expected = "7,560,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Mexico is wrong", expected, result);
    }
    @Test public void utilFormatNumberRussiaTest() throws Exception {
        Locale.setDefault(new Locale("ru"));
        double testNumber = 7560123.25978;

        String expected = "7 560 123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Russia is wrong", expected, result);
    }
    @Test public void utilFormatNumberSpainTest() throws Exception {
        Locale.setDefault(new Locale("es", "es"));
        double testNumber = 7560123.25978;

        String expected = "7 560 123,25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Spain is wrong", expected, result);
    }
    @Test public void utilFormatNumberSwitzerlandTest() throws Exception {
        Locale.setDefault(new Locale("ch"));
        double testNumber = 7560123.25978;

        String expected = "7'560'123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in Switzerland is wrong", expected, result);
    }
    @Test public void utilFormatNumberUKTest() throws Exception {
        Locale.setDefault(Locale.UK);
        double testNumber = 7560123.25978;

        String expected = "7,560,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in UK is wrong", expected, result);
    }
    @Test public void utilFormatNumberUSTest() throws Exception {
        Locale.setDefault(Locale.US);
        double testNumber = 7560123.25978;

        String expected = "7,560,123.25978";
        String result = Utils.getFormattedNumberString(testNumber);

        assertEquals("Formatted Number String in US is wrong", expected, result);
    }
}