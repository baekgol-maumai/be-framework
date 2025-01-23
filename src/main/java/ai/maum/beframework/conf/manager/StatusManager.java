package ai.maum.beframework.conf.manager;

import ai.maum.beframework.vo.meta.Manager;
import ai.maum.beframework.vo.meta.status.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * 상태 관리자
 * @author baekgol@maum.ai
 */
@Getter
@Setter
public class StatusManager implements Manager {
    private Status status;

    protected StatusManager(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "상태: " + status;
    }
}
