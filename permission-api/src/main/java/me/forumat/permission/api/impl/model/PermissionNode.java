package me.forumat.permission.api.impl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.forumat.permission.api.shared.model.IPermissionNode;

@Data
@Getter
@AllArgsConstructor
public class PermissionNode implements IPermissionNode {

    private final String permissionString;
    private final boolean negated;

}
