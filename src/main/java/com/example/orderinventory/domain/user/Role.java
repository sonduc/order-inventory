package com.example.orderinventory.domain.user;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(
            Permission.PRODUCT_READ,
            Permission.PRODUCT_WRITE,
            Permission.ORDER_READ,
            Permission.ORDER_WRITE,
            Permission.PAYMENT_PROCESS,
            Permission.WEBHOOK_MANAGE,
            Permission.CONSOLE_USE
    )),
    CUSTOMER(Set.of(
            Permission.PRODUCT_READ,
            Permission.ORDER_READ,
            Permission.ORDER_WRITE,
            Permission.PAYMENT_PROCESS
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
