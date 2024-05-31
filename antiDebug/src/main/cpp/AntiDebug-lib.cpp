#include <string.h>
#include <jni.h>
#include "AntiDebug-lib.h"
//#include "mlog.h"
#define CHECK_TIME 1
#define MAX 128
#define WCHAN_ELSE 0;
#define WCHAN_RUNNING 1;
#define WCHAN_TRACING 2;

int keep_running;
pthread_t t_id;

//Anti Debug 试参考资料： https://blog.csdn.net/darmao/article/details/78816964
int getNumberForStr(char *str) {
    if (str == NULL) {
        return -1;
    }
    char result[20];
    int count = 0;
    while (*str != '\0') {
        if (*str >= 48 && *str <= 57) {
            result[count] = *str;
            count++;
        }
        str++;
    }
    int val = atoi(result);
    return val;
}

/**
 *
 * @return
 */
int getWchanStatus() {
    char *wchaninfo = (char *) malloc(128);
    int result = WCHAN_ELSE;
    char *cmd = (char *) malloc(128);
    pid_t pid = syscall(__NR_getpid);
    sprintf(cmd, "cat /proc/%d/wchan", pid);
    if (cmd == NULL) {
        return WCHAN_ELSE;
    }
    FILE *ptr;
    if ((ptr = popen(cmd, "r")) != NULL) {

        if (ptr == NULL) {
            return result;
        }

        if (fgets(wchaninfo, 128, ptr) != NULL) {
//            LOGI("wchaninfo= %s", wchaninfo);
        }
    }
    if (strncasecmp(wchaninfo, "sys_epoll\0", strlen("sys_epoll\0")) == 0) {
        result = WCHAN_RUNNING;
    } else if (strncasecmp(wchaninfo, "ptrace_stop\0", strlen("ptrace_stop\0")) == 0) {
        result = WCHAN_TRACING;
    }
    return result;
}


/**
 * 调试进程名检测
 *
 */
void searchObjProcess() {
    FILE *pfile = NULL;
    char buf[0x1000] = {0};

    // 执行命令
    //pfile=popen("ps | awk '{print $9}'","r"); // 部分不支持awk命令
    pfile = popen("ps", "r");
    if (NULL == pfile) {
//        LOGE("SearchObjProcess popen打开命令失败!\n");
        return;
    }
    // 获取结果
    while (fgets(buf, sizeof(buf), pfile)) {
        // 打印进程
//        LOGE("遍历进程:%s\n",buf);
        // 查找子串
        char *strA = strstr(buf, "android_server");
        char *strB = strstr(buf, "gdbserver");
        char *strC = strstr(buf, "gdb");
        char *strD = strstr(buf, "fuwu");
        if (strA || strB || strC || strD) {
//          LOGE("SearchObjProcess 检测到调试器!\n");
            // 执行到这里，判定为调试状态
            kill(0, SIGKILL);
        }//if
    }//while
    pclose(pfile);
}


void readStatus() {
    FILE *fd;
    char filename[MAX];
    char line[MAX];
    pid_t pid = syscall(__NR_getpid);
    int ret = getWchanStatus();
    int i = 0, traceid = 0;
//    LOGE("ret:%d", ret);
    if (2 == ret) {
//        kill(pid, SIGKILL);
        kill(0, SIGKILL);
    }


    sprintf(filename, "/proc/%d/status", pid);    // proc/pid/status TracerPid
    int iRes = fork();
    if (iRes == 0) {
        int pt;
        //为了不让被其他进程调试，提前ptrace 自己
        pt = ptrace(PTRACE_TRACEME, 0, 0, 0);
        if (pt == -1)
            exit(0);
        while (1) {
            searchObjProcess();
            i = 0;
            fd = fopen(filename, "r");
            if (fd == NULL) {
                break;
            }

            while (!feof(fd)) {
                fgets(line, 128, fd);
                if (i == 5) {
                    traceid = getNumberForStr(line);
//                LOGE("traceid:%d", traceid);
                    if (traceid > 0) {
//                  LOGE("I was be traced...trace pid:%d", traceid);
                        kill(0, SIGKILL);
                        return;
                    }
                    break;
                }
                i++;
            }
            fclose(fd);
            sleep(CHECK_TIME);
        }
    } else {
        //断点 iRes 值
        int pt = 2;
    }
}


/**
 * 防止被别人 ptrace
 *
 * @param args
 * @return
 */
void *anti_ptrace(void *args) {
    try {
        readStatus();
    } catch (std::exception &e) {
//		LOGE("Exception occur......");
    }
    return NULL;
}


/**
 * 多进程ptrace反调试
 *
 */
void antiPtrace(void) {
    pid_t child;

    // 创建子进程
    child = fork();
    if (child != 0) {
        // 返回在父进程中
//		wait(NULL);
        ptrace(PTRACE_TRACEME, getppid(), 0, 0);
    }
}


/**
 * 对外暴露的JNI 接口，AntiDebug
 * 加一个时间限制
 *
 */
JNICALL
extern "C"
void Java_com_anylife_antidebug_AntiDebugInterface_checkDebug2(JNIEnv *env, jclass clazz) {
    //添加一个时间的限制



    time_t tmpcal_ptr;
    time(&tmpcal_ptr);
    printf("tmpcal_ptr=%d\n", tmpcal_ptr);



    if (pthread_create(&t_id, NULL, anti_ptrace, NULL) != 0) {

        kill(0, SIGKILL);
    }
}


/**
 * 这里验证Fork 进程，子进程的问题
 *
 */
JNICALL
extern "C"
void Java_com_anylife_antidebug_AntiDebugInterface_checkDebug(JNIEnv *env, jclass clazz) {
    pid_t pid;
    pid = fork();
    if (pid == 0) {
        //返回子进程
        printf("child pid: %d\n", getpid());
    } else {
        printf("pid: %d\n"
               "", pid);//父进程中返回子进程的pid
        pid_t fatherPid = getpid();
        printf("father pid: %d\n", fatherPid);
    }
}