#include <iostream>
#include <pthread.h>
#include <random>
std::default_random_engine generator;
std::uniform_int_distribution<int> distribution(1,6);

// size of array 
#define MAX 12

// maximum number of threads 
#define MAX_THREAD 4

using namespace std;

int a[MAX];// = { 1, 5, 7, 10, 12, 14, 15, 18, 20, 22, 25, 27, 30, 64, 110, 220 };
int sum[4] = { 0 };
int part = 0;
int maxEl = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;


void* sum_array(void* arg){
    int thread_part = (*(int*)arg);

    for (int i = thread_part * (MAX / 4); i < (thread_part + 1) * (MAX / 4); i++) {
        sum[thread_part] += a[i];
    }
}

void* findMax(void* arg){
    int thread_part = (*(int*)arg);
    for (int i = thread_part * (MAX / 4); i < (thread_part + 1) * (MAX / 4); i++) {
        pthread_mutex_lock(&mutex);
        if (a[i] > maxEl){
            maxEl = a[i];
        }
        pthread_mutex_unlock(&mutex);
    }
}

void* printNumThread(void* threadNum) {
//    string tn = std::to_string(threadNum);
    printf("\r\nThread num: %d", (*(int*)threadNum));
}

int main()
{
    for (int i = 0; i < MAX; i++){
        a[i] = 0;
    }

    for (int i = 0; i < MAX; i++){
        a[i] = distribution(generator);
        printf("%d ", a[i]);
    }

    pthread_t threadsPrint[MAX_THREAD];

    // Creating 4 threads
    for (int i = 0; i < MAX_THREAD; i++) {
        int* threadNum = (int*) malloc(sizeof(int));
        *threadNum = i;
        pthread_create(&threadsPrint[i], NULL, printNumThread, threadNum);
    }


    for (int i = 0; i < MAX_THREAD; i++)
        pthread_join(threadsPrint[i], NULL);

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