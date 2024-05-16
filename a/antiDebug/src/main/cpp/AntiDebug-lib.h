#ifndef _ANTIDEBUG
#define _ANTIDEBUG

#include <stdio.h>
#include <sys/ptrace.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <android/log.h>
#include <sys/syscall.h>
#include <sys/inotify.h>
#include<pthread.h>
#include<sys/prctl.h>
#include<sys/wait.h>
#include <signal.h>
#include <errno.h>
#include <sys/queue.h>
#include <sys/select.h>
#include <stdio.h>
#include <stdlib.h>
#include <exception>

extern void readStatus();

extern void AntiDebug();

extern void CalcTime(int, int);

void safe_attach(pid_t pid);

void handle_events();

void checkDebugger();

void checkAndroidServer();

void runInotify();

void anti_ptrace();

void checkDebug();

#endif
