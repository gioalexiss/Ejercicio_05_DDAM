package com.example.eje_garg_06.util;

public class Basicas {
    public static double sumar(double a, double b) {
        return a + b;
    }

    public static double restar(double a, double b) {
        return a - b;
    }

    public static double multiplicar(double a, double b) {
        return a * b;
    }

    public static double dividir(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("División por cero");
        }
        return a / b;
    }

    public static double modulo(double a, double b) {
        return a % b;
    }

    public static double evaluar(String str) {
        // Pre-procesar el string para manejar multiplicaciones implícitas
        // Por ejemplo, convertir (2+2)(5) en (2+2)*(5) o 2(5) en 2*(5)
        str = str.replaceAll("(?<=[0-9)])(?=\\()", "*");
        
        final String finalStr = str;
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < finalStr.length()) ? finalStr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < finalStr.length()) throw new RuntimeException("Inesperado: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x = sumar(x, parseTerm()); // suma
                    else if (eat('-')) x = restar(x, parseTerm()); // resta
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x = multiplicar(x, parseFactor()); // multiplicacion
                    else if (eat('/')) x = dividir(x, parseFactor()); // division
                    else if (eat('%')) x = modulo(x, parseFactor()); // modulo
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // mas unitario
                if (eat('-')) return restar(0, parseFactor()); // menos unitario

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentesis
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Falta parentesis de cierre");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numeros
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(finalStr.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Inesperado: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}