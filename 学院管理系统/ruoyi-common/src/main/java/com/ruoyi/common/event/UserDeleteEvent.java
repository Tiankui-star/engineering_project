package com.ruoyi.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 用户删除事件
 * 当删除用户时发布此事件，用于通知其他模块进行关联数据清理
 *
 * @author ruoyi
 */
public class UserDeleteEvent extends ApplicationEvent
{
    /**
     * 被删除的用户ID数组
     */
    private final Long[] userIds;

    /**
     * 构造函数
     *
     * @param source 事件源
     * @param userIds 被删除的用户ID数组
     */
    public UserDeleteEvent(Object source, Long[] userIds)
    {
        super(source);
        this.userIds = userIds;
    }

    /**
     * 构造函数（单个用户）
     *
     * @param source 事件源
     * @param userId 被删除的用户ID
     */
    public UserDeleteEvent(Object source, Long userId)
    {
        super(source);
        this.userIds = new Long[] { userId };
    }

    /**
     * 获取被删除的用户ID数组
     *
     * @return 用户ID数组
     */
    public Long[] getUserIds()
    {
        return userIds;
    }
}
