package br.jus.trf1.sjba.contavinculada.utils;

public class Reflection {

    public static boolean eligibleClass(Class<?> classOfObject) {

        return (classOfObject.getName().contains("br.jus.trf1.sjba.contavinculada.security.dto.UpdateUserDto")
                || classOfObject.getName().contains("br.jus.trf1.sjba.contavinculada.core.persistence.model"))
                && !classOfObject.isEnum();
    }
}
