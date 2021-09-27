package com.blue.secure.service.inter;

import com.blue.base.model.base.Access;
import com.blue.secure.api.model.*;
import reactor.core.publisher.Mono;

/**
 * 用户认证授权业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface SecureService {

    /**
     * 更新资源或关联信息
     *
     * @return
     */
    boolean refreshResourceKeyOrRelation();

    /**
     * 更新角色信息
     *
     * @return
     */
    boolean refreshRoleInfo();

    /**
     * 客户端登录
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberAuth> loginByClient(ClientLoginParam clientLoginParam);

    /**
     * 小程序登录
     *
     * @param miniProLoginParam
     * @return
     */
    Mono<MemberAuth> loginByMiniPro(MiniProLoginParam miniProLoginParam);

    /**
     * 微信登录
     *
     * @param wechatProLoginParam
     * @return
     */
    Mono<MemberAuth> loginByWechat(WechatProLoginParam wechatProLoginParam);

    /**
     * 校验auth合法性
     *
     * @param assertAuth
     * @return
     */
    Mono<AuthAsserted> assertAuth(AssertAuth assertAuth);

    /**
     * 生成用户jwt信息
     *
     * @param authGenParam
     * @return
     */
    Mono<MemberAuth> generateAuth(AuthGenParam authGenParam);

    /**
     * 根据access清除认证信息
     *
     * @param access
     * @return
     */
    Mono<Boolean> invalidAuthByAccess(Access access);

    /**
     * 根据jwt清除认证信息
     *
     * @param jwt
     * @return
     */
    Mono<Boolean> invalidAuthByJwt(String jwt);

    /**
     * 根据keyId失效本地缓存
     *
     * @param keyId
     * @return
     */
    Mono<Boolean> invalidLocalAuthByKeyId(String keyId);

    /**
     * 根据access更新成员auth的角色信息
     *
     * @param access
     * @param roleId
     * @return
     */
    void updateMemberRoleByAccess(Access access, Long roleId);

    /**
     * 根据memberId更新成员auth的角色信息
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    void updateMemberRoleById(Long memberId, Long roleId, Long operatorId);

    /**
     * 根据access更新成员auth的密钥信息
     *
     * @param access
     * @return
     */
    Mono<String> updateSecKeyByAccess(Access access);

    /**
     * 为成员分配默认角色
     *
     * @param memberId
     */
    void insertDefaultMemberRoleRelation(Long memberId);

    /**
     * 根据access查询成员的角色及权限信息
     *
     * @param access
     * @return
     */
    Mono<Authority> getAuthorityByAccess(Access access);

    /**
     * 根据memberId查询成员的角色及权限信息
     *
     * @param memberId
     * @return
     */
    Mono<Authority> getAuthorityByMemberId(Long memberId);

}
