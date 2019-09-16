package com.sasitha.springavrowebflux.context;

public class TenantContext {
    private static ThreadLocal<String> tenantId = new ThreadLocal<>();

    public static void setTenant(String id){
        tenantId.set(id);
    }

    public static void removeTenant(){
        tenantId.remove();
    }

    public static String getTenantId(){
        return tenantId.get();
    }
}
