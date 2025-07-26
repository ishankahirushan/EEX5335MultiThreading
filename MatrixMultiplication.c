
#include <stdio.h>
#include <pthread.h>

#define SIZE 3

int A[SIZE][SIZE] = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

int B[SIZE][SIZE] = {
    {7, 8, 9},
    {4, 5, 6},
    {1, 2, 3}
};

int result[SIZE][SIZE];

void* compute_row(void* arg) {
    int row = *(int*)arg;

    for (int col = 0; col < SIZE; col++) {
        result[row][col] = 0;
        for (int k = 0; k < SIZE; k++) {
            result[row][col] += A[row][k] * B[k][col];
        }
    }

    pthread_exit(NULL);
}

int main() {
    pthread_t threads[SIZE];
    int row_ids[SIZE];

    for (int i = 0; i < SIZE; i++) {
        row_ids[i] = i;
        pthread_create(&threads[i], NULL, compute_row, &row_ids[i]);
    }

    for (int i = 0; i < SIZE; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("Resultant Matrix:\n");
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            printf("%d\t", result[i][j]);
        }
        printf("\n");
    }

    return 0;
}
