/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ec.edu.epn.modelo;

import ec.edu.epn.excepciones.ExcepcionCuentaNoCreada;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Marcela
 */
public class CuentaAhorrosTest {

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCuentaAhorro1() throws ExcepcionCuentaNoCreada {
        double monto = 180;
        CuentaAhorros cuenta = new CuentaAhorros(monto);
        int antiguedadEsperada = 0;
        assertEquals(antiguedadEsperada, cuenta.getAntiguedad());

        String categoriaEsperada = "NORMAL";
        assertEquals(categoriaEsperada, cuenta.getCategoria());
    }

    @Test
    public void testCuentaAhorro2() {
        double monto = 179;
        String mensajeEsperado = "La cuenta no se puede crear con menos de $180";
        ExcepcionCuentaNoCreada excepcionEsperada = assertThrows(ExcepcionCuentaNoCreada.class,
                () -> new CuentaAhorros(monto));
        assertEquals(mensajeEsperado, excepcionEsperada.getMessage());
    }

    @Test
    public void testDepositar() throws ExcepcionCuentaNoCreada {
        double montoInicial = 2500;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        double cantidadDeposito = 50;
        cuenta.depositar(cantidadDeposito);

        double montoEsperado = montoInicial + cantidadDeposito;
        assertEquals(montoEsperado, cuenta.getMonto());
    }

    @Test
    public void testRetirar() throws ExcepcionCuentaNoCreada {
        double montoInicial = 400;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);
        
        cuenta.incrementarAntiguedad(); // Incrementar la antigüedad para poder retirar más tarde
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();

        double cantidadRetiro = 200;
        cuenta.retirar(cantidadRetiro);

        double montoEsperado = montoInicial - cantidadRetiro;
        assertEquals(montoEsperado, cuenta.getMonto());
        assertEquals("NORMAL", cuenta.getCategoria()); // Aún en la categoría NORMAL después del retiro

        cuenta.incrementarAntiguedad(); // Incrementar la antigüedad para poder retirar más tarde
        cuenta.retirar(100);
        assertEquals(100, cuenta.getMonto()); // Después del segundo retiro
        assertEquals("NORMAL", cuenta.getCategoria()); // Aún en la categoría NORMAL

        cuenta.incrementarAntiguedad(); // Incrementar la antigüedad para poder retirar más tarde
        cuenta.retirar(100);
        assertEquals(0, cuenta.getMonto()); // Después del tercer retiro
        assertEquals("NORMAL", cuenta.getCategoria()); // Aún en la categoría NORMAL

        cuenta.incrementarAntiguedad(); // Incrementar la antigüedad para poder retirar más tarde
        cuenta.retirar(50);
        assertEquals(0, cuenta.getMonto()); // Después del cuarto retiro, monto insuficiente
        assertEquals("NORMAL", cuenta.getCategoria()); // Aún en la categoría NORMAL
    }

    @Test
    public void testRetirarConAntiguedadSuficiente() throws ExcepcionCuentaNoCreada {
        double montoInicial = 5000;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        cuenta.incrementarAntiguedad(); // Incrementar la antigüedad para poder retirar más tarde
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();

        double cantidadRetiro = 2000;
        cuenta.retirar(cantidadRetiro);

        double montoEsperado = montoInicial - cantidadRetiro;
        assertEquals(montoEsperado, cuenta.getMonto());
        assertEquals(5, cuenta.getAntiguedad());
        assertEquals("VIP", cuenta.getCategoria()); // Ahora en la categoría VIP después del retiro

    }

    @Test
    public void testRetirarMontoInsuficiente() throws ExcepcionCuentaNoCreada {
        double montoInicial = 180;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        double cantidadRetiro = 200;
        cuenta.retirar(cantidadRetiro);
        assertEquals(montoInicial, cuenta.getMonto()); // El monto no cambia, retiro no realizado
    }

    @Test
    public void testDepositarCantidadNegativa() throws ExcepcionCuentaNoCreada {
        double montoInicial = 1000;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        double cantidadDeposito = -50;
        cuenta.depositar(cantidadDeposito);

        assertEquals(montoInicial, cuenta.getMonto()); // El monto no cambia, depósito no realizado
    }

    @Test
    public void testRetirarCantidadNegativa() throws ExcepcionCuentaNoCreada {
        double montoInicial = 1000;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        double cantidadRetiro = -50;
        cuenta.retirar(cantidadRetiro);

        assertEquals(montoInicial, cuenta.getMonto()); // El monto no cambia, retiro no realizado
    }

    @Test
    public void testRetirarConAntiguedadInsuficiente() throws ExcepcionCuentaNoCreada {
        double montoInicial = 1000;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        double cantidadRetiro = 100;
        cuenta.retirar(cantidadRetiro); // Se intenta retirar con antigüedad insuficiente

        assertEquals(montoInicial, cuenta.getMonto()); // El monto no cambia, retiro no realizado
    }

    @Test
    public void testCategoriaVIPDespuesDeMultiplesDepositos() throws ExcepcionCuentaNoCreada {
        double montoInicial = 2400; // Monto justo por debajo del límite para la categoría VIP
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        // Realizar múltiples depósitos que superen el límite para la categoría NORMAL
        cuenta.depositar(150);
        cuenta.depositar(200);
        cuenta.depositar(300);

        assertEquals("VIP", cuenta.getCategoria()); // La cuenta se actualiza a la categoría VIP
    }

    @Test
    public void testIncrementarAntiguedadDespuesDeRetiros() throws ExcepcionCuentaNoCreada {
        double montoInicial = 1000;
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        cuenta.incrementarAntiguedad(); // Incrementar la antigüedad después de crear la cuenta
        cuenta.retirar(100); // Realizar un retiro después de incrementar la antigüedad

        assertEquals(1, cuenta.getAntiguedad()); // La antigüedad se incrementa correctamente después del retiro
    }

    @Test
    public void testCategoriaNORMALDespuesDeMultiplesRetiros() throws ExcepcionCuentaNoCreada {
        double montoInicial = 3000; // Monto suficiente para categoría VIP
        CuentaAhorros cuenta = new CuentaAhorros(montoInicial);

        cuenta.incrementarAntiguedad(); // Incrementar la antigüedad para poder retirar más tarde
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();
        cuenta.incrementarAntiguedad();

        // Realizar múltiples retiros que reduzcan el saldo por debajo del límite para la categoría VIP
        cuenta.retirar(1000);
        cuenta.retirar(500);
        cuenta.retirar(300);
        assertEquals("NORMAL", cuenta.getCategoria()); // La cuenta se mantiene en la categoría NORMAL
    }
}
