package br.com.navis.transportadora.config.hibernate;

public class TenantContext {

    private static final String DEFAULT_TENANT = "transportadora_1"; // Resolver o problema aqui, que não permite a mudança de contexto do tenant ao fazer o login
    private static final ThreadLocal<String> currentTenant = ThreadLocal.withInitial(() -> DEFAULT_TENANT);

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }

}
