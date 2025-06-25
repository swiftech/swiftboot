package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * 管理员
 *
 * @author swiftech 2020-01-06
 **/
@Schema
public class AdminUserResult extends BasePopulateDto<AdminUserEntity> {

    @Schema(description = "Login name of administrator", example = "admin")
    @JsonProperty("login_name")
    private String loginName;

    @Schema(description = "Login password to login name(MD5 with salt)", example = "a43b66902590c003c213a5ed1b6f92e3")
    @JsonProperty("login_pwd")
    private String loginPwd;

    @Schema(description = "Name of the user", example = "James Bond")
    @JsonProperty("user_name")
    private String userName;

    @Schema(description = "Updating time", example = "1545355038524")
    @JsonProperty("update_time")
    private Long updateTime;

    @Schema(description = "Creation time", example = "1545355038524")
    @JsonProperty("create_time")
    private Long createTime;

    @Schema(description = "Is deleted", example = "false")
    @JsonProperty("is_delete")
    private Boolean isDelete;

    @Schema(description = "Entity ID", example = "basident20191119010450544siobnic")
    @JsonProperty("id")
    private String id;

    /**
     * 获取Login name of administrator
     *
     * @return
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置Login name of administrator
     *
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取Login password to login name
     *
     * @return
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * 设置Login password to login name
     *
     * @param loginPwd
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    /**
     * 获取Name of the user
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置Name of the user
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取Updating time
     *
     * @return
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置Updating time
     *
     * @param updateTime
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取Creation time
     *
     * @return
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置Creation time
     *
     * @param createTime
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取Is deleted
     *
     * @return
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * 设置Is deleted
     *
     * @param isDelete
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取Entity ID
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 设置Entity ID
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

}
