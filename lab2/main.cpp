#include <iostream>
#include <pthread.h>
#include <random>
std::default_random_engine generator;
std::uniform_int_distribution<int> distribution(1,6);

// size of array 
#define MAX 100

// maximum number of threads 
#define MAX_THREAD 10

using namespace std;

int a[MAX];
int sum[4] = { 0 };
int part = 0;
int maxEl = -1000000;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;


void* sum_array(void* arg){
    int thread_part = (*(int*)arg);
    for (int i = thread_part * (MAX / MAX_THREAD); i < (thread_part + 1) * (MAX / MAX_THREAD); i++) {
        sum[thread_part] += a[i];
    }
}

void* findMax(void* arg){
    int thread_part = *(int*)arg;
    printf("\r\nThread num: %d", thread_part);
    for (int i = thread_part * (MAX / MAX_THREAD); i < (thread_part + 1) * (MAX / MAX_THREAD); i++) {
        if (a[i] > maxEl) {
            pthread_mutex_lock(&mutex);
            if (a[i] > maxEl) {
                maxEl = a[i];
            }
            pthread_mutex_unlock(&mutex);
        }
    }
}

int main() {
    for (int i = 0; i < MAX; i++){
        a[i] = 0;
    }

    for (int i = 0; i < MAX; i++){
        a[i] =  i;
        printf("%d ", a[i]);
    }

    pthread_t threadsSum[MAX_THREAD];

    // Creating 4 threads 
    for (int i = 0; i < MAX_THREAD; i++) {
        int* threadNum = (int*) malloc(sizeof(int));
        *threadNum = i;
        pthread_create(&threadsSum[i], NULL, sum_array, threadNum);
    }


    for (int i = 0; i < MAX_THREAD; i++)
        pthread_join(threadsSum[i], NULL);

    int total_sum = 0;
    for (int i = 0; i < MAX_THREAD; i++)
        total_sum += sum[i];

    cout << "\r\nsum is " << total_sum << endl;


    pthread_t threadsFindMax[MAX_THREAD];
    // Creating 4 threads
    for (int i = 0; i < MAX_THREAD; i++) {
        int* threadNum = (int*) malloc(sizeof(int));
        *threadNum = i;
        pthread_create(&threadsFindMax[i], NULL, findMax, threadNum);
    }

    // joining 4 threads i.e. waiting for all 4 threads to complete
    for (int i = 0; i < MAX_THREAD; i++)
        pthread_join(threadsFindMax[i], NULL);

    cout << "\r\nMax el =  " << maxEl << endl;

    return 0;
}