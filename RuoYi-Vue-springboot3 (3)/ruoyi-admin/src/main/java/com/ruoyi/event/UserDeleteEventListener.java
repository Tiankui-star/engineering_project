package com.ruoyi.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.ruoyi.Faculty.mapper.FacultyMapper;
import com.ruoyi.Stu.mapper.StudentMapper;
import com.ruoyi.common.event.UserDeleteEvent;

/**
 * 用户删除事件监听器
 * 监听用户删除事件，同步删除关联的学生和教师记录
 *
 * @author ruoyi
 */
@Component
public class UserDeleteEventListener
{
    private static final Logger log = LoggerFactory.getLogger(UserDeleteEventListener.class);

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private FacultyMapper facultyMapper;

    /**
     * 处理用户删除事件
     * 删除关联的学生和教师记录
     *
     * @param event 用户删除事件
     */
    @EventListener
    public void handleUserDeleteEvent(UserDeleteEvent event)
    {
        Long[] userIds = event.getUserIds();
        if (userIds == null || userIds.length == 0)
        {
            return;
        }

        log.info("处理用户删除事件，删除关联的学生和教师记录，用户ID数量：{}", userIds.length);

        try
        {
            // 删除学生记录（包括获奖记录）
            studentMapper.deleteStudentAwardByStudentIds(userIds);
            studentMapper.deleteStudentByStudentIds(userIds);

            // 删除教师记录
            facultyMapper.deleteFacultyByFacultyIds(userIds);

            log.info("用户删除事件处理完成");
        }
        catch (Exception e)
        {
            log.error("处理用户删除事件失败", e);
        }
    }
}
