//
// Created by anylife on 2024/5/28.
//

#ifndef ANDROID_ARCHITECTURE_TIME_H
#define ANDROID_ARCHITECTURE_TIME_H


#include <time.h>
#include <stdio.h>


int main(int argc, char **argv)
{
    time_t tmpcal_ptr;
    struct tm *tmp_ptr = NULL;

    time(&tmpcal_ptr);
    //tmpcal_ptr = time(NULL);   两种取值方法均可以
    printf("tmpcal_ptr=%d\n", tmpcal_ptr);

    tmp_ptr = gmtime(&tmpcal_ptr);
    printf("after gmtime, the time is:%d:%d:%d\n", tmp_ptr->tm_hour, tmp_ptr->tm_min, tmp_ptr->tm_sec);

    tmp_ptr = localtime(&tmpcal_ptr);
    printf ("after localtime, the time is:%d.%d.%d ", (1900+tmp_ptr->tm_year), (1+tmp_ptr->tm_mon), tmp_ptr->tm_mday);
    printf("%d:%d:%d\n", tmp_ptr->tm_hour, tmp_ptr->tm_min, tmp_ptr->tm_sec);

    return 0;
}

#endif //ANDROID_ARCHITECTURE_TIME_H
