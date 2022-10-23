package org.swiftboot.demo.config;

import java.util.List;
import java.util.Objects;

/**
 * @author swiftech
 */
public class PermissionConfigBean {

    private String code;

    private String desc;

    private List<PermissionConfigBean> permissions;

    public PermissionConfigBean() {
    }

    public PermissionConfigBean(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<PermissionConfigBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionConfigBean> permissions) {
        this.permissions = permissions;
    }

    /**
     * 通过关联关系确认一个权限是否是子权限
     *
     * @param permissionConfigBean
     * @return
     */
    public boolean isSubPermission(PermissionConfigBean permissionConfigBean) {
        if (permissionConfigBean == null || "*".equals(permissionConfigBean.getCode())) {
            return false;
        }
        for (PermissionConfigBean permission : permissions) {
            if (permission.equals(permissionConfigBean)) {
                return true;
            }
            else {
                return isSubPermission(permission);
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionConfigBean that = (PermissionConfigBean) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
