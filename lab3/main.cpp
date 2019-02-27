#include <iostream>
#include <cmath>
#include <random>
#define MAX 1000
#define MAX_THREAD 2

int a[MAX];
int sum[MAX_THREAD] = {0};
int thread_num = -1;
int max = 0;
int summ = 0;

void *summ_array() {

    int sum1 = 0;
    #pragma omp parallel reduction(+:sum1)
    {
    #pragma omp parallel for num_threads(MAX_THREAD)
        for (int i = 0; i < MAX; i++) {
            sum1 += a[i];
        }
    }
    summ = sum1;

}

void *find_max() {
    #pragma omp parallel for reduction(max : max)
    for (int i = 0; i < MAX; i++){
        if (max < a[i]){
            max = a[i];
        }
    }
}

void openmp(int thread_num){
//    omp_set_dynamic(0;
//    omp_set_num_threads(thread_num)
#pragma omp parallel for num_threads(thread_num)
    for (int i = 0; i < thread_num; i++) {
        summ_array();
    }
}

void normal(){
    int total_sum = 0;
    for (int i = 0; i < MAX; i++){
        total_sum+= a[i];
    }
    printf("normal sum array: %d\n", total_sum);
}


int main(int argc, char** argv) {
    int counter = 0;
    int threads_num = MAX_THREAD;

    for (int &i : a) {
        i = 0;
    }

    for (int i = 0; i < MAX; i++){
        a[i] = i;
    }

    {
        unsigned int start_time = static_cast<unsigned int>(clock());
        normal();
        unsigned end_time = clock();
        unsigned search_time = end_time - start_time;
        printf("time working normal: %d\n", search_time);
    }

    {
        unsigned int start_time = clock();
        openmp(threads_num);
        unsigned end_time = clock();
        unsigned search_time = end_time - start_time;
        printf("time working openmp: %d\n", search_time);
    }

    find_max();

    printf("sum array: %d\n", summ);
    printf("max el array: %d\n", max);

    return 0;
}