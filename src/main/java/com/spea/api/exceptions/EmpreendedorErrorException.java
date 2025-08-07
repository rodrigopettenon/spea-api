package com.spea.api.exceptions;

public class EmpreendedorErrorException extends RuntimeException {

    private static final long serialVersionUID = 6289540138837603846L;

    private Object object;

    public EmpreendedorErrorException(String mensagem) {
        super(mensagem);
    }

    public EmpreendedorErrorException(String msg, Throwable causa) {
        super(msg, causa);
    }

    public EmpreendedorErrorException(String msg, Object object){
        super(msg);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
