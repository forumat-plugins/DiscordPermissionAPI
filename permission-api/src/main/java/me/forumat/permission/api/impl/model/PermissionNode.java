package me.forumat.permission.api.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class PermissionNode {

    private final String permissionString;
    private final int power;

}
