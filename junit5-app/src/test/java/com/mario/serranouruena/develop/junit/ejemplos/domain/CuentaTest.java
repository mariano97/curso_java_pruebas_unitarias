package com.mario.serranouruena.develop.junit.ejemplos.domain;

import com.mario.serranouruena.develop.junit.ejemplos.exceptions.DineroInsificienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;

    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
        System.out.println("iniciando el metodo.");
        this.cuenta = new Cuenta("Mario Serrano", new BigDecimal("1000.12345"));
        testReporter.publishEntry(" Ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().get().getName() +
                " con las etiquetas " + testInfo.getTags());
        this.testInfo = testInfo;
        this.testReporter = testReporter;
    }

    @AfterEach
    void afterEach() {
        System.out.println("finalizando el metodo.");

    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finalizando el test.");
    }

    @Tag("cuenta")
    @Nested
    class CuentaTestNombreSaldo {
        @Test
        @DisplayName("prabando el nombre de la cuenta corriente")
        void testNombreCuenta() {
            System.out.println(testInfo.getTags());
            if (testInfo.getTags().contains("cuenta")) {
                System.out.println("Hacer algo con la etiqueta cuenta");
            }
            //cuenta.setPersona("Mario Serrano");

            String esperado = "Mario Serrano";
            String real = cuenta.getPersona();
            Assertions.assertNotNull(real, () -> "la cuenta no puede ser nula");
            Assertions.assertEquals(esperado, real, () -> "el nombre de la cuenta no es el que se esperaba");
            Assertions.assertTrue(real.equals(esperado), () -> "nombre esperado debe ser igual al real");
        }

        @Test
        @DisplayName("prabando el saldo de la cuenta corriente")
        void testSaldoCuenta() {
            //cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            Assertions.assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        void testReferenciaCuenta() {
            Cuenta cuenta = new Cuenta("Jhon Doe", new BigDecimal("89000.9997"));
            Cuenta cuenta2 = new Cuenta("Jhon Doe", new BigDecimal("89000.9997"));

            //Assertions.assertNotEquals(cuenta2, cuenta);
            Assertions.assertEquals(cuenta2, cuenta);
        }
    }

    @Nested
    @DisplayName("Probando atributos cuenta")
    class CuentaOperacionesTest {

        @Tag("cuenta")
        @Test
        @DisplayName("el nombre!")
        void testDebitoCuenta() {
            cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
            cuenta.debito(new BigDecimal("100"));
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertEquals(900, cuenta.getSaldo().intValue());
            Assertions.assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Tag("cuenta")
        @Test
        void testCreditoCuenta() {
            cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
            cuenta.credito(new BigDecimal("100"));
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertEquals(1100, cuenta.getSaldo().intValue());
            Assertions.assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }

        @Tag("cuenta")
        @Tag("banco")
        @Test
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("Joe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));
            Banco banco = new Banco();
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            Assertions.assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            Assertions.assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }
    }


    @Tag("cuenta")
    @Tag("error")
    @Test
    void testDineroInsuficienteExceptionuento() {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        Exception exception = Assertions.assertThrows(DineroInsificienteException.class, () -> {
            cuenta.debito(new BigDecimal("1500"));
        });
        String real = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        Assertions.assertEquals(esperado, real);
    }


    @Tag("cuenta")
    @Tag("banco")
    @Test
    //@Disabled
    void testRelacionBancoCuentas() {
        //Assertions.fail();
        Cuenta cuenta1 = new Cuenta("Joe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        Assertions.assertAll(() -> {
                    Assertions.assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
                },
                () -> {
                    Assertions.assertEquals("3000", cuenta1.getSaldo().toPlainString());
                },
                () -> {
                    Assertions.assertEquals(2, banco.getCuentas().size());
                },
                () -> {
                    Assertions.assertEquals("Banco del Estado", cuenta1.getBanco().getNombre());
                },
                () -> {
                    Assertions.assertEquals("Andres", banco.getCuentas().stream()
                            .filter(cuenta -> cuenta.getPersona().equals("Andres"))
                            .findFirst()
                            .get().getPersona());
                },
                () -> {
                    Assertions.assertTrue(banco.getCuentas().stream()
                            .anyMatch(cuenta -> cuenta.getPersona().equals("Andres")));
                }
        );

    }

    @Nested
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }


        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void solJDK8() {

        }

        @Test
        @EnabledOnJre(JRE.JAVA_11)
        void soloJDK11() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_11)
        void testNoJDK11() {
        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((key, value) -> System.out.println(key + ":" + value));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*11.*")
        void testJavaVersion() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
        void testSolo64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "mario.serrano")
        void testUserPrticular() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDesarrollo() {
        }
    }

    @Nested
    class VariableAmbienteTest {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + " : " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-11.0.12.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "12")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {

        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProd() {

        }
    }

    @Test
    // @DisplayName("prabando el saldo de la cuenta corriente 2")
    void testSaldoCuentaDEV() {
        //cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        Assumptions.assumeTrue(esDev);
        Assertions.assertNotNull(cuenta.getSaldo());
        Assertions.assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        Assertions.assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
        // @DisplayName("prabando el saldo de la cuenta corriente 2")
    void testSaldoCuentaDEV2() {
        //cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        Assumptions.assumingThat(esDev, () -> {
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        });

        Assertions.assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @RepeatedTest(value= 5, name = "Repetición número {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 3) {
            System.out.println("info: " + info.getCurrentRepetition());
        }
        cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal("100"));
        Assertions.assertNotNull(cuenta.getSaldo());
        Assertions.assertEquals(900, cuenta.getSaldo().intValue());
        Assertions.assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("param")
    @Nested
    class PruebasParametrizadasTest {


        @ParameterizedTest(name = "numro {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
        void testDebitoCuentaValueSources(String monto) {
            cuenta.debito(new BigDecimal(monto));
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numro {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.12345"})
        void testDebitoCuentaCSVSource(String index, String monto) {
            System.out.println(index + "->" + monto);
            cuenta.debito(new BigDecimal(monto));
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numro {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCSVFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numro {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvSource({"200,100,John,Andres", "250,200,Pepe,Pepe", "300,300,maria,Maria", "510,500,Pepa,Pepa", "750,700,Lucas,Luca", "1000.12345,1000.12345,Cata,Cata"})
        void testDebitoCuentaCSVSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + "->" + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);
            Assertions.assertNotNull(cuenta.getPersona());
            Assertions.assertEquals(esperado,actual);
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numro {index} ejecutando con el valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoCuentaCSVFileSource2(String saldo, String monto, String esperado, String actual) {
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);
            Assertions.assertNotNull(cuenta.getPersona());
            Assertions.assertEquals(esperado,actual);
            Assertions.assertNotNull(cuenta.getSaldo());
            Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }
    }


    @Tag("param")
    @ParameterizedTest(name = "numro {index} ejecutando con el valor {0} - {argumentsWithNames}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        Assertions.assertNotNull(cuenta.getSaldo());
        Assertions.assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700", "1000.12345");
    }

    @Tag("timeout")
    @Nested
    class EjemploTimeOutTest {

        @Test
        @Timeout(1)
        void pruebaTimeout() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        void testTimeoutAssertions() {
            Assertions.assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.MILLISECONDS.sleep(4000);
            });
        }
    }

}